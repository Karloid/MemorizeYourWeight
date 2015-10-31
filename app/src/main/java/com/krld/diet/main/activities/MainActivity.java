package com.krld.diet.main.activities;

import android.support.v4.app.Fragment;

import com.krld.diet.base.activities.BaseNavDrawerActivity;
import com.krld.diet.profile.fragments.ProfileFragment;

public class MainActivity extends BaseNavDrawerActivity {
    @Override
    protected Fragment createFragment() {
        return ProfileFragment.newInstance();
    }
}
