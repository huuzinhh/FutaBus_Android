package com.example.futasbus.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.NguoiDung;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassCustomerFragment extends Fragment {

    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editConfirmPassword;
    private ImageView iconBack;
    private Button btnEdit;
    private NguoiDung user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass_customer, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        editOldPassword = view.findViewById(R.id.editOldPassword);
        editNewPassword = view.findViewById(R.id.editNewPassword);
        editConfirmPassword = view.findViewById(R.id.editConfirmPassword);

        setupTogglePasswordVisibility(editOldPassword);
        setupTogglePasswordVisibility(editNewPassword);
        setupTogglePasswordVisibility(editConfirmPassword);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int idNguoiDung = sharedPreferences.getInt("idNguoiDung", -1);

        ApiService apiService = ApiClient.getApiService();
        apiService.getGeneralInformation(idNguoiDung).enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();

                    String password = user.getMatKhau();
                    int idNguoiDung = user.getIdNguoiDung();

                    Log.d("API", "Id người dùng: " + idNguoiDung);
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Log.e("API", "Lỗi gọi API: " + t.getMessage());
            }
        });

        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            String oldPassword = editOldPassword.getText().toString().trim();
            String newPassword = editNewPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = user.getMatKhau();
            if (!oldPassword.equals(password)) {
                Toast.makeText(getContext(), "Mật khẩu cũ chưa đúng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu nhập lại chưa đúng!", Toast.LENGTH_SHORT).show();
                return;
            }

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Xác nhận đổi mật khẩu")
                    .setMessage("Bạn có chắc chắn muốn đổi mật khẩu?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        NguoiDung nguoiDung = new NguoiDung();
                        nguoiDung.setIdNguoiDung(user.getIdNguoiDung());
                        nguoiDung.setMatKhau(newPassword);

                        apiService.updatePassword(nguoiDung).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                                    user.setMatKhau(newPassword);

                                    editOldPassword.setText("");
                                    editNewPassword.setText("");
                                    editConfirmPassword.setText("");
                                } else {
                                    Toast.makeText(getContext(), "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi kết nối đến server!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupTogglePasswordVisibility(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight()
                            - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
                        } else {
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0);
                        }

                        editText.setSelection(editText.length());
                        v.performClick();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}

