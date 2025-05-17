package com.example.futasbus.Activity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.model.NguoiDung;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class EditCustomerInfoFragment extends Fragment {
    private EditText tvHoTen, tvDiaChi, tvPhone;
    private TextView tvCCCD, tvEmail,NameError,DiaChiError,PhoneError;
    private Spinner spinnerNamSinh;
    private RadioGroup rgGioiTinh;
    private RadioButton rbNam, rbNu;
    private Button btnSave;
    private NguoiDung user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_customer_info, container, false);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        rgGioiTinh = view.findViewById(R.id.rgGioiTinh);
        rbNam = view.findViewById(R.id.rbNam);
        rbNu = view.findViewById(R.id.rbNu);
        spinnerNamSinh = view.findViewById(R.id.spinnerNamSinh);
        tvCCCD = view.findViewById(R.id.tvCCCD);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvPhone = view.findViewById(R.id.tvSoDienThoai);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnSave = view.findViewById(R.id.btnSave);
        NameError = view.findViewById(R.id.NameError);
        DiaChiError = view.findViewById(R.id.DiaChiError);
        PhoneError = view.findViewById(R.id.PhoneError);
        Bundle args = getArguments();
        if (args != null && args.containsKey("user_data")) {
            user = (NguoiDung) args.getSerializable("user_data");
            if (user != null) {
                tvHoTen.setText(user.getHoTen());
                if (user.isGioiTinh()) {
                    rbNu.setChecked(true);
                } else {
                    rbNam.setChecked(true);
                }
                setupNamSinhSpinner(spinnerNamSinh, user.getNamSinh());
                tvCCCD.setText(user.getCCCD());
                tvDiaChi.setText(user.getDiaChi());
                tvPhone.setText(user.getSoDienThoai());
                tvEmail.setText(user.getEmail());
            }
        }

        // Xử lý nút lưu
        btnSave.setOnClickListener(v -> Valid());

        return view;
    }

    private void setupNamSinhSpinner(Spinner spinner, int selectedYear) {
        List<Integer> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            years.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int position = years.indexOf(selectedYear);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    private void EditUserInfo() {
        if (user == null) return;

        NguoiDung editUser = new NguoiDung();
        editUser.setIdNguoiDung(user.getIdNguoiDung());
        editUser.setMatKhau(user.getMatKhau());
        editUser.setNgayDangKy(user.getNgayDangKy());
        editUser.setTrangThai(user.getTrangThai());
        editUser.setIdPhanQuyen(user.getIdPhanQuyen());

        editUser.setHoTen(tvHoTen.getText().toString().trim());
        editUser.setGioiTinh(rgGioiTinh.getCheckedRadioButtonId() == R.id.rbNu);
        editUser.setNamSinh((Integer) spinnerNamSinh.getSelectedItem());
        editUser.setCCCD(tvCCCD.getText().toString().trim());
        editUser.setDiaChi(tvDiaChi.getText().toString().trim());
        editUser.setSoDienThoai(tvPhone.getText().toString().trim());
        editUser.setEmail(tvEmail.getText().toString().trim());

        ApiService apiService = ApiClient.getApiService();
        apiService.updateUserInfo(editUser).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = (boolean) response.body().get("success");
                    String message = (String) response.body().get("message");
                    if (success) {
                        ToastHelper.show(requireContext(),message);
                        Log.d("EditUser", "Cập nhật thành công: " + message);
                        CustomerInfoFragment infoFragment = new CustomerInfoFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user_data", editUser);
                        infoFragment.setArguments(bundle);

                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.CustomerActivity, infoFragment)
                                .commit();
                    } else {
                        Toast.makeText(requireContext(), "Thất bại: " + message, Toast.LENGTH_LONG).show();
                        Log.e("EditUser", "Server trả về lỗi: " + message);
                    }
                } else {
                    Toast.makeText(requireContext(), "Lỗi phản hồi từ máy chủ.", Toast.LENGTH_LONG).show();
                    Log.e("EditUser", "Lỗi response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không thể kết nối máy chủ!", Toast.LENGTH_LONG).show();
                Log.e("EditUser", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    private void Valid(){
        boolean isValid = true;

        String name = tvHoTen.getText().toString().trim();
        if (name.isEmpty()) {
            NameError.setText("Tên không được để trống!");
            NameError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!name.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            NameError.setText("Tên chỉ được chứa chữ cái và khoảng trắng!");
            NameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Kiểm tra Số điện thoại
        String phone = tvPhone.getText().toString().trim();
       if (!phone.matches("^0[0-9]{9}$")) {
            PhoneError.setText("Số điện thoại không hợp lệ !");
            PhoneError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        String diachi = tvDiaChi.getText().toString().trim();
        if (diachi.isEmpty()) {
            DiaChiError.setText("Địa Chỉ không được để trống !");
            DiaChiError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (isValid) {
            EditUserInfo();
        }
    }
}
