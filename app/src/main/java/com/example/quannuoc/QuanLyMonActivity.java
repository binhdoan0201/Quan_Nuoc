package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuanLyMonActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    EditText edittenmon, editmota, editgia;
    Spinner spdanhmuc, spanh;
    ImageView imgxemtruoc;
    Button btnthem, btnsua, btnxoa, btnlammoi, btnquaylai;
    ListView lvdanhsach;
    SQLiteDatabase mydatabase;

    ArrayList<DanhMuc> listDanhMuc;
    ArrayAdapter<DanhMuc> adapterDanhMuc;

    ArrayList<HinhAnh> listHinhAnh;
    ArrayAdapter<HinhAnh> adapterHinhAnh;

    ArrayList<Mon> listMon;
    MonAdapter adapterMon;
    int selectedId = -1;
    boolean isUpdatingFromList = false;

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

    // Lớp mô hình ảnh mẫu
    class HinhAnh {
        String ma;
        String ten;

        public HinhAnh(String ma, String ten) {
            this.ma = ma;
            this.ten = ten;
        }

        @Override
        public String toString() {
            return ten;
        }
    }

    // Lớp mô hình món
    class Mon {
        int id, gia, madm;
        String tenmon, mota, tendm, hinhanh;

        public Mon(int id, String tenmon, String mota, int gia, int madm, String tendm, String hinhanh) {
            this.id = id;
            this.tenmon = tenmon;
            this.mota = mota;
            this.gia = gia;
            this.madm = madm;
            this.tendm = tendm;
            this.hinhanh = (hinhanh != null && !hinhanh.isEmpty()) ? hinhanh : ("mon" + id);
        }
    }

    // Adapter tuỳ chỉnh để hiển thị món trên 2 dòng
    class MonAdapter extends ArrayAdapter<Mon> {
        public MonAdapter(Context context, List<Mon> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);
            Mon m = getItem(position);
            if (m != null) {
                text1.setText(m.tenmon);
                text1.setTypeface(null, Typeface.BOLD);
                String tenDanhMuc = (m.tendm != null && !m.tendm.isEmpty()) ? m.tendm : "Chưa phân loại";
                text2.setText("Giá: " + dinhDangTien(m.gia) + "  |  Danh mục: " + tenDanhMuc);
            }
            return convertView;
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
        spanh = findViewById(R.id.spanh);
        imgxemtruoc = findViewById(R.id.imgxemtruoc);
        btnthem = findViewById(R.id.btnthem);
        btnsua = findViewById(R.id.btnsua);
        btnxoa = findViewById(R.id.btnxoa);
        btnlammoi = findViewById(R.id.btnlammoi);
        btnquaylai = findViewById(R.id.btnquaylai);
        lvdanhsach = findViewById(R.id.lvdanhsach);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        // Khởi tạo Spinner danh mục (có mục Tất cả danh mục)
        listDanhMuc = new ArrayList<>();
        adapterDanhMuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listDanhMuc);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdanhmuc.setAdapter(adapterDanhMuc);

        // Khởi tạo danh sách ảnh mẫu từ drawable
        khoiTaoDanhSachAnh();

        // Khởi tạo ListView món
        listMon = new ArrayList<>();
        adapterMon = new MonAdapter(this, listMon);
        lvdanhsach.setAdapter(adapterMon);

        // Nạp danh mục và tất cả món
        loadDanhMuc();
        loadMon(0);

        // Lọc danh sách món khi chọn danh mục trên Spinner
        spdanhmuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUpdatingFromList) {
                    isUpdatingFromList = false;
                    return;
                }
                DanhMuc dm = listDanhMuc.get(position);
                loadMon(dm.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Đổi ảnh xem trước khi người dùng chọn ảnh mẫu trên Spinner
        spanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HinhAnh h = listHinhAnh.get(position);
                int resId = getResources().getIdentifier(h.ma, "drawable", getPackageName());
                if (resId != 0) {
                    imgxemtruoc.setImageResource(resId);
                } else {
                    imgxemtruoc.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Xử lý nút Thêm món
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

                DanhMuc dmChon = (DanhMuc) spdanhmuc.getSelectedItem();
                if (dmChon == null || dmChon.id == 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn một danh mục cụ thể (khác 'Tất cả') để thêm món", Toast.LENGTH_SHORT).show();
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

                HinhAnh haChon = (HinhAnh) spanh.getSelectedItem();
                String tenHinh = (haChon != null) ? haChon.ma : "mon1";

                ContentValues cv = new ContentValues();
                cv.put("tenmon", ten);
                cv.put("mota", mota);
                cv.put("gia", gia);
                cv.put("madm", dmChon.id);
                cv.put("hinhanh", tenHinh);
                mydatabase.insert("tblmon", null, cv);

                Toast.makeText(QuanLyMonActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon(dmChon.id);
            }
        });

        // Xử lý nút Sửa món
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn món trong danh sách để sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ten = edittenmon.getText().toString().trim();
                String mota = editmota.getText().toString().trim();
                String giastr = editgia.getText().toString().trim();

                if (ten.isEmpty() || giastr.isEmpty()) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng nhập đủ tên và giá", Toast.LENGTH_SHORT).show();
                    return;
                }

                DanhMuc dmChon = (DanhMuc) spdanhmuc.getSelectedItem();
                if (dmChon == null || dmChon.id == 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn một danh mục cụ thể cho món", Toast.LENGTH_SHORT).show();
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

                HinhAnh haChon = (HinhAnh) spanh.getSelectedItem();
                String tenHinh = (haChon != null) ? haChon.ma : "mon1";

                ContentValues cv = new ContentValues();
                cv.put("tenmon", ten);
                cv.put("mota", mota);
                cv.put("gia", gia);
                cv.put("madm", dmChon.id);
                cv.put("hinhanh", tenHinh);
                mydatabase.update("tblmon", cv, "id = ?", new String[]{String.valueOf(selectedId)});

                Toast.makeText(QuanLyMonActivity.this, "Cập nhật món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon(dmChon.id);
            }
        });

        // Xử lý nút Xóa món
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedId == -1) {
                    Toast.makeText(QuanLyMonActivity.this, "Vui lòng chọn món trong danh sách để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }
                DanhMuc dmChon = (DanhMuc) spdanhmuc.getSelectedItem();
                int madmHienTai = (dmChon != null) ? dmChon.id : 0;

                mydatabase.delete("tblmon", "id = ?", new String[]{String.valueOf(selectedId)});
                Toast.makeText(QuanLyMonActivity.this, "Xóa món thành công", Toast.LENGTH_SHORT).show();
                resetForm();
                loadMon(madmHienTai);
            }
        });

        // Xử lý nút Làm mới form và hiển thị lại toàn bộ danh sách
        btnlammoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
                spdanhmuc.setSelection(0);
                loadMon(0);
                Toast.makeText(QuanLyMonActivity.this, "Đã làm mới form", Toast.LENGTH_SHORT).show();
            }
        });

        // Chọn món từ danh sách để sửa hoặc xóa
        lvdanhsach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mon m = listMon.get(position);
                selectedId = m.id;
                edittenmon.setText(m.tenmon);
                editmota.setText(m.mota);
                editgia.setText(String.valueOf(m.gia));

                // Bật cờ để tránh việc setSelection tự kích hoạt lọc lại danh sách
                isUpdatingFromList = true;
                for (int i = 0; i < listDanhMuc.size(); i++) {
                    if (listDanhMuc.get(i).id == m.madm) {
                        spdanhmuc.setSelection(i);
                        break;
                    }
                }

                // Chọn ảnh tương ứng trên Spinner ảnh
                for (int j = 0; j < listHinhAnh.size(); j++) {
                    if (listHinhAnh.get(j).ma.equals(m.hinhanh)) {
                        spanh.setSelection(j);
                        break;
                    }
                }
            }
        });

        // Nút quay lại màn hình trước
        btnquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý padding Edge-to-Edge bằng anonymous class (không dùng lambda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });
    }

    private void khoiTaoDanhSachAnh() {
        listHinhAnh = new ArrayList<>();
        listHinhAnh.add(new HinhAnh("mon1", "1. Cà phê đen"));
        listHinhAnh.add(new HinhAnh("mon2", "2. Cà phê sữa"));
        listHinhAnh.add(new HinhAnh("mon3", "3. Bạc xỉu"));
        listHinhAnh.add(new HinhAnh("mon4", "4. Cà phê muối"));
        listHinhAnh.add(new HinhAnh("mon5", "5. Cappuccino"));
        listHinhAnh.add(new HinhAnh("mon6", "6. Trà sữa trân châu"));
        listHinhAnh.add(new HinhAnh("mon7", "7. Trà sữa Oolong"));
        listHinhAnh.add(new HinhAnh("mon8", "8. Trà sữa Thái xanh"));
        listHinhAnh.add(new HinhAnh("mon9", "9. Trà sữa khoai môn"));
        listHinhAnh.add(new HinhAnh("mon10", "10. Trà đào cam sả"));
        listHinhAnh.add(new HinhAnh("mon11", "11. Trà vải hoa hồng"));
        listHinhAnh.add(new HinhAnh("mon12", "12. Trà ổi hồng"));
        listHinhAnh.add(new HinhAnh("mon13", "13. Matcha đá xay"));
        listHinhAnh.add(new HinhAnh("mon14", "14. Socola đá xay"));
        listHinhAnh.add(new HinhAnh("mon15", "15. Cookie đá xay"));
        listHinhAnh.add(new HinhAnh("mon16", "16. Nước cam vắt"));
        listHinhAnh.add(new HinhAnh("mon17", "17. Nước ép dưa hấu"));
        listHinhAnh.add(new HinhAnh("mon18", "18. Sinh tố bơ"));
        listHinhAnh.add(new HinhAnh("mon19", "19. Bánh Tiramisu"));
        listHinhAnh.add(new HinhAnh("mon20", "20. Bánh Croissant"));

        adapterHinhAnh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listHinhAnh);
        adapterHinhAnh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spanh.setAdapter(adapterHinhAnh);
    }

    private void resetForm() {
        edittenmon.setText("");
        editmota.setText("");
        editgia.setText("");
        selectedId = -1;
        if (listHinhAnh != null && !listHinhAnh.isEmpty()) {
            spanh.setSelection(0);
        }
    }

    private void loadDanhMuc() {
        listDanhMuc.clear();
        // Mục đầu tiên là Tất cả để xem toàn bộ thực đơn
        listDanhMuc.add(new DanhMuc(0, "Tất cả danh mục"));
        Cursor c = mydatabase.rawQuery("SELECT id, tendm FROM tbldanhmuc", null);
        while (c.moveToNext()) {
            listDanhMuc.add(new DanhMuc(c.getInt(0), c.getString(1)));
        }
        c.close();
        adapterDanhMuc.notifyDataSetChanged();
    }

    private void loadMon(int madm) {
        listMon.clear();
        Cursor c;
        if (madm == 0) {
            // Lấy tất cả món kèm tên danh mục và tên ảnh thông qua LEFT JOIN
            c = mydatabase.rawQuery("SELECT tblmon.id, tblmon.tenmon, tblmon.mota, tblmon.gia, tblmon.madm, tbldanhmuc.tendm, tblmon.hinhanh FROM tblmon LEFT JOIN tbldanhmuc ON tblmon.madm = tbldanhmuc.id", null);
        } else {
            // Lọc theo danh mục cụ thể
            c = mydatabase.rawQuery("SELECT tblmon.id, tblmon.tenmon, tblmon.mota, tblmon.gia, tblmon.madm, tbldanhmuc.tendm, tblmon.hinhanh FROM tblmon LEFT JOIN tbldanhmuc ON tblmon.madm = tbldanhmuc.id WHERE tblmon.madm = ?", new String[]{String.valueOf(madm)});
        }
        while (c.moveToNext()) {
            listMon.add(new Mon(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4), c.getString(5), c.getString(6)));
        }
        c.close();
        adapterMon.notifyDataSetChanged();
    }

    private String dinhDangTien(int sotien) {
        return String.format("%,d", sotien).replace(",", ".") + " đ";
    }
}
