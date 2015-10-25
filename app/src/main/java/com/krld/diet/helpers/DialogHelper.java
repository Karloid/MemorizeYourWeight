package com.krld.diet.helpers;

import android.app.AlertDialog;
import android.content.Context;

import com.krld.diet.R;

import rx.functions.Action0;

public class DialogHelper {
    public static void showError(String message, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.label_error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showAlert(Context context, String message, Action0 action0) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.label_alert)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    action0.call();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showInfo(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.label_info)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
