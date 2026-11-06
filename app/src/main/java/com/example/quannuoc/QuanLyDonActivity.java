package com.example.quannuoc;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuanLyDonActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    ListView lvdonhang;
    Button btnquaylai;
    SQLiteDatabase mydatabase;

    ArrayList<DonHang> listDon;
    DonHangAdapter adapterDon;

    // Lớp mô hình đơn hàng
    class DonHang {
        int id, userid, tongtien, trangthai;
        String ngaydat;
        public DonHang(int id, int userid, String ngaydat, int tongtien, int trangthai) {
            this.id = id; this.userid = userid; this.ngaydat = ngaydat; this.tongtien = tongtien; this.trangthai = trangthai;
        }
    }

    // Adapter hiển thị thông tin 2 dòng cho danh sách đơn hàng
    class DonHangAdapter extends ArrayAdapter<DonHang> {
        public DonHangAdapter(Context context, List<DonHang> objects) {
            super(context, android.R.layout.simple_list_item_2, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text1 = view.findViewById(android.R.id.text1);
            TextView text2 = view.findViewById(android.R.id.text2);
            DonHang d = getItem(position);
            
            text1.setText("Đơn hàng #" + d.id + " - Ngày: " + d.ngaydat);
            
            // Xử lý chuỗi trạng thái
            String strTrangThai = "";
            if (d.trangthai == 0) strTrangThai = "Chờ xác nhận";
            else if (d.trangthai == 1) strTrangThai = "Đang làm";
            else if (d.trangthai == 2) strTrangThai = "Hoàn thành";
            else if (d.trangthai == 3) strTrangThai = "Đã hủy";
            
            text2.setText("Tổng tiền: " + dinhDangTien(d.tongtien) + " - " + strTrangThai);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanlydon);

        //Bước 2: Ánh xạ biến giao diện
        lvdonhang = findViewById(R.id.lvdonhang);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        listDon = new ArrayList<>();
        adapterDon = new DonHangAdapter(this, listDon);
        lvdonhang.setAdapter(adapterDon);

        loadDon();

        // Nhấn vào một đơn hàng để cập nhật trạng thái
        lvdonhang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonHang don = listDon.get(position);
                String[] statusNames = {"Chờ xác nhận", "Đang làm", "Hoàn thành", "Hủy"};
                
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDonActivity.this);
                builder.setTitle("Cập nhật trạng thái đơn #" + don.id);
                builder.setSingleChoiceItems(statusNames, don.trangthai, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which tương ứng với chỉ mục 0,1,2,3 - đúng bằng trạng thái trong CSDL
                        ContentValues cv = new ContentValues();
                        cv.put("trangthai", which);
                        mydatabase.update("tbldonhang", cv, "id = ?", new String[]{String.valueOf(don.id)});
                        dialog.dismiss();
                        Toast.makeText(QuanLyDonActivity.this, "Đã cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                        loadDon();
                    }
                });
                builder.setNegativeButton("Đóng", null);
                builder.show();
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

    private void loadDon() {
        listDon.clear();
        Cursor c = mydatabase.rawQuery("SELECT * FROM tbldonhang ORDER BY id DESC", null);
        while (c.moveToNext()) {
            listDon.add(new DonHang(c.getInt(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4)));
        }
        c.close();
        adapterDon.notifyDataSetChanged();
    }

    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}
