package com.krld.diet.memorize.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatterHelper {

    private static NumberFormat formatter = new DecimalFormat("#0.00");

    public static String formatDouble(double value) {
        return formatter.format(value);
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy H:mm").format(date);
    }

    public static String formatAmount(float value, int decimalPlaces) {
        String format = "%." +decimalPlaces+"f";
        return String.format(Locale.US, format, value);
    }
}
