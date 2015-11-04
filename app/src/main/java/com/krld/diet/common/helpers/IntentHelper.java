package com.krld.diet.common.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.krld.diet.base.Constants;
import com.krld.diet.base.fragments.BaseFragment;
import com.krld.diet.common.models.Meal;
import com.krld.diet.meals.activities.MealActivity;

public class IntentHelper {
    public static void showMeal(Context context, Meal meal) {
        Intent intent = new Intent(context, MealActivity.class);
        intent.putExtra(Constants.EXTRA_MEAL, new Gson().toJson(meal));
        context.startActivity(intent);
    }
}
