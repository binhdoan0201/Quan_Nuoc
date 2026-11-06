# PHÂN CÔNG CÔNG VIỆC (BẢN 5 THÀNH VIÊN): ĐỒ ÁN QUANNUOC

**Đề tài:** Xây dựng ứng dụng đặt món quán nước trên nền tảng Android (project `QuanNuoc`)
**Repo:** https://github.com/binhdoan0201/Quan_Nuoc
**Nhóm:** Thiện, Nam, Hưng, Quyn, Bình
**Hôm nay:** Thứ Tư, 04/11/2026
**DEADLINE CUỐI: 19:00 Thứ Sáu, 06/11/2026**

> Ghi chú: phân công của Bình là tạm thời (quản lý repo, gộp nhánh, kiểm thử, báo cáo). Nếu sau này Bình nhận thêm Activity thì cập nhật bảng này.

---

## 1. Bảng phân công

| Thành viên | Vai trò | Phụ trách | Nhánh Git |
|---|---|---|---|
| **Thiện** | Nền tảng và tài khoản | `MainActivity`, `DangKyActivity`, `AdminActivity`; hàm `taoBang()` và dữ liệu mẫu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity` | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity` | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity` | `tv4-quyn-admin` |
| **Bình** | Quản lý repo, gộp nhánh, kiểm thử, báo cáo | Không viết Activity. Quản lý repo, duyệt PR, gộp vào `main`, kiểm thử tổng, báo cáo, slide, kịch bản demo | `main` (và nhánh `bc-baocao` cho tài liệu) |

---

## 2. Mốc thời gian

| Mốc | Thời hạn | Ai | Nội dung |
|---|---|---|---|
| M1 | Trước 12:00 Thứ Tư 04/11 | Bình | Rà repo: đủ 4 nhánh, bảo vệ `main`, mời đủ thành viên, báo cả nhóm pull về |
| M2 | 22:00 Thứ Tư 04/11 | Cả nhóm | Mỗi người xong khoảng một nửa việc, màn hình chính chạy được |
| M3 | 22:00 Thứ Năm 05/11 | Cả nhóm | **Nộp Pull Request**, đã tự test trên máy ảo |
| M4 | Trước 12:00 Thứ Sáu 06/11 | Bình | Gộp theo thứ tự: Thiện, Quyn, Nam, Hưng |
| M5 | 12:00 đến 17:00 Thứ Sáu | Cả nhóm | Test chéo theo kịch bản demo, sửa lỗi |
| **M6** | **19:00 Thứ Sáu 06/11** | **Cả nhóm** | **`main` chạy đủ 10 màn hình, báo cáo và slide xong** |

---

## 3. Công việc của BÌNH

### 3.1. Quản lý repo

**M1 (trước 12:00 Thứ Tư 04/11)**
- [ ] Kiểm tra repo `Quan_Nuoc` có đủ 4 nhánh `tv1-thien-taikhoan`, `tv2-nam-menu`, `tv3-hung-giohang`, `tv4-quyn-admin`; thiếu nhánh nào thì tạo từ `main`
- [ ] Mời Thiện, Nam, Hưng, Quyn vào repo (Settings, Collaborators) và xác nhận ai cũng có quyền ghi
- [ ] Bật bảo vệ nhánh `main` (Settings, Branches, Add rule): bắt buộc Pull Request và ít nhất 1 người duyệt, không cho đẩy thẳng
- [ ] Cập nhật `README.md` và `docs/phan-cong.md` thành bản 5 thành viên (thêm Bình)
- [ ] Đổi `app_name` trong `strings.xml` thành tên hiển thị thống nhất (ví dụ "Quán Nước")
- [ ] Nhắn nhóm: pull `main`, checkout đúng nhánh của mình, quy tắc commit và hạn M2, M3
- [ ] Kiểm tra app mở bằng Android Studio và build được (Sync Gradle không lỗi) trước khi mọi người code tiếp

**Trong thời gian làm việc**
- [ ] Là người duy nhất sửa file dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`); ai cần đổi thì nhắn Bình
- [ ] Theo dõi: mỗi người đã có commit trên nhánh của mình chưa; nhắc ai chưa đẩy code vào tối Thứ Tư
- [ ] Giữ `docs/csdl.md` khớp với code; nếu ai đổi cột, bảng hoặc key Bundle thì cập nhật và báo cả nhóm

### 3.2. Gộp nhánh và kiểm thử

**M3 (Thứ Năm 05/11, sau 22:00): nhận PR**
- [ ] Đọc nhanh từng PR: đúng file của người đó, không đụng file người khác, không còn Log debug, import thừa, code thử
- [ ] Kiểm tra đúng quy ước: comment `//Bước 1`, `//Bước 2`, `//Bước 3`; không dùng lambda (trừ `ViewCompat...`); có `c.close()`; kiểm tra rỗng trước `Integer.parseInt`
- [ ] Chạy thử nhánh đó trên máy ảo trước khi duyệt

**M4 (Thứ Sáu 06/11, trước 12:00): gộp**
- [ ] Gộp theo thứ tự: **Thiện, rồi Quyn, rồi Nam, rồi Hưng**. Sau mỗi lần gộp, build và mở app kiểm tra không văng
- [ ] Xử lý conflict (nếu có), nhắn người liên quan khi cần
- [ ] Gỡ ứng dụng khỏi máy ảo rồi chạy lại sau khi gộp để bảng CSDL tạo mới

**M5 (Thứ Sáu 06/11, 12:00 đến 17:00): kiểm thử tổng**
- [ ] Chạy trọn kịch bản demo 8 bước, ghi kết quả vào bảng test case (xem mục 4.2)
- [ ] Ghi lỗi gửi đúng người phụ trách, kèm cách tái hiện và ảnh chụp
- [ ] Chạy checklist nghiệm thu (mục 5), chỉ đánh dấu đạt khi đã kiểm tra thật
- [ ] Gộp các bản sửa lỗi cuối; trước 19:00 tạo tag bản cuối trên `main` (ví dụ `v1.0`) và kiểm tra `main` build sạch

### 3.3. Báo cáo, slide, kịch bản demo

| Hạn | Việc |
|---|---|
| Tối Thứ Tư 04/11 | Chốt tên đề tài thống nhất; viết xong Tóm tắt, Lời mở đầu, Chương 1; vẽ xong Use Case, ERD, luồng điều hướng cho Chương 2 |
| Sáng Thứ Năm 05/11 | Hoàn thành Chương 2 (bảng CSDL, nghiệp vụ đặt hàng, vòng đời đơn); dựng khung Chương 3 và bảng test case Chương 4 |
| Tối Thứ Năm 05/11 | Xin mô tả từng màn hình (3 đến 5 câu) từ Thiện, Nam, Hưng, Quyn; chọn đoạn code tiêu biểu |
| Chiều Thứ Sáu 06/11 | Chụp ảnh màn hình thật, hoàn thành Chương 3, 4, 5, phụ lục; làm slide và `docs/kich-ban-demo.md` |
| 17:00 đến 18:30 Thứ Sáu | Cập nhật mục lục, đánh số hình và bảng, kiểm tra chính tả, xuất PDF |
| **19:00 Thứ Sáu** | **Nộp báo cáo, slide, mã nguồn** |

Cần từ các bạn: mỗi người gửi Bình (trước tối Thứ Năm 05/11) mô tả 3 đến 5 câu cho từng màn hình của mình và tên đoạn code muốn trích.

---

## 4. Công việc của các thành viên còn lại

Mỗi người làm trên nhánh của mình, không commit thẳng vào `main`, tạo Pull Request cho Bình trước 22:00 Thứ Năm 05/11.

### 4.1. THIỆN: Nền tảng, tài khoản

**Hạn M2 (22:00 Thứ Tư 04/11)**
- [ ] `taoBang()` tạo đủ 6 bảng; dữ liệu mẫu: `admin / 123` (role = 1), tối thiểu 3 danh mục, 15 món
- [ ] `MainActivity`: đăng nhập, kiểm tra rỗng (`setError`), sai thì Toast báo lỗi; phân quyền role = 0 sang `MenuActivity`, role = 1 sang `AdminActivity` (Bundle `userid`, `role`)
- [ ] `DangKyActivity`: nhập username, password, fullname, phone; kiểm tra rỗng và trùng username; insert vào `tbluser` (role = 0)

**Hạn M3 (22:00 Thứ Năm 05/11)**
- [ ] `AdminActivity`: 3 nút sang `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity` (Bundle `userid`, `role`), nút quay lại
- [ ] Tự test đăng ký, đăng nhập sai, đăng nhập đúng, phân quyền; tạo Pull Request

### 4.2. NAM: Menu và chi tiết món

**Hạn M2 (22:00 Thứ Tư 04/11)**
- [ ] `MenuActivity`: nhận Bundle (`userid`, `role`); Spinner danh mục; chọn danh mục thì load món lên ListView (`mã - tên - giá đ`, dùng `DecimalFormat("#,###")`); click món sang `ChiTietMonActivity` (Bundle `userid`, `mamon`)

**Hạn M3 (22:00 Thứ Năm 05/11)**
- [ ] Tìm kiếm theo tên món (`LIKE`); nút sang `GioHangActivity` và `LichSuActivity` (Bundle `userid`)
- [ ] `ChiTietMonActivity`: hiển thị tên, mô tả, giá; chọn số lượng (kiểm tra rỗng, lớn hơn 0); thêm vào `tblgiohang` (đã có món thì cộng dồn số lượng); nút sang giỏ, nút quay lại
- [ ] Tự test và tạo Pull Request

### 4.3. HƯNG: Giỏ hàng, đặt hàng, lịch sử

**Hạn M2 (22:00 Thứ Tư 04/11)**
- [ ] `GioHangActivity`: nhận `userid`; load giỏ lên ListView (`tên - số lượng - thành tiền đ`); hàm `tinhTongTien()`

**Hạn M3 (22:00 Thứ Năm 05/11)**
- [ ] Sửa số lượng (kiểm tra rỗng, sai định dạng số); xóa bằng long click
- [ ] Đặt hàng: kiểm tra giỏ không rỗng; ghi `tbldonhang` (`ngaydat` bằng `SimpleDateFormat("dd/MM/yyyy HH:mm")`, `trangthai` = 0); ghi `tblchitiet` (lưu `gia` lúc đặt); xóa giỏ của user
- [ ] `LichSuActivity`: danh sách đơn của user (`mã - ngày - tổng tiền đ - trạng thái`); xem chi tiết đơn; hủy đơn khi còn "chờ" (`trangthai` = 3)
- [ ] Nút quay lại; xóa dữ liệu thử; tự test; tạo Pull Request

### 4.4. QUYN: Quản trị món, danh mục, đơn

**Hạn M2 (22:00 Thứ Tư 04/11)**
- [ ] `QuanLyDanhMucActivity`: load danh sách (`mã - tên`), thêm danh mục (kiểm tra rỗng)
- [ ] `QuanLyMonActivity`: load danh sách (`mã - tên - giá đ`), thêm món (kiểm tra rỗng và số trước `Integer.parseInt`)

**Hạn M3 (22:00 Thứ Năm 05/11)**
- [ ] Danh mục: sửa, xóa (còn món thuộc danh mục thì không cho xóa, báo bằng Toast)
- [ ] Món: sửa (đặc biệt sửa giá), xóa; chọn danh mục cho món bằng Spinner
- [ ] `QuanLyDonActivity`: load tất cả đơn; đổi trạng thái 0, 1, 2 (hoặc 3)
- [ ] Nút quay lại ở cả 3 màn hình; tự test; tạo Pull Request

---

## 5. Checklist nghiệm thu (Bình kiểm tra trước 19:00 Thứ Sáu)

- [ ] Đủ 10 màn hình chạy được, không văng ứng dụng
- [ ] Đủ thêm, sửa, xóa, xem trên món và danh mục
- [ ] Đăng nhập đúng, phân quyền người dùng và admin
- [ ] Dữ liệu nhập đều được kiểm tra (rỗng, sai định dạng số)
- [ ] Tiền hiển thị có dấu phân cách hàng nghìn và chữ "đ"
- [ ] Code đúng quy ước đặt tên, có comment Bước 1, 2, 3
- [ ] Có dữ liệu mẫu (tối thiểu 3 danh mục và 15 món)
- [ ] Chạy trọn kịch bản demo 8 bước không lỗi
- [ ] Mỗi người chuẩn bị phần trình bày màn hình mình làm

## 6. Kịch bản demo (8 bước)

| Bước | Nội dung | Người trình bày |
|---|---|---|
| 1 | Đăng ký tài khoản mới, đăng nhập sai (hiện lỗi), đăng nhập đúng | Thiện |
| 2 | Chọn danh mục trên Spinner, xem món, tìm kiếm một món | Nam |
| 3 | Mở chi tiết món, thêm vào giỏ hai món | Nam |
| 4 | Vào giỏ hàng: sửa số lượng, xóa một món bằng long click, đặt hàng | Hưng |
| 5 | Vào lịch sử, xem chi tiết đơn vừa đặt (trạng thái "chờ") | Hưng |
| 6 | Đăng nhập admin: thêm món mới, sửa giá, xóa một món | Quyn |
| 7 | Vào quản lý đơn, đổi trạng thái sang "đang làm" rồi "hoàn thành" | Quyn |
| 8 | Đăng nhập lại tài khoản người dùng, xem trạng thái đơn đã cập nhật | Hưng |
| Mở đầu, kết | Giới thiệu đề tài, tổng kết, trả lời câu hỏi | Bình |

## 7. Quy tắc làm việc

1. Mỗi người chỉ sửa file thuộc phần mình. File dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`) chỉ **Bình** sửa.
2. Không commit thẳng vào `main`. Làm trên nhánh riêng, xong tạo Pull Request cho Bình.
3. Thứ tự gộp vào `main`: Thiện, Quyn, Nam, Hưng.
4. Commit message ngắn, có tiền tố, ví dụ `GioHang: them xoa mon bang long click`.
5. Báo cả nhóm trước khi đổi tên bảng, tên cột hoặc key Bundle.
6. Đổi cấu trúc bảng: gỡ ứng dụng khỏi máy ảo rồi chạy lại để bảng được tạo mới.
7. Trước khi nộp PR: xóa import thừa, code chạy thử, code comment bỏ dở và Log debug.
