package com.example.futasbus.Activity;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.BusRouteAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.QuanHuyen;
import com.example.futasbus.model.TinhThanh;
import com.example.futasbus.model.TuyenXe;
import com.example.futasbus.model.TuyenXeUpdateDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusRouteManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewBusRoute;
    private List<TuyenXe> tuyenXeList;
    private BusRouteAdapter adapter;
    private List<BenXe> danhSachBenXe = new ArrayList<>();
    private List<QuanHuyen> danhSachQuanHuyen = new ArrayList<>();
    TuyenXeUpdateDTO tuyenxeUpdate = new TuyenXeUpdateDTO();
    private List<TuyenXe> filteredList = new ArrayList<>();

    public BusRouteManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busroute_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        gridViewBusRoute = view.findViewById(R.id.gridViewBusRoute);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getAllBenXeDTO().enqueue(new Callback<List<BenXe>>() {
            @Override
            public void onResponse(Call<List<BenXe>> call, Response<List<BenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachBenXe = response.body();
                } else {
                    Toast.makeText(getContext(), "Không lấy được danh sách bến xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối bến xe: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnAddRoute = view.findViewById(R.id.btnAddRoute);
        btnAddRoute.setOnClickListener(v -> {
            showAddRouteDialog();
        });

        apiService.getAllQuanHuyen().enqueue(new Callback<List<QuanHuyen>>() {
            @Override
            public void onResponse(Call<List<QuanHuyen>> call, Response<List<QuanHuyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachQuanHuyen = response.body();
                } else {
                    Toast.makeText(getContext(), "Không lấy được danh sách quận huyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuanHuyen>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối quận huyện: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        EditText edtSearchRoute = view.findViewById(R.id.edtSearchRoute);
        edtSearchRoute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBusRoutes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        apiService.getAllTuyenXe().enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tuyenXeList = response.body();
                    filteredList = new ArrayList<>(tuyenXeList);
                    adapter = new BusRouteAdapter(requireContext(), filteredList, new BusRouteAdapter.OnBusRouteActionListener() {
                        @Override
                        public void onView(TuyenXe tuyenXe) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_bus_route_detail, null);

                            ((TextView) view.findViewById(R.id.tvTenTuyen)).setText(tuyenXe.getTenTuyen());
                            ((TextView) view.findViewById(R.id.tvBenXeDi)).setText("" + tuyenXe.getBenXeDi());
                            ((TextView) view.findViewById(R.id.tvBenXeDen)).setText("" + tuyenXe.getBenXeDen());
                            ((TextView) view.findViewById(R.id.tvQuangDuong)).setText(tuyenXe.getQuangDuong() + " km");
                            ((TextView) view.findViewById(R.id.tvThoiGian)).setText(tuyenXe.getThoiGianDiChuyenTB() + " giờ");
                            ((TextView) view.findViewById(R.id.tvSoChuyenNgay)).setText(String.valueOf(tuyenXe.getSoChuyenTrongNgay()));
                            ((TextView) view.findViewById(R.id.tvSoNgayTuan)).setText(String.valueOf(tuyenXe.getSoNgayChayTrongTuan()));

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onEdit(TuyenXe tuyenXe) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_edit_bus_route, null);

                            EditText edtTenTuyen = view.findViewById(R.id.edtTenTuyen);
                            Spinner spinnerBenXeDi = view.findViewById(R.id.spinnerBenXeDi);
                            Spinner spinnerBenXeDen = view.findViewById(R.id.spinnerBenXeDen);
                            EditText edtQuangDuong = view.findViewById(R.id.edtQuangDuong);
                            EditText edtThoiGian = view.findViewById(R.id.edtThoiGian);
                            EditText edtSoChuyenNgay = view.findViewById(R.id.edtSoChuyenNgay);
                            EditText edtSoNgayTuan = view.findViewById(R.id.edtSoNgayTuan);

                            edtTenTuyen.setText(tuyenXe.getTenTuyen());
                            edtQuangDuong.setText(String.valueOf(tuyenXe.getQuangDuong()));
                            edtThoiGian.setText(String.valueOf(tuyenXe.getThoiGianDiChuyenTB()));
                            edtSoChuyenNgay.setText(String.valueOf(tuyenXe.getSoChuyenTrongNgay()));
                            edtSoNgayTuan.setText(String.valueOf(tuyenXe.getSoNgayChayTrongTuan()));

                            List<String> tenBenXeList = new ArrayList<>();
                            for (BenXe benXe : danhSachBenXe) {
                                tenBenXeList.add(benXe.getTenBenXe());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, tenBenXeList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerBenXeDi.setAdapter(adapter);
                            spinnerBenXeDen.setAdapter(adapter);

                            int viTriDi = timViTriBenXeTheoId(tuyenXe.getBenXeDi().getIdBenXe(), danhSachBenXe);
                            int viTriDen = timViTriBenXeTheoId(tuyenXe.getBenXeDen().getIdBenXe(), danhSachBenXe);

                            spinnerBenXeDi.setSelection(viTriDi);
                            spinnerBenXeDen.setSelection(viTriDen);

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Lưu", (dialog, which) -> {
                                int viTriBenXeDi = spinnerBenXeDi.getSelectedItemPosition();
                                int viTriBenXeDen = spinnerBenXeDen.getSelectedItemPosition();

                                BenXe benXeDi = danhSachBenXe.get(viTriBenXeDi);
                                BenXe benXeDen = danhSachBenXe.get(viTriBenXeDen);

                                tuyenXe.setTenTuyen(edtTenTuyen.getText().toString());
                                tuyenXe.setBenXeDi(benXeDi);
                                tuyenXe.setBenXeDen(benXeDen);
                                tuyenXe.setQuangDuong(Float.parseFloat(edtQuangDuong.getText().toString()));
                                tuyenXe.setThoiGianDiChuyenTB(Float.parseFloat(edtThoiGian.getText().toString()));
                                tuyenXe.setSoChuyenTrongNgay(Integer.parseInt(edtSoChuyenNgay.getText().toString()));
                                tuyenXe.setSoNgayChayTrongTuan(Integer.parseInt(edtSoNgayTuan.getText().toString()));

                                int idTinhThanhDi = getTenTinhThanhByQuanHuyenId(tuyenXe.getBenXeDi().getIdQuanHuyen(), danhSachQuanHuyen);
                                int idTinhThanhDen = getTenTinhThanhByQuanHuyenId(tuyenXe.getBenXeDen().getIdQuanHuyen(), danhSachQuanHuyen);

                                Log.d("TuyenXe", "Tên tuyến: " + tuyenXe.getTenTuyen()
                                            + ", Id bến xe đi: " + tuyenXe.getBenXeDi().getIdBenXe()
                                            + ", Bến xe đi: " + tuyenXe.getBenXeDi().getTenBenXe()
                                            + ", Id tỉnh đi: " + idTinhThanhDi
                                            + ", Id bến xe đến: " + tuyenXe.getBenXeDen().getIdBenXe()
                                            + ", Bến xe đến: " + tuyenXe.getBenXeDen().getTenBenXe()
                                            + ", Id tỉnh đến: " + idTinhThanhDen
                                            + ", Quãng đường: " + tuyenXe.getQuangDuong()
                                            + ", Thời gian: " + tuyenXe.getThoiGianDiChuyenTB()
                                            + ", Số chuyến/ngày: " + tuyenXe.getSoChuyenTrongNgay()
                                            + ", Số ngày/tuần: " + tuyenXe.getSoNgayChayTrongTuan());

                                tuyenxeUpdate.setIdTuyenXe(tuyenXe.getIdTuyenXe());
                                tuyenxeUpdate.setTenTuyen(tuyenXe.getTenTuyen());
                                tuyenxeUpdate.setIdBenXeDi(tuyenXe.getBenXeDi().getIdBenXe());
                                tuyenxeUpdate.setIdBenXeDen(tuyenXe.getBenXeDen().getIdBenXe());
                                tuyenxeUpdate.setIdTinhThanhDi(idTinhThanhDi);
                                tuyenxeUpdate.setIdTinhThanhDen(idTinhThanhDen);
                                tuyenxeUpdate.setQuangDuong(tuyenXe.getQuangDuong());
                                tuyenxeUpdate.setThoiGianDiChuyenTB(tuyenXe.getThoiGianDiChuyenTB());
                                tuyenxeUpdate.setSoChuyenTrongNgay(tuyenXe.getSoChuyenTrongNgay());
                                tuyenxeUpdate.setSoNgayChayTrongTuan(tuyenXe.getSoNgayChayTrongTuan());

                                capNhatTuyenXe(tuyenxeUpdate);
                            });
                            builder.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onDelete(TuyenXe tuyenXe) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Xác nhận xoá")
                                    .setMessage("Bạn có chắc chắn muốn xoá tuyến xe này không?")
                                    .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                                            Call<ResponseBody> call = apiService.xoaTuyenXe(tuyenXe.getIdTuyenXe());

                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Xoá tuyến xe thành công", Toast.LENGTH_SHORT).show();
                                                        loadTuyenXe();
                                                    } else {
                                                        Toast.makeText(getContext(), "Không tìm thấy tuyến xe", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null)
                                    .show();
                        }
                    });
                    gridViewBusRoute.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private int timViTriBenXeTheoId(int id, List<BenXe> danhSachBenXe) {
        for (int i = 0; i < danhSachBenXe.size(); i++) {
            if (danhSachBenXe.get(i).getIdBenXe() == id) {
                return i;
            }
        }
        return 0;
    }

    private int getTenTinhThanhByQuanHuyenId(int idQuanHuyen, List<QuanHuyen> danhSachQuanHuyen) {
        for (QuanHuyen qh : danhSachQuanHuyen) {
            if (qh.getIdQuanHuyen() == idQuanHuyen) {
                return qh.getTinhThanh().getIdTinhThanh();
            }
        }
        return 0;
    }

    private void capNhatTuyenXe(TuyenXeUpdateDTO tuyenXe) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Map<String, Object>> call = apiService.updateTuyenXe(tuyenXe);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = (Boolean) response.body().get("success");
                    String message = (String) response.body().get("message");

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    if (success != null && success) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTuyenXe() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllTuyenXe().enqueue(new Callback<List<TuyenXe>>() {
            @Override
            public void onResponse(Call<List<TuyenXe>> call, Response<List<TuyenXe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tuyenXeList.clear();
                    loadTuyenXe();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách tuyến xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TuyenXe>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddRouteDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_bus_route, null);

        EditText edtTenTuyen = dialogView.findViewById(R.id.edtTenTuyen);
        Spinner spinnerBenXeDi = dialogView.findViewById(R.id.spinnerBenXeDi);
        Spinner spinnerBenXeDen = dialogView.findViewById(R.id.spinnerBenXeDen);
        EditText edtQuangDuong = dialogView.findViewById(R.id.edtQuangDuong);
        EditText edtThoiGian = dialogView.findViewById(R.id.edtThoiGian);
        EditText edtSoChuyenNgay = dialogView.findViewById(R.id.edtSoChuyenNgay);
        EditText edtSoNgayTuan = dialogView.findViewById(R.id.edtSoNgayTuan);

        ArrayAdapter<BenXe> benXeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, danhSachBenXe);
        benXeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBenXeDi.setAdapter(benXeAdapter);
        spinnerBenXeDen.setAdapter(benXeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String tenTuyen = edtTenTuyen.getText().toString();
            BenXe benXeDi = danhSachBenXe.get(spinnerBenXeDi.getSelectedItemPosition());
            BenXe benXeDen = danhSachBenXe.get(spinnerBenXeDen.getSelectedItemPosition());
            float quangDuong = Float.parseFloat(edtQuangDuong.getText().toString());
            float thoiGian = Float.parseFloat(edtThoiGian.getText().toString());
            int soChuyenNgay = Integer.parseInt(edtSoChuyenNgay.getText().toString());
            int soNgayTuan = Integer.parseInt(edtSoNgayTuan.getText().toString());

            TuyenXe newTuyenXe = new TuyenXe();
            newTuyenXe.setTenTuyen(tenTuyen);
            newTuyenXe.setBenXeDi(benXeDi);
            newTuyenXe.setBenXeDen(benXeDen);
            newTuyenXe.setQuangDuong(quangDuong);
            newTuyenXe.setThoiGianDiChuyenTB(thoiGian);
            newTuyenXe.setSoChuyenTrongNgay(soChuyenNgay);
            newTuyenXe.setSoNgayChayTrongTuan(soNgayTuan);

            addBusRoute(newTuyenXe);
            dialog.dismiss();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addBusRoute(TuyenXe tuyenXe) {
        BenXe benXeDi = new BenXe();
        benXeDi.setIdBenXe(tuyenXe.getBenXeDi().getIdBenXe());
        benXeDi.setTenBenXe(tuyenXe.getBenXeDi().getTenBenXe());
        benXeDi.setIdQuanHuyen(tuyenXe.getBenXeDi().getIdQuanHuyen());
        tuyenXe.setBenXeDi(benXeDi);

        BenXe benXeDen = new BenXe();
        benXeDen.setIdBenXe(tuyenXe.getBenXeDen().getIdBenXe());
        benXeDen.setTenBenXe(tuyenXe.getBenXeDen().getTenBenXe());
        benXeDen.setIdQuanHuyen(tuyenXe.getBenXeDen().getIdQuanHuyen());
        tuyenXe.setBenXeDen(benXeDen);

        int tinhDi = getTenTinhThanhByQuanHuyenId(tuyenXe.getBenXeDi().getIdQuanHuyen(), danhSachQuanHuyen);
        int tinhDen = getTenTinhThanhByQuanHuyenId(tuyenXe.getBenXeDen().getIdQuanHuyen(), danhSachQuanHuyen);

        TinhThanh tinhThanhDi = new TinhThanh();
        tinhThanhDi.setIdTinhThanh(tinhDi);
        tuyenXe.setTinhThanhDi(tinhThanhDi);

        TinhThanh tinhThanhDen = new TinhThanh();
        tinhThanhDen.setIdTinhThanh(tinhDen);
        tuyenXe.setTinhThanhDen(tinhThanhDen);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.themTuyenXe(tuyenXe).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = (Boolean) response.body().get("success");
                    String message = (String) response.body().get("message");

                    if (success != null && success) {
                        tuyenXeList.add(tuyenXe);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        if (success != null && success) {
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Thêm thất bại: " + message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Phản hồi không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterBusRoutes(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(tuyenXeList);
        } else {
            for (TuyenXe tuyenXe : tuyenXeList) {
                if (tuyenXe.getTenTuyen().toLowerCase().contains(query.toLowerCase()) || tuyenXe.getBenXeDi().getTenBenXe().toLowerCase().contains(query.toLowerCase()) || tuyenXe.getBenXeDen().getTenBenXe().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(tuyenXe);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}