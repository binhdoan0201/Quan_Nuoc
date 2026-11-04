package com.example.quannuoc;

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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText editusername, editpassword;
    Button btndangnhap, btndangky;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editusername = findViewById(R.id.editusername);
        editpassword = findViewById(R.id.editpassword);
        btndangnhap = findViewById(R.id.btndangnhap);
        btndangky = findViewById(R.id.btndangky);

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
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void dangNhap() {
        String username = editusername.getText().toString().trim();
        String password = editpassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = mydatabase.rawQuery("SELECT id, role FROM tbluser WHERE username = ? AND password = ?",
                new String[]{username, password});
        if (!c.moveToFirst()) {
            c.close();
            Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        int userid = c.getInt(0);
        int role = c.getInt(1);
        c.close();

        Intent intent;
        if (role == 1) {
            intent = new Intent(MainActivity.this, AdminActivity.class);
        } else {
            intent = new Intent(MainActivity.this, MenuActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        bundle.putInt("role", role);
        intent.putExtra("goi", bundle);
        startActivity(intent);

        editpassword.setText("");
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
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
                "madm INTEGER)");

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
        Cursor c = mydatabase.rawQuery("SELECT COUNT(*) FROM tbluser", null);
        c.moveToFirst();
        int soDong = c.getInt(0);
        c.close();
        if (soDong > 0) return;

        mydatabase.execSQL("INSERT INTO tbluser (username, password, fullname, phone, role) VALUES " +
                "('admin', '123', 'Quản Trị Viên', '0901234567', 1), " +
                "('khachhang', '123', 'Nguyễn Văn An', '0912345678', 0)");

        mydatabase.execSQL("INSERT INTO tbldanhmuc (id, tendm) VALUES " +
                "(1, 'Cà phê'), " +
                "(2, 'Trà sữa'), " +
                "(3, 'Trà trái cây'), " +
                "(4, 'Đá xay'), " +
                "(5, 'Nước ép & Sinh tố'), " +
                "(6, 'Bánh ngọt & Ăn vặt')");

        mydatabase.execSQL("INSERT INTO tblmon (tenmon, mota, gia, madm) VALUES " +
                "('Cà phê đen đá', 'Cà phê Robusta Đắk Lắk nguyên chất, đậm vị đắng thanh', 20000, 1), " +
                "('Cà phê sữa đá', 'Cà phê phin đậm đà hòa cùng sữa đặc ngọt béo', 25000, 1), " +
                "('Bạc xỉu đá', 'Nhiều sữa tươi, phảng phất vị cà phê thơm dịu', 28000, 1), " +
                "('Cà phê muối', 'Lớp kem muối béo mặn hòa cùng cà phê phin', 32000, 1), " +
                "('Cappuccino', 'Espresso hòa cùng lớp bọt sữa nóng dày mịn', 40000, 1), " +
                "('Trà sữa trân châu đường đen', 'Trà sữa béo ngậy cùng trân châu đường đen dẻo dai', 35000, 2), " +
                "('Trà sữa Oolong nướng', 'Hương trà ô long rang nướng thơm lừng', 38000, 2), " +
                "('Trà sữa Thái xanh', 'Trà xanh Thái Lan thanh mát, thơm mùi thảo mộc', 32000, 2), " +
                "('Trà sữa khoai môn', 'Vị bùi béo tự nhiên từ khoai môn tươi xay mịn', 36000, 2), " +
                "('Trà đào cam sả', 'Sả thơm, cam tươi chua dịu và miếng đào giòn ngọt', 35000, 3), " +
                "('Trà vải hoa hồng', 'Hương hoa hồng kết hợp trái vải ngâm mọng nước', 38000, 3), " +
                "('Trà ổi hồng', 'Vị chua ngọt giải nhiệt trong ngày hè', 38000, 3), " +
                "('Matcha đá xay', 'Matcha Nhật Bản xay mịn cùng sữa, phủ kem phô mai', 45000, 4), " +
                "('Socola đá xay', 'Socola đen xay nhuyễn, rắc hạnh nhân nướng', 45000, 4), " +
                "('Cookie đá xay', 'Bánh Oreo xay cùng sữa tươi và kem tươi', 42000, 4), " +
                "('Nước cam vắt', '100% cam sành tươi, giàu vitamin C', 30000, 5), " +
                "('Nước ép dưa hấu', 'Dưa hấu đỏ tươi ngọt thanh tự nhiên', 28000, 5), " +
                "('Sinh tố bơ', 'Bơ sáp Đắk Lắk dẻo đặc hòa cùng sữa đặc', 42000, 5), " +
                "('Bánh Tiramisu', 'Phô mai Mascarpone mềm tan thơm hương cà phê', 35000, 6), " +
                "('Bánh Croissant', 'Bánh sừng bò nướng ngàn lớp thơm bơ', 25000, 6)");

        mydatabase.execSQL("INSERT INTO tbldonhang (id, userid, ngaydat, tongtien, trangthai) VALUES " +
                "(1, 2, '01/11/2026 08:30', 60000, 2), " +
                "(2, 2, '04/11/2026 10:15', 73000, 0)");

        mydatabase.execSQL("INSERT INTO tblchitiet (madon, mamon, soluong, gia) VALUES " +
                "(1, 2, 1, 25000), " +
                "(1, 10, 1, 35000), " +
                "(2, 6, 1, 35000), " +
                "(2, 7, 1, 38000)");
    }
}
