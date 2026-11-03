# HƯỚNG DẪN CHO LLM: KHỞI TẠO PROJECT VÀ REPO GIT "QuanNuoc"

> Bạn là trợ lý lập trình. Hãy đọc toàn bộ file này rồi thực hiện lần lượt các bước bên dưới để dựng **khung** (scaffold) project Android và repo Git cho nhóm 4 người. **Chỉ dựng khung, KHÔNG viết logic nghiệp vụ** (trừ những chỗ file này yêu cầu rõ). Mỗi thành viên sẽ tự viết phần của mình sau.

---

## 0. Thông tin chung

| Mục | Nội dung |
|---|---|
| Tên project | `QuanNuoc` |
| Package | `com.example.quannuoc` |
| Ngôn ngữ | Java (KHÔNG Kotlin) |
| Template | Empty Views Activity, Build configuration language: **Groovy DSL** |
| CSDL | SQLite, mở bằng `openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null)` |
| Đề tài | Ứng dụng đặt món quán nước / căng tin (đồ án môn Lập trình ứng dụng di động Android) |
| Nhóm | 4 người: Thiện, Nam, Hưng, Quyn |

### Ràng buộc kỹ thuật (bắt buộc tuân thủ)
- Không dùng Kotlin, ViewBinding, RecyclerView, Room, Fragment, thư viện ngoài.
- Sự kiện viết kiểu `new View.OnClickListener() { ... }`, không dùng lambda (trừ đoạn `ViewCompat.setOnApplyWindowInsetsListener` do Android Studio sinh sẵn).
- Mọi Activity chia 3 bước, ghi comment đúng nguyên văn:
  - `//Bước 1: Khai báo biến giao diện`
  - `//Bước 2: Ánh xạ biến giao diện`
  - `//Bước 3: Viết code xử lý`
- Thụt lề 4 khoảng trắng, mỗi lệnh một dòng, comment tiếng Việt ngắn gọn.
- Tên class/biến/ID viết tiếng Việt không dấu, viết liền (ví dụ `GioHangActivity`, `activity_giohang`, `lvmon`).

---

## 1. Tạo project Android

### Cách A (khuyến nghị): tạo bằng Android Studio
Vì file Gradle và wrapper dễ sai khi LLM tự viết, hãy để người dùng tạo project bằng Android Studio trước:
1. New Project, chọn **Empty Views Activity**.
2. Name: `QuanNuoc`, Package name: `com.example.quannuoc`, Language: **Java**, Build configuration language: **Groovy DSL**.
3. Minimum SDK: API 24 trở lên.
4. Sau đó đặt thư mục project đó làm thư mục gốc của repo, rồi chạy tiếp từ **Bước 2** bên dưới.

### Cách B: LLM tự tạo
Nếu thư mục project chưa tồn tại, hãy hỏi người dùng có muốn LLM tự sinh file Gradle không. Nếu có, sinh đủ: `settings.gradle`, `build.gradle` (gốc), `app/build.gradle` (Groovy DSL), `gradle.properties`, `app/proguard-rules.pro`, và nhắc người dùng mở bằng Android Studio để tải Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`).

---

## 2. Cấu trúc thư mục cần có

```
QuanNuoc/
├── README.md
├── .gitignore
├── docs/
│   ├── dac-ta-du-an.pdf            (người dùng tự copy bản đặc tả vào, LLM chỉ tạo thư mục)
│   ├── csdl.md
│   ├── phan-cong.md
│   ├── kich-ban-demo.md
│   └── slide/
│       └── .gitkeep
├── gradlew, gradlew.bat, settings.gradle, build.gradle
└── app/
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/quannuoc/
        │   ├── MainActivity.java
        │   ├── DangKyActivity.java
        │   ├── AdminActivity.java
        │   ├── MenuActivity.java
        │   ├── ChiTietMonActivity.java
        │   ├── GioHangActivity.java
        │   ├── LichSuActivity.java
        │   ├── QuanLyMonActivity.java
        │   ├── QuanLyDanhMucActivity.java
        │   └── QuanLyDonActivity.java
        └── res/
            ├── layout/
            │   ├── activity_main.xml
            │   ├── activity_dangky.xml
            │   ├── activity_admin.xml
            │   ├── activity_menu.xml
            │   ├── activity_chitietmon.xml
            │   ├── activity_giohang.xml
            │   ├── activity_lichsu.xml
            │   ├── activity_quanlymon.xml
            │   ├── activity_quanlydanhmuc.xml
            │   └── activity_quanlydon.xml
            ├── drawable/
            └── values/
```

Tất cả Activity để chung một package `com.example.quannuoc`, không chia thư mục con.

---

## 3. Sinh 10 Activity rỗng (khung)

Với mỗi Activity trong bảng dưới, tạo file `.java` theo **khung mẫu** ở mục 3.1 và file layout tương ứng theo mục 3.2.

| STT | Activity | Layout | Người phụ trách | Nhận Bundle (key) |
|---|---|---|---|---|
| 1 | `MainActivity` | `activity_main` | Thiện | không |
| 2 | `DangKyActivity` | `activity_dangky` | Thiện | không |
| 3 | `MenuActivity` | `activity_menu` | Nam | `userid`, `role` |
| 4 | `ChiTietMonActivity` | `activity_chitietmon` | Nam | `userid`, `mamon` |
| 5 | `GioHangActivity` | `activity_giohang` | Hưng | `userid` |
| 6 | `LichSuActivity` | `activity_lichsu` | Hưng | `userid` |
| 7 | `AdminActivity` | `activity_admin` | Thiện | `userid`, `role` |
| 8 | `QuanLyMonActivity` | `activity_quanlymon` | Quyn | `userid`, `role` |
| 9 | `QuanLyDanhMucActivity` | `activity_quanlydanhmuc` | Quyn | `userid`, `role` |
| 10 | `QuanLyDonActivity` | `activity_quanlydon` | Quyn | `userid`, `role` |

### 3.1. Khung mẫu một Activity (thay `TenActivity` và `activity_ten`)

```java
package com.example.quannuoc;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TenActivity extends AppCompatActivity {
    //Bước 1: Khai báo biến giao diện
    Button btnquaylai;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ten);

        //Bước 2: Ánh xạ biến giao diện
        btnquaylai = findViewById(R.id.btnquaylai);

        //Bước 3: Viết code xử lý
        mydatabase = openOrCreateDatabase("quannuoc.db", MODE_PRIVATE, null);

        btnquaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
```

Lưu ý:
- `MainActivity` là màn hình đầu tiên, **không có nút quay lại**: bỏ `btnquaylai` và đoạn `finish()` ở file này.
- Mọi màn hình khác phải có nút QUAY LẠI gọi `finish()`.
- Chỉ `MainActivity` được tạo bảng. Các Activity khác chỉ mở lại CSDL bằng `openOrCreateDatabase`.
- Bỏ import không dùng trước khi commit.

### 3.2. Khung layout mẫu (thay phần tiêu đề cho từng màn hình)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".TenActivity">

    <TextView
        android:id="@+id/txttieude"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Tên màn hình"
        android:textSize="22sp"
        android:textStyle="bold" />

    <Button
        android:id="@+id/btnquaylai"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="QUAY LẠI" />
</LinearLayout>
```

Quy ước ID view: id view gốc **luôn là `main`**. Các view khác dùng tiền tố loại view + tên viết thường liền: `edit` (EditText), `btn` (Button), `txt` (TextView), `lv` (ListView), `sp` (Spinner), `img` (ImageView).

Tiêu đề hiển thị cho từng layout:

| Layout | Tiêu đề (`txttieude`) |
|---|---|
| activity_main | Đăng nhập |
| activity_dangky | Đăng ký tài khoản |
| activity_menu | Menu |
| activity_chitietmon | Chi tiết món |
| activity_giohang | Giỏ hàng |
| activity_lichsu | Lịch sử đơn hàng |
| activity_admin | Quản trị |
| activity_quanlymon | Quản lý món |
| activity_quanlydanhmuc | Quản lý danh mục |
| activity_quanlydon | Quản lý đơn hàng |

---

## 4. Khai báo trong `AndroidManifest.xml`

Khai báo đủ 10 Activity ngay từ đầu để 3 thành viên còn lại không phải sửa file này (tránh conflict). `MainActivity` là launcher.

```xml
<application ...>
    <activity android:name=".MainActivity" android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    <activity android:name=".DangKyActivity" android:exported="false" />
    <activity android:name=".MenuActivity" android:exported="false" />
    <activity android:name=".ChiTietMonActivity" android:exported="false" />
    <activity android:name=".GioHangActivity" android:exported="false" />
    <activity android:name=".LichSuActivity" android:exported="false" />
    <activity android:name=".AdminActivity" android:exported="false" />
    <activity android:name=".QuanLyMonActivity" android:exported="false" />
    <activity android:name=".QuanLyDanhMucActivity" android:exported="false" />
    <activity android:name=".QuanLyDonActivity" android:exported="false" />
</application>
```

---

## 5. Tạo các file tài liệu trong `docs/`

### 5.1. `docs/csdl.md`
Nội dung: mô tả CSDL `quannuoc.db`, 5 bảng:

- **tbluser**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `username` (TEXT), `password` (TEXT), `fullname` (TEXT), `phone` (TEXT), `role` (INTEGER: 0 = người dùng, 1 = admin)
- **tbldanhmuc**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `tendm` (TEXT)
- **tblmon**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `tenmon` (TEXT), `mota` (TEXT), `gia` (INTEGER, đồng), `madm` (INTEGER, tham chiếu `tbldanhmuc.id`)
- **tblgiohang**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `userid` (INTEGER), `mamon` (INTEGER), `soluong` (INTEGER)
- **tbldonhang**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `userid` (INTEGER), `ngaydat` (TEXT, định dạng `dd/MM/yyyy HH:mm`), `tongtien` (INTEGER), `trangthai` (INTEGER)
- **tblchitiet**: `id` (INTEGER PRIMARY KEY AUTOINCREMENT), `madon` (INTEGER), `mamon` (INTEGER), `soluong` (INTEGER), `gia` (INTEGER, đơn giá tại thời điểm đặt)

Giá trị quy ước:
- `tbluser.role`: 0 = người dùng, 1 = admin
- `tbldonhang.trangthai`: 0 = chờ, 1 = đang làm, 2 = hoàn thành, 3 = hủy
- Tài khoản mẫu: `admin / 123` (role = 1)
- Dữ liệu mẫu tối thiểu: 3 danh mục, 15 món

Ghi thêm: quy tắc đặt hàng (ghi 1 dòng `tbldonhang`, ghi các dòng `tblchitiet` từ `tblgiohang`, rồi xóa các dòng giỏ của người dùng đó) và lưu ý "mọi thay đổi cấu trúc bảng phải cập nhật file này và báo cả nhóm".

> Lưu ý (tên bảng là 6 bảng gồm cả `tblchitiet`; đặc tả gốc gọi chung là "các bảng").

### 5.2. `docs/phan-cong.md`
Nội dung: bảng phân công ở mục 8 của file này.

### 5.3. `docs/kich-ban-demo.md`
Nội dung: kịch bản demo 8 bước:
1. Đăng ký tài khoản mới, đăng nhập sai (hiện thông báo lỗi), đăng nhập đúng.
2. Chọn danh mục trên Spinner, xem món, tìm kiếm một món.
3. Mở chi tiết món, thêm vào giỏ hai món.
4. Vào giỏ hàng: sửa số lượng, xóa một món bằng long click, đặt hàng.
5. Vào lịch sử, xem chi tiết đơn vừa đặt (trạng thái "chờ").
6. Đăng nhập admin: thêm một món mới, sửa giá, xóa một món.
7. Vào quản lý đơn, đổi trạng thái đơn sang "đang làm" rồi "hoàn thành".
8. Đăng nhập lại tài khoản người dùng, xem trạng thái đơn đã được cập nhật.

Kèm checklist nghiệm thu:
- [ ] Đủ 10 màn hình chạy được, không văng ứng dụng
- [ ] Đủ thêm, sửa, xóa, xem trên món và danh mục
- [ ] Đăng nhập đúng, phân quyền người dùng và admin
- [ ] Dữ liệu nhập đều được kiểm tra (rỗng, sai định dạng số)
- [ ] Tiền hiển thị có dấu phân cách hàng nghìn và chữ "đ"
- [ ] Code đúng quy ước đặt tên và có comment Bước 1, Bước 2, Bước 3
- [ ] Có sẵn dữ liệu mẫu (tối thiểu 3 danh mục và 15 món) để demo

---

## 6. File `README.md` ở thư mục gốc

Tạo README gồm các mục:
1. Tên đề tài và mô tả ngắn.
2. Công nghệ: Java, SQLite, Android Studio, Groovy DSL.
3. Chức năng người dùng và admin (tóm tắt).
4. Cách chạy: mở bằng Android Studio, Sync Gradle, chạy trên máy ảo. Nếu đổi cấu trúc bảng thì **gỡ ứng dụng khỏi máy ảo rồi chạy lại** để bảng được tạo mới.
5. Tài khoản mẫu: `admin / 123`.
6. Bảng thành viên (xem mục 8).
7. Quy ước nhánh Git và quy tắc commit (xem mục 7).

---

## 7. File `.gitignore`

```
# Gradle
.gradle/
build/
*/build/

# Android Studio
.idea/
*.iml
local.properties
captures/
.externalNativeBuild/
.cxx/

# Hệ điều hành
.DS_Store
Thumbs.db

# Khác
*.apk
*.aab
*.log
```

---

## 8. Phân công (dùng cho `docs/phan-cong.md` và README)

| Thành viên | Vai trò | Activity và layout | Bảng thao tác | Nhánh |
|---|---|---|---|---|
| **Thiện** | Nền tảng, tài khoản, trưởng nhóm ghép code | `MainActivity`, `DangKyActivity`, `AdminActivity` | `tbluser`, tạo toàn bộ bảng bằng `taoBang()`, dữ liệu mẫu | `tv1-thien-taikhoan` |
| **Nam** | Menu và chi tiết món | `MenuActivity`, `ChiTietMonActivity` | `tbldanhmuc` (đọc), `tblmon` (đọc), `tblgiohang` (ghi) | `tv2-nam-menu` |
| **Hưng** | Giỏ hàng, đặt hàng, lịch sử | `GioHangActivity`, `LichSuActivity` | `tblgiohang`, `tbldonhang` (ghi), `tblchitiet` (ghi), đọc đơn | `tv3-hung-giohang` |
| **Quyn** | Quản trị món, danh mục, đơn | `QuanLyMonActivity`, `QuanLyDanhMucActivity`, `QuanLyDonActivity` | `tblmon`, `tbldanhmuc`, `tbldonhang` (đổi trạng thái) | `tv4-quyn-admin` |

Luồng điều hướng và Bundle (key trùng tên cột, tên Bundle trong Intent là `"goi"`):

| Từ | Đến | Bundle gửi đi |
|---|---|---|
| MainActivity | DangKyActivity | không có |
| MainActivity | MenuActivity (role = 0) | `userid`, `role` |
| MainActivity | AdminActivity (role = 1) | `userid`, `role` |
| MenuActivity | ChiTietMonActivity | `userid`, `mamon` |
| MenuActivity | GioHangActivity | `userid` |
| MenuActivity | LichSuActivity | `userid` |
| ChiTietMonActivity | GioHangActivity | `userid` |
| AdminActivity | QuanLyMonActivity, QuanLyDanhMucActivity, QuanLyDonActivity | `userid`, `role` |

---

## 9. Thao tác Git (LLM chạy lần lượt, hoặc in ra để người dùng tự chạy)

> Nếu chưa có remote, hỏi người dùng URL repo GitHub/GitLab (đã tạo rỗng, không có README) rồi mới chạy lệnh `git remote add`.

```bash
# 1. Khởi tạo repo và commit nền
cd QuanNuoc
git init
git branch -M main
git add .
git commit -m "Khoi tao project QuanNuoc: khung 10 Activity, Manifest, docs"

# 2. Gắn remote và đẩy main
git remote add origin <URL_REPO>
git push -u origin main

# 3. Tạo 4 nhánh từ main và đẩy lên
git checkout -b tv1-thien-taikhoan  && git push -u origin tv1-thien-taikhoan  && git checkout main
git checkout -b tv2-nam-menu        && git push -u origin tv2-nam-menu        && git checkout main
git checkout -b tv3-hung-giohang    && git push -u origin tv3-hung-giohang    && git checkout main
git checkout -b tv4-quyn-admin      && git push -u origin tv4-quyn-admin      && git checkout main
```

### Quy tắc làm việc nhóm (ghi vào README)
1. Mỗi người chỉ sửa file thuộc phần mình phụ trách. Cần sửa file người khác thì nhắn người đó.
2. File dùng chung (`AndroidManifest.xml`, `strings.xml`, `docs/csdl.md`) chỉ **Thiện** sửa.
3. Không commit thẳng vào `main`. Làm trên nhánh riêng, xong tạo Pull Request cho Thiện duyệt.
4. Thứ tự gộp vào `main`: **Thiện, rồi Quyn, rồi Nam, rồi Hưng**.
5. Thiện làm `MainActivity` và hàm `taoBang()` đầu tiên, đẩy lên để mọi người có CSDL để test.
6. Commit message ngắn, có tiền tố tên màn hình, ví dụ `GioHang: them xoa mon bang long click`.
7. Đổi tên bảng, tên cột hoặc key Bundle: báo cả nhóm trước và cập nhật `docs/csdl.md`.
8. Trước khi nộp: xóa import thừa, code chạy thử, code bị comment bỏ dở và Log debug.

---

## 10. Việc LLM KHÔNG được làm
- Không viết logic đăng nhập, truy vấn DB, adapter... trong các Activity (chỉ để khung).
- Không đổi tên package, tên Activity, tên bảng, tên cột, key Bundle so với file này.
- Không thêm thư viện vào `build.gradle`.
- Không đổi sang Kotlin hoặc ViewBinding.
- Không commit thư mục `build/`, `.gradle/`, `.idea/`, `local.properties`.

---

## 11. Checklist hoàn thành (LLM tự kiểm tra rồi báo lại)
- [ ] Có đủ 10 file `.java` và 10 file layout đúng tên
- [ ] Mỗi Activity có comment `//Bước 1`, `//Bước 2`, `//Bước 3`
- [ ] `AndroidManifest.xml` khai báo đủ 10 Activity, `MainActivity` là launcher
- [ ] Có `README.md`, `.gitignore`, thư mục `docs/` với `csdl.md`, `phan-cong.md`, `kich-ban-demo.md`
- [ ] Repo đã commit đầu tiên trên `main`
- [ ] Đã tạo 4 nhánh: `tv1-thien-taikhoan`, `tv2-nam-menu`, `tv3-hung-giohang`, `tv4-quyn-admin`
- [ ] Project build được (Sync Gradle không lỗi)
