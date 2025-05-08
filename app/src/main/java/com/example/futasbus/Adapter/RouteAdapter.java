package com.example.futasbus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futasbus.R;
import com.example.futasbus.model.TuyenXe;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.TuyenXeViewHolder> {
    private List<TuyenXe> tuyenXeList;

    public RouteAdapter(List<TuyenXe> tuyenXeList) {
        this.tuyenXeList = tuyenXeList;
    }

    @NonNull
    @Override
    public TuyenXeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new TuyenXeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TuyenXeViewHolder holder, int position) {
        TuyenXe tuyenXe = tuyenXeList.get(position);
        holder.txtTenTuyen.setText(tuyenXe.getTenTuyen());
        holder.txtQuangDuong.setText("Quãng đường: " + tuyenXe.getQuangDuong() + " km");
        holder.txtGia.setText("Giá: " + String.format("%,.0f đ", tuyenXe.getGiaHienHanh()));
    }

    @Override
    public int getItemCount() {
        return tuyenXeList.size();
    }

    public static class TuyenXeViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenTuyen, txtQuangDuong, txtGia;

        public TuyenXeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenTuyen = itemView.findViewById(R.id.txtTenTuyen);
            txtQuangDuong = itemView.findViewById(R.id.txtQuangDuong);
            txtGia = itemView.findViewById(R.id.txtGia);
        }
    }
}
