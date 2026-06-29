# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS MEMBERMANAGER (MANAGER)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

Ở Non-OOP (`codeNoOOP.md`), phần Member nằm từ dòng 317 đến dòng 469 trong file `Main`. Khắc phục tình trạng "Viết gộp", hệ thống mới chuyển logic qua `manager/MemberManager.java`.

| Tên Hàm (Method) | Trạng thái Non-OOP (Trong `Main.java`) | Trạng thái OOP (`MemberManager.java`) | Giải thích sự lột xác |
| :--- | :--- | :--- | :--- |
| **Tìm Member bằng ID** | `static int findMemberIndex(String var0)` | `public Member getMemberById(String id)` | Không dùng kiểu trả về `int` (index). Trả về nguyên một Thực thể `Member` để tiện thao tác về sau. |
| **Thêm Member** | `static void addMember()` (Dòng 352):<br>- Khá rườm rà vì dính tới `Scanner` và gán 6 mảng.<br>- Có logic giới hạn `memberCount >= 100`. | `public String addMember(...)`:<br>- `members.add(new Member(id, name, phone, email))` | Khởi tạo qua Constructor, không phải set `currentBorrowCount = 0` thủ công vì `Member` class đã tự làm việc đó khi khởi tạo. |
| **Cập nhật Member** | `static void updateMember()` (Dòng 390):<br>- Ép nhập `Scanner` phone, email.<br>- Check `isEmpty` rồi gán mảng. | `public String updateMember(...)`:<br>- `m.setPhone()`, `m.setEmail()` | Tương tự BookManager, logic này dùng các hàm Setters của `Member` object. |
| **Xóa Member** | `static void removeMember()` (Dòng 430):<br>- Lại phải dùng vòng lặp `for` lùi, chép đè mảng để xóa `memberIds[var3] = memberIds[var3 + 1];...` (Shift array). | `public String removeMember(String id)`:<br>- `members.remove(m);` | Giải phóng bộ nhớ nhanh gọn với hàm `.remove()` của `List`. Tránh tuyệt đối lỗi rác bộ nhớ (Memory Leak) do quên trừ biến `memberCount`. |
| **Hiển thị Members** | `static void viewMembers()` (Dòng 467) | `public List<Member> getAllMembers()` | Trả list để giao diện `Main` tự in. |
| **Tìm kiếm Members** | `static void searchMembers()` (Dòng 482) | `public List<Member> searchMembers(String keyword)` | Tìm theo regex/chuỗi và trả List. |
| **Thống kê Mượn sách** | `static void viewMembersBorrowingCount()` (Dòng 663):<br>- Dùng Bubble Sort và tạo mảng index trung gian. | `public List<Member> getMembersBorrowingCount()`:<br>- Dùng `sorted.sort()` và Comparator của List. | Cắt giảm lượng code từ 30 dòng (Non-OOP) xuống còn đúng 4 dòng (OOP). |

## 2. Kết luận đánh giá Class MemberManager
Cũng như `BookManager`, `MemberManager` đạt được nguyên tắc "Single Responsibility Principle" (SRP). Nó chỉ quan tâm đến danh sách `members` bên trong nó, mọi hành động điều chỉnh dữ liệu thành viên đều phải đi qua cái phễu Manager này, không có ngoại lệ. Tình trạng loạn biến mảng `static` của Non-OOP đã được khai tử.
