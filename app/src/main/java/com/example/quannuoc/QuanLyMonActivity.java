package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Context;
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
    LinearLayout llheader, llform;
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

    // Adapter tuỳ chỉnh: mỗi món là một thẻ có ảnh
    class MonAdapter extends ArrayAdapter<Mon> {
        public MonAdapter(Context context, List<Mon> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Mon m = getItem(position);
            LinearLayout the = new LinearLayout(QuanLyMonActivity.this);
            the.setOrientation(LinearLayout.HORIZONTAL);
            the.setGravity(Gravity.CENTER_VERTICAL);
            the.setPadding(dp(10), dp(10), dp(10), dp(10));
            // Món đang được chọn thì viền màu san hô
            if (m.id == selectedId) {
                the.setBackground(taoNen(Color.parseColor("#FFF1E6"), 16, "#FC6076"));
            } else {
                the.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));
            }

            ImageView img = new ImageView(QuanLyMonActivity.this);
            img.setLayoutParams(new LinearLayout.LayoutParams(dp(56), dp(56)));
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setBackground(taoNen(Color.parseColor("#F1E9E0"), 12, null));
            img.setClipToOutline(true);
            int maanh = getResources().getIdentifier(m.hinhanh, "drawable", getPackageName());
            if (maanh != 0) {
                img.setImageResource(maanh);
            } else {
                img.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            the.addView(img);

            LinearLayout cot = new LinearLayout(QuanLyMonActivity.this);
            cot.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams thamsocot = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            thamsocot.setMarginStart(dp(12));
            cot.setLayoutParams(thamsocot);

            TextView txtten = new TextView(QuanLyMonActivity.this);
            txtten.setText(m.tenmon);
            txtten.setTextSize(15);
            txtten.setTypeface(null, Typeface.BOLD);
            txtten.setTextColor(Color.parseColor("#3E2C23"));
            cot.addView(txtten);

            String tenDanhMuc = (m.tendm != null && !m.tendm.isEmpty()) ? m.tendm : "Chưa phân loại";
            TextView txtdm = new TextView(QuanLyMonActivity.this);
            txtdm.setText(tenDanhMuc);
            txtdm.setTextSize(12);
            txtdm.setTextColor(Color.parseColor("#9A8B7E"));
            cot.addView(txtdm);

            TextView txtgia = new TextView(QuanLyMonActivity.this);
            txtgia.setText(dinhDangTien(m.gia));
            txtgia.setTextSize(15);
            txtgia.setTypeface(null, Typeface.BOLD);
            txtgia.setTextColor(Color.parseColor("#FC6076"));
            cot.addView(txtgia);

            the.addView(cot);
            return the;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quanlymon);

        //Bước 2: Ánh xạ biến giao diện
        llheader = findViewById(R.id.llheader);
        llform = findViewById(R.id.llform);
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

        // Tạo nền bo góc bằng code (đồng bộ màu với Menu)
        GradientDrawable nenheader = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FF9A44"), Color.parseColor("#FC6076")});
        nenheader.setCornerRadius(dp(24));
        llheader.setBackground(nenheader);
        llform.setBackground(taoNen(Color.WHITE, 16, "#EFE6DC"));
        edittenmon.setBackground(taoNen(Color.parseColor("#FFF8F3"), 10, "#E3D8CC"));
        editgia.setBackground(taoNen(Color.parseColor("#FFF8F3"), 10, "#E3D8CC"));
        editmota.setBackground(taoNen(Color.parseColor("#FFF8F3"), 10, "#E3D8CC"));
        imgxemtruoc.setBackground(taoNen(Color.parseColor("#F1E9E0"), 10, null));
        imgxemtruoc.setClipToOutline(true);

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
                if (gia <= 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món phải lớn hơn 0", Toast.LENGTH_SHORT).show();
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
                if (gia <= 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Giá món phải lớn hơn 0", Toast.LENGTH_SHORT).show();
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

                // Kiểm tra xem món đã từng được đặt trong đơn hàng chưa (để tránh làm rách dữ liệu chi tiết đơn)
                Cursor cChiTiet = mydatabase.rawQuery("SELECT COUNT(*) FROM tblchitiet WHERE mamon = ?", new String[]{String.valueOf(selectedId)});
                int countChiTiet = 0;
                if (cChiTiet != null) {
                    if (cChiTiet.moveToFirst()) {
                        countChiTiet = cChiTiet.getInt(0);
                    }
                    cChiTiet.close();
                }
                if (countChiTiet > 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Món đã có trong lịch sử đơn hàng, không thể xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem món có đang nằm trong giỏ hàng của khách nào không
                Cursor cGio = mydatabase.rawQuery("SELECT COUNT(*) FROM tblgiohang WHERE mamon = ?", new String[]{String.valueOf(selectedId)});
                int countGio = 0;
                if (cGio != null) {
                    if (cGio.moveToFirst()) {
                        countGio = cGio.getInt(0);
                    }
                    cGio.close();
                }
                if (countGio > 0) {
                    Toast.makeText(QuanLyMonActivity.this, "Món đang có trong giỏ hàng của khách, không thể xóa", Toast.LENGTH_SHORT).show();
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

                // Vẽ lại danh sách để tô viền món đang chọn
                adapterMon.notifyDataSetChanged();
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