# 📚 BÁO CÁO TOÀN DIỆN: HỆ THỐNG QUẢN LÝ THƯ VIỆN (OOP)

---

## 📋 MỤC LỤC
1. [Tổng Quan Dự Án](#tổng-quan-dự-án)
2. [Kiến Trúc Hệ Thống](#kiến-trúc-hệ-thống)
3. [Luồng Hoạt Động Chính (Main Flow)](#luồng-hoạt-động-chính)
4. [Chi Tiết Từng Class](#chi-tiết-từng-class)
5. [Các Use Case Chính](#các-use-case-chính)
6. [So Sánh Non-OOP vs OOP](#so-sánh-non-oop-vs-oop)
7. [Kết Luận](#kết-luận)

---

## 🎯 Tổng Quan Dự Án

### Mục Đích
Xây dựng một **hệ thống quản lý thư viện (Library Management System)** trên nền tảng Java console (CLI) giúp thủ thư:
- ✅ Quản lý Sách (CRUD, tìm kiếm, sắp xếp)
- ✅ Quản lý Thành viên (CRUD, cập nhật thông tin)
- ✅ Quản lý Mượn/Trả sách (tính tiền phạt tự động)
- ✅ Báo cáo thống kê (sách phổ biến, quá hạn, top thành viên)
- ✅ Lưu trữ dữ liệu (tự động lưu file .txt)

### Đặc Điểm Nổi Bật
- **Từ Non-OOP → OOP:** Dự án được refactor từ 1,000+ dòng code Single File sang kiến trúc hợp lý 
- **Separation of Concerns:** Mỗi class có trách nhiệm rõ ràng
- **Dependency Injection:** Tránh Coupling quá chặt chẽ
- **ArrayList thay Mảng tĩnh:** Không giới hạn dung lượng

---

## 🏗️ Kiến Trúc Hệ Thống

### Cấu Trúc Thư Mục
```
testPo/
├── entity/                    # 🟢 ENTITY LAYER (Thực thể dữ liệu)
│   ├── Book.java             # Đại diện cho 1 cuốn sách
│   ├── Member.java           # Đại diện cho 1 thành viên thư viện
│   └── BorrowRecord.java     # Đại diện cho 1 lần mượn sách
│
├── manager/                   # 🔵 BUSINESS LOGIC LAYER (Quản lý)
│   ├── BookManager.java      # Quản lý danh sách sách
│   ├── MemberManager.java    # Quản lý danh sách thành viên
│   ├── BorrowManager.java    # Quản lý lịch sử mượn/trả
│   └── DataManager.java      # Quản lý file I/O (lưu/tải dữ liệu)
│
├── data/                      # 📁 DATA FILES
│   ├── books.txt             # Danh sách sách
│   ├── members.txt           # Danh sách thành viên
│   └── borrows.txt           # Lịch sử mượn/trả
│
├── Main.java                  # 🔴 PRESENTATION LAYER (Giao diện)
├── report.md                  # Báo cáo tổng quan
├── reports/                   # Báo cáo chi tiết từng class
│   ├── 1_Entity_Book.md
│   ├── 2_Entity_Member.md
│   ├── 3_Entity_BorrowRecord.md
│   ├── 4_Manager_BookManager.md
│   ├── 5_Manager_MemberManager.md
│   ├── 6_Manager_BorrowManager.md
│   ├── 7_Manager_DataManager.md
│   └── 8_Main.md
└── codeNoOOP.md              # Code cũ (trước khi refactor)
```

### Sơ Đồ Kiến Trúc Hệ Thống

```
┌─────────────────────────────────────────────────────────────┐
│           🟠 PRESENTATION LAYER: Main.java                  │
│  (Giao diện CLI, Menu, Nhân Input từ User)                  │
└────────────────────┬──────────────────────────────────────┘
                     │ Gọi hàm của Managers
                     ▼
        ┌────────────────────────────┐
        │  🔵 BUSINESS LOGIC LAYER    │
        │      (Manager Classes)      │
        ├────────────────────────────┤
        │ • BookManager              │
        │ • MemberManager            │
        │ • BorrowManager            │
        │ • DataManager              │
        └────────────────┬────────────┘
                         │ Quản lý + Thao tác Objects
                         ▼
        ┌────────────────────────────┐
        │  🟢 ENTITY LAYER            │
        │   (Domain Objects)          │
        ├────────────────────────────┤
        │ • Book (private fields)     │
        │ • Member (private fields)   │
        │ • BorrowRecord (getters)    │
        └────────────────┬────────────┘
                         │ ArrayList<Entity>
                         ▼
        ┌────────────────────────────┐
        │  📁 DATA LAYER              │
        │  (File I/O: .txt files)     │
        └────────────────────────────┘
```

---

## 🔄 Luồng Hoạt Động Chính

### Quy Trình Khởi Động Ứng Dụng

```
1. JVM chạy: java Main
   ↓
2. main() method thực thi:
   - Khởi tạo BookManager()
   - Khởi tạo MemberManager()
   - Khởi tạo BorrowManager(bookManager, memberManager)
   ↓
3. DataManager.loadData() ← Tải dữ liệu từ file .txt
   - Đọc file books.txt → Khởi tạo ArrayList<Book>
   - Đọc file members.txt → Khởi tạo ArrayList<Member>
   - Đọc file borrows.txt → Khởi tạo ArrayList<BorrowRecord>
   ↓
4. new Main(managers) → Dependency Injection
   ↓
5. app.run() → Vòng lặp Menu CLI chính
   ↓
6. Người dùng chọn thoát (option 5)
   ↓
7. DataManager.saveData() ← Lưu dữ liệu vào file .txt
   ↓
8. Chương trình kết thúc
```

### Sơ Đồ Interaction Giữa Các Class

```
┌─────────────┐
│Main (View)  │
│             │
│ - Menu CLI  │
│ - Input UI  │
└──────┬──────┘
       │ Gọi các manager
       │
   ┌───┴────────────────┬──────────────┬──────────────┐
   │                    │              │              │
   ▼                    ▼              ▼              ▼
┌──────────────┐ ┌────────────┐ ┌──────────┐ ┌────────────────┐
│BookManager   │ │Member      │ │Borrow    │ │DataManager     │
│              │ │Manager     │ │Manager   │ │                │
│- List<Book>  │ │            │ │          │ │- Load file     │
│- CRUD Book   │ │- List<Mem> │ │- List    │ │- Save file     │
│              │ │- CRUD Mem  │ │  BRecord │ │                │
└──────┬───────┘ └──────┬─────┘ │          │ └────────────────┘
       │                │       │   Dependency  
       │                │       │   Injection
       └────────────────┴───────┘
              │
              │ Sử dụng
              ▼
        ┌──────────────────┐
        │   Entity Layer   │
        ├──────────────────┤
        │ - Book objects   │
        │ - Member objects │
        │ - BorrowRecord   │
        │   objects        │
        └──────────────────┘
```

---

## 📦 Chi Tiết Từng Class

### 1️⃣ ENTITY LAYER (Lớp Thực Thể)

#### **Book.java** - Đại Diện Một Cuốn Sách

**Chức Năng:**
- Lưu trữ thông tin về một cuốn sách
- Quản lý trạng thái của cuốn sách (số lượng, lượt mượn)

**Các Thuộc Tính (Fields):**
```java
private String id;              // ID sách (unique)
private String title;           // Tiêu đề
private String author;          // Tác giả
private String genre;           // Thể loại
private int year;              // Năm xuất bản
private int quantity;          // Số lượng sách còn lại
private int borrowCount;       // Tổng số lần đã được mượn
```

**Các Hàm Quan Trọng:**
```java
// Constructor
public Book(String id, String title, String author, String genre, int year, int quantity)

// Getters (Đọc dữ liệu)
public String getId()
public String getTitle()
public int getQuantity()
public int getBorrowCount()
// ... các getter khác

// Setters (Ghi dữ liệu)
public void setQuantity(int quantity)

// Business Methods (Hành động của sách)
public void decreaseQuantity()     // Mượn sách: trừ số lượng
public void increaseQuantity()     // Trả sách: cộng số lượng
public void incrementBorrowCount() // Tăng lượt mượn
```

**Ví Dụ Sử Dụng:**
```java
// Tạo sách mới
Book b = new Book("BK001", "Clean Code", "Robert C. Martin", "Programming", 2008, 5);

// Lấy thông tin
System.out.println(b.getTitle());      // Output: Clean Code
System.out.println(b.getQuantity());   // Output: 5

// Mượn sách
b.decreaseQuantity();                  // Số lượng: 5 → 4
b.incrementBorrowCount();              // Lượt mươn: 0 → 1

// Trả sách
b.increaseQuantity();                  // Số lượng: 4 → 5
```

---

#### **Member.java** - Đại Diện Một Thành Viên

**Chức Năng:**
- Lưu trữ thông tin về một thành viên thư viện
- Quản lý lịch sử mượn sách của thành viên

**Các Thuộc Tính:**
```java
private String id;                  // ID thành viên
private String name;                // Tên thành viên
private String phone;               // Số điện thoại
private String email;               // Email
private int currentBorrowCount;    // Số sách ĐANG mượn
private int totalBorrowCount;      // TỔNG lần mượn (từ trước đến nay)
```

**Các Hàm Quan Trọng:**
```java
// Constructor
public Member(String id, String name, String phone, String email)

// Getters
public String getId()
public String getName()
public int getCurrentBorrowCount()
public int getTotalBorrowCount()

// Setters
public void setPhone(String phone)
public void setEmail(String email)

// Business Methods
public void incrementCurrentBorrowCount()   // Mượn sách
public void decrementCurrentBorrowCount()   // Trả sách
public void incrementTotalBorrowCount()     // Tính tổng lượt mượn
```

---

#### **BorrowRecord.java** - Đại Diện Một Lần Mượn Sách

**Chức Năng:**
- Ghi lại thông tin một lần mượn sách
- Theo dõi thời hạn mượn và tính tiền phạt

**Các Thuộc Tính:**
```java
private String id;              // ID ghi nhận mượn
private String bookId;          // ID cuốn sách mượn
private String memberId;        // ID thành viên mượn
private LocalDate borrowDate;   // Ngày mượn
private LocalDate dueDate;      // Ngày hết hạn
private boolean isReturned;     // Đã trả chưa?
private LocalDate returnDate;   // Ngày trả (nếu có)
private long fine;              // Tiền phạt (nếu trễ hạn)
```

**Các Hàm Quan Trọng:**
```java
// Constructor
public BorrowRecord(String id, String bookId, String memberId, LocalDate borrowDate, LocalDate dueDate)

// Getters
public String getBookId()
public String getMemberId()
public LocalDate getBorrowDate()
public boolean isReturned()
public long getFine()

// Setters
public void setReturned(boolean returned)
public void setReturnDate(LocalDate returnDate)
public void setFine(long fine)
```

---

### 2️⃣ BUSINESS LOGIC LAYER (Lớp Quản Lý)

#### **BookManager.java** - Quản Lý Danh Sách Sách

**Chức Năng:**
- Quản lý `ArrayList<Book>` toàn bộ sách
- Thực hiện CRUD (Create, Read, Update, Delete)
- Tìm kiếm, sắp xếp sách

**Cấu Trúc Dữ Liệu:**
```java
private List<Book> books = new ArrayList<>();  // Danh sách sách
```

**Các Hàm Chính:**

| Hàm | Chức Năng |
|-----|-----------|
| `addBook(String id, ...)` | Thêm sách mới |
| `getBookById(String id)` | Tìm sách bằng ID |
| `getAllBooks()` | Lấy danh sách tất cả sách |
| `removeBook(String id)` | Xoá sách |
| `updateBookQuantity(...)` | Cập nhật số lượng |
| `searchBooks(String keyword)` | Tìm kiếm sách theo tên |
| `getPopularBooksSimple()` | Lấy sách phổ biến nhất |
| `addLoadedBook(Book book)` | Thêm sách từ file (DataManager gọi) |

**Ví Dụ Sử Dụng:**
```java
BookManager bm = new BookManager();

// Thêm sách
String result = bm.addBook("BK001", "Clean Code", "Robert C. Martin", "Programming", 2008, 5);
System.out.println(result);  // Output: Book added successfully!

// Tìm sách
Book book = bm.getBookById("BK001");
System.out.println(book.getTitle());  // Output: Clean Code

// Tìm kiếm theo từ khóa
List<Book> results = bm.searchBooks("Clean");

// Lấy sách phổ biến
List<Book> popular = bm.getPopularBooksSimple();
```

---

#### **MemberManager.java** - Quản Lý Danh Sách Thành Viên

**Chức Năng:**
- Quản lý `ArrayList<Member>` toàn bộ thành viên
- CRUD Member
- Tìm kiếm, sắp xếp thành viên

**Cấu Trúc Dữ Liệu:**
```java
private List<Member> members = new ArrayList<>();
```

**Các Hàm Chính:**

| Hàm | Chức Năng |
|-----|-----------|
| `addMember(String id, ...)` | Thêm thành viên |
| `getMemberById(String id)` | Tìm thành viên theo ID |
| `getAllMembers()` | Lấy danh sách tất cả |
| `removeMember(String id)` | Xoá thành viên |
| `updateMember(String id, ...)` | Cập nhật thông tin |
| `searchMembers(String keyword)` | Tìm kiếm thành viên |
| `getTopBorrowersSimple()` | Lấy top thành viên mượn nhiều |
| `addLoadedMember(Member m)` | Thêm thành viên từ file |

---

#### **BorrowManager.java** - Quản Lý Mượn/Trả Sách

**Chức Năng:**
- Quản lý `ArrayList<BorrowRecord>` lịch sử mượn/trả
- Xử lý logic mượn sách (Borrow)
- Xử lý logic trả sách + tính phạt (Return)
- Báo cáo sách đang mượn, sách quá hạn

**Dependency Injection:**
```java
private BookManager bookManager;      // Để lấy/sửa thông tin sách
private MemberManager memberManager;  // Để lấy/sửa thông tin thành viên
private List<BorrowRecord> borrowRecords = new ArrayList<>();

public BorrowManager(BookManager bookManager, MemberManager memberManager) {
    this.bookManager = bookManager;
    this.memberManager = memberManager;
}
```

**Các Hàm Chính:**

| Hàm | Chức Năng |
|-----|-----------|
| `borrowBook(String memberId, String bookId, String borrowDate, String dueDate)` | Mượn sách |
| `returnBook(String borrowRecordId, String returnDate)` | Trả sách + tính phạt |
| `isBookCurrentlyBorrowed(String bookId)` | Kiểm tra sách đang được mượn không? |
| `getCurrentlyBorrowedRecords()` | Lấy sách đang mượn |
| `getOverdueBooksReport(String currentDate)` | Lấy sách quá hạn |
| `addLoadedBorrowRecord(BorrowRecord r)` | Thêm từ file |

**Luồng Mượn Sách:**
```
borrowBook(memberId="M001", bookId="BK001", borrowDate="2024-01-01", dueDate="2024-01-15")
  │
  ├─► Kiểm tra Member có tồn tại? → book = memberManager.getMemberById(memberId)
  │
  ├─► Kiểm tra Book có tồn tại? → book = bookManager.getBookById(bookId)
  │
  ├─► Kiểm tra Book còn số lượng? → book.getQuantity() > 0?
  │
  ├─► Tạo BorrowRecord mới → new BorrowRecord(...)
  │
  ├─► Cập nhật Sách:
  │   • book.decreaseQuantity()    (Trừ số lượng)
  │   • book.incrementBorrowCount() (Tăng lượt mượn)
  │
  ├─► Cập nhật Thành viên:
  │   • member.incrementCurrentBorrowCount()
  │   • member.incrementTotalBorrowCount()
  │
  └─► Thêm vào borrowRecords
      borrowRecords.add(borrowRecord)
```

**Luồng Trả Sách:**
```
returnBook(borrowRecordId="BR001", returnDate="2024-01-15")
  │
  ├─► Tìm BorrowRecord → findActiveBorrowRecord(borrowRecordId)
  │
  ├─► Tính tiền phạt:
  │   • Nếu returnDate > dueDate:
  │       fine = (returnDate - dueDate) * finePerDay
  │   • Nếu không: fine = 0
  │
  ├─► Cập nhật BorrowRecord:
  │   • record.setReturned(true)
  │   • record.setReturnDate(returnDate)
  │   • record.setFine(fine)
  │
  ├─► Cập nhật Sách:
  │   • book.increaseQuantity()  (Cộng số lượng)
  │
  ├─► Cập nhật Thành viên:
  │   • member.decrementCurrentBorrowCount()
  │
  └─► Trả về String kết quả + tiền phạt
```

---

#### **DataManager.java** - Quản Lý File I/O

**Chức Năng:**
- Tải dữ liệu từ file .txt khi khởi động
- Lưu dữ liệu vào file .txt khi thoát

**Các File Dữ Liệu:**
```
data/
├── books.txt      # Format: id|title|author|genre|year|quantity|borrowCount
├── members.txt    # Format: id|name|phone|email|currentBorrow|totalBorrow
└── borrows.txt    # Format: id|bookId|memberId|borrowDate|dueDate|isReturned|returnDate|fine
```

**Các Hàm Chính:**

| Hàm | Chức Năng |
|-----|-----------|
| `loadData(BookManager bm, MemberManager mm, BorrowManager bom)` | Tải dữ liệu từ file |
| `saveData(BookManager bm, MemberManager mm, BorrowManager bom)` | Lưu dữ liệu vào file |
| `ensureDataDirectoryExists()` | Kiểm tra/Tạo thư mục data/ |

**Luồng Load Dữ Liệu:**
```
loadData()
  │
  ├─► loadBooks():
  │   • Mở file books.txt
  │   • Duyệt từng dòng:
  │     - Tách chuỗi bằng split("|")
  │     - Khởi tạo Book object
  │     - bookManager.addLoadedBook(book)
  │
  ├─► loadMembers():
  │   • Tương tự với members.txt
  │   • Khởi tạo Member object
  │   • memberManager.addLoadedMember(member)
  │
  └─► loadBorrowRecords():
      • Tương tự với borrows.txt
      • Khởi tạo BorrowRecord object
      • borrowManager.addLoadedBorrowRecord(record)
```

**Luồng Save Dữ Liệu:**
```
saveData()
  │
  ├─► ensureDataDirectoryExists()
  │   • Kiểm tra thư mục "data/" tồn tại?
  │   • Nếu không → Tạo mới
  │
  ├─► saveBooks():
  │   • Lấy danh sách: bookManager.getAllBooks()
  │   • Duyệt từng Book:
  │     - Tạo chuỗi: String.format("%s|%s|%s|...", b.getId(), b.getTitle(), ...)
  │     - Ghi vào books.txt
  │
  ├─► saveMembers():
  │   • Tương tự với members.txt
  │
  └─► saveBorrowRecords():
      • Tương tự với borrows.txt
```

---

### 3️⃣ PRESENTATION LAYER (Lớp Giao Diện)

#### **Main.java** - Giao Diện CLI

**Chức Năng:**
- Menu CLI chính (5 chức năng chính)
- Tiếp nhận input từ người dùng
- Gọi các Manager để xử lý logic
- Hiển thị kết quả cho người dùng

**Dependency Injection:**
```java
private BookManager bookManager;
private MemberManager memberManager;
private BorrowManager borrowManager;

public Main(BookManager bookManager, MemberManager memberManager, BorrowManager borrowManager) {
    this.bookManager = bookManager;
    this.memberManager = memberManager;
    this.borrowManager = borrowManager;
}
```

**Cấu Trúc Main Menu:**
```
╔══════════════════════════════════════════════════╗
║     📚 LIBRARY MANAGEMENT SYSTEM (OOP)          ║
╠══════════════════════════════════════════════════╣
║  1. Manage Books (Quản lý sách)                 ║
║  2. Manage Members (Quản lý thành viên)         ║
║  3. Borrow/Return Books (Mượn/Trả sách)         ║
║  4. View Reports (Xem báo cáo thống kê)         ║
║  5. Exit (Thoát chương trình)                   ║
╚══════════════════════════════════════════════════╝
```

**Luồng Của Main.run():**
```
run()
  │
  └─► do-while loop:
      1. Hiển thị menu
      2. Nhận lựa chọn từ user
      3. switch(choice):
         │
         ├─ Case 1: manageBooks()
         │  └─► Sub-menu:
         │      - Add Book → Scanner input → bookManager.addBook(...)
         │      - Remove Book → Scanner input → bookManager.removeBook(...)
         │      - Update Book → Scanner input → bookManager.updateBookQuantity(...)
         │      - View All Books → bookManager.getAllBooks() → In table
         │      - Search Books → Scanner input → bookManager.searchBooks(...) → In kết quả
         │      - View Popular Books → bookManager.getPopularBooksSimple() → In table
         │
         ├─ Case 2: manageMembers()
         │  └─► Sub-menu: (Tương tự Books)
         │
         ├─ Case 3: borrowReturnBooks()
         │  └─► Sub-menu:
         │      - Borrow Book → Scanner input → borrowManager.borrowBook(...) → In kết quả
         │      - Return Book → Scanner input → borrowManager.returnBook(...) → In kết quả + phạt
         │      - View Borrowed Books → borrowManager.getCurrentlyBorrowedRecords() → In table
         │      - View Overdue Books → borrowManager.getOverdueBooksReport(...) → In table
         │
         ├─ Case 4: viewReports()
         │  └─► Hiển thị các báo cáo thống kê
         │
         └─ Case 5: Exit
            └─► Thoát vòng lặp
      
      4. Nếu choice != 5: Quay lại bước 1
```

**Ví Dụ: Thêm Sách Qua Main**
```
User: Chọn "1. Manage Books" → Chọn "Add Book"
  │
  Main: Hiển thị prompt:
        "Enter Book ID: "
  User: Nhập "BK001"
  │
  Main: Hiển thị prompt:
        "Enter Book Title: "
  User: Nhập "Clean Code"
  │
  Main: ... (tiếp tục nhập author, genre, year, quantity)
  │
  Main: Gọi bookManager.addBook("BK001", "Clean Code", ...)
  BookManager: Xử lý logic, khởi tạo Book, add vào ArrayList
  │
  BookManager: Return "Book added successfully!"
  Main: System.out.println("Book added successfully!")
  │
  User: Xem thông báo thành công
```

---

## 🎬 Các Use Case Chính

### Use Case 1: Mượn Sách

**Kịch Bản:**
- Thành viên M001 muốn mượn sách BK001 từ 01/01/2024 đến 15/01/2024

**Các Bước Thực Hiện:**

```
1. Main hiển thị Menu → User chọn "3. Borrow/Return Books"
2. Main hiển thị Sub-menu → User chọn "1. Borrow Book"
3. Main hỏi:
   - "Enter Member ID: " → M001
   - "Enter Book ID: " → BK001
   - "Enter Borrow Date (yyyy-MM-dd): " → 2024-01-01
   - "Enter Due Date (yyyy-MM-dd): " → 2024-01-15
4. Main gọi: borrowManager.borrowBook("M001", "BK001", "2024-01-01", "2024-01-15")
5. BorrowManager thực hiện:
   - Tìm Member: member = memberManager.getMemberById("M001")
   - Tìm Book: book = bookManager.getBookById("BK001")
   - Kiểm tra Book có số lượng: book.getQuantity() > 0? ✓
   - Tạo BorrowRecord: new BorrowRecord("BR001", "BK001", "M001", ...)
   - Cập nhật Book:
     • book.decreaseQuantity()      // 5 → 4
     • book.incrementBorrowCount()   // 10 → 11
   - Cập nhật Member:
     • member.incrementCurrentBorrowCount()  // 0 → 1
     • member.incrementTotalBorrowCount()    // 2 → 3
   - borrowRecords.add(record)
   - Return: "Book borrowed successfully!"
6. Main: System.out.println("Book borrowed successfully!")
```

---

### Use Case 2: Trả Sách (Có Phạt)

**Kịch Bản:**
- Thành viên M001 trả sách BK001 ngày 20/01/2024 (quá hạn 5 ngày)
- Tiền phạt: 5 ngày × 10,000 VND/ngày = 50,000 VND

**Các Bước Thực Hiện:**

```
1. Main → User chọn "3. Borrow/Return Books" → "2. Return Book"
2. Main hỏi:
   - "Enter Borrow Record ID: " → BR001
   - "Enter Return Date (yyyy-MM-dd): " → 2024-01-20
3. Main gọi: borrowManager.returnBook("BR001", "2024-01-20")
4. BorrowManager thực hiện:
   - Tìm BorrowRecord: record = findActiveBorrowRecord("BR001")
   - Tính tiền phạt:
     • dueDate = 2024-01-15
     • returnDate = 2024-01-20
     • days overdue = 5
     • fine = 5 × 10000 = 50000
   - Cập nhật BorrowRecord:
     • record.setReturned(true)
     • record.setReturnDate("2024-01-20")
     • record.setFine(50000)
   - Tìm Book & Thành viên từ record
   - Cập nhật Book:
     • book.increaseQuantity()  // 4 → 5
   - Cập nhật Member:
     • member.decrementCurrentBorrowCount()  // 1 → 0
   - Return: "Book returned successfully! Fine: 50000"
5. Main: System.out.println("Book returned successfully! Fine: 50000")
```

---

### Use Case 3: Xem Báo Cáo Sách Phổ Biến

**Kịch Bản:**
- User muốn xem danh sách sách được mượn nhiều nhất

**Các Bước Thực Hiện:**

```
1. Main → User chọn "4. View Reports"
2. Main gọi: bookManager.getPopularBooksSimple()
3. BookManager thực hiện:
   - Lấy danh sách: books
   - Sắp xếp theo borrowCount giảm dần (sử dụng .sort())
   - Return List<Book> đã sắp xếp
4. Main hiển thị table:
   ╔════════════════════════════════════════════════════════╗
   ║ Popular Books (Most Borrowed)                          ║
   ╠════════════════════════════════════════════════════════╣
   ║ ID      │ Title           │ Author           │ Borrow  ║
   ╠════════════════════════════════════════════════════════╣
   ║ BK001   │ Clean Code      │ Robert Martin    │ 15      ║
   ║ BK002   │ Design Patterns │ Gang of Four     │ 12      ║
   ║ BK003   │ Java Concur...  │ Brian Goetz      │ 10      ║
   ╚════════════════════════════════════════════════════════╝
```

---

## 📊 So Sánh Non-OOP vs OOP

### Cấu Trúc Dữ Liệu

| Aspect | Non-OOP | OOP |
|--------|---------|-----|
| **Khai Báo** | `static String[] bookIds;`<br>`static String[] titles;`<br>... (7 mảng) | `private List<Book> books;`<br>(1 class, 1 ArrayList) |
| **Giới Hạn Dung Lượng** | `[100]` (Cố định) | Không giới hạn (ArrayList tự scale) |
| **Xoá Dữ Liệu** | Phải dịch chuyển thủ công từng mảng (Shifting) | `books.remove(b)` (Tự động) |
| **Tìm Dữ Liệu** | Trả về `int` index | Trả về `Book` object |
| **Risk** | Dễ sai index (Off-by-one errors) | Type-safe |

---

### Tổ Chức Mã Nguồn

| Aspect | Non-OOP | OOP |
|--------|---------|-----|
| **File** | Tất cả trong 1 `Main.java` (1086 dòng) | Chia thành 7 file riêng biệt |
| **Logic** | Lẫn lộn UI, Business, Data | Tách rõ: Entity, Manager, Main |
| **Scanner** | Toàn bộ code chứa `Scanner` và `System.out` | Chỉ trong `Main.java` |
| **Tính Bảo Trì** | Khó (Thay đổi nhỏ ảnh hưởng rộng) | Dễ (Thay đổi riêng biệt) |

---

### Ví Dụ: Thêm Sách

**Non-OOP (Trong Main.java):**
```java
static void addBook() {
    if (bookCount >= 100) {
        System.out.println("Error: Maximum books reached!");
        return;
    }
    
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Book ID: ");
    String id = sc.nextLine();
    
    System.out.print("Enter Title: ");
    String title = sc.nextLine();
    
    // ... input khác
    
    // GÁN TRỰC TIẾP VÀO MẢNG
    bookIds[bookCount] = id;
    titles[bookCount] = title;
    authors[bookCount] = author;
    // ... gán khác
    bookBorrowCount[bookCount] = 0;
    
    ++bookCount;
    System.out.println("Book added!");
}
```

**OOP (Tách thành 2 phần):**

1. **Main.java (Giao diện):**
```java
private void addBook() {
    System.out.print("Enter Book ID: ");
    String id = sc.nextLine();
    
    System.out.print("Enter Title: ");
    String title = sc.nextLine();
    
    // ... input khác
    
    // GỌI MANAGER
    String result = bookManager.addBook(id, title, author, genre, year, quantity);
    System.out.println(result);
}
```

2. **BookManager.java (Logic):**
```java
public String addBook(String id, String title, String author, String genre, int year, int quantity) {
    // Kiểm tra
    if (id.isEmpty() || title.isEmpty()) {
        return "Error: ID and Title cannot be empty!";
    }
    
    // Khởi tạo Object
    Book book = new Book(id, title, author, genre, year, quantity);
    
    // Thêm vào ArrayList
    books.add(book);
    
    return "Book added successfully!";
}
```

---

### Bảng Tóm Tắt

```
┌─────────────────────────────────────────────────────────────┐
│                 NON-OOP vs OOP COMPARISON                   │
├──────────────────────┬──────────────┬──────────────────────┤
│ Tiêu Chí             │ Non-OOP      │ OOP                  │
├──────────────────────┼──────────────┼──────────────────────┤
│ Số File              │ 1 (Main.java)│ 8 (Entity + Manager) │
│ Dòng Code Mỗi File   │ 1086 (Khổng) │ 100-200 (Nhỏ gọn)   │
│ Dễ Bảo Trì           │ ❌ Khó       │ ✅ Dễ                │
│ Tái Sử Dụng Code     │ ❌ Khó       │ ✅ Dễ                │
│ Test Unit            │ ❌ Khó       │ ✅ Dễ                │
│ Limit Dữ Liệu        │ ⚠️ 100 items │ ✅ Vô Hạn            │
│ Type-Safe            │ ⚠️ index     │ ✅ Object            │
│ Readability          │ ❌ Khó       │ ✅ Dễ                │
└──────────────────────┴──────────────┴──────────────────────┘
```

---

## 📚 Hướng Dẫn Thêm Tính Năng Mới

Nếu muốn thêm tính năng mới, quy trình như sau:

### Ví Dụ: Thêm Chức Năng "Extend Due Date" (Gia Hạn Sách)

**Bước 1: Thêm Method vào BorrowRecord.java**
```java
public void extendDueDate(LocalDate newDueDate) {
    this.dueDate = newDueDate;
}
```

**Bước 2: Thêm Method vào BorrowManager.java**
```java
public String extendBorrowPeriod(String borrowRecordId, String newDueDate) {
    BorrowRecord record = findActiveBorrowRecord(borrowRecordId);
    if (record == null) {
        return "Borrow record not found!";
    }
    record.extendDueDate(LocalDate.parse(newDueDate));
    return "Due date extended successfully!";
}
```

**Bước 3: Thêm UI vào Main.java**
```java
// Trong borrowReturnBooks() sub-menu
case 5:
    System.out.print("Enter Borrow Record ID: ");
    String recordId = sc.nextLine();
    System.out.print("Enter New Due Date (yyyy-MM-dd): ");
    String newDueDate = sc.nextLine();
    System.out.println(borrowManager.extendBorrowPeriod(recordId, newDueDate));
    break;
```

**Bước 4: Cập nhật DataManager.java** (nếu cần lưu thêm trường)

---

## ✅ Kết Luận

### Sức Mạnh Của OOP
1. **Encapsulation** - Dữ liệu được bảo vệ, truy cập qua Getter/Setter
2. **Inheritance** - (Có thể mở rộng sau: `Book extends Entity`)
3. **Polymorphism** - (Có thể dùng Interface)
4. **Abstraction** - Ẩn chi tiết phức tạp, chỉ hiển thị interface

### Lợi Ích Thiết Kế
1. **Dễ Bảo Trì** - Mỗi class có trách nhiệm rõ ràng
2. **Dễ Mở Rộng** - Thêm tính năng mà không ảnh hưởng code cũ
3. **Dễ Test** - Có thể test từng Manager riêng lẻ
4. **Dễ Chuyển Đổi** - Từ Console → Web → Mobile (logic Manager không đổi)

### Những Cải Tiến Có Thể Làm Tiếp
- 🔐 **Database** - Thay file .txt bằng SQL Database
- 🌐 **Web API** - Tạo REST API từ Managers bằng Spring Boot
- 🧪 **Unit Tests** - Viết JUnit Tests cho từng Manager
- 📱 **Mobile** - Chia sẻ logic (Manager) cho ứng dụng mobile
- 🔔 **Notifications** - Thêm Email/SMS khi quá hạn sách

---

**Báo cáo được biên soạn để giúp bạn và mọi người hiểu rõ cấu trúc, flow và từng chi tiết của hệ thống.**

📝 *For more detailed information, see individual reports in `/reports/` folder.*

---

**Ngày lập báo cáo:** June 2, 2026
**Phiên bản:** OOP v1.0
**Trạng thái:** Hoàn thiện
