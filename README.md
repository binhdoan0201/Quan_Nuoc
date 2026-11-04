# QuanNuoc - Ứng Dụng Đặt Món Quán Nước / Căng Tin

Đồ án môn học: **Lập trình ứng dụng di động Android**

---

## 1. Tên đề tài & Mô tả ngắn
Ứng dụng đặt món quán nước / căng tin trường học hỗ trợ người dùng duyệt menu thức uống, tìm kiếm, quản lý giỏ hàng và đặt đơn tiện lợi. Đồng thời cung cấp bảng điều khiển quản trị cho chủ quán để quản lý danh mục, thực đơn và xử lý trạng thái đơn hàng.

## 2. Công nghệ sử dụng
- **Ngôn ngữ lập trình**: Java (thuần Java, không sử dụng Kotlin)
- **Cơ sở dữ liệu**: SQLite cục bộ, mở và thao tác qua `openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null)`
- **Môi trường & Công cụ**: Android Studio, Groovy DSL
- **Giao diện**: View chuẩn Android (`LinearLayout`, `ListView`, `Spinner`, `Button`, `TextView`, `EditText`), không sử dụng Fragment, RecyclerView, ViewBinding hay thư viện ngoài.

## 3. Chức năng chính
- **Khách hàng (Người dùng)**:
  - Đăng ký và đăng nhập tài khoản.
  - Xem danh mục (Spinner), danh sách món ăn/uống (ListView), tìm kiếm món theo tên.
  - Xem thông tin chi tiết món, chọn số lượng và thêm vào giỏ hàng.
  - Xem giỏ hàng, cập nhật số lượng, xóa món bằng nhấn giữ (long-click), xác nhận đặt hàng.
  - Xem lịch sử đơn hàng và theo dõi trạng thái đơn.
- **Quản trị viên (Admin)**:
  - Đăng nhập phân quyền admin.
  - Quản lý danh mục: Thêm, sửa, xóa, xem danh mục.
  - Quản lý món: Thêm món mới, cập nhật giá, xóa món.
  - Quản trị đơn hàng: Theo dõi danh sách đơn đặt, chuyển trạng thái đơn (chờ -> đang làm -> hoàn thành -> hủy).

## 4. Hướng dẫn cài đặt và Chạy ứng dụng
1. Mở thư mục dự án bằng **Android Studio**.
2. Đợi Android Studio đồng bộ xong Gradle (Sync Project with Gradle Files).
3. Chọn máy ảo Android (API 24 trở lên) hoặc cắm thiết bị thật có bật gỡ lỗi USB (USB Debugging).
4. Bấm nút **Run** (Shift + F10) để biên dịch và cài đặt ứng dụng.
> **Lưu ý**: Nếu có thay đổi cấu trúc bảng CSDL SQLite, hãy **gỡ ứng dụng (Uninstall) khỏi máy ảo rồi chạy lại** để bảng được tạo mới từ đầu.

## 5. Tài khoản dùng thử mẫu
- **Tài khoản Quản trị viên (Admin)**:
  - Tên đăng nhập: `admin`
  - Mật khẩu: `123`
  - Vai trò (`role`): `1` (Admin)

## 6. Bảng thành viên và Phân công nhiệm vụ

| Thành viên | Vai trò | Activity & Layout phụ trách | Bảng thao tác | Nhánh Git |
|---|---|---|---|---|
| **Thiện** | Nền tảng, tài khoản, trưởng nhóm ghép code | `MainActivity`, `DangKyActivity`, `AdminActivity`<br>(`activity_main`, `activity_dangky`, `activity_admin`) | `tbluser`, tạo toàn bộ bảng bằng `taoBang()`, dữ liệu mẫu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity`<br>(`activity_menu`, `activity_chitietmon`) | `tbldanhmuc` (đọc), `tblmon` (đọc), `tblgiohang` (ghi) | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity`<br>(`activity_giohang`, `activity_lichsu`) | `tblgiohang`, `tbldonhang` (ghi), `tblchitiet` (ghi), đọc đơn | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity`<br>(`activity_quanlymon`, `activity_quanlydanhmuc`, `activity_quanlydon`) | `tblmon`, `tbldanhmuc`, `tbldonhang` (đổi trạng thái) | `tv4-quyn-admin` |

Xem chi tiết phân công và luồng Bundle tại [docs/phan-cong.md](docs/phan-cong.md), cấu trúc CSDL tại [docs/csdl.md](docs/csdl.md).

## 7. Quy ước nhánh Git và Quy tắc làm việc nhóm

### 7.1. Quy ước nhánh
- `main`: Nhánh chính, code hoàn chỉnh chạy được.
- Các nhánh chức năng cá nhân:
  - `tv1-thien-taikhoan`
  - `tv2-nam-menu`
  - `tv3-hung-giohang`
  - `tv4-quyn-admin`

### 7.2. Quy tắc làm việc nhóm
1. **Phạm vi code**: Mỗi người chỉ sửa file thuộc phần mình phụ trách. Cần sửa file người khác thì trao đổi với người đó.
2. **File dùng chung**: `AndroidManifest.xml`, `strings.xml`, `docs/csdl.md` chỉ **Thiện** sửa.
3. **Quy tắc commit**: Không commit thẳng vào `main`. Làm trên nhánh riêng, xong tạo Pull Request để Thiện duyệt và merge.
4. **Thứ tự gộp vào `main`**: **Thiện, rồi Quyn, rồi Nam, rồi Hưng**.
5. **CSDL mẫu**: Thiện làm `MainActivity` và hàm `taoBang()` đầu tiên, đẩy lên để mọi người có CSDL để test.
6. **Tiêu đề Commit**: Ngắn gọn, có tiền tố tên màn hình, ví dụ: `GioHang: them xoa mon bang long click`.
7. **Thay đổi CSDL / Bundle**: Đổi tên bảng, tên cột hoặc key Bundle phải báo cả nhóm trước và cập nhật lại vào `docs/csdl.md`.
8. **Kiểm tra trước khi nộp**: Xóa import thừa, mã lệnh thử nghiệm không dùng, code chú thích dở dang và các log debug.
