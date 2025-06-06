package com.example.futasbus.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.TuyenXe;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TripManagementAdapter extends BaseAdapter {
    private Context context;
    private List<ChuyenXe> chuyenXeList;
    private List<ChuyenXe> originalList;

    private TripManagementAdapter.OnBusTripActionListener listener;

    public interface OnBusTripActionListener {
        void onView(ChuyenXe chuyenXe);

        void onEdit(ChuyenXe chuyenXe);

        void onDelete(ChuyenXe chuyenXe);
    }

    public TripManagementAdapter(Context context, List<ChuyenXe> chuyenXeList, TripManagementAdapter.OnBusTripActionListener listener) {
        this.context = context;
        this.chuyenXeList = chuyenXeList;
        this.originalList = List.copyOf(chuyenXeList);
        this.listener = listener;
    }

    public List<ChuyenXe> getChuyenXeList() {
        return chuyenXeList;
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
        TextView tvTuyenXe = convertView.findViewById(R.id.tvChuyenXe);
        TextView tvThoiGianDi = convertView.findViewById(R.id.tvThoiGianDi);
        TextView tvThoiGianDen = convertView.findViewById(R.id.tvThoiGianDen);
        TextView tvGiaVe = convertView.findViewById(R.id.tvGiaVe);
        ChuyenXe chuyenXe = chuyenXeList.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

        tvTuyenXe.setText("Tuyến " + chuyenXe.getTuyenXe().getTenTuyen());
        tvThoiGianDi.setText("Từ " + sdf.format(chuyenXe.getThoiDiemDi()));
        tvThoiGianDen.setText("Đến "+sdf.format(chuyenXe.getThoiDiemDen()));
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvGiaVe.setText("Giá vé: " + currencyFormat.format(chuyenXe.getGiaVe() ));


        LinearLayout layoutThongTin = convertView.findViewById(R.id.layoutThongTin);
        layoutThongTin.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Thông tin chuyến xe");



// Lấy thông tin từ đối tượng ChuyenXe
            String tenTuyen = chuyenXe.getTuyenXe() != null ? chuyenXe.getTuyenXe().getTenTuyen() : "Chưa rõ";
            String thoiGianDi = chuyenXe.getThoiDiemDi() != null ? sdf.format(chuyenXe.getThoiDiemDi()) : "Chưa rõ";
            String thoiGianDen = chuyenXe.getThoiDiemDen() != null ? sdf.format(chuyenXe.getThoiDiemDen()) : "Chưa rõ";
            String giaVe = chuyenXe.getGiaVe() != null ? currencyFormat.format(chuyenXe.getGiaVe()) : "Chưa rõ";
            String bienSoXe = chuyenXe.getXe() != null ? chuyenXe.getXe().getBienSo() : "Chưa rõ";
            String tenTaiXe = chuyenXe.getTaiXe() != null ? chuyenXe.getTaiXe().getHoTen() : "Chưa rõ";

// Tạo nội dung chi tiết
            String thongTin = "Tuyến: " + tenTuyen + "\n"
                    + "Thời gian đi: " + thoiGianDi + "\n"
                    + "Thời gian đến: " + thoiGianDen + "\n"
                    + "Giá vé: " + giaVe + "\n"
                    + "Xe: " + bienSoXe + "\n"
                    + "Tài xế: " + tenTaiXe;

// Hiển thị AlertDialog
            new AlertDialog.Builder(context)
                    .setTitle("Chi tiết chuyến xe")
                    .setMessage(thongTin)
                    .setPositiveButton("Đóng", null)
                    .show();

        });

        ImageView btnEdit = convertView.findViewById(R.id.btnSua);
        ImageView btnDelete = convertView.findViewById(R.id.btnXoa);

// Gọi hàm sửa
        btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(chuyenXe);
            }
        });

// Gọi hàm xóa với confirm
        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa chuyến xe này?")
                        .setPositiveButton("Xóa", (dialog, which) -> listener.onDelete(chuyenXe))
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });


        return convertView;
    }
    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault());
        chuyenXeList.clear();

        if (query.trim().isEmpty()) {
            chuyenXeList.addAll(originalList);
        } else {
            for (ChuyenXe cx : originalList) {
                String tenTuyen = cx.getTuyenXe() != null ? cx.getTuyenXe().getTenTuyen().toLowerCase() : "";
                String thoiGianDi = cx.getThoiDiemDi() != null ? new SimpleDateFormat("HH:mm dd/MM/yyyy").format(cx.getThoiDiemDi()).toLowerCase() : "";
                String thoiGianDen = cx.getThoiDiemDen() != null ? new SimpleDateFormat("HH:mm dd/MM/yyyy").format(cx.getThoiDiemDen()).toLowerCase() : "";
                String giaVe = cx.getGiaVe() != null ? String.valueOf(cx.getGiaVe()) : "";
                String bienSoXe = cx.getXe() != null ? cx.getXe().getBienSo().toLowerCase() : "";
                String tenTaiXe = cx.getTaiXe() != null ? cx.getTaiXe().getHoTen().toLowerCase() : "";

                if (tenTuyen.contains(query) || thoiGianDi.contains(query) || thoiGianDen.contains(query)
                        || giaVe.contains(query) || bienSoXe.contains(query) || tenTaiXe.contains(query)) {
                    chuyenXeList.add(cx);
                }
            }
        }

        notifyDataSetChanged();
    }

}




