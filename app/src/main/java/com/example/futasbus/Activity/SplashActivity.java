package com.example.futasbus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.futasbus.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        Intent intent;
        if (isLoggedIn) {
            int role = sharedPref.getInt("idPhanQuyen", -1);
            if (role == 1) {
                intent = new Intent(this, UserHomeActivity.class);
            } else if (role == 2) {
                intent = new Intent(this, AdminHomeActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class); // nếu không xác định được quyền
            }
        } else {
            intent = new Intent(this, LoginActivity.class); // nếu chưa đăng nhập
        }

        startActivity(intent);
        finish();
    }
}
