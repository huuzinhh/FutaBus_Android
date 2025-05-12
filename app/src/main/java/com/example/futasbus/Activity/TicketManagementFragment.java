package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.TicketAdapter;
import com.example.futasbus.R;
import android.widget.GridView;
import android.widget.Toast;
import com.example.futasbus.ApiClient;
import com.example.futasbus.ApiService;
import com.example.futasbus.model.BookingInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class TicketManagementFragment extends Fragment {

    private ImageView iconBack;
    private GridView gridViewTicket;
    private List<BookingInfo> veXeList;
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
                    veXeList = response.body();
                    adapter = new TicketAdapter(requireContext(), veXeList);
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



