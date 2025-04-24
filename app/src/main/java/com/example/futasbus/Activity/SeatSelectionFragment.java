package com.example.futasbus.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futasbus.R;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFragment extends Fragment {

    private static final String ARG_DIRECTION = "direction";
    private GridLayout gridLayoutAbove;
    private GridLayout gridLayoutUnder;
    private final List<String> selectedSeats = new ArrayList<>();
    private List<String> soldSeats = new ArrayList<>();
    private int MAX_SEATS;
    public static SeatSelectionFragment newInstance(String direction, ArrayList<String> soldSeats, int maxSeats) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIRECTION, direction);
        args.putStringArrayList("SOLD_SEATS", soldSeats);
        args.putInt("MAX_SEATS", maxSeats);
        fragment.setArguments(args);
        return fragment;
    }
    private OnSeatSelectedListener seatSelectedListener;
    private String direction; // để biết đây là "go" hay "return"

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            direction = getArguments().getString(ARG_DIRECTION, "go");
        }
    }

    public void setOnSeatSelectedListener(OnSeatSelectedListener listener) {
        this.seatSelectedListener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);
        gridLayoutAbove = view.findViewById(R.id.gridLayoutAbove);
        gridLayoutUnder = view.findViewById(R.id.gridLayoutUnder);

        if (getArguments() != null) {
            soldSeats = getArguments().getStringArrayList("SOLD_SEATS");
            if (soldSeats == null) soldSeats = new ArrayList<>();
        }

        if (getArguments() != null) {
            soldSeats = getArguments().getStringArrayList("SOLD_SEATS");
            if (soldSeats == null) soldSeats = new ArrayList<>();
            MAX_SEATS = getArguments().getInt("MAX_SEATS", 1);
        }

        createSeatButtons();
        return view;
    }
    public interface OnSeatSelectedListener {
        void onSeatSelected(String tripType, List<String> selectedSeats);
    }


    private void createSeatButtons() {
        gridLayoutAbove.removeAllViews();
        gridLayoutUnder.removeAllViews();

        gridLayoutAbove.setColumnCount(3);
        gridLayoutUnder.setColumnCount(3);

        // ===== Tầng A =====
        int seatNumberA = 1;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 3; col++) {
                if (row == 0 && col == 1) continue; // Bỏ ghế giữa ở hàng đầu
                if (seatNumberA > 17) break;

                Button seatBtn = createSeatButton("A", seatNumberA, row, col);
                gridLayoutAbove.addView(seatBtn);
                seatNumberA++;
            }
        }

        // ===== Tầng B =====
        int seatNumberB = 1;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 3; col++) {
                if (row == 0 && col == 1) continue;
                if (seatNumberB > 17) break;

                Button seatBtn = createSeatButton("B", seatNumberB, row, col);
                gridLayoutUnder.addView(seatBtn);
                seatNumberB++;
            }
        }
    }

    private Button createSeatButton(String prefix, int seatNumber, int row, int col) {
        Button seatBtn = new Button(getContext());
        String seatId = prefix + seatNumber;
        seatBtn.setText(seatId);
        seatBtn.setTag(seatId);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row);
        params.setMargins(2, 2, 2, 2);
        seatBtn.setLayoutParams(params);

        if (soldSeats.contains(seatId)) {
            seatBtn.setBackgroundResource(R.drawable.ic_chair_unavalable);
            seatBtn.setEnabled(false);
        } else {
            seatBtn.setBackgroundResource(R.drawable.ic_chair_available);
            seatBtn.setOnClickListener(v -> onSeatClicked((Button) v));
        }

        return seatBtn;
    }

    private void onSeatClicked(Button seatBtn) {
        String seatId = (String) seatBtn.getTag();
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            seatBtn.setBackgroundResource(R.drawable.ic_chair_available);
        } else {
            if (selectedSeats.size() >= MAX_SEATS) {
                Toast.makeText(getContext(), "Bạn chỉ được chọn " + MAX_SEATS + " ghế", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedSeats.add(seatId);
            seatBtn.setBackgroundResource(R.drawable.ic_chair_seleted);
            Log.d("seatSelect", "onSeatClicked: "+seatId);
        }
        if (seatSelectedListener != null) {
            seatSelectedListener.onSeatSelected(direction, selectedSeats);
        }
    }
}
