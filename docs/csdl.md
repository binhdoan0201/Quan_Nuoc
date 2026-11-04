# Tài Liệu Cấu Trúc Cơ Sở Dữ Liệu `quannuoc.db`

Ứng dụng sử dụng cơ sở dữ liệu SQLite cục bộ, được mở/tạo bằng câu lệnh:
```java
mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);
```

---

## 1. Danh sách và cấu trúc các bảng (6 bảng)

### 1.1. Bảng `tbluser` (Người dùng và Quản trị viên)
Lưu trữ thông tin tài khoản đăng nhập của khách hàng và người quản trị.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã định danh tài khoản |
| `username` | TEXT | NOT NULL UNIQUE | Tên đăng nhập |
| `password` | TEXT | NOT NULL | Mật khẩu |
| `fullname` | TEXT | | Họ và tên người dùng |
| `phone` | TEXT | | Số điện thoại liên lạc |
| `role` | INTEGER | DEFAULT 0 | Quyền: `0` = người dùng, `1` = admin |

### 1.2. Bảng `tbldanhmuc` (Danh mục sản phẩm)
Phân loại các loại đồ uống/món ăn (ví dụ: Trà sữa, Cà phê, Ăn vặt).

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã định danh danh mục |
| `tendm` | TEXT | NOT NULL | Tên danh mục |

### 1.3. Bảng `tblmon` (Món ăn / Đồ uống)
Danh sách chi tiết các món được bày bán.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã định danh món |
| `tenmon` | TEXT | NOT NULL | Tên món ăn / thức uống |
| `mota` | TEXT | | Mô tả chi tiết món |
| `gia` | INTEGER | NOT NULL | Đơn giá hiện tại (đồng) |
| `madm` | INTEGER | | Mã danh mục (tham chiếu `tbldanhmuc.id`) |

### 1.4. Bảng `tblgiohang` (Giỏ hàng tạm thời)
Lưu các món người dùng chọn trước khi tiến hành đặt hàng.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã dòng giỏ hàng |
| `userid` | INTEGER | NOT NULL | Mã người dùng (tham chiếu `tbluser.id`) |
| `mamon` | INTEGER | NOT NULL | Mã món chọn (tham chiếu `tblmon.id`) |
| `soluong` | INTEGER | NOT NULL | Số lượng chọn |

### 1.5. Bảng `tbldonhang` (Đơn đặt hàng)
Thông tin tổng quát của từng đơn hàng đã được người dùng gửi.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã đơn hàng |
| `userid` | INTEGER | NOT NULL | Mã người đặt (tham chiếu `tbluser.id`) |
| `ngaydat` | TEXT | NOT NULL | Thời gian đặt hàng (định dạng `dd/MM/yyyy HH:mm`) |
| `tongtien` | INTEGER | NOT NULL | Tổng số tiền thanh toán của đơn hàng (đồng) |
| `trangthai` | INTEGER | DEFAULT 0 | Trạng thái: `0` = chờ, `1` = đang làm, `2` = hoàn thành, `3` = hủy |

### 1.6. Bảng `tblchitiet` (Chi tiết đơn hàng)
Lưu các món cụ thể trong từng đơn hàng cùng giá bán tại thời điểm đặt.

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
|---|---|---|---|
| `id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Mã định danh chi tiết |
| `madon` | INTEGER | NOT NULL | Mã đơn hàng (tham chiếu `tbldonhang.id`) |
| `mamon` | INTEGER | NOT NULL | Mã món (tham chiếu `tblmon.id`) |
| `soluong` | INTEGER | NOT NULL | Số lượng đặt món |
| `gia` | INTEGER | NOT NULL | Đơn giá tại thời điểm đặt hàng (đồng) |

---

## 2. Giá trị quy ước

- **Quyền hạn tài khoản (`tbluser.role`)**:
  - `0`: Người dùng thông thường (khách hàng)
  - `1`: Quản trị viên (admin)
- **Trạng thái đơn hàng (`tbldonhang.trangthai`)**:
  - `0`: Chờ
  - `1`: Đang làm
  - `2`: Hoàn thành
  - `3`: Hủy
- **Tài khoản mẫu**:
  - Tài khoản admin: `admin` / `123` (với `role` = 1)
- **Dữ liệu mẫu khởi tạo**:
  - Tối thiểu 3 danh mục
  - Tối thiểu 15 món

---

## 3. Quy tắc nghiệp vụ đặt hàng

Khi khách hàng bấm đặt hàng từ `GioHangActivity`:
1. Ghi **1 dòng** vào `tbldonhang` chứa thông tin: `userid`, `ngaydat` (`dd/MM/yyyy HH:mm`), `tongtien`, và `trangthai` = 0 (chờ).
2. Lấy `id` của đơn hàng vừa tạo (`madon`).
3. Đọc dữ liệu từ `tblgiohang` của người dùng đó, ghi các dòng tương ứng vào `tblchitiet` (`madon`, `mamon`, `soluong`, `gia` tại thời điểm đặt).
4. Xóa toàn bộ các dòng thuộc `userid` đó trong `tblgiohang`.

---

## 4. Lưu ý quan trọng
> **Mọi thay đổi cấu trúc bảng phải cập nhật file này và báo cả nhóm.**
