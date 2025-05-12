package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.futasbus.R;
import com.example.futasbus.model.BookingInfo;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<BookingInfo> bookingList;

    public TicketAdapter(Context context, List<BookingInfo> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @Override
    public int getCount() {
        return bookingList.size();
    }

    @Override
    public Object getItem(int i) {
        return bookingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_ticket_management, parent, false);
        }

        TextView tvTenKhach = convertView.findViewById(R.id.tv_ten_khach);
        TextView tvSoDienThoai = convertView.findViewById(R.id.tv_so_dien_thoai);
        TextView tvEmail = convertView.findViewById(R.id.tv_email);
        TextView tvSoLuong = convertView.findViewById(R.id.tv_so_luong);
        TextView tvTongTien = convertView.findViewById(R.id.tv_tong_tien);
        TextView tvTrangThai = convertView.findViewById(R.id.tv_trang_thai);

        ImageView btnView = convertView.findViewById(R.id.btn_view);
        ImageView btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete);

        BookingInfo booking = bookingList.get(i);

        tvTenKhach.setText("Tên khách: " + booking.getHoTen());
        tvSoDienThoai.setText("SĐT: " + booking.getSoDienThoai());
        tvEmail.setText("Email: " + booking.getEmail());
        tvSoLuong.setText("Số lượng vé: " + booking.getSoLuongVe());
        tvTongTien.setText("Tổng tiền: " + formatCurrency(booking.getTongTien()));

        int trangThai = booking.getTrangThai();
        String trangThaiText;

        switch (trangThai) {
            case 0:
                trangThaiText = "Đã hủy";
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
                break;
        }

        tvTrangThai.setText("Trạng thái: " + trangThaiText);

        btnView.setOnClickListener(v -> {
            // TODO: Hiển thị chi tiết vé
        });

        btnEdit.setOnClickListener(v -> {
            // TODO: Mở màn hình sửa vé
        });

        btnDelete.setOnClickListener(v -> {
            // TODO: Hiển thị dialog xác nhận xoá vé
        });

        return convertView;
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}
