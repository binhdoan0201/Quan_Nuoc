package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class QuanLyDonActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader;
    TextView txttrong;
    ListView lvdonhang;
    Button btnquaylai;
    SQLiteDatabase mydatabase;

    ArrayList<DonHang> listDon;
    DonHangAdapter adapterDon;

    // Lớp mô hình đơn hàng đầy đủ thông tin khách
    class DonHang {
        int id, userid, tongtien, trangthai;
        String ngaydat, fullname, phone;

        public DonHang(int id, int userid, String ngaydat, int tongtien, int trangthai, String fullname, String phone) {
            this.id = id;
            this.userid = userid;
            this.ngaydat = ngaydat;
            this.tongtien = tongtien;
            this.trangthai = trangthai;
            this.fullname = (fullname != null && !fullname.isEmpty()) ? fullname : "Khách vãng lai";
            this.phone = (phone != null && !phone.isEmpty()) ? phone : "Chưa có SĐT";
        }
    }

    // Adapter tuỳ chỉnh: mỗi đơn hàng là một thẻ có nhãn trạng thái
    class DonHangAdapter extends ArrayAdapter<DonHang> {
        public DonHangAdapter(Context context, List<DonHang> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DonHang d = getItem(position);
            LinearLayout the = new LinearLayout(QuanLyDonActivity.this);
            the.setOrientation(LinearLayout.VERTICAL);
            the.setPadding(dp(14), dp(12), dp(14), dp(12));
            the.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));

            // Hàng trên: mã đơn và khách bên trái, nhãn trạng thái bên phải
            LinearLayout hangtren = new LinearLayout(QuanLyDonActivity.this);
            hangtren.setOrientation(LinearLayout.HORIZONTAL);
            hangtren.setGravity(Gravity.CENTER_VERTICAL);

            TextView txtkhach = new TextView(QuanLyDonActivity.this);
            txtkhach.setText("Đơn #" + d.id + " - " + d.fullname);
            txtkhach.setTextSize(16);
            txtkhach.setTypeface(null, Typeface.BOLD);
            txtkhach.setTextColor(Color.parseColor("#3E2C23"));
            txtkhach.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            hangtren.addView(txtkhach);

            TextView txttrangthai = new TextView(QuanLyDonActivity.this);
            txttrangthai.setText(layTenTrangThai(d.trangthai));
            txttrangthai.setTextSize(12);
            txttrangthai.setTypeface(null, Typeface.BOLD);
            txttrangthai.setTextColor(Color.WHITE);
            txttrangthai.setPadding(dp(10), dp(4), dp(10), dp(4));
            txttrangthai.setBackground(taoNen(layMauTrangThai(d.trangthai), 20, null));
            hangtren.addView(txttrangthai);
            the.addView(hangtren);

            TextView txtsdt = new TextView(QuanLyDonActivity.this);
            txtsdt.setText(d.phone + "   •   " + d.ngaydat);
            txtsdt.setTextSize(13);
            txtsdt.setTextColor(Color.parseColor("#9A8B7E"));
            txtsdt.setPadding(0, dp(4), 0, 0);
            the.addView(txtsdt);

            TextView txttongtien = new TextView(QuanLyDonActivity.this);
            txttongtien.setText(dinhDangTien(d.tongtien));
            txttongtien.setTextSize(16);
            txttongtien.setTypeface(null, Typeface.BOLD);
            txttongtien.setTextColor(Color.parseColor("#FC6076"));
            txttongtien.setPadding(0, dp(4), 0, 0);
            the.addView(txttongtien);
            return the;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanlydon);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        txttrong = findViewById(R.id.txttrong);
        lvdonhang = findViewById(R.id.lvdonhang);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);

        listDon = new ArrayList<>();
        adapterDon = new DonHangAdapter(this, listDon);
        lvdonhang.setAdapter(adapterDon);
        lvdonhang.setEmptyView(txttrong);

        loadDon();

        // Nhấn vào một đơn hàng để xem chi tiết món và cập nhật trạng thái theo luồng chuẩn
        lvdonhang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonHang don = listDon.get(position);
                hienThiChiTietVaXuLyDon(don);
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

    // Màu nhãn theo trạng thái đơn
    private int layMauTrangThai(int trangthai) {
        switch (trangthai) {
            case 0:
                return Color.parseColor("#FF9A44");
            case 1:
                return Color.parseColor("#2EC4B6");
            case 2:
                return Color.parseColor("#4CAF50");
            default:
                return Color.parseColor("#9A8B7E");
        }
    }

    private void loadDon() {
        listDon.clear();
        String sql = "SELECT tbldonhang.id, tbldonhang.userid, tbldonhang.ngaydat, tbldonhang.tongtien, tbldonhang.trangthai, tbluser.fullname, tbluser.phone " +
                "FROM tbldonhang " +
                "LEFT JOIN tbluser ON tbldonhang.userid = tbluser.id " +
                "ORDER BY tbldonhang.id DESC";
        Cursor c = mydatabase.rawQuery(sql, null);
        while (c.moveToNext()) {
            listDon.add(new DonHang(c.getInt(0), c.getInt(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5), c.getString(6)));
        }
        c.close();
        adapterDon.notifyDataSetChanged();
    }

    private void hienThiChiTietVaXuLyDon(DonHang don) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyDonActivity.this);
        builder.setTitle("Chi tiết đơn hàng #" + don.id);

        // Đọc danh sách chi tiết các món đã gọi từ tblchitiet
        StringBuilder chiTietMon = new StringBuilder();
        String sql = "SELECT tblmon.tenmon, tblchitiet.soluong, tblchitiet.gia " +
                "FROM tblchitiet " +
                "INNER JOIN tblmon ON tblchitiet.mamon = tblmon.id " +
                "WHERE tblchitiet.madon = ?";
        Cursor c = mydatabase.rawQuery(sql, new String[]{String.valueOf(don.id)});

        if (c != null) {
            if (c.moveToFirst()) {
                int stt = 1;
                do {
                    String tenmon = c.getString(0);
                    int soluong = c.getInt(1);
                    int gia = c.getInt(2);
                    int thanhTien = soluong * gia;

                    chiTietMon.append(stt).append(". ").append(tenmon)
                            .append(" x").append(soluong)
                            .append(" (").append(dinhDangTien(gia)).append(")")
                            .append(" = ").append(dinhDangTien(thanhTien)).append("\n");
                    stt++;
                } while (c.moveToNext());
            } else {
                chiTietMon.append("(Không có dữ liệu chi tiết món)\n");
            }
            c.close();
        }

        StringBuilder noiDung = new StringBuilder();
        noiDung.append("Khách hàng: ").append(don.fullname).append("\n");
        noiDung.append("Số điện thoại: ").append(don.phone).append("\n");
        noiDung.append("Thời gian: ").append(don.ngaydat).append("\n\n");
        noiDung.append("--- CÁC MÓN CẦN PHA CHẾ ---\n");
        noiDung.append(chiTietMon);
        noiDung.append("\nTổng thanh toán: ").append(dinhDangTien(don.tongtien)).append("\n");
        noiDung.append("Trạng thái: ").append(layTenTrangThai(don.trangthai));

        // Áp dụng State Machine: Chỉ cho phép chuyển trạng thái theo luồng hợp lệ
        if (don.trangthai == 0) {
            // Trạng thái 0 (Chờ): Cho phép Nhận đơn (-> 1) hoặc Hủy đơn (-> 3)
            builder.setMessage(noiDung.toString());
            builder.setPositiveButton("Nhận đơn (Đang làm)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    capNhatTrangThai(don.id, 1, "Đã nhận đơn và chuyển sang Đang làm");
                }
            });
            builder.setNeutralButton("Hủy đơn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    capNhatTrangThai(don.id, 3, "Đã hủy đơn hàng");
                }
            });
            builder.setNegativeButton("Đóng", null);
        } else if (don.trangthai == 1) {
            // Trạng thái 1 (Đang làm): Cho phép Hoàn thành (-> 2) hoặc Hủy đơn (-> 3)
            builder.setMessage(noiDung.toString());
            builder.setPositiveButton("Hoàn thành & Giao", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    capNhatTrangThai(don.id, 2, "Đơn hàng đã hoàn thành");
                }
            });
            builder.setNeutralButton("Hủy đơn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    capNhatTrangThai(don.id, 3, "Đã hủy đơn hàng");
                }
            });
            builder.setNegativeButton("Đóng", null);
        } else if (don.trangthai == 2) {
            // Trạng thái 2 (Hoàn thành): Khóa đơn, không cho sửa trạng thái
            noiDung.append("\n\n(Đơn hàng đã hoàn thành - Không thể chỉnh sửa)");
            builder.setMessage(noiDung.toString());
            builder.setNegativeButton("Đóng", null);
        } else {
            // Trạng thái 3 (Đã hủy): Khóa đơn, không cho sửa trạng thái
            noiDung.append("\n\n(Đơn hàng đã bị hủy - Không thể chỉnh sửa)");
            builder.setMessage(noiDung.toString());
            builder.setNegativeButton("Đóng", null);
        }

        builder.show();
    }

    private void capNhatTrangThai(int madon, int trangthaiMoi, String thongBao) {
        ContentValues cv = new ContentValues();
        cv.put("trangthai", trangthaiMoi);
        mydatabase.update("tbldonhang", cv, "id = ?", new String[]{String.valueOf(madon)});
        Toast.makeText(QuanLyDonActivity.this, thongBao, Toast.LENGTH_SHORT).show();
        loadDon();
    }

    private String layTenTrangThai(int trangthai) {
        switch (trangthai) {
            case 0:
                return "Chờ xác nhận";
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

    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}