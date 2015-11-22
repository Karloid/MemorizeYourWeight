package com.krld.diet.common.models;

import java.util.ArrayList;
import java.util.List;

public class MealModel {
    public List<Integer> products;
    public int productId;
    public MealEnumeration mealEnumeration;

    public static MealModel create(MealEnumeration mealEnumeration) {
        MealModel mealModel = new MealModel();
        mealModel.mealEnumeration = mealEnumeration;
        mealModel.products = new ArrayList<>();
        return mealModel;
    }
}
