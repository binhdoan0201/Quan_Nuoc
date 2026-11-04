package com.example.quannuoc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    TextView txtxinchao;
    Button btnquanlymon, btnquanlydanhmuc, btnquanlydon, btnquaylai;
    SQLiteDatabase mydatabase;
    int userid, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        txtxinchao = findViewById(R.id.txtxinchao);
        btnquanlymon = findViewById(R.id.btnquanlymon);
        btnquanlydanhmuc = findViewById(R.id.btnquanlydanhmuc);
        btnquanlydon = findViewById(R.id.btnquanlydon);
        btnquaylai = findViewById(R.id.btnquaylai);

        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("goi");
        if (bundle != null) {
            userid = bundle.getInt("userid");
            role = bundle.getInt("role");
        }
        if (role != 1) {
            Toast.makeText(this, "Bạn không có quyền truy cập trang quản trị", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor c = mydatabase.rawQuery("SELECT fullname FROM tbluser WHERE id = ?",
                new String[]{String.valueOf(userid)});
        if (c.moveToFirst()) {
            txtxinchao.setText("Xin chào, " + c.getString(0));
        }
        c.close();

        btnquanlymon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moManHinh(QuanLyMonActivity.class);
            }
        });

        btnquanlydanhmuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moManHinh(QuanLyDanhMucActivity.class);
            }
        });

        btnquanlydon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moManHinh(QuanLyDonActivity.class);
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

    private void moManHinh(Class<?> manHinh) {
        Intent intent = new Intent(AdminActivity.this, manHinh);
        Bundle bundle = new Bundle();
        bundle.putInt("userid", userid);
        bundle.putInt("role", role);
        intent.putExtra("goi", bundle);
        startActivity(intent);
    }
}
