package com.mycompany.manager;

import com.mycompany.entity.Book;
import com.mycompany.entity.Member;
import com.mycompany.entity.BorrowRecord;
import com.mycompany.entity.Ebook;
import com.mycompany.entity.EbookBorrowRecord;
import com.mycompany.entity.PhysicalBorrowRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for Borrow/Return operations in the library management system.
 * 
 * Business Rules Implemented:
 * - BR3: Validates that member exists before borrowing
 * - BR4: Validates that book is available (stock > 0) before borrowing
 * - BR5: Enforces borrowing limit (polymorphic - varies by member type)
 * - BR6: Validates borrow dates (current or past) and return dates (after borrow date)
 * - BR7: Calculates overdue fine using polymorphic calculateFine() method
 * - BR8: Updates book stock and member borrow counts
 * - BR9: Validates all inputs before processing
 */
public class BorrowManager extends BaseManager<BorrowRecord> {

    private BookManager bookManager = new BookManager();
    private MemberManager memberManager = new MemberManager();

    public BorrowManager() {
        super();
    }

    public BookManager getBookManager() {
        return this.bookManager;
    }

    public MemberManager getMemberManager() {
        return this.memberManager;
    }

    private boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isBookCurrentlyBorrowed(String bookId) {
        for (BorrowRecord r : this.items) {
            if (r.getBookId().equals(bookId) && !r.isReturned()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes a book borrowing transaction.
     * 
     * Implements validation for:
     * - BR3: Member must exist
     * - BR4: Book must be available (stock > 0)
     * - BR5: Member must not exceed borrowing limit (polymorphic)
     * - BR6: Borrow date must be current or in the past
     * - BR9: All inputs must be validated
     * 
     * Updates:
     * - BR8: Decreases book stock and increments borrow count
     * - BR8: Increments member's current and total borrow counts
     */
    public String borrowBook(String memberId, String bookId, String borrowDate) {
        // BR3: A member must exist before borrowing a book
        Member member = this.memberManager.getMemberById(memberId);
        if (member == null) return "Fail: Member does not exist.";
        
        // BR5: A book can only be borrowed if the member has not exceeded their borrowing limit (polymorphic)
        if (member.getCurrentBorrowCount() >= member.getBorrowLimit()) 
            return "Fail: This member has reached borrow limit.";

        // BR4: A book must be available (stock > 0) before it can be borrowed
        Book book = this.bookManager.getBookById(bookId);
        if (book == null) return "Fail: Book does not exist.";
        if (book.getQuantity() <= 0 && !(book instanceof Ebook)) return "Fail: Book is out of stock.";

        // BR6: Borrow date must be current or in the past
        // BR9: All inputs must be validated before processing
        if (borrowDate.isEmpty() || !this.isValidDate(borrowDate)) return "Fail: Invalid borrow date format.";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowLocalDate = LocalDate.parse(borrowDate, formatter);
        if (borrowLocalDate.isAfter(LocalDate.now())) return "Fail: Borrow date cannot be in the future.";

        if (book instanceof Ebook) {
            this.items.add(new EbookBorrowRecord(member.getId(), book.getId(), borrowDate, "http://library.com/download/" + book.getId()));
        } else {
            this.items.add(new PhysicalBorrowRecord(member.getId(), book.getId(), borrowDate));
        }

        // BR8: Book stock is reduced upon borrowing and increased upon returning
        book.decreaseQuantity();
        book.incrementBorrowCount();
        member.incrementCurrentBorrowCount();
        member.incrementTotalBorrowCount();

        return "Output: Book '" + book.getTitle() + "' borrowed by '" + member.getName() + "' successfully.";
    }

    private BorrowRecord findActiveBorrowRecord(String memberId, String bookId) {
        for (BorrowRecord r : this.items) {
            if (r.getMemberId().equals(memberId) && r.getBookId().equals(bookId) && !r.isReturned()) {
                return r;
            }
        }
        return null;
    }

    /**
     * Processes a book return transaction.
     * 
     * Implements validation for:
     * - BR6: Return date must be after borrow date
     * - BR9: All inputs must be validated
     * 
     * Calculates:
     * - BR7: Overdue fine if return date is after due date
     * 
     * Updates:
     * - BR8: Increases book stock and decrements member's current borrow count
     */
    public String returnBook(String memberId, String bookId, String returnDateStr) {
        // BR9: All inputs must be validated before processing
        Member member = this.memberManager.getMemberById(memberId);
        if (member == null) return "Fail: Member not found.";

        Book book = this.bookManager.getBookById(bookId);
        if (book == null) return "Fail: Book not found.";

        BorrowRecord record = this.findActiveBorrowRecord(memberId, bookId);
        if (record == null) return "Fail: This member is not borrowing this book.";

        // BR6: Return date must be after borrow date
        // BR9: All inputs must be validated before processing
        if (returnDateStr.isEmpty() || !this.isValidDate(returnDateStr)) return "Fail: Invalid return date format.";

        String borrowDateStr = record.getBorrowDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate borrowDate = LocalDate.parse(borrowDateStr, formatter);
        LocalDate returnDate = LocalDate.parse(returnDateStr, formatter);

        if (!returnDate.isAfter(borrowDate)) return "Fail: Return date must be after borrow date.";

        // BR7: Overdue fine is calculated using polymorphic method
        long fine = 0;
        int borrowPeriod = member.getBorrowPeriod();
        LocalDate dueDate = borrowDate.plusDays(borrowPeriod);
        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = record.calculateFine(daysOverdue, member);
        }

        record.setReturned(true);
        // BR8: Book stock is increased upon returning (was decreased when borrowed)
        book.increaseQuantity();
        member.decrementCurrentBorrowCount();

        String msg = "Output: Book '" + book.getTitle() + "' returned by '" + member.getName() + "'. ";
        msg += (fine > 0) ? "Overdue fine: " + fine + " VND." : "No overdue fine.";
        return msg;
    }

    public List<BorrowRecord> getCurrentlyBorrowedRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : this.items) {
            if (!r.isReturned()) result.add(r);
        }
        return result;
    }

    public List<BorrowRecord> getBorrowingHistoryByMember(String memberId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (BorrowRecord r : this.items) {
            if (r.getMemberId().equals(memberId)) result.add(r);
        }
        return result;
    }

    public List<String> getOverdueBooksReport(String reportDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate reportDate = LocalDate.parse(reportDateStr, formatter);

        List<String> result = new ArrayList<>();
        for (BorrowRecord r : this.items) {
            if (!r.isReturned()) {
                Member member = this.memberManager.getMemberById(r.getMemberId());
                if (member == null) continue;

                LocalDate borrowDate = LocalDate.parse(r.getBorrowDate(), formatter);
                int borrowPeriod = member.getBorrowPeriod();
                LocalDate dueDate = borrowDate.plusDays(borrowPeriod);

                if (reportDate.isAfter(dueDate)) {
                    long daysOverdue = ChronoUnit.DAYS.between(dueDate, reportDate);
                    Book book = this.bookManager.getBookById(r.getBookId());

                    // Calculate fine polymorphically
                    long fine = r.calculateFine(daysOverdue, member);

                    if (book != null && fine > 0) {
                        String line = String.format("%-10s %-25s %-10s %-20s %-15s %-15d",
                                r.getBookId(), book.getTitle(), r.getMemberId(),
                                member.getName(), dueDate.format(formatter), daysOverdue);
                        result.add(line);
                    }
                }
            }
        }
        return result;
    }

    public void addLoadedRecord(BorrowRecord r) {
        this.addLoadedItem(r);
    }

    public List<BorrowRecord> getAllRecords() {
        return this.getAll();
    }
}
