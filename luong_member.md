# Luồng Quản Lý Thành Viên (Member Flow)

Luồng xử lý Thành Viên trong hệ thống có nhiệm vụ quản lý người dùng thư viện, được điều phối thông qua `MemberManager`.

## 1. Thêm Thành Viên (Add Member)
- Người dùng nhập thông tin thành viên mới: ID, Name, Phone, Email, Type (Regular hoặc Premium).
- `MemberManager` thực hiện xác thực:
  - ID và Name không được để trống.
  - ID không được trùng với thành viên đã có trong hệ thống.
- Dựa vào trường `Type`, đối tượng `Member` mới được khởi tạo và lưu vào hệ thống. Các loại thành viên khác nhau (Regular/Premium) sẽ có các giới hạn (borrowLimit), chu kỳ (borrowPeriod) và mức phạt (fine) khác nhau được quy định bên trong class `Member`.

## 2. Cập Nhật Thông Tin (Update Member)
- Cho phép chỉnh sửa thông tin liên lạc (Phone, Email) của thành viên thông qua ID.
- Nếu thành viên tồn tại trong hệ thống, các thông tin mới được nhập (nếu không trống) sẽ được ghi đè lên thông tin cũ.

## 3. Xóa Thành Viên (Remove Member)
- Xóa thành viên khỏi thư viện thông qua ID.
- **Điều kiện ràng buộc quan trọng:** Chỉ có thể xóa thành viên nếu họ **không** đang mượn bất kỳ cuốn sách nào (currentBorrowCount = 0). Nếu thành viên vẫn đang giữ sách của thư viện, hệ thống sẽ chặn thao tác xóa.

## 4. Các Chức Năng Bổ Trợ
- **Xem tất cả thành viên (View Members):** Trả về danh sách toàn bộ thành viên.
- **Tìm kiếm thành viên (Search Members):** Tìm kiếm theo tên hoặc ID.
- **Thống kê thành viên mượn sách nhiều (Borrowing Count):** Sắp xếp danh sách thành viên theo tổng số lượt mượn từ trước đến nay (`totalBorrowCount`) theo thứ tự giảm dần để xác định các thành viên tích cực nhất.
