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
import com.example.futasbus.respone.SelectedSeat;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFragment extends Fragment {

    private static final String ARG_DIRECTION = "direction";
    private GridLayout gridLayoutAbove;
    private GridLayout gridLayoutUnder;
    private final List<SelectedSeat> selectedSeats = new ArrayList<>();
    private List<SelectedSeat> allSeats = new ArrayList<>();
    private List<String> soldSeats = new ArrayList<>();
    private int MAX_SEATS;
    public static SeatSelectionFragment newInstance(String direction,ArrayList<SelectedSeat>AllSeat, ArrayList<String> soldSeats, int maxSeats) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIRECTION, direction);
        args.putSerializable("ALL_SEATS", AllSeat);
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
            MAX_SEATS = getArguments().getInt("MAX_SEATS", 1);
            allSeats = (ArrayList<SelectedSeat>) getArguments().getSerializable("ALL_SEATS");
        }
        createSeatButtons();
        return view;
    }
    public interface OnSeatSelectedListener {
        void onSeatSelected(String tripType, List<SelectedSeat> selectedSeats);
    }

    private void createSeatButtons() {
        gridLayoutAbove.removeAllViews();
        gridLayoutUnder.removeAllViews();

        gridLayoutAbove.setColumnCount(3);
        gridLayoutUnder.setColumnCount(3);

        int rowA = 0, colA = 0;
        int rowB = 0, colB = 0;

        for (SelectedSeat seat : allSeats) {
            String name = seat.getTenViTri();
            if (name == null || name.length() < 2) continue;

            char tầng = name.charAt(0); // 'A' hoặc 'B'

            // Bỏ ghế giữa hàng đầu tiên (row == 0, col == 1)
            if (tầng == 'A') {
                if (rowA == 0 && colA == 1) colA++; // bỏ cột giữa hàng đầu tiên

                Button btn = createSeatButton(seat, rowA, colA);
                gridLayoutAbove.addView(btn);

                colA++;
                if (colA == 3) { colA = 0; rowA++; }

            } else if (tầng == 'B') {
                if (rowB == 0 && colB == 1) colB++; // bỏ cột giữa hàng đầu tiên

                Button btn = createSeatButton(seat, rowB, colB);
                gridLayoutUnder.addView(btn);

                colB++;
                if (colB == 3) { colB = 0; rowB++; }
            }
        }
    }


    private Button createSeatButton(SelectedSeat selectedSeat, int row, int col) {
        Button seatBtn = new Button(getContext());
        seatBtn.setText(selectedSeat.getTenViTri());
        seatBtn.setTag(selectedSeat); // Gán đối tượng SelectedSeat vào tag

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(col, 1f);
        params.rowSpec = GridLayout.spec(row);
        params.setMargins(2, 2, 2, 2);
        seatBtn.setLayoutParams(params);

        // Kiểm tra xem ghế đã được bán chưa (sử dụng thông tin từ đối tượng SelectedSeat)
        if (soldSeats.contains(selectedSeat.getTenViTri())) {
            seatBtn.setBackgroundResource(R.drawable.ic_chair_unavalable);
            seatBtn.setEnabled(false);
        } else {
            seatBtn.setBackgroundResource(R.drawable.ic_chair_available);
            seatBtn.setOnClickListener(v -> onSeatClicked((Button) v));
        }

        return seatBtn;
    }

    private void onSeatClicked(Button seatBtn) {
        SelectedSeat seat = (SelectedSeat) seatBtn.getTag();

        if (selectedSeats.contains(seat)) {
            selectedSeats.remove(seat);
            seatBtn.setBackgroundResource(R.drawable.ic_chair_available);
        } else {
            if (selectedSeats.size() >= MAX_SEATS) {
                Toast.makeText(getContext(), "Bạn chỉ được chọn " + MAX_SEATS + " ghế", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedSeats.add(seat);
            seatBtn.setBackgroundResource(R.drawable.ic_chair_seleted);
            Log.d("seatSelect", "onSeatClicked: " + seat.getTenViTri());
            Log.d("seatSelect", "onSeatClicked: " + seat.getIdViTri());
        }

        if (seatSelectedListener != null) {
            seatSelectedListener.onSeatSelected(direction, selectedSeats);
        }
    }


}
