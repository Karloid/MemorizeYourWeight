package com.krld.diet.common.models;

public class MealSummary {

    public MealEnumeration mealEnumeration;


    public float proteins;
    public float fats;
    public float carbs;
    public float weight;
    public float calories;

    public static MealSummary create(MealEnumeration mealEnumeration) {
        MealSummary summary = new MealSummary();
        summary.mealEnumeration = mealEnumeration;
        return summary;
    }

    public void clear() {
        proteins = 0;
        fats = 0;
        carbs = 0;
        weight = 0;
        calories = 0;
    }

    public void append(Product product) {
        float koeff = product.weight / 100f;
        proteins += product.proteins * koeff;
        fats += product.fats * koeff;
        carbs += product.carbs * koeff;
        weight += product.weight;
        calories += product.calories;
    }
}
