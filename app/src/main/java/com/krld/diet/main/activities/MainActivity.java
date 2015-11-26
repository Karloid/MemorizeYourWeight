package com.krld.diet.main.activities;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import com.krld.diet.Application;
import com.krld.diet.base.activities.BaseNavDrawerActivity;
import com.krld.diet.meals.fragments.MealsListFragment;
import com.krld.diet.profile.fragments.ProfileFragment;

public class MainActivity extends BaseNavDrawerActivity {

    public static final String FIRST_RUN = "FIRST_RUN";

    @Override
    protected Fragment createFragment() {
        SharedPreferences sharedPrefs = Application.getInstance().getSharedPrefs();
        boolean isFirstRun = sharedPrefs.getBoolean(FIRST_RUN, true);
        if (isFirstRun) {
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.putBoolean(FIRST_RUN, false);
            edit.commit();
            return ProfileFragment.newInstance();
        }
        return MealsListFragment.newInstance();
    }
}
