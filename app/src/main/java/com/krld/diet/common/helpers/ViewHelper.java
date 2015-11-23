package com.krld.diet.common.helpers;

import android.widget.TextView;

import static com.krld.diet.memorize.common.FormatterHelper.formatAmount;

public class ViewHelper {
    public static void setAmount(float source, TextView view) {
        setAmount(source, view, 1);
    }

    public static void setAmount(float source, TextView view, int decimalPlaces) {
        String newValue = formatAmount(source, decimalPlaces);
        setString(newValue, view);
    }

    public static void setString(String newString, TextView view) {
        if (!view.getText().toString().equals(newString))
            view.setText(newString);
    }
}
