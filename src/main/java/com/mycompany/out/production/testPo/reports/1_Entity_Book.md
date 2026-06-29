# BÁO CÁO MÃ NGUỒN CHI TIẾT - CLASS BOOK (ENTITY)

## 1. Bản đồ ánh xạ (Mapping) từ Non-OOP sang OOP

Trong mô hình Non-OOP (`codeNoOOP.md`), khái niệm Sách (Book) bị vỡ vụn thành 7 mảng tĩnh và 1 biến đếm (từ dòng 21 đến 28). Toàn bộ thao tác dữ liệu được thực hiện thủ công bằng cách trỏ vào `index`. 

Sang mô hình OOP, chúng ta gộp toàn bộ thành Class `entity/Book.java`. Dưới đây là đối chiếu chi tiết:

| Tính năng | Mã nguồn Non-OOP (trong Main.class) | Mã nguồn OOP (Book.java) | Giải thích sự cải tiến |
| :--- | :--- | :--- | :--- |
| **Khai báo dữ liệu** | `static String[] bookIds;`<br>`static String[] titles;`<br>`static String[] authors;`<br>`static String[] genres;`<br>`static int[] years;`<br>`static int[] quantities;`<br>`static int[] bookBorrowCount;`<br>`static int bookCount;` | `private String id;`<br>`private String title;`<br>`private String author;`<br>`private String genre;`<br>`private int year;`<br>`private int quantity;`<br>`private int borrowCount;` | - Dữ liệu không còn là các mảng toàn cục.<br>- Tính đóng gói (Encapsulation): gom 7 thuộc tính về 1 thực thể.<br>- Biến đếm `bookCount` bị loại bỏ hoàn toàn nhờ cấu trúc danh sách động `List` ở Manager. |
| **Gán giá trị khởi tạo (Lúc thêm sách)** | `bookIds[bookCount] = var0;`<br>`titles[bookCount] = var1;`<br>`...`<br>`bookBorrowCount[bookCount] = 0;`<br>`++bookCount;` (trong `addBook`) | `public Book(String id, String title...) {`<br> `this.id = id;`<br> `this.title = title;`<br> `...`<br> `this.borrowCount = 0;`<br>`}` | Việc gán dữ liệu quy về đúng 1 hàm duy nhất là `Constructor`. Các giá trị mặc định như `borrowCount = 0` được tự động set khi khởi tạo Object, không sợ bị thiếu sót. |
| **Đọc dữ liệu sách (Read)** | Truy cập thông qua index: `bookIds[var2]`, `titles[var2]` (ví dụ trong `searchBooks`, `updateBook`) | `public String getId()`<br>`public String getTitle()`<br>`...` (các hàm Getters) | Truy cập thông qua Getters bảo vệ dữ liệu tránh bị ghi đè nhầm ở các class khác. |
| **Sửa dữ liệu sách (Update)** | `quantities[var1] = var3;` (trong `updateBook`) | `public void setQuantity(int quantity)` | Cung cấp hàm Setter có thể kiểm soát được luồng gán dữ liệu. |
| **Tăng giảm số lượng (Khi Mượn / Trả sách)** | `quantities[var3]--;`<br>`bookBorrowCount[var3]++;`<br>`quantities[var3]++;`<br>(trong `borrowBook` và `returnBook`) | `public void decreaseQuantity()`<br>`public void increaseQuantity()`<br>`public void incrementBorrowCount()` | - Chuyển giao trách nhiệm: Thay vì hàm Mượn Sách phải thao túng số lượng sách, thực thể Sách tự cung cấp method để tăng giảm số lượng của chính nó.<br>- Logic an toàn: Hàm `decreaseQuantity()` chứa logic `if (this.quantity > 0)` để đảm bảo số lượng không bao giờ bị âm. Ở Non-OOP, không có kiểm tra này lúc trừ trực tiếp. |

## 2. Kết luận đánh giá Class Book
Toàn bộ mảng rời rạc và các hành vi thao tác biến (tăng/giảm) trong `Main` đã được đưa về đúng vị trí của nó: Các phương thức nội tại của thực thể `Book`. Việc này làm tăng tính độc lập (Cohesion) và là nền tảng để xây dựng `BookManager`.
