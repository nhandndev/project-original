package com.mycompany;

import com.mycompany.entity.Book;
import com.mycompany.entity.Member;
import com.mycompany.entity.BorrowRecord;
import com.mycompany.manager.BookManager;
import com.mycompany.manager.MemberManager;
import com.mycompany.manager.BorrowManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class Main {
    private Scanner sc = new Scanner(System.in);

    private BookManager bookManager;
    private MemberManager memberManager;
    private BorrowManager borrowManager;

    // TODO (Nhan): Khoi tao cac Manager
    public Main() {
        this.borrowManager = new BorrowManager();
        this.bookManager = this.borrowManager.getBookManager();
        this.memberManager = this.borrowManager.getMemberManager();
    }

    // TODO (Nhan): Ham main de chay chuong trinh
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    // TODO (Nhan): Viet vong lap chinh cua chuong trinh UI
    private void run() {
        int choice;
        do {
            this.printMainMenu();
            choice = this.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    this.manageBooks();
                    break;
                case 2:
                    this.manageMembers();
                    break;
                case 3:
                    this.manageBorrowing();
                    break;
                case 4:
                    this.manageReports();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        } while (choice != 5);
    }

    private void printMainMenu() {
        System.out.println("\n======================================");
        System.out.println("LIBRARY MANAGEMENT SYSTEM (OOP Version)");
        System.out.println("======================================");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Members");
        System.out.println("3. Borrowing/Returning");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------");
    }

    // ================= BOOK MANAGEMENT =================
    // TODO (Van): Viet cac ham quan ly Book UI
    private void manageBooks() {
        int choice;
        do {
            System.out.println("\n----------- BOOK MANAGEMENT -----------");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Remove Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Books");
            System.out.println("6. Back");
            choice = this.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    this.addBook();
                    break;
                case 2:
                    this.updateBook();
                    break;
                case 3:
                    this.removeBook();
                    break;
                case 4:
                    this.viewAllBooks();
                    break;
                case 5:
                    this.searchBooks();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } while (choice != 6);
    }

    // TODO (Van): Ham UI them sach
    private void addBook() {
        System.out.println("\n----------- ADD BOOK -----------");
        System.out.println("1. Physical Book");
        System.out.println("2. Ebook");
        int typeChoice;
        while (true) {
            typeChoice = this.readInt("Select type [1-2]: ");
            if (typeChoice == 1 || typeChoice == 2) break;
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }

        System.out.print("Book ID: ");
        String id = this.sc.nextLine().trim();
        System.out.print("Title: ");
        String title = this.sc.nextLine().trim();
        System.out.print("Author: ");
        String author = this.sc.nextLine().trim();
        System.out.print("Genre: ");
        String genre = this.sc.nextLine().trim();
        int year = this.readInt("Publication Year: ");
        
        Book newBook = null;

        if (typeChoice == 1) {
            int quantity = this.readInt("Quantity: ");
            System.out.print("Weight (kg): ");
            double weight = Double.parseDouble(this.sc.nextLine().trim());
            System.out.print("Shelf Location: ");
            String location = this.sc.nextLine().trim();
            newBook = new Book(id, title, author, genre, year, quantity, "physical");
            newBook.setWeight(weight);
            newBook.setShelfLocation(location);
        } else {
            System.out.print("File Size (MB): ");
            double size = Double.parseDouble(this.sc.nextLine().trim());
            System.out.print("Format (PDF/EPUB): ");
            String format = this.sc.nextLine().trim();
            newBook = new Book(id, title, author, genre, year, 999, "ebook");
            newBook.setFileSize(size);
            newBook.setFormat(format);
        }

        System.out.println("[1] Save [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.bookManager.addBook(newBook);
            System.out.println(result.equals("Success") ? "Output: Book added successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI cap nhat sach
    private void updateBook() {
        System.out.println("\n----------- UPDATE BOOK -----------");
        System.out.print("Enter Book ID: ");
        String id = this.sc.nextLine().trim();

        Book b = this.bookManager.getBookById(id);
        if (b == null) {
            System.out.println("Fail: Book not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        System.out.println("Title: " + b.getTitle());
        System.out.println("Author: " + b.getAuthor());
        System.out.println("Genre: " + b.getGenre());
        System.out.println("Publication Year: " + b.getYear());
        System.out.println("Quantity: " + b.getQuantity());

        System.out.print("Enter new Quantity (leave blank to skip): ");
        String quantityInput = this.sc.nextLine().trim();

        System.out.println("[1] Update [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.bookManager.updateBookQuantity(id, quantityInput);
            System.out.println(result.equals("Success") ? "Output: Book updated successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI xoa sach
    private void removeBook() {
        System.out.println("\n----------- REMOVE BOOK -----------");
        System.out.print("Enter Book ID: ");
        String id = this.sc.nextLine().trim();

        System.out.println("[1] Remove [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            boolean isBorrowed = this.borrowManager.isBookCurrentlyBorrowed(id);
            String result = this.bookManager.removeBook(id, isBorrowed);
            System.out.println(result.equals("Success") ? "Output: Book removed successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI hien thi danh sach sach
    private void viewAllBooks() {
        System.out.println("\n----------- BOOK LIST -----------");
        List<Book> books = this.bookManager.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        System.out.printf("%-10s %-25s %-20s %-15s %-8s %-5s\n", "ID", "Title", "Author", "Genre", "Year", "Qty");
        System.out.println("--------------------------------------------------------------------------------");
        for (Book b : books) {
            System.out.printf("%-10s %-25s %-20s %-15s %-8d %-5d\n",
                    b.getId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getYear(), b.getQuantity());
        }
    }

    // TODO (Van): Ham UI tim kiem sach
    private void searchBooks() {
        System.out.println("\n----------- SEARCH BOOKS -----------");
        System.out.print("Enter keyword title/author/genre: ");
        String keyword = this.sc.nextLine().trim().toLowerCase();

        List<Book> books = this.bookManager.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book b : books) {
                System.out.println(b.getId() + " | " + b.getTitle() + " | " + b.getAuthor()
                        + " | " + b.getGenre() + " | Qty: " + b.getQuantity());
            }
        }
    }

    // ================= MEMBER MANAGEMENT =================
    // TODO (Van): Viet cac ham quan ly Member UI
    private void manageMembers() {
        int choice;
        do {
            System.out.println("\n----------- MEMBER MANAGEMENT -----------");
            System.out.println("1. Add Member");
            System.out.println("2. Update Member");
            System.out.println("3. Remove Member");
            System.out.println("4. View All Members");
            System.out.println("5. Search Members");
            System.out.println("6. Back");
            choice = this.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    this.addMember();
                    break;
                case 2:
                    this.updateMember();
                    break;
                case 3:
                    this.removeMember();
                    break;
                case 4:
                    this.viewAllMembers();
                    break;
                case 5:
                    this.searchMembers();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } while (choice != 6);
    }

    // TODO (Van): Ham UI them Member
    private void addMember() {
        System.out.println("\n----------- ADD MEMBER -----------");
        System.out.print("Member ID: ");
        String id = this.sc.nextLine().trim();
        System.out.print("Name: ");
        String name = this.sc.nextLine().trim();
        System.out.print("Phone: ");
        String phone = this.sc.nextLine().trim();
        System.out.print("Email: ");
        String email = this.sc.nextLine().trim();

        System.out.println("\nMember Type:");
        System.out.println("1. Regular Member (limit 3 books, 5000 VND/day fine)");
        System.out.println("2. Premium Member (limit 5 books, 2500 VND/day fine)");
        int typeChoice;
        while (true) {
            typeChoice = this.readInt("Select type [1-2]: ");
            if (typeChoice == 1 || typeChoice == 2) break;
            System.out.println("Invalid choice. Please enter 1 or 2.");
        }
        String memberType = (typeChoice == 2) ? "premium" : "regular";

        System.out.println("[1] Save [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.memberManager.addMember(id, name, phone, email, memberType);
            System.out.println(result.equals("Success") ? "Output: Member added successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI cap nhat Member
    private void updateMember() {
        System.out.println("\n----------- UPDATE MEMBER -----------");
        System.out.print("Enter Member ID: ");
        String id = this.sc.nextLine().trim();

        Member m = this.memberManager.getMemberById(id);
        if (m == null) {
            System.out.println("Fail: Member not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        System.out.println("Name: " + m.getName());
        System.out.println("Phone: " + m.getPhone());
        System.out.println("Email: " + m.getEmail());

        System.out.print("New Phone (leave blank to skip): ");
        String newPhone = this.sc.nextLine().trim();
        System.out.print("New Email (leave blank to skip): ");
        String newEmail = this.sc.nextLine().trim();

        System.out.println("[1] Update [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.memberManager.updateMember(id, newPhone, newEmail);
            System.out.println(result.equals("Success") ? "Output: Member updated successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI xoa Member
    private void removeMember() {
        System.out.println("\n----------- REMOVE MEMBER -----------");
        System.out.print("Enter Member ID: ");
        String id = this.sc.nextLine().trim();

        System.out.println("[1] Remove [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.memberManager.removeMember(id);
            System.out.println(result.equals("Success") ? "Output: Member removed successfully." : result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    // TODO (Van): Ham UI xem tat ca Member
    private void viewAllMembers() {
        System.out.println("\n----------- MEMBER LIST -----------");
        List<Member> members = this.memberManager.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members available.");
            return;
        }

        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-10s\n", "ID", "Name", "Phone", "Type", "Email", "Borrowing");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Member m : members) {
            String type = m.getType().equals("premium") ? "Premium" : "Regular";
            System.out.printf("%-10s %-20s %-15s %-20s %-15s %-10d\n",
                    m.getId(), m.getName(), m.getPhone(), type, m.getEmail(), m.getCurrentBorrowCount());
        }
    }

    // TODO (Van): Ham UI tim kiem Member
    private void searchMembers() {
        System.out.println("\n----------- SEARCH MEMBERS -----------");
        System.out.print("Enter keyword name/id: ");
        String keyword = this.sc.nextLine().trim().toLowerCase();

        List<Member> members = this.memberManager.searchMembers(keyword);
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            for (Member m : members) {
                System.out.println(m.getId() + " | " + m.getName() + " | " + m.getPhone() + " | " + m.getEmail());
            }
        }
    }

    // ================= BORROWING / RETURNING =================
    // TODO (Nhan): Viet cac ham quan ly muon tra UI (co dung DI tu BorrowManager)
    private void manageBorrowing() {
        int choice;
        do {
            System.out.println("\n----------- BORROWING / RETURNING -----------");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Currently Borrowed Books");
            System.out.println("4. View Borrowing History By Member");
            System.out.println("5. Back");
            choice = this.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    this.borrowBook();
                    break;
                case 2:
                    this.returnBook();
                    break;
                case 3:
                    this.viewCurrentlyBorrowedBooks();
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

    private void borrowBook() {
        System.out.println("\n----------- BORROW BOOK -----------");
        System.out.print("Member ID: ");
        String memberId = this.sc.nextLine().trim();
        System.out.print("Book ID: ");
        String bookId = this.sc.nextLine().trim();
        System.out.print("Borrow Date (DD/MM/YYYY): ");
        String borrowDate = this.sc.nextLine().trim();

        System.out.println("[1] Confirm [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.borrowManager.borrowBook(memberId, bookId, borrowDate);
            System.out.println(result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void returnBook() {
        System.out.println("\n----------- RETURN BOOK -----------");
        System.out.print("Member ID: ");
        String memberId = this.sc.nextLine().trim();
        System.out.print("Book ID: ");
        String bookId = this.sc.nextLine().trim();
        System.out.print("Return Date (DD/MM/YYYY): ");
        String returnDateStr = this.sc.nextLine().trim();

        System.out.println("[1] Confirm Return [2] Cancel");
        if (this.readInt("Choice: ") == 1) {
            String result = this.borrowManager.returnBook(memberId, bookId, returnDateStr);
            System.out.println(result);
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void viewCurrentlyBorrowedBooks() {
        System.out.println("\n----------- CURRENTLY BORROWED BOOKS -----------");
        List<BorrowRecord> records = this.borrowManager.getCurrentlyBorrowedRecords();
        if (records.isEmpty()) {
            System.out.println("No books are currently borrowed.");
            return;
        }

        for (BorrowRecord record : records) {
            Book b = this.bookManager.getBookById(record.getBookId());
            Member m = this.memberManager.getMemberById(record.getMemberId());
            if (b != null && m != null) {
                System.out.println("Book: " + record.getBookId() + " - " + b.getTitle()
                        + " | Member: " + record.getMemberId() + " - " + m.getName()
                        + " | Borrow Date: " + record.getBorrowDate());
            }
        }
    }

    private void viewBorrowingHistoryByMember() {
        System.out.println("\n----------- BORROWING HISTORY -----------");
        System.out.print("Enter Member ID: ");
        String memberId = this.sc.nextLine().trim();

        Member member = this.memberManager.getMemberById(memberId);
        if (member == null) {
            System.out.println("Fail: Member not found.");
            return;
        }

        System.out.println("Member: " + member.getName());
        List<BorrowRecord> records = this.borrowManager.getBorrowingHistoryByMember(memberId);

        if (records.isEmpty()) {
            System.out.println("No borrowing history found.");
            return;
        }

        for (BorrowRecord record : records) {
            String status = record.isReturned() ? "Returned" : "Borrowing";
            Book b = this.bookManager.getBookById(record.getBookId());
            System.out.println("Book: " + record.getBookId() + " - " + (b != null ? b.getTitle() : "Unknown")
                    + " | Borrow Date: " + record.getBorrowDate()
                    + " | Status: " + status);
        }
    }

    // ================= REPORTS =================
    // TODO (Nhan): Viet cac ham bao cao UI
    private void manageReports() {
        int choice;
        do {
            System.out.println("\n----------- REPORTS -----------");
            System.out.println("1. List Currently Borrowed Books");
            System.out.println("2. List Overdue Books");
            System.out.println("3. List Most Popular Books");
            System.out.println("4. List Members With Borrowing Count");
            System.out.println("5. Back");
            choice = this.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    this.viewCurrentlyBorrowedBooks();
                    break;
                case 2:
                    this.viewOverdueBooks();
                    break;
                case 3:
                    viewPopularBooksSimple();
                    break;
                case 4:
                    this.viewMembersBorrowingCount();
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-5.");
            }
        } while (choice != 5);
    }

    private void viewPopularBooksSimple() {
        System.out.println("\n----------- MOST POPULAR BOOKS SIMPLE -----------");
        List<Book> books = this.bookManager.getPopularBooksSimple();
        boolean found = false;

        for (Book b : books) {
            if (b.getBorrowCount() > 0) {
                System.out.println(b.getId() + " | " + b.getTitle() + " | Times Borrowed: " + b.getBorrowCount());
                found = true;
            }
        }
        if (!found)
            System.out.println("No book has been borrowed yet.");
    }

    private void viewMembersBorrowingCount() {
        System.out.println("\n----------- MEMBER TOTAL BORROWING COUNT -----------");
        List<Member> members = this.memberManager.getMembersBorrowingCount();
        boolean found = false;

        for (Member m : members) {
            if (m.getTotalBorrowCount() > 0) {
                System.out.println(m.getId() + " | " + m.getName() + " | Total Borrowings: " + m.getTotalBorrowCount());
                found = true;
            }
        }
        if (!found)
            System.out.println("No member has borrowed any books yet.");
    }

    private void viewOverdueBooks() {
        System.out.println("\n----------- OVERDUE BOOKS -----------");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String reportDateStr = LocalDate.now().format(formatter);
        System.out.println("Report Date: " + reportDateStr);

        List<String> reportLines = this.borrowManager.getOverdueBooksReport(reportDateStr);
        if (reportLines.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }

        System.out.printf("%-10s %-25s %-10s %-20s %-15s %-15s\n",
                "Book ID", "Title", "Member ID", "Member Name", "Due Date", "Days Overdue");
        System.out.println(
                "--------------------------------------------------------------------------------------------------");
        for (String line : reportLines) {
            System.out.println(line);
        }
    }

    // ================= HELPER METHODS =================
    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(this.sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number. Please enter again.");
            }
        }
    }
}
