package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.futasbus.R;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.model.ChuyenXeResult;
import com.example.futasbus.model.NguoiDung;
import com.example.futasbus.respone.SelectedSeat;
import com.example.futasbus.respone.TicketResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThankYouActivity extends AppCompatActivity {
    private double priceGo = 0, priceReturn = 0;
    private NguoiDung user;
    private Button btnConfirm;
    private TicketResponse ticketResponse;
    private ChuyenXeResult selectedGo, selectedReturn;
    private FrameLayout layoutGo, layoutReturn;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        layoutGo = findViewById(R.id.layoutGo);
        layoutReturn = findViewById(R.id.layoutReturn);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        handleIntentData();
        if (selectedGo != null) {
            View itemView = createTicketView(ticketResponse,selectedGo, priceGo, "Lượt đi");
            layoutGo.addView(itemView);
        }

        // Inflate lượt về nếu có
        if (selectedReturn != null) {
            View itemView = createTicketView(ticketResponse,selectedReturn, priceReturn, "Lượt về");
            layoutReturn.addView(itemView);
            layoutReturn.setVisibility(View.VISIBLE);
        }

        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ThankYouActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }

    private View createTicketView(TicketResponse ticketResponse,ChuyenXeResult trip, double price, String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_thankyou, null);
        TextView tvTime = view.findViewById(R.id.tvThoiGianXuatBen);
        TextView tvRoute = view.findViewById(R.id.tvTuyenXe);
        TextView tvSeats = view.findViewById(R.id.tvSoLuongGhe);
        TextView tvTotalPrice = view.findViewById(R.id.tvTongTien);

        tvTime.setText(DateTimeHelper.toFullDateTime(trip.getThoiDiemDi()));
        tvRoute.setText(trip.getTenBenXeDi()+ " - " +trip.getTenBenXeDen());
        tvSeats.setText(ticketResponse.getTickets() + " ghế");

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(formatter.format(price) + " VND");
        return view;
    }
    private void handleIntentData() {
        Intent intent = getIntent();
        user = (NguoiDung) intent.getSerializableExtra("User_data");
        selectedGo = (ChuyenXeResult) intent.getSerializableExtra("goTrip");
        selectedReturn = (ChuyenXeResult) intent.getSerializableExtra("returnTrip");
        ticketResponse = (TicketResponse) intent.getSerializableExtra("ticket");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            priceGo = bundle.getDouble("priceGo", 0);
            if (selectedReturn != null) {
                priceReturn = bundle.getDouble("priceReturn", 0);
            }
        }
    }
}

