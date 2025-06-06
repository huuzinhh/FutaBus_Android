package com.example.futasbus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.TuyenXe;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TripManagementAdapter extends BaseAdapter {
    private Context context;
    private List<ChuyenXe> chuyenXeList;
    private TripManagementAdapter.OnBusTripActionListener listener;

    public interface OnBusTripActionListener {
        void onView(ChuyenXe chuyenXe);

        void onEdit(ChuyenXe chuyenXe);

        void onDelete(ChuyenXe chuyenXe);
    }

    public TripManagementAdapter(Context context, List<ChuyenXe> chuyenXeList, TripManagementAdapter.OnBusTripActionListener listener) {
        this.context = context;
        this.chuyenXeList = chuyenXeList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return chuyenXeList.size();
    }

    @Override
    public Object getItem(int i) {
        return chuyenXeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_trip_managent, parent, false);
        }
        TextView tvTuyenXe = convertView.findViewById(R.id.tvTuyenXe);
        TextView tvThoiGian = convertView.findViewById(R.id.tvThoiGian);
        TextView tvGiaVe = convertView.findViewById(R.id.tvGiaVe);
        ChuyenXe chuyenXe = chuyenXeList.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

        tvTuyenXe.setText("Tuyến " + chuyenXe.getTuyenXe().getTenTuyen());
        tvThoiGian.setText("Từ " + sdf.format(chuyenXe.getThoiDiemDi()) + " đến " + sdf.format(chuyenXe.getThoiDiemDen()));
        tvGiaVe.setText("Giá vé: " + chuyenXe.getGiaVe() + "đ");



        return convertView;
    }
}




