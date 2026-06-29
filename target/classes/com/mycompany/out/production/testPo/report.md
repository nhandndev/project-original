# BÁO CÁO THUYẾT TRÌNH: DỰ ÁN QUẢN LÝ THƯ VIỆN (OOP vs NON-OOP)

---

## I. GIỚI THIỆU DỰ ÁN
Xin chào thầy cô và các bạn. Hôm nay nhóm/mình xin phép được trình bày về dự án **Hệ thống Quản lý Thư viện (Library Management System)** viết bằng ngôn ngữ Java. 
Mục tiêu của dự án là xây dựng một phần mềm console (CLI) giúp thủ thư thực hiện các công việc:
1. **Quản lý Sách**: Thêm, sửa (số lượng), xoá, xem danh sách, tìm kiếm sách.
2. **Quản lý Thành viên**: Thêm, sửa (số điện thoại, email), xoá, xem, tìm kiếm thành viên.
3. **Quản lý Mượn/Trả**: Thực hiện nghiệp vụ cho mượn sách, trả sách (tính tiền phạt tự động nếu trễ hạn), theo dõi sách đang được mượn.
4. **Báo cáo Thống kê**: Sách được mượn nhiều nhất, danh sách quá hạn, top thành viên mượn nhiều.
5. **Lưu trữ dữ liệu**: Tự động lưu và tải dữ liệu từ file văn bản (.txt) khi bật/tắt chương trình.

Điểm đặc biệt của bài báo cáo hôm nay là chúng ta sẽ cùng phân tích sự tiến hoá của mã nguồn: từ lúc **không áp dụng Lập trình hướng đối tượng (Non-OOP)** cho đến khi **được tái cấu trúc theo chuẩn OOP (Object-Oriented Programming)**.

---

## II. PHÂN TÍCH KIẾN TRÚC NON-OOP (TRƯỚC KHI CẢI TIẾN)

### 1. Cấu trúc mã nguồn
Ở phiên bản Non-OOP (như trong file `codeNoOOP`), toàn bộ chương trình chỉ nằm trong **MỘT FILE DUY NHẤT** là `Main.java` kéo dài hơn 1000 dòng code.

### 2. Cách lưu trữ dữ liệu (Parallel Arrays)
Do không có khái niệm "Đối tượng" (Object), dữ liệu của một cuốn sách bị chia cắt thành nhiều mảng tĩnh (Static Arrays) rời rạc:
```java
static String[] bookIds = new String[100];
static String[] titles = new String[100];
static String[] authors = new String[100];
// ...
static int bookCount = 0;
```
Để truy xuất thông tin của cuốn sách thứ `i`, chương trình phải gọi `bookIds[i]`, `titles[i]`, `authors[i]`. Điều tương tự cũng diễn ra với thực thể Member và BorrowRecord.

### 3. Những hạn chế lớn (Pain points)
- **Khó bảo trì và mở rộng:** Mọi thứ từ UI (in ra màn hình), xử lý logic (tính toán ngày trễ hạn), đến thao tác dữ liệu (đọc/ghi file) đều dồn hết vào các hàm `static` trong `Main`. Thay đổi một nghiệp vụ nhỏ có thể gây vỡ toàn bộ cấu trúc.
- **Dễ sai sót (Bug-prone):** Việc quản lý các mảng song song (Parallel Arrays) rất dễ dẫn đến lỗi lệch index. Khi xoá 1 cuốn sách, chúng ta phải "dịch chuyển" thủ công từng phần tử của 7 mảng khác nhau.
- **Giới hạn cứng (Hard-coded limits):** Sử dụng mảng tĩnh `[100]` khiến hệ thống báo "đầy" chỉ sau 100 dòng dữ liệu, lãng phí RAM hoặc thiếu hụt không gian lưu trữ thực tế.

---

## III. KIẾN TRÚC OOP (SAU KHI CẢI TIẾN)

Để giải quyết các vấn đề trên, dự án đã được đập đi xây lại (Refactor) áp dụng triệt để 4 tính chất của OOP và các nguyên tắc thiết kế tốt (SOLID).

### 1. Tổ chức Package rõ ràng (Separation of Concerns)
Code được chia thành các thư mục đóng vai trò cụ thể:
- **`entity` (Thực thể):** Chứa `Book`, `Member`, `BorrowRecord`. Đóng gói (Encapsulation) dữ liệu liên quan thành các Object.
- **`manager` (Nghiệp vụ):** Chứa `BookManager`, `MemberManager`, `BorrowManager` và `DataManager`. Đảm nhiệm xử lý logic kinh doanh và cấu trúc dữ liệu (`List`).
- **`Main.java` (Giao diện):** Chỉ còn đóng vai trò là View, hiển thị Menu CLI, tiếp nhận thao tác của người dùng và điều hướng (route) xuống các `Manager` thông qua cơ chế tiêm phụ thuộc (Dependency Injection).

### 2. Sự nâng cấp về Data Structure
Thay vì mảng tĩnh rời rạc, dự án sử dụng `ArrayList<Entity>`:
```java
private List<Book> books = new ArrayList<>();
```
- Khắc phục hoàn toàn lỗi `MAX_SIZE`.
- Việc xoá sách giờ chỉ đơn giản là `books.remove(b)`.
- Các hàm return về `List<Book>` rất chuẩn xác thay vì phải thao tác với biến đếm `bookCount` thủ công.

---

## IV. SO SÁNH LUỒNG THỰC THI (FLOW CODE)

Lấy ví dụ chức năng: **Thêm một cuốn sách mới (Add Book)**

### Luồng Non-OOP:
1. `Main` hỏi người dùng nhập thông tin.
2. Kiểm tra biến `bookCount` có vượt quá 100 không.
3. `Main` trực tiếp can thiệp vào bộ nhớ:
   `bookIds[bookCount] = id; titles[bookCount] = title; ...`
4. `Main` tăng `bookCount++`.
> **Nhận xét:** Lớp giao diện (Main) kiêm luôn việc cấp phát bộ nhớ và quản lý cấu trúc dữ liệu. Vi phạm Single Responsibility Principle (SRP).

### Luồng OOP hiện tại:
1. `Main` chỉ làm nhiệm vụ hỏi input (ID, Title, Author...).
2. `Main` gọi hàm: `bookManager.addBook(id, title, author...);`
3. Trong `BookManager`, hệ thống tự kiểm tra logic nghiệp vụ (id trống, id trùng).
4. `BookManager` khởi tạo Object: `new Book(...)` và ném vào danh sách: `books.add(...)`.
5. `BookManager` trả về `String` thông báo kết quả. `Main` in thông báo này ra màn hình.
> **Nhận xét:** Luồng đi rành mạch. UI tách biệt với Logic. Lớp nào làm việc của lớp đó. 

---

## V. TỔNG KẾT VÀ BÀI HỌC KINH NGHIỆM

Thông qua quá trình nâng cấp từ mã nguồn thuần cấu trúc (Procedural/Non-OOP) lên Hướng đối tượng, chúng ta có thể rút ra những bài học rõ ràng về sức mạnh của OOP:
1. **Tính đóng gói (Encapsulation)** giúp bảo vệ dữ liệu an toàn, các field như `borrowCount` được chỉnh sửa thông qua `getter/setter`.
2. Sử dụng **ArrayList** kết hợp với **Object** dẹp bỏ hoàn toàn nỗi ác mộng quản lý index bằng tay.
3. Việc đọc ghi file (IO) được gom vào lớp `DataManager`, việc xử lý mượn trả gom vào `BorrowManager` giúp code dễ bảo trì. Nếu sau này cần chuyển từ lưu file `.txt` sang Database `SQL`, chúng ta chỉ cần sửa đúng file `DataManager` mà không làm ảnh hưởng đến bất kỳ file nào khác trong hệ thống.

**Cảm ơn thầy cô và các bạn đã lắng nghe. Mình/Nhóm xin phép kết thúc phần trình bày và chuyển sang demo chạy thử phần mềm!**
