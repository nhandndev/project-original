# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS MAIN (GIAO DIỆN / ROUTER)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

`Main.java` của `codeNoOOP.md` là "Con Quái Vật" 1086 dòng code, ôm tất cả mọi trách nhiệm (God Class). Sang phiên bản OOP, `Main.java` quay về đúng vị trí nhỏ bé của nó: Làm View (Hiển thị) và Controller (Điều phối).

| Tên Hàm (Method) | Trạng thái Non-OOP (Trong `Main.java`) | Trạng thái OOP (`Main.java`) | Giải thích sự lột xác |
| :--- | :--- | :--- | :--- |
| **Khai báo biến** | Dòng 13 - 42: `static String[] bookIds; static int bookCount; static Scanner sc; ...` (Gần 30 biến mảng tĩnh) | `private BookManager bookManager;`<br>`private MemberManager memberManager;`<br>`private BorrowManager borrowManager;` | Xóa sổ 100% mảng toàn cục. `Main` hiện tại CHỈ LÀ TRẠM TRUNG CHUYỂN, nó yêu cầu 3 ông Quản lý (Manager) bằng mô hình Dependency Injection (Tiêm phụ thuộc). |
| **Hàm `main(String[] args)`** | Gọi `loadData()` rồi mở vòng lặp `switch-case`. | - Khởi tạo 3 Object Managers: `new BookManager()`...<br>- Gọi `DataManager.loadData(bm, mm, bom)`.<br>- Khởi tạo `app = new Main(...)`. Chạy `app.run()`. | Hàm `main` chỉ đóng vai trò Setup mô hình (Khởi tạo App) sau đó bàn giao luồng chạy (thread) cho hàm `run()`. Kỹ thuật này chuẩn mực và chuyên nghiệp, tránh việc phải dùng lệnh `static` khắp mọi nơi. |
| **Các menu UI (`manageBooks`, `manageMembers`...)** | Chứa logic menu. | Vẫn giữ nguyên logic vòng lặp UI (Switch-case). | Menu Console thì cách chạy vòng lặp do-while luôn là tối ưu. |
| **Chức năng chi tiết (`addBook`, `removeMember`...)** | Chứa code System.out VÀ chứa code gán `array[count] = ...` kết hợp ép kiểu toán học, xử lý Date time trực tiếp. | - Hỏi input người dùng (Scanner).<br>- Trực tiếp gọi hàm `Manager`: `String result = bookManager.addBook(id, title...);`<br>- `System.out.println(result);` | - Trách nhiệm được giao phó toàn bộ về phía Back-end (Managers). Lớp `Main` không cần suy nghĩ gì đến cấu trúc dữ liệu, không cần ép kiểu khó chịu. Nếu sau này ta vứt bỏ console để code Web bằng SpringBoot/React, ta chỉ cần bỏ file `Main.java` này và nối API thẳng vào `Managers`. Logic không mất 1 dòng nào! |
| **Helper Methods (`readInt`)** | `static int readInt(String var0)` | `private int readInt(String message)` | Đổi thành hàm private của nội bộ Object `Main`, từ bỏ từ khoá `static`. |

## 2. Tổng kết cuối cùng về Dự án OOP
Bằng việc rã đông (de-coupling) 1086 dòng code Non-OOP, chúng ta đã kiến tạo thành công một phần mềm Thư viện (Library System) chia theo các Layer. 
- Entity: Khu vực xác định thực thể.
- Manager: Khu vực làm việc của não bộ (Business Logic).
- DataManager: Lớp kết nối IO ổ cứng.
- Main: Lớp tiếp tân (Giao tiếp người dùng).

Kiến trúc này cho thấy sự trưởng thành rõ rệt trong tư duy thiết kế phần mềm (Software Engineering). Báo cáo chi tiết trên từng class xin được phép kết thúc tại đây! Cảm ơn thầy cô và các bạn đã lắng nghe và cùng phân tích code!
