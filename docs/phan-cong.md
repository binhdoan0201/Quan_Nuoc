# Phân Công Công Việc Nhóm - Dự Án QuanNuoc (Bản 5 Thành Viên)

**Đề tài:** Xây dựng ứng dụng đặt món quán nước / căng tin trên nền tảng Android (project `QuanNuoc`)  
**Repo:** [https://github.com/binhdoan0201/Quan_Nuoc](https://github.com/binhdoan0201/Quan_Nuoc)  
**Nhóm 5 thành viên:** Thiện, Nam, Hưng, Quyn, Bình  
**Thời hạn hoàn thành:** 19:00 Thứ Sáu, 06/11/2026  

---

## 1. Bảng phân công thành viên

| Thành viên | Vai trò | Activity và Layout phụ trách | Bảng thao tác | Nhánh Git |
|---|---|---|---|---|
| **Thiện** | Nền tảng và tài khoản | `MainActivity`, `DangKyActivity`, `AdminActivity`<br>(layout: `activity_main`, `activity_dangky`, `activity_admin`) | `tbluser`, tạo toàn bộ bảng bằng `taoBang()`, nạp dữ liệu mẫu ban đầu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity`<br>(layout: `activity_menu`, `activity_chitietmon`) | `tbldanhmuc` (đọc), `tblmon` (đọc), `tblgiohang` (ghi) | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity`<br>(layout: `activity_giohang`, `activity_lichsu`) | `tblgiohang`, `tbldonhang` (ghi), `tblchitiet` (ghi), đọc đơn | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity`<br>(layout: `activity_quanlymon`, `activity_quanlydanhmuc`, `activity_quanlydon`) | `tblmon`, `tbldanhmuc`, `tbldonhang` (xử lý đơn) | `tv4-quyn-admin` |
| **Bình** | Quản lý repo, gộp nhánh, kiểm thử, báo cáo | Quản lý repo, duyệt Pull Request, gộp nhánh vào `main`, kiểm thử tổng thể, viết báo cáo, làm slide, kịch bản demo | Quản lý file dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`) | `main` (và nhánh `bc-baocao`) |

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

## 3. Quy ước nhánh Git & Quy tắc làm việc nhóm

1. **Nhánh Git**:
   - `main`: Nhánh gốc, chỉ chứa mã nguồn hoàn chỉnh đã qua kiểm thử.
   - `tv1-thien-taikhoan`: Nhánh của Thiện.
   - `tv2-nam-menu`: Nhánh của Nam.
   - `tv3-hung-giohang`: Nhánh của Hưng.
   - `tv4-quyn-admin`: Nhánh của Quyn.
   - `bc-baocao`: Nhánh soạn thảo tài liệu, báo cáo của Bình.
2. **Quy tắc làm việc**:
   - Mỗi thành viên làm việc độc lập trên nhánh của mình, không commit thẳng vào `main`.
   - File dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`) do **Bình** quản lý.
   - Khi hoàn thành tính năng, tạo Pull Request cho Bình duyệt và gộp theo thứ tự: **Thiện ➔ Quyn ➔ Nam ➔ Hưng**.
