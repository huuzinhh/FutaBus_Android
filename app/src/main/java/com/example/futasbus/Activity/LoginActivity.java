package com.example.futasbus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.request.CreateAccountRequest;
import com.example.futasbus.request.LoginRequest;
import com.example.futasbus.request.OtpRequest;
import com.example.futasbus.request.VerifyOtpRequest;
import com.example.futasbus.respone.CreateAccountResponse;
import com.example.futasbus.respone.LoginResponse;
import com.example.futasbus.respone.OtpResponse;
import com.example.futasbus.respone.VerifyOtpResponse;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvTitle, tvTabLogin, tvTabRegister, tvForgotPassword;
    private LinearLayout layoutLoginForm, layoutRegisterForm, layoutOtpForm,layoutNewPasswordForm;
    private EditText etEmail, etPassword, etRegisterEmail, etOtp,etPasswordRegister,etComfirmPasswordRegister;
    private Button btnLogin, btnSendOtp, btnVerifyOtp,registerComplete;
    private String registerEmail;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageView googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvTitle = findViewById(R.id.tv_title);
        tvTabLogin = findViewById(R.id.tv_tab_login);
        tvTabRegister = findViewById(R.id.tv_tab_register);
        layoutLoginForm = findViewById(R.id.layout_login_form);
        layoutRegisterForm = findViewById(R.id.layout_register_form);
        layoutOtpForm = findViewById(R.id.layout_otp_form);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etRegisterEmail = findViewById(R.id.et_register_email);
        etOtp = findViewById(R.id.et_otp);
        btnLogin = findViewById(R.id.btn_login);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        etPasswordRegister = findViewById(R.id.et_new_password_register);
        layoutNewPasswordForm = findViewById(R.id.layout_password_form_register);
        etComfirmPasswordRegister = findViewById(R.id.et_confirm_password_register);
        registerComplete = findViewById(R.id.btn_finish_register);


        tvTabLogin.setOnClickListener(v -> {
            tvTitle.setText("Đăng nhập tài khoản");
            tvTabLogin.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
            tvTabRegister.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            layoutLoginForm.setVisibility(View.VISIBLE);
            layoutRegisterForm.setVisibility(View.GONE);
            layoutOtpForm.setVisibility(View.GONE);
        });

        tvTabRegister.setOnClickListener(v -> {
            tvTitle.setText("Đăng ký tài khoản");
            tvTabLogin.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            tvTabRegister.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
            layoutLoginForm.setVisibility(View.GONE);
            layoutRegisterForm.setVisibility(View.VISIBLE);
            layoutOtpForm.setVisibility(View.GONE);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        btnSendOtp.setOnClickListener(v -> {
            String email = etRegisterEmail.getText().toString().trim();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                registerEmail = email;
                sendOtpToEmail(email);
                layoutRegisterForm.setVisibility(View.GONE);
                layoutOtpForm.setVisibility(View.VISIBLE);
            }
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOtp(registerEmail, otp);
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
        });

        googleBtn = findViewById(R.id.google_btn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String email = acct.getEmail();
            loginWithGoogle(email);
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void loginUser(String email, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

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
                            editor.putBoolean("isLoggedIn", true);

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

    private void sendOtpToEmail(String email) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        OtpRequest request = new OtpRequest(email);
        Call<OtpResponse> call = apiService.sendOtp(request);
        call.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Đã gửi OTP đến: " + email, Toast.LENGTH_SHORT).show();
                    layoutRegisterForm.setVisibility(View.GONE);
                    layoutOtpForm.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi khi gửi OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp(String email, String otp) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        VerifyOtpRequest request = new VerifyOtpRequest(email, otp);
        Call<VerifyOtpResponse> call = apiService.verifyOtp(request);
        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    layoutOtpForm.setVisibility(View.GONE);
                    layoutLoginForm.setVisibility(View.GONE);
                    layoutNewPasswordForm.setVisibility(View.VISIBLE);
//                    tvTabLogin.performClick();
                    registerComplete.setOnClickListener(v -> {
                        String newPassword = etPasswordRegister.getText().toString().trim();
                        String confirmPassword = etComfirmPasswordRegister.getText().toString().trim();

                        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ mật khẩu", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(LoginActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Gọi API tạo tài khoản hoặc đặt lại mật khẩu
                        createAccount(email, newPassword);
                    });

                } else {
                    Toast.makeText(LoginActivity.this, response.body() != null ? response.body().getMessage() : "Lỗi xác thực OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    String name = account.getDisplayName();
                    String email = account.getEmail();

                    Map<String, String> dataNguoiDung = new HashMap<>();
                    dataNguoiDung.put("email", account.getEmail());
                    dataNguoiDung.put("name", account.getDisplayName());

                    ApiService apiService = ApiClient.getClient().create(ApiService.class);

                    Call<LoginResponse> call = apiService.loginWithGoogleMobile(dataNguoiDung);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                loginWithGoogle(email);
                            } else {
                                String errorMsg = "Lỗi: " + response.code() + " - " + response.message();
                                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                Log.e("API_ERROR", errorMsg);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("API_FAILURE", t.toString());
                        }
                    });
                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void loginWithGoogle(String email) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email);

        Call<LoginResponse> call = apiService.loginGoogleAndroid(request);
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

                        if (loginResponse.getUser() != null) {
                            editor.putInt("idNguoiDung", loginResponse.getUser().getIdNguoiDung());
                            editor.putString("hoTen", loginResponse.getUser().getHoTen());
                            editor.putBoolean("isLoggedIn", true);
                        }

                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("GOOGLE_LOGIN_ERROR", "Phản hồi không thành công: " + response.code());
                    Toast.makeText(LoginActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMsg = t.getMessage() != null ? t.getMessage() : "Lỗi kết nối không xác định";
                Log.e("GOOGLE_LOGIN_ERROR", "Lỗi Retrofit: " + errorMsg, t);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createAccount(String email, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        CreateAccountRequest request = new CreateAccountRequest(email, password);

        Call<CreateAccountResponse> call = apiService.createAccount(request);
        call.enqueue(new Callback<CreateAccountResponse>() {
            @Override
            public void onResponse(Call<CreateAccountResponse> call, Response<CreateAccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CreateAccountResponse res = response.body();
                    if (res.isSuccess()) {
                        Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                        // Chuyển về màn hình đăng nhập
                        layoutNewPasswordForm.setVisibility(View.GONE);
                        layoutLoginForm.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                        Log.e("API_ERROR", "Lỗi từ server: " + errorMsg);
                        Toast.makeText(LoginActivity.this, "Lỗi máy chủ: " + errorMsg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Không đọc được lỗi từ server!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateAccountResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}