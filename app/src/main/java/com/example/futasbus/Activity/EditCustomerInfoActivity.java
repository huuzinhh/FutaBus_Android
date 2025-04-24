package com.example.futasbus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.futasbus.R;

public class EditCustomerInfoActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtCCCD;
    private TextView NameError, EmailError, PhoneError, CCCDError;
    private Button btnConfirm;
    private ImageButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_info);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtCCCD = findViewById(R.id.edtIdCard);
        btnConfirm = findViewById(R.id.btnSave);

        NameError = findViewById(R.id.NameError);
        EmailError = findViewById(R.id.emailError);
        PhoneError = findViewById(R.id.SDTError);
        CCCDError = findViewById(R.id.CCCDError);
        returnButton = findViewById(R.id.btnBack);
        returnButton.setOnClickListener(v -> finish());
        // Nhận dữ liệu ban đầu từ ConfirmBookingActivity
        Intent intent = getIntent();
        edtName.setText(intent.getStringExtra("name"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtPhone.setText(intent.getStringExtra("phone"));
        edtCCCD.setText(intent.getStringExtra("CCCD"));

        btnConfirm.setOnClickListener(view -> {
            boolean isValid = true;

            // Kiểm tra Tên
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                NameError.setText("Tên không được để trống !");
                NameError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra Email
            String email = edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                EmailError.setText("Email không được để trống !");
                EmailError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EmailError.setText("Email không hợp lệ !");
                EmailError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra Số điện thoại
            String phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                PhoneError.setText("Số điện thoại không được để trống !");
                PhoneError.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (!phone.matches("^0[0-9]{9}$")) {
                PhoneError.setText("Số điện thoại không hợp lệ !");
                PhoneError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            // Kiểm tra CCCD
            String cccd = edtCCCD.getText().toString().trim();
            if (cccd.isEmpty()) {
                CCCDError.setText("CCCD không được để trống !");
                CCCDError.setVisibility(View.VISIBLE);
                isValid = false;
            }

            if (!isValid) return;

            // Nếu hợp lệ, gửi kết quả về ConfirmBookingActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("email", email);
            resultIntent.putExtra("phone", phone);
            resultIntent.putExtra("CCCD", cccd);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
