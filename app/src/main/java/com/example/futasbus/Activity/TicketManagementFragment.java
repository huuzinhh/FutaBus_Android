package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.TicketAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.BookingInfo;

import java.util.Arrays;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TicketManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewTicket;
    private List<BookingInfo> bookingList;
    private TicketAdapter adapter;

    public TicketManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_management, container, false);

        iconBack = view.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        gridViewTicket = view.findViewById(R.id.gridViewTicket);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllPhieuDatVe().enqueue(new Callback<List<BookingInfo>>() {
            @Override
            public void onResponse(Call<List<BookingInfo>> call, Response<List<BookingInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingList = response.body();
                    adapter = new TicketAdapter(requireContext(), bookingList, new TicketAdapter.OnTicketActionListener() {
                        @Override
                        public void onView(BookingInfo bookingInfo) {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_ticket_detail, null);

                            ((TextView) view.findViewById(R.id.tv_full_name)).setText(bookingInfo.getHoTen());
                            ((TextView) view.findViewById(R.id.tv_phone)).setText(bookingInfo.getSoDienThoai());
                            ((TextView) view.findViewById(R.id.tv_email)).setText(bookingInfo.getEmail());

                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                            try {
                                Date departureTime = inputFormat.parse(bookingInfo.getThoiDiemDi());
                                Date arrivalTime = inputFormat.parse(bookingInfo.getThoiDiemDen());
                                Date bookingTime = inputFormat.parse(bookingInfo.getThoiGianDatVe());

                                String formattedDepartureTime = outputFormat.format(departureTime);
                                String formattedArrivalTime = outputFormat.format(arrivalTime);
                                String formattedBookingTime = outputFormat.format(bookingTime);

                                ((TextView) view.findViewById(R.id.tv_departure_time)).setText(formattedDepartureTime);
                                ((TextView) view.findViewById(R.id.tv_arrival_time)).setText(formattedArrivalTime);
                                ((TextView) view.findViewById(R.id.tv_booking_time)).setText(formattedBookingTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            double giaVe = bookingInfo.getGiaVe();
                            double tongTien = bookingInfo.getTongTien();
                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedPrice = currencyFormat.format(giaVe);
                            String formattedTotalPrice = currencyFormat.format(tongTien);
                            ((TextView) view.findViewById(R.id.tv_ticket_price)).setText(formattedPrice);
                            ((TextView) view.findViewById(R.id.tv_ticket_quantity)).setText(String.valueOf(bookingInfo.getSoLuongVe()));
                            ((TextView) view.findViewById(R.id.tv_total_price)).setText(formattedTotalPrice);
                            int trangThai = bookingInfo.getTrangThai();
                            String trangThaiText;

                            switch (trangThai) {
                                case 0:
                                    trangThaiText = "Đã huỷ";
                                    break;
                                case 1:
                                    trangThaiText = "Đã đặt";
                                    break;
                                case 2:
                                    trangThaiText = "Chờ thanh toán";
                                    break;
                                case 3:
                                    trangThaiText = "Đã thanh toán";
                                    break;
                                case 4:
                                    trangThaiText = "Hoàn tất";
                                    break;
                                default:
                                    trangThaiText = "Không xác định";
                            }
                            ((TextView) view.findViewById(R.id.tv_status)).setText(trangThaiText);
                            ((TextView) view.findViewById(R.id.tv_route_name)).setText(bookingInfo.getTenTuyen());
                            ((TextView) view.findViewById(R.id.tv_departure_station)).setText(bookingInfo.getBenDi());
                            ((TextView) view.findViewById(R.id.tv_arrival_station)).setText(bookingInfo.getBenDen());
                            ((TextView) view.findViewById(R.id.tv_vehicle_number)).setText(bookingInfo.getBienSoXe());
                            ((TextView) view.findViewById(R.id.tv_vehicle_type)).setText(bookingInfo.getLoaiXe());
                            ((TextView) view.findViewById(R.id.tv_seat_list)).setText(bookingInfo.getDanhSachGhe());

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onEdit(BookingInfo bookingInfo) {
                            if (bookingInfo.getTrangThai() == 0) {
                                Toast.makeText(getContext(), "Vé đã bị huỷ, không thể chỉnh sửa!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View view = inflater.inflate(R.layout.dialog_edit_booking_info, null);

                            EditText edtHoTen = view.findViewById(R.id.edt_full_name);
                            EditText edtSoDienThoai = view.findViewById(R.id.edt_phone);
                            EditText edtEmail = view.findViewById(R.id.edt_email);
                            EditText edtSoLuongVe = view.findViewById(R.id.edt_ticket_quantity);
                            EditText edtGiaVe = view.findViewById(R.id.edt_ticket_price);
                            EditText edtThoiDiemDi = view.findViewById(R.id.edt_departure_time);
                            EditText edtTenTuyen = view.findViewById(R.id.edt_route_name);
                            EditText edtBienSoXe = view.findViewById(R.id.edt_vehicle_number);
                            EditText edtLoaiXe = view.findViewById(R.id.edt_vehicle_type);
                            EditText edtDanhSachGhe = view.findViewById(R.id.edt_seat_list);
                            Spinner spinnerTrangThai = view.findViewById(R.id.spinner_status);

                            edtHoTen.setText(bookingInfo.getHoTen());
                            edtSoDienThoai.setText(bookingInfo.getSoDienThoai());
                            edtEmail.setText(bookingInfo.getEmail());
                            edtSoLuongVe.setText(String.valueOf(bookingInfo.getSoLuongVe()));

                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            edtGiaVe.setText(currencyFormat.format(bookingInfo.getGiaVe()));

                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

                                Date departureDate = inputFormat.parse(bookingInfo.getThoiDiemDi());
                                if (departureDate != null) {
                                    edtThoiDiemDi.setText(outputFormat.format(departureDate));
                                }
                            } catch (ParseException e) {
                                edtThoiDiemDi.setText(bookingInfo.getThoiDiemDi());
                            }

                            edtTenTuyen.setText(bookingInfo.getTenTuyen());
                            edtBienSoXe.setText(bookingInfo.getBienSoXe());
                            edtLoaiXe.setText(bookingInfo.getLoaiXe());
                            edtDanhSachGhe.setText(bookingInfo.getDanhSachGhe());

                            List<String> trangThaiList = Arrays.asList("Đã huỷ", "Đã đặt", "Chờ thanh toán", "Đã thanh toán", "Hoàn tất");
                            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, trangThaiList);
                            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTrangThai.setAdapter(statusAdapter);

                            if (bookingInfo.getTrangThai() >= 0 && bookingInfo.getTrangThai() < trangThaiList.size()) {
                                spinnerTrangThai.setSelection(bookingInfo.getTrangThai());
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setView(view);
                            builder.setPositiveButton("Lưu", (dialog, which) -> {
                                String hoTen = edtHoTen.getText().toString().trim();
                                String soDienThoai = edtSoDienThoai.getText().toString().trim();
                                String email = edtEmail.getText().toString().trim();
                                int soLuongVe = Integer.parseInt(edtSoLuongVe.getText().toString().trim());

                                double giaVe;
                                try {
                                    Number parsed = currencyFormat.parse(edtGiaVe.getText().toString().trim());
                                    giaVe = parsed.doubleValue();
                                } catch (ParseException e) {
                                    giaVe = 0;
                                }

                                String thoiDiemDi;
                                try {
                                    Date parsedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                            .parse(edtThoiDiemDi.getText().toString().trim());
                                    thoiDiemDi = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                            .format(parsedDate);
                                } catch (ParseException e) {
                                    thoiDiemDi = edtThoiDiemDi.getText().toString().trim();
                                }

                                String tenTuyen = edtTenTuyen.getText().toString().trim();
                                String bienSoXe = edtBienSoXe.getText().toString().trim();
                                String loaiXe = edtLoaiXe.getText().toString().trim();
                                String danhSachGhe = edtDanhSachGhe.getText().toString().trim();
                                int trangThai = spinnerTrangThai.getSelectedItemPosition();

                                bookingInfo.setHoTen(hoTen);
                                bookingInfo.setSoDienThoai(soDienThoai);
                                bookingInfo.setEmail(email);
                                bookingInfo.setSoLuongVe(soLuongVe);
                                bookingInfo.setGiaVe(giaVe);
                                bookingInfo.setThoiDiemDi(thoiDiemDi);
                                bookingInfo.setTenTuyen(tenTuyen);
                                bookingInfo.setBienSoXe(bienSoXe);
                                bookingInfo.setLoaiXe(loaiXe);
                                bookingInfo.setDanhSachGhe(danhSachGhe);
                                bookingInfo.setTrangThai(trangThai);

                                capNhatBookingInfo(bookingInfo);
                            });

                            builder.setNegativeButton("Huỷ", (dialog, which) -> dialog.dismiss());
                            builder.show();
                        }

                        @Override
                        public void onDelete(BookingInfo bookingInfor) {
                            if (bookingInfor.getTrangThai() == 4) {
                                Toast.makeText(getContext(), "Vé đã hoàn tất, không thể huỷ!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Xác nhận huỷ vé")
                                    .setMessage("Bạn có chắc chắn muốn huỷ vé này không?")
                                    .setPositiveButton("Huỷ vé", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int idPhieuDatVe = bookingInfor.getIdPhieuDatVe();
                                            String idGheStr = bookingInfor.getDanhSachIDGhe();
                                            List<Integer> idGheList = new ArrayList<>();

                                            if (idGheStr != null && !idGheStr.trim().isEmpty()) {
                                                String[] idArray = idGheStr.split(",");
                                                for (String id : idArray) {
                                                    try {
                                                        idGheList.add(Integer.parseInt(id.trim()));
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            Map<String, Object> body = new HashMap<>();
                                            body.put("trangThai", 0);
                                            body.put("idGheList", idGheList);

                                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                                            Call<Map<String, Object>> call = apiService.cancelVe(idPhieuDatVe, body);

                                            call.enqueue(new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        Boolean success = (Boolean) response.body().get("success");
                                                        String message = (String) response.body().get("message");

                                                        if (success != null && success) {
                                                            Toast.makeText(getContext(), "Huỷ vé thành công.", Toast.LENGTH_SHORT).show();
                                                            loadDanhSachPhieuDatVe();
                                                        } else {
                                                            Toast.makeText(getContext(), "Huỷ thất bại: " + message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getContext(), "Không thể huỷ vé. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("Huỷ", null)
                                    .show();
                        }
                    });
                    gridViewTicket.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingInfo>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadDanhSachPhieuDatVe() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllPhieuDatVe().enqueue(new Callback<List<BookingInfo>>() {
            @Override
            public void onResponse(Call<List<BookingInfo>> call, Response<List<BookingInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không tải được danh sách phiếu đặt vé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookingInfo>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void capNhatBookingInfo(BookingInfo bookingInfo) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("trangThai", bookingInfo.getTrangThai());

        Call<Map<String, Object>> call = apiService.capNhatTrangThaiVe(bookingInfo.getIdPhieuDatVe(), requestBody);

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
                    Toast.makeText(getContext(), "Cập nhật trạng thái vé thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}



