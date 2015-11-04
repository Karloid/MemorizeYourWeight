package com.krld.diet.meals.activities;

import android.support.v4.app.Fragment;

import com.krld.diet.base.activities.BaseNavDrawerActivity;
import com.krld.diet.meals.fragments.MealFragment;

public class MealActivity extends BaseNavDrawerActivity{
    @Override
    protected Fragment createFragment() {
        return MealFragment.newInstance();
    }
}
