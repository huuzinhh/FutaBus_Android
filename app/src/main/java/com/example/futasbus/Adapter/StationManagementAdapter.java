package com.example.futasbus.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.futasbus.R;
import com.example.futasbus.model.BenXe;
import com.example.futasbus.model.ChuyenXe;
import com.example.futasbus.model.QuanHuyen;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StationManagementAdapter extends BaseAdapter {

    private Context context;
    private List<BenXe> benXeList;
    private List<BenXe> originalList;
    private OnBusStationActionListener listener;
    private List<QuanHuyen> quanHuyenList;
    public List<BenXe> getBenXeList() {
        return benXeList;
    }

    public interface OnBusStationActionListener {
        void onView(BenXe benXe );

        void onEdit(BenXe benXe );

        void onDelete(BenXe benXe );
    }

    public StationManagementAdapter(Context context, List<BenXe> benXeList, List<QuanHuyen> quanHuyenList , OnBusStationActionListener listener) {
        this.context = context;
        this.benXeList = benXeList;
        this.originalList = List.copyOf(benXeList);
        this.quanHuyenList = quanHuyenList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return benXeList.size();
    }

    @Override
    public Object getItem(int i) {
        return benXeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_station_managent, parent, false);
        }
        TextView tvBenXe = convertView.findViewById(R.id.tvBenXe);
        TextView tvSdt = convertView.findViewById(R.id.tvSdt);
        TextView tvQuanHuyen = convertView.findViewById(R.id.tvQuanHuyen);
        BenXe benXe  = benXeList.get(i);
        tvBenXe.setText( benXe.getTenBenXe());
        tvSdt.setText(benXe.getSoDienThoai());
        tvQuanHuyen.setText(getTenQuanHuyen(benXe.getIdQuanHuyen()));

        LinearLayout layoutThongTin = convertView.findViewById(R.id.layoutThongTin);
        layoutThongTin.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Thông tin bến xe");

            String tenBenXe = benXe.getTenBenXe() != null ? benXe.getTenBenXe() : "Chưa rõ";
            String diaChi = benXe.getDiaChi() != null ? benXe.getDiaChi() : "Chưa rõ";
            String SDT = benXe.getSoDienThoai() != null ? benXe.getSoDienThoai() : "Chưa rõ";
            String quanHuyen = benXe.getIdQuanHuyen()!=1 && benXe.getIdQuanHuyen() !=0 ? getTenQuanHuyen(benXe.getIdQuanHuyen()) : "Chưa rõ";



// Tạo nội dung chi tiết
            String thongTin = "Tên bến xe: " + tenBenXe + "\n"
                    + "Địa chỉ: " + diaChi + "\n"
                    + "Số điện thoại: " + SDT + "\n"
                    + "Quận huyện: " + quanHuyen + "\n";


// Hiển thị AlertDialog
            new AlertDialog.Builder(context)
                    .setTitle("Chi tiết bến xe")
                    .setMessage(thongTin)
                    .setPositiveButton("Đóng", null)
                    .show();

        });

        ImageView btnEdit = convertView.findViewById(R.id.btnSua);
        ImageView btnDelete = convertView.findViewById(R.id.btnXoa);
// Gọi hàm sửa
        btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(benXe);
            }
        });

// Gọi hàm xóa với confirm
        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa bến xe này?")
                        .setPositiveButton("Xóa", (dialog, which) -> listener.onDelete(benXe))
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        return convertView;
    }
    private String getTenQuanHuyen(int idQuanHuyen) {
        for (QuanHuyen qh : quanHuyenList) {
            if (qh.getIdQuanHuyen() == idQuanHuyen) {
                return qh.getTenQuanHuyen()+" - " +qh.getTinhThanh().getTenTinh();
            }
        }
        return "Không rõ";
    }
    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault()).trim();
        benXeList.clear();

        if (query.isEmpty()) {
            benXeList.addAll(originalList);
        } else {
            for (BenXe bx : originalList) {
                String tenBenXe = safeLower(bx.getTenBenXe());
                String diaChi = safeLower(bx.getDiaChi());
                String soDienThoai = safeLower(bx.getSoDienThoai());
                String idBenXe = String.valueOf(bx.getIdBenXe());
                String idQuanHuyen = String.valueOf(bx.getIdQuanHuyen());
                String trangThai = String.valueOf(bx.getTrangThai());

                if (tenBenXe.contains(query) || diaChi.contains(query) || soDienThoai.contains(query)
                        || idBenXe.contains(query) || idQuanHuyen.contains(query) || trangThai.contains(query)) {
                    benXeList.add(bx);
                }
            }
        }

        notifyDataSetChanged();
    }


    // Hàm hỗ trợ để tránh lỗi null
    private String safeLower(String input) {
        return input == null ? "" : input.toLowerCase(Locale.getDefault()).trim();
    }

}
