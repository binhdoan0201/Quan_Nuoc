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


---

## 9. Nhật ký nâng cấp & hoàn thiện (Changelog sau gộp nhánh)

Sau khi tích hợp 4 nhánh chức năng, toàn bộ mã nguồn đã được rà soát, kiểm thử toàn diện và tiến hành các đợt nâng cấp kỹ thuật, cải thiện trải nghiệm người dùng, đảm bảo tính toàn vẹn dữ liệu:

### 9.1. Sửa lỗi Crash hệ thống & Ổn định bộ nhớ
- **Sửa lỗi crash `ArrayAdapter` tại `GioHangActivity`**: Chuyển đổi từ `android.R.layout.simple_list_item_2` sang `simple_list_item_1` phù hợp với `ArrayAdapter<String>`, khắc phục hoàn toàn lỗi ném ngoại lệ khi render danh sách giỏ hàng.
- **Bọc `try-catch NumberFormatException` khi sửa số lượng trong giỏ hàng**: Thêm cơ chế bắt lỗi khi người dùng nhập số quá lớn hoặc ký tự bất thường, đồng thời giới hạn số lượng hợp lệ trong khoảng `1 – 999` món kèm thông báo Toast chi tiết.
- **Xử lý triệt để rò rỉ con trỏ SQLite (`Cursor Leak`) tại 5 vị trí**: Đưa lệnh `c.close()` ra ngoài khối `if (c.moveToFirst())` và đảm bảo luôn đóng con trỏ trong mọi kịch bản dữ liệu (bao gồm `GioHangActivity` [2 chỗ], `LichSuActivity` [2 chỗ] và `QuanLyDonActivity` [1 chỗ]).
- **Chuẩn hóa cú pháp thuần Java (Loại bỏ 100% Lambda)**: Toàn bộ listener và callback được chuyển đổi sang dạng lớp nặc danh (`new View.OnClickListener()`, `new DialogInterface.OnClickListener()`,...) tuân thủ chặt chẽ yêu cầu kỹ thuật của đề tài.

### 9.2. Nâng cấp Giao diện & Trải nghiệm người dùng (UI/UX)
- **Tối ưu hóa giao diện `QuanLyMonActivity`**: Thiết kế lại layout dạng biểu mẫu nhập liệu trực quan, phân vùng rõ ràng giữa ô nhập, bộ chọn ảnh và danh sách món ăn.
- **Bổ sung nút "Làm mới" (Reset Form) tại `QuanLyMonActivity`**: Cho phép quản trị viên nhanh chóng xóa trắng form nhập liệu để thêm món mới mà không bị dính dữ liệu của món vừa chọn.
- **Chuẩn hóa điều hướng & nút Đăng xuất**: Thay thế các nút "Đăng xuất" không phù hợp tại các màn hình con thành nút "Quay lại" trực quan; chỉ giữ chức năng Đăng xuất tại màn hình chính của khách hàng (`MenuActivity`) và quản trị viên (`AdminActivity`).
- **Tự động điền tên đăng nhập sau đăng ký**: Sau khi đăng ký tài khoản thành công tại `DangKyActivity`, hệ thống tự động quay về `MainActivity` và điền sẵn username vào ô đăng nhập.
- **Đồng bộ hóa hiển thị tiền tệ**: Chuẩn hóa toàn bộ định dạng hiển thị giá tiền sang dạng có dấu chấm phân cách hàng nghìn (ví dụ: `25.000 đ`) trên tất cả 10 màn hình của ứng dụng.

### 9.3. Nâng cấp Nghiệp vụ & Bổ sung tính năng mới
- **Bộ lọc danh mục thức uống trên form quản trị món**: Bổ sung Spinner lọc danh sách món theo từng danh mục tại `QuanLyMonActivity`, xử lý cờ `isUpdatingFromList` tránh xung đột dữ liệu khi chọn món từ danh sách.
- **Tích hợp bộ chọn 20 ảnh mẫu đồ uống có xem trước (Preview)**: Bổ sung bộ tài nguyên hình ảnh phong phú, cho phép Admin chọn ảnh trực quan kèm ImageView xem trước tức thì; đồng bộ hiển thị ảnh đại diện tại `MenuActivity` và `ChiTietMonActivity`.
- **Hiển thị thông tin khách hàng trên đơn đặt**: Bổ sung hiển thị Tên khách hàng và Số điện thoại liên hệ trực tiếp trên từng mục đơn hàng tại màn hình `QuanLyDonActivity`, giúp quán liên hệ giao nhận dễ dàng.
- **Xem chi tiết danh sách món cần pha chế**: Hỗ trợ Admin nhấn vào từng đơn hàng để xem danh sách chi tiết các món, số lượng, đơn giá và tổng tiền cần xử lý.
- **Áp dụng State Machine quản lý trạng thái đơn hàng**: Quản lý vòng đời đơn hàng theo luồng chuẩn 1 chiều (`Chờ xác nhận (0)` ➔ `Đang làm (1)` ➔ `Hoàn thành (2)` hoặc `Đã hủy (3)`), khóa cứng không cho phép chuyển ngược trạng thái đơn hàng đã hoàn tất hoặc đã hủy.

### 9.4. Ràng buộc dữ liệu & Toàn vẹn cơ sở dữ liệu (Data Integrity)
- **Chặn xóa danh mục khi còn món**: Kiểm tra `SELECT COUNT(*) FROM tblmon WHERE madm = ?` trước khi xóa danh mục trong `QuanLyDanhMucActivity`; nếu còn món sẽ cảnh báo Toast và từ chối xóa để tránh tạo ra dữ liệu mồ côi.
- **Chặn xóa món đang nằm trong đơn hàng hoặc giỏ hàng**: Kiểm tra sự tồn tại của món trong `tblchitiet` và `tblgiohang` trước khi xóa tại `QuanLyMonActivity`, đảm bảo tính toàn vẹn dữ liệu lịch sử đơn hàng và tổng tiền.
- **Ràng buộc đơn giá món bắt buộc `> 0`**: Chặn nhập giá `0` hoặc số âm ở cả hai chức năng Thêm mới và Sửa món, hiển thị thông báo yêu cầu nhập đơn giá hợp lệ.
- **Ràng buộc trạng thái khi khách hủy đơn**: Tại `LichSuActivity`, câu lệnh hủy đơn được siết chặt với điều kiện `id = ? AND trangthai = 0`; ngăn chặn tình trạng khách hủy nhầm đơn hàng khi quán đã chuyển sang trạng thái "Đang làm".

### 9.5. Cập nhật Cơ sở dữ liệu
- **Mở rộng lược đồ bảng `tblmon`**: Bổ sung cột `hinhanh TEXT` để lưu trữ tên tài nguyên ảnh đồ uống, tích hợp cơ chế tự động nâng cấp CSDL (Auto-migration) kiểm tra và thêm cột an toàn qua `ALTER TABLE` khi khởi chạy ứng dụng.
