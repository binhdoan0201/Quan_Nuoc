package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKyActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader, llform;
    EditText editusername, editpassword, editnhaplaipassword, editfullname, editphone;
    Button btndangky, btnquaylai;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dangky);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        llform = findViewById(R.id.llform);
        editusername = findViewById(R.id.editusername);
        editpassword = findViewById(R.id.editpassword);
        editnhaplaipassword = findViewById(R.id.editnhaplaipassword);
        editfullname = findViewById(R.id.editfullname);
        editphone = findViewById(R.id.editphone);
        btndangky = findViewById(R.id.btndangky);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);
        llform.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));
        editusername.setBackground(taoNen(Color.parseColor("#FFF8F3"), 12, "#E3D8CC"));
        editpassword.setBackground(taoNen(Color.parseColor("#FFF8F3"), 12, "#E3D8CC"));
        editnhaplaipassword.setBackground(taoNen(Color.parseColor("#FFF8F3"), 12, "#E3D8CC"));
        editfullname.setBackground(taoNen(Color.parseColor("#FFF8F3"), 12, "#E3D8CC"));
        editphone.setBackground(taoNen(Color.parseColor("#FFF8F3"), 12, "#E3D8CC"));

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });
    }

    // Đổi dp sang pixel
    private int dp(int giatri) {
        return (int) (giatri * getResources().getDisplayMetrics().density);
    }

    // Tạo nền bo góc, vien là mã màu viền (null nếu không có viền)
    private GradientDrawable taoNen(int mau, int bogoc, String vien) {
        GradientDrawable nen = new GradientDrawable();
        nen.setColor(mau);
        nen.setCornerRadius(dp(bogoc));
        if (vien != null) {
            nen.setStroke(dp(1), Color.parseColor(vien));
        }
        return nen;
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

        // Gửi username về lại MainActivity để tự điền vào form đăng nhập
        Intent resultIntent = new Intent();
        resultIntent.putExtra("registered_username", username);
        setResult(RESULT_OK, resultIntent);
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