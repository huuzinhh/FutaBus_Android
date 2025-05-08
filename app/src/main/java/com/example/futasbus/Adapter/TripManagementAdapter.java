package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXe;


import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TripManagementAdapter extends RecyclerView.Adapter<TripManagementAdapter.TripViewHolder> {

    private List<ChuyenXe> tripList;
    private Context context;

    public TripManagementAdapter(List<ChuyenXe> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip_managent, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ChuyenXe chuyenXe = tripList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        holder.tvTuyenXe.setText("Tuyến " + chuyenXe.getTuyenXe().getTenTuyen());
        holder.tvThoiGian.setText("Từ " + sdf.format(chuyenXe.getThoiDiemDi()) + " đến " + sdf.format(chuyenXe.getThoiDiemDen()));
        holder.tvGiaVe.setText("Giá vé: " + chuyenXe.getGiaVe() + "đ");

        holder.btnSua.setOnClickListener(v -> {
            Toast.makeText(context, "Sửa chuyến " + chuyenXe.getIdChuyenXe(), Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình sửa chuyến xe
        });

        holder.btnXoa.setOnClickListener(v -> {
            Toast.makeText(context, "Xóa chuyến " + chuyenXe.getIdChuyenXe(), Toast.LENGTH_SHORT).show();
            // TODO: Gọi API xóa chuyến
        });
    }

    @Override
    public int getItemCount() {
        return tripList != null ? tripList.size() : 0;
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tvTuyenXe, tvThoiGian, tvGiaVe;
        Button btnSua, btnXoa;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTuyenXe = itemView.findViewById(R.id.tvTuyenXe);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvGiaVe = itemView.findViewById(R.id.tvGiaVe);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}