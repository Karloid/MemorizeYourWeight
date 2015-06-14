package com.krld.memorize.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatterHelper {

    private static NumberFormat formatter = new DecimalFormat("#0.00");

    public static String formatDouble(double value) {
        return formatter.format(value);
    }
}
