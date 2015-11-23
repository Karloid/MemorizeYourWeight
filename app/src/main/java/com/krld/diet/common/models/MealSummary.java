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
}
