# QuanNuoc - Ứng Dụng Đặt Món Quán Nước / Căng Tin

Đồ án môn học: **Lập trình ứng dụng di động Android**  
**Repository:** [https://github.com/binhdoan0201/Quan_Nuoc](https://github.com/binhdoan0201/Quan_Nuoc)  
**Nhóm sinh viên thực hiện (5 thành viên):** Thiện, Nam, Hưng, Quyn, Bình  
**Thời hạn hoàn thành:** 19:00 Thứ Sáu, 06/11/2026  

---

## 1. Tên đề tài & Mô tả ngắn
Ứng dụng đặt món quán nước / căng tin trường học hỗ trợ khách hàng duyệt menu thức uống, tìm kiếm, quản lý giỏ hàng và đặt đơn tiện lợi. Đồng thời cung cấp bảng điều khiển quản trị cho chủ quán (Admin) để quản lý danh mục, thực đơn (món ăn, đơn giá, ảnh đại diện) và tiếp nhận, xử lý trạng thái đơn hàng theo thời gian thực.

## 2. Công nghệ sử dụng & Ràng buộc kỹ thuật
- **Ngôn ngữ lập trình**: Java (thuần Java 100%, không sử dụng Kotlin).
- **Cơ sở dữ liệu**: SQLite cục bộ, mở và thao tác qua `openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null)`.
- **Môi trường & Công cụ**: Android Studio, Groovy DSL, Git & GitHub.
- **Giao diện**: View chuẩn Android (`LinearLayout`, `ListView`, `Spinner`, `Button`, `TextView`, `EditText`), không sử dụng Fragment, RecyclerView, ViewBinding hay thư viện ngoài.
- **Quy tắc code**: Code chia rõ 3 bước comment (`//Bước 1`, `//Bước 2`, `//Bước 3`), không sử dụng lambda.

## 3. Chức năng chính
- **Khách hàng (Người dùng)**:
  - Đăng ký và đăng nhập tài khoản (tự động điền username sau khi đăng ký thành công).
  - Xem danh mục (Spinner), danh sách món ăn/uống (ListView), tìm kiếm món theo tên.
  - Xem thông tin chi tiết món, hiển thị hình ảnh đại diện, chọn số lượng và thêm vào giỏ hàng.
  - Xem giỏ hàng, cập nhật số lượng, xóa món bằng nhấn giữ (long-click), xác nhận đặt hàng.
  - Xem lịch sử đơn hàng, xem chi tiết món đã đặt và hủy đơn khi còn ở trạng thái "Chờ".
- **Quản trị viên (Admin)**:
  - Đăng nhập phân quyền admin an toàn (`role = 1`).
  - **Quản lý danh mục**: Thêm, sửa, xóa (ràng buộc không xóa khi còn món), xem danh mục.
  - **Quản lý món**: Thêm món mới, cập nhật giá/mô tả, chọn ảnh mẫu hiển thị xem trước (preview), xóa món, lọc món theo danh mục trên form.
  - **Quản lý đơn hàng**: Hiển thị đầy đủ Tên khách hàng & Số điện thoại, xem chi tiết các món cần pha chế, chuyển đổi trạng thái đơn hàng theo luồng chuẩn (State Machine: Chờ xác nhận ➔ Đang làm ➔ Hoàn thành / Hủy).

## 4. Hướng dẫn cài đặt và Chạy ứng dụng
1. Mở thư mục dự án bằng **Android Studio**.
2. Đợi Android Studio đồng bộ xong Gradle (Sync Project with Gradle Files).
3. Chọn máy ảo Android (API 24 trở lên) hoặc cắm thiết bị thật có bật gỡ lỗi USB (USB Debugging).
4. Bấm nút **Run** (Shift + F10) để biên dịch và cài đặt ứng dụng.
> **Lưu ý**: Database đã có cơ chế tự động nâng cấp (Auto-migration). Nếu muốn làm mới toàn bộ dữ liệu mẫu, có thể gỡ ứng dụng khỏi máy ảo rồi chạy lại.

## 5. Tài khoản dùng thử mẫu
- **Tài khoản Quản trị viên (Admin)**:
  - Tên đăng nhập: `admin`
  - Mật khẩu: `123`
  - Vai trò (`role`): `1` (Admin)
- **Tài khoản Khách hàng (User)**:
  - Tên đăng nhập: `khachhang`
  - Mật khẩu: `123`
  - Vai trò (`role`): `0` (Khách hàng)

---

## 6. Bảng thành viên và Phân công nhiệm vụ (Bản 5 thành viên)

| Thành viên | Vai trò | Activity & Layout phụ trách | Bảng thao tác | Nhánh Git |
|---|---|---|---|---|
| **Thiện** | Nền tảng và tài khoản | `MainActivity`, `DangKyActivity`, `AdminActivity`<br>(`activity_main`, `activity_dangky`, `activity_admin`) | `tbluser`, tạo toàn bộ bảng bằng `taoBang()`, nạp dữ liệu mẫu ban đầu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity`<br>(`activity_menu`, `activity_chitietmon`) | `tbldanhmuc` (đọc), `tblmon` (đọc), `tblgiohang` (ghi) | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity`<br>(`activity_giohang`, `activity_lichsu`) | `tblgiohang`, `tbldonhang` (ghi), `tblchitiet` (ghi), đọc đơn | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity`<br>(`activity_quanlymon`, `activity_quanlydanhmuc`, `activity_quanlydon`) | `tblmon`, `tbldanhmuc`, `tbldonhang` (xử lý đơn) | `tv4-quyn-admin` |
| **Bình** | Quản lý repo, gộp nhánh, kiểm thử, báo cáo | Quản lý repo, duyệt Pull Request, giải quyết conflict, gộp nhánh vào `main`, kiểm thử tổng thể, viết báo cáo, làm slide, kịch bản demo | Quản lý các file dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`) | `main` (và nhánh `bc-baocao`) |

---

## 7. Mốc thời gian thực hiện dự án

| Mốc | Thời hạn | Người phụ trách | Nội dung công việc |
|---|---|---|---|
| **M1** | Trước 12:00 Thứ Tư 04/11 | Bình | Rà soát repo: đủ 4 nhánh, bảo vệ nhánh `main`, mời đủ thành viên, đồng bộ tài liệu |
| **M2** | 22:00 Thứ Tư 04/11 | Cả nhóm | Mỗi người xong khoảng 50% khối lượng, màn hình chính hoạt động được |
| **M3** | 22:00 Thứ Năm 05/11 | Cả nhóm | **Nộp Pull Request**, đã tự kiểm thử trên máy ảo |
| **M4** | Trước 12:00 Thứ Sáu 06/11 | Bình | Gộp nhánh vào `main` theo thứ tự: Thiện ➔ Quyn ➔ Nam ➔ Hưng |
| **M5** | 12:00 – 17:00 Thứ Sáu 06/11 | Cả nhóm | Kiểm thử chéo theo kịch bản demo 8 bước, sửa lỗi phát sinh |
| **M6** | **19:00 Thứ Sáu 06/11** | **Cả nhóm** | **Bàn giao bản hoàn chỉnh: nhánh `main` chạy đủ 10 màn hình, nộp báo cáo và slide** |

---

## 8. Quy ước nhánh Git và Quy tắc làm việc nhóm

### 8.1. Quy ước nhánh
- `main`: Nhánh chính được bảo vệ, chỉ chứa mã nguồn hoàn chỉnh đã qua kiểm thử.
- Các nhánh chức năng cá nhân:
  - `tv1-thien-taikhoan`: Nhánh của Thiện.
  - `tv2-nam-menu`: Nhánh của Nam.
  - `tv3-hung-giohang`: Nhánh của Hưng.
  - `tv4-quyn-admin`: Nhánh của Quyn.
  - `bc-baocao`: Nhánh soạn thảo tài liệu, báo cáo của Bình.

### 8.2. Quy tắc làm việc nhóm
1. **Phạm vi code**: Mỗi người chỉ làm việc trên nhánh của mình và sửa các file thuộc phân công.
2. **File dùng chung**: `AndroidManifest.xml`, `strings.xml`, `docs/csdl.md` chỉ do **Bình** quản lý và chỉnh sửa.
3. **Quy tắc commit**: Không commit trực tiếp vào `main`. Làm trên nhánh riêng, đẩy lên GitHub và tạo Pull Request cho Bình duyệt.
4. **Thứ tự gộp vào `main`**: **Thiện ➔ Quyn ➔ Nam ➔ Hưng**.
5. **Tiêu đề Commit**: Ngắn gọn, có tiền tố chỉ rõ màn hình, ví dụ: `GioHang: them xoa mon bang long click`.
6. **Thay đổi CSDL / Bundle**: Bắt buộc phải thông báo cho cả nhóm trước khi đổi tên bảng, tên cột hoặc key Bundle và cập nhật vào `docs/csdl.md`.
7. **Kiểm tra trước khi nộp PR**: Xóa import thừa, mã lệnh thử nghiệm không dùng, code chú thích dở dang và log debug.
