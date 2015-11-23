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

import rx.Observable;
import rx.subjects.PublishSubject;


public class DataHelper {
    private static DataHelper instance;
    private final RxSharedPreferences rxPrefs;
    private final PublishSubject<Product> deletedProductsObs;
    private String profileKey = "PROFILE_" + 1;

    public synchronized static DataHelper getInstance() {
        if (instance == null) {
            instance = new DataHelper();
        }
        return instance;
    }

    private DataHelper() {
        rxPrefs = Application.getInstance().getRxPrefs();
        deletedProductsObs = PublishSubject.create();
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
                .map(s -> TextUtils.isEmpty(s) ? MealModel.create(mealEnumeration) : convertFromJson(s, MealModel.class));
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
