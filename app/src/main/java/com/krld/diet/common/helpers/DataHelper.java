package com.krld.diet.common.helpers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.krld.diet.Application;
import com.krld.diet.common.models.MealEnumeration;
import com.krld.diet.common.models.MealModel;
import com.krld.diet.common.models.MealSummary;
import com.krld.diet.common.models.Product;
import com.krld.diet.common.models.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.subjects.PublishSubject;

import static rx.android.schedulers.AndroidSchedulers.*;


public class DataHelper {
    private static DataHelper instance;
    private final RxSharedPreferences rxPrefs;
    private final PublishSubject<Product> deletedProductsObs;
    private String profileKey = "PROFILE_" + 1;
    private Map<MealEnumeration, Subscription> subscriptionMap;

    public synchronized static DataHelper getInstance() {
        if (instance == null) {
            instance = new DataHelper();
        }
        return instance;
    }

    private DataHelper() {
        rxPrefs = Application.getInstance().getRxPrefs();
        deletedProductsObs = PublishSubject.create();


        Observable.from(MealEnumeration.values())
                .flatMap(this::getMealObs)
                .observeOn(mainThread())
                .subscribe(mealModel -> {
                    Observable<Integer> productIds = Observable.<Integer>from(mealModel.products).cache();

                    Func0<Observable<Product>> productsObs = () -> productIds
                            .flatMap(id -> DataHelper.this.getProductObs(id, mealModel.mealEnumeration));

                    Func0<Observable<Product>> productsObsTake1 = () -> productIds
                            .flatMap(id -> DataHelper.this.getProductObs(id, mealModel.mealEnumeration).take(1));

                    Subscription subscription = getSubscription(mealModel.mealEnumeration);
                    if (subscription != null) {
                        subscription.unsubscribe();
                    }
                    subscription = productsObs.call()
                            .debounce(50, TimeUnit.MILLISECONDS)
                            .observeOn(mainThread())
                            .subscribe(p -> {
                                MealSummary summary = DataHelper.this.getMealSummaryObs(mealModel.mealEnumeration).toBlocking().first();
                                summary.clear();

                                productsObsTake1.call()
                                        .reduce(summary, (mealSummary, product) -> {
                                            mealSummary.append(product);
                                            return mealSummary;
                                        })
                                        .observeOn(mainThread())
                                        .subscribe((s) -> {
                                            FLog.d(this, "save Summary: " + s.mealEnumeration);
                                            DataHelper.this.saveSummary(s);
                                        });

                            });
                    subscriptionMap.put(mealModel.mealEnumeration, subscription);
                });
    }

    private Subscription getSubscription(MealEnumeration mealEnumeration) {
        if (subscriptionMap == null) {
            subscriptionMap = new HashMap<>();
        }
        return subscriptionMap.get(mealEnumeration);
    }

    private void saveSummary(MealSummary s) {
        getMealSummaryPref(s.mealEnumeration).asAction().call(convertToJson(s));
    }

    public synchronized Observable<Profile> getProfile() {
        return getProfilePref().asObservable()
                .map(s -> TextUtils.isEmpty(s) ? Profile.create() : convertFromJson(s, Profile.class));
    }

    @NonNull
    private Preference<String> getProfilePref() {
        return rxPrefs.getString(profileKey);
    }

    private <T> T convertFromJson(String s, Class<T> klass) {
        return new Gson().fromJson(s, klass);
    }

    public void saveProfile(Profile profile) {
        profile.calcBMI();
        getProfilePref().asAction().call(convertToJson(profile));
    }

    private String convertToJson(Object o) {
        return new Gson().toJson(o);
    }

    public Observable<MealSummary> getMealSummaryObs(MealEnumeration mealEnumeration) {
        return getMealSummaryPref(mealEnumeration).asObservable().map(s -> TextUtils.isEmpty(s) ? MealSummary.create(mealEnumeration) : convertFromJson(s, MealSummary.class));
    }

    public Observable<MealModel> getMealObs(MealEnumeration mealEnumeration) {
        return getMealPref(mealEnumeration)
                .asObservable()
                .map(s -> TextUtils.isEmpty(s) ? MealModel.create(mealEnumeration) : convertFromJson(s, MealModel.class))
                .map(m -> {
                    if (m.products == null) {
                        m.products = new ArrayList<Integer>();
                    }
                    return m;
                });
    }

    @NonNull
    private Preference<String> getMealPref(MealEnumeration mealEnumeration) {
        return rxPrefs.getString(profileKey + mealEnumeration);
    }

    @NonNull
    private Preference<String> getMealSummaryPref(MealEnumeration mealEnumeration) {
        return rxPrefs.getString(profileKey + mealEnumeration + "_summary_");
    }

    public void addNewProduct(MealEnumeration mealEnumeration) {
        getMealObs(mealEnumeration).take(1).subscribe(mealModel -> {
            Product product = Product.create(mealEnumeration);
            product.id = mealModel.productId++;
            mealModel.products.add(product.id);
            saveProduct(product);
            saveMeal(mealModel);
        });
    }

    private void saveMeal(MealModel mealModel) {
        getMealPref(mealModel.mealEnumeration).asAction().call(convertToJson(mealModel));
    }

    public void saveProduct(Product product) {
        product.calcCalories();
        getProductPref(product.id, product.mealEnumeration).asAction().call(convertToJson(product));
    }

    private Preference<String> getProductPref(int id, MealEnumeration mealEnumeration) {
        return rxPrefs.getString(profileKey + mealEnumeration + id);
    }

    public Observable<Product> getProductObs(int id, MealEnumeration mealEnumeration) {
        return getProductPref(id, mealEnumeration).asObservable().map(s -> convertFromJson(s, Product.class));
    }

    public void deleteProduct(MealEnumeration mealEnumeration, Product product) {
        getMealObs(mealEnumeration).take(1).subscribe(mealModel -> {
            int index = mealModel.products.indexOf(product.id);
            if (index != -1)
                mealModel.products.remove(index);
            saveMeal(mealModel);
            deletedProductsObs.onNext(product);
        });
    }

    public Observable<Product> getDeletedProductsObs() {
        return deletedProductsObs.asObservable();
    }
}
