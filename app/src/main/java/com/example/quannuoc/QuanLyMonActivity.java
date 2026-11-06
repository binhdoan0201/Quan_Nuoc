package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuanLyMonActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    EditText edittenmon, editmota, editgia;
    Spinner spdanhmuc;
    Button btnthem, btnsua, btnxoa, btnquaylai;
    ListView lvdanhsach;
    SQLiteDatabase mydatabase;

    ArrayList<DanhMuc> listDanhMuc;
    ArrayAdapter<DanhMuc> adapterDanhMuc;

    ArrayList<Mon> listMon;
    MonAdapter adapterMon;
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

    // Lớp mô hình món
    class Mon {
        int id, gia, madm;
        String tenmon, mota, tendm;
        public Mon(int id, String tenmon, String mota, int gia, int madm, String tendm) {
            this.id = id; this.tenmon = tenmon; this.mota = mota; this.gia = gia; this.madm = madm; this.tendm = tendm;
        }
    }

    // Adapter tuỳ chỉnh để hiển thị món trên 2 dòng
    class MonAdapter extends ArrayAdapter<Mon> {
        public MonAdapter(Context context, List<Mon> objects) {
            super(context, android.R.layout.simple_list_item_2, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView text1 = view.findViewById(android.R.id.text1);
            TextView text2 = view.findViewById(android.R.id.text2);
            Mon m = getItem(position);
            text1.setText(m.tenmon);
            text2.setText("Giá: " + dinhDangTien(m.gia) + " - Danh mục: " + m.tendm);
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanlymon);

        //Bước 2: Ánh xạ biến giao diện
        edittenmon = findViewById(R.id.edittenmon);
        editmota = findViewById(R.id.editmota);
        editgia = findViewById(R.id.editgia);
        spdanhmuc = findViewById(R.id.spdanhmuc);
        btnthem = findViewById(R.id.btnthem);
        btnsua = findViewById(R.id.btnsua);
        btnxoa = findViewById(R.id.btnxoa);
        btnquaylai = findViewById(R.id.btnquaylai);
        lvdanhsach = findViewById(R.id.lvdanhsach);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Khởi tạo Spinner danh mục
        listDanhMuc = new ArrayList<>();
        adapterDanhMuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDanhMuc);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdanhmuc.setAdapter(adapterDanhMuc);

        // Khởi tạo ListView món
        listMon = new ArrayList<>();
        adapterMon = new MonAdapter(this, listMon);
        lvdanhsach.setAdapter(adapterMon);

        loadDanhMuc();
        loadMon();

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edittenmon.getText().toString().trim();
                String mota = editmota.getText().toString().trim();
                String giastr = editgia.getText().toString().trim();
                
                if (ten.isEmpty() || giastr.isEmpty()) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng nhập đủ tên và giá món", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spdanhmuc.getSelectedItem() == null) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng tạo danh mục trước khi thêm món", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                int gia;
                try {
                    gia = Integer.parseInt(giastr);
                } catch (NumberFormatException e) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gia < 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món không được âm", Toast.LENGTH_SHORT).show();
                    return;
                }

                int madm = ((DanhMuc) spdanhmuc.getSelectedItem()).id;
                
                ContentValues cv = new ContentValues();
                cv.put("tenmon", ten);
                cv.put("mota", mota);
                cv.put("gia", gia);
                cv.put("madm", madm);
                mydatabase.insert("tblmon", null, cv);

                Toast.makeText(QuanLyMonActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon();
            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn món để sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ten = edittenmon.getText().toString().trim();
                String mota = editmota.getText().toString().trim();
                String giastr = editgia.getText().toString().trim();
                
                if (ten.isEmpty() || giastr.isEmpty()) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng nhập đủ tên và giá", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                int gia;
                try {
                    gia = Integer.parseInt(giastr);
                } catch (NumberFormatException e) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gia < 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món không được âm", Toast.LENGTH_SHORT).show();
                    return;
                }

                int madm = ((DanhMuc) spdanhmuc.getSelectedItem()).id;
                
                ContentValues cv = new ContentValues();
                cv.put("tenmon", ten);
                cv.put("mota", mota);
                cv.put("gia", gia);
                cv.put("madm", madm);
                mydatabase.update("tblmon", cv, "id = ?", new String[]{String.valueOf(selectedId)});

                Toast.makeText(QuanLyMonActivity.this, "Cập nhật món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon();
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn món để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }
                mydatabase.delete("tblmon", "id = ?", new String[]{String.valueOf(selectedId)});
                Toast.makeText(QuanLyMonActivity.this, "Xóa món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon();
            }
        });

        lvdanhsach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mon m = listMon.get(position);
                selectedId = m.id;
                edittenmon.setText(m.tenmon);
                editmota.setText(m.mota);
                editgia.setText(String.valueOf(m.gia));
                
                // Chọn lại giá trị cho spinner
                for (int i = 0; i < listDanhMuc.size(); i++) {
                    if (listDanhMuc.get(i).id == m.madm) {
                        spdanhmuc.setSelection(i);
                        break;
                    }
                }
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

    private void resetForm() {
        edittenmon.setText("");
        editmota.setText("");
        editgia.setText("");
        selectedId = -1;
    }

    private void loadDanhMuc() {
        listDanhMuc.clear();
        Cursor c = mydatabase.rawQuery("SELECT * FROM tbldanhmuc", null);
        while (c.moveToNext()) {
            listDanhMuc.add(new DanhMuc(c.getInt(0), c.getString(1)));
        }
        c.close();
        adapterDanhMuc.notifyDataSetChanged();
    }

    private void loadMon() {
        listMon.clear();
        // Lấy thông tin món kèm theo tên danh mục thông qua LEFT JOIN
        Cursor c = mydatabase.rawQuery("SELECT tblmon.*, tbldanhmuc.tendm FROM tblmon LEFT JOIN tbldanhmuc ON tblmon.madm = tbldanhmuc.id", null);
        while (c.moveToNext()) {
            listMon.add(new Mon(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5)));
        }
        c.close();
        adapterMon.notifyDataSetChanged();
    }

    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}
