package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.model.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private LinearLayout logoutLayout, ll_darkmode, ll_sharefriend, ll_changepassword;
    private ImageButton btnNotification, btnSetting, btnShareWithFriend, btnChangePassword, btnLogout;
    private ConstraintLayout accountInfo;
    private TextView tv_username, tv_phonenumber, tv_login;
    private LinearLayout ll_info;
    private NguoiDung user;
    private Switch switcher;
    private boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        // Ánh xạ view
        logoutLayout = rootView.findViewById(R.id.logout);
        accountInfo = rootView.findViewById(R.id.accountInfo);
        tv_username = rootView.findViewById(R.id.tv_username);
        tv_phonenumber = rootView.findViewById(R.id.tv_phonenumber);
        ll_darkmode = rootView.findViewById(R.id.ll_darkmode);
        tv_login = rootView.findViewById(R.id.tv_Login);
        ll_info = rootView.findViewById(R.id.ll_info);
        ll_changepassword = rootView.findViewById(R.id.ll_ChangePassword);
        ll_sharefriend = rootView.findViewById(R.id.ll_ShareFriend);
        btnShareWithFriend = rootView.findViewById(R.id.btnsharewithfriend);
        btnChangePassword = rootView.findViewById(R.id.btnChangePassword);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        btnSetting = rootView.findViewById(R.id.btnInfoUser);
        switcher = rootView.findViewById(R.id.switch_dark_mode);

        sharedPreferences = requireActivity().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode = sharedPreferences.getBoolean("night", false);
        switcher.setChecked(nightmode);

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                if (nightmode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("night", false);
                    nightmode = false; // cập nhật lại biến
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("night", true);
                    nightmode = true; // cập nhật lại biến
                }
                editor.apply();
            }
        });
        // Listener chung cho account info
        View.OnClickListener accountClickListener = view -> {
            if (user != null) {
                Intent intent = new Intent(getActivity(), CustomerActivity.class);
                intent.putExtra("user_data", user);
                startActivity(intent);
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        };
        accountInfo.setOnClickListener(accountClickListener);
        if (btnSetting != null) btnSetting.setOnClickListener(accountClickListener);

        // Listener chung cho logout
        View.OnClickListener logoutClickListener = view -> Logout();
        logoutLayout.setOnClickListener(logoutClickListener);
        if (btnLogout != null) btnLogout.setOnClickListener(logoutClickListener);

        // Listener chung cho share with friend
        View.OnClickListener shareClickListener = view -> {
            ToastHelper.show(requireContext(), "Chia sẻ với bạn bè chưa khả dụng");
        };
        ll_sharefriend.setOnClickListener(shareClickListener);
        if (btnShareWithFriend != null) btnShareWithFriend.setOnClickListener(shareClickListener);

        // Listener chung cho change password
        View.OnClickListener changePasswordClickListener = view -> {
            ToastHelper.show(requireContext(), "Thay đổi mật khẩu đang phát triển");
        };
        ll_changepassword.setOnClickListener(changePasswordClickListener);
        if (btnChangePassword != null) btnChangePassword.setOnClickListener(changePasswordClickListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleLoginUIState();
    }

    private void handleLoginUIState() {
        int userId = SharedPrefHelper.getUserId(requireContext());
        boolean isLoggedIn = userId != -1;

        if (!isLoggedIn) {
            tv_login.setVisibility(View.VISIBLE);
            ll_info.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.GONE);
            ll_darkmode.setVisibility(View.GONE);
            ll_changepassword.setVisibility(View.GONE);
            ll_sharefriend.setVisibility(View.GONE);
        } else {
            tv_login.setVisibility(View.GONE);
            ll_info.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            ll_darkmode.setVisibility(View.VISIBLE);
            ll_changepassword.setVisibility(View.VISIBLE);
            ll_sharefriend.setVisibility(View.VISIBLE);

            fetchUserInfo();
        }
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
                    tv_username.setText(user.getHoTen());
                    tv_phonenumber.setText(user.getSoDienThoai());
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

    private void Logout() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_logout, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(cancelView -> dialog.dismiss());
        dialogView.findViewById(R.id.btnLogout).setOnClickListener(logoutView -> {
            SharedPreferences preferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ToastHelper.show(requireContext(), "Đăng Xuất Thành Công !");
            dialog.dismiss();
        });

        dialog.show();
    }

}
