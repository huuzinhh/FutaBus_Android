package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.R;

import java.util.List;
public class CityManagementAdapter extends BaseAdapter {
    private Context context;
    private List<TinhThanh> tinhThanhList;
    private OnTinhThanhActionListener listener;
    public interface OnTinhThanhActionListener {
        void onEdit(TinhThanh tinhThanh);
        void onDelete(TinhThanh tinhThanh);
    }
    public CityManagementAdapter(Context context, List<TinhThanh> tinhThanhList, OnTinhThanhActionListener listener) {
        this.context = context;
        this.tinhThanhList = tinhThanhList;
        this.listener = listener;
    }
    @Override
    public int getCount() {
        return tinhThanhList.size();
    }

    @Override
    public TinhThanh getItem(int position) {
        return tinhThanhList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tinhThanhList.get(position).getIdTinhThanh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_location_city_management, parent, false);
        }

        TinhThanh tinhThanh = getItem(position);
        TextView tvTinhThanhName = convertView.findViewById(R.id.tvTinhThanh);
        ImageView btnEditTinhThanh = convertView.findViewById(R.id.btnSua);
        ImageView btnDeleteTinhThanh = convertView.findViewById(R.id.btnXoa);

        tvTinhThanhName.setText(tinhThanh.getTenTinh());
        btnEditTinhThanh.setOnClickListener(v -> listener.onEdit(tinhThanh));
        btnDeleteTinhThanh.setOnClickListener(v -> listener.onDelete(tinhThanh));

        return convertView;
    }

    public void updateTinhThanhList(List<TinhThanh> newTinhThanhList) {
        this.tinhThanhList = newTinhThanhList;
        notifyDataSetChanged();
    }
}
