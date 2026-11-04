package com.example.quannuoc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKyActivity extends AppCompatActivity {
    EditText edtusername, edtpassword, edtnhaplaipassword, edtfullname, edtphone;
    Button btndangky, btnquaylai;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dangky);

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        edtnhaplaipassword = findViewById(R.id.edtnhaplaipassword);
        edtfullname = findViewById(R.id.edtfullname);
        edtphone = findViewById(R.id.edtphone);
        btndangky = findViewById(R.id.btndangky);
        btnquaylai = findViewById(R.id.btnquaylai);

        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });

        btnquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void dangKy() {
        String username = edtusername.getText().toString().trim();
        String password = edtpassword.getText().toString().trim();
        String nhaplai = edtnhaplaipassword.getText().toString().trim();
        String fullname = edtfullname.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || nhaplai.isEmpty() || fullname.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.contains(" ")) {
            Toast.makeText(this, "Tên đăng nhập không được chứa khoảng trắng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 3) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 3 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(nhaplai)) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.matches("0\\d{9}")) {
            Toast.makeText(this, "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = mydatabase.rawQuery("SELECT id FROM tbluser WHERE username = ?", new String[]{username});
        boolean daTonTai = c.moveToFirst();
        c.close();
        if (daTonTai) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        mydatabase.execSQL("INSERT INTO tbluser (username, password, fullname, phone, role) VALUES (?, ?, ?, ?, 0)",
                new Object[]{username, password, fullname, phone});
        Toast.makeText(this, "Đăng ký thành công, mời bạn đăng nhập", Toast.LENGTH_SHORT).show();
        finish();
    }
}
