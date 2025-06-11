package com.example.futasbus.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.Xe;

import java.util.List;
import java.util.Locale;


public class BusManagementAdapter extends BaseAdapter {

    private Context context;
    private List<Xe> xeList;
    private List<Xe> originalList;
    private OnBusActionListener listener;
    public List<Xe> getXeList() {
        return xeList;
    }

    public interface OnBusActionListener {
        void onView(Xe xe );

        void onEdit(Xe xe );

        void onDelete(Xe xe );
    }

    public BusManagementAdapter(Context context, List<Xe> xeList, OnBusActionListener listener) {
        this.context = context;
        this.xeList = xeList;
        this.originalList = List.copyOf(xeList);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return xeList.size();
    }

    @Override
    public Object getItem(int i) {
        return xeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_bus_managent, parent, false);
        }
        TextView tvTenXe = convertView.findViewById(R.id.tvTenXe);
        TextView tvBienSo = convertView.findViewById(R.id.tvBienSo);
        TextView tvLoaiXe = convertView.findViewById(R.id.tvLoaiXe);
        Xe xe  = xeList.get(i);

        tvTenXe.setText(xe.getTenXe());
        tvBienSo.setText("Biển số : "+xe.getBienSo());
        tvLoaiXe.setText("Loại : " +xe.getLoaiXe().getTenLoai() +" - "+xe.getLoaiXe().getSoGhe());

        ImageView btnEdit = convertView.findViewById(R.id.btnSua);
        ImageView btnDelete = convertView.findViewById(R.id.btnXoa);
// Gọi hàm sửa
        btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(xe);
            }
        });

// Gọi hàm xóa với confirm
        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa xe này?")
                        .setPositiveButton("Xóa", (dialog, which) -> listener.onDelete(xe))
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        return convertView;
    }

    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault()).trim();
        xeList.clear(); // Giả sử bạn có List<Xe> xeList và originalList

        if (query.isEmpty()) {
            xeList.addAll(originalList);
        } else {
            for (Xe xe : originalList) {
                String idXe = String.valueOf(xe.getIdXe());
                String bienSo = safeLower(xe.getBienSo());
                String tenXe = safeLower(xe.getTenXe());
                String tenLoaiXe = xe.getLoaiXe() != null ? safeLower(xe.getLoaiXe().getTenLoai()) : "";

                if (idXe.contains(query) || bienSo.contains(query) || tenXe.contains(query) || tenLoaiXe.contains(query)) {
                    xeList.add(xe);
                }
            }
        }

        notifyDataSetChanged();
    }



    // Hàm hỗ trợ để tránh lỗi null
    private String safeLower(String input) {
        return input == null ? "" : input.toLowerCase(Locale.getDefault()).trim();
    }
}
