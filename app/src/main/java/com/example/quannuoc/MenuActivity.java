package com.example.quannuoc;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader;
    LinearLayout llspinner;
    LinearLayout llsearch;
    TextView txtxinchao;
    TextView txttrong;
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
    ArrayList<String[]> dsmon;
    ArrayAdapter<String[]> adaptermon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        llspinner = findViewById(R.id.llspinner);
        llsearch = findViewById(R.id.llsearch);
        txtxinchao = findViewById(R.id.txtxinchao);
        txttrong = findViewById(R.id.txttrong);
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

        // Tạo nền bo góc bằng code (không cần file drawable)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);
        llspinner.setBackground(taoNen(Color.WHITE, 14, "#E3D8CC"));
        llsearch.setBackground(taoNen(Color.WHITE, 14, "#E3D8CC"));

        // Hiện lời chào theo tên người dùng
        Cursor cuser = mydatabase.rawQuery("SELECT fullname FROM tbluser WHERE id = ?", new String[]{String.valueOf(userid)});
        if (cuser.moveToFirst()) {
            txtxinchao.setText("Xin chào, " + cuser.getString(0));
        }
        cuser.close();

        // Khởi tạo danh sách món, mỗi dòng là một thẻ có ảnh
        dsmamon = new ArrayList<Integer>();
        dsmon = new ArrayList<String[]>();
        adaptermon = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_1, dsmon) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return taoTheMon(getItem(position));
            }
        };
        lvmon.setAdapter(adaptermon);
        lvmon.setEmptyView(txttrong);

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

    // Tạo thẻ món: dong gồm {tên món, danh mục, giá, mã món}
    private View taoTheMon(String[] dong) {
        LinearLayout the = new LinearLayout(this);
        the.setOrientation(LinearLayout.HORIZONTAL);
        the.setGravity(Gravity.CENTER_VERTICAL);
        the.setPadding(dp(10), dp(10), dp(10), dp(10));
        the.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));

        // Ảnh món lấy từ drawable mon + mã món, không có thì dùng ảnh mặc định
        ImageView imgmon = new ImageView(this);
        imgmon.setLayoutParams(new LinearLayout.LayoutParams(dp(76), dp(76)));
        imgmon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgmon.setBackground(taoNen(Color.parseColor("#F1E9E0"), 12, null));
        imgmon.setClipToOutline(true);
        int maanh = 0;
        if (dong.length > 4 && dong[4] != null && !dong[4].isEmpty()) {
            maanh = getResources().getIdentifier(dong[4], "drawable", getPackageName());
        }
        if (maanh == 0) {
            maanh = getResources().getIdentifier("mon" + dong[3], "drawable", getPackageName());
        }
        if (maanh != 0) {
            imgmon.setImageResource(maanh);
        } else {
            imgmon.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        the.addView(imgmon);

        // Cột chữ bên phải ảnh
        LinearLayout cot = new LinearLayout(this);
        cot.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams thamsocot = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        thamsocot.setMarginStart(dp(12));
        cot.setLayoutParams(thamsocot);

        TextView txtten = new TextView(this);
        txtten.setText(dong[0]);
        txtten.setTextSize(16);
        txtten.setTypeface(null, Typeface.BOLD);
        txtten.setTextColor(Color.parseColor("#3E2C23"));
        txtten.setMaxLines(2);
        cot.addView(txtten);

        TextView txtdanhmuc = new TextView(this);
        txtdanhmuc.setText(dong[1]);
        txtdanhmuc.setTextSize(12);
        txtdanhmuc.setTextColor(Color.parseColor("#9A8B7E"));
        cot.addView(txtdanhmuc);

        TextView txtgia = new TextView(this);
        txtgia.setText(dong[2]);
        txtgia.setTextSize(16);
        txtgia.setTypeface(null, Typeface.BOLD);
        txtgia.setTextColor(Color.parseColor("#FC6076"));
        txtgia.setPadding(0, dp(6), 0, 0);
        cot.addView(txtgia);

        the.addView(cot);
        return the;
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
        String sql = "SELECT m.id, m.tenmon, m.gia, d.tendm, m.hinhanh FROM tblmon m LEFT JOIN tbldanhmuc d ON m.madm = d.id WHERE m.tenmon LIKE ?";
        String[] thamso;
        if (madm == 0) {
            thamso = new String[]{"%" + tukhoa + "%"};
        } else {
            sql = sql + " AND m.madm = ?";
            thamso = new String[]{"%" + tukhoa + "%", String.valueOf(madm)};
        }
        Cursor c = mydatabase.rawQuery(sql, thamso);
        while (c.moveToNext()) {
            dsmon.add(new String[]{c.getString(1), c.getString(3), dinhDangTien(c.getInt(2)), String.valueOf(c.getInt(0)), c.getString(4)});
            dsmamon.add(c.getInt(0));
        }
        c.close();
        adaptermon.notifyDataSetChanged();
    }

    // Định dạng tiền: 25000 -> 25.000 đ
    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}