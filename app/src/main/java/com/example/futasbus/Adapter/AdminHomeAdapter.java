package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.model.AdminHomeModel;

import java.util.List;

public class AdminHomeAdapter extends BaseAdapter {
    private Context context;
    private List<AdminHomeModel> adminHomeList;
    public AdminHomeAdapter(Context context, List<AdminHomeModel> adminHomeList) {
        this.context = context;
        this.adminHomeList = adminHomeList;
    }

    @Override
    public int getCount() {
        return adminHomeList.size();
    }

    @Override
    public Object getItem(int position) {
        return adminHomeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gird_item_admin_home, parent, false);
        }

        ImageView img = convertView.findViewById(R.id.img);
        TextView name = convertView.findViewById(R.id.tv_name);


        AdminHomeModel adminHomeModel = adminHomeList.get(position);
        img.setImageResource(adminHomeModel.getImageResource());
        name.setText(adminHomeModel.getName());

        return convertView;
    }
}
