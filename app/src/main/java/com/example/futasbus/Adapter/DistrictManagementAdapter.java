package com.example.futasbus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futasbus.R;
import com.example.futasbus.model.QuanHuyen;

import java.util.ArrayList;
import java.util.List;

public class DistrictManagementAdapter extends RecyclerView.Adapter<DistrictManagementAdapter.QuanHuyenViewHolder>{
    private List<QuanHuyen> quanHuyenList;
    private OnQuanHuyenActionListener listener;

    public interface OnQuanHuyenActionListener {
        void onEdit(QuanHuyen quanHuyen);
        void onDelete(QuanHuyen quanHuyen);
    }

    public DistrictManagementAdapter(List<QuanHuyen> quanHuyenList, OnQuanHuyenActionListener listener) {
        this.quanHuyenList = quanHuyenList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuanHuyenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_district_management, parent, false);
        return new QuanHuyenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuanHuyenViewHolder holder, int position) {
        QuanHuyen quanHuyen = quanHuyenList.get(position);
        holder.tvQuanHuyenName.setText(quanHuyen.getTenQuanHuyen());
        holder.btnEditQuanHuyen.setOnClickListener(v -> listener.onEdit(quanHuyen));
        holder.btnDeleteQuanHuyen.setOnClickListener(v -> listener.onDelete(quanHuyen));
    }

    @Override
    public int getItemCount() {
        return quanHuyenList.size();
    }

    public void updateQuanHuyenList(List<QuanHuyen> newQuanHuyenList) {
        this.quanHuyenList = new ArrayList<>(newQuanHuyenList);
        notifyDataSetChanged();
    }

    static class QuanHuyenViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuanHuyenName;
        ImageView btnEditQuanHuyen;
        ImageView btnDeleteQuanHuyen;

        public QuanHuyenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuanHuyenName = itemView.findViewById(R.id.tvQuanHuyen);
            btnEditQuanHuyen = itemView.findViewById(R.id.btnSua);
            btnDeleteQuanHuyen = itemView.findViewById(R.id.btnXoa);
        }
    }
}
