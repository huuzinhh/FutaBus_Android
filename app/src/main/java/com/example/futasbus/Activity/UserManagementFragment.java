package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.example.futasbus.model.NguoiDung;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.Map;

public class UserManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewUser;
    private List<NguoiDung> nguoiDungList;
    private CustomerAdapter adapter;

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

        apiService.getAllNguoiDung().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    nguoiDungList = response.body();
                    adapter = new CustomerAdapter(requireContext(), nguoiDungList, new CustomerAdapter.OnCustomerActionListener() {
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
                            Toast.makeText(getContext(), "Xoá: " + nguoiDung.getHoTen(), Toast.LENGTH_SHORT).show();
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
}


