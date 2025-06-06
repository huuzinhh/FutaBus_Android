package com.example.futasbus.Activity;

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

import com.example.futasbus.Adapter.BusManagementAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.BenXeDTO;
import com.example.futasbus.model.LoaiXe;
import com.example.futasbus.model.QuanHuyen;
import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.model.Xe;
import com.example.futasbus.model.XeDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusManagementFragment extends Fragment {

    private ImageView iconBack,iconAdd;
    private GridView gridViewBus;
    private List<Xe> xeList;
    private List<LoaiXe> danhSachLoaiXe;
    private BusManagementAdapter adapter;

    public BusManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        iconAdd = view.findViewById(R.id.btnAddBus);
        gridViewBus = view.findViewById(R.id.gridViewBus);

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

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllXe().enqueue(new Callback<List<Xe>>() {
            @Override
            public void onResponse(Call<List<Xe>> call, Response<List<Xe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                     xeList= response.body();
                    adapter = new BusManagementAdapter(requireContext(), xeList, new BusManagementAdapter.OnBusActionListener() {
                        @Override
                        public void onView(Xe xe) {

                        }

                        @Override
                        public void onEdit(Xe xe) {
                            suaXe(xe);
                        }

                        @Override
                        public void onDelete(Xe xe) {
//                            xoaXe(xe.getIdXe());
                        }


                    });
                    gridViewBus.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Xe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void themBenXe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_bus, null);
        builder.setView(dialogView);

        EditText etBienSo = dialogView.findViewById(R.id.etBienSo);
        EditText etTenXe = dialogView.findViewById(R.id.etTenXe);
        Spinner spinnerLoaiXe = dialogView.findViewById(R.id.spinnerLoaiXe);
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.GONE);
        spinnerTrangThai.setVisibility(View.GONE);



        // Spinner trạng thái
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Không hoạt động", "Hoạt động"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);



        ApiClient.getClient().create(ApiService.class).getAllLoaiXe().enqueue(new Callback<List<LoaiXe>>() {
            @Override
            public void onResponse(Call<List<LoaiXe>> call, Response<List<LoaiXe>> response) {
                if (response.isSuccessful()) {
                    danhSachLoaiXe = response.body();
                    List<String> loaiXeList = new ArrayList<>();
                    for (LoaiXe lx : danhSachLoaiXe) {
                        loaiXeList.add(lx.getTenLoai()+" - "+lx.getSoGhe());
                    }
                    ArrayAdapter<String> adapterLoaiXe = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, loaiXeList);
                    adapterLoaiXe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLoaiXe.setAdapter(adapterLoaiXe);
                }
            }

            @Override
            public void onFailure(Call<List<LoaiXe>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách loại xe", Toast.LENGTH_SHORT).show();
            }
        });




        builder.setTitle("Thêm xe mới");
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            Xe newXe = new Xe();
            newXe.setBienSo(etBienSo.getText().toString());
            newXe.setTenXe(etTenXe.getText().toString());
            newXe.setLoaiXe(danhSachLoaiXe.get(spinnerLoaiXe.getSelectedItemPosition()));

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.themXe(newXe).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    try {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(newXe);
                            Log.d("BenXeDTO_JSON try if", json);
                            Map<String, Object> result = response.body();
                            String message = String.valueOf(result.get("message"));
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            loadXe();
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(newXe);
                            Log.d("BenXeDTO_JSON try else", json);
                            Map<String, Object> result = response.body();
                            String errorBody = response.errorBody().string();
                            String errorMessage = String.valueOf(result.get("message"));

                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Gson gson = new Gson();
                        String json = gson.toJson(newXe);
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
    private void loadXe() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllXe().enqueue(new Callback<List<Xe>>() {
            @Override
            public void onResponse(Call<List<Xe>> call, Response<List<Xe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Xe> danhSach = response.body();

                    // Cập nhật lại dữ liệu trong adapter
                    adapter.getXeList().clear();
                    adapter.getXeList().addAll(danhSach);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Xe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải danh sách: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void suaXe(Xe xe ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_bus, null);
        builder.setView(dialogView);

        EditText etBienSo = dialogView.findViewById(R.id.etBienSo);
        EditText etTenXe = dialogView.findViewById(R.id.etTenXe);

        Spinner spinnerLoaiXe = dialogView.findViewById(R.id.spinnerLoaiXe);

        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        TextView tvTT = dialogView.findViewById(R.id.tvTT);
        tvTT.setVisibility(View.GONE);
        spinnerTrangThai.setVisibility(View.GONE);

        etBienSo.setText(xe.getBienSo());
        etTenXe.setText(xe.getTenXe());


        // Spinner trạng thái
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Không hoạt động", "Hoạt động"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);
        spinnerTrangThai.setSelection(xe.getTrangThai());



        ApiClient.getClient().create(ApiService.class).getAllLoaiXe().enqueue(new Callback<List<LoaiXe>>() {
            @Override
            public void onResponse(Call<List<LoaiXe>> call, Response<List<LoaiXe>> response) {
                if (response.isSuccessful()) {
                    danhSachLoaiXe = response.body();
                    List<String> loaiXeList = new ArrayList<>();
                    for (LoaiXe lx : danhSachLoaiXe) {
                        loaiXeList.add(lx.getTenLoai() + " - " + lx.getSoGhe());
                    }

                    ArrayAdapter<String> adapterLoai = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, loaiXeList);
                    adapterLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLoaiXe.setAdapter(adapterLoai);

                    if (xe.getLoaiXe() != null) {
                        int viTriLoaiXeHienTai = 0;
                        for (int i = 0; i < danhSachLoaiXe.size(); i++) {
                            if (danhSachLoaiXe.get(i).getIdLoaiXe() == xe.getLoaiXe().getIdLoaiXe()) {
                                viTriLoaiXeHienTai = i;
                                break;
                            }
                        }
                        spinnerLoaiXe.setSelection(viTriLoaiXeHienTai);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LoaiXe>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không tải được danh sách loại xe", Toast.LENGTH_SHORT).show();
            }
        });




        builder.setTitle("Chỉnh sửa xe");
        builder.setPositiveButton("Lưu", (dialog, which) -> {

            String tenXe = etTenXe.getText().toString().trim();
            String bienSo = etBienSo.getText().toString().trim();

            int idLoaiXe = danhSachLoaiXe.get(spinnerLoaiXe.getSelectedItemPosition()).getIdLoaiXe();

            if (tenXe.isEmpty() || bienSo.isEmpty() ) {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            XeDTO updatedXe = new XeDTO();
            updatedXe.setIdXe(xe.getIdXe());
            updatedXe.setTenXe(tenXe);
            updatedXe.setBienSo(bienSo);
            updatedXe.setLoaiXe(idLoaiXe);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.capNhatXe(updatedXe).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    try {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(updatedXe);
                            Log.d("BenXeDTO_JSON try if", json);
                            Map<String, Object> result = response.body();
                            String message = String.valueOf(result.get("message"));
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            loadXe();
                        } else {
                            Gson gson = new Gson();
                            String json = gson.toJson(updatedXe);
                            Log.d("BenXeDTO_JSON try else", json);
                            Map<String, Object> result = response.body();
                            String errorBody = response.errorBody().string();
                            String errorMessage = String.valueOf(result.get("message"));

                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Gson gson = new Gson();
                        String json = gson.toJson(updatedXe);
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
    private int getTenTinhThanhByQuanHuyenId(int idLoaiXe, List<LoaiXe> danhSachLoaiXe) {
        for (LoaiXe lx : danhSachLoaiXe) {
            if (lx.getIdLoaiXe() == idLoaiXe) {
                return lx.getIdLoaiXe();
            }
        }
        return 0;
    }
}