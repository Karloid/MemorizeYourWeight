package com.krld.diet.common.models;

import com.krld.diet.R;

public enum MealEnumeration {
    BREAKFAST(R.string.breakfast, 0.25f),
    TIFFIN(R.string.tiffin, 0.15f),
    LUNCH(R.string.lunch, 0.35f),
    AFTERNOON_SNACK(R.string.afternoon_snack, 0.10f),
    DINNER(R.string.dinner, 0.15f)
    ,;
    public final int nameLocResId;
    public final float caloriesPercent;

    MealEnumeration(int nameLocResId, float caloriesPercent) {
        this.nameLocResId = nameLocResId;
        this.caloriesPercent = caloriesPercent;
    }
}
