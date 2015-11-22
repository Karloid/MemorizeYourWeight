package com.krld.diet.common.models;

import com.krld.diet.Application;
import com.krld.diet.R;

public class Product {
    public String name;
    public int id;
    public MealEnumeration mealEnumeration;

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
}
