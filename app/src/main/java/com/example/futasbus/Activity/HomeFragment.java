package com.example.futasbus.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.helper.SharedPrefHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.ApiClient;
import com.example.futasbus.R;
import com.example.futasbus.helper.ToastHelper;
import com.example.futasbus.respone.TinhThanhResponse;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.TinhThanh;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView tvUsername,tvLogin;
    private ImageButton avata;
    private Spinner spinnerDiemDi, spinnerDiemDen, spinnerSoVe;
    private EditText editTextNgayDi, editTextNgayVe;
    private Switch switchKhuHoi;
    private Button btnTimTuyen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchTinhThanhData();
        updateUserInfo();
    }

    private void initViews(View view) {
        ImageButton btnSwap = view.findViewById(R.id.btnSwap);
        btnSwap.setOnClickListener(v -> swapSpinners());
        LinearLayout returnCheck = view.findViewById(R.id.returnCheck);
        spinnerDiemDi = view.findViewById(R.id.spinnerDiemDi);
        spinnerDiemDen = view.findViewById(R.id.spinnerDiemDen);
        spinnerSoVe = view.findViewById(R.id.spinnerSoVe);
        tvUsername = view.findViewById(R.id.tv_username);
        tvLogin = view.findViewById(R.id.tv_login);
        avata = view.findViewById(R.id.avata);
        editTextNgayDi = view.findViewById(R.id.editTextNgayDi);
        editTextNgayVe = view.findViewById(R.id.editTextNgayVe);
        switchKhuHoi = view.findViewById(R.id.switchKhuhHoi);
        btnTimTuyen = view.findViewById(R.id.btnTimTuyen);

        switchKhuHoi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                returnCheck.setVisibility(View.VISIBLE);
            } else {
                returnCheck.setVisibility(View.GONE);
                editTextNgayVe.setText("");
            }
        });

        // Sự kiện click vào EditText để chọn ngày
        editTextNgayDi.setOnClickListener(v -> showDatePickerDialog(editTextNgayDi));
        editTextNgayVe.setOnClickListener(v -> showDatePickerDialog(editTextNgayVe));

        // Sự kiện click vào nút tìm tuyến
        btnTimTuyen.setOnClickListener(v -> onSearchRoute());
    }
    private void swapSpinners() {
        int viTriDiemDi = spinnerDiemDi.getSelectedItemPosition();
        int viTriDiemDen = spinnerDiemDen.getSelectedItemPosition();

        // Đổi vị trí
        spinnerDiemDi.setSelection(viTriDiemDen);
        spinnerDiemDen.setSelection(viTriDiemDi);
    }

    private void fetchTinhThanhData() {
        ApiService apiService = ApiClient.getApiService();
        Call<TinhThanhResponse> call = apiService.getDanhSachTinhThanh();

        call.enqueue(new Callback<TinhThanhResponse>() {
            @Override
            public void onResponse(Call<TinhThanhResponse> call, Response<TinhThanhResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TinhThanh> list = response.body().getTinhThanhList();
                    populateSpinners(list);
                } else {
                    ToastHelper.show(requireContext(), "Không lấy được dữ liệu tỉnh thành!");
                }
            }

            @Override
            public void onFailure(Call<TinhThanhResponse> call, Throwable t) {
                ToastHelper.show(requireContext(), "Lỗi kết nối: " + t.getMessage());
                Log.e("API_ERROR", "Lỗi kết nối", t);
            }
        });
    }

    private void populateSpinners(List<TinhThanh> tinhThanhList) {
        ArrayAdapter<TinhThanh> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, tinhThanhList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDiemDi.setAdapter(adapter);
        spinnerDiemDen.setAdapter(adapter);

        // Set adapter cho số vé (có thể làm tùy chỉnh theo yêu cầu)
        ArrayAdapter<CharSequence> soVeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.so_ve_array, android.R.layout.simple_spinner_item);
        soVeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSoVe.setAdapter(soVeAdapter);
    }

    private void showDatePickerDialog(EditText targetEditText) {
        final Calendar calendar = Calendar.getInstance();

        // Nếu đang chọn ngày về và đã có ngày đi
        if (targetEditText == editTextNgayVe && !editTextNgayDi.getText().toString().isEmpty()) {
            String[] parts = editTextNgayDi.getText().toString().split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // Java calendar month is 0-based
            int year = Integer.parseInt(parts[2]);
            calendar.set(year, month, day); // Đặt ngày tối thiểu là ngày đi
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    targetEditText.setText(selectedDate);
                },
                year, month, day
        );

        // Đặt ngày tối thiểu là hôm nay hoặc ngày đi (nếu chọn ngày về)
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    private void onSearchRoute() {
        // Lấy giá trị từ Spinner
        TinhThanh diemDi = (TinhThanh) spinnerDiemDi.getSelectedItem();
        TinhThanh diemDen = (TinhThanh) spinnerDiemDen.getSelectedItem();
        Boolean isRoundTrip = switchKhuHoi.isChecked();

        // Lấy giá trị từ EditText
        String ngayDi = editTextNgayDi.getText().toString();
        String ngayVe = editTextNgayVe.getText().toString();
        String soVe = spinnerSoVe.getSelectedItem().toString();

        if (diemDi == null || diemDen == null) {
            ToastHelper.show(requireContext(), "Vui lòng Chọn đầy đủ điểm đi và điểm đến !");
            return;
        }
        if (ngayDi.isEmpty()) {
            ToastHelper.show(requireContext(), "Vui lòng Chọn ngày đi !");
            return;
        }

        if (isRoundTrip && ngayVe.isEmpty()) {
            ToastHelper.show(requireContext(), "Vui lòng chọn ngày về cho chuyến khứ hồi !");
            return;
        }

        // Tạo Intent để chuyển sang BookBusActivity và truyền dữ liệu
        Intent intent = new Intent(getActivity(), BookBusActivity.class);
        Bundle Location = new Bundle();
        Location.putInt("departureId", diemDi.getIdTinhThanh());
        Location.putString("departure", diemDi.getTenTinh());
        Location.putInt("destinationId", diemDen.getIdTinhThanh());
        Location.putString("destination", diemDen.getTenTinh());
        Location.putString("departureDate", ngayDi);
        Location.putString("returnDate", ngayVe);
        Location.putBoolean("isRoundTrip", isRoundTrip);
        Location.putInt("tickets", Integer.parseInt(soVe));
        intent.putExtras(Location);
        startActivity(intent);
    }
    private void updateUserInfo() {
        String userName = SharedPrefHelper.getUserName(requireContext());
        if (userName == null || userName.isEmpty()) {
            avata.setImageResource(R.drawable.ic_login);
            tvUsername.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);

            avata.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            avata.setImageResource(R.drawable.avata);
            tvUsername.setVisibility(View.VISIBLE);
            tvUsername.setText("Xin Chào,\n" + userName);
            tvLogin.setVisibility(View.GONE);

            avata.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToAccountTab();
                }
            });
        }
    }

}
