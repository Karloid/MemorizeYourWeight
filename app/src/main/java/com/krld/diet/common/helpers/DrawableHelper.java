package com.krld.diet.common.helpers;


import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.krld.diet.Application;

public class DrawableHelper {
    public static Drawable getTintedDrawable(@DrawableRes int drawableResId, @ColorRes int colorResId) {
        Resources res = Application.getInstance().getResources();
        Drawable drawable = res.getDrawable(drawableResId);
        int color = res.getColor(colorResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}