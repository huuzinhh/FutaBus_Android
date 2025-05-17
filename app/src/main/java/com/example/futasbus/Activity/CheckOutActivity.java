package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.futasbus.Adapter.BookBusPagerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.model.ChuyenXeResult;
import com.example.futasbus.model.NguoiDung;
import com.example.futasbus.request.BookingRequest;
import com.example.futasbus.request.BookingWrapper;
import com.example.futasbus.respone.SelectedSeat;
import com.example.futasbus.respone.TicketResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    private TextView tvHoTen,tvPhone,tvEmail,tvgiavedi, tvgiaveve, tvtongtien;
    private double priceGo = 0, priceReturn = 0;
    private Button btnConfirm;
    private ImageButton returnButton;
    private TicketResponse ticketResponse;
    private ChuyenXeResult selectedGo, selectedReturn;
    private String startTimeGo, endTimeGo, startTimeReturn, endTimeReturn;
    private List<SelectedSeat> selectedSeatsGo = new ArrayList<>();
    private List<SelectedSeat> selectedSeatsReturn = new ArrayList<>();
    private NguoiDung user;
    private RadioGroup rgPaymentMethods;
    private RadioButton rbAgribank, rbCash;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private BookBusPagerAdapter adapter;
    private LinearLayout luotve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);
        tvgiavedi = findViewById(R.id.tvGiaVeLuotDi);
        tvgiaveve = findViewById(R.id.tvGiaVeLuotVe);
        tvtongtien = findViewById(R.id.tvTongTien);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        luotve = findViewById(R.id.luotve);
        rgPaymentMethods = findViewById(R.id.rgPaymentMethods);
        rbAgribank = findViewById(R.id.rbAgribank);
        rbCash = findViewById(R.id.rbCash);
        viewPager = findViewById(R.id.vptrip);
        tabLayout = findViewById(R.id.tabLayout);
        btnConfirm = findViewById(R.id.btnConfirm);
        returnButton = findViewById(R.id.btnBack);
        handleIntentData();

        setupFragments();
        returnButton.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }
    private void setupFragments() {
        adapter = new BookBusPagerAdapter(this);
        adapter.addFragment(CheckoutFragment.newInstance(ticketResponse,selectedGo,selectedSeatsGo,priceGo), "Ngày Đi");

        if (selectedReturn != null) {
            adapter.addFragment(CheckoutFragment.newInstance(ticketResponse,selectedReturn,selectedSeatsReturn, priceReturn), "Ngày Về");
        }

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Ngày Đi\n" + DateTimeHelper.toFullDateTime(selectedGo.getThoiDiemDi()));
            } else if (position == 1 && selectedReturn != null) {
                tab.setText("Ngày Về\n" + DateTimeHelper.toFullDateTime(selectedReturn.getThoiDiemDi()));
            }
        }).attach();
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        user = (NguoiDung) intent.getSerializableExtra("User_data");
        tvHoTen.setText(user.getHoTen());
        tvPhone.setText(user.getSoDienThoai());
        tvEmail.setText(user.getEmail());
        selectedGo = (ChuyenXeResult) intent.getSerializableExtra("goTrip");
        selectedReturn = (ChuyenXeResult) intent.getSerializableExtra("returnTrip");
        ticketResponse = (TicketResponse) intent.getSerializableExtra("ticket");
        selectedSeatsGo = (ArrayList<SelectedSeat>) intent.getSerializableExtra("selectedSeatsGo");
        selectedSeatsReturn = (ArrayList<SelectedSeat>) intent.getSerializableExtra("selectedSeatsReturn");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            startTimeGo = bundle.getString("Starttimego");
            endTimeGo = bundle.getString("Endtimego");
            priceGo = bundle.getDouble("priceGo", 0);

            if (selectedReturn != null) {
                startTimeReturn = bundle.getString("Starttimereturn");
                endTimeReturn = bundle.getString("Endtimereturn");
                priceReturn = bundle.getDouble("priceReturn", 0);
            }
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
//        txtDeparture.setText(ticketResponse.getDeparture());
//        txtDestination.setText(ticketResponse.getDestination());
        tvgiavedi.setText("Số tiền: " + formatter.format(priceGo) + " VND");
        tvtongtien.setText("Số tiền: " + formatter.format(priceGo + priceReturn) + " VND");

        if (selectedReturn != null) {
            tvgiaveve.setText("Số tiền: " + formatter.format(priceReturn) + " VND");
            luotve.setVisibility(View.VISIBLE);
        }
    }
    private void goToThankYouActivity() {
        Intent intent = new Intent(this, ThankYouActivity.class);

        intent.putExtra("User_data",user);

        intent.putExtra("goTrip", selectedGo);
        intent.putExtra("ticket", ticketResponse);
        intent.putExtra("priceGo", priceGo);

        if (selectedReturn != null) {
            intent.putExtra("priceReturn", priceReturn);
            intent.putExtra("returnTrip", selectedReturn);
        }

        startActivity(intent);
    }
    private void confirmBooking() {
        BookingRequest bookingGo = new BookingRequest();
        bookingGo.setSoLuongVe(ticketResponse.getTickets());
        bookingGo.setTongTien(priceGo);
        bookingGo.setHoTen(tvHoTen.getText().toString());
        bookingGo.setSoDienThoai(tvPhone.getText().toString());
        bookingGo.setEmail(tvEmail.getText().toString());
        bookingGo.setIdChuyenXe(selectedGo.getTripId());
        bookingGo.setIdViTriGhe(
                String.join(",", selectedSeatsGo.stream()
                        .map(seat -> String.valueOf(seat.getIdViTri()))
                        .toArray(String[]::new))
        );

        Log.d("BookingGo", "HoTen: " + bookingGo.getHoTen());
        Log.d("BookingGo", "SoDienThoai: " + bookingGo.getSoDienThoai());
        Log.d("BookingGo", "Email: " + bookingGo.getEmail());
        Log.d("BookingGo", "IdChuyenXe: " + bookingGo.getIdChuyenXe());
        Log.d("BookingGo", "IdViTriGhe: " + bookingGo.getIdViTriGhe());
        Log.d("BookingGo", "SoLuongVe: " + bookingGo.getSoLuongVe());
        Log.d("BookingGo", "TongTien: " + bookingGo.getTongTien());


        BookingRequest bookingReturn = null;
        if (selectedReturn != null && !selectedSeatsReturn.isEmpty()) {
            bookingReturn = new BookingRequest();
            bookingReturn.setSoLuongVe(selectedSeatsReturn.size());
            bookingReturn.setTongTien(priceReturn);
            bookingReturn.setHoTen(tvHoTen.getText().toString());
            bookingReturn.setSoDienThoai(tvPhone.getText().toString());
            bookingReturn.setEmail(tvEmail.getText().toString());
            bookingReturn.setIdChuyenXe(selectedReturn.getTripId());
            bookingReturn.setIdViTriGhe(
                    String.join(",", selectedSeatsReturn.stream()
                            .map(seat -> String.valueOf(seat.getIdViTri()))
                            .toArray(String[]::new))
            );
            Log.d("BookingReturn", "HoTen: " + bookingReturn.getHoTen());
            Log.d("BookingReturn", "SoDienThoai: " + bookingReturn.getSoDienThoai());
            Log.d("BookingReturn", "Email: " + bookingReturn.getEmail());
            Log.d("BookingReturn", "IdChuyenXe: " + bookingReturn.getIdChuyenXe());
            Log.d("BookingReturn", "IdViTriGhe: " + bookingReturn.getIdViTriGhe());
            Log.d("BookingReturn", "SoLuongVe: " + bookingReturn.getSoLuongVe());
            Log.d("BookingReturn", "TongTien: " + bookingReturn.getTongTien());
        }


        BookingWrapper wrapper = new BookingWrapper();
        wrapper.setBookingData(bookingGo);
        wrapper.setBookingDataReturn(bookingReturn);
        Log.d("Booking", "User ID: " + user.getIdNguoiDung());
            ApiService apiService = ApiClient.getApiService();
            apiService.confirmBooking(user.getIdNguoiDung(), wrapper).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = (String) response.body().get("status");
                        String message = (String) response.body().get("message");
                        if ("success".equalsIgnoreCase(status)) {
                            ToastHelper.show(CheckOutActivity.this, "Đặt vé thành công!");
                            goToThankYouActivity();
                        } else {
                            ToastHelper.show(CheckOutActivity.this, message != null ? message : "Đặt vé thất bại");
                        }
                    } else {
                        try {
                            String error = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                            Log.e("API_ERROR", "Mã lỗi: " + response.code() + " - Nội dung: " + error);
                            ToastHelper.show(CheckOutActivity.this, "Lỗi khi đặt vé: " + error);
                        } catch (IOException e) {
                            Log.e("API_ERROR", "Lỗi khi đọc errorBody", e);
                            ToastHelper.show(CheckOutActivity.this, "Không thể đọc phản hồi lỗi từ server");
                        }
                    }
                }
    
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    ToastHelper.show(CheckOutActivity.this, "Lỗi mạng: " + t.getMessage());
                }
            });
    }
}
