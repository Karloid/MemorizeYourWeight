package com.krld.diet.common.models;

import com.krld.diet.R;

public enum Meal {
    BREAKFAST(R.string.breakfast),
    TIFFIN(R.string.tiffin),
    DINNER(R.string.dinner),
    AFTERNOON_SNACK(R.string.afternoon_snack),
    LUNCH(R.string.lunch),;
    public int nameLocResId;

    Meal(int nameLocResId) {
        this.nameLocResId = nameLocResId;
    }


}
