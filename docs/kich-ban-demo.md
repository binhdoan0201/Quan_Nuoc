# Kịch Bản Demo và Checklist Nghiệm Thu - Dự Án QuanNuoc

## 1. Kịch bản Demo 8 bước

1. **Bước 1**: Đăng ký tài khoản mới, thử đăng nhập sai thông tin (hiển thị thông báo lỗi rõ ràng), sau đó đăng nhập đúng vào hệ thống.
2. **Bước 2**: Chọn danh mục món trên Spinner, xem danh sách món lọc theo danh mục, thực hiện tìm kiếm một món cụ thể.
3. **Bước 3**: Mở màn hình chi tiết món, thêm vào giỏ hàng hai món khác nhau.
4. **Bước 4**: Vào màn hình giỏ hàng: sửa đổi số lượng món, thực hiện xóa một món bằng thao tác long click (nhấn giữ), sau đó bấm đặt hàng.
5. **Bước 5**: Vào màn hình lịch sử đơn hàng, xem chi tiết đơn hàng vừa đặt (kiểm tra trạng thái ban đầu là "chờ").
6. **Bước 6**: Đăng nhập tài khoản admin (`admin` / `123`): thêm một món mới, sửa giá của một món, xóa một món khỏi danh sách.
7. **Bước 7**: Vào trang quản lý đơn hàng: chọn đơn hàng vừa đặt ở Bước 4, đổi trạng thái đơn sang "đang làm" rồi sang "hoàn thành".
8. **Bước 8**: Đăng nhập lại vào tài khoản người dùng ban đầu, vào mục lịch sử để kiểm tra trạng thái đơn đã được cập nhật thành công.

---

## 2. Checklist nghiệm thu

- [ ] Đủ 10 màn hình chạy được, không văng ứng dụng
- [ ] Đủ thêm, sửa, xóa, xem trên món và danh mục
- [ ] Đăng nhập đúng, phân quyền người dùng và admin
- [ ] Dữ liệu nhập đều được kiểm tra (rỗng, sai định dạng số)
- [ ] Tiền hiển thị có dấu phân cách hàng nghìn và chữ "đ"
- [ ] Code đúng quy ước đặt tên và có comment Bước 1, Bước 2, Bước 3
- [ ] Có sẵn dữ liệu mẫu (tối thiểu 3 danh mục và 15 món) để demo
