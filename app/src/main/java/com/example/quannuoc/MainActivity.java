package com.example.quannuoc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Bước 2: Ánh xạ biến giao diện

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);
        taoBang();
        themDuLieuMau();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Tạo 6 bảng theo docs/csdl.md (chỉ tạo khi chưa có)
    private void taoBang() {
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbluser ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "fullname TEXT, "
                + "phone TEXT, "
                + "role INTEGER DEFAULT 0)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbldanhmuc ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tendm TEXT NOT NULL)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblmon ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "tenmon TEXT NOT NULL, "
                + "mota TEXT, "
                + "gia INTEGER NOT NULL, "
                + "madm INTEGER)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblgiohang ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userid INTEGER NOT NULL, "
                + "mamon INTEGER NOT NULL, "
                + "soluong INTEGER NOT NULL)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tbldonhang ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userid INTEGER NOT NULL, "
                + "ngaydat TEXT NOT NULL, "
                + "tongtien INTEGER NOT NULL, "
                + "trangthai INTEGER DEFAULT 0)");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tblchitiet ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "madon INTEGER NOT NULL, "
                + "mamon INTEGER NOT NULL, "
                + "soluong INTEGER NOT NULL, "
                + "gia INTEGER NOT NULL)");
    }

    // Thêm dữ liệu mẫu, chỉ thêm khi bảng tbluser còn rỗng
    private void themDuLieuMau() {
        Cursor c = mydatabase.rawQuery("SELECT COUNT(*) FROM tbluser", null);
        c.moveToFirst();
        int soDong = c.getInt(0);
        c.close();
        if (soDong > 0) {
            return;
        }

        // Tài khoản: admin (role = 1) và 2 người dùng (role = 0)
        mydatabase.execSQL("INSERT INTO tbluser (username, password, fullname, phone, role) VALUES "
                + "('admin', '123', 'Quản trị viên', '0900000000', 1), "
                + "('user1', '123', 'Nguyễn Văn An', '0911111111', 0), "
                + "('user2', '123', 'Trần Thị Bình', '0922222222', 0)");

        // 4 danh mục
        mydatabase.execSQL("INSERT INTO tbldanhmuc (tendm) VALUES "
                + "('Trà sữa'), "
                + "('Cà phê'), "
                + "('Nước ép - Sinh tố'), "
                + "('Ăn vặt')");

        // 16 món
        mydatabase.execSQL("INSERT INTO tblmon (tenmon, mota, gia, madm) VALUES "
                + "('Trà sữa truyền thống', 'Trà đen pha sữa, trân châu trắng', 25000, 1), "
                + "('Trà sữa trân châu đường đen', 'Trân châu nấu đường đen', 30000, 1), "
                + "('Trà sữa matcha', 'Bột matcha Nhật', 30000, 1), "
                + "('Trà đào cam sả', 'Đào miếng, cam, sả tươi', 28000, 1), "
                + "('Cà phê đen đá', 'Cà phê phin truyền thống', 15000, 2), "
                + "('Cà phê sữa đá', 'Cà phê phin pha sữa đặc', 18000, 2), "
                + "('Bạc xỉu', 'Nhiều sữa, ít cà phê', 20000, 2), "
                + "('Cà phê muối', 'Kem muối béo mặn', 25000, 2), "
                + "('Nước cam ép', 'Cam tươi vắt', 25000, 3), "
                + "('Nước ép dưa hấu', 'Dưa hấu ép nguyên chất', 22000, 3), "
                + "('Sinh tố bơ', 'Bơ sáp xay cùng sữa', 30000, 3), "
                + "('Sinh tố xoài', 'Xoài cát xay', 28000, 3), "
                + "('Bánh tráng trộn', 'Bánh tráng, khô bò, trứng cút', 20000, 4), "
                + "('Khoai tây chiên', 'Khoai tây chiên giòn', 25000, 4), "
                + "('Xúc xích nướng', '2 cây xúc xích nướng', 15000, 4), "
                + "('Hướng dương', 'Hạt hướng dương rang', 10000, 4)");

        // Giỏ hàng mẫu của user1 (id = 2)
        mydatabase.execSQL("INSERT INTO tblgiohang (userid, mamon, soluong) VALUES "
                + "(2, 13, 1)");

        // Đơn hàng mẫu của user1: 1 đơn hoàn thành, 1 đơn đang chờ
        mydatabase.execSQL("INSERT INTO tbldonhang (userid, ngaydat, tongtien, trangthai) VALUES "
                + "(2, '01/11/2026 10:15', 66000, 2), "
                + "(2, '03/11/2026 09:30', 25000, 0)");

        // Chi tiết 2 đơn hàng trên
        mydatabase.execSQL("INSERT INTO tblchitiet (madon, mamon, soluong, gia) VALUES "
                + "(1, 2, 1, 30000), "
                + "(1, 6, 2, 18000), "
                + "(2, 9, 1, 25000)");
    }
}
