package com.krld.diet.common.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.krld.diet.Application;

public class MetricsHelper {
    private static DisplayMetrics displaymetrics;

    public static float getLargestWidthDp() {
        return Math.max(getHeight(), getWidth()) / getDisplayMetrics().density;
    }

    public static float getSmallestWidthDp() {
        return Math.min(getHeight(), getWidth()) / getDisplayMetrics().density;
    }

    public static int getHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static int getWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static float getHeightDp() {
        return getDisplayMetrics().heightPixels / getDisplayMetrics().density;
    }

    public static float getWidthDp() {
        return getDisplayMetrics().widthPixels / getDisplayMetrics().density;
    }

    @NonNull
    private static DisplayMetrics getDisplayMetrics() {
        if (displaymetrics == null) {
            displaymetrics = new DisplayMetrics();
            ((WindowManager) Application.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        }
        return displaymetrics;
    }
}
