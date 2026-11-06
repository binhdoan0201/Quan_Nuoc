package com.example.quannuoc;

import android.content.ContentValues;
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
    //Bước 1: Khai báo biến giao diện
    EditText editusername, editpassword, editnhaplaipassword, editfullname, editphone;
    Button btndangky, btnquaylai;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dangky);

        //Bước 2: Ánh xạ biến giao diện
        editusername = findViewById(R.id.editusername);
        editpassword = findViewById(R.id.editpassword);
        editnhaplaipassword = findViewById(R.id.editnhaplaipassword);
        editfullname = findViewById(R.id.editfullname);
        editphone = findViewById(R.id.editphone);
        btndangky = findViewById(R.id.btndangky);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
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
        String username = editusername.getText().toString().trim();
        String password = editpassword.getText().toString().trim();
        String nhaplai = editnhaplaipassword.getText().toString().trim();
        String fullname = editfullname.getText().toString().trim();
        String phone = editphone.getText().toString().trim();
        String msg = "";

        if (!kiemTraNhap(username, password, nhaplai, fullname, phone)) {
            return;
        }

        Cursor c = mydatabase.query("tbluser", null, "username = ?",
                new String[]{username}, null, null, null);
        int sodong = c.getCount();
        c.close();
        if (sodong > 0) {
            editusername.setError("Tên đăng nhập đã tồn tại");
            return;
        }

        ContentValues myvalue = new ContentValues();
        myvalue.put("username", username);
        myvalue.put("password", password);
        myvalue.put("fullname", fullname);
        myvalue.put("phone", phone);
        myvalue.put("role", 0);
        if (mydatabase.insert("tbluser", null, myvalue) == -1) {
            msg = "Đăng ký thất bại";
            Toast.makeText(DangKyActivity.this, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        msg = "Đăng ký thành công, mời bạn đăng nhập";
        Toast.makeText(DangKyActivity.this, msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean kiemTraNhap(String username, String password, String nhaplai, String fullname, String phone) {
        if (username.isEmpty()) {
            editusername.setError("Vui lòng nhập tên đăng nhập");
            return false;
        }
        if (username.contains(" ")) {
            editusername.setError("Tên đăng nhập không được chứa khoảng trắng");
            return false;
        }
        if (password.isEmpty()) {
            editpassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if (password.length() < 3) {
            editpassword.setError("Mật khẩu phải có ít nhất 3 ký tự");
            return false;
        }
        if (!password.equals(nhaplai)) {
            editnhaplaipassword.setError("Mật khẩu nhập lại không khớp");
            return false;
        }
        if (fullname.isEmpty()) {
            editfullname.setError("Vui lòng nhập họ tên");
            return false;
        }
        if (!phone.matches("0[0-9]{9}")) {
            editphone.setError("Số điện thoại gồm 10 chữ số, bắt đầu bằng 0");
            return false;
        }
        return true;
    }
}
