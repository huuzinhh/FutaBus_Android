package com.example.futasbus.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.futasbus.model.BenXe;

public class LocationManagementAdapter extends BaseAdapter {
    public interface OnLocationActionListener {
        void onView(BenXe benXe );

        void onEdit(BenXe benXe );

        void onDelete(BenXe benXe );
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
