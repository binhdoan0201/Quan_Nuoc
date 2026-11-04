# Phân Công Công Việc Nhóm - Dự Án QuanNuoc

Dự án nhóm 4 thành viên: Thiện, Nam, Hưng, Quyn.

---

## 1. Bảng phân công thành viên

| Thành viên | Vai trò | Activity và Layout | Bảng thao tác | Nhánh Git |
|---|---|---|---|---|
| **Thiện** | Nền tảng, tài khoản, trưởng nhóm ghép code | `MainActivity`, `DangKyActivity`, `AdminActivity`<br>(layout: `activity_main`, `activity_dangky`, `activity_admin`) | `tbluser`, tạo toàn bộ bảng bằng `taoBang()`, dữ liệu mẫu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity`<br>(layout: `activity_menu`, `activity_chitietmon`) | `tbldanhmuc` (đọc), `tblmon` (đọc), `tblgiohang` (ghi) | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity`<br>(layout: `activity_giohang`, `activity_lichsu`) | `tblgiohang`, `tbldonhang` (ghi), `tblchitiet` (ghi), đọc đơn | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity`<br>(layout: `activity_quanlymon`, `activity_quanlydanhmuc`, `activity_quanlydon`) | `tblmon`, `tbldanhmuc`, `tbldonhang` (đổi trạng thái) | `tv4-quyn-admin` |

---

## 2. Luồng điều hướng và Bundle truyền nhận

Quy tắc:
- Key trong Bundle trùng tên cột trong CSDL SQLite.
- Tên Bundle trong Intent được đặt là `"goi"`.
  Ví dụ gửi:
  ```java
  Intent intent = new Intent(MainActivity.this, MenuActivity.class);
  Bundle bundle = new Bundle();
  bundle.putInt("userid", id);
  bundle.putInt("role", role);
  intent.putExtra("goi", bundle);
  startActivity(intent);
  ```
  Ví dụ nhận:
  ```java
  Intent intent = getIntent();
  Bundle bundle = intent.getBundleExtra("goi");
  if (bundle != null) {
      int userid = bundle.getInt("userid");
      int role = bundle.getInt("role");
  }
  ```

### Bảng luồng chuyển màn hình:

| Từ | Đến | Bundle gửi đi | Mục đích |
|---|---|---|---|
| `MainActivity` | `DangKyActivity` | *(không có)* | Mở màn hình đăng ký tài khoản mới |
| `MainActivity` | `MenuActivity` | `userid`, `role` | Đăng nhập thành công với quyền người dùng (`role = 0`) |
| `MainActivity` | `AdminActivity` | `userid`, `role` | Đăng nhập thành công với quyền quản trị viên (`role = 1`) |
| `MenuActivity` | `ChiTietMonActivity` | `userid`, `mamon` | Xem thông tin chi tiết một món ăn/đồ uống |
| `MenuActivity` | `GioHangActivity` | `userid` | Xem giỏ hàng của người dùng hiện tại |
| `MenuActivity` | `LichSuActivity` | `userid` | Xem lịch sử các đơn hàng đã đặt |
| `ChiTietMonActivity` | `GioHangActivity` | `userid` | Chuyển tới giỏ hàng sau khi chọn món |
| `AdminActivity` | `QuanLyMonActivity` | `userid`, `role` | Mở trang quản trị danh sách món ăn |
| `AdminActivity` | `QuanLyDanhMucActivity` | `userid`, `role` | Mở trang quản trị danh mục món |
| `AdminActivity` | `QuanLyDonActivity` | `userid`, `role` | Mở trang quản trị danh sách đơn hàng |

---

## 3. Quy ước nhánh Git

Mỗi thành viên làm việc độc lập trên nhánh của mình và tuân thủ quy tắc làm việc nhóm:
- `main`: Nhánh gốc, ổn định.
- `tv1-thien-taikhoan`: Nhánh của Thiện.
- `tv2-nam-menu`: Nhánh của Nam.
- `tv3-hung-giohang`: Nhánh của Hưng.
- `tv4-quyn-admin`: Nhánh của Quyn.
