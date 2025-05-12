package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.futasbus.R;
import com.example.futasbus.model.TuyenXe;
import java.util.List;

public class BusRouteAdapter extends BaseAdapter {
    private Context context;
    private List<TuyenXe> tuyenXeList;

    public BusRouteAdapter(Context context, List<TuyenXe> tuyenXeList) {
        this.context = context;
        this.tuyenXeList = tuyenXeList;
    }

    @Override
    public int getCount() {
        return tuyenXeList.size();
    }

    @Override
    public Object getItem(int i) {
        return tuyenXeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_bus_route_management, parent, false);
        }

        TextView tvTenTuyen = convertView.findViewById(R.id.tv_ten_tuyen);
        TextView tvBenDi = convertView.findViewById(R.id.tv_ben_di);
        TextView tvBenDen = convertView.findViewById(R.id.tv_ben_den);
        TextView tvQuangDuong = convertView.findViewById(R.id.tv_quang_duong);
        TextView tvThoiGian = convertView.findViewById(R.id.tv_thoi_gian);
        ImageView btnView = convertView.findViewById(R.id.btn_view);
        ImageView btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete);

        TuyenXe tuyenXe = tuyenXeList.get(i);

        tvTenTuyen.setText("Tuyến: " + tuyenXe.getTenTuyen());
        tvBenDi.setText("Bến xe đi: " + tuyenXe.getBenXeDi());
        tvBenDen.setText("Bến xe đến: " + tuyenXe.getBenXeDen());
        tvQuangDuong.setText("Quãng đường: " + tuyenXe.getQuangDuong() + " km");
        tvThoiGian.setText("Thời gian: " + tuyenXe.getThoiGianDiChuyenTB() + " giờ");

        btnView.setOnClickListener(v -> {
            // Hiển thị chi tiết tuyến xe
        });

        btnEdit.setOnClickListener(v -> {
            // Mở màn hình/sửa tuyến xe
        });

        btnDelete.setOnClickListener(v -> {
            // Hiển thị dialog xác nhận xóa tuyến xe
        });

        return convertView;
    }
}
