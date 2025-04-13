package com.example.futasbus.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.futasbus.R;

public class UserHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String hoTen = sharedPref.getString("hoTen", "Khách");
        TextView tvWelcome = findViewById(R.id.tv_welcom);
        tvWelcome.setText("Chào mừng " + hoTen + "!");
    }
}
