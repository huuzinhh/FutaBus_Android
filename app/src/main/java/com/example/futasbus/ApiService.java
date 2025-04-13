package com.example.futasbus;
import com.example.futasbus.request.LoginRequest;
import com.example.futasbus.request.OtpRequest;
import com.example.futasbus.request.RegisterRequest;
import com.example.futasbus.request.VerifyOtpRequest;
import com.example.futasbus.respone.LoginResponse;
import com.example.futasbus.respone.OtpResponse;
import com.example.futasbus.respone.RegisterResponse;
import com.example.futasbus.respone.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("api/user/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
    @POST("api/user/send-otp")
    Call<OtpResponse> sendOtp(@Body OtpRequest otpRequest);

    @POST("api/user/verify-otp")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest verifyOtpRequest);
}