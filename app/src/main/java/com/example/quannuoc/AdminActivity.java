package com.example.quannuoc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader;
    TextView txtxinchao;
    Button btnquanlymon, btnquanlydanhmuc, btnquanlydon, btnquaylai;
    SQLiteDatabase mydatabase;
    int userid, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        txtxinchao = findViewById(R.id.txtxinchao);
        btnquanlymon = findViewById(R.id.btnquanlymon);
        btnquanlydanhmuc = findViewById(R.id.btnquanlydanhmuc);
        btnquanlydon = findViewById(R.id.btnquanlydon);
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(24 * getResources().getDisplayMetrics().density);
        llheader.setBackground(nenheader);

        Bundle bundle = getIntent().getBundleExtra("goi");
        if (bundle != null) {
            userid = bundle.getInt("userid");
            role = bundle.getInt("role");
        }
        if (role != 1) {
            String msg = "Bạn không có quyền truy cập trang quản trị";
            Toast.makeText(AdminActivity.this, msg, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        hienXinChao();

        btnquanlymon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminActivity.this, QuanLyMonActivity.class);
                myIntent.putExtra("goi", taoGoi());
                startActivity(myIntent);
            }
        });

        btnquanlydanhmuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminActivity.this, QuanLyDanhMucActivity.class);
                myIntent.putExtra("goi", taoGoi());
                startActivity(myIntent);
            }
        });

        btnquanlydon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AdminActivity.this, QuanLyDonActivity.class);
                myIntent.putExtra("goi", taoGoi());
                startActivity(myIntent);
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

    private void hienXinChao() {
        Cursor c = mydatabase.query("tbluser", null, "id = ?",
                new String[]{String.valueOf(userid)}, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            txtxinchao.setText("Xin chào, " + c.getString(3));
        }
        c.close();
    }

    private Bundle taoGoi() {
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        bundle.putInt("role", role);
        return bundle;
    }
}