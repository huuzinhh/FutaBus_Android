package com.example.futasbus.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.futasbus.helper.ToastHelper;


import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.NguoiDung;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAccountFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editFullName;
    private Spinner spinnerGender;
    private EditText editBirthDay;
    private EditText editPhone;
    private EditText editEmail;
    private EditText editAddress;
    private NguoiDung user;
    private Button btnEdit;
    private Button btnCancel;
    private Button btnUpdate;
    private LinearLayout sidebar;
    private ImageView iconMenu;
    private FragmentTransaction transaction;
    private Fragment currentFragment;
    private Fragment newFragment;

    public AdminAccountFragment() {

    }

    public static AdminAccountFragment newInstance(String param1, String param2) {
        AdminAccountFragment fragment = new AdminAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_account, container, false);

        editFullName = view.findViewById(R.id.editFullName);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        editBirthDay = view.findViewById(R.id.editBirthDay);
        editPhone = view.findViewById(R.id.editPhone);
        editEmail = view.findViewById(R.id.editEmail);
        editAddress = view.findViewById(R.id.editAddress);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        sidebar = view.findViewById(R.id.sidebar);
        iconMenu = view.findViewById(R.id.iconMenu);

        iconMenu.setOnClickListener(v -> {
            if (sidebar.getVisibility() == View.GONE) {
                sidebar.setVisibility(View.VISIBLE);
            } else {
                sidebar.setVisibility(View.GONE);
            }
        });

        LinearLayout topBar = view.findViewById(R.id.topBar);

        topBar.setOnClickListener(v -> {
            if (sidebar.getVisibility() == View.VISIBLE) {
                sidebar.setVisibility(View.GONE);
            }
        });



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        int idNguoiDung = sharedPreferences.getInt("idNguoiDung", -1);

        TextView tvUsername = view.findViewById(R.id.tvUsername);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                Arrays.asList("Nam", "Nữ"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnTouchListener((v, event) -> true);

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

        TextView btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            sidebar.setVisibility(View.GONE);

            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.admin_fragment_account);

            if (currentFragment != null) {
                transaction.remove(currentFragment);
            }

            newFragment = new ChangePasswordFragment();
            transaction.replace(R.id.admin_fragment_account, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        ApiService apiService = ApiClient.getApiService();
        apiService.getGeneralInformation(idNguoiDung).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    editFullName.setText(user.getHoTen());
                    if (user.isGioiTinh()) {
                        spinnerGender.setSelection(1);
                    } else {
                        spinnerGender.setSelection(0);
                    }
                    editBirthDay.setText(String.valueOf(user.getNamSinh()));
                    editAddress.setText(user.getDiaChi());
                    editPhone.setText(user.getSoDienThoai());
                    editEmail.setText(user.getEmail());

                    tvUsername.setText(user.getHoTen());
                } else {
                    Log.e("API", "Không lấy được thông tin người dùng");
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e("API", "Lỗi gọi API: " + t.getMessage());
            }
        });

        btnEdit.setOnClickListener(v -> {
            editFullName.setEnabled(true);
            editBirthDay.setEnabled(true);
            editPhone.setEnabled(true);
            editEmail.setEnabled(true);
            editAddress.setEnabled(true);
            spinnerGender.setEnabled(true);
            spinnerGender.setClickable(true);
            spinnerGender.setOnTouchListener(null);

            btnEdit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> {
            if (user != null) {
                editFullName.setText(user.getHoTen());
                if (user.isGioiTinh()) {
                    spinnerGender.setSelection(1);
                } else {
                    spinnerGender.setSelection(0);
                }
                editBirthDay.setText(String.valueOf(user.getNamSinh()));
                editAddress.setText(user.getDiaChi());
                editPhone.setText(user.getSoDienThoai());
                editEmail.setText(user.getEmail());
            }

            editFullName.setEnabled(false);
            editBirthDay.setEnabled(false);
            editPhone.setEnabled(false);
            editEmail.setEnabled(false);
            editAddress.setEnabled(false);
            spinnerGender.setEnabled(false);

            btnCancel.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        });

        btnUpdate.setOnClickListener(v -> {
            String fullName = editFullName.getText().toString();
            String birthDay = editBirthDay.getText().toString();
            String phone = editPhone.getText().toString();
            String email = editEmail.getText().toString();
            String address = editAddress.getText().toString();
            String gender = spinnerGender.getSelectedItem().toString();
            int birthYear = Integer.parseInt(birthDay);
            boolean genderUser = gender.equals("Nữ");

            if (fullName.isEmpty() || birthDay.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                ToastHelper.show(getContext(), "Vui lòng điền đầy đủ thông tin.");
                return;
            }

            try {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (currentYear - birthYear < 16) {
                    ToastHelper.show(getContext(), "Bạn phải ít nhất 16 tuổi.");
                    return;
                }
            } catch (NumberFormatException e) {
                ToastHelper.show(getContext(), "Ngày sinh không hợp lệ.");
                return;
            }

            if (phone.length() != 10 || !phone.matches("\\d{10}")) {
                ToastHelper.show(getContext(), "Số điện thoại có 10 chữ số.");
                return;
            }

            if (!email.endsWith("@gmail.com")) {
                ToastHelper.show(getContext(), "Email có đuôi @gmail.com.");
                return;
            }

            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setIdNguoiDung(idNguoiDung);
            nguoiDung.setHoTen(fullName);
            nguoiDung.setNamSinh(birthYear);
            nguoiDung.setSoDienThoai(phone);
            nguoiDung.setEmail(email);
            nguoiDung.setDiaChi(address);
            nguoiDung.setGioiTinh(genderUser);

            apiService.updateUserInfo(nguoiDung).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Cập nhật thành công");
                        ToastHelper.show(getContext(), "Cập nhật thành công");
                    } else {
                        Log.e("API", "Cập nhật thất bại: " + response.message());
                        ToastHelper.show(getContext(), "Cập nhật thất bại");
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Log.e("API", "Lỗi khi gọi API: " + t.getMessage());
                    ToastHelper.show(getContext(), "Lỗi khi gọi API");
                }
            });

            editFullName.setEnabled(false);
            editBirthDay.setEnabled(false);
            editPhone.setEnabled(false);
            editEmail.setEnabled(false);
            editAddress.setEnabled(false);
            spinnerGender.setEnabled(false);

            btnCancel.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        });

        return view;
    }
}