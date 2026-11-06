package com.example.quannuoc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LichSuActivity extends AppCompatActivity {

    //Bước 1: Khai báo biến giao diện
    ListView lvlichsu;
    Button btnquaylai;

    SQLiteDatabase mydatabase;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    ArrayList<Integer> listDonHangId;
    ArrayList<Integer> listTrangThai;

    int userid;
    DecimalFormat df = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lichsu);

        //Bước 2: Ánh xạ biến giao diện
        lvlichsu = findViewById(R.id.lvlichsu);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Nhận Bundle truyền sang từ MenuActivity
        Bundle bundle = getIntent().getBundleExtra("goi");
        if (bundle != null) {
            userid = bundle.getInt("userid");
        }

        mylist = new ArrayList<>();
        listDonHangId = new ArrayList<>();
        listTrangThai = new ArrayList<>();
        myadapter = new ArrayAdapter<>(LichSuActivity.this, android.R.layout.simple_list_item_1, mylist);
        lvlichsu.setAdapter(myadapter);

        // Load lịch sử đơn hàng
        loadLichSuDonHang();

        // Click một dòng để xem chi tiết đơn hàng và cho phép hủy đơn nếu còn "Chờ"
        lvlichsu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int madon = listDonHangId.get(position);
                int trangthai = listTrangThai.get(position);
                hienThiChiTietDonHang(madon, trangthai);
            }
        });

        // Nút quay lại
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

    private void loadLichSuDonHang() {
        mylist.clear();
        listDonHangId.clear();
        listTrangThai.clear();

        Cursor c = mydatabase.query("tbldonhang", null, "userid = ?", new String[]{String.valueOf(userid)}, null, null, "id DESC");

        if (c != null && c.moveToFirst()) {
            do {
                int madon = c.getInt(0);
                String ngaydat = c.getString(2);
                int tongtien = c.getInt(3);
                int trangthai = c.getInt(4);

                String strTrangThai = getTenTrangThai(trangthai);
                String data = "Mã đơn: #" + madon + "\nNgày: " + ngaydat + "\nTổng tiền: " + df.format(tongtien) + " đ\nTrạng thái: " + strTrangThai;

                mylist.add(data);
                listDonHangId.add(madon);
                listTrangThai.add(trangthai);
            } while (c.moveToNext());
            c.close();
        }

        myadapter.notifyDataSetChanged();
    }

    private String getTenTrangThai(int trangthai) {
        switch (trangthai) {
            case 0:
                return "Chờ";
            case 1:
                return "Đang làm";
            case 2:
                return "Hoàn thành";
            case 3:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

    private void hienThiChiTietDonHang(int madon, int trangthai) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LichSuActivity.this);
        builder.setTitle("Chi tiết đơn hàng #" + madon);

        StringBuilder details = new StringBuilder();
        String sql = "SELECT tblmon.tenmon, tblchitiet.soluong, tblchitiet.gia " +
                "FROM tblchitiet " +
                "INNER JOIN tblmon ON tblchitiet.mamon = tblmon.id " +
                "WHERE tblchitiet.madon = ?";
        Cursor c = mydatabase.rawQuery(sql, new String[]{String.valueOf(madon)});

        if (c != null && c.moveToFirst()) {
            int stt = 1;
            do {
                String tenmon = c.getString(0);
                int soluong = c.getInt(1);
                int gia = c.getInt(2);
                int thanhTien = soluong * gia;

                details.append(stt).append(". ").append(tenmon)
                        .append(" x").append(soluong)
                        .append(" (").append(df.format(gia)).append(" đ)")
                        .append(" = ").append(df.format(thanhTien)).append(" đ\n");
                stt++;
            } while (c.moveToNext());
            c.close();
        } else {
            details.append("Không có chi tiết sản phẩm.");
        }

        details.append("\nTrạng thái: ").append(getTenTrangThai(trangthai));
        builder.setMessage(details.toString());

        // Cho phép hủy đơn nếu đơn hàng vẫn đang ở trạng thái 0 ("Chờ")
        if (trangthai == 0) {
            builder.setPositiveButton("Hủy đơn hàng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xuLyHuyDon(madon);
                }
            });
        }

        builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void xuLyHuyDon(int madon) {
        ContentValues myvalue = new ContentValues();
        myvalue.put("trangthai", 3); // 3 = Đã hủy

        int rows = mydatabase.update("tbldonhang", myvalue, "id = ?", new String[]{String.valueOf(madon)});
        if (rows > 0) {
            Toast.makeText(LichSuActivity.this, "Đã hủy đơn hàng #" + madon, Toast.LENGTH_SHORT).show();
            loadLichSuDonHang();
        } else {
            Toast.makeText(LichSuActivity.this, "Không thể hủy đơn hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}