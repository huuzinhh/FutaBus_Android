package com.example.futasbus.Adapter;

import android.content.Context;
import android.util.Log;
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
    private int selectedPosition = -1;

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

        if (trip != null) {
            tvTimeStart.setText(DateTimeHelper.toHour(trip.getThoiDiemDi()));
            tvTimeEnd.setText(DateTimeHelper.toHour(trip.getThoiDiemDen()));
            tvDateStart.setText(DateTimeHelper.toDate(trip.getThoiDiemDi()));
            tvDateEnd.setText(DateTimeHelper.toDate(trip.getThoiDiemDen()));
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
        }

        // Highlight item nếu đang được chọn
        if (position == selectedPosition) {
            convertView.setBackground(context.getResources().getDrawable(R.drawable.bg_stroke_select_item_card));
        } else {
            convertView.setBackground(context.getResources().getDrawable(R.drawable.bg_stroke_black_item_card));
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
