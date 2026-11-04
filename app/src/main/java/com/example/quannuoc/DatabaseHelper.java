package com.example.quannuoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quannuoc.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        taoToanBoBang(db);
        themDuLieuMau(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        datLaiDuLieu(db);
    }

    /**
     * Tạo toàn bộ các bảng trong cơ sở dữ liệu nếu chưa tồn tại
     */
    public static void taoToanBoBang(SQLiteDatabase db) {
        // Bật hỗ trợ khóa ngoại
        db.execSQL("PRAGMA foreign_keys = ON;");

        // 1. Bảng taikhoan
        String sqlTaiKhoan = "CREATE TABLE IF NOT EXISTS taikhoan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tendangnhap TEXT NOT NULL UNIQUE, " +
                "matkhau TEXT NOT NULL, " +
                "hoten TEXT NOT NULL, " +
                "sdt TEXT, " +
                "email TEXT, " +
                "diachi TEXT, " +
                "quyen TEXT NOT NULL DEFAULT 'user');";
        db.execSQL(sqlTaiKhoan);

        // 2. Bảng danhmuc
        String sqlDanhMuc = "CREATE TABLE IF NOT EXISTS danhmuc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tendanhmuc TEXT NOT NULL, " +
                "mota TEXT, " +
                "hinhanh TEXT);";
        db.execSQL(sqlDanhMuc);

        // 3. Bảng mon
        String sqlMon = "CREATE TABLE IF NOT EXISTS mon (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenmon TEXT NOT NULL, " +
                "giatien REAL NOT NULL, " +
                "mota TEXT, " +
                "hinhanh TEXT, " +
                "iddanhmuc INTEGER NOT NULL, " +
                "trangthai INTEGER DEFAULT 1, " +
                "FOREIGN KEY (iddanhmuc) REFERENCES danhmuc(id) ON DELETE CASCADE);";
        db.execSQL(sqlMon);

        // 4. Bảng donhang
        String sqlDonHang = "CREATE TABLE IF NOT EXISTS donhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idtaikhoan INTEGER NOT NULL, " +
                "ngaydat TEXT NOT NULL, " +
                "tongtien REAL NOT NULL, " +
                "diachigiaohang TEXT, " +
                "sdtnguoinhan TEXT, " +
                "ghichu TEXT, " +
                "trangthai TEXT NOT NULL DEFAULT 'Chờ xác nhận', " +
                "phuongthuctt TEXT DEFAULT 'Tiền mặt', " +
                "FOREIGN KEY (idtaikhoan) REFERENCES taikhoan(id) ON DELETE CASCADE);";
        db.execSQL(sqlDonHang);

        // 5. Bảng chitietdonhang
        String sqlChiTietDonHang = "CREATE TABLE IF NOT EXISTS chitietdonhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "iddonhang INTEGER NOT NULL, " +
                "idmon INTEGER NOT NULL, " +
                "tenmon TEXT NOT NULL, " +
                "soluong INTEGER NOT NULL, " +
                "dongia REAL NOT NULL, " +
                "thanhtien REAL NOT NULL, " +
                "ghichumon TEXT, " +
                "FOREIGN KEY (iddonhang) REFERENCES donhang(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (idmon) REFERENCES mon(id) ON DELETE CASCADE);";
        db.execSQL(sqlChiTietDonHang);

        // 6. Bảng giohang
        String sqlGioHang = "CREATE TABLE IF NOT EXISTS giohang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idtaikhoan INTEGER NOT NULL, " +
                "idmon INTEGER NOT NULL, " +
                "soluong INTEGER NOT NULL DEFAULT 1, " +
                "ghichu TEXT, " +
                "FOREIGN KEY (idtaikhoan) REFERENCES taikhoan(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (idmon) REFERENCES mon(id) ON DELETE CASCADE);";
        db.execSQL(sqlGioHang);

        Log.d(TAG, "Đã khởi tạo toàn bộ bảng thành công!");
    }

    /**
     * Thêm dữ liệu mẫu vào cơ sở dữ liệu nếu các bảng đang rỗng
     */
    public static void themDuLieuMau(SQLiteDatabase db) {
        themTaiKhoanMau(db);
        themDanhMucMau(db);
        themMonMau(db);
        themDonHangMau(db);
        themGioHangMau(db);
    }

    /**
     * Gọi hàm này để đảm bảo toàn bộ bảng và dữ liệu mẫu được khởi tạo đầy đủ
     */
    public static void khoiTaoDuLieu(SQLiteDatabase db) {
        taoToanBoBang(db);
        themDuLieuMau(db);
    }

    /**
     * Xóa toàn bộ bảng và nạp lại cấu trúc + dữ liệu ban đầu
     */
    public static void datLaiDuLieu(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS giohang");
        db.execSQL("DROP TABLE IF EXISTS chitietdonhang");
        db.execSQL("DROP TABLE IF EXISTS donhang");
        db.execSQL("DROP TABLE IF EXISTS mon");
        db.execSQL("DROP TABLE IF EXISTS danhmuc");
        db.execSQL("DROP TABLE IF EXISTS taikhoan");
        taoToanBoBang(db);
        themDuLieuMau(db);
    }

    private static boolean isBangRong(SQLiteDatabase db, String tenBang) {
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tenBang, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0) == 0;
            }
        } catch (Exception e) {
            Log.e(TAG, "Lỗi kiểm tra bảng: " + tenBang, e);
        }
        return true;
    }

    // --- DỮ LIỆU MẪU: TÀI KHOẢN ---
    private static void themTaiKhoanMau(SQLiteDatabase db) {
        if (!isBangRong(db, "taikhoan")) return;

        chenTaiKhoan(db, "admin", "admin123", "Quản Trị Viên", "0901234567", "admin@quannuoc.vn", "123 Lê Duẩn, Q.1, TP.HCM", "admin");
        chenTaiKhoan(db, "nhanvien", "123456", "Lê Hoàng Long", "0933445566", "long.le@quannuoc.vn", "12 Võ Văn Tần, Q.3, TP.HCM", "admin");
        chenTaiKhoan(db, "khachhang1", "123456", "Nguyễn Văn An", "0912345678", "an.nguyen@gmail.com", "45 Lê Lợi, Q.1, TP.HCM", "user");
        chenTaiKhoan(db, "khachhang2", "123456", "Trần Thị Mai", "0987654321", "mai.tran@gmail.com", "78 Cách Mạng Tháng 8, Q.3, TP.HCM", "user");
    }

    private static void chenTaiKhoan(SQLiteDatabase db, String user, String pass, String ten, String sdt, String email, String diaChi, String quyen) {
        ContentValues values = new ContentValues();
        values.put("tendangnhap", user);
        values.put("matkhau", pass);
        values.put("hoten", ten);
        values.put("sdt", sdt);
        values.put("email", email);
        values.put("diachi", diaChi);
        values.put("quyen", quyen);
        db.insert("taikhoan", null, values);
    }

    // --- DỮ LIỆU MẪU: DANH MỤC ---
    private static void themDanhMucMau(SQLiteDatabase db) {
        if (!isBangRong(db, "danhmuc")) return;

        chenDanhMuc(db, 1, "Cà phê", "Cà phê Robusta & Arabica truyền thống và pha máy thơm nồng đậm vị", "ic_cafe");
        chenDanhMuc(db, 2, "Trà sữa", "Trà sữa thơm béo, đậm đà vị trà hòa cùng topping đa dạng", "ic_trasua");
        chenDanhMuc(db, 3, "Trà trái cây", "Trà hoa quả tươi mát, thanh lọc giải nhiệt tự nhiên", "ic_tratraicay");
        chenDanhMuc(db, 4, "Đá xay & Frappe", "Thức uống đá xay mát lạnh, béo ngậy phủ lớp kem tươi whipping", "ic_daxay");
        chenDanhMuc(db, 5, "Nước ép & Sinh tố", "Nước ép nguyên chất và sinh tố trái cây nhiệt đới bổ dưỡng", "ic_sinhto");
        chenDanhMuc(db, 6, "Bánh ngọt & Ăn vặt", "Bánh nướng tươi ngon mỗi ngày và các món nhâm nhi hấp dẫn", "ic_banhngot");
    }

    private static void chenDanhMuc(SQLiteDatabase db, int id, String ten, String mota, String hinh) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("tendanhmuc", ten);
        values.put("mota", mota);
        values.put("hinhanh", hinh);
        db.insert("danhmuc", null, values);
    }

    // --- DỮ LIỆU MẪU: MÓN NƯỚC ---
    private static void themMonMau(SQLiteDatabase db) {
        if (!isBangRong(db, "mon")) return;

        // 1. Cà phê (idDanhMuc = 1)
        chenMon(db, "Cà phê đen đá", 20000, "Cà phê Robusta Đắk Lắk nguyên chất, đậm vị đắng thanh", "cafe_den", 1, 1);
        chenMon(db, "Cà phê sữa đá", 25000, "Hòa quyện tinh tế giữa cà phê phin đậm đà và sữa đặc ngọt béo", "cafe_sua", 1, 1);
        chenMon(db, "Bạc xỉu đá", 28000, "Hương vị ngọt ngào nhiều sữa tươi, phảng phất vị cà phê thơm dịu", "bac_xiu", 1, 1);
        chenMon(db, "Cà phê muối", 32000, "Lớp kem muối béo mặn sánh mịn hòa cùng cà phê pha phin truyền thống", "cafe_muoi", 1, 1);
        chenMon(db, "Espresso", 30000, "Một shot cà phê đậm đặc chiết xuất từ hạt Arabica Cầu Đất nguyên chất", "espresso", 1, 1);
        chenMon(db, "Cappuccino", 40000, "Espresso hòa cùng lớp bọt sữa nóng dày mịn nghệ thuật", "cappuccino", 1, 1);

        // 2. Trà sữa (idDanhMuc = 2)
        chenMon(db, "Trà sữa Trân châu Đường đen", 35000, "Trà sữa béo ngậy ngọt vừa kết hợp trân châu đường đen dẻo dai", "ts_tranchauduongden", 2, 1);
        chenMon(db, "Trà sữa Oolong nướng", 38000, "Hương trà ô long rang nướng thơm lừng, đậm hậu vị", "ts_oolongnuong", 2, 1);
        chenMon(db, "Trà sữa Thái xanh", 32000, "Trà xanh Thái Lan thanh mát, màu sắc bắt mắt và thơm mùi thảo mộc", "ts_thaixanh", 2, 1);
        chenMon(db, "Trà sữa Khoai môn", 36000, "Vị bùi béo tự nhiên từ khoai môn tươi xay mịn, màu tím pastel", "ts_khoaimon", 2, 1);

        // 3. Trà trái cây (idDanhMuc = 3)
        chenMon(db, "Trà đào cam sả", 35000, "Vị sả thơm nồng ấm, cam tươi chua dịu và miếng đào giòn ngọt", "tra_daocamsa", 3, 1);
        chenMon(db, "Trà vải hoa hồng", 38000, "Hương hoa hồng thơm ngát kết hợp cùng những trái vải ngâm mọng nước", "tra_vai", 3, 1);
        chenMon(db, "Trà ổi hồng thanh long", 38000, "Sắc hồng quyến rũ, vị chua ngọt giải nhiệt tức thì trong ngày hè", "tra_oihong", 3, 1);
        chenMon(db, "Trà dâu tằm tuyết nhĩ", 40000, "Dâu tằm dầm thanh mát kết hợp nấm tuyết giòn sần sật vui miệng", "tra_dautam", 3, 1);

        // 4. Đá xay & Frappe (idDanhMuc = 4)
        chenMon(db, "Matcha đá xay kem cheese", 45000, "Matcha Nhật Bản xay mịn cùng sữa, phủ kem phô mai béo mặn", "matcha_daxay", 4, 1);
        chenMon(db, "Socola đá xay hạnh nhân", 45000, "Socola đen nguyên chất xay nhuyễn, rắc lát hạnh nhân nướng bùi giòn", "choco_daxay", 4, 1);
        chenMon(db, "Cookie đá xay béo ngậy", 42000, "Bánh Oreo giòn rụm xay cùng sữa tươi, ngập tràn kem bông tuyết", "cookie_daxay", 4, 1);

        // 5. Nước ép & Sinh tố (idDanhMuc = 5)
        chenMon(db, "Nước cam vắt nguyên chất", 30000, "100% cam sành tươi mọng nước, giàu vitamin C tăng cường đề kháng", "ep_cam", 5, 1);
        chenMon(db, "Nước ép dưa hấu mát lạnh", 28000, "Dưa hấu đỏ tươi ngọt thanh tự nhiên, giải khát sảng khoái", "ep_duahau", 5, 1);
        chenMon(db, "Sinh tố bơ sáp béo ngậy", 42000, "Bơ sáp Đắk Lắk dẻo đặc hòa cùng sữa đặc sánh mịn", "sinhto_bo", 5, 1);
        chenMon(db, "Sinh tố xoài cát chuối", 38000, "Xoài cát chín vàng ngọt lịm xay cùng chuối tươi thơm phức", "sinhto_xoai", 5, 1);

        // 6. Bánh ngọt & Ăn vặt (idDanhMuc = 6)
        chenMon(db, "Bánh Tiramisu Ý", 35000, "Bánh phô mai Mascarpone mềm tan thơm hương cà phê và cacao cao cấp", "banh_tiramisu", 6, 1);
        chenMon(db, "Bánh Mousse chanh dây", 32000, "Lớp bánh mousse chua ngọt thanh nhẹ, mềm mại mát lịm đầu lưỡi", "banh_mousse", 6, 1);
        chenMon(db, "Croissant bơ tỏi nướng giòn", 25000, "Bánh sừng bò nướng ngàn lớp thơm ngậy hương sốt bơ tỏi", "croissant", 6, 1);
        chenMon(db, "Hướng dương vị dừa thơm bùi", 15000, "Hạt hướng dương rang củi ngấm vị dừa thơm béo, món nhâm nhi chuẩn bài", "huongduong", 6, 1);
    }

    private static void chenMon(SQLiteDatabase db, String ten, double gia, String mota, String hinh, int idDanhMuc, int trangThai) {
        ContentValues values = new ContentValues();
        values.put("tenmon", ten);
        values.put("giatien", gia);
        values.put("mota", mota);
        values.put("hinhanh", hinh);
        values.put("iddanhmuc", idDanhMuc);
        values.put("trangthai", trangThai);
        db.insert("mon", null, values);
    }

    // --- DỮ LIỆU MẪU: ĐƠN HÀNG & CHI TIẾT ĐƠN HÀNG ---
    private static void themDonHangMau(SQLiteDatabase db) {
        if (!isBangRong(db, "donhang")) return;

        // Đơn 1: Hoàn thành
        long idDon1 = chenDonHang(db, 3, "2026-11-01 08:30:00", 60000, "45 Lê Lợi, Q.1, TP.HCM", "0912345678", "Giao trước 9h sáng", "Hoàn thành", "Tiền mặt");
        if (idDon1 != -1) {
            chenChiTietDon(db, (int) idDon1, 2, "Cà phê sữa đá", 1, 25000, 25000, "Ít ngọt");
            chenChiTietDon(db, (int) idDon1, 11, "Trà đào cam sả", 1, 35000, 35000, "Nhiều đá");
        }

        // Đơn 2: Đang pha chế
        long idDon2 = chenDonHang(db, 3, "2026-11-04 10:15:00", 73000, "45 Lê Lợi, Q.1, TP.HCM", "0912345678", "Giao nhanh giúp mình", "Đang pha chế", "Chuyển khoản QR");
        if (idDon2 != -1) {
            chenChiTietDon(db, (int) idDon2, 7, "Trà sữa Trân châu Đường đen", 1, 35000, 35000, "70% đường, 100% đá");
            chenChiTietDon(db, (int) idDon2, 8, "Trà sữa Oolong nướng", 1, 38000, 38000, "50% đường, 50% đá");
        }

        // Đơn 3: Chờ xác nhận
        long idDon3 = chenDonHang(db, 4, "2026-11-04 11:45:00", 87000, "78 Cách Mạng Tháng 8, Q.3, TP.HCM", "0987654321", "Xin thêm 2 ống hút giấy", "Chờ xác nhận", "Tiền mặt");
        if (idDon3 != -1) {
            chenChiTietDon(db, (int) idDon3, 15, "Matcha đá xay kem cheese", 1, 45000, 45000, "Kem cheese để riêng");
            chenChiTietDon(db, (int) idDon3, 20, "Sinh tố bơ sáp béo ngậy", 1, 42000, 42000, "Ít sữa đặc");
        }

        // Đơn 4: Đã hủy
        long idDon4 = chenDonHang(db, 4, "2026-10-28 14:20:00", 30000, "78 Cách Mạng Tháng 8, Q.3, TP.HCM", "0987654321", "Khách bận đột xuất nên hủy đơn", "Đã hủy", "Tiền mặt");
        if (idDon4 != -1) {
            chenChiTietDon(db, (int) idDon4, 18, "Nước cam vắt nguyên chất", 1, 30000, 30000, "Không đường");
        }
    }

    private static long chenDonHang(SQLiteDatabase db, int idTaiKhoan, String ngayDat, double tongTien, String diaChi, String sdt, String ghiChu, String trangThai, String pttt) {
        ContentValues values = new ContentValues();
        values.put("idtaikhoan", idTaiKhoan);
        values.put("ngaydat", ngayDat);
        values.put("tongtien", tongTien);
        values.put("diachigiaohang", diaChi);
        values.put("sdtnguoinhan", sdt);
        values.put("ghichu", ghiChu);
        values.put("trangthai", trangThai);
        values.put("phuongthuctt", pttt);
        return db.insert("donhang", null, values);
    }

    private static void chenChiTietDon(SQLiteDatabase db, int idDonHang, int idMon, String tenMon, int soLuong, double donGia, double thanhTien, String ghiChu) {
        ContentValues values = new ContentValues();
        values.put("iddonhang", idDonHang);
        values.put("idmon", idMon);
        values.put("tenmon", tenMon);
        values.put("soluong", soLuong);
        values.put("dongia", donGia);
        values.put("thanhtien", thanhTien);
        values.put("ghichumon", ghiChu);
        db.insert("chitietdonhang", null, values);
    }

    // --- DỮ LIỆU MẪU: GIỎ HÀNG ---
    private static void themGioHangMau(SQLiteDatabase db) {
        if (!isBangRong(db, "giohang")) return;

        chenGioHang(db, 3, 4, 1, "Ít đá, ít ngọt");       // Cà phê muối
        chenGioHang(db, 3, 22, 1, "Lấy muỗng nhỏ");       // Bánh Tiramisu Ý
    }

    private static void chenGioHang(SQLiteDatabase db, int idTaiKhoan, int idMon, int soLuong, String ghiChu) {
        ContentValues values = new ContentValues();
        values.put("idtaikhoan", idTaiKhoan);
        values.put("idmon", idMon);
        values.put("soluong", soLuong);
        values.put("ghichu", ghiChu);
        db.insert("giohang", null, values);
    }
}

