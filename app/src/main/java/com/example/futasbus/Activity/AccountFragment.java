package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.model.NguoiDung;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private LinearLayout logoutLayout, ll_notification, ll_sharefriend, ll_changepassword;
    private ImageButton btnNotification, btnSetting, btnShareWithFriend, btnChangePassword, btnLogout;
    private ConstraintLayout accountInfo;
    private TextView tv_username, tv_phonenumber, tv_login;
    private LinearLayout ll_info;
    private NguoiDung user;
    private FragmentTransaction transaction;
    private Fragment currentFragment;
    private Fragment newFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        logoutLayout = rootView.findViewById(R.id.logout);
        accountInfo = rootView.findViewById(R.id.accountInfo);
        tv_username = rootView.findViewById(R.id.tv_username);
        tv_phonenumber = rootView.findViewById(R.id.tv_phonenumber);
        tv_login = rootView.findViewById(R.id.tv_Login);
        ll_info = rootView.findViewById(R.id.ll_info);

        ll_notification = rootView.findViewById(R.id.ll_notification);
        ll_changepassword = rootView.findViewById(R.id.ll_ChangePassword);
        ll_sharefriend = rootView.findViewById(R.id.ll_ShareFriend);

        btnNotification = rootView.findViewById(R.id.btnNotification);
        btnShareWithFriend = rootView.findViewById(R.id.btnsharewithfriend);
        btnChangePassword = rootView.findViewById(R.id.btnChangePassword);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        btnSetting = rootView.findViewById(R.id.btnInfoUser); // nếu có thêm nút chỉnh sửa info

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

        // Listener chung cho notification
        View.OnClickListener notificationClickListener = view -> {
            ToastHelper.show(requireContext(), "Chức năng thông báo đang phát triển");
        };
        ll_notification.setOnClickListener(notificationClickListener);
        if (btnNotification != null) btnNotification.setOnClickListener(notificationClickListener);

        // Listener chung cho share with friend
        View.OnClickListener shareClickListener = view -> {
            ToastHelper.show(requireContext(), "Chia sẻ với bạn bè chưa khả dụng");
        };
        ll_sharefriend.setOnClickListener(shareClickListener);
        if (btnShareWithFriend != null) btnShareWithFriend.setOnClickListener(shareClickListener);

        View.OnClickListener changePasswordClickListener = view -> {
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.account_fragment_container);

            if (currentFragment != null) {
                transaction.remove(currentFragment);
            }

            newFragment = new ChangePassCustomerFragment();
            transaction.replace(R.id.account_fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
            ll_notification.setVisibility(View.GONE);
            ll_changepassword.setVisibility(View.GONE);
            ll_sharefriend.setVisibility(View.GONE);
        } else {
            tv_login.setVisibility(View.GONE);
            ll_info.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            ll_notification.setVisibility(View.VISIBLE);
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
            boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
            String hoTen = preferences.getString("hoTen", "");
            preferences.edit().clear().apply();

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
            if (account != null) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
                googleSignInClient.signOut().addOnCompleteListener(task -> {
                    moveToMain();
                });
            } else {
                moveToMain();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void moveToMain() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ToastHelper.show(requireContext(), "Đăng Xuất Thành Công !");
    }
}
