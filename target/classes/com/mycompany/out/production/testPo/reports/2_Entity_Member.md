# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS MEMBER (ENTITY)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

Tương tự Book, thực thể Người dùng (Member) bị chia cắt ra thành 6 mảng tĩnh và 1 biến đếm trong `codeNoOOP.md` (từ dòng 29 đến 35). Ở bản OOP, tất cả gói gọn vào `entity/Member.java`.

| Tính năng | Mã nguồn Non-OOP (trong Main.class) | Mã nguồn OOP (Member.java) | Giải thích sự cải tiến |
| :--- | :--- | :--- | :--- |
| **Khai báo dữ liệu** | `static String[] memberIds;`<br>`static String[] memberNames;`<br>`static String[] phones;`<br>`static String[] emails;`<br>`static int[] memberCurrentBorrowCount;`<br>`static int[] memberTotalBorrowCount;`<br>`static int memberCount;` | `private String id;`<br>`private String name;`<br>`private String phone;`<br>`private String email;`<br>`private int currentBorrowCount;`<br>`private int totalBorrowCount;` | - Biến mảng tĩnh biến mất hoàn toàn.<br>- Dữ liệu cá nhân (điện thoại, email) nằm đúng vào một cụm. |
| **Khởi tạo dữ liệu** | `memberIds[memberCount] = var0;`<br>`memberNames[memberCount] = var1;`<br>`...`<br>`++memberCount;` (trong `addMember`) | `public Member(String id, String name, String phone, String email) { ... }` | - Logic khởi tạo (như set currentBorrow = 0, total = 0) được gom chặt chẽ vào Constructor. |
| **Cập nhật thông tin (Phone, Email)** | `phones[var1] = var2;`<br>`emails[var1] = var3;`<br>(trong `updateMember`) | `public void setPhone(String phone)`<br>`public void setEmail(String email)` | Thông qua Setter. |
| **Cập nhật số lượt mượn (Borrow Count)** | Trong hàm `borrowBook()`:<br>`memberCurrentBorrowCount[var1]++;`<br>`memberTotalBorrowCount[var1]++;`<br><br>Trong hàm `returnBook()`:<br>`memberCurrentBorrowCount[var1]--;` | `public void incrementCurrentBorrowCount()`<br>`public void decrementCurrentBorrowCount()`<br>`public void incrementTotalBorrowCount()` | - Hành động tăng giảm lượt mượn của thành viên được xem là "hành vi" (behavior) của thành viên đó.<br>- Hàm `decrementCurrentBorrowCount()` trong OOP có bước kiểm tra `> 0` trước khi trừ, tránh bug số lượt mượn bị âm nếu hệ thống có lỗi logic. Non-OOP trừ thẳng tay `memberCurrentBorrowCount[var1]--`. |

## 2. Kết luận đánh giá Class Member
Việc đóng gói (Encapsulation) thực thể Member cho phép thao tác dữ liệu qua Object. Đặc biệt, phân tách rõ `currentBorrowCount` (số sách ĐANG mượn) và `totalBorrowCount` (TỔNG số lượt mượn) thành 2 biến cục bộ được quản lý cẩn thận bởi các method `increment/decrement`.
