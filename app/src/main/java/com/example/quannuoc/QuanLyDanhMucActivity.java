package com.example.quannuoc;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class QuanLyDanhMucActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    LinearLayout llheader, llform;
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
        llheader = findViewById(R.id.llheader);
        llform = findViewById(R.id.llform);
        edittendm = findViewById(R.id.edittendm);
        btnthem = findViewById(R.id.btnthem);
        btnsua = findViewById(R.id.btnsua);
        btnxoa = findViewById(R.id.btnxoa);
        btnquaylai = findViewById(R.id.btnquaylai);
        lvdanhsach = findViewById(R.id.lvdanhsach);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);
        llform.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));
        edittendm.setBackground(taoNen(Color.parseColor("#FFF8F3"), 10, "#E3D8CC"));

        list = new ArrayList<>();
        // Mỗi danh mục là một thẻ bo góc, danh mục đang chọn có viền màu san hô
        adapter = new ArrayAdapter<DanhMuc>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                DanhMuc dm = getItem(position);
                TextView txt = new TextView(QuanLyDanhMucActivity.this);
                txt.setText(dm.tendm);
                txt.setTextSize(16);
                txt.setTypeface(null, Typeface.BOLD);
                txt.setTextColor(Color.parseColor("#3E2C23"));
                txt.setPadding(dp(16), dp(14), dp(16), dp(14));
                if (dm.id == selectedId) {
                    txt.setBackground(taoNen(Color.parseColor("#FFF1E6"), 14, "#FC6076"));
                } else {
                    txt.setBackground(taoNen(Color.WHITE, 14, "#EFE6DC"));
                }
                return txt;
            }
        };
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

                // Kiểm tra xem danh mục có còn món hay không trước khi xóa
                Cursor c = mydatabase.rawQuery("SELECT COUNT(*) FROM tblmon WHERE madm = ?", new String[]{String.valueOf(selectedId)});
                int soMon = 0;
                if (c != null) {
                    if (c.moveToFirst()) {
                        soMon = c.getInt(0);
                    }
                    c.close();
                }

                if (soMon > 0) {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Danh mục còn món, không thể xóa", Toast.LENGTH_SHORT).show();
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
                // Vẽ lại danh sách để tô viền danh mục đang chọn
                adapter.notifyDataSetChanged();
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