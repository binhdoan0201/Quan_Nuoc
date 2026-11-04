-- ==============================================================
-- CƠ SỞ DỮ LIỆU: QUÁN NƯỚC (quannuoc.db)
-- HỆ QUẢN TRỊ CSDL: SQLite
-- ==============================================================

PRAGMA foreign_keys = ON;

-- ==============================================================
-- 1. BẢNG TÀI KHOẢN (taikhoan)
-- ==============================================================
CREATE TABLE IF NOT EXISTS taikhoan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tendangnhap TEXT NOT NULL UNIQUE,
    matkhau TEXT NOT NULL,
    hoten TEXT NOT NULL,
    sdt TEXT,
    email TEXT,
    diachi TEXT,
    quyen TEXT NOT NULL DEFAULT 'user' -- 'admin' hoặc 'user'
);

-- ==============================================================
-- 2. BẢNG DANH MỤC (danhmuc)
-- ==============================================================
CREATE TABLE IF NOT EXISTS danhmuc (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tendanhmuc TEXT NOT NULL,
    mota TEXT,
    hinhanh TEXT
);

-- ==============================================================
-- 3. BẢNG MÓN NƯỚC (mon)
-- ==============================================================
CREATE TABLE IF NOT EXISTS mon (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tenmon TEXT NOT NULL,
    giatien REAL NOT NULL,
    mota TEXT,
    hinhanh TEXT,
    iddanhmuc INTEGER NOT NULL,
    trangthai INTEGER DEFAULT 1, -- 1: Còn bán, 0: Hết hàng / Ngừng bán
    FOREIGN KEY (iddanhmuc) REFERENCES danhmuc(id) ON DELETE CASCADE
);

-- ==============================================================
-- 4. BẢNG ĐƠN HÀNG (donhang)
-- ==============================================================
CREATE TABLE IF NOT EXISTS donhang (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idtaikhoan INTEGER NOT NULL,
    ngaydat TEXT NOT NULL,
    tongtien REAL NOT NULL,
    diachigiaohang TEXT,
    sdtnguoinhan TEXT,
    ghichu TEXT,
    trangthai TEXT NOT NULL DEFAULT 'Chờ xác nhận', -- 'Chờ xác nhận', 'Đang pha chế', 'Đang giao', 'Hoàn thành', 'Đã hủy'
    phuongthuctt TEXT DEFAULT 'Tiền mặt',          -- 'Tiền mặt', 'Chuyển khoản QR', 'Ví điện tử'
    FOREIGN KEY (idtaikhoan) REFERENCES taikhoan(id) ON DELETE CASCADE
);

-- ==============================================================
-- 5. BẢNG CHI TIẾT ĐƠN HÀNG (chitietdonhang)
-- ==============================================================
CREATE TABLE IF NOT EXISTS chitietdonhang (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    iddonhang INTEGER NOT NULL,
    idmon INTEGER NOT NULL,
    tenmon TEXT NOT NULL,
    soluong INTEGER NOT NULL,
    dongia REAL NOT NULL,
    thanhtien REAL NOT NULL,
    ghichumon TEXT,
    FOREIGN KEY (iddonhang) REFERENCES donhang(id) ON DELETE CASCADE,
    FOREIGN KEY (idmon) REFERENCES mon(id) ON DELETE CASCADE
);

-- ==============================================================
-- 6. BẢNG GIỎ HÀNG (giohang)
-- ==============================================================
CREATE TABLE IF NOT EXISTS giohang (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idtaikhoan INTEGER NOT NULL,
    idmon INTEGER NOT NULL,
    soluong INTEGER NOT NULL DEFAULT 1,
    ghichu TEXT,
    FOREIGN KEY (idtaikhoan) REFERENCES taikhoan(id) ON DELETE CASCADE,
    FOREIGN KEY (idmon) REFERENCES mon(id) ON DELETE CASCADE
);


-- ==============================================================
-- DỮ LIỆU MẪU (SAMPLE DATA)
-- ==============================================================

-- 1. Dữ liệu bảng taikhoan
INSERT INTO taikhoan (tendangnhap, matkhau, hoten, sdt, email, diachi, quyen) VALUES
('admin', 'admin123', 'Quản Trị Viên', '0901234567', 'admin@quannuoc.vn', '123 Lê Duẩn, Q.1, TP.HCM', 'admin'),
('nhanvien', '123456', 'Lê Hoàng Long', '0933445566', 'long.le@quannuoc.vn', '12 Võ Văn Tần, Q.3, TP.HCM', 'admin'),
('khachhang1', '123456', 'Nguyễn Văn An', '0912345678', 'an.nguyen@gmail.com', '45 Lê Lợi, Q.1, TP.HCM', 'user'),
('khachhang2', '123456', 'Trần Thị Mai', '0987654321', 'mai.tran@gmail.com', '78 Cách Mạng Tháng 8, Q.3, TP.HCM', 'user');

-- 2. Dữ liệu bảng danhmuc
INSERT INTO danhmuc (id, tendanhmuc, mota, hinhanh) VALUES
(1, 'Cà phê', 'Cà phê Robusta & Arabica truyền thống và pha máy thơm nồng đậm vị', 'ic_cafe'),
(2, 'Trà sữa', 'Trà sữa thơm béo, đậm đà vị trà hòa cùng topping đa dạng', 'ic_trasua'),
(3, 'Trà trái cây', 'Trà hoa quả tươi mát, thanh lọc giải nhiệt tự nhiên', 'ic_tratraicay'),
(4, 'Đá xay & Frappe', 'Thức uống đá xay mát lạnh, béo ngậy phủ lớp kem tươi whipping', 'ic_daxay'),
(5, 'Nước ép & Sinh tố', 'Nước ép nguyên chất và sinh tố trái cây nhiệt đới bổ dưỡng', 'ic_sinhto'),
(6, 'Bánh ngọt & Ăn vặt', 'Bánh nướng tươi ngon mỗi ngày và các món nhâm nhi hấp dẫn', 'ic_banhngot');

-- 3. Dữ liệu bảng mon
INSERT INTO mon (tenmon, giatien, mota, hinhanh, iddanhmuc, trangthai) VALUES
-- Cà phê (iddanhmuc = 1)
('Cà phê đen đá', 20000, 'Cà phê Robusta Đắk Lắk nguyên chất, đậm vị đắng thanh', 'cafe_den', 1, 1),
('Cà phê sữa đá', 25000, 'Hòa quyện tinh tế giữa cà phê phin đậm đà và sữa đặc ngọt béo', 'cafe_sua', 1, 1),
('Bạc xỉu đá', 28000, 'Hương vị ngọt ngào nhiều sữa tươi, phảng phất vị cà phê thơm dịu', 'bac_xiu', 1, 1),
('Cà phê muối', 32000, 'Lớp kem muối béo mặn sánh mịn hòa cùng cà phê phin truyền thống', 'cafe_muoi', 1, 1),
('Espresso', 30000, 'Một shot cà phê đậm đặc chiết xuất từ hạt Arabica Cầu Đất nguyên chất', 'espresso', 1, 1),
('Cappuccino', 40000, 'Espresso hòa cùng lớp bọt sữa nóng dày mịn nghệ thuật', 'cappuccino', 1, 1),

-- Trà sữa (iddanhmuc = 2)
('Trà sữa Trân châu Đường đen', 35000, 'Trà sữa béo ngậy ngọt vừa kết hợp trân châu đường đen dẻo dai', 'ts_tranchauduongden', 2, 1),
('Trà sữa Oolong nướng', 38000, 'Hương trà ô long rang nướng thơm lừng, đậm hậu vị', 'ts_oolongnuong', 2, 1),
('Trà sữa Thái xanh', 32000, 'Trà xanh Thái Lan thanh mát, màu sắc bắt mắt và thơm mùi thảo mộc', 'ts_thaixanh', 2, 1),
('Trà sữa Khoai môn', 36000, 'Vị bùi béo tự nhiên từ khoai môn tươi xay mịn, màu tím pastel', 'ts_khoaimon', 2, 1),

-- Trà trái cây (iddanhmuc = 3)
('Trà đào cam sả', 35000, 'Vị sả thơm nồng ấm, cam tươi chua dịu và miếng đào giòn ngọt', 'tra_daocamsa', 3, 1),
('Trà vải hoa hồng', 38000, 'Hương hoa hồng thơm ngát kết hợp cùng những trái vải ngâm mọng nước', 'tra_vai', 3, 1),
('Trà ổi hồng thanh long', 38000, 'Sắc hồng quyến rũ, vị chua ngọt giải nhiệt tức thì trong ngày hè', 'tra_oihong', 3, 1),
('Trà dâu tằm tuyết nhĩ', 40000, 'Dâu tằm dầm thanh mát kết hợp nấm tuyết giòn sần sật vui miệng', 'tra_dautam', 3, 1),

-- Đá xay & Frappe (iddanhmuc = 4)
('Matcha đá xay kem cheese', 45000, 'Matcha Nhật Bản xay mịn cùng sữa, phủ kem phô mai béo mặn', 'matcha_daxay', 4, 1),
('Socola đá xay hạnh nhân', 45000, 'Socola đen nguyên chất xay nhuyễn, rắc lát hạnh nhân nướng bùi giòn', 'choco_daxay', 4, 1),
('Cookie đá xay béo ngậy', 42000, 'Bánh Oreo giòn rụm xay cùng sữa tươi, ngập tràn kem bông tuyết', 'cookie_daxay', 4, 1),

-- Nước ép & Sinh tố (iddanhmuc = 5)
('Nước cam vắt nguyên chất', 30000, '100% cam sành tươi mọng nước, giàu vitamin C tăng cường đề kháng', 'ep_cam', 5, 1),
('Nước ép dưa hấu mát lạnh', 28000, 'Dưa hấu đỏ tươi ngọt thanh tự nhiên, giải khát sảng khoái', 'ep_duahau', 5, 1),
('Sinh tố bơ sáp béo ngậy', 42000, 'Bơ sáp Đắk Lắk dẻo đặc hòa cùng sữa đặc sánh mịn', 'sinhto_bo', 5, 1),
('Sinh tố xoài cát chuối', 38000, 'Xoài cát chín vàng ngọt lịm xay cùng chuối tươi thơm phức', 'sinhto_xoai', 5, 1),

-- Bánh ngọt & Ăn vặt (iddanhmuc = 6)
('Bánh Tiramisu Ý', 35000, 'Bánh phô mai Mascarpone mềm tan thơm hương cà phê và cacao cao cấp', 'banh_tiramisu', 6, 1),
('Bánh Mousse chanh dây', 32000, 'Lớp bánh mousse chua ngọt thanh nhẹ, mềm mại mát lịm đầu lưỡi', 'banh_mousse', 6, 1),
('Croissant bơ tỏi nướng giòn', 25000, 'Bánh sừng bò nướng ngàn lớp thơm ngậy hương sốt bơ tỏi', 'croissant', 6, 1),
('Hướng dương vị dừa thơm bùi', 15000, 'Hạt hướng dương rang củi ngấm vị dừa thơm béo, món nhâm nhi chuẩn bài', 'huongduong', 6, 1);

-- 4. Dữ liệu bảng donhang
INSERT INTO donhang (id, idtaikhoan, ngaydat, tongtien, diachigiaohang, sdtnguoinhan, ghichu, trangthai, phuongthuctt) VALUES
(1, 3, '2026-11-01 08:30:00', 60000, '45 Lê Lợi, Q.1, TP.HCM', '0912345678', 'Giao trước 9h sáng', 'Hoàn thành', 'Tiền mặt'),
(2, 3, '2026-11-04 10:15:00', 73000, '45 Lê Lợi, Q.1, TP.HCM', '0912345678', 'Giao nhanh giúp mình', 'Đang pha chế', 'Chuyển khoản QR'),
(3, 4, '2026-11-04 11:45:00', 87000, '78 Cách Mạng Tháng 8, Q.3, TP.HCM', '0987654321', 'Xin thêm 2 ống hút giấy', 'Chờ xác nhận', 'Tiền mặt'),
(4, 4, '2026-10-28 14:20:00', 30000, '78 Cách Mạng Tháng 8, Q.3, TP.HCM', '0987654321', 'Khách bận đột xuất nên hủy đơn', 'Đã hủy', 'Tiền mặt');

-- 5. Dữ liệu bảng chitietdonhang
INSERT INTO chitietdonhang (iddonhang, idmon, tenmon, soluong, dongia, thanhtien, ghichumon) VALUES
(1, 2, 'Cà phê sữa đá', 1, 25000, 25000, 'Ít ngọt'),
(1, 11, 'Trà đào cam sả', 1, 35000, 35000, 'Nhiều đá'),
(2, 7, 'Trà sữa Trân châu Đường đen', 1, 35000, 35000, '70% đường, 100% đá'),
(2, 8, 'Trà sữa Oolong nướng', 1, 38000, 38000, '50% đường, 50% đá'),
(3, 15, 'Matcha đá xay kem cheese', 1, 45000, 45000, 'Kem cheese để riêng'),
(3, 20, 'Sinh tố bơ sáp béo ngậy', 1, 42000, 42000, 'Ít sữa đặc'),
(4, 18, 'Nước cam vắt nguyên chất', 1, 30000, 30000, 'Không đường');

-- 6. Dữ liệu bảng giohang
INSERT INTO giohang (idtaikhoan, idmon, soluong, ghichu) VALUES
(3, 4, 1, 'Ít đá, ít ngọt'),
(3, 22, 1, 'Lấy muỗng nhỏ');

