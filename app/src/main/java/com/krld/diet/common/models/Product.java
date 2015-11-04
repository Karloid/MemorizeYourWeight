package com.krld.diet.common.models;

import com.krld.diet.Application;
import com.krld.diet.R;

public class Product {
    public String name;

    public static Product create() {
        Product product = new Product();
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
