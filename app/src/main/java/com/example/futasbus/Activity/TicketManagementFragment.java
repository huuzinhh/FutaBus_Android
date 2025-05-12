package com.example.futasbus.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.example.futasbus.Adapter.CustomerAdapter;
import com.example.futasbus.Adapter.TicketAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.BookingInfo;
import com.example.futasbus.model.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                        public void onEdit(BookingInfo bookingInfor) {
                            Toast.makeText(getContext(), "Sửa: " + bookingInfor.getHoTen(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onDelete(BookingInfo bookingInfor) {
                            Toast.makeText(getContext(), "Xoá: " + bookingInfor.getHoTen(), Toast.LENGTH_SHORT).show();
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
}



