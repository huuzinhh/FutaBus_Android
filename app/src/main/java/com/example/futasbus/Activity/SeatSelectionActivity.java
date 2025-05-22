package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.futasbus.Adapter.SeatPagerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.R;
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.respone.BookTicketsResponse;
import com.example.futasbus.respone.SelectedSeat;
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
    private String startTimeGo, endTimeGo, startTimeReturn, endTimeReturn,biensoxeGo,biensoxeReturn;

    private List<SelectedSeat> selectedSeatsGo = new ArrayList<>();
    private List<SelectedSeat> selectedSeatsReturn = new ArrayList<>();
    private ActivityResultLauncher<Intent> loginLauncher;

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

        loginLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d("DEBUG", "LoginActivity returned with resultCode: " + result.getResultCode());
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("UserId", "Đăng nhập thành công, ID: " + SharedPrefHelper.getUserId(this));
                    } else {
                        Log.d("UserId", "LoginActivity trả về không thành công");
                    }
                }
        );

        Button btnConfirmSeat = findViewById(R.id.btnConfirmSeats);
        btnConfirmSeat.setOnClickListener(v -> {

            int idNguoiDung = SharedPrefHelper.getUserId(this);
            if (idNguoiDung == -1) {
                // Inflate layout tùy chỉnh
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_login, null);

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                // Ánh xạ các thành phần
                Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                Button btnLogin = dialogView.findViewById(R.id.btnLogout);
                TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
                TextView tvTitle = dialogView.findViewById(R.id.tvTitle);

                // Có thể chỉnh sửa lại nội dung tại đây nếu cần
                tvTitle.setText("Thông báo");
                tvMessage.setText("Bạn chưa đăng nhập! \n Vui lòng đăng nhập để tiếp tục!");

                btnCancel.setOnClickListener(view -> alertDialog.dismiss());
                btnLogin.setOnClickListener(view -> {
                    alertDialog.dismiss();
                    Intent login = new Intent(this, LoginActivity.class);
                    login.putExtra("fromSeatSelection", true);
                    loginLauncher.launch(login);
                });

                alertDialog.show();
                return;
            }
//            Log.d("SEATS_GO", "Danh sách ghế chiều đi:");
//            for (SelectedSeat seat : selectedSeatsGo) {
//                Log.d("SEATS_GO","vị trí :" +seat.getIdViTri());
//            }
//
//            if (ticketResponse.getRoundTrip()) {
//                Log.d("SEATS_RETURN", "Danh sách ghế chiều về:");
//                for (SelectedSeat seat : selectedSeatsReturn) {
//                    Log.d("SEATS_RETURN", "vị trí :" +seat.getTenViTri());
//                }
//            }
            if (selectedSeatsGo.size() < ticketResponse.getTickets()) {
                ToastHelper.show(this, "Lượt đi: Bạn phải chọn tối thiểu " + ticketResponse.getTickets() + " ghế");
                return;
            }
            if (ticketResponse.getRoundTrip()) {
                if (selectedSeatsReturn.size() < ticketResponse.getTickets()) {
                    ToastHelper.show(this, "Lượt về: Bạn phải chọn tối thiểu " + ticketResponse.getTickets() + " ghế");
                    return;
                }
            }

            // Tiếp tục xử lý khi đã đăng nhập
            Bundle bundle = new Bundle();
            bundle.putString("Starttimego", startTimeGo);
            bundle.putString("Endtimego", endTimeGo);
            bundle.putDouble("priceGo", priceGo);
            bundle.putString("biensoxeGo", biensoxeGo);
            Intent putintent = new Intent(SeatSelectionActivity.this, ConfirmBookingActivity.class);
            putintent.putExtra("ticket", ticketResponse);
            putintent.putExtra("goTrip", selectedGo);
            putintent.putExtra("selectedSeatsGo", new ArrayList<>(selectedSeatsGo));
            if (ticketResponse.getRoundTrip()) {
                putintent.putExtra("returnTrip", selectedReturn);
                putintent.putExtra("selectedSeatsReturn", new ArrayList<>(selectedSeatsReturn));
                bundle.putString("Starttimereturn", startTimeReturn);
                bundle.putString("Endtimereturn", endTimeReturn);
                bundle.putDouble("priceReturn", priceReturn);
                bundle.putString("biensoxeReturn", biensoxeReturn);
            }

            putintent.putExtras(bundle);
            startActivity(putintent);
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
                    ArrayList<SelectedSeat> SeatGo = new ArrayList<>();
                    ArrayList<SelectedSeat> SeatReturn = new ArrayList<>();
                    ArrayList<String> soldSeatsGo = new ArrayList<>();
                    ArrayList<String> soldSeatsReturn = new ArrayList<>();
                    biensoxeGo = data.getBiensoxeGo();
                    biensoxeReturn = data.getBiensoxeReturn() != null ? data.getBiensoxeReturn() : "";

                    for (ViTriGhe ghe : data.getViTriGheTangDuoiList()) {
                        SeatGo.add(new SelectedSeat(ghe.getIdViTriGhe(), ghe.getTenViTri()));
                        if (ghe.getTrangThai() == 1){
                            soldSeatsGo.add(ghe.getTenViTri());
                        }
                    }
                    for (ViTriGhe ghe : data.getViTriGheTangTrenList()) {
                        SeatGo.add(new SelectedSeat(ghe.getIdViTriGhe(), ghe.getTenViTri()));
                        if (ghe.getTrangThai() == 1){
                            soldSeatsGo.add(ghe.getTenViTri());
                        }
                    }
                    if (data.getViTriGheTangDuoiReturnList() != null) {
                        for (ViTriGhe ghe : data.getViTriGheTangDuoiReturnList()) {
                            SeatReturn.add(new SelectedSeat(ghe.getIdViTriGhe(), ghe.getTenViTri()));
                            if (ghe.getTrangThai() == 1) {
                                soldSeatsReturn.add(ghe.getTenViTri());
                            }
                        }
                    }

                    if (data.getViTriGheTangTrenReturnList() != null) {
                        for (ViTriGhe ghe : data.getViTriGheTangTrenReturnList()) {
                            SeatReturn.add(new SelectedSeat(ghe.getIdViTriGhe(), ghe.getTenViTri()));
                            if (ghe.getTrangThai() == 1) {
                                soldSeatsReturn.add(ghe.getTenViTri());
                            }
                        }
                    }


                    setupFragments(SeatGo,SeatReturn,soldSeatsGo, soldSeatsReturn);

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

    private void setupFragments(ArrayList<SelectedSeat> SeatGo,ArrayList<SelectedSeat> SeatReturn,ArrayList<String> soldSeatsGo, ArrayList<String> soldSeatsReturn) {
        adapter = new SeatPagerAdapter(this);
        int maxSeats = ticketResponse.getTickets();

        // Tạo trước Fragment
        SeatSelectionFragment goFragment = SeatSelectionFragment.newInstance("go",SeatGo, soldSeatsGo, maxSeats);
        SeatSelectionFragment returnFragment = null;

        // Thêm Fragment vào adapter
        adapter.addFragment(goFragment, "Chiều đi");
        if (ticketResponse.getRoundTrip()) {
            returnFragment = SeatSelectionFragment.newInstance("return", SeatReturn,soldSeatsReturn, maxSeats);
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
        TextView tvSeatGo = findViewById(R.id.seatNumbers);
        TextView tvSeatReturn = findViewById(R.id.returnSeatNumbers);
        TextView tvTongTien = findViewById(R.id.Giatien);

        List<String> seatNamesGo = new ArrayList<>();
        for (SelectedSeat seat : selectedSeatsGo) {
            seatNamesGo.add(seat.getTenViTri());
        }

        List<String> seatNamesReturn = new ArrayList<>();
        for (SelectedSeat seat : selectedSeatsReturn) {
            seatNamesReturn.add(seat.getTenViTri());
        }

        tvSeatGo.setText(getString(R.string.seats_selected, TextUtils.join(", ", seatNamesGo)));
        if (ticketResponse.getRoundTrip()) {
            tvSeatReturn.setText(getString(R.string.seats_selected, TextUtils.join(", ", seatNamesReturn)));
        }

        priceGo = seatNamesGo.size() * selectedGo.getGiaHienHanh();
        priceReturn = seatNamesReturn.size() * (selectedReturn != null ? selectedReturn.getGiaHienHanh() : 0);
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTongTien.setText("Số tiền: " + formatter.format(priceGo + priceReturn) + " VND");
    }
}