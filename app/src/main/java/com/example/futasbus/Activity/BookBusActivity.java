package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.futasbus.Adapter.BookBusPagerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.R;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.respone.TripResponse;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.ChuyenXeResult;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookBusActivity extends AppCompatActivity {
    List<ChuyenXeResult>  listGo, listReturn;
    private TextView txtDeparture, txtDestination;
    private ImageButton returnButton;
    private ImageView tripRoundArrow;

    private int departureId, destinationId, tickets;
    private String departure, destination, departureDate, returnDate;
    private boolean isRoundTrip;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BookBusPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bus);

        // Ánh xạ view
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vpBookTrip);
        txtDeparture = findViewById(R.id.txtDeparture);
        txtDestination = findViewById(R.id.txtDestination);
        returnButton = findViewById(R.id.btnBack);
        tripRoundArrow = findViewById(R.id.iv_round_trip);
        returnButton.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Intent
        Bundle Location = getIntent().getExtras();
        if (Location != null) {
            departureId = Location.getInt("departureId", -1);
            departure = Location.getString("departure");
            destinationId = Location.getInt("destinationId", -1);
            destination = Location.getString("destination");
            departureDate = Location.getString("departureDate");
            returnDate = Location.getString("returnDate");
            isRoundTrip = Location.getBoolean("isRoundTrip", false);
            tickets = Location.getInt("tickets");
        }

        txtDeparture.setText(departure);
        txtDestination.setText(destination);
        if(isRoundTrip){
            tripRoundArrow.setImageResource(R.drawable.ic_round_trip);
        }
        adapter = new BookBusPagerAdapter(this);
        viewPager.setAdapter(adapter);

        fetchTrips();

        Button btnConfirm = findViewById(R.id.btnConfirmTrip);
        btnConfirm.setOnClickListener(v -> {
            BookBusFragment goFragment = findFragmentByPosition(0);
            if (goFragment == null) {
                Log.d("DEBUG", "goFragment null");
                return;
            }
            ChuyenXeResult selectedGo = goFragment.getSelectedTrip();

            ChuyenXeResult selectedReturn = null;
            if (isRoundTrip && adapter.getItemCount() > 1) {
                BookBusFragment returnFragment = findFragmentByPosition(1);
                if (returnFragment == null) {
                    Log.d("DEBUG", "returnFragment null");
                    return;
                }
                selectedReturn = returnFragment.getSelectedTrip();
            }

            if (selectedGo == null || (isRoundTrip && selectedReturn == null)) {
                ToastHelper.show(BookBusActivity.this, "Vui lòng chọn đầy đủ chuyến xe");
                return;
            }

            // Chuyển sang màn hình tiếp theo (ví dụ chọn chỗ ngồi)
            Bundle bundle = new Bundle();
            bundle.putInt("departureId", departureId);
            bundle.putString("departure", departure);
            bundle.putInt("destinationId", destinationId);
            bundle.putString("destination", destination);
            bundle.putString("departureDate", departureDate);
            bundle.putString("returnDate", returnDate);
            bundle.putBoolean("isRoundTrip", isRoundTrip);
            bundle.putInt("tickets", tickets);

            Intent intent = new Intent(BookBusActivity.this, SeatSelectionActivity.class);
            intent.putExtras(bundle);

            intent.putExtra("goTrip", selectedGo);
            if (selectedReturn != null) {
                intent.putExtra("returnTrip", selectedReturn);
            }
            startActivity(intent);
        });
    }

    private BookBusFragment findFragmentByPosition(int position) {
        Fragment fragment = adapter.getFragment(position);
        if (fragment instanceof BookBusFragment) {
            return (BookBusFragment) fragment;
        }
        return null;
    }


    private void fetchTrips() {
        ApiService apiService = ApiClient.getApiService();
        Call<TripResponse> call = apiService.getTripSelection(
                departureId,
                departure,
                destinationId,
                destination,
                departureDate,
                returnDate,
                tickets);

        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listGo = response.body().getChuyenXeResultList();
                    listReturn = response.body().getChuyenXeReturnList();

                    BookBusFragment goFragment = BookBusFragment.newInstance(new ArrayList<>(listGo));
                    adapter.addFragment(goFragment, "Chiều đi");

                    if (isRoundTrip) {
                        BookBusFragment returnFragment = BookBusFragment.newInstance(new ArrayList<>(listReturn));
                        adapter.addFragment(returnFragment, "Chiều về");
                    }

                    viewPager.setAdapter(adapter);
                    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                        tab.setText(adapter.getTitle(position));
                    }).attach();
                } else {
                    ToastHelper.show(BookBusActivity.this, "Không có chuyến xe nào");
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Toast.makeText(BookBusActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }
}