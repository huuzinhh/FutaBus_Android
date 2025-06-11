package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.TripManagementAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.NguoiDung;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.model.Xe;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TripManagementFragment extends Fragment {

    private ImageView iconBack, iconAdd;
    private GridView gridViewBusTrip;
    private List<ChuyenXe> ChuyenXeList;
    private TripManagementAdapter adapter;
    private List<TuyenXe> listTuyenXe;
    private List<Xe> xeList;
    private List<NguoiDung> danhSachTaiXe;
    private boolean darkMode;
    Switch switchTheme;
    private List<ChuyenXe> filteredList = new ArrayList<>();
    public TripManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconAdd = view.findViewById(R.id.btnAddTrip);
        gridViewBusTrip = view.findViewById(R.id.gridViewBusTrip);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        iconAdd.setOnClickListener(v -> {
            themChuyenXe();
        });

//        switchTheme = view.findViewById(R.id.switchTheme);
//        switchTheme.setChecked(darkMode);
//
//        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            AppCompatDelegate.setDefaultNightMode(
//                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
//            );
//        });
        EditText edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getDanhSachChuyenXe().enqueue(new Callback<List<ChuyenXe>>() {
            @Override
            public void onResponse(Call<List<ChuyenXe>> call, Response<List<ChuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChuyenXeList = response.body();
                    adapter = new TripManagementAdapter(requireContext(), ChuyenXeList, new TripManagementAdapter.OnBusTripActionListener() {
                        @Override
                        public void onView(ChuyenXe chuyenXe) {

                        }

                        @Override
                        public void onEdit(ChuyenXe chuyenXe) {
                            suaChuyenXe(chuyenXe);
                        }

                        @Override
                        public void onDelete(ChuyenXe chuyenXe) {
                            xoaChuyenXe(chuyenXe.getIdChuyenXe());

                        }


                    });
                    gridViewBusTrip.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

        return view;
    }
    private void xoaChuyenXe(int idChuyenXe) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.xoaChuyenXe(idChuyenXe);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xoá chuyến xe thành công", Toast.LENGTH_SHORT).show();
                    // Cập nhật danh sách
                    for (int i = 0; i < ChuyenXeList.size(); i++) {
                        if (ChuyenXeList.get(i).getIdChuyenXe() == idChuyenXe) {
                            ChuyenXeList.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy chuyến xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void suaChuyenXe(ChuyenXe chuyenXe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_trip, null);
        builder.setView(dialogView);

        Spinner spinnerTuyenXe = dialogView.findViewById(R.id.spinnerTuyenXe);
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        EditText edtThoiDiemDi = dialogView.findViewById(R.id.etThoiDiemDi);
        EditText edtThoiDiemDen = dialogView.findViewById(R.id.etThoiDiemDen);
        EditText edtGiaVe = dialogView.findViewById(R.id.etGiaVe);
        Spinner spinnerXe = dialogView.findViewById(R.id.spinnerXe);
        Spinner spinnerTaiXe = dialogView.findViewById(R.id.spinnerTaiXe);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.VISIBLE);
        spinnerTrangThai.setVisibility(View.VISIBLE);

        // Gán giá trị hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        edtThoiDiemDi.setText(sdf.format(chuyenXe.getThoiDiemDi()));
        edtThoiDiemDen.setText(sdf.format(chuyenXe.getThoiDiemDen()));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        edtGiaVe.setText(currencyFormat.format(chuyenXe.getGiaVe()));


        // Spinner trạng thái
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Không hoạt động", "Hoạt động"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);
        spinnerTrangThai.setSelection(chuyenXe.getTrangThai());

        // Spinner tuyến xe
        ApiClient.getClient().create(ApiService.class).getDanhSachTuyenXe().enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listTuyenXe = response.body();

                    List<String> tenTuyenXeList = new ArrayList<>();
                    for (TuyenXe tx : listTuyenXe) {
                        tenTuyenXeList.add(tx.getTenTuyen());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenTuyenXeList);
                    spinnerTuyenXe.setAdapter(adapter);



                    // Chọn đúng tuyến xe hiện tại
                    for (int i = 0; i < listTuyenXe.size(); i++) {
                        if (listTuyenXe.get(i).getIdTuyenXe() == chuyenXe.getTuyenXe().getIdTuyenXe()) {
                            spinnerTuyenXe.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách tuyến xe", Toast.LENGTH_SHORT).show();
            }
        });
        ApiClient.getClient().create(ApiService.class).getAllXe().enqueue(new Callback<List<Xe>>() {
            @Override
            public void onResponse(Call<List<Xe>> call, Response<List<Xe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    xeList = response.body();

                    List<String> tenXeList = new ArrayList<>();
                    for (Xe xe : xeList) {
                        tenXeList.add(xe.getBienSo());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenXeList);
                    spinnerXe.setAdapter(adapter);



                    // Chọn đúng xe hiện tại
                    for (int i = 0; i < xeList.size(); i++) {
                        if (xeList.get(i).getIdXe() == chuyenXe.getXe().getIdXe()) {
                            spinnerXe.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Xe>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách xe", Toast.LENGTH_SHORT).show();
            }
        });

        ApiClient.getClient().create(ApiService.class).getAllTaiXe().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful()) {
                    danhSachTaiXe = response.body();

                    List<String> tenTx = new ArrayList<>();
                    for (NguoiDung nguoiDung : danhSachTaiXe) {
                        tenTx.add(nguoiDung.getIdNguoiDung()+" "+nguoiDung.getHoTen());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenTx);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTaiXe.setAdapter(adapter);



                    // Set selection nếu đang chỉnh sửa chuyến xe
                    for (int i = 0; i < danhSachTaiXe.size(); i++) {
                        if (danhSachTaiXe.get(i).getIdNguoiDung() == chuyenXe.getTaiXe().getIdNguoiDung()) {
                            spinnerTaiXe.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi tải danh sách tài xế", Toast.LENGTH_SHORT).show();
            }
        });
//
        edtThoiDiemDi.setOnClickListener(v -> showDateTimePicker(edtThoiDiemDi, edtThoiDiemDen, true));
        edtThoiDiemDen.setOnClickListener(v -> showDateTimePicker(edtThoiDiemDen, edtThoiDiemDi, false));

        builder.setTitle("Chỉnh sửa chuyến xe");
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String thoiDiemDiStr = edtThoiDiemDi.getText().toString();
            String thoiDiemDenStr = edtThoiDiemDen.getText().toString();

            try {

                Date thoiDiemDi = sdf.parse(thoiDiemDiStr);
                Date thoiDiemDen = sdf.parse(thoiDiemDenStr);
                String giaVeStr = edtGiaVe.getText().toString()
                        .replace(".", "")         // bỏ dấu chấm ngăn cách hàng nghìn
                        .replace("₫", "")         // bỏ ký hiệu tiền
                        .replace("\u00A0", "")    // bỏ khoảng trắng đặc biệt nếu có
                        .trim();                  // bỏ khoảng trắng đầu/cuối

                double giaVe = Double.parseDouble(giaVeStr);

//                double giaVe = Double.parseDouble(edtGiaVe.getText().toString());
                int trangThai = spinnerTrangThai.getSelectedItemPosition();
                int viTriTuyenXe = spinnerTuyenXe.getSelectedItemPosition();
                TuyenXe selectedTuyenXe = listTuyenXe.get(viTriTuyenXe);
                int viTritx = spinnerTaiXe.getSelectedItemPosition();
                NguoiDung selectedTaiXe = danhSachTaiXe.get(viTritx);
                int viTriXe = spinnerXe.getSelectedItemPosition();
                Xe selectedXe = xeList.get(viTriXe);

                ChuyenXe updated = new ChuyenXe();
                updated.setIdChuyenXe(chuyenXe.getIdChuyenXe());
                updated.setThoiDiemDi(thoiDiemDi);
                updated.setThoiDiemDen(thoiDiemDen);
                updated.setGiaVe(BigDecimal.valueOf(giaVe));
                updated.setTrangThai(trangThai);
                updated.setTuyenXe(selectedTuyenXe);

                updated.setTaiXe(selectedTaiXe);
                updated.setXe(selectedXe);

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                apiService.capNhatChuyenXe(updated.getIdChuyenXe(), updated).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");

                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                                loadChuyenXe();
                            } else {
                                String errorBody = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String errorMessage = jsonObject.optString("message", "Lỗi không xác định");
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Lỗi xử lý phản hồi!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            } catch (ParseException e) {
                Toast.makeText(requireContext(), "Định dạng ngày giờ không hợp lệ", Toast.LENGTH_SHORT).show();
            }


        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private void loadChuyenXe() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getDanhSachChuyenXe().enqueue(new Callback<List<ChuyenXe>>() {
            @Override
            public void onResponse(Call<List<ChuyenXe>> call, Response<List<ChuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChuyenXe> danhSach = response.body();

                    // Cập nhật lại dữ liệu trong adapter
                    adapter.getChuyenXeList().clear();
                    adapter.getChuyenXeList().addAll(danhSach);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ChuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải danh sách: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void themChuyenXe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_trip, null);
        builder.setView(dialogView);

        Spinner spinnerTuyenXe = dialogView.findViewById(R.id.spinnerTuyenXe);
        EditText edtThoiDiemDi = dialogView.findViewById(R.id.etThoiDiemDi);
        EditText edtThoiDiemDen = dialogView.findViewById(R.id.etThoiDiemDen);
        EditText edtGiaVe = dialogView.findViewById(R.id.etGiaVe);
        Spinner spinnerXe = dialogView.findViewById(R.id.spinnerXe);
        Spinner spinnerTaiXe = dialogView.findViewById(R.id.spinnerTaiXe);
        Spinner edtTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.GONE);
        edtTrangThai.setVisibility(View.GONE);
        // Load spinner Tuyến, Xe, Tài xế
        loadTuyenXe(spinnerTuyenXe);
        loadXe(spinnerXe);
        loadTaiXe(spinnerTaiXe);

        edtThoiDiemDi.setOnClickListener(v -> showDateTimePicker(edtThoiDiemDi, edtThoiDiemDen, true));
        edtThoiDiemDen.setOnClickListener(v -> showDateTimePicker(edtThoiDiemDen, edtThoiDiemDi, false));



        builder.setTitle("Thêm chuyến xe mới");
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                Date thoiDiemDi = sdf.parse(edtThoiDiemDi.getText().toString());
                Date thoiDiemDen = sdf.parse(edtThoiDiemDen.getText().toString());

                int selectedTuyenIndex = spinnerTuyenXe.getSelectedItemPosition();
                int selectedXeIndex = spinnerXe.getSelectedItemPosition();
                int selectedTaiXeIndex = spinnerTaiXe.getSelectedItemPosition();

                BigDecimal giaVe = new BigDecimal(edtGiaVe.getText().toString());

                ChuyenXe newTrip = new ChuyenXe();
                newTrip.setThoiDiemDi(thoiDiemDi);
                newTrip.setThoiDiemDen(thoiDiemDen);
                newTrip.setGiaVe(giaVe);

                newTrip.setTuyenXe(listTuyenXe.get(selectedTuyenIndex));
                newTrip.setXe(xeList.get(selectedXeIndex));
                newTrip.setTaiXe(danhSachTaiXe.get(selectedTaiXeIndex));

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                apiService.themChuyenXe(newTrip).enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean success = (Boolean) response.body().get("success");
                            if (success) {
                                Toast.makeText(requireContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                loadChuyenXe();
                            } else {
                                Toast.makeText(requireContext(), "Thêm thất bại: " + response.body().get("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ParseException e) {
                Toast.makeText(requireContext(), "Lỗi định dạng ngày giờ", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }

    private void loadTuyenXe(Spinner spinner) {
        ApiClient.getClient().create(ApiService.class).getDanhSachTuyenXe().enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful()) {
                    listTuyenXe = response.body();
                    List<String> names = new ArrayList<>();
                    for (TuyenXe tx : listTuyenXe) names.add(tx.getTenTuyen());
                    spinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names));
                }
            }

            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {}
        });
    }

    private void loadXe(Spinner spinner) {
        ApiClient.getClient().create(ApiService.class).getAllXe().enqueue(new Callback<List<Xe>>() {
            @Override
            public void onResponse(Call<List<Xe>> call, Response<List<Xe>> response) {
                if (response.isSuccessful()) {
                    xeList = response.body();
                    List<String> bienSoList = new ArrayList<>();
                    for (Xe xe : xeList) bienSoList.add(xe.getBienSo());
                    spinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, bienSoList));
                }
            }

            @Override
            public void onFailure(Call<List<Xe>> call, Throwable t) {}
        });
    }

    private void loadTaiXe(Spinner spinner) {
        ApiClient.getClient().create(ApiService.class).getAllTaiXe().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful()) {
                    danhSachTaiXe = response.body();
                    List<String> names = new ArrayList<>();
                    for (NguoiDung tx : danhSachTaiXe) names.add(tx.getIdNguoiDung() + " " + tx.getHoTen());
                    spinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, names));
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {}
        });
    }

    private void showDateTimePicker(EditText targetEditText, EditText otherTimeEditText, boolean isThoiDiemDi) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view1, year, month, dayOfMonth) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (view2, hourOfDay, minute) -> {
                                calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                                try {
                                    Date thisTime = calendar.getTime();

                                    String otherTimeStr = otherTimeEditText.getText().toString();
                                    Date otherTime = null;
                                    if (!otherTimeStr.isEmpty()) {
                                        otherTime = sdf.parse(otherTimeStr);
                                    }

                                    if (thisTime.before(new Date())) {
                                        Toast.makeText(requireContext(), "Không thể chọn thời điểm quá khứ!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (isThoiDiemDi && otherTime != null && !thisTime.before(otherTime)) {
                                        Toast.makeText(requireContext(), "Thời điểm đi phải trước thời điểm đến!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (!isThoiDiemDi && otherTime != null && !thisTime.after(otherTime)) {
                                        Toast.makeText(requireContext(), "Thời điểm đến phải sau thời điểm đi!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    targetEditText.setText(sdf.format(thisTime));
                                } catch (ParseException e) {
                                    Toast.makeText(requireContext(), "Lỗi định dạng thời gian!", Toast.LENGTH_SHORT).show();
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void filterBusTrip(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(ChuyenXeList);
        } else {
            for (ChuyenXe chuyenXe : ChuyenXeList) {
                if (chuyenXe.getTuyenXe().getTenTuyen().toLowerCase().contains(query.toLowerCase()) ) {
                    filteredList.add(chuyenXe);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}
