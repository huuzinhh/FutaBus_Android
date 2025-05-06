package com.example.futasbus.helper;

import android.content.Context;

public class ToastHelper {

    public static void show(Context context, String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }
}
