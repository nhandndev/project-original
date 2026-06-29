# Luồng Quản Lý Sách (Book Flow)

Luồng xử lý Sách trong hệ thống chịu trách nhiệm quản lý kho sách của thư viện, được thực hiện chủ yếu qua class `BookManager`. Dưới đây là giải thích chi tiết về luồng hoạt động:

## 1. Thêm Sách (Add Book)
- Người dùng nhập thông tin sách (ID, Title, Author, Genre, Year, Quantity, Type...).
- `BookManager` kiểm tra tính hợp lệ:
  - ID không được để trống và không được trùng lặp.
  - Các trường thông tin cơ bản không được trống.
  - Số lượng sách không được âm.
- Nếu hợp lệ, sách mới được khởi tạo bằng class `Book` (chứa các thuộc tính phẳng, phân biệt bằng trường `type` như `physical` hoặc `ebook`) và được thêm vào danh sách quản lý.

## 2. Cập Nhật Sách (Update Book)
- Chức năng này cho phép cập nhật số lượng của một cuốn sách hiện có.
- Người dùng cung cấp ID của sách cần cập nhật và số lượng mới.
- Hệ thống tìm sách theo ID, nếu tìm thấy và số lượng nhập vào hợp lệ (không âm), hệ thống sẽ cập nhật lại số lượng sách.

## 3. Xóa Sách (Remove Book)
- Khi muốn xóa sách, người dùng cung cấp ID sách.
- `BookManager` kiểm tra điều kiện an toàn: 
  - Sách phải tồn tại trong hệ thống.
  - Sách phải **không** nằm trong trạng thái đang được mượn (isBorrowed).
- Nếu thỏa mãn, sách sẽ bị xóa khỏi danh sách.

## 4. Các Chức Năng Bổ Trợ
- **Xem tất cả sách (View Books):** Trả về toàn bộ danh sách sách hiện có.
- **Tìm kiếm sách (Search Books):** Lọc sách dựa theo từ khóa, tìm kiếm trên tiêu đề, tác giả hoặc thể loại (so khớp không phân biệt hoa thường).
- **Thống kê sách phổ biến (Popular Books):** Trả về danh sách được sắp xếp giảm dần dựa trên tổng số lượt mượn (`borrowCount`) của từng sách để xem sách nào được mượn nhiều nhất.
