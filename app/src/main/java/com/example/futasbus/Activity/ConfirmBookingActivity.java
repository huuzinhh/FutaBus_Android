package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.futasbus.Adapter.BookBusPagerAdapter;
import com.example.futasbus.R;
import com.example.futasbus.respone.TicketResponse;
import com.example.futasbus.helper.DateTimeHelper;
import com.example.futasbus.model.ChuyenXeResult;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfirmBookingActivity extends AppCompatActivity {

    private TextView txtDeparture, txtDestination, tvgiavedi, tvgiaveve, tvtongtien;
    private TextView tvHoTen, tvPhone, tvEmail;
    private ImageButton returnButton, editseatbutton, btnEdit;
    private double priceGo = 0, priceReturn = 0;

    private TicketResponse ticketResponse;
    private ChuyenXeResult selectedGo, selectedReturn;
    private String startTimeGo, endTimeGo, startTimeReturn, endTimeReturn;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BookBusPagerAdapter adapter;
    private LinearLayout luotve;

    private List<String> selectedSeatsGo = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_booking);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vpBookTrip);
        txtDeparture = findViewById(R.id.txtDeparture);
        txtDestination = findViewById(R.id.txtDestination);
        tvgiavedi = findViewById(R.id.tvGiaVeLuotDi);
        tvgiaveve = findViewById(R.id.tvGiaVeLuotVe);
        tvtongtien = findViewById(R.id.tvTongTien);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        luotve = findViewById(R.id.luotve);
        luotve.setVisibility(View.GONE);
        btnEdit = findViewById(R.id.btnEdit);
        editseatbutton = findViewById(R.id.btnEditSeats);
        returnButton = findViewById(R.id.btnBack);


        returnButton.setOnClickListener(v -> finish());
        editseatbutton.setOnClickListener(v -> finish());

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmBookingActivity.this, EditCustomerInfoActivity.class);
            intent.putExtra("name", tvHoTen.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("phone", tvPhone.getText().toString());
            intent.putExtra("CCCD", "");
            startActivityForResult(intent, 1);
        });

        adapter = new BookBusPagerAdapter(this);
        viewPager.setAdapter(adapter);


        Intent intent = getIntent();
        selectedGo = (ChuyenXeResult) intent.getSerializableExtra("goTrip");
        selectedReturn = (ChuyenXeResult) intent.getSerializableExtra("returnTrip");
        ticketResponse = (TicketResponse) intent.getSerializableExtra("ticket");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            startTimeGo = bundle.getString("Starttimego");
            endTimeGo = bundle.getString("Endtimego");
            selectedSeatsGo = bundle.getStringArrayList("selectedSeatsGo");
            priceGo = bundle.getDouble("priceGo", 0);

            if (selectedReturn != null) {
                startTimeReturn = bundle.getString("Starttimereturn");
                endTimeReturn = bundle.getString("Endtimereturn");
                selectedSeatsReturn = bundle.getStringArrayList("selectedSeatsReturn");
                priceReturn = bundle.getDouble("priceReturn", 0);
            }
        }


        txtDeparture.setText(ticketResponse.getDeparture());
        txtDestination.setText(ticketResponse.getDestination());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvgiavedi.setText("Số tiền: " + formatter.format(priceGo) + " VND");
        tvtongtien.setText("Số tiền: " + formatter.format(priceGo + priceReturn) + " VND");


        ConfirmBookingFragment goFragment = ConfirmBookingFragment.newInstance(selectedGo, startTimeGo, endTimeGo);
        adapter.addFragment(goFragment, "Ngày Đi");


        if (selectedReturn != null) {
            tvgiaveve.setText("Số tiền: " + formatter.format(priceReturn) + " VND");
            luotve.setVisibility(View.VISIBLE);
            ConfirmBookingFragment returnFragment = ConfirmBookingFragment.newInstance(selectedReturn, startTimeReturn, endTimeReturn);
            adapter.addFragment(returnFragment, "Ngày Về");
        }

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Ngày Đi\n" + DateTimeHelper.toFullDateTime(selectedGo.getThoiDiemDi()));
            } else if (position == 1 && selectedReturn != null) {
                tab.setText("Ngày Về\n" + DateTimeHelper.toFullDateTime(selectedReturn.getThoiDiemDi()));
            }
        }).attach();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            handleCustomerInfoResult(data);
        }
    }
    private void handleCustomerInfoResult(Intent data) {
        String name = data.getStringExtra("name");
        String phone = data.getStringExtra("phone");
        String email = data.getStringExtra("email");

        tvHoTen.setText(name);
        tvPhone.setText(phone);
        tvEmail.setText(email);
    }
}
