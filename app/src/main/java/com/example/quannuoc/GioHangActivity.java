package com.example.quannuoc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GioHangActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader;
    LinearLayout lltongtien;
    TextView txttongtien;
    TextView txttrong;
    ListView lvgiohang;
    Button btndathang, btnquaylai;

    SQLiteDatabase mydatabase;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    ArrayList<Integer> listGioHangId;
    ArrayList<Integer> listSoLuong;

    int userid;
    int tongTienTatCa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giohang);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        lltongtien = findViewById(R.id.lltongtien);
        txttongtien = findViewById(R.id.txttongtien);
        txttrong = findViewById(R.id.txttrong);
        lvgiohang = findViewById(R.id.lvgiohang);
        btndathang = findViewById(R.id.btndathang);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);
        lltongtien.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));

        // Nhận Bundle gửi sang từ MenuActivity hoặc ChiTietMonActivity
        Bundle bundle = getIntent().getBundleExtra("goi");
        if (bundle != null) {
            userid = bundle.getInt("userid");
        }

        mylist = new ArrayList<>();
        listGioHangId = new ArrayList<>();
        listSoLuong = new ArrayList<>();

        // Mỗi món trong giỏ là một thẻ bo góc
        myadapter = new ArrayAdapter<String>(GioHangActivity.this, android.R.layout.simple_list_item_1, mylist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return taoTheMon(getItem(position));
            }
        };
        lvgiohang.setAdapter(myadapter);
        lvgiohang.setEmptyView(txttrong);

        // Load danh sách giỏ hàng
        loadGioHang();

        // Sửa số lượng khi click vào món trong ListView
        lvgiohang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int giohangId = listGioHangId.get(position);
                int soluongHienTai = listSoLuong.get(position);
                hienThiDialogSuaSoLuong(giohangId, soluongHienTai);
            }
        });

        // Xóa món khỏi giỏ bằng Long Click (có dialog xác nhận)
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int giohangId = listGioHangId.get(position);
                hienThiDialogXoa(giohangId);
                return true;
            }
        });

        // Đặt hàng
        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDatHang();
            }
        });

        // Nút quay lại
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

    // Tạo thẻ món từ chuỗi: tên | SL | Đơn giá | Thành tiền
    private View taoTheMon(String data) {
        String[] phan = data.split(" \\| ");
        LinearLayout the = new LinearLayout(this);
        the.setOrientation(LinearLayout.VERTICAL);
        the.setPadding(dp(14), dp(12), dp(14), dp(12));
        the.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));

        TextView txtten = new TextView(this);
        txtten.setText(phan[0]);
        txtten.setTextSize(16);
        txtten.setTypeface(null, Typeface.BOLD);
        txtten.setTextColor(Color.parseColor("#3E2C23"));
        the.addView(txtten);

        if (phan.length >= 4) {
            TextView txtchitiet = new TextView(this);
            txtchitiet.setText(phan[1] + "   •   " + phan[2]);
            txtchitiet.setTextSize(13);
            txtchitiet.setTextColor(Color.parseColor("#9A8B7E"));
            the.addView(txtchitiet);

            TextView txtthanhtien = new TextView(this);
            txtthanhtien.setText(phan[3]);
            txtthanhtien.setTextSize(15);
            txtthanhtien.setTypeface(null, Typeface.BOLD);
            txtthanhtien.setTextColor(Color.parseColor("#FC6076"));
            txtthanhtien.setPadding(0, dp(4), 0, 0);
            the.addView(txtthanhtien);
        }
        return the;
    }

    private void loadGioHang() {
        mylist.clear();
        listGioHangId.clear();
        listSoLuong.clear();
        tongTienTatCa = 0;

        String sql = "SELECT tblgiohang.id, tblmon.tenmon, tblgiohang.soluong, tblmon.gia " +
                "FROM tblgiohang " +
                "INNER JOIN tblmon ON tblgiohang.mamon = tblmon.id " +
                "WHERE tblgiohang.userid = ?";
        Cursor c = mydatabase.rawQuery(sql, new String[]{String.valueOf(userid)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    int idGioHang = c.getInt(0);
                    String tenmon = c.getString(1);
                    int soluong = c.getInt(2);
                    int gia = c.getInt(3);
                    int thanhTien = soluong * gia;
                    tongTienTatCa += thanhTien;

                    String data = tenmon + " | SL: " + soluong + " | Đơn giá: " + dinhDangTien(gia) + " | Thành tiền: " + dinhDangTien(thanhTien);
                    mylist.add(data);
                    listGioHangId.add(idGioHang);
                    listSoLuong.add(soluong);
                } while (c.moveToNext());
            }
            c.close();
        }

        myadapter.notifyDataSetChanged();
        txttongtien.setText("Tổng tiền: " + dinhDangTien(tongTienTatCa));
    }

    private void hienThiDialogSuaSoLuong(int giohangId, int soluongHienTai) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
        builder.setTitle("Sửa số lượng");

        final EditText editsoluong = new EditText(GioHangActivity.this);
        editsoluong.setInputType(InputType.TYPE_CLASS_NUMBER);
        editsoluong.setText(String.valueOf(soluongHienTai));
        builder.setView(editsoluong);

        builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strSoLuong = editsoluong.getText().toString().trim();
                if (strSoLuong.isEmpty()) {
                    Toast.makeText(GioHangActivity.this, "Số lượng không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                int soluongMoi;
                try {
                    soluongMoi = Integer.parseInt(strSoLuong);
                } catch (NumberFormatException e) {
                    Toast.makeText(GioHangActivity.this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (soluongMoi <= 0 || soluongMoi > 999) {
                    Toast.makeText(GioHangActivity.this, "Số lượng phải từ 1 đến 999", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues myvalue = new ContentValues();
                myvalue.put("soluong", soluongMoi);
                mydatabase.update("tblgiohang", myvalue, "id = ?", new String[]{String.valueOf(giohangId)});
                Toast.makeText(GioHangActivity.this, "Đã cập nhật số lượng", Toast.LENGTH_SHORT).show();
                loadGioHang();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void hienThiDialogXoa(int giohangId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa món này khỏi giỏ hàng?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydatabase.delete("tblgiohang", "id = ?", new String[]{String.valueOf(giohangId)});
                Toast.makeText(GioHangActivity.this, "Đã xóa món khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                loadGioHang();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void xuLyDatHang() {
        if (mylist.isEmpty()) {
            Toast.makeText(GioHangActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Tạo 1 dòng trong tbldonhang
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String ngaydat = sdf.format(new Date());

        ContentValues valDonHang = new ContentValues();
        valDonHang.put("userid", userid);
        valDonHang.put("ngaydat", ngaydat);
        valDonHang.put("tongtien", tongTienTatCa);
        valDonHang.put("trangthai", 0); // 0 = Chờ

        long madonLong = mydatabase.insert("tbldonhang", null, valDonHang);
        int madon = (int) madonLong;

        if (madon != -1) {
            // 2. Chuyển tất cả món từ tblgiohang sang tblchitiet
            String sql = "SELECT tblgiohang.mamon, tblgiohang.soluong, tblmon.gia " +
                    "FROM tblgiohang " +
                    "INNER JOIN tblmon ON tblgiohang.mamon = tblmon.id " +
                    "WHERE tblgiohang.userid = ?";
            Cursor c = mydatabase.rawQuery(sql, new String[]{String.valueOf(userid)});

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        int mamon = c.getInt(0);
                        int soluong = c.getInt(1);
                        int gia = c.getInt(2);

                        ContentValues valChiTiet = new ContentValues();
                        valChiTiet.put("madon", madon);
                        valChiTiet.put("mamon", mamon);
                        valChiTiet.put("soluong", soluong);
                        valChiTiet.put("gia", gia);

                        mydatabase.insert("tblchitiet", null, valChiTiet);
                    } while (c.moveToNext());
                }
                c.close();
            }

            // 3. Xóa các dòng giỏ hàng của user sau khi đã đặt hàng thành công
            mydatabase.delete("tblgiohang", "userid = ?", new String[]{String.valueOf(userid)});

            Toast.makeText(GioHangActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            loadGioHang();
        } else {
            Toast.makeText(GioHangActivity.this, "Đặt hàng thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}