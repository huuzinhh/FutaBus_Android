package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.BusRouteAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.TextView;
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
                    adapter = new BusRouteAdapter(requireContext(), tuyenXeList, new BusRouteAdapter.OnBusRouteActionListener() {
                        @Override
                        public void onView(TuyenXe tuyenXe) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_bus_route_detail, null);

                            ((TextView) view.findViewById(R.id.tvTenTuyen)).setText(tuyenXe.getTenTuyen());
                            ((TextView) view.findViewById(R.id.tvBenXeDi)).setText("" + tuyenXe.getBenXeDi());
                            ((TextView) view.findViewById(R.id.tvBenXeDen)).setText("" + tuyenXe.getBenXeDen());
                            ((TextView) view.findViewById(R.id.tvQuangDuong)).setText(tuyenXe.getQuangDuong() + " km");
                            ((TextView) view.findViewById(R.id.tvThoiGian)).setText(tuyenXe.getThoiGianDiChuyenTB() + " giờ");
                            ((TextView) view.findViewById(R.id.tvSoChuyenNgay)).setText(String.valueOf(tuyenXe.getSoChuyenTrongNgay()));
                            ((TextView) view.findViewById(R.id.tvSoNgayTuan)).setText(String.valueOf(tuyenXe.getSoNgayChayTrongTuan()));

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onEdit(TuyenXe tuyenXe) {
                            Toast.makeText(getContext(), "Sửa: " + tuyenXe.getTenTuyen(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDelete(TuyenXe tuyenXe) {
                            Toast.makeText(getContext(), "Xoá: " + tuyenXe.getTenTuyen(), Toast.LENGTH_SHORT).show();
//                            new android.app.AlertDialog.Builder(requireContext())
//                                    .setTitle("Xoá tuyến xe")
//                                    .setMessage("Bạn có chắc muốn xoá tuyến " + tuyenXe.getTenTuyen() + " không?")
//                                    .setPositiveButton("Xoá", (dialog, which) -> {
//                                        tuyenXeList.remove(tuyenXe);
//                                        adapter.notifyDataSetChanged();
//                                        Toast.makeText(getContext(), "Đã xoá " + tuyenXe.getTenTuyen(), Toast.LENGTH_SHORT).show();
//                                    })
//                                    .setNegativeButton("Huỷ", null)
//                                    .show();
                        }
                    });
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