package com.krld.diet.common.helpers;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.krld.diet.Application;
import com.krld.diet.R;
import com.krld.diet.common.models.DataStore;
import com.krld.diet.common.models.Profile;

public class DataHelper {
    public static final String KEY_DATA_STORE = "data_store";
    private static DataHelper instance;
    private final SharedPreferences sharedPrefs;

    private DataHelper() {
        sharedPrefs = Application.getInstance().getSharedPrefs();
    }

    public synchronized static DataHelper getInstance() {
        if (instance == null) {
            instance = new DataHelper();
        }
        return instance;
    }

    public synchronized Profile getProfile() {
        Profile profile = getDataStore().profile;
        if (profile == null) {
            DataStore dataStore = getDataStore();
            dataStore.profile = Profile.create();
            save(dataStore);
            return dataStore.profile;
        }
        return profile;
    }

    private synchronized DataStore getDataStore() {
        String dataStoreString = sharedPrefs.getString(KEY_DATA_STORE, "");
        DataStore dataStore;
        if (TextUtils.isEmpty(dataStoreString)) {
            dataStore = new DataStore();
            dataStore.profile = Profile.create();
            save(dataStore);
        } else {
            dataStore = new Gson().fromJson(dataStoreString, DataStore.class);
        }
        return dataStore;
    }

    @SuppressLint("CommitPrefEdits")
    private synchronized void save(DataStore dataStore) {
        sharedPrefs.edit().putString(KEY_DATA_STORE, new Gson().toJson(dataStore)).commit();
    }

    public void save(Profile profile) {
        DataStore ds = getDataStore();
        ds.profile = profile;
        save(ds);
    }
}
