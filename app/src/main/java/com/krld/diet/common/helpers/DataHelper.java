package com.krld.diet.common.helpers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.krld.diet.Application;
import com.krld.diet.common.models.MealEnumeration;
import com.krld.diet.common.models.MealModel;
import com.krld.diet.common.models.Product;
import com.krld.diet.common.models.Profile;

import rx.Observable;


public class DataHelper {
    private static DataHelper instance;
    private final RxSharedPreferences rxPrefs;
    private String profileKey = "PROFILE_" + 1;

    public synchronized static DataHelper getInstance() {
        if (instance == null) {
            instance = new DataHelper();
        }
        return instance;
    }

    private DataHelper() {
        rxPrefs = Application.getInstance().getRxPrefs();
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

    public void save(Profile profile) {
        profile.calcBMI();
        getProfilePref().asAction().call(convertToJson(profile));
    }

    private String convertToJson(Object o) {
        return new Gson().toJson(o);
    }

    public Product createNewProduct() {
        //TODO save to datastore
        Product product = Product.create();

        return product;
    }

    public Object getMealSummary() {
        return null; //TODO
    }

    public Observable<MealModel> getMeal(MealEnumeration mealEnumeration) {
        return rxPrefs.getString(profileKey + mealEnumeration.name())
                .asObservable()
                .map(s -> TextUtils.isEmpty(s) ? MealModel.create() : convertFromJson(s, MealModel.class));
    }
}
