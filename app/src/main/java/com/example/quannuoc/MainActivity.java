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
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    EditText editusername, editpassword;
    Button btndangnhap, btndangky;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Bước 2: Ánh xạ biến giao diện
        editusername = findViewById(R.id.editusername);
        editpassword = findViewById(R.id.editpassword);
        btndangnhap = findViewById(R.id.btndangnhap);
        btndangky = findViewById(R.id.btndangky);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);
        taoBang();
        themDuLieuMau();

        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangNhap();
            }
        });

        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(myIntent);
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

    private void dangNhap() {
        String username = editusername.getText().toString().trim();
        String password = editpassword.getText().toString().trim();
        String msg = "";

        if (username.isEmpty()) {
            editusername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (password.isEmpty()) {
            editpassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        Cursor c = mydatabase.query("tbluser", null, "username = ? AND password = ?",
                new String[]{username, password}, null, null, null);
        if (c.getCount() == 0) {
            c.close();
            msg = "Sai tên đăng nhập hoặc mật khẩu";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        c.moveToFirst();
        int userid = c.getInt(0);
        int role = c.getInt(5);
        c.close();

        Intent myIntent;
        if (role == 1) {
            myIntent = new Intent(MainActivity.this, AdminActivity.class);
        } else {
            myIntent = new Intent(MainActivity.this, MenuActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        bundle.putInt("role", role);
        myIntent.putExtra("goi", bundle);
        startActivity(myIntent);

        editpassword.setText("");
        msg = "Đăng nhập thành công";
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void taoBang() {
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbluser (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "fullname TEXT, " +
                "phone TEXT, " +
                "role INTEGER DEFAULT 0)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbldanhmuc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tendm TEXT NOT NULL)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblmon (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenmon TEXT NOT NULL, " +
                "mota TEXT, " +
                "gia INTEGER NOT NULL, " +
                "madm INTEGER, " +
                "hinhanh TEXT)");

        // Tự động bổ sung cột hinhanh nếu database đã tạo từ trước
        try {
            mydatabase.execSQL("ALTER TABLE tblmon ADD COLUMN hinhanh TEXT");
        } catch (Exception e) {
            // Cột hinhanh đã tồn tại
        }

        // Tự động cập nhật tên ảnh cho dữ liệu cũ nếu đang để rỗng
        try {
            mydatabase.execSQL("UPDATE tblmon SET hinhanh = 'mon' || id WHERE hinhanh IS NULL OR hinhanh = ''");
        } catch (Exception e) {
        }

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblgiohang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER NOT NULL, " +
                "mamon INTEGER NOT NULL, " +
                "soluong INTEGER NOT NULL)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbldonhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER NOT NULL, " +
                "ngaydat TEXT NOT NULL, " +
                "tongtien INTEGER NOT NULL, " +
                "trangthai INTEGER DEFAULT 0)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblchitiet (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "madon INTEGER NOT NULL, " +
                "mamon INTEGER NOT NULL, " +
                "soluong INTEGER NOT NULL, " +
                "gia INTEGER NOT NULL)");
    }

    private void themDuLieuMau() {
        Cursor c = mydatabase.query("tbluser", null, null, null, null, null, null);
        int sodong = c.getCount();
        c.close();
        if (sodong > 0) {
            return;
        }
        themUserMau();
        themDanhMucMau();
        themMonMau();
        themDonHangMau();
    }

    private void themUserMau() {
        themUser("admin", "123", "Quản Trị Viên", "0901234567", 1);
        themUser("khachhang", "123", "Nguyễn Văn An", "0912345678", 0);
    }

    private void themDanhMucMau() {
        themDanhMuc("Cà phê");
        themDanhMuc("Trà sữa");
        themDanhMuc("Trà trái cây");
        themDanhMuc("Đá xay");
        themDanhMuc("Nước ép & Sinh tố");
        themDanhMuc("Bánh ngọt & Ăn vặt");
    }

    private void themMonMau() {
        themMon("Cà phê đen đá", "Cà phê Robusta Đắk Lắk nguyên chất, đậm vị đắng thanh", 20000, 1, "mon1");
        themMon("Cà phê sữa đá", "Cà phê phin đậm đà hòa cùng sữa đặc ngọt béo", 25000, 1, "mon2");
        themMon("Bạc xỉu đá", "Nhiều sữa tươi, phảng phất vị cà phê thơm dịu", 28000, 1, "mon3");
        themMon("Cà phê muối", "Lớp kem muối béo mặn hòa cùng cà phê phin", 32000, 1, "mon4");
        themMon("Cappuccino", "Espresso hòa cùng lớp bọt sữa nóng dày mịn", 40000, 1, "mon5");
        themMon("Trà sữa trân châu đường đen", "Trà sữa béo ngậy cùng trân châu đường đen dẻo dai", 35000, 2, "mon6");
        themMon("Trà sữa Oolong nướng", "Hương trà ô long rang nướng thơm lừng", 38000, 2, "mon7");
        themMon("Trà sữa Thái xanh", "Trà xanh Thái Lan thanh mát, thơm mùi thảo mộc", 32000, 2, "mon8");
        themMon("Trà sữa khoai môn", "Vị bùi béo tự nhiên từ khoai môn tươi xay mịn", 36000, 2, "mon9");
        themMon("Trà đào cam sả", "Sả thơm, cam tươi chua dịu và miếng đào giòn ngọt", 35000, 3, "mon10");
        themMon("Trà vải hoa hồng", "Hương hoa hồng kết hợp trái vải ngâm mọng nước", 38000, 3, "mon11");
        themMon("Trà ổi hồng", "Vị chua ngọt giải nhiệt trong ngày hè", 38000, 3, "mon12");
        themMon("Matcha đá xay", "Matcha Nhật Bản xay mịn cùng sữa, phủ kem phô mai", 45000, 4, "mon13");
        themMon("Socola đá xay", "Socola đen xay nhuyễn, rắc hạnh nhân nướng", 45000, 4, "mon14");
        themMon("Cookie đá xay", "Bánh Oreo xay cùng sữa tươi và kem tươi", 42000, 4, "mon15");
        themMon("Nước cam vắt", "100% cam sành tươi, giàu vitamin C", 30000, 5, "mon16");
        themMon("Nước ép dưa hấu", "Dưa hấu đỏ tươi ngọt thanh tự nhiên", 28000, 5, "mon17");
        themMon("Sinh tố bơ", "Bơ sáp Đắk Lắk dẻo đặc hòa cùng sữa đặc", 42000, 5, "mon18");
        themMon("Bánh Tiramisu", "Phô mai Mascarpone mềm tan thơm hương cà phê", 35000, 6, "mon19");
        themMon("Bánh Croissant", "Bánh sừng bò nướng ngàn lớp thơm bơ", 25000, 6, "mon20");
    }

    private void themDonHangMau() {
        long madon1 = themDonHang(2, "01/11/2026 08:30", 60000, 2);
        themChiTiet(madon1, 2, 1, 25000);
        themChiTiet(madon1, 10, 1, 35000);

        long madon2 = themDonHang(2, "04/11/2026 10:15", 73000, 0);
        themChiTiet(madon2, 6, 1, 35000);
        themChiTiet(madon2, 7, 1, 38000);
    }

    private void themUser(String username, String password, String fullname, String phone, int role) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("username", username);
        myvalue.put("password", password);
        myvalue.put("fullname", fullname);
        myvalue.put("phone", phone);
        myvalue.put("role", role);
        mydatabase.insert("tbluser", null, myvalue);
    }

    private void themDanhMuc(String tendm) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("tendm", tendm);
        mydatabase.insert("tbldanhmuc", null, myvalue);
    }

    private void themMon(String tenmon, String mota, int gia, int madm, String hinhanh) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("tenmon", tenmon);
        myvalue.put("mota", mota);
        myvalue.put("gia", gia);
        myvalue.put("madm", madm);
        myvalue.put("hinhanh", hinhanh);
        mydatabase.insert("tblmon", null, myvalue);
    }

    private long themDonHang(int userid, String ngaydat, int tongtien, int trangthai) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("userid", userid);
        myvalue.put("ngaydat", ngaydat);
        myvalue.put("tongtien", tongtien);
        myvalue.put("trangthai", trangthai);
        return mydatabase.insert("tbldonhang", null, myvalue);
    }

    private void themChiTiet(long madon, int mamon, int soluong, int gia) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("madon", madon);
        myvalue.put("mamon", mamon);
        myvalue.put("soluong", soluong);
        myvalue.put("gia", gia);
        mydatabase.insert("tblchitiet", null, myvalue);
    }
}
