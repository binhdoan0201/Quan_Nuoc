package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChiTietMonActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    ImageView imgmon;
    TextView txttenmon;
    TextView txtdanhmuc;
    TextView txtgia;
    TextView txtmota;
    EditText editsoluong;
    Button btngiam;
    Button btntang;
    Button btnthemgio;
    Button btngiohang;
    Button btnquaylai;
    SQLiteDatabase mydatabase;
    int userid;
    int mamon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitietmon);

        //Bước 2: Ánh xạ biến giao diện
        imgmon = findViewById(R.id.imgmon);
        txttenmon = findViewById(R.id.txttenmon);
        txtdanhmuc = findViewById(R.id.txtdanhmuc);
        txtgia = findViewById(R.id.txtgia);
        txtmota = findViewById(R.id.txtmota);
        editsoluong = findViewById(R.id.editsoluong);
        btngiam = findViewById(R.id.btngiam);
        btntang = findViewById(R.id.btntang);
        btnthemgio = findViewById(R.id.btnthemgio);
        btngiohang = findViewById(R.id.btngiohang);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Nhận dữ liệu từ màn hình trước qua Bundle goi
        Bundle goi = getIntent().getBundleExtra("goi");
        if (goi != null) {
            userid = goi.getInt("userid", -1);
            mamon = goi.getInt("mamon", -1);
        }

        // Đọc thông tin món kèm tên danh mục
        Cursor c = mydatabase.rawQuery("SELECT m.tenmon, m.mota, m.gia, d.tendm FROM tblmon m LEFT JOIN tbldanhmuc d ON m.madm = d.id WHERE m.id = ?", new String[]{String.valueOf(mamon)});
        if (c.moveToFirst()) {
            txttenmon.setText(c.getString(0));
            txtmota.setText(c.getString(1));
            txtgia.setText("Giá: " + dinhDangTien(c.getInt(2)));
            txtdanhmuc.setText("Danh mục: " + c.getString(3));
        } else {
            Toast.makeText(this, "Không tìm thấy món", Toast.LENGTH_SHORT).show();
            finish();
        }
        c.close();

        // Hiện ảnh món
        hienAnh();

        // Giảm số lượng (tối thiểu 1)
        btngiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = laySoLuong();
                if (soluong > 1) {
                    editsoluong.setText(String.valueOf(soluong - 1));
                }
            }
        });

        // Tăng số lượng
        btntang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsoluong.setText(String.valueOf(laySoLuong() + 1));
            }
        });

        // Thêm vào giỏ hàng
        btnthemgio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themVaoGio();
            }
        });

        // Mở giỏ hàng
        btngiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietMonActivity.this, GioHangActivity.class);
                Bundle goi = new Bundle();
                goi.putInt("userid", userid);
                intent.putExtra("goi", goi);
                startActivity(intent);
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

    // Hiện ảnh từ drawable tên mon + mã món (mon1, mon2...), không có thì dùng ảnh mặc định
    private void hienAnh() {
        int maanh = getResources().getIdentifier("mon" + mamon, "drawable", getPackageName());
        if (maanh != 0) {
            imgmon.setImageResource(maanh);
        } else {
            imgmon.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    // Lấy số lượng đang nhập, nếu rỗng hoặc sai thì coi là 1
    private int laySoLuong() {
        try {
            int soluong = Integer.parseInt(editsoluong.getText().toString().trim());
            if (soluong < 1) {
                return 1;
            }
            return soluong;
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    // Kiểm tra số lượng rồi thêm vào giỏ, nếu món đã có thì cộng dồn
    private void themVaoGio() {
        String chuoi = editsoluong.getText().toString().trim();
        if (chuoi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            return;
        }
        int soluong;
        try {
            soluong = Integer.parseInt(chuoi);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (soluong <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor c = mydatabase.rawQuery("SELECT id, soluong FROM tblgiohang WHERE userid = ? AND mamon = ?", new String[]{String.valueOf(userid), String.valueOf(mamon)});
        ContentValues giatri = new ContentValues();
        if (c.moveToFirst()) {
            giatri.put("soluong", c.getInt(1) + soluong);
            mydatabase.update("tblgiohang", giatri, "id = ?", new String[]{String.valueOf(c.getInt(0))});
        } else {
            giatri.put("userid", userid);
            giatri.put("mamon", mamon);
            giatri.put("soluong", soluong);
            mydatabase.insert("tblgiohang", null, giatri);
        }
        c.close();
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    // Định dạng tiền: 25000 -> 25.000 đ
    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}