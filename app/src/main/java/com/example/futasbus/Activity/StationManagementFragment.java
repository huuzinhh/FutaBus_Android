package com.example.futasbus.Activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.futasbus.Adapter.StationManagementAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.BenXeDTO;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.NguoiDung;
import com.example.futasbus.model.QuanHuyen;
import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.model.Xe;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StationManagementFragment extends Fragment {

    private ImageView iconBack,iconAdd;
    private GridView gridViewBusStation;
    private List<BenXe> benXeList;
    private StationManagementAdapter adapter;
    private List<QuanHuyen> danhSachQuanHuyen = new ArrayList<>();
    private List<QuanHuyen> danhSachQuanHuyentheoTinh = new ArrayList<>();
    private List<TinhThanh> danhSachTinhThanh = new ArrayList<>();
    public StationManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        iconAdd=view.findViewById(R.id.btnAddStation);
        gridViewBusStation = view.findViewById(R.id.gridViewBusStation);


        iconAdd.setOnClickListener(v -> {
            themBenXe();
        });


        EditText edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        // lấy danh sách quận huyện trước
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllQuanHuyen().enqueue(new Callback<List<QuanHuyen>>() {
            @Override
            public void onResponse(Call<List<QuanHuyen>> call, Response<List<QuanHuyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachQuanHuyen = response.body();

                    // có danh sách quận huyện, lấy danh sách Bến Xe
                    apiService.getAllBenXeDTO().enqueue(new Callback<List<BenXe>>() {
                        @Override
                        public void onResponse(Call<List<BenXe>> call, Response<List<BenXe>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                benXeList = response.body();

                                adapter = new StationManagementAdapter(
                                        requireContext(),
                                        benXeList,
                                        danhSachQuanHuyen,
                                        new StationManagementAdapter.OnBusStationActionListener() {
                                            @Override
                                            public void onView(BenXe benXe) {}

                                            @Override
                                            public void onEdit(BenXe benXe) {
                                                suaBenXe(benXe);
                                            }

                                            @Override
                                            public void onDelete(BenXe benXe) {
                                                xoaBenXe(benXe.getIdBenXe());
                                            }
                                        }
                                );
                                gridViewBusStation.setAdapter(adapter);
                            } else {
                                Toast.makeText(getContext(), "Không lấy được bến xe", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BenXe>> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi kết nối bến xe: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Không lấy được quận huyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuanHuyen>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối quận huyện: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
    private void xoaBenXe(int idBenXe) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.xoaBenXe(idBenXe);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xoá bến xe thành công", Toast.LENGTH_SHORT).show();
                    // Cập nhật danh sách
                    for (int i = 0; i < benXeList.size(); i++) {
                        if (benXeList.get(i).getIdBenXe() == idBenXe) {
                            benXeList.remove(i);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy bến xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadBenXe() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllBenXeDTO().enqueue(new Callback<List<BenXe>>() {
            @Override
            public void onResponse(Call<List<BenXe>> call, Response<List<BenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BenXe> danhSach = response.body();

                    // Cập nhật lại dữ liệu trong adapter
                    adapter.getBenXeList().clear();
                    adapter.getBenXeList().addAll(danhSach);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải danh sách: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void suaBenXe(BenXe benXe ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_station, null);
        builder.setView(dialogView);

        EditText etTenBenXe = dialogView.findViewById(R.id.etTenBenXe);
        EditText etDiaChi = dialogView.findViewById(R.id.etDiaChi);
        EditText etSDT = dialogView.findViewById(R.id.etSDT);
        Spinner spinnerTinhThanh = dialogView.findViewById(R.id.spinneTinhThanh);
        Spinner spinnerQuanHuyen = dialogView.findViewById(R.id.spinnerQuanHuyen);
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.VISIBLE);
        spinnerTrangThai.setVisibility(View.VISIBLE);

        etTenBenXe.setText(benXe.getTenBenXe());
        etDiaChi.setText(benXe.getDiaChi());
        etSDT.setText(benXe.getSoDienThoai());

        // Spinner trạng thái
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Không hoạt động", "Hoạt động"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);
        spinnerTrangThai.setSelection(benXe.getTrangThai());



// Load tỉnh khi mở dialog
        ApiClient.getClient().create(ApiService.class).getAllTinhThanh().enqueue(new Callback<List<TinhThanh>>() {
            @Override
            public void onResponse(Call<List<TinhThanh>> call, Response<List<TinhThanh>> response) {
                if (response.isSuccessful()) {
                    danhSachTinhThanh = response.body();
                    List<String> tenTinhList = new ArrayList<>();
                    for (TinhThanh t : danhSachTinhThanh) {
                        tenTinhList.add(t.getTenTinh());
                    }
                    ArrayAdapter<String> adapterTinh = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenTinhList);
                    adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTinhThanh.setAdapter(adapterTinh);
                }
            }

            @Override
            public void onFailure(Call<List<TinhThanh>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách tỉnh", Toast.LENGTH_SHORT).show();
            }
        });

// Khi chọn tỉnh thì load quận theo tỉnh
        spinnerTinhThanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idTinh = danhSachTinhThanh.get(position).getIdTinhThanh();

                ApiClient.getClient().create(ApiService.class).getQuanHuyenByTinh(idTinh).enqueue(new Callback<List<QuanHuyen>>() {
                    @Override
                    public void onResponse(Call<List<QuanHuyen>> call, Response<List<QuanHuyen>> response) {
                        if (response.isSuccessful()) {
                            danhSachQuanHuyentheoTinh = response.body();
                            List<String> tenQuanList = new ArrayList<>();
                            for (QuanHuyen q : danhSachQuanHuyentheoTinh) {

                                tenQuanList.add(q.getTenQuanHuyen());
                            }
                            ArrayAdapter<String> adapterQuan = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenQuanList);
                            adapterQuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerQuanHuyen.setAdapter(adapterQuan);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<QuanHuyen>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Không tải được danh sách quận", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

//


        builder.setTitle("Chỉnh sửa bến xe");
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenBenXe = etTenBenXe.getText().toString().trim();
            String diaChi = etDiaChi.getText().toString().trim();
            String soDienThoai = etSDT.getText().toString().trim();
            int trangThai = spinnerTrangThai.getSelectedItemPosition();
            int idTinhThanh = danhSachTinhThanh.get(spinnerTinhThanh.getSelectedItemPosition()).getIdTinhThanh();
            int idQuanHuyen = danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getIdQuanHuyen();
            if (tenBenXe.isEmpty() || diaChi.isEmpty() || soDienThoai.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra định dạng số điện thoại
            if (!soDienThoai.matches("^(\\+84|0)[0-9]{9}$")) {
                Toast.makeText(requireContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            BenXeDTO updatedBenXe = new BenXeDTO();
            updatedBenXe.setIdBenXe(benXe.getIdBenXe());
            updatedBenXe.setTenBenXe(tenBenXe);
            updatedBenXe.setDiaChi(diaChi);
            updatedBenXe.setSoDienThoai(soDienThoai);
            QuanHuyen updateQuanHuyen = new QuanHuyen();

            updateQuanHuyen.setIdQuanHuyen(idQuanHuyen);
            updateQuanHuyen.setTenQuanHuyen(danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getTenQuanHuyen());
            updateQuanHuyen.setTinhThanh(danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getTinhThanh());

            updatedBenXe.setTrangThai(trangThai);
            updatedBenXe.setIdQuanHuyen(updateQuanHuyen);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.capNhatBenXe(updatedBenXe).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    try {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(updatedBenXe);
                            Log.d("BenXeDTO_JSON try if", json);
                            Map<String, Object> result = response.body();
                            String message = String.valueOf(result.get("message"));
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            loadBenXe();
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(updatedBenXe);
                            Log.d("BenXeDTO_JSON try else", json);
                            Map<String, Object> result = response.body();
                            String errorBody = response.errorBody().string();
                            String errorMessage = String.valueOf(result.get("message"));

                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Gson gson = new Gson();
                        String json = gson.toJson(updatedBenXe);
                        Log.d("BenXeDTO_JSON catch", json);
                        Toast.makeText(requireContext(), "Lỗi xử lý phản hồi!", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });





        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
    private void themBenXe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_station, null);
        builder.setView(dialogView);

        EditText etTenBenXe = dialogView.findViewById(R.id.etTenBenXe);
        EditText etDiaChi = dialogView.findViewById(R.id.etDiaChi);
        EditText etSDT = dialogView.findViewById(R.id.etSDT);
        Spinner spinnerTinhThanh = dialogView.findViewById(R.id.spinneTinhThanh);
        Spinner spinnerQuanHuyen = dialogView.findViewById(R.id.spinnerQuanHuyen);
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.GONE);
        spinnerTrangThai.setVisibility(View.GONE);



        // Spinner trạng thái
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Không hoạt động", "Hoạt động"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);



        ApiClient.getClient().create(ApiService.class).getAllTinhThanh().enqueue(new Callback<List<TinhThanh>>() {
            @Override
            public void onResponse(Call<List<TinhThanh>> call, Response<List<TinhThanh>> response) {
                if (response.isSuccessful()) {
                    danhSachTinhThanh = response.body();
                    List<String> tenTinhList = new ArrayList<>();
                    for (TinhThanh t : danhSachTinhThanh) {
                        tenTinhList.add(t.getTenTinh());
                    }
                    ArrayAdapter<String> adapterTinh = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenTinhList);
                    adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTinhThanh.setAdapter(adapterTinh);
                }
            }

            @Override
            public void onFailure(Call<List<TinhThanh>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách tỉnh", Toast.LENGTH_SHORT).show();
            }
        });

// Khi chọn tỉnh thì load quận theo tỉnh
        spinnerTinhThanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int idTinh = danhSachTinhThanh.get(position).getIdTinhThanh();

                ApiClient.getClient().create(ApiService.class).getQuanHuyenByTinh(idTinh).enqueue(new Callback<List<QuanHuyen>>() {
                    @Override
                    public void onResponse(Call<List<QuanHuyen>> call, Response<List<QuanHuyen>> response) {
                        if (response.isSuccessful()) {
                            danhSachQuanHuyentheoTinh = response.body();
                            List<String> tenQuanList = new ArrayList<>();
                            for (QuanHuyen q : danhSachQuanHuyentheoTinh) {

                                tenQuanList.add(q.getTenQuanHuyen());
                            }
                            ArrayAdapter<String> adapterQuan = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenQuanList);
                            adapterQuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerQuanHuyen.setAdapter(adapterQuan);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<QuanHuyen>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Không tải được danh sách quận", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        builder.setTitle("Thêm bến xe mới");
        builder.setPositiveButton("Thêm", (dialog, which) -> {



            BenXeDTO newstation = new BenXeDTO();

            newstation.setTenBenXe(etTenBenXe.getText().toString());
            newstation.setDiaChi(etDiaChi.getText().toString());
            newstation.setSoDienThoai(etSDT.getText().toString());
            QuanHuyen updateQuanHuyen = new QuanHuyen();

            updateQuanHuyen.setIdQuanHuyen(danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getIdQuanHuyen());
            updateQuanHuyen.setTenQuanHuyen(danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getTenQuanHuyen());
            updateQuanHuyen.setTinhThanh(danhSachQuanHuyentheoTinh.get(spinnerQuanHuyen.getSelectedItemPosition()).getTinhThanh());


            newstation.setIdQuanHuyen(updateQuanHuyen);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.themBenXe(newstation).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    try {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(newstation);
                            Log.d("BenXeDTO_JSON try if", json);
                            Map<String, Object> result = response.body();
                            String message = String.valueOf(result.get("message"));
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            loadBenXe();
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(newstation);
                            Log.d("BenXeDTO_JSON try else", json);
                            Map<String, Object> result = response.body();
                            String errorBody = response.errorBody().string();
                            String errorMessage = String.valueOf(result.get("message"));

                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Gson gson = new Gson();
                        String json = gson.toJson(newstation);
                        Log.d("BenXeDTO_JSON catch", json);
                        Toast.makeText(requireContext(), "Lỗi xử lý phản hồi!", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        });
        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }
private String getTenTinhThanhByQuanHuyenId(int idQuanHuyen, List<QuanHuyen> danhSachQuanHuyen) {
    for (QuanHuyen qh : danhSachQuanHuyen) {
        if (qh.getIdQuanHuyen() == idQuanHuyen) {
            return qh.getTinhThanh().getTenTinh();
        }
    }
    return "chưa rõ";
}
}

