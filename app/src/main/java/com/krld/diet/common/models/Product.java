package com.krld.diet.common.models;

import com.krld.diet.Application;
import com.krld.diet.R;

public class Product {
    public static final float CALORIES_PROTEINS = 4.1f;
    public static final float CALORIES_FATS = 9.1f;
    public static final float CALORIES_CARBS = 4.1f;
    public String name;
    public int id;
    public MealEnumeration mealEnumeration;


    public float proteins;
    public float fats;
    public float carbs;
    public float weight;
    public float calories;

    public static Product create(MealEnumeration mealEnumeration) {
        Product product = new Product();
        product.mealEnumeration = mealEnumeration;
        product.init();
        return product;
    }

    private boolean init() {
        boolean isChanged = false;

        if (name == null) {
            name = Application.getInstance().getResources().getString(R.string.new_product_default_name);
            isChanged = true;
        }

        return isChanged;
    }

    public void calcCalories() {
        //TODO
        calories = proteins * CALORIES_PROTEINS + fats * CALORIES_FATS + carbs * CALORIES_CARBS;
        calories = calories * (weight / 100f);
    }
}
