package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.BusRouteAdapter;
import com.example.futasbus.Adapter.TripManagementAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.QuanHuyen;
import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.model.TuyenXeUpdateDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TripManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewBusTrip;
    private List<ChuyenXe> ChuyenXeList;
    private TripManagementAdapter adapter;



    public TripManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        gridViewBusTrip = view.findViewById(R.id.gridViewBusTrip);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);



        apiService.getDanhSachChuyenXe().enqueue(new Callback<List<ChuyenXe>>() {
            @Override
            public void onResponse(Call<List<ChuyenXe>> call, Response<List<ChuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChuyenXeList = response.body();
                    adapter = new TripManagementAdapter(requireContext(), ChuyenXeList, new TripManagementAdapter.OnBusTripActionListener() {
                        @Override
                        public void onView(ChuyenXe chuyenXe) {

                        }

                        @Override
                        public void onEdit(ChuyenXe chuyenXe) {

                        }

                        @Override
                        public void onDelete(ChuyenXe chuyenXe) {
                        }


                    });
                    gridViewBusTrip.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

        return view;
    }
}