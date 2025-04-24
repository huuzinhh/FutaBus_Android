package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.model.ChuyenXeResult;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TripAdapter extends ArrayAdapter<ChuyenXeResult> {

    private Context context;
    private int resource;
    private int selectedPosition = -1; // vị trí được chọn

    public TripAdapter(Context context, int resource, List<ChuyenXeResult> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public ChuyenXeResult getSelectedTrip() {
        if (selectedPosition >= 0 && selectedPosition < getCount()) {
            return getItem(selectedPosition);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChuyenXeResult trip = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView tvDateStart = convertView.findViewById(R.id.tvDateStart);
        TextView tvDateEnd = convertView.findViewById(R.id.tvDateEnd);
        TextView tvTimeStart = convertView.findViewById(R.id.tvTimeStart);
        TextView tvTimeEnd = convertView.findViewById(R.id.tvTimeEnd);
        TextView tvFrom = convertView.findViewById(R.id.tvFrom);
        TextView tvTo = convertView.findViewById(R.id.tvTo);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvSeatType = convertView.findViewById(R.id.tvSeatType);
        TextView tvSeatsLeft = convertView.findViewById(R.id.tvSeatsLeft);
        TextView tvDistanceDuration = convertView.findViewById(R.id.tvDistanceDuration);
        TextView tvLichTrinh = convertView.findViewById(R.id.tvLichTrinh);

        if (trip != null) {
            tvTimeStart.setText(DateTimeHelper.toHour(trip.getThoiDiemDi()));
            tvTimeEnd.setText(DateTimeHelper.toHour(trip.getThoiDiemDi()));
            tvDateStart.setText(DateTimeHelper.toDate(trip.getThoiDiemDi()));
            tvDateEnd.setText(DateTimeHelper.toDate(trip.getThoiDiemDi()));
            tvFrom.setText(trip.getTenBenXeDi());
            tvTo.setText(trip.getTenBenXeDen());
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPrice.setText(formatter.format(trip.getGiaHienHanh()) + " VND");
            tvSeatType.setText(trip.getTenLoai());

            if (trip.getSoGheTrong() != null) {
                tvSeatsLeft.setText("Còn " + trip.getSoGheTrong() + " chỗ");
            } else {
                tvSeatsLeft.setText("Đang cập nhật");
            }

            tvDistanceDuration.setText("Thời Gian Dự Kiến: "+formatter.format(trip.getThoiGianDiChuyenTB()) + "H");
            tvLichTrinh.setText("Lịch trình");
        }

        // Highlight item nếu đang được chọn
        if (position == selectedPosition) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.selected_item)); // chọn màu tùy ý
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        convertView.setOnClickListener(v -> {
            if (selectedPosition == position) {
                selectedPosition = -1; // bỏ chọn nếu nhấn lại
            } else {
                selectedPosition = position; // chọn item mới
            }
            notifyDataSetChanged(); // cập nhật lại giao diện
        });


        return convertView;
    }
}
