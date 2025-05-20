package com.example.futasbus.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.futasbus.Adapter.TripAdapter;
import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXeResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class BookBusFragment extends Fragment {
    private ListView listView;
    Spinner spinnerPrice, spinnerSeatType, spinnerTime;

    private TripAdapter adapter;
    private List<ChuyenXeResult> allTrips,originalTrips = new ArrayList<>();;


    private LinearLayout emptyLayout;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_bus, container, false);
        listView = view.findViewById(R.id.listViewTrips);
        emptyLayout = view.findViewById(R.id.EmptyLayout);

        spinnerPrice     = view.findViewById(R.id.spinnerPrice);
        spinnerSeatType  = view.findViewById(R.id.spinnerSeatType);
        spinnerTime      = view.findViewById(R.id.spinnerTime);

        setupSpinners();

        List<ChuyenXeResult> trips = (List<ChuyenXeResult>) getArguments().getSerializable(ARG_TRIPS);

        allTrips = trips != null ? new ArrayList<>(trips) : new ArrayList<>();
        originalTrips = new ArrayList<>(allTrips); // Lưu bản gốc không thay đổi
        adapter  = new TripAdapter(requireContext(),
                R.layout.item_bus_trip,
                allTrips);
        listView.setAdapter(adapter);

        if (allTrips.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        return view;
    }

    private void setupSpinners() {
        // Dữ liệu mẫu
        String[] priceOptions = {"Tất cả", "Giá tăng dần", "Giả giảm dần"};
        String[] seatTypeOptions = {"Tất cả", "Ghế", "Giường", "Limousine"};
        String[] timeOptions = {"Tất cả", "Buổi Sáng (00:00 - 11:59)", "Buổi Chiều (12:00 - 17:59)", "Buổi Tối (18:00 - 23:59)"};

        // Adapter cho Spinner Giá
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, priceOptions);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrice.setAdapter(priceAdapter);

        // Adapter cho Spinner Loại ghế
        ArrayAdapter<String> seatTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, seatTypeOptions);
        seatTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeatType.setAdapter(seatTypeAdapter);

        // Adapter cho Spinner Giờ
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeOptions);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        // Xử lý sự kiện khi chọn item (nếu cần lọc dữ liệu thật)
        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTrips();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerSeatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTrips();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTrips();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
    private void filterTrips() {
        String selectedPrice = spinnerPrice.getSelectedItem().toString();
        String selectedSeatType = spinnerSeatType.getSelectedItem().toString();
        String selectedTime = spinnerTime.getSelectedItem().toString();

        List<ChuyenXeResult> filtered = new ArrayList<>();
        for (ChuyenXeResult trip : originalTrips) {
            boolean match = true;

            // Lọc theo loại ghế nếu không chọn "Tất cả"
            if (!selectedSeatType.equals("Tất cả") && !trip.getTenLoai().equalsIgnoreCase(selectedSeatType)) {
                Log.d("SEAT_TYPE", "Trip seat type: " + trip.getTenLoai());
                match = false;
            }

            // Lọc theo thời gian nếu không chọn "Tất cả"
            if (!selectedTime.equals("Tất cả")) {
                int hour = Integer.parseInt(trip.getThoiDiemDi().substring(11, 13)); // Định dạng "yyyy-MM-dd HH:mm:ss"
                switch (selectedTime) {
                    case "Buổi Sáng (00:00 - 11:59)":
                        if (hour < 0 || hour > 11) match = false;
                        break;
                    case "Buổi Chiều (12:00 - 17:59)":
                        if (hour < 12 || hour > 17) match = false;
                        break;
                    case "Buổi Tối (18:00 - 23:59)":
                        if (hour < 18 || hour > 23) match = false;
                        break;
                }
            }

            if (match) filtered.add(trip);
        }

        // Sắp xếp nếu không chọn "Tất cả"
        if (selectedPrice.equals("Giá tăng dần")) {
            filtered.sort(Comparator.comparingDouble(ChuyenXeResult::getGiaHienHanh));
        } else if (selectedPrice.equals("Giả giảm dần")) {
            filtered.sort(Comparator.comparingDouble(ChuyenXeResult::getGiaHienHanh).reversed());
        }

        adapter.clear();
        adapter.addAll(filtered);
        adapter.notifyDataSetChanged();

        // Hiện hoặc ẩn layout trống
        if (filtered.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }



}

