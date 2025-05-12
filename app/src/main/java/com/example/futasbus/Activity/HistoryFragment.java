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
import com.example.futasbus.helper.SharedPrefHelper;
import com.example.futasbus.respone.ListPurchaseResponse;
import com.example.futasbus.respone.PurchaseResponse;

import java.util.ArrayList;
import java.util.List;

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
        return v;

    }
    @Override
    public void onResume() {
        super.onResume();
        getPurchaseHistory();
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
