package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.futasbus.Adapter.SeatPagerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.R;
import com.example.futasbus.respone.BookTicketsResponse;
import com.example.futasbus.respone.TicketResponse;
import com.example.futasbus.ApiService;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.model.ChuyenXeResult;
import com.example.futasbus.model.ViTriGhe;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeatSelectionActivity extends AppCompatActivity {
    TicketResponse ticketResponse;
    ChuyenXeResult selectedGo, selectedReturn;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SeatPagerAdapter adapter;
    private String startTimeGo, endTimeGo, startTimeReturn, endTimeReturn;

    private List<String> selectedSeatsGo = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();

    private double priceGo, priceReturn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        selectedGo = (ChuyenXeResult) intent.getSerializableExtra("goTrip");
        selectedReturn = (ChuyenXeResult) intent.getSerializableExtra("returnTrip");

        if (selectedGo != null) {
            startTimeGo = DateTimeHelper.toHour(selectedGo.getThoiDiemDi());
            endTimeGo = DateTimeHelper.toHour(selectedGo.getThoiDiemDen());
        }

        if (selectedReturn != null) {
            startTimeReturn = DateTimeHelper.toHour(selectedReturn.getThoiDiemDi());
            endTimeReturn = DateTimeHelper.toHour(selectedReturn.getThoiDiemDen());
        }

        Bundle Location = getIntent().getExtras();
        if (Location != null) {
            ticketResponse = new TicketResponse(
                    Location.getInt("departureId", -1),
                    Location.getInt("destinationId", -1),
                    Location.getString("departure"),
                    Location.getString("destination"),
                    Location.getString("departureDate"),
                    Location.getString("returnDate"),
                    Location.getBoolean("isRoundTrip", false),
                    Location.getInt("tickets")
            );
            if (!ticketResponse.getRoundTrip()) {
                findViewById(R.id.layoutReturnTrip).setVisibility(View.GONE);
            }
        }

        tabLayout = findViewById(R.id.tabLayout);
        if (!ticketResponse.getRoundTrip()) {
            tabLayout.setVisibility(View.GONE);
        }

        viewPager = findViewById(R.id.SeatviewPager);

        booktrip();

        Button btnConfirmSeat = findViewById(R.id.btnConfirmSeats);
        btnConfirmSeat.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("Starttimego", startTimeGo);
            bundle.putString("Endtimego", endTimeGo);
            bundle.putDouble("priceGo",priceGo);
            bundle.putStringArrayList("selectedSeatsGo", new ArrayList<>(selectedSeatsGo));
            Intent putintent = new Intent(SeatSelectionActivity.this, ConfirmBookingActivity.class);
            putintent.putExtra("ticket", ticketResponse);
            putintent.putExtra("goTrip", selectedGo);
            if (ticketResponse.getRoundTrip()) {
                putintent.putExtra("returnTrip", selectedReturn);
                bundle.putStringArrayList("selectedSeatsReturn", new ArrayList<>(selectedSeatsReturn));
                bundle.putString("Starttimereturn", startTimeReturn);
                bundle.putString("Endtimereturn", endTimeReturn);
                bundle.putDouble("priceReturn",priceReturn);
            }
            putintent.putExtras(bundle);
            startActivity(putintent);


//            Log.d("Starttimego", "startTimeGo: "+startTimeGo);
//            Log.d("endTimeGo", "endTimeGo: "+endTimeGo);
//            Log.d("priceGo", "priceGo: "+priceGo);
//            Log.d("selectedSeatsGo", "selectedSeatsGo: "+selectedSeatsGo);
//            Log.d("selectedSeatsReturn", "selectedSeatsReturn: "+selectedSeatsReturn);
//            Log.d("Starttimereturn", "Starttimereturn: "+startTimeReturn);
//            Log.d("endTimeReturn", "endTimeReturn: "+endTimeReturn);
//            Log.d("ticketResponse", "ticketResponse: "+ticketResponse.getDeparture());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getIdXe());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getThoiDiemDi());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getTenBenXeDi());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getThoiDiemDen());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getThoiDiemDen());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getGiaHienHanh());
//            Log.d("selectedGo", "selectedGo: "+selectedGo.getTripId());
        });
    }

    private void booktrip() {
        ApiService apiService = ApiClient.getApiService();
        Call<BookTicketsResponse> call = apiService.bookTickets(
                ticketResponse.getDepartureId(),
                ticketResponse.getDeparture(),
                ticketResponse.getDestinationId(),
                ticketResponse.getDestination(),
                selectedGo.getTenBenXeDi(),
                selectedGo.getTenBenXeDen(),
                selectedGo.getThoiDiemDi(),
                selectedReturn != null ? selectedReturn.getThoiDiemDi() : "",
                selectedGo.getTripId(),
                startTimeGo,
                endTimeGo,
                selectedGo.getTenLoai(),
                selectedGo.getGiaHienHanh(),
                ticketResponse.getTickets(),
                selectedGo.getIdXe(),
                selectedReturn != null ? selectedReturn.getTripId() : -1,
                selectedReturn != null ? startTimeReturn : "",
                selectedReturn != null ? endTimeReturn : "",
                selectedReturn != null ? selectedReturn.getGiaHienHanh() : 0,
                ticketResponse.getTickets(),
                selectedReturn != null ? selectedReturn.getIdXe() : -1
        );

        call.enqueue(new Callback<BookTicketsResponse>() {
            @Override
            public void onResponse(Call<BookTicketsResponse> call, Response<BookTicketsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookTicketsResponse data = response.body();

                    ArrayList<String> soldSeatsGo = new ArrayList<>();
                    ArrayList<String> soldSeatsReturn = new ArrayList<>();

                    for (ViTriGhe ghe : data.getViTriGheTangDuoiList()) {
                        if (ghe.getTrangThai() == 1) soldSeatsGo.add(ghe.getTenViTri());
                    }
                    for (ViTriGhe ghe : data.getViTriGheTangTrenList()) {
                        if (ghe.getTrangThai() == 1) soldSeatsGo.add(ghe.getTenViTri());
                    }
                    if (data.getViTriGheTangDuoiReturnList() != null) {
                        for (ViTriGhe ghe : data.getViTriGheTangDuoiReturnList()) {
                            if (ghe.getTrangThai() == 1) soldSeatsReturn.add(ghe.getTenViTri());
                        }
                    }

                    if (data.getViTriGheTangTrenReturnList() != null) {
                        for (ViTriGhe ghe : data.getViTriGheTangTrenReturnList()) {
                            if (ghe.getTrangThai() == 1) soldSeatsReturn.add(ghe.getTenViTri());
                        }
                    }


                    setupFragments(soldSeatsGo, soldSeatsReturn);

                } else {
                    Log.e("API", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BookTicketsResponse> call, Throwable t) {
                Log.e("API", "Thất bại: " + t.getMessage());
            }
        });
    }

    private void setupFragments(ArrayList<String> soldSeatsGo, ArrayList<String> soldSeatsReturn) {
        adapter = new SeatPagerAdapter(this);
        int maxSeats = ticketResponse.getTickets();

        // Tạo trước Fragment
        SeatSelectionFragment goFragment = SeatSelectionFragment.newInstance("go", soldSeatsGo, maxSeats);
        SeatSelectionFragment returnFragment = null;

        // Thêm Fragment vào adapter
        adapter.addFragment(goFragment, "Chiều đi");
        if (ticketResponse.getRoundTrip()) {
            returnFragment = SeatSelectionFragment.newInstance("return", soldSeatsReturn, maxSeats);
            adapter.addFragment(returnFragment, "Chiều về");
        }

        // Gán adapter sau khi đã add tất cả fragment
        viewPager.setAdapter(adapter);

        goFragment.setOnSeatSelectedListener((tripType, seats) -> {
            selectedSeatsGo = seats;
            updateSummaryView();
        });

        if (returnFragment != null) {
            returnFragment.setOnSeatSelectedListener((tripType, seats) -> {
                selectedSeatsReturn = seats;
                updateSummaryView();
            });
        }


        // Gắn TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(adapter.getTitle(position));
        }).attach();
    }


    private void updateSummaryView() {
        TextView tvSoVeGo = findViewById(R.id.Sove);
        TextView tvSeatGo = findViewById(R.id.seatNumbers);
        TextView tvSoVeReturn = findViewById(R.id.returnSove);
        TextView tvSeatReturn = findViewById(R.id.returnSeatNumbers);
        TextView tvTongTien = findViewById(R.id.Giatien);

        tvSoVeGo.setText(getString(R.string.ticket_count_seat, ticketResponse.getTickets()));
        tvSeatGo.setText(getString(R.string.seats_selected, TextUtils.join(", ", selectedSeatsGo)));

        if (ticketResponse.getRoundTrip()) {
            tvSoVeReturn.setText(getString(R.string.ticket_count_seat, ticketResponse.getTickets()));
            tvSeatReturn.setText(getString(R.string.seats_selected, TextUtils.join(", ", selectedSeatsReturn)));
        }

        priceGo = selectedSeatsGo.size() * selectedGo.getGiaHienHanh();
        priceReturn = selectedSeatsReturn.size() * (selectedReturn != null ? selectedReturn.getGiaHienHanh() : 0);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTongTien.setText("Số tiền: " + formatter.format(priceGo + priceReturn) + " VND");
    }
}