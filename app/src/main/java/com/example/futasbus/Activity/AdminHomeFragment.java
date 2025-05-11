package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

import com.example.futasbus.Adapter.AdminHomeAdapter;
import com.example.futasbus.R;
import com.example.futasbus.model.AdminHomeModel;

import java.util.ArrayList;
import java.util.List;


public class AdminHomeFragment extends Fragment {

    private GridView gridView;
    private AdminHomeAdapter adminHomeAdapter;
    private List<AdminHomeModel> adminHomeList;
    private Map<String, Class<?>> activityMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        gridView = view.findViewById(R.id.gridViewAdminHome);
        adminHomeList = new ArrayList<>();

        adminHomeList.add(new AdminHomeModel("Quản lý người dùng", R.drawable.ic_customer));
        adminHomeList.add(new AdminHomeModel("Quản lý vé", R.drawable.ic_ticket_bus));
        adminHomeList.add(new AdminHomeModel("Quản lý tuyến xe", R.drawable.ic_bus_route));
        adminHomeList.add(new AdminHomeModel("Quản lý chuyến xe", R.drawable.ic_bus_trip));
        adminHomeList.add(new AdminHomeModel("Quản lý bến xe", R.drawable.ic_bus_station));
        adminHomeList.add(new AdminHomeModel("Quản lý xe",R.drawable.ic_bus));
        adminHomeList.add(new AdminHomeModel("Quản lý địa điểm",R.drawable.ic_location));
        adminHomeList.add(new AdminHomeModel("Thống kê",R.drawable.ic_statistic));

        adminHomeAdapter = new AdminHomeAdapter(getActivity(), adminHomeList);
        gridView.setAdapter(adminHomeAdapter);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedItem = adminHomeList.get(position).getName();

            switch (selectedItem) {
                case "Quản lý người dùng":
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    Fragment newFragment = new UserManagementFragment();
                    transaction.replace(R.id.admin_fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "Quản lý vé":
                    //startActivity(new Intent(getActivity(), UserManagementActivity.class));
                    break;
                case "Quản lý tuyến xe":
                    //startActivity(new Intent(getActivity(), VehicleManagementActivity.class));
                    break;
                case "Quản lý chuyến xe":
                    //startActivity(new Intent(getActivity(), LocationManagementActivity.class));
                    break;
                case "Quản lý bến xe":
                    //startActivity(new Intent(getActivity(), VehicleManagementActivity.class));
                    break;
                case "Quản lý xe":
                    //startActivity(new Intent(getActivity(), InvoiceActivity.class));
                    break;
                case "Quản lý địa điểm":
                    //startActivity(new Intent(getActivity(), InvoiceActivity.class));
                    break;
                case "Thống kê":
                    //startActivity(new Intent(getActivity(), InvoiceActivity.class));
                    break;
            }
        });

        return view;
    }

}