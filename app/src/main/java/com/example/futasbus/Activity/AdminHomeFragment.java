package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.example.futasbus.Adapter.AdminHomeAdapter;
import com.example.futasbus.R;
import com.example.futasbus.model.AdminHomeModel;

import java.util.ArrayList;
import java.util.List;


public class AdminHomeFragment extends Fragment {

    private GridView gridView;
    private AdminHomeAdapter adminHomeAdapter;
    private List<AdminHomeModel> adminHomeList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        gridView = view.findViewById(R.id.gridViewAdminHome);
        adminHomeList = new ArrayList<>();


        adminHomeList.add(new AdminHomeModel("Tuyến xe", R.drawable.ic_bell));
        adminHomeList.add(new AdminHomeModel("Chuyến xe", R.drawable.ic_bell));
        adminHomeList.add(new AdminHomeModel("Xe", R.drawable.ic_bell));
        adminHomeList.add(new AdminHomeModel("Địa điểm",R.drawable.ic_bell));
        adminHomeList.add(new AdminHomeModel("Hóa đơn",R.drawable.ic_bell));
        adminHomeList.add(new AdminHomeModel("Tỉnh/thành",R.drawable.ic_bell));

        adminHomeAdapter = new AdminHomeAdapter(getActivity(), adminHomeList);
        gridView.setAdapter(adminHomeAdapter);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedItem = adminHomeList.get(position).getName();

            switch (selectedItem) {
                case "Tuyến xe":
                    startActivity(new Intent(getActivity(), RouteManagementActivity.class));
                    break;
                case "Chuyến xe":
                    startActivity(new Intent(getActivity(), TripManagementActivity.class));
                    break;
                case "Xe":
                    startActivity(new Intent(getActivity(), VehicleManagementActivity.class));
                    break;
                case "Địa điểm":
                    startActivity(new Intent(getActivity(), LocationManagementActivity.class));
                    break;
                case "Hóa đơn":
                    startActivity(new Intent(getActivity(), InvoiceActivity.class));
                    break;
            }
        });

        return view;
    }

}