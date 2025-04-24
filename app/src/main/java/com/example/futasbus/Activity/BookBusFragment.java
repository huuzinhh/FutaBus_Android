package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.TripAdapter;
import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXeResult;

import java.util.ArrayList;
import java.util.List;
public class BookBusFragment extends Fragment {
    private ListView listView;
    private TripAdapter adapter;
    private TextView tvEmptyMessage;
    public ChuyenXeResult getSelectedTrip() {
        return adapter != null ? adapter.getSelectedTrip() : null;
    }
    private static final String ARG_TRIPS = "trips";

    public static BookBusFragment newInstance(ArrayList<ChuyenXeResult> trips) {
        BookBusFragment fragment = new BookBusFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRIPS, trips);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_bus, container, false);
        ListView listView = view.findViewById(R.id.listViewTrips);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
        List<ChuyenXeResult> trips = (List<ChuyenXeResult>) getArguments().getSerializable(ARG_TRIPS);

        // Gán vào adapter field thay vì khai báo mới
        adapter = new TripAdapter(getContext(), R.layout.item_bus_trip, trips);
        listView.setAdapter(adapter);
        if (trips == null || trips.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            adapter = new TripAdapter(requireContext(), R.layout.item_bus_trip, trips);
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);
        }
        return view;
    }
}

