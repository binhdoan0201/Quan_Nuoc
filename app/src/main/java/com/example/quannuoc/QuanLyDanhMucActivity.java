package com.example.quannuoc;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class QuanLyDanhMucActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    EditText edittendm;
    Button btnthem, btnsua, btnxoa, btnquaylai;
    ListView lvdanhsach;
    SQLiteDatabase mydatabase;

    ArrayList<DanhMuc> list;
    ArrayAdapter<DanhMuc> adapter;
    int selectedId = -1;

    // Lớp mô hình danh mục
    class DanhMuc {
        int id;
        String tendm;
        public DanhMuc(int id, String tendm) { 
            this.id = id; 
            this.tendm = tendm; 
        }
        @Override 
        public String toString() { 
            return tendm; 
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanlydanhmuc);

        //Bước 2: Ánh xạ biến giao diện
        edittendm = findViewById(R.id.edittendm);
        btnthem = findViewById(R.id.btnthem);
        btnsua = findViewById(R.id.btnsua);
        btnxoa = findViewById(R.id.btnxoa);
        btnquaylai = findViewById(R.id.btnquaylai);
        lvdanhsach = findViewById(R.id.lvdanhsach);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvdanhsach.setAdapter(adapter);

        loadData();

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edittendm.getText().toString().trim();
                if (ten.isEmpty()) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues cv = new ContentValues();
                cv.put("tendm", ten);
                mydatabase.insert("tbldanhmuc", null, cv);
                Toast.makeText(QuanLyDanhMucActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                edittendm.setText("");
                loadData();
            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Vui lòng chọn danh mục bên dưới để sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ten = edittendm.getText().toString().trim();
                if (ten.isEmpty()) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues cv = new ContentValues();
                cv.put("tendm", ten);
                mydatabase.update("tbldanhmuc", cv, "id = ?", new String[]{String.valueOf(selectedId)});
                Toast.makeText(QuanLyDanhMucActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                edittendm.setText("");
                selectedId = -1;
                loadData();
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Vui lòng chọn danh mục để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }
                mydatabase.delete("tbldanhmuc", "id = ?", new String[]{String.valueOf(selectedId)});
                Toast.makeText(QuanLyDanhMucActivity.this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                edittendm.setText("");
                selectedId = -1;
                loadData();
            }
        });

        lvdanhsach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanhMuc dm = list.get(position);
                selectedId = dm.id;
                edittendm.setText(dm.tendm);
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

    private void loadData() {
        list.clear();
        Cursor c = mydatabase.rawQuery("SELECT * FROM tbldanhmuc", null);
        while (c.moveToNext()) {
            list.add(new DanhMuc(c.getInt(0), c.getString(1)));
        }
        c.close();
        adapter.notifyDataSetChanged();
    }
}
