package com.krld.memorize.sugar;

import com.orm.SugarRecord;

public class TestMeasure extends SugarRecord<TestMeasure> {
    private double value;

    public TestMeasure() {
    }

    public TestMeasure(double value) {
        this.value = value;
    }
}
