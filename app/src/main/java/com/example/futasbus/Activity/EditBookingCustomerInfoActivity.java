package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.futasbus.R;

public class EditBookingCustomerInfoActivity extends AppCompatActivity {

    private EditText edtName, edtPhone;
    private TextView NameError, edtEmail,Departure,Destination;
    private Button btnConfirm;
    private ImageButton returnButton;
    private ImageView tripRoundArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking_customer_info);
        Departure = findViewById(R.id.txtDeparture);
        Destination = findViewById(R.id.txtDestination);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnConfirm = findViewById(R.id.btnSave);

        NameError = findViewById(R.id.NameError);
        returnButton = findViewById(R.id.btnBack);
        tripRoundArrow = findViewById(R.id.iv_round_trip);

        returnButton.setOnClickListener(v -> finish());
        // Nhận dữ liệu ban đầu từ ConfirmBookingActivity
        Intent intent = getIntent();
        edtName.setText(intent.getStringExtra("name"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtPhone.setText(intent.getStringExtra("phone"));
        Departure.setText(intent.getStringExtra("departure"));
        Destination.setText(intent.getStringExtra("destination"));
        if(getIntent().getBooleanExtra("istripround", false)){
            tripRoundArrow.setImageResource(R.drawable.ic_round_trip);
        }
        btnConfirm.setOnClickListener(view -> {
            boolean isValid = true;

            // Kiểm tra Tên
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                NameError.setText("Tên không được để trống !");
                NameError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (!name.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
                NameError.setText("Tên chỉ được chứa chữ cái và khoảng trắng!");
                NameError.setVisibility(View.VISIBLE);
                isValid = false;
            }
            // Kiểm tra Số điện thoại
            String phone = edtPhone.getText().toString().trim();
            if (!isValid) return;

            // Nếu hợp lệ, gửi kết quả về ConfirmBookingActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("phone", phone);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
