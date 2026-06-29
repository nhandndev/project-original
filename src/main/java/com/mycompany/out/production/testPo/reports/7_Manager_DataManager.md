# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS DATAMANAGER (MANAGER)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

Quản lý dữ liệu file (I/O) là phần xấu xí nhất trong Non-OOP `codeNoOOP.md` (từ dòng 830 tới dòng 984). Nó là 2 hàm tĩnh vĩ đại `loadData()` và `saveData()`, sử dụng `BufferedReader` và bắt vô vàn cái try/catch trong class giao diện chính. Bây giờ, nó đã được tách ra 1 module riêng biệt là `manager/DataManager.java`.

| Tên Hàm (Method) | Trạng thái Non-OOP (Trong `Main.java`) | Trạng thái OOP (`DataManager.java`) | Giải thích sự lột xác |
| :--- | :--- | :--- | :--- |
| **Vị trí và File đường dẫn** | `static final String FILE_BOOKS = "data/data_books.txt";` nằm rải rác trên đầu file `Main`. | `private static final String DATA_DIR = "data";` (Được đưa vào DataManager) | Cấu hình thư mục được giấu kín đi (Information Hiding). Giao diện không cần biết file nằm ở thư mục nào. |
| **Hàm `saveData()`** | (Dòng 928):<br>Duyệt qua biến `bookCount`. Sau đó nối chuỗi khổng lồ: `bookIds[var1] + "|" + titles[var1] + "|" ...` <br>Sau đó ghi vào file qua `BufferedWriter`. | `public static void saveData(BookManager bookManager...)`:<br>- Lấy danh sách List sách qua `bookManager.getAllBooks()`.<br>- Dùng `String.format()` với thông tin từ `Book` object: `b.getId()`, `b.getTitle()`. | - Ở OOP, ta gọi hàm lấy danh sách (List) trực tiếp từ các Manager. Việc dùng String.format kết hợp Object Getters giúp mã nguồn sáng sủa, dễ đọc, không bị lỗi nối chuỗi lằng nhằng (`+ "|" +`).<br>- Thêm cơ chế tự sinh thư mục `data/` nếu lỡ bị xóa (Hàm `ensureDataDirectoryExists()`). |
| **Hàm `loadData()`** | (Dòng 830):<br>- Mở file bằng `FileReader`.<br>- Tách dòng bằng `line.split("\\|")`.<br>- Gán lại vào tận 7 mảng ở vị trí `bookCount` bằng tay: `bookIds[bookCount] = var2[0]; ...`<br>- Bắt `Throwable var12`, lồng try-catch dồn dập. | `public static void loadData(...)`:<br>- Tách mảng bằng `.split()`.<br>- Khởi tạo THỰC THỂ Sách: `Book b = new Book(...)`.<br>- Gọi hàm cấp quyền: `bookManager.addLoadedBook(b);`. | - Thay vì tự đếm `bookCount` và gán thủ công rất mạo hiểm (lỡ vượt quá 100 dòng file txt là crash ngay phần mềm), phiên bản OOP khởi tạo Object và tống thẳng nó vào `ArrayList` nội tại của Manager. Java sẽ tự lo bộ nhớ `ArrayList` dù file .txt của bạn có 100 hay 100,000 dòng. |

## 2. Kết luận đánh giá Class DataManager
DataManager như một nhà xưởng: Chuyên đóng gói các "Mảng dữ liệu rời rạc từ Text" thành các "Sản phẩm Thực thể OOP (Book, Member)", sau đó điều phối xe bốc hàng đưa về các Tổng kho (`BookManager`, `MemberManager`). 

Vì cấu trúc dữ liệu đã được trừu tượng hóa, `Main.java` chỉ cần chạy đúng 2 dòng code: `loadData()` lúc vừa bật máy, và `saveData()` lúc chọn số 5 để thoát. Code trong UI Main cực kì gọn nhẹ.
