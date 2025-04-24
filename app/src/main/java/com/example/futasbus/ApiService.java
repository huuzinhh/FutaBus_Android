package com.example.futasbus;
import com.example.futasbus.request.LoginRequest;
import com.example.futasbus.request.OtpRequest;
import com.example.futasbus.request.RegisterRequest;
import com.example.futasbus.request.VerifyOtpRequest;
import com.example.futasbus.respone.BookTicketsResponse;
import com.example.futasbus.respone.LoginResponse;
import com.example.futasbus.respone.OtpResponse;
import com.example.futasbus.respone.RegisterResponse;
import com.example.futasbus.respone.TinhThanhResponse;
import com.example.futasbus.respone.TripResponse;
import com.example.futasbus.respone.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("api/user/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
    @POST("api/user/send-otp")
    Call<OtpResponse> sendOtp(@Body OtpRequest otpRequest);

    @POST("api/user/verify-otp")
    Call<VerifyOtpResponse> verifyOtp(@Body VerifyOtpRequest verifyOtpRequest);
    @GET("api/user/tinhthanh") // ví dụ route API
    Call<TinhThanhResponse> getDanhSachTinhThanh();
    @GET("api/user/trip-selection")
    Call<TripResponse> getTripSelection(
            @Query("departureId") int departureId,
            @Query("departure") String departure,
            @Query("destinationId") int destinationId,
            @Query("destination") String destination,
            @Query("departureDate") String departureDate,
            @Query("returnDate") String returnDate,
            @Query("tickets") int tickets
    );
    @GET("api/user/book-tickets")
    Call<BookTicketsResponse> bookTickets(
            @Query("departureId") int departureId,
            @Query("departure") String departure,
            @Query("destinationId") int destinationId,
            @Query("destination") String destination,
            @Query("start") String start,
            @Query("end") String end,
            @Query("departureDate") String departureDate,
            @Query("returnDate") String returnDate,
            @Query("idTrip") int idTrip,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime,
            @Query("loai") String loai,
            @Query("price") double price,
            @Query("soGhe") int soGhe,
            @Query("idXe") int idXe,
            @Query("idTripReturn") int idTripReturn,
            @Query("startTimeReturn") String startTimeReturn,
            @Query("endTimeReturn") String endTimeReturn,
            @Query("priceReturn") double priceReturn,
            @Query("soGheReturn") int soGheReturn,
            @Query("idXeReturn") int idXeReturn
    );
}