package com.krld.diet.common.helpers;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.krld.diet.base.Constants;
import com.krld.diet.common.models.MealEnumeration;
import com.krld.diet.meals.activities.MealActivity;

public class IntentHelper {
    public static void showMeal(Context context, MealEnumeration mealEnumeration) {
        Intent intent = new Intent(context, MealActivity.class);
        intent.putExtra(Constants.EXTRA_MEAL, new Gson().toJson(mealEnumeration));
        context.startActivity(intent);
    }
}
