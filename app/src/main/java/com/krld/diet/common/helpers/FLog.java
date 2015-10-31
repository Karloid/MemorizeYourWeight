package com.krld.diet.common.helpers;

import android.util.Log;


import com.krld.diet.BuildConfig;

import rx.functions.Action2;

public class FLog {
    public static final String LOG_TAG = "FUZD_LOG_TAG";
    private static Action2<String, String> d = Log::d;
    private static Action2<String, String> e = Log::e;
    private static Action2<String, String> i = Log::i;
    private static Action2<String, String> w = Log::w;

    private static void output(Action2<String, String> action, String string) {
        if (BuildConfig.DEBUG_LOG_ENABLED /*|| BuildConfig.CRASHLYTICS_ENABLED*/) {
            if (string == null)
                string = "";
            int maxLogSize = 1000;
            for (int i = 0; i <= string.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > string.length() ? string.length() : end;
                String result = string.substring(start, end);

                if (BuildConfig.DEBUG_LOG_ENABLED) {
                    action.call(LOG_TAG, result);
                }
         /*       if (BuildConfig.CRASHLYTICS_ENABLED) {
                    Crashlytics.log(result); //TODO specify log level
                }*/
            }
        }
    }

    public static void d(String string) {
        output(d, string);
    }

    public static void e(String string) {
        output(e, string);
    }

    public static void w(String string) {
        output(w, string);
    }

    public static void i(String string) {
        output(i, string);
    }
}
