# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS BORROWRECORD (ENTITY)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

Lịch sử giao dịch (Mượn trả) trong `codeNoOOP.md` được tracking qua 4 mảng tĩnh (dòng 36 đến 40). Chúng ta gom lại thành `entity/BorrowRecord.java`.

| Tính năng | Mã nguồn Non-OOP (trong Main.class) | Mã nguồn OOP (BorrowRecord.java) | Giải thích sự cải tiến |
| :--- | :--- | :--- | :--- |
| **Khai báo dữ liệu** | `static String[] borrowBookIds;`<br>`static String[] borrowMemberIds;`<br>`static String[] borrowDates;`<br>`static boolean[] isReturned;`<br>`static int borrowCount;` | `private String memberId;`<br>`private String bookId;`<br>`private String borrowDate;`<br>`private boolean isReturned;` | Giao dịch nay đã trở thành một Thực thể (Entity) độc lập, chứa Foreign Keys (`memberId`, `bookId`) kết nối sang các đối tượng khác. |
| **Ghi nhận lịch sử mượn** | `borrowBookIds[borrowCount] = var2;`<br>`borrowMemberIds[borrowCount] = var0;`<br>`borrowDates[borrowCount] = var4;`<br>`isReturned[borrowCount] = false;`<br>`++borrowCount;` | `public BorrowRecord(String memberId, String bookId, String borrowDate)` | Trạng thái mặc định `isReturned = false` được set cứng trong constructor lúc khởi tạo giao dịch mới, an toàn tuyệt đối. |
| **Xác nhận trả sách** | `isReturned[var4] = true;` (Trong hàm `returnBook`) | `public void setReturned(boolean returned)` | Khi gọi `record.setReturned(true)`, trạng thái giao dịch sẽ thay đổi. |
| **Kiểm tra trạng thái (Is Active)** | Dùng phép check mảng: `!isReturned[var1]` | `public boolean isReturned()` | Thay vì gọi mảng boolean, gọi hàm getter `record.isReturned()`. Giúp code biểu đạt bằng tiếng Anh rất dễ hiểu (Self-documenting code). |

## 2. Kết luận đánh giá Class BorrowRecord
Bằng việc tạo ra Class này, hệ thống chấm dứt hoàn toàn sự nhầm lẫn giữa việc "Một cuốn sách bị xoá" hay "Một người dùng bị xoá" gây ảnh hưởng thế nào đến các mảng lịch sử. Mỗi Record là một Object độc lập, có thể lưu trữ trong file hoặc truy vấn thoải mái mà không sợ lệch dòng (index).
