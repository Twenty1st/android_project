package com.example.rating_movie_app;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
