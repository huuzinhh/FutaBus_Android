package com.example.futasbus.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.futasbus.R;
import com.example.futasbus.model.NguoiDung;

public class CustomerActivity extends AppCompatActivity {

    private TextView tvHoTen, tvGioiTinh, tvNgaySinh, tvCCCD, tvDiaChi, tvPhone, tvEmail;
    private ImageView returnButton;
    private NguoiDung user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_info);

        initViews();

        // Lấy user từ Intent
        user = (NguoiDung) getIntent().getSerializableExtra("user_data");
        if (user != null) {
            CustomerInfoFragment fragment = new CustomerInfoFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("user_data", user);
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.CustomerActivity, fragment)
                    .commit();
        }
        returnButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        tvHoTen = findViewById(R.id.tvHoTen);
        tvGioiTinh = findViewById(R.id.tvGioiTinh);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        tvCCCD = findViewById(R.id.tvCCCD);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvPhone = findViewById(R.id.tvSoDienThoai);
        tvEmail = findViewById(R.id.tvEmail);
        returnButton = findViewById(R.id.btnBack);
    }

    private void displayUserInfo() {
        tvHoTen.setText(user.getHoTen());
        tvGioiTinh.setText(user.isGioiTinh() ? "Nữ" : "Nam");
        tvNgaySinh.setText(String.valueOf(user.getNamSinh()));
        tvCCCD.setText(user.getCCCD());
        tvDiaChi.setText(user.getDiaChi());
        tvPhone.setText(user.getSoDienThoai());
        tvEmail.setText(user.getEmail());
    }
}
