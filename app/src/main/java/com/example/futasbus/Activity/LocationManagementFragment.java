package com.example.futasbus.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.futasbus.Adapter.CityManagementAdapter;
import com.example.futasbus.Adapter.DistrictManagementAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.databinding.FragmentLocationManagementBinding;
import com.example.futasbus.model.QuanHuyen;
import com.example.futasbus.model.QuanHuyenDTO;
import com.example.futasbus.model.TinhThanh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationManagementFragment extends Fragment {

    private FragmentLocationManagementBinding binding;
    private CityManagementAdapter tinhThanhAdapter;
    private DistrictManagementAdapter quanHuyenAdapter;
    private List<TinhThanh> tinhThanhList;
    private List<QuanHuyen> quanHuyenList;
    private TinhThanh selectedTinhThanh;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationManagementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);
        tinhThanhList = new ArrayList<>();
        quanHuyenList = new ArrayList<>();

        // Setup TinhThanh Adapter
        tinhThanhAdapter = new CityManagementAdapter(
                getContext(),
                tinhThanhList,
                new CityManagementAdapter.OnTinhThanhActionListener() {
                    @Override
                    public void onEdit(TinhThanh tinhThanh) {
                        showEditDialog(tinhThanh, true);
                    }

                    @Override
                    public void onDelete(TinhThanh tinhThanh) {
                        deleteTinhThanh(tinhThanh);
                    }
                }
        );
        binding.gridViewLocation.setAdapter(tinhThanhAdapter);

        // Setup RecyclerView for QuanHuyen
        quanHuyenAdapter = new DistrictManagementAdapter(
                new ArrayList<>(),
                new DistrictManagementAdapter.OnQuanHuyenActionListener() {
                    @Override
                    public void onEdit(QuanHuyen quanHuyen) {
                        showEditDialog(quanHuyen, false);
                    }

                    @Override
                    public void onDelete(QuanHuyen quanHuyen) {
                        deleteQuanHuyen(quanHuyen);
                    }
                }
        );
        binding.recyclerQuanHuyen.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerQuanHuyen.setAdapter(quanHuyenAdapter);

        // Load initial data
        loadTinhThanhList();

        // GridView item click
        binding.gridViewLocation.setOnItemClickListener((parent, v, position, id) -> {
            selectedTinhThanh = tinhThanhAdapter.getItem(position);
            if (selectedTinhThanh != null) {
                loadQuanHuyenList(selectedTinhThanh.getIdTinhThanh());
                binding.recyclerQuanHuyen.setVisibility(View.VISIBLE);
            }
        });

        // Back button
        binding.iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Add location button
        binding.btnAddLocation.setOnClickListener(v -> {
            if (selectedTinhThanh == null) {
                addTinhThanh();
            } else {
                addQuanHuyen();
            }
        });
        TextView topBarTitle = binding.topBarTitle;
        topBarTitle.setOnClickListener(v->{
            selectedTinhThanh=null;
        });

        // Search
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                List<TinhThanh> filteredTinhThanh = tinhThanhList.stream()
                        .filter(tinhThanh -> tinhThanh.getTenTinh().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
                tinhThanhAdapter.updateTinhThanhList(filteredTinhThanh);
            }
        });

        // Search button
        binding.btnSearch.setOnClickListener(v -> {
            // Trigger search on click if needed
        });
    }

    private void loadTinhThanhList() {
        apiService.getAllTinhThanh().enqueue(new Callback<List<TinhThanh>>() {
            @Override
            public void onResponse(Call<List<TinhThanh>> call, Response<List<TinhThanh>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tinhThanhList.clear();
                    tinhThanhList.addAll(response.body());
                    tinhThanhAdapter.updateTinhThanhList(tinhThanhList);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải danh sách tỉnh/thành phố", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TinhThanh>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuanHuyenList(int tinhThanhId) {
        apiService.getQuanHuyenByTinh(tinhThanhId).enqueue(new Callback<List<QuanHuyen>>() {
            @Override
            public void onResponse(Call<List<QuanHuyen>> call, Response<List<QuanHuyen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quanHuyenList.clear();
                    quanHuyenList.addAll(response.body());
                    quanHuyenAdapter.updateQuanHuyenList(quanHuyenList);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải danh sách quận/huyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuanHuyen>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTinhThanh() {
        EditText editText = new EditText(getContext());
        editText.setHint("Nhập tên tỉnh/thành phố");
        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm tỉnh/thành phố")
                .setView(editText)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String tenTinhThanh = editText.getText().toString();
                    if (!tenTinhThanh.isEmpty()) {
                        TinhThanh newTinhThanh = new TinhThanh(0, tenTinhThanh);
                        apiService.themTinhThanh(newTinhThanh).enqueue(new Callback<Map<String, Object>>() {
                            @Override
                            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    // Giả sử API trả về ID mới trong response
                                    int newId = ((Double) response.body().getOrDefault("idTinhThanh", 0.0)).intValue();
                                    newTinhThanh.setIdTinhThanh(newId);
                                    tinhThanhList.add(newTinhThanh);
                                    tinhThanhAdapter.updateTinhThanhList(tinhThanhList);
                                    Toast.makeText(getContext(), "Thêm tỉnh/thành phố thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Lỗi khi thêm tỉnh/thành phố", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void addQuanHuyen() {
        if (selectedTinhThanh != null) {
            EditText editText = new EditText(getContext());
            editText.setHint("Nhập tên quận/huyện");
            new AlertDialog.Builder(requireContext())
                    .setTitle("Thêm quận/huyện")
                    .setView(editText)
                    .setPositiveButton("Lưu", (dialog, which) -> {
                        String tenQuanHuyen = editText.getText().toString();
                        if (!tenQuanHuyen.isEmpty()) {
                            QuanHuyen newQuanHuyen = new QuanHuyen(0, tenQuanHuyen, selectedTinhThanh);
                            apiService.themQuanHuyen(newQuanHuyen).enqueue(new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        int newId = ((Double) response.body().getOrDefault("idQuanHuyen", 0.0)).intValue();
                                        newQuanHuyen.setIdQuanHuyen(newId);
                                        quanHuyenList.add(newQuanHuyen);
                                        quanHuyenAdapter.updateQuanHuyenList(quanHuyenList);
                                        Toast.makeText(getContext(), "Thêm quận/huyện thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Lỗi khi thêm quận/huyện", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        }
    }

    private void deleteTinhThanh(TinhThanh tinhThanh) {
        apiService.xoaTinhThanh(tinhThanh.getIdTinhThanh()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    tinhThanhList.remove(tinhThanh);
                    quanHuyenList.removeIf(qh -> qh.getTinhThanh().getIdTinhThanh() == tinhThanh.getIdTinhThanh());
                    tinhThanhAdapter.updateTinhThanhList(tinhThanhList);
                    if (selectedTinhThanh == tinhThanh) {
                        selectedTinhThanh = null;
                        binding.recyclerQuanHuyen.setVisibility(View.GONE);
                    }
                    Toast.makeText(getContext(), "Xóa tỉnh/thành phố thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi xóa tỉnh/thành phố", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteQuanHuyen(QuanHuyen quanHuyen) {
        apiService.xoaQuanHuyen(quanHuyen.getIdQuanHuyen()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    quanHuyenList.remove(quanHuyen);
                    quanHuyenAdapter.updateQuanHuyenList(quanHuyenList);
                    Toast.makeText(getContext(), "Xóa quận/huyện thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi xóa quận/huyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Object item, boolean isTinhThanh) {
        EditText editText = new EditText(getContext());
        editText.setText(isTinhThanh ? ((TinhThanh) item).getTenTinh() : ((QuanHuyen) item).getTenQuanHuyen());

        new AlertDialog.Builder(requireContext())
                .setTitle(isTinhThanh ? "Sửa tỉnh/thành phố" : "Sửa quận/huyện")
                .setView(editText)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String newName = editText.getText().toString();
                    if (!newName.isEmpty()) {
                        if (isTinhThanh) {
                            TinhThanh tinhThanh = (TinhThanh) item;
                            tinhThanh.setTenTinh(newName);
                            apiService.capNhatTinhThanh(tinhThanh).enqueue(new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                    if (response.isSuccessful()) {
                                        tinhThanhAdapter.updateTinhThanhList(tinhThanhList);
                                        Toast.makeText(getContext(), "Cập nhật tỉnh/thành phố thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Lỗi khi cập nhật tỉnh/thành phố", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            QuanHuyenDTO quanHuyen = new QuanHuyenDTO(((QuanHuyen) item).getIdQuanHuyen(),((QuanHuyen) item).getTinhThanh().getIdTinhThanh(),newName) ;
                            apiService.capNhatQuanHuyen(quanHuyen).enqueue(new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                    if (response.isSuccessful()) {
                                        quanHuyenAdapter.updateQuanHuyenList(quanHuyenList);
                                        loadQuanHuyenList(selectedTinhThanh.getIdTinhThanh());


                                        Toast.makeText(getContext(), "Cập nhật quận/huyện thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Lỗi khi cập nhật quận/huyện", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}