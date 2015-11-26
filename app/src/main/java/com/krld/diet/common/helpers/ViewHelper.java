package com.krld.diet.common.helpers;

import android.widget.TextView;

import com.krld.diet.Application;
import com.krld.diet.R;

import static com.krld.diet.memorize.common.FormatterHelper.formatAmount;

public class ViewHelper {
    public static void setAmount(float source, TextView view) {
        setAmount(source, view, 1);
    }

    public static void setAmount(float source, TextView view, int decimalPlaces) {
        String newValue = formatAmount(source, decimalPlaces);
        setString(newValue, view);
    }

    public static void setAmountWithTotal(float current, float total, TextView view, int decimalPlaces) {
        String newValue = formatAmount(current, decimalPlaces) + "/" + formatAmount(total, decimalPlaces);
        float koeff = current / total;
        int textColor;
        if (koeff < 0.7) {
            textColor = R.color.red_1;
        } else if (koeff < 0.9) {
            textColor = R.color.text_yellow;
        } else if (koeff < 1.1) {
            textColor = R.color.green_1;
        } else if (koeff < 1.3) {
            textColor = R.color.text_yellow;
        } else {
            textColor = R.color.red_1;
        }
        view.setTextColor(Application.getInstance().getResources().getColor(textColor));
        setString(newValue, view);
    }

    public static void setString(String newString, TextView view) {
        if (!view.getText().toString().equals(newString))
            view.setText(newString);
    }
}
