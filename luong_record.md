# Luồng Quản Lý Lịch Sử Mượn Trả (Borrow Record Flow)

Luồng này quản lý toàn bộ giao dịch mượn và trả sách, chịu trách nhiệm chính về nghiệp vụ logic cốt lõi. Nó được xử lý bởi `BorrowManager`, là cầu nối liên kết giữa `BookManager` và `MemberManager`.

## 1. Mượn Sách (Borrow Book)
- Người dùng nhập ID thành viên, ID sách, Ngày mượn.
- `BorrowManager` thực hiện các kiểm tra khắt khe (Validation):
  - Thành viên phải tồn tại trong hệ thống.
  - Thành viên chưa đạt đến giới hạn mượn sách tối đa (`currentBorrowCount` < `borrowLimit`).
  - Sách phải tồn tại trong hệ thống.
  - Số lượng sách phải > 0 (Trừ khi đó là Ebook, vì Ebook không bị giới hạn số lượng kho).
  - Ngày mượn phải hợp lệ và không được là ngày trong tương lai so với hiện tại.
- **Sau khi xác thực thành công:**
  - Một đối tượng `BorrowRecord` mới được tạo ra với trạng thái trả là `false`. Đối với sách vật lý có thể bao gồm tình trạng sách, đối với ebook có thể kèm theo link download.
  - **Cập nhật dữ liệu liên quan:** 
    - Sách: `decreaseQuantity()` (giảm số lượng kho) và `incrementBorrowCount()` (tăng lượt mượn).
    - Thành viên: `incrementCurrentBorrowCount()` (số sách đang mượn) và `incrementTotalBorrowCount()` (tổng số lần mượn lịch sử).

## 2. Trả Sách (Return Book)
- Người dùng cung cấp ID thành viên, ID sách và Ngày trả.
- `BorrowManager` tiến hành kiểm tra:
  - Xác nhận thành viên và sách tồn tại.
  - Tìm bản ghi mượn sách (BorrowRecord) đang hoạt động (chưa trả) khớp với ID người dùng và sách.
  - Đảm bảo Ngày trả phải sau (hoặc bằng) Ngày mượn.
- **Xử lý trả sách và tính phạt:**
  - Hệ thống tính toán Ngày đến hạn (`dueDate`) dựa trên Ngày mượn và Chu kỳ mượn cho phép của loại thành viên đó (`borrowPeriod`).
  - Nếu Ngày trả sau Ngày đến hạn, hệ thống tính số ngày trễ và dùng hàm `calculateFine()` của class Member để tính tiền phạt (Ebook có thể không tính phạt tùy logic áp dụng).
- **Hoàn thành giao dịch:**
  - Đánh dấu bản ghi đã trả (`isReturned = true`).
  - Sách: `increaseQuantity()` (phục hồi kho).
  - Thành viên: `decrementCurrentBorrowCount()` (giảm số sách đang giữ).

## 3. Các Báo Cáo Liên Quan
- **Sách đang mượn (Currently Borrowed):** Liệt kê các bản ghi chưa trả (`isReturned = false`).
- **Lịch sử của thành viên (Borrowing History):** Lấy tất cả các giao dịch (cả đã trả và chưa trả) của một thành viên cụ thể.
- **Báo cáo sách quá hạn (Overdue Report):** Dựa vào ngày được chỉ định (thường là ngày hôm nay), hệ thống kiểm tra các bản ghi chưa trả và so sánh với `dueDate`. Nếu quá hạn, hiển thị tên sách, tên người mượn, ngày đến hạn và số ngày trễ phạt.
