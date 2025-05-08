package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.futasbus.Adapter.BookBusPagerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.helper.SharedPrefHelper;
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
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingActivity extends AppCompatActivity {

    private TextView txtDeparture, txtDestination, tvgiavedi, tvgiaveve, tvtongtien,tvHoTen, tvPhone, tvEmail;
    private ImageButton returnButton, btnEdit;
    private ImageView tripRoundArrow;
    private Button btnConfirmTrip;

    private TicketResponse ticketResponse;
    private ChuyenXeResult selectedGo, selectedReturn;
    private String startTimeGo, endTimeGo, startTimeReturn, endTimeReturn;
    private double priceGo = 0, priceReturn = 0;
    private boolean isTripRound;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BookBusPagerAdapter adapter;
    private LinearLayout luotve;

    private List<SelectedSeat> selectedSeatsGo = new ArrayList<>();
    private List<SelectedSeat> selectedSeatsReturn = new ArrayList<>();
    private NguoiDung user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_booking);

        initViews();
        fetchUserInfo();

        handleIntentData();

        setupFragments();

        btnConfirmTrip.setOnClickListener(v -> goToCheckOutActivity());
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vpBookTrip);
        txtDeparture = findViewById(R.id.txtDeparture);
        txtDestination = findViewById(R.id.txtDestination);
        tvgiavedi = findViewById(R.id.tvGiaVeLuotDi);
        tvgiaveve = findViewById(R.id.tvGiaVeLuotVe);
        tvtongtien = findViewById(R.id.tvTongTien);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        luotve = findViewById(R.id.luotve);
        btnEdit = findViewById(R.id.btnEdit);
        returnButton = findViewById(R.id.btnBack);
        tripRoundArrow = findViewById(R.id.iv_round_trip);
        btnConfirmTrip = findViewById(R.id.btnConfirmTrip);

        luotve.setVisibility(View.GONE);

        returnButton.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditBookingCustomerInfoActivity.class);
            intent.putExtra("name", tvHoTen.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("phone", tvPhone.getText().toString());
            intent.putExtra("istripround", isTripRound);
            intent.putExtra("departure", ticketResponse.getDeparture());
            intent.putExtra("destination", ticketResponse.getDestination());
            startActivityForResult(intent, 1);
        });
    }

    @SuppressWarnings("unchecked")
    private void handleIntentData() {
        Intent intent = getIntent();

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
                isTripRound = true;
                tripRoundArrow.setImageResource(R.drawable.ic_round_trip);
            }
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        txtDeparture.setText(ticketResponse.getDeparture());
        txtDestination.setText(ticketResponse.getDestination());
        tvgiavedi.setText("Số tiền: " + formatter.format(priceGo) + " VND");
        tvtongtien.setText("Số tiền: " + formatter.format(priceGo + priceReturn) + " VND");

        if (selectedReturn != null) {
            tvgiaveve.setText("Số tiền: " + formatter.format(priceReturn) + " VND");
            luotve.setVisibility(View.VISIBLE);
        }
    }

    private void setupFragments() {
        adapter = new BookBusPagerAdapter(this);
        adapter.addFragment(ConfirmBookingFragment.newInstance(selectedGo,selectedSeatsGo, startTimeGo, endTimeGo), "Ngày Đi");

        if (selectedReturn != null) {
            adapter.addFragment(ConfirmBookingFragment.newInstance(selectedReturn,selectedSeatsReturn, startTimeReturn, endTimeReturn), "Ngày Về");
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

    private void fetchUserInfo() {
        int userId = SharedPrefHelper.getUserId(this);
        if (userId == -1) return;

        ApiService apiService = ApiClient.getApiService();
        apiService.getGeneralInformation(userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    tvHoTen.setText(user.getHoTen());
                    tvPhone.setText(user.getSoDienThoai());
                    tvEmail.setText(user.getEmail());
                } else {
                    Log.e("API", "Không lấy được thông tin người dùng");
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e("API", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            tvHoTen.setText(data.getStringExtra("name"));
            tvPhone.setText(data.getStringExtra("phone"));
            user.setHoTen(data.getStringExtra("name"));
            user.setSoDienThoai(data.getStringExtra("phone"));
        }
    }

    private void goToCheckOutActivity() {
        Intent intent = new Intent(this, CheckOutActivity.class);

        intent.putExtra("User_data",user);

        intent.putExtra("goTrip", selectedGo);
        intent.putExtra("ticket", ticketResponse);
        intent.putExtra("selectedSeatsGo", new ArrayList<>(selectedSeatsGo));
        intent.putExtra("Starttimego", startTimeGo);
        intent.putExtra("Endtimego", endTimeGo);
        intent.putExtra("priceGo", priceGo);

        if (selectedReturn != null) {
            intent.putExtra("Starttimereturn", startTimeReturn);
            intent.putExtra("Endtimereturn", endTimeReturn);
            intent.putExtra("priceReturn", priceReturn);
            intent.putExtra("returnTrip", selectedReturn);
            intent.putExtra("selectedSeatsReturn", new ArrayList<>(selectedSeatsReturn));
        }

        startActivity(intent);
    }

}
