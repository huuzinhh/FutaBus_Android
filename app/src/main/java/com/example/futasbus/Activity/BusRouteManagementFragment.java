package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.BusRouteAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.TuyenXe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class BusRouteManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewBusRoute;
    private List<TuyenXe> tuyenXeList;
    private BusRouteAdapter adapter;

    public BusRouteManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busroute_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        gridViewBusRoute = view.findViewById(R.id.gridViewBusRoute);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllTuyenXe().enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tuyenXeList = response.body();
                    adapter = new BusRouteAdapter(requireContext(), tuyenXeList);
                    gridViewBusRoute.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}