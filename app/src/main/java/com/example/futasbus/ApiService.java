package com.example.futasbus;
import com.example.futasbus.model.BookingInfo;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.NguoiDung;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.request.BookingWrapper;
import com.example.futasbus.request.LoginRequest;
import com.example.futasbus.request.OtpRequest;
import com.example.futasbus.request.RegisterRequest;
import com.example.futasbus.request.VerifyOtpRequest;
import com.example.futasbus.respone.BookTicketsResponse;
import com.example.futasbus.respone.ListPurchaseResponse;
import com.example.futasbus.respone.LoginResponse;
import com.example.futasbus.respone.OtpResponse;
import com.example.futasbus.respone.RegisterResponse;
import com.example.futasbus.respone.TinhThanhResponse;
import com.example.futasbus.respone.TripResponse;
import com.example.futasbus.respone.VerifyOtpResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    @GET("api/user/purchase-history/{userId}")
    Call<ListPurchaseResponse> getPurchaseHistory(
            @Path("userId") int userId
    );
    @GET("api/user/general-information/{id}")
    Call<NguoiDung> getGeneralInformation(@Path("id") int idNguoiDung);
    @Headers("Content-Type: application/json")
    @POST("api/user/confirmBooking1")
    Call<Map<String, Object>> confirmBooking(@Header("userid") int userId, @Body BookingWrapper wrapper);

    @POST("api/user/update-user")
    Call<Map<String, Object>> updateUserInfo(@Body NguoiDung nguoiDung);
    @GET("api/admin/getListTuyenXe")
    Call<List<TuyenXe>> getDanhSachTuyenXe();
    @GET("api/admin/getListChuyenXe")
    Call<List<ChuyenXe>> getDanhSachChuyenXe();

    @GET("api/admin/nguoidung/all")
    Call<List<NguoiDung>> getAllNguoiDung();

    @GET("api/admin/tuyenxe/all")
    Call<List<TuyenXe>> getAllTuyenXe();

    @GET("api/admin/phieudatve/all")
    Call<List<BookingInfo>> getAllPhieuDatVe();

}