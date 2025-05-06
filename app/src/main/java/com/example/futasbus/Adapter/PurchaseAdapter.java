package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.respone.PurchaseResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends ArrayAdapter<PurchaseResponse> {
    private Context context;
    private int resource;

    public PurchaseAdapter(Context context, int resource, List<PurchaseResponse> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseResponse purchase = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tvTenTuyen = convertView.findViewById(R.id.tvTenTuyen);
        TextView tvThoiDiemDi = convertView.findViewById(R.id.tvThoiDiemDi);
        TextView tvSoLuongVe = convertView.findViewById(R.id.tvSoLuongVe);
        TextView tvTongTien = convertView.findViewById(R.id.tvTongTien);

        if (purchase != null) {
            tvTenTuyen.setText(purchase.getTenTuyen());

            // Format lại thời gian nếu cần, còn không thì hiển thị luôn:
            tvThoiDiemDi.setText(DateTimeHelper.toFullDateTime(purchase.getThoiDiemDi()));

            tvSoLuongVe.setText("Số lượng vé: " + purchase.getSoLuongVe());

            NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedPrice = format.format(purchase.getTongTien()) + "đ";
            tvTongTien.setText("Tổng tiền: " + formattedPrice);
        }
        return convertView;
    }
}
