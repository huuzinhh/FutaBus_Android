package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.CustomerAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.model.BookingInfo;
import com.example.futasbus.model.NguoiDung;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewUser;
    private List<NguoiDung> nguoiDungList;
    private CustomerAdapter adapter;
    private List<NguoiDung> filteredList = new ArrayList<>();

    public UserManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        gridViewUser = view.findViewById(R.id.gridViewUser);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        EditText edtSearchUser = view.findViewById(R.id.edtSearchUser);
        edtSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        apiService.getAllNguoiDung().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    nguoiDungList = response.body();
                    filteredList = new ArrayList<>(nguoiDungList);
                    adapter = new CustomerAdapter(requireContext(), filteredList, new CustomerAdapter.OnCustomerActionListener() {
                        @Override
                        public void onView(NguoiDung nguoiDung) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_customer_detail, null);

                            ((TextView) view.findViewById(R.id.tvHoTen)).setText(nguoiDung.getHoTen());
                            ((TextView) view.findViewById(R.id.tvNamSinh)).setText(String.valueOf(nguoiDung.getNamSinh()));
                            ((TextView) view.findViewById(R.id.tvGioiTinh)).setText(nguoiDung.isGioiTinh() ? "Nữ" : "Nam");
                            ((TextView) view.findViewById(R.id.tvDiaChi)).setText(nguoiDung.getDiaChi());
                            ((TextView) view.findViewById(R.id.tvSoDienThoai)).setText(nguoiDung.getSoDienThoai());
                            ((TextView) view.findViewById(R.id.tvEmail)).setText(nguoiDung.getEmail());

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onEdit(NguoiDung nguoiDung) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_edit_customer, null);

                            EditText edtHoTen = view.findViewById(R.id.edtHoTen);
                            EditText edtNamSinh = view.findViewById(R.id.edtNamSinh);
                            EditText edtDiaChi = view.findViewById(R.id.edtDiaChi);
                            EditText edtSoDienThoai = view.findViewById(R.id.edtSoDienThoai);
                            EditText edtEmail = view.findViewById(R.id.edtEmail);
                            RadioButton rdNam = view.findViewById(R.id.rdNam);
                            RadioButton rdNu = view.findViewById(R.id.rdNu);

                            edtHoTen.setText(nguoiDung.getHoTen());
                            edtNamSinh.setText(String.valueOf(nguoiDung.getNamSinh()));
                            edtDiaChi.setText(nguoiDung.getDiaChi());
                            edtSoDienThoai.setText(nguoiDung.getSoDienThoai());
                            edtEmail.setText(nguoiDung.getEmail());
                            if (nguoiDung.isGioiTinh()) rdNu.setChecked(true); else rdNam.setChecked(true);

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Lưu", (dialog, which) -> {
                                nguoiDung.setIdNguoiDung(nguoiDung.getIdNguoiDung());
                                nguoiDung.setHoTen(edtHoTen.getText().toString());
                                nguoiDung.setNamSinh(Integer.parseInt(edtNamSinh.getText().toString()));
                                nguoiDung.setDiaChi(edtDiaChi.getText().toString());
                                nguoiDung.setSoDienThoai(edtSoDienThoai.getText().toString());
                                nguoiDung.setEmail(edtEmail.getText().toString());
                                nguoiDung.setGioiTinh(rdNu.isChecked());

                                System.out.println("Dữ liệu nhận được: " + nguoiDung);

                                capNhatNguoiDung(nguoiDung);
                            });
                            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onDelete(NguoiDung nguoiDung) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Xác nhận xoá")
                                    .setMessage("Bạn có chắc chắn muốn xoá người dùng này không?")
                                    .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                                            Call<ResponseBody> call = apiService.xoaNguoiDung(nguoiDung.getIdNguoiDung());

                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Xoá thành công", Toast.LENGTH_SHORT).show();
                                                        loadNguoiDung();
                                                    } else {
                                                        Toast.makeText(getContext(), "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
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
                    gridViewUser.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void capNhatNguoiDung(NguoiDung nguoiDung) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Map<String, Object>> call = apiService.updateNguoiDung(nguoiDung);

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

    private void loadNguoiDung() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllNguoiDung().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    nguoiDungList.clear();
                    nguoiDungList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUser(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(nguoiDungList);
        } else {
            for (NguoiDung nguoiDung : nguoiDungList) {
                if (nguoiDung.getHoTen().toLowerCase().contains(query.toLowerCase())
                        || nguoiDung.getSoDienThoai().toLowerCase().contains(query.toLowerCase())
                        || nguoiDung.getEmail().toLowerCase().contains(query.toLowerCase())
                        || nguoiDung.getDiaChi().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(nguoiDung);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}


