package com.example.futasbus.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.model.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerInfoFragment extends Fragment {
    private TextView tvHoTen, tvGioiTinh, tvNgaySinh, tvCCCD, tvDiaChi, tvPhone, tvEmail;
    private Button btnEit;
    private NguoiDung user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);

        // Ánh xạ view
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvGioiTinh = view.findViewById(R.id.tvGioiTinh);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvCCCD = view.findViewById(R.id.tvCCCD);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvPhone = view.findViewById(R.id.tvSoDienThoai);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnEit = view.findViewById(R.id.btnEdit);
        btnEit.setOnClickListener(v -> {
            EditCustomerInfoFragment editCustomerInfoFragment = new EditCustomerInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_data", user);
            editCustomerInfoFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.CustomerActivity, editCustomerInfoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
    private void fetchUserInfo() {
        int userId = SharedPrefHelper.getUserId(requireContext());
        if (userId == -1) return;

        ApiService apiService = ApiClient.getApiService();
        apiService.getGeneralInformation(userId).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    tvHoTen.setText(user.getHoTen());
                    tvGioiTinh.setText(user.isGioiTinh() ? "Nữ" : "Nam");
                    tvNgaySinh.setText(String.valueOf(user.getNamSinh()));
                    tvCCCD.setText(user.getCCCD());
                    tvDiaChi.setText(user.getDiaChi());
                    tvPhone.setText(user.getSoDienThoai());
                    tvEmail.setText(user.getEmail());
                } else {
                    Log.e("API", "Không lấy được thông tin người dùng");
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e("API", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchUserInfo();
    }
}