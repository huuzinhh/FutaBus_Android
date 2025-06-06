package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.R;
import com.example.futasbus.model.ChuyenXeResult;
import com.example.futasbus.respone.SelectedSeat;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfirmBookingFragment extends Fragment {

    private static final String ARG_TRIP = "trip";
    private static final String ARG_Seat = "seat";
    private static final String ARG_START_TIME = "start_time";
    private static final String ARG_END_TIME = "end_time";
    private ImageButton btnEditSeat;
    public static ConfirmBookingFragment newInstance(ChuyenXeResult trip, List<SelectedSeat> seats, String starttime, String endtime) {
        ConfirmBookingFragment fragment = new ConfirmBookingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRIP, trip);
        args.putSerializable(ARG_Seat, (Serializable) seats);
        args.putString(ARG_START_TIME, starttime);
        args.putString(ARG_END_TIME, endtime);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_trip_bus_confirm, container, false);

        ChuyenXeResult trip = (ChuyenXeResult) getArguments().getSerializable(ARG_TRIP);
        List<SelectedSeat> seats = (List<SelectedSeat>) getArguments().getSerializable(ARG_Seat);
        String startTime = getArguments().getString(ARG_START_TIME);
        String endTime = getArguments().getString(ARG_END_TIME);
        btnEditSeat = view.findViewById(R.id.btnEditSeats);

        btnEditSeat.setOnClickListener(v -> {
            requireActivity().finish();
        });
        if (trip != null) {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

            ((TextView) view.findViewById(R.id.tvTimeStart)).setText(startTime);
            ((TextView) view.findViewById(R.id.tvTimeEnd)).setText(endTime);
            ((TextView) view.findViewById(R.id.tvPrice)).setText(formatter.format(trip.getGiaHienHanh()) + " VND");
            ((TextView) view.findViewById(R.id.tvFrom)).setText(trip.getTenBenXeDi());
            ((TextView) view.findViewById(R.id.tvTo)).setText(trip.getTenBenXeDen());
            ((TextView) view.findViewById(R.id.tvSeatType)).setText(trip.getTenLoai());
            ((TextView) view.findViewById(R.id.tvDistanceDuration)).setText("Thời Gian Dự Kiến: " + formatter.format(trip.getThoiGianDiChuyenTB()) + "H");
            ((TextView) view.findViewById(R.id.tvSeatNumbers)).setText(getSeatNamesString(seats));

        }

        return view;
    }
    private String getSeatNamesString(List<SelectedSeat> seats) {
        if (seats == null || seats.isEmpty()) return "";
        List<String> seatNames = new ArrayList<>();
        for (SelectedSeat seat : seats) {
            seatNames.add(seat.getTenViTri());
        }
        return String.join(", ", seatNames);
    }

}
