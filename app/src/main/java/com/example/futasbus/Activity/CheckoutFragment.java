package com.example.futasbus.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.model.ChuyenXeResult;
import com.example.futasbus.respone.SelectedSeat;
import com.example.futasbus.respone.TicketResponse;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutFragment extends Fragment {
    private static final String ARG_TICKET = "ticket";
    private static final String ARG_TRIP = "trip";
    private static final String ARG_Seat = "seat";
    private static final String ARG_Price = "price";

    public static CheckoutFragment newInstance(TicketResponse ticketResponse, ChuyenXeResult trip, List<SelectedSeat> seats, double price) {
        CheckoutFragment fragment = new CheckoutFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TICKET, ticketResponse);
        args.putSerializable(ARG_TRIP, trip);
        args.putSerializable(ARG_Seat, (Serializable) seats);
        args.putDouble(ARG_Price, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_checkout, container, false);
        TicketResponse ticketResponse = (TicketResponse) getArguments().getSerializable(ARG_TICKET);
        ChuyenXeResult trip = (ChuyenXeResult) getArguments().getSerializable(ARG_TRIP);
        List<SelectedSeat> seats = (List<SelectedSeat>) getArguments().getSerializable(ARG_Seat);
        double price = getArguments().getDouble(ARG_Price);

        if (trip != null) {
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

            ((TextView) view.findViewById(R.id.tvTuyenXe)).setText(trip.getTenBenXeDi()+" - "+trip.getTenBenXeDen());
            ((TextView) view.findViewById(R.id.tvThoiGianXuatBen)).setText(DateTimeHelper.toFullDateTime(trip.getThoiDiemDi()));
            ((TextView) view.findViewById(R.id.tvSoLuongGhe)).setText(ticketResponse.getTickets()+ " Ghế");
            ((TextView) view.findViewById(R.id.tvSoGhe)).setText(getSeatNamesString(seats));
            ((TextView) view.findViewById(R.id.tvDiemLenXe)).setText(trip.getTenBenXeDi());
            ((TextView) view.findViewById(R.id.tvThoiGianDon)).setText(DateTimeHelper.toFullDateTime(trip.getThoiDiemDi()));
            ((TextView) view.findViewById(R.id.tvDiemTra)).setText(trip.getTenBenXeDen());
            ((TextView) view.findViewById(R.id.tvTongTien)).setText(formatter.format(trip.getGiaHienHanh()) + " VND");

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
