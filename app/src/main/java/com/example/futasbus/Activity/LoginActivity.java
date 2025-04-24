package com.example.futasbus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Import các activity
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.request.LoginRequest;
import com.example.futasbus.request.RegisterRequest;
import com.example.futasbus.respone.LoginResponse;
import com.example.futasbus.respone.RegisterResponse;


public class LoginActivity extends AppCompatActivity {

    private TextView tvTitle, tvTabLogin, tvTabRegister, tvForgotPassword;
    private LinearLayout layoutLoginForm, layoutRegisterForm;
    private EditText etPhoneNumber, etPassword, etFullName, etRegisterPhoneNumber, etRegisterPassword, etConfirmPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvTabLogin = findViewById(R.id.tv_tab_login);
        tvTabRegister = findViewById(R.id.tv_tab_register);
        layoutLoginForm = findViewById(R.id.layout_login_form);
        layoutRegisterForm = findViewById(R.id.layout_register_form);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);
        etRegisterPhoneNumber = findViewById(R.id.et_register_phone_number);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // Tab Đăng Nhập
        tvTabLogin.setOnClickListener(v -> {
            tvTitle.setText("Đăng nhập tài khoản");
            tvTabLogin.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            tvTabRegister.setTextColor(getResources().getColor(android.R.color.darker_gray));
            layoutLoginForm.setVisibility(View.VISIBLE);
            layoutRegisterForm.setVisibility(View.GONE);
        });

        // Tab Đăng Ký
        tvTabRegister.setOnClickListener(v -> {
            tvTitle.setText("Đăng ký tài khoản");
            tvTabLogin.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvTabRegister.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            layoutLoginForm.setVisibility(View.GONE);
            layoutRegisterForm.setVisibility(View.VISIBLE);
        });

        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(phoneNumber, password);
            }
        });



        // Handle forgot password click
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void loginUser(String soDienThoai, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(soDienThoai, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String status = loginResponse.getStatus();
                    String message = loginResponse.getMessage();

                    if ("success".equals(status)) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        int idPhanQuyen = -1;

                        if (loginResponse.getUser() != null) {
                            editor.putInt("idNguoiDung", loginResponse.getUser().getIdNguoiDung());
                            editor.putString("hoTen", loginResponse.getUser().getHoTen());
                            idPhanQuyen = loginResponse.getUser().getIdPhanQuyen();
                            editor.putInt("idPhanQuyen", idPhanQuyen);
                        } else {
                            Log.w("LOGIN_WARNING", "Không nhận được thông tin người dùng từ server");
                        }
                        editor.apply();

                        Intent intent;
                        switch (idPhanQuyen) {
                            case 1:
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                break;
                            case 2:
                                intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                break;

                            default:
                                Toast.makeText(LoginActivity.this, "Phân quyền không hợp lệ", Toast.LENGTH_SHORT).show();
                                return;
                        }

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LOGIN_ERROR", "Phản hồi không thành công: " + response.code());
                    Toast.makeText(LoginActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMsg = t.getMessage() != null ? t.getMessage() : "Lỗi kết nối không xác định";
                Log.e("LOGIN_ERROR", "Lỗi Retrofit: " + errorMsg, t);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


}