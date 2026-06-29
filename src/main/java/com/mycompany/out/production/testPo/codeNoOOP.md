import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.io.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    static final int MAX_SIZE = 100;
    static final int BORROW_LIMIT = 3;
    static final int BORROW_PERIOD_DAYS = 7;
    static final int FINE_PER_DAY = 5000;

    // Books
    static String[] bookIds = new String[MAX_SIZE];
    static String[] titles = new String[MAX_SIZE];
    static String[] authors = new String[MAX_SIZE];
    static String[] genres = new String[MAX_SIZE];
    static int[] years = new int[MAX_SIZE];
    static int[] quantities = new int[MAX_SIZE];
    static int[] bookBorrowCount = new int[MAX_SIZE];
    static int bookCount = 0;

    // Members
    static String[] memberIds = new String[MAX_SIZE];
    static String[] memberNames = new String[MAX_SIZE];
    static String[] phones = new String[MAX_SIZE];
    static String[] emails = new String[MAX_SIZE];
    static int[] memberCurrentBorrowCount = new int[MAX_SIZE];
    static int[] memberTotalBorrowCount = new int[MAX_SIZE]; // Thêm biến lưu tổng lượt mượn
    static int memberCount = 0;

    // Borrow records
    static String[] borrowBookIds = new String[MAX_SIZE];
    static String[] borrowMemberIds = new String[MAX_SIZE];
    static String[] borrowDates = new String[MAX_SIZE];
    static boolean[] isReturned = new boolean[MAX_SIZE];
    static int borrowCount = 0;

    public static void main(String[] args) {
        loadData();
        int choice;

        do {
            printMainMenu();
            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageMembers();
                    break;
                case 3:
                    manageBorrowing();
                    break;
                case 4:
                    manageReports();
                    break;
                case 5:
                    saveData();
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        } while (choice != 5);
    }

    static void printMainMenu() {
        System.out.println("\n======================================");
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Members");
        System.out.println("3. Borrowing/Returning");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------");
    }

    // ================= BOOK MANAGEMENT =================

    static void manageBooks() {
        int choice;

        do {
            System.out.println("\n----------- BOOK MANAGEMENT -----------");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Books");
            System.out.println("6. Back");

            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    removeBook();
                    break;
                case 4:
                    viewBooks();
                    break;
                case 5:
                    searchBooks();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } while (choice != 6);
    }

    static void addBook() {
        System.out.println("\n----------- ADD BOOK -----------");

        if (bookCount >= MAX_SIZE) {
            System.out.println("Book list is full.");
            return;
        }

        System.out.print("Book ID: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Fail: Book ID cannot be empty.");
            return;
        }

        // [Business Logic BR1]: ID Sách (Book ID) phải là duy nhất, không được trùng
        // lặp.
        if (findBookIndex(id) != -1) {
            System.out.println("Fail: Book ID already exists.");
            return;
        }

        System.out.print("Title: ");
        String title = sc.nextLine().trim();

        // [Business Logic BR2]: Tiêu đề sách (Title) không được để trống.
        if (title.isEmpty()) {
            System.out.println("Fail: Title cannot be empty.");
            return;
        }

        System.out.print("Author: ");
        String author = sc.nextLine().trim();

        // [Business Logic BR2]: Tác giả (Author) không được để trống.
        if (author.isEmpty()) {
            System.out.println("Fail: Author cannot be empty.");
            return;
        }

        System.out.print("Genre: ");
        String genre = sc.nextLine().trim();

        // [Business Logic BR2]: Thể loại (Genre) không được để trống.
        if (genre.isEmpty()) {
            System.out.println("Fail: Genre cannot be empty.");
            return;
        }

        int year = readInt("Publication Year: ");
        int quantity = readInt("Quantity: ");

        if (quantity < 0) {
            System.out.println("Fail: Quantity cannot be negative.");
            return;
        }

        System.out.println("[1] Save [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            bookIds[bookCount] = id;
            titles[bookCount] = title;
            authors[bookCount] = author;
            genres[bookCount] = genre;
            years[bookCount] = year;
            quantities[bookCount] = quantity;
            bookBorrowCount[bookCount] = 0;
            bookCount++;

            System.out.println("Output: Book added successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void updateBook() {
        System.out.println("\n----------- UPDATE BOOK -----------");

        System.out.print("Enter Book ID: ");
        String id = sc.nextLine().trim();

        int index = findBookIndex(id);

        if (index == -1) {
            System.out.println("Fail: Book not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        System.out.println("Title: " + titles[index]);
        System.out.println("Author: " + authors[index]);
        System.out.println("Genre: " + genres[index]);
        System.out.println("Publication Year: " + years[index]);
        System.out.println("Quantity: " + quantities[index]);

        System.out.println("\nBeginner version: only update quantity.");
        System.out.print("Enter new Quantity (leave blank to skip): ");
        String quantityInput = sc.nextLine().trim();

        // Fix: Cho phép bỏ qua cập nhật nếu người dùng nhấn Enter (để trống).
        // Cố gắng parse int, nếu lỗi thì báo lỗi.
        int newQuantity = quantities[index];
        if (!quantityInput.isEmpty()) {
            try {
                newQuantity = Integer.parseInt(quantityInput);
                if (newQuantity < 0) {
                    System.out.println("Fail: Quantity cannot be negative.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Fail: Invalid number.");
                return;
            }
        }

        System.out.println("[1] Update [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            quantities[index] = newQuantity;
            System.out.println("Output: Book updated successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void removeBook() {
        System.out.println("\n----------- REMOVE BOOK -----------");

        System.out.print("Enter Book ID: ");
        String id = sc.nextLine().trim();

        int index = findBookIndex(id);

        if (index == -1) {
            System.out.println("Fail: Book not found.");
            return;
        }

        // [Business Logic]: Không được phép xóa sách nếu sách đang có người mượn (chưa
        // trả).
        if (isBookCurrentlyBorrowed(id)) {
            System.out.println("Fail: Cannot remove this book because it is currently borrowed.");
            return;
        }

        System.out.println("[1] Remove [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            for (int i = index; i < bookCount - 1; i++) {
                bookIds[i] = bookIds[i + 1];
                titles[i] = titles[i + 1];
                authors[i] = authors[i + 1];
                genres[i] = genres[i + 1];
                years[i] = years[i + 1];
                quantities[i] = quantities[i + 1];
                bookBorrowCount[i] = bookBorrowCount[i + 1];
            }

            bookCount--;
            System.out.println("Output: Book removed successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void viewBooks() {
        System.out.println("\n----------- BOOK LIST -----------");

        if (bookCount == 0) {
            System.out.println("No books available.");
            return;
        }

        System.out.printf("%-10s %-25s %-20s %-15s %-8s %-5s\n",
                "ID", "Title", "Author", "Genre", "Year", "Qty");
        System.out.println("--------------------------------------------------------------------------------");

        for (int i = 0; i < bookCount; i++) {
            System.out.printf("%-10s %-25s %-20s %-15s %-8d %-5d\n",
                    bookIds[i],
                    titles[i],
                    authors[i],
                    genres[i],
                    years[i],
                    quantities[i]);
        }
    }

    static void searchBooks() {
        System.out.println("\n----------- SEARCH BOOKS -----------");

        System.out.print("Enter keyword title/author/genre: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;

        for (int i = 0; i < bookCount; i++) {
            if (titles[i].toLowerCase().contains(keyword)
                    || authors[i].toLowerCase().contains(keyword)
                    || genres[i].toLowerCase().contains(keyword)) {

                System.out.println(bookIds[i] + " | " + titles[i] + " | " + authors[i]
                        + " | " + genres[i] + " | Qty: " + quantities[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books found.");
        }
    }

    // ================= MEMBER MANAGEMENT =================

    static void manageMembers() {
        int choice;

        do {
            System.out.println("\n----------- MEMBER MANAGEMENT -----------");
            System.out.println("1. Add Member");
            System.out.println("2. Update Member");
            System.out.println("3. Remove Member");
            System.out.println("4. View All Members");
            System.out.println("5. Search Members");
            System.out.println("6. Back");

            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    updateMember();
                    break;
                case 3:
                    removeMember();
                    break;
                case 4:
                    viewMembers();
                    break;
                case 5:
                    searchMembers();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } while (choice != 6);
    }

    static void addMember() {
        System.out.println("\n----------- ADD MEMBER -----------");

        if (memberCount >= MAX_SIZE) {
            System.out.println("Member list is full.");
            return;
        }

        System.out.print("Member ID: ");
        String id = sc.nextLine().trim();

        if (id.isEmpty()) {
            System.out.println("Fail: Member ID cannot be empty.");
            return;
        }

        // [Business Logic BR1]: ID Thành viên (Member ID) phải là duy nhất.
        if (findMemberIndex(id) != -1) {
            System.out.println("Fail: Member ID already exists.");
            return;
        }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        // [Business Logic BR2]: Tên thành viên (Name) không được để trống.
        if (name.isEmpty()) {
            System.out.println("Fail: Name cannot be empty.");
            return;
        }

        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.println("[1] Save [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            memberIds[memberCount] = id;
            memberNames[memberCount] = name;
            phones[memberCount] = phone;
            emails[memberCount] = email;
            memberCurrentBorrowCount[memberCount] = 0;
            memberTotalBorrowCount[memberCount] = 0;
            memberCount++;

            System.out.println("Output: Member added successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void updateMember() {
        System.out.println("\n----------- UPDATE MEMBER -----------");

        System.out.print("Enter Member ID: ");
        String id = sc.nextLine().trim();

        int index = findMemberIndex(id);

        if (index == -1) {
            System.out.println("Fail: Member not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        System.out.println("Name: " + memberNames[index]);
        System.out.println("Phone: " + phones[index]);
        System.out.println("Email: " + emails[index]);

        System.out.println("\nBeginner version: only update phone and email.");

        System.out.print("New Phone (leave blank to skip): ");
        String newPhone = sc.nextLine().trim();

        System.out.print("New Email (leave blank to skip): ");
        String newEmail = sc.nextLine().trim();

        System.out.println("[1] Update [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            // Fix: Chỉ cập nhật các trường nếu người dùng nhập dữ liệu (không bỏ trống)
            if (!newPhone.isEmpty()) {
                phones[index] = newPhone;
            }
            if (!newEmail.isEmpty()) {
                emails[index] = newEmail;
            }
            System.out.println("Output: Member updated successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void removeMember() {
        System.out.println("\n----------- REMOVE MEMBER -----------");

        System.out.print("Enter Member ID: ");
        String id = sc.nextLine().trim();

        int index = findMemberIndex(id);

        if (index == -1) {
            System.out.println("Fail: Member not found.");
            return;
        }

        // [Business Logic]: Không được phép xóa thành viên nếu họ vẫn đang mượn sách
        // (chưa trả hết).
        if (memberCurrentBorrowCount[index] > 0) {
            System.out.println("Fail: Cannot remove member because this member is borrowing books.");
            return;
        }

        System.out.println("[1] Remove [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            for (int i = index; i < memberCount - 1; i++) {
                memberIds[i] = memberIds[i + 1];
                memberNames[i] = memberNames[i + 1];
                phones[i] = phones[i + 1];
                emails[i] = emails[i + 1];
                memberCurrentBorrowCount[i] = memberCurrentBorrowCount[i + 1];
                memberTotalBorrowCount[i] = memberTotalBorrowCount[i + 1];
            }

            memberCount--;
            System.out.println("Output: Member removed successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void viewMembers() {
        System.out.println("\n----------- MEMBER LIST -----------");

        if (memberCount == 0) {
            System.out.println("No members available.");
            return;
        }

        System.out.printf("%-10s %-20s %-15s %-25s %-10s\n",
                "ID", "Name", "Phone", "Email", "Borrowing");
        System.out.println("--------------------------------------------------------------------------------");

        for (int i = 0; i < memberCount; i++) {
            System.out.printf("%-10s %-20s %-15s %-25s %-10d\n",
                    memberIds[i],
                    memberNames[i],
                    phones[i],
                    emails[i],
                    memberCurrentBorrowCount[i]);
        }
    }

    static void searchMembers() {
        System.out.println("\n----------- SEARCH MEMBERS -----------");

        System.out.print("Enter keyword name/id: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        boolean found = false;

        for (int i = 0; i < memberCount; i++) {
            if (memberIds[i].toLowerCase().contains(keyword)
                    || memberNames[i].toLowerCase().contains(keyword)) {

                System.out.println(memberIds[i] + " | " + memberNames[i]
                        + " | " + phones[i] + " | " + emails[i]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No members found.");
        }
    }

    // ================= BORROWING / RETURNING =================

    static void manageBorrowing() {
        int choice;

        do {
            System.out.println("\n----------- BORROWING / RETURNING -----------");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Currently Borrowed Books");
            System.out.println("4. View Borrowing History By Member");
            System.out.println("5. Back");

            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewCurrentlyBorrowedBooks();
                    break;
                case 4:
                    viewBorrowingHistoryByMember();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        } while (choice != 5);
    }

    static void borrowBook() {
        System.out.println("\n----------- BORROW BOOK -----------");

        if (borrowCount >= MAX_SIZE) {
            System.out.println("Borrow record list is full.");
            return;
        }

        System.out.print("Member ID: ");
        String memberId = sc.nextLine().trim();

        int memberIndex = findMemberIndex(memberId);

        // [Business Logic BR3]: Thành viên phải tồn tại trong hệ thống trước khi mượn
        // sách.
        if (memberIndex == -1) {
            System.out.println("Fail: Member does not exist.");
            return;
        }

        // [Business Logic BR5]: Một thành viên không được mượn vượt quá giới hạn sách
        // cho phép (BORROW_LIMIT).
        if (memberCurrentBorrowCount[memberIndex] >= BORROW_LIMIT) {
            System.out.println("Fail: This member has reached borrow limit.");
            return;
        }

        System.out.print("Book ID: ");
        String bookId = sc.nextLine().trim();

        int bookIndex = findBookIndex(bookId);

        if (bookIndex == -1) {
            System.out.println("Fail: Book does not exist.");
            return;
        }

        // [Business Logic BR4]: Sách phải còn sẵn trong kho (quantity > 0) thì mới được
        // mượn.
        if (quantities[bookIndex] <= 0) {
            System.out.println("Fail: Book is out of stock.");
            return;
        }

        System.out.print("Borrow Date (DD/MM/YYYY): ");
        String borrowDate = sc.nextLine().trim();

        // [Business Logic BR6 & BR9]: Ngày mượn phải hợp lệ và đúng định dạng
        // DD/MM/YYYY.
        if (borrowDate.isEmpty() || !isValidDate(borrowDate)) {
            System.out.println("Fail: Invalid borrow date format.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowLocalDate = LocalDate.parse(borrowDate, formatter);

        // [Business Logic BR6]: Ngày mượn (Borrow Date) không được ở tương lai (phải <=
        // hiện tại).
        if (borrowLocalDate.isAfter(LocalDate.now())) {
            System.out.println("Fail: Borrow date cannot be in the future.");
            return;
        }

        System.out.println("[1] Confirm [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            borrowBookIds[borrowCount] = bookId;
            borrowMemberIds[borrowCount] = memberId;
            borrowDates[borrowCount] = borrowDate;
            isReturned[borrowCount] = false;
            borrowCount++;

            // [Business Logic BR8]: Giảm số lượng sách trong kho đi 1 khi có người mượn.
            quantities[bookIndex]--;
            bookBorrowCount[bookIndex]++;
            memberCurrentBorrowCount[memberIndex]++;
            memberTotalBorrowCount[memberIndex]++; // Tăng tổng số lượt mượn

            System.out.println("Output: Book '" + titles[bookIndex]
                    + "' borrowed by '" + memberNames[memberIndex] + "' successfully.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void returnBook() {
        System.out.println("\n----------- RETURN BOOK -----------");

        System.out.print("Member ID: ");
        String memberId = sc.nextLine().trim();

        int memberIndex = findMemberIndex(memberId);

        // [Business Logic BR3]: Thành viên phải tồn tại trong hệ thống trước khi mượn
        // sách.
        if (memberIndex == -1) {
            System.out.println("Fail: Member not found.");
            return;
        }

        System.out.print("Book ID: ");
        String bookId = sc.nextLine().trim();

        int bookIndex = findBookIndex(bookId);

        if (bookIndex == -1) {
            System.out.println("Fail: Book not found.");
            return;
        }

        int borrowIndex = findActiveBorrowRecord(memberId, bookId);

        if (borrowIndex == -1) {
            System.out.println("Fail: This member is not borrowing this book.");
            return;
        }

        System.out.print("Return Date (DD/MM/YYYY): ");
        String returnDateStr = sc.nextLine().trim();

        if (returnDateStr.isEmpty() || !isValidDate(returnDateStr)) {
            System.out.println("Fail: Invalid return date format.");
            return;
        }

        String borrowDateStr = borrowDates[borrowIndex];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowDate = LocalDate.parse(borrowDateStr, formatter);
        LocalDate returnDate = LocalDate.parse(returnDateStr, formatter);

        // [Business Logic BR6]: Ngày trả (Return Date) bắt buộc phải sau ngày mượn
        // (Borrow Date).
        if (!returnDate.isAfter(borrowDate)) {
            System.out.println("Fail: Return date must be after borrow date.");
            return;
        }

        LocalDate dueDate = borrowDate.plusDays(BORROW_PERIOD_DAYS);
        long daysOverdue = 0;
        long fine = 0;

        // [Business Logic BR7]: Tính phí trễ hạn (Overdue fine) dựa trên số ngày trễ
        // nhân với mức phạt (FINE_PER_DAY).
        if (returnDate.isAfter(dueDate)) {
            daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = daysOverdue * FINE_PER_DAY;
        }

        System.out.println("[1] Confirm Return [2] Cancel");
        int confirm = readInt("Choice: ");

        if (confirm == 1) {
            isReturned[borrowIndex] = true;
            // [Business Logic BR8]: Tăng số lượng sách trong kho lên 1 khi sách được trả
            // lại.
            quantities[bookIndex]++;
            memberCurrentBorrowCount[memberIndex]--;

            System.out.print("Output: Book '" + titles[bookIndex]
                    + "' returned by '" + memberNames[memberIndex] + "'. ");
            if (fine > 0) {
                System.out.println("Overdue fine: " + fine + " VND.");
            } else {
                System.out.println("No overdue fine.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    static void viewCurrentlyBorrowedBooks() {
        System.out.println("\n----------- CURRENTLY BORROWED BOOKS -----------");

        boolean found = false;

        for (int i = 0; i < borrowCount; i++) {
            if (!isReturned[i]) {
                int bookIndex = findBookIndex(borrowBookIds[i]);
                int memberIndex = findMemberIndex(borrowMemberIds[i]);

                if (bookIndex != -1 && memberIndex != -1) {
                    System.out.println("Book: " + borrowBookIds[i] + " - " + titles[bookIndex]
                            + " | Member: " + borrowMemberIds[i] + " - " + memberNames[memberIndex]
                            + " | Borrow Date: " + borrowDates[i]);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No books are currently borrowed.");
        }
    }

    static void viewBorrowingHistoryByMember() {
        System.out.println("\n----------- BORROWING HISTORY -----------");

        System.out.print("Enter Member ID: ");
        String memberId = sc.nextLine().trim();

        int memberIndex = findMemberIndex(memberId);

        // [Business Logic BR3]: Thành viên phải tồn tại trong hệ thống trước khi mượn
        // sách.
        if (memberIndex == -1) {
            System.out.println("Fail: Member not found.");
            return;
        }

        boolean found = false;

        System.out.println("Member: " + memberNames[memberIndex]);

        for (int i = 0; i < borrowCount; i++) {
            if (borrowMemberIds[i].equals(memberId)) {
                String status = isReturned[i] ? "Returned" : "Borrowing";

                int bookIndex = findBookIndex(borrowBookIds[i]);

                if (bookIndex != -1) {
                    System.out.println("Book: " + borrowBookIds[i] + " - " + titles[bookIndex]
                            + " | Borrow Date: " + borrowDates[i]
                            + " | Status: " + status);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No borrowing history found.");
        }
    }

    // ================= REPORTS =================

    static void manageReports() {
        int choice;

        do {
            System.out.println("\n----------- REPORTS -----------");
            System.out.println("1. List Currently Borrowed Books");
            System.out.println("2. List Overdue Books");
            System.out.println("3. List Most Popular Books");
            System.out.println("4. List Members With Borrowing Count");
            System.out.println("5. Back");

            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    viewCurrentlyBorrowedBooks();
                    break;
                case 2:
                    viewOverdueBooks();
                    break;
                case 3:
                    viewPopularBooksSimple();
                    break;
                case 4:
                    viewMembersBorrowingCount();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        } while (choice != 5);
    }

    static void viewPopularBooksSimple() {
        System.out.println("\n----------- MOST POPULAR BOOKS SIMPLE -----------");

        if (bookCount == 0) {
            System.out.println("No books available.");
            return;
        }

        // Tạo mảng chỉ số để sắp xếp (không thay đổi thứ tự mảng gốc)
        int[] sortedIndices = new int[bookCount];
        for (int i = 0; i < bookCount; i++) {
            sortedIndices[i] = i;
        }

        // [Business Logic BR10]: Sách phổ biến nhất được xác định dựa trên tổng số lượt
        // mượn.
        // Tiến hành sắp xếp Bubble Sort giảm dần theo bookBorrowCount
        for (int i = 0; i < bookCount - 1; i++) {
            for (int j = 0; j < bookCount - i - 1; j++) {
                if (bookBorrowCount[sortedIndices[j]] < bookBorrowCount[sortedIndices[j + 1]]) {
                    int temp = sortedIndices[j];
                    sortedIndices[j] = sortedIndices[j + 1];
                    sortedIndices[j + 1] = temp;
                }
            }
        }

        boolean found = false;
        for (int i = 0; i < bookCount; i++) {
            int idx = sortedIndices[i];
            if (bookBorrowCount[idx] > 0) {
                System.out.println(bookIds[idx] + " | " + titles[idx]
                        + " | Times Borrowed: " + bookBorrowCount[idx]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No book has been borrowed yet.");
        }
    }

    static void viewMembersBorrowingCount() {
        System.out.println("\n----------- MEMBER TOTAL BORROWING COUNT -----------");

        if (memberCount == 0) {
            System.out.println("No members available.");
            return;
        }

        // Tạo mảng chỉ số để sắp xếp giảm dần
        int[] sortedIndices = new int[memberCount];
        for (int i = 0; i < memberCount; i++) {
            sortedIndices[i] = i;
        }

        // Sắp xếp Bubble Sort giảm dần theo memberTotalBorrowCount
        for (int i = 0; i < memberCount - 1; i++) {
            for (int j = 0; j < memberCount - i - 1; j++) {
                if (memberTotalBorrowCount[sortedIndices[j]] < memberTotalBorrowCount[sortedIndices[j + 1]]) {
                    int temp = sortedIndices[j];
                    sortedIndices[j] = sortedIndices[j + 1];
                    sortedIndices[j + 1] = temp;
                }
            }
        }

        boolean found = false;
        for (int i = 0; i < memberCount; i++) {
            int idx = sortedIndices[i];
            if (memberTotalBorrowCount[idx] > 0) {
                System.out.println(memberIds[idx] + " | " + memberNames[idx]
                        + " | Total Borrowings: " + memberTotalBorrowCount[idx]);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No member has borrowed any books yet.");
        }
    }

    static void viewOverdueBooks() {
        System.out.println("\n----------- OVERDUE BOOKS -----------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate reportDate = LocalDate.now();

        System.out.println("Report Date: " + reportDate.format(formatter));

        boolean found = false;

        System.out.printf("%-10s %-25s %-10s %-20s %-15s %-15s\n",
                "Book ID", "Title", "Member ID", "Member Name", "Due Date", "Days Overdue");
        System.out.println(
                "--------------------------------------------------------------------------------------------------");

        for (int i = 0; i < borrowCount; i++) {
            if (!isReturned[i]) {
                LocalDate borrowDate = LocalDate.parse(borrowDates[i], formatter);
                LocalDate dueDate = borrowDate.plusDays(BORROW_PERIOD_DAYS);

                if (reportDate.isAfter(dueDate)) {
                    long daysOverdue = ChronoUnit.DAYS.between(dueDate, reportDate);

                    int bookIndex = findBookIndex(borrowBookIds[i]);
                    int memberIndex = findMemberIndex(borrowMemberIds[i]);

                    if (bookIndex != -1 && memberIndex != -1) {
                        System.out.printf("%-10s %-25s %-10s %-20s %-15s %-15d\n",
                                borrowBookIds[i],
                                titles[bookIndex],
                                borrowMemberIds[i],
                                memberNames[memberIndex],
                                dueDate.format(formatter),
                                daysOverdue);

                        found = true;
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No overdue books found.");
        }
    }

    // ================= HELPER METHODS =================

    static int findBookIndex(String id) {
        for (int i = 0; i < bookCount; i++) {
            if (bookIds[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static int findMemberIndex(String id) {
        for (int i = 0; i < memberCount; i++) {
            if (memberIds[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    static boolean isBookCurrentlyBorrowed(String bookId) {
        for (int i = 0; i < borrowCount; i++) {
            if (borrowBookIds[i].equals(bookId) && !isReturned[i]) {
                return true;
            }
        }
        return false;
    }

    static int findActiveBorrowRecord(String memberId, String bookId) {
        for (int i = 0; i < borrowCount; i++) {
            if (borrowMemberIds[i].equals(memberId)
                    && borrowBookIds[i].equals(bookId)
                    && !isReturned[i]) {
                return i;
            }
        }
        return -1;
    }

    static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number. Please enter again.");
            }
        }
    }

    static boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    static void loadData() {
        try {
            // Load Books
            File fileBooks = new File("data/books.txt");
            if (fileBooks.exists()) {
                Scanner scFile = new Scanner(fileBooks);
                while (scFile.hasNextLine()) {
                    String line = scFile.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 7) {
                            bookIds[bookCount] = parts[0];
                            titles[bookCount] = parts[1];
                            authors[bookCount] = parts[2];
                            genres[bookCount] = parts[3];
                            years[bookCount] = Integer.parseInt(parts[4]);
                            quantities[bookCount] = Integer.parseInt(parts[5]);
                            bookBorrowCount[bookCount] = Integer.parseInt(parts[6]);
                            bookCount++;
                        }
                    }
                }
                scFile.close();
            }

            // Load Members
            File fileMembers = new File("data/members.txt");
            if (fileMembers.exists()) {
                Scanner scFile = new Scanner(fileMembers);
                while (scFile.hasNextLine()) {
                    String line = scFile.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 6) {
                            memberIds[memberCount] = parts[0];
                            memberNames[memberCount] = parts[1];
                            phones[memberCount] = parts[2];
                            emails[memberCount] = parts[3];
                            memberCurrentBorrowCount[memberCount] = Integer.parseInt(parts[4]);
                            memberTotalBorrowCount[memberCount] = Integer.parseInt(parts[5]);
                            memberCount++;
                        }
                    }
                }
                scFile.close();
            }

            // Load Borrow Records
            File fileBorrows = new File("data/borrow_records.txt");
            if (fileBorrows.exists()) {
                Scanner scFile = new Scanner(fileBorrows);
                while (scFile.hasNextLine()) {
                    String line = scFile.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] parts = line.split("\\|");
                        if (parts.length == 4) {
                            borrowBookIds[borrowCount] = parts[0];
                            borrowMemberIds[borrowCount] = parts[1];
                            borrowDates[borrowCount] = parts[2];
                            isReturned[borrowCount] = Boolean.parseBoolean(parts[3]);
                            borrowCount++;
                        }
                    }
                }
                scFile.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    static void saveData() {
        try {
            File dir = new File("data");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Save Books
            PrintWriter pwBooks = new PrintWriter(new FileWriter("data/books.txt"));
            for (int i = 0; i < bookCount; i++) {
                pwBooks.println(bookIds[i] + "|" + titles[i] + "|" + authors[i] + "|" + genres[i] + "|" + years[i] + "|" + quantities[i] + "|" + bookBorrowCount[i]);
            }
            pwBooks.close();

            // Save Members
            PrintWriter pwMembers = new PrintWriter(new FileWriter("data/members.txt"));
            for (int i = 0; i < memberCount; i++) {
                pwMembers.println(memberIds[i] + "|" + memberNames[i] + "|" + phones[i] + "|" + emails[i] + "|" + memberCurrentBorrowCount[i] + "|" + memberTotalBorrowCount[i]);
            }
            pwMembers.close();

            // Save Borrow Records
            PrintWriter pwBorrows = new PrintWriter(new FileWriter("data/borrow_records.txt"));
            for (int i = 0; i < borrowCount; i++) {
                pwBorrows.println(borrowBookIds[i] + "|" + borrowMemberIds[i] + "|" + borrowDates[i] + "|" + isReturned[i]);
            }
            pwBorrows.close();
            
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
