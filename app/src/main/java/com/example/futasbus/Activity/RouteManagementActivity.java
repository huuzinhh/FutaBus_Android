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
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.respone.RouteResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteManagementActivity extends AppCompatActivity {
    RecyclerView recyclerViewRoutes;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management);
        recyclerViewRoutes = findViewById(R.id.recyclerViewRoutes);
        btnBack = findViewById(R.id.btnBack);
        fetchTuyenXeData();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void fetchTuyenXeData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<TuyenXe>> call = apiService.getDanhSachTuyenXe();

        call.enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TuyenXe> tuyenXeList = response.body();

                    // Gán vào RecyclerView
                    RouteAdapter adapter = new RouteAdapter(tuyenXeList);
                    recyclerViewRoutes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    recyclerViewRoutes.setAdapter(adapter);

                } else {
                    Toast.makeText(RouteManagementActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {
                Toast.makeText(RouteManagementActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}