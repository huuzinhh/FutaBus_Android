package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.Adapter.PurchaseAdapter;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.respone.ListPurchaseResponse;
import com.example.futasbus.respone.PurchaseItem;
import com.example.futasbus.respone.PurchaseItemResponse;
import com.example.futasbus.respone.PurchaseResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private ListView listViewTrips;
    private LinearLayout emptyLayout;
    private TextView tvNotification;
    private PurchaseAdapter purchaseAdapter;
    private List<PurchaseResponse> purchaseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        listViewTrips = v.findViewById(R.id.listViewTrips);
        emptyLayout = v.findViewById(R.id.emptyLayout);
        tvNotification = v.findViewById(R.id.tv_notification);

        purchaseList = new ArrayList<>();
        purchaseAdapter = new PurchaseAdapter(getContext(), R.layout.item_purchase, purchaseList);
        listViewTrips.setAdapter(purchaseAdapter);

        listViewTrips.setOnItemClickListener((parent, view, position, id) -> {
            PurchaseResponse purchase = purchaseList.get(position);
            showPurchaseDetails(purchase);
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        getPurchaseHistory();
    }
    private void showPurchaseDetails(PurchaseResponse purchase) {
        int idPhieuDatVe = purchase.getIdPhieuDatVe();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPurchaseItem(idPhieuDatVe).enqueue(new Callback<PurchaseItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseItemResponse> call, @NonNull Response<PurchaseItemResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    PurchaseItem item = response.body().getData();

                    LayoutInflater inflater = LayoutInflater.from(requireContext());
                    View dialogView = inflater.inflate(R.layout.dialog_purchase_details, null);
                    ((TextView) dialogView.findViewById(R.id.tvMaVe)).setText(String.valueOf(item.getIdPhieuDatVe()));
                    ((TextView) dialogView.findViewById(R.id.tvTuyenXe)).setText(item.getTenTuyen());
                    ((TextView) dialogView.findViewById(R.id.tv_departure_time)).setText(DateTimeHelper.toFullDateTime(item.getThoiDiemDi()));
                    ((TextView) dialogView.findViewById(R.id.tvSoVe)).setText(String.valueOf(item.getSoLuongVe()));
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    ((TextView) dialogView.findViewById(R.id.tvTongTien)).setText(formatter.format(item.getTongTien()) + " VND");
                    ((TextView) dialogView.findViewById(R.id.tvGhe)).setText(item.getDanhSachIDGhe());
                    ((TextView) dialogView.findViewById(R.id.tvHovaTen)).setText(item.getHoTenNguoiDatVe());
                    ((TextView) dialogView.findViewById(R.id.tvSDT)).setText(item.getSdtNguoiDatVe());
                    ((TextView) dialogView.findViewById(R.id.tvEmail)).setText(item.getEmailNguoiDatVe());
                    ((TextView) dialogView.findViewById(R.id.tvtimedatve)).setText(DateTimeHelper.toFullDateTime(item.getThoiGianDatVe()));
                    ((TextView) dialogView.findViewById(R.id.tvBienSoXe)).setText(item.getBienSoXe());
                    ((TextView) dialogView.findViewById(R.id.tvLoaiXe)).setText(item.getLoaiXe());
                    ((TextView) dialogView.findViewById(R.id.tvDiaChiBenXeDi)).setText(item.getDiaChiBenXeDi());
                    ((TextView) dialogView.findViewById(R.id.tvSDTBXDi)).setText(item.getSdtBenXeDi());
                    ((TextView) dialogView.findViewById(R.id.tvDiaChiBenXeDen)).setText(item.getDiaChiBenXeDen());

                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("")
                            .setView(dialogView)
                            .setPositiveButton("Đóng", null)
                            .show();

                } else {
                    Toast.makeText(getContext(), "Không thể tải chi tiết vé.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PurchaseItemResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void getPurchaseHistory() {
        int idNguoiDung = SharedPrefHelper.getUserId(requireContext());

        if (idNguoiDung == -1) {
            // Nếu chưa đăng nhập
            listViewTrips.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            tvNotification.setText("Vui lòng đăng nhập để xem lịch sử");
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPurchaseHistory(idNguoiDung).enqueue(new Callback<ListPurchaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<ListPurchaseResponse> call, @NonNull Response<ListPurchaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PurchaseResponse> trips = response.body().getData();

                    if (trips != null && !trips.isEmpty()) {
                        purchaseList.clear();
                        purchaseList.addAll(trips);
                        purchaseAdapter.notifyDataSetChanged();

                        listViewTrips.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                    } else {
                        listViewTrips.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        tvNotification.setText("Không tìm thấy dữ liệu nào!");
                    }
                } else {
                    listViewTrips.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    tvNotification.setText("Đã có lỗi xảy ra khi tải dữ liệu");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListPurchaseResponse> call, @NonNull Throwable t) {
                listViewTrips.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                tvNotification.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
