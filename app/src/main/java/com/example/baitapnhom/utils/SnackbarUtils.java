package com.example.baitapnhom.utils;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    public static void show(View rootView, String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showError(View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(0xFFD32F2F);
        snackbar.show();
    }

    public static void showSuccess(View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(0xFF2E7D32);
        snackbar.show();
    }


    public static void show(Activity activity, String message) {
        View root = activity.findViewById(android.R.id.content);
        show(root, message);
    }

    public static void showError(Activity activity, String message) {
        View root = activity.findViewById(android.R.id.content);
        showError(root, message);
    }

    public static void showSuccess(Activity activity, String message) {
        View root = activity.findViewById(android.R.id.content);
        showSuccess(root, message);
    }
}