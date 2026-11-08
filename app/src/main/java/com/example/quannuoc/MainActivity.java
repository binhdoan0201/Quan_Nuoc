package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Bước 1: Khai báo biến giao diện
    EditText edtuser, edtpass;
    Button btndangnhap, btndangky, btngiohang, btnlichsu;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Tắt Dark Mode tự động để giữ giao diện sáng rõ ràng
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Bước 2: Ánh xạ biến giao diện
        edtuser = findViewById(R.id.edtuser);
        edtpass = findViewById(R.id.edtpass);
        btndangnhap = findViewById(R.id.btndangnhap);
        btndangky = findViewById(R.id.btndangky);
        btngiohang = findViewById(R.id.btngiohang);
        btnlichsu = findViewById(R.id.btnlichsu);

        // Bước 3: Khởi tạo CSDL & tạo bảng nếu chưa có
        initDatabase();

        // Xử lý nút Đăng nhập
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = edtuser.getText().toString().trim();
                String p = edtpass.getText().toString().trim();

                if (u.isEmpty() || p.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor c = mydatabase.rawQuery("SELECT * FROM tbluser WHERE user = ? AND pass = ?", new String[]{u, p});
                if (c != null && c.moveToFirst()) {
                    int userid = c.getInt(0);
                    c.close();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("userid", userid);
                    intent.putExtra("goi", b);
                    startActivity(intent);
                } else {
                    if (c != null) c.close();
                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút Đăng ký
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        // Nút mở Giỏ hàng nhanh (dành cho trải nghiệm & kiểm thử)
        btngiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GioHangActivity.class);
                Bundle b = new Bundle();
                b.putInt("userid", 1);
                intent.putExtra("goi", b);
                startActivity(intent);
            }
        });

        // Nút mở Lịch sử nhanh (dành cho trải nghiệm & kiểm thử)
        btnlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LichSuActivity.class);
                Bundle b = new Bundle();
                b.putInt("userid", 1);
                intent.putExtra("goi", b);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initDatabase() {
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // 1. Bảng User
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbluser (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user TEXT, " +
                "pass TEXT, " +
                "phanquyen INTEGER)");

        // 2. Bảng Món
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblmon (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenmon TEXT, " +
                "gia INTEGER, " +
                "madanhmuc INTEGER, " +
                "hinh TEXT)");

        // 3. Bảng Giỏ hàng
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblgiohang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER, " +
                "mamon INTEGER, " +
                "soluong INTEGER)");

        // 4. Bảng Đơn hàng
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbldonhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER, " +
                "ngaydat TEXT, " +
                "tongtien INTEGER, " +
                "trangthai INTEGER)");

        // 5. Bảng Chi tiết đơn hàng
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblchitiet (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "madon INTEGER, " +
                "mamon INTEGER, " +
                "soluong INTEGER, " +
                "gia INTEGER)");

        // Thêm user mẫu nếu chưa có
        Cursor cu = mydatabase.rawQuery("SELECT count(*) FROM tbluser", null);
        if (cu != null && cu.moveToFirst() && cu.getInt(0) == 0) {
            mydatabase.execSQL("INSERT INTO tbluser(user, pass, phanquyen) VALUES ('admin', '123', 1)");
            mydatabase.execSQL("INSERT INTO tbluser(user, pass, phanquyen) VALUES ('khach', '123', 0)");
        }
        if (cu != null) cu.close();

        // Thêm món mẫu nếu chưa có
        Cursor cm = mydatabase.rawQuery("SELECT count(*) FROM tblmon", null);
        if (cm != null && cm.moveToFirst() && cm.getInt(0) == 0) {
            mydatabase.execSQL("INSERT INTO tblmon(tenmon, gia, madanhmuc, hinh) VALUES ('Cà phê đen', 20000, 1, '')");
            mydatabase.execSQL("INSERT INTO tblmon(tenmon, gia, madanhmuc, hinh) VALUES ('Cà phê sữa', 25000, 1, '')");
            mydatabase.execSQL("INSERT INTO tblmon(tenmon, gia, madanhmuc, hinh) VALUES ('Trà đào cam sả', 35000, 2, '')");
            mydatabase.execSQL("INSERT INTO tblmon(tenmon, gia, madanhmuc, hinh) VALUES ('Sinh tố bơ', 40000, 3, '')");
        }
        if (cm != null) cm.close();

        // Thêm món mẫu vào giỏ hàng nếu chưa có
        Cursor cg = mydatabase.rawQuery("SELECT count(*) FROM tblgiohang WHERE userid = 1", null);
        if (cg != null && cg.moveToFirst() && cg.getInt(0) == 0) {
            ContentValues v1 = new ContentValues();
            v1.put("userid", 1);
            v1.put("mamon", 2); // Cà phê sữa
            v1.put("soluong", 2);
            mydatabase.insert("tblgiohang", null, v1);

            ContentValues v2 = new ContentValues();
            v2.put("userid", 1);
            v2.put("mamon", 3); // Trà đào
            v2.put("soluong", 1);
            mydatabase.insert("tblgiohang", null, v2);
        }
        if (cg != null) cg.close();
    }
}