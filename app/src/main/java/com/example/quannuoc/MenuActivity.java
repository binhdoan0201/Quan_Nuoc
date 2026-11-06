package com.example.quannuoc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    TextView txtxinchao;
    Spinner spdanhmuc;
    EditText edittimkiem;
    Button btntim;
    ListView lvmon;
    Button btngiohang;
    Button btnlichsu;
    Button btnquaylai;
    SQLiteDatabase mydatabase;
    int userid;
    ArrayList<Integer> dsmadm;
    ArrayList<Integer> dsmamon;
    ArrayList<String> dsmon;
    ArrayAdapter<String> adaptermon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //Bước 2: Ánh xạ biến giao diện
        txtxinchao = findViewById(R.id.txtxinchao);
        spdanhmuc = findViewById(R.id.spdanhmuc);
        edittimkiem = findViewById(R.id.edittimkiem);
        btntim = findViewById(R.id.btntim);
        lvmon = findViewById(R.id.lvmon);
        btngiohang = findViewById(R.id.btngiohang);
        btnlichsu = findViewById(R.id.btnlichsu);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Nhận dữ liệu từ màn hình trước qua Bundle goi
        Bundle goi = getIntent().getBundleExtra("goi");
        if (goi != null) {
            userid = goi.getInt("userid", -1);
        }

        // Hiện lời chào theo tên người dùng
        Cursor cuser = mydatabase.rawQuery("SELECT fullname FROM tbluser WHERE id = ?", new String[]{String.valueOf(userid)});
        if (cuser.moveToFirst()) {
            txtxinchao.setText("Xin chào, " + cuser.getString(0));
        }
        cuser.close();

        // Khởi tạo danh sách món
        dsmamon = new ArrayList<Integer>();
        dsmon = new ArrayList<String>();
        adaptermon = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dsmon);
        lvmon.setAdapter(adaptermon);

        // Nạp danh mục vào Spinner
        taiDanhMuc();
        taiDanhSachMon();

        // Đổi danh mục thì nạp lại danh sách món
        spdanhmuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taiDanhSachMon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Bấm nút tìm
        btntim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taiDanhSachMon();
            }
        });

        // Bấm vào món để xem chi tiết
        lvmon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MenuActivity.this, ChiTietMonActivity.class);
                Bundle goi = new Bundle();
                goi.putInt("userid", userid);
                goi.putInt("mamon", dsmamon.get(position));
                intent.putExtra("goi", goi);
                startActivity(intent);
            }
        });

        // Mở giỏ hàng
        btngiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GioHangActivity.class);
                Bundle goi = new Bundle();
                goi.putInt("userid", userid);
                intent.putExtra("goi", goi);
                startActivity(intent);
            }
        });

        // Mở lịch sử đơn hàng
        btnlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LichSuActivity.class);
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

    // Nạp danh mục vào Spinner, vị trí 0 là "Tất cả"
    private void taiDanhMuc() {
        dsmadm = new ArrayList<Integer>();
        ArrayList<String> dsten = new ArrayList<String>();
        dsmadm.add(0);
        dsten.add("Tất cả");
        Cursor c = mydatabase.rawQuery("SELECT id, tendm FROM tbldanhmuc", null);
        while (c.moveToNext()) {
            dsmadm.add(c.getInt(0));
            dsten.add(c.getString(1));
        }
        c.close();
        ArrayAdapter<String> adapterdm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsten);
        adapterdm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdanhmuc.setAdapter(adapterdm);
    }

    // Nạp danh sách món theo danh mục và từ khóa tìm kiếm
    private void taiDanhSachMon() {
        dsmamon.clear();
        dsmon.clear();
        String tukhoa = edittimkiem.getText().toString().trim();
        int madm = dsmadm.get(spdanhmuc.getSelectedItemPosition());
        String sql = "SELECT id, tenmon, gia FROM tblmon WHERE tenmon LIKE ?";
        String[] thamso;
        if (madm == 0) {
            thamso = new String[]{"%" + tukhoa + "%"};
        } else {
            sql = sql + " AND madm = ?";
            thamso = new String[]{"%" + tukhoa + "%", String.valueOf(madm)};
        }
        Cursor c = mydatabase.rawQuery(sql, thamso);
        while (c.moveToNext()) {
            dsmamon.add(c.getInt(0));
            dsmon.add(c.getString(1) + " - " + dinhDangTien(c.getInt(2)));
        }
        c.close();
        adaptermon.notifyDataSetChanged();
    }

    // Định dạng tiền: 25000 -> 25.000 đ
    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}