package com.example.futasbus.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futasbus.Adapter.RouteAdapter;
import com.example.futasbus.Adapter.TripAdapter;
import com.example.futasbus.Adapter.TripManagementAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.TuyenXe;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripManagementActivity extends AppCompatActivity {
    RecyclerView recyclerViewTrips ;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_management);
        recyclerViewTrips  = findViewById(R.id.recyclerViewTrip);
        btnBack = findViewById(R.id.btnBack);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));
        fetchTripData();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void fetchTripData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ChuyenXe>> call = apiService.getDanhSachChuyenXe();

        call.enqueue(new Callback<List<ChuyenXe>>() {
            @Override
            public void onResponse(Call<List<ChuyenXe>> call, Response<List<ChuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChuyenXe> chuyenXeList = response.body();

                    // Gán vào RecyclerView
                    TripManagementAdapter adapter = new TripManagementAdapter(chuyenXeList);
                    recyclerViewTrips.setAdapter(adapter);

                } else {

                    Toast.makeText(TripManagementActivity.this, "Không tải được danh sách chuyến xe", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<ChuyenXe>> call, Throwable t) {
                Toast.makeText(TripManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

