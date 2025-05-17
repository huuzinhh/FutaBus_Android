package com.example.futasbus.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.example.futasbus.Adapter.AdminHomeAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.AdminHomeModel;
import com.example.futasbus.model.NguoiDung;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminHomeFragment extends Fragment {

    private GridView gridView;
    private AdminHomeAdapter adminHomeAdapter;
    private List<AdminHomeModel> adminHomeList;
    private Map<String, Class<?>> activityMap;
    private FragmentTransaction transaction;
    private Fragment currentFragment;
    private Fragment newFragment;
    private LinearLayout sidebar;
    private ImageView iconMenu;

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

        sidebar = view.findViewById(R.id.sidebar);
        iconMenu = view.findViewById(R.id.iconMenu);

        iconMenu.setOnClickListener(v -> {
            if (sidebar.getVisibility() == View.GONE) {
                sidebar.setVisibility(View.VISIBLE);
            } else {
                sidebar.setVisibility(View.GONE);
            }
        });

        gridView.setOnTouchListener((v, event) -> {
            if (sidebar.getVisibility() == View.VISIBLE) {
                sidebar.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

        LinearLayout topBar = view.findViewById(R.id.topBar);

        topBar.setOnClickListener(v -> {
            if (sidebar.getVisibility() == View.VISIBLE) {
                sidebar.setVisibility(View.GONE);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);

        int idNguoiDung = sharedPreferences.getInt("idNguoiDung", -1);

        TextView tvUsername = view.findViewById(R.id.tvUsername);

        ApiService apiService = ApiClient.getApiService();
        apiService.getGeneralInformation(idNguoiDung).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NguoiDung nguoiDung = response.body();
                    tvUsername.setText(nguoiDung.getHoTen());
                } else {
                    Log.e("API", "Không lấy được thông tin người dùng");
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e("API", "Lỗi gọi API: " + t.getMessage());
            }
        });

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        SharedPreferences preferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        adminHomeAdapter = new AdminHomeAdapter(getActivity(), adminHomeList);
        gridView.setAdapter(adminHomeAdapter);
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedItem = adminHomeList.get(position).getName();

            switch (selectedItem) {
                case "Quản lý người dùng":
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    newFragment = new UserManagementFragment();
                    transaction.replace(R.id.admin_fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "Quản lý vé":
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    newFragment = new TicketManagementFragment();
                    transaction.replace(R.id.admin_fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "Quản lý tuyến xe":
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    newFragment = new BusRouteManagementFragment();
                    transaction.replace(R.id.admin_fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "Quản lý chuyến xe":
                    transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_container);

                    if (currentFragment != null) {
                        transaction.remove(currentFragment);
                    }

                    newFragment = new TripManagementFragment();
                    transaction.replace(R.id.admin_fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
                case "Quản lý bến xe":

                    break;
                case "Quản lý xe":

                    break;
                case "Quản lý địa điểm":

                    break;
                case "Thống kê":

                    break;
            }
        });

        return view;
    }
}