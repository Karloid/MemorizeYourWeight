package com.krld.diet.memorize.common;

import com.krld.diet.R;

public enum DataType {
    WAIST(R.string.waist), HIPS(R.string.hips), WEIGHT(R.string.weight);

    public final int stringId;

    DataType(int stringId) {
        this.stringId = stringId;
    }
}
