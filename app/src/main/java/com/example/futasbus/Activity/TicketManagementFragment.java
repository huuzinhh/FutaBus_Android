package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
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
import com.example.futasbus.model.PhieuDatVe;
import com.example.futasbus.model.TuyenXe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
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
    private List<BookingInfo> filteredList = new ArrayList<>();
    private List<BookingInfo> displayList = new ArrayList<>();
    private ImageView imgPrint;

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

        EditText edtSearchTicket = view.findViewById(R.id.edtSearchTicket);
        edtSearchTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTicket(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Spinner spinnerTrangThai = view.findViewById(R.id.spinnerStatus);

        List<String> trangThaiList = Arrays.asList("Tất cả", "Đã huỷ", "Đã đặt", "Chờ thanh toán", "Đã thanh toán", "Hoàn tất");
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, trangThaiList);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(statusAdapter);

        spinnerTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterBookingListByStatus(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        apiService.getAllPhieuDatVe().enqueue(new Callback<List<BookingInfo>>() {
            @Override
            public void onResponse(Call<List<BookingInfo>> call, Response<List<BookingInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingList = response.body();
                    filteredList = new ArrayList<>(bookingList);
                    adapter = new TicketAdapter(requireContext(), filteredList, new TicketAdapter.OnTicketActionListener() {
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

                            ImageView imgPrint = view.findViewById(R.id.img_print);
                            imgPrint.setOnClickListener(v -> {
                                View ticketView = inflater.inflate(R.layout.ticket_layout, null);
                                showTicketOnLayout(ticketView, bookingInfo);
                                saveTicketViewAsPdf(ticketView, "VeXe_" + System.currentTimeMillis());
                            });

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

    private void filterTicket(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(displayList);
        } else {
            for (BookingInfo bookingInfo : displayList) {
                if (bookingInfo.getHoTen().toLowerCase().contains(query.toLowerCase())
                        || bookingInfo.getSoDienThoai().toLowerCase().contains(query.toLowerCase())
                        || bookingInfo.getEmail().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(bookingInfo);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void filterBookingListByStatus(int statusIndex) {
        filteredList.clear();

        if (bookingList == null) {
            Log.e("TicketManagement", "bookingList is null!");
            return;
        }

        if (statusIndex == 0) {
            filteredList.addAll(bookingList);
        } else {
            int actualStatus = statusIndex - 1;
            for (BookingInfo booking : bookingList) {
                if (booking.getTrangThai() == actualStatus) {
                    filteredList.add(booking);
                }
            }
        }

        displayList.clear();
        displayList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private String formatDateTime(String inputDate) {
        String[] possibleFormats = {
                "yyyy-MM-dd HH:mm:ss.S",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm"
        };

        for (String format : possibleFormats) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat(format, Locale.getDefault());
                Date parsedDate = inputFormat.parse(inputDate);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(parsedDate);
            } catch (ParseException e) {
            }
        }

        return inputDate;
    }

    private String generateTicketContent(BookingInfo bookingInfo) {
        String trangThaiText;

        switch (bookingInfo.getTrangThai()) {
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

        String thoiDiemDiFormatted = formatDateTime(bookingInfo.getThoiDiemDi());
        String thoiDiemDenFormatted = formatDateTime(bookingInfo.getThoiDiemDen());
        String thoiGianDatVeFormatted = formatDateTime(bookingInfo.getThoiGianDatVe());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedGiaVe = currencyFormat.format(bookingInfo.getGiaVe());
        String formattedTongTien = currencyFormat.format(bookingInfo.getTongTien());

        return "PHIẾU ĐẶT VÉ\n"
                + "-------------------------\n"
                + "Họ tên: " + bookingInfo.getHoTen() + "\n"
                + "Số điện thoại: " + bookingInfo.getSoDienThoai() + "\n"
                + "Email: " + bookingInfo.getEmail() + "\n"
                + "Thời điểm đi: " + thoiDiemDiFormatted + "\n"
                + "Thời điểm đến: " + thoiDiemDenFormatted + "\n"
                + "Tên tuyến: " + bookingInfo.getTenTuyen() + "\n"
                + "Bến đi: " + bookingInfo.getBenDi() + "\n"
                + "Bến đến: " + bookingInfo.getBenDen() + "\n"
                + "Biển số xe: " + bookingInfo.getBienSoXe() + "\n"
                + "Loại xe: " + bookingInfo.getLoaiXe() + "\n"
                + "Số lượng vé: " + bookingInfo.getSoLuongVe() + "\n"
                + "Danh sách ghế: " + bookingInfo.getDanhSachGhe() + "\n"
                + "Giá vé: " + formattedGiaVe + "\n"
                + "Tổng tiền: " + formattedTongTien + "\n"
                + "Thời gian đặt vé: " + thoiGianDatVeFormatted + "\n"
                + "Trạng thái: " + trangThaiText + "\n";
    }

    public String removeVietnameseDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private void saveToFile(String content, BookingInfo bookingInfo, Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        String hoTen = removeVietnameseDiacritics(bookingInfo.getHoTen())
                .replaceAll("[^a-zA-Z0-9]", "_");
        String fileName = "Phieu_" + hoTen + "_" + timeStamp + ".txt";

        File path = new File(context.getExternalFilesDir(null), "Tickets");
        if (!path.exists()) {
            path.mkdirs();
        }

        File file = new File(path, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            Toast.makeText(context, "Xuất vé thành công!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi ghi file", Toast.LENGTH_SHORT).show();
        }
    }

    private void showTicketOnLayout(View view, BookingInfo bookingInfo) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        ((TextView) view.findViewById(R.id.tv_ho_ten)).setText("Họ tên: " + bookingInfo.getHoTen());
        ((TextView) view.findViewById(R.id.tv_so_dien_thoai)).setText("Số điện thoại: " + bookingInfo.getSoDienThoai());
        ((TextView) view.findViewById(R.id.tv_email)).setText("Email: " + bookingInfo.getEmail());
        ((TextView) view.findViewById(R.id.tv_thoi_diem_di)).setText("Thời điểm đi: " + formatDateTime(bookingInfo.getThoiDiemDi()));
        ((TextView) view.findViewById(R.id.tv_thoi_diem_den)).setText("Thời điểm đến: " + formatDateTime(bookingInfo.getThoiDiemDen()));
        ((TextView) view.findViewById(R.id.tv_ten_tuyen)).setText(bookingInfo.getTenTuyen());
        ((TextView) view.findViewById(R.id.tv_ben_di)).setText("Nơi đi: " + bookingInfo.getBenDi());
        ((TextView) view.findViewById(R.id.tv_ben_den)).setText("Nơi đến: " + bookingInfo.getBenDen());
        ((TextView) view.findViewById(R.id.tv_bien_so_xe)).setText("Biển số xe: " + bookingInfo.getBienSoXe());
        ((TextView) view.findViewById(R.id.tv_loai_xe)).setText("Loại xe: " + bookingInfo.getLoaiXe());
        ((TextView) view.findViewById(R.id.tv_so_luong_ve)).setText("Số lượng vé: " + bookingInfo.getSoLuongVe());
        ((TextView) view.findViewById(R.id.tv_danh_sach_ghe)).setText("Ghế: " + bookingInfo.getDanhSachGhe());
        ((TextView) view.findViewById(R.id.tv_gia_ve)).setText("Giá vé: " + currencyFormat.format(bookingInfo.getGiaVe()));
        ((TextView) view.findViewById(R.id.tv_tong_tien)).setText("Tổng tiền: " + currencyFormat.format(bookingInfo.getTongTien()));
        ((TextView) view.findViewById(R.id.tv_thoi_gian_dat)).setText("Thời gian đặt vé: " + formatDateTime(bookingInfo.getThoiGianDatVe()));
        ((TextView) view.findViewById(R.id.tv_trang_thai)).setText("Trạng thái: " + getTrangThaiText(bookingInfo.getTrangThai()));
    }

    private String getTrangThaiText(int trangThai) {
        switch (trangThai) {
            case 0: return "Đã huỷ";
            case 1: return "Đã đặt";
            case 2: return "Chờ thanh toán";
            case 3: return "Đã thanh toán";
            case 4: return "Hoàn tất";
            default: return "Không xác định";
        }
    }

    private void saveTicketViewAsPdf(View ticketView, String fileName) {
        // Đo và layout View
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY); // hoặc kích thước bạn mong muốn
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        ticketView.measure(widthSpec, heightSpec);
        ticketView.layout(0, 0, ticketView.getMeasuredWidth(), ticketView.getMeasuredHeight());

        // Tạo bitmap từ View
        Bitmap bitmap = Bitmap.createBitmap(ticketView.getMeasuredWidth(), ticketView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        ticketView.draw(canvas);

        // Tạo PDF
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas pdfCanvas = page.getCanvas();
        pdfCanvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // Lưu file
        File dir = new File(requireContext().getExternalFilesDir(null), "tickets");
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName + ".pdf");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            Toast.makeText(requireContext(), "Xuất vé PDF thành công!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Lỗi khi lưu PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }




}



