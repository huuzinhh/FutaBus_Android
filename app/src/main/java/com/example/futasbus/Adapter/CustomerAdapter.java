package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.model.NguoiDung;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    private Context context;
    private List<NguoiDung> nguoiDungList;
    private CustomerAdapter.OnCustomerActionListener listener;

    public interface OnCustomerActionListener {
        void onView(NguoiDung nguoiDung);
        void onEdit(NguoiDung nguoiDung);
        void onDelete(NguoiDung nguoiDung);
    }

    public CustomerAdapter(Context context, List<NguoiDung> nguoiDungList, CustomerAdapter.OnCustomerActionListener listener) {
        this.context = context;
        this.nguoiDungList = nguoiDungList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return nguoiDungList.size();
    }

    @Override
    public Object getItem(int i) {
        return nguoiDungList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_user_management, parent, false);
        }

        TextView tvHoTen = convertView.findViewById(R.id.tv_ho_ten);
        TextView tvGioiTinh = convertView.findViewById(R.id.tv_gioi_tinh);
        TextView tvNamSinh = convertView.findViewById(R.id.tv_nam_sinh);
        TextView tvSoDienThoai = convertView.findViewById(R.id.tv_so_dien_thoai);
        TextView tvEmail = convertView.findViewById(R.id.tv_email);
        TextView tvDiaChi = convertView.findViewById(R.id.tv_dia_chi);
        ImageView btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete);
        ImageView btnView = convertView.findViewById(R.id.btn_see);

        NguoiDung nguoiDung = nguoiDungList.get(i);

        tvHoTen.setText("Họ tên: " + nguoiDung.getHoTen());
        tvGioiTinh.setText("Giới tính: " + (nguoiDung.isGioiTinh() ? "Nữ" : "Nam"));
        tvNamSinh.setText("Năm sinh: " + nguoiDung.getNamSinh());
        tvSoDienThoai.setText("SĐT: " + nguoiDung.getSoDienThoai());
        tvEmail.setText("Email: " + nguoiDung.getEmail());
        tvDiaChi.setText("Địa chỉ: " + nguoiDung.getDiaChi());

        btnView.setOnClickListener(v -> {
            if (listener != null) listener.onView(nguoiDung);
        });

        btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(nguoiDung);
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(nguoiDung);
        });

        return convertView;
    }
}

