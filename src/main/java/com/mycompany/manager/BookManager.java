package com.mycompany.manager;

import com.mycompany.entity.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for Book operations in the library management system.
 */
public class BookManager extends BaseManager<Book> {

    public BookManager() {
        super();
    }

    public Book getBookById(String id) {
        for (Book b : this.items) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    public String addBook(Book book) {
        if (book.getId().isEmpty()) return "Fail: Book ID cannot be empty.";
        if (this.getBookById(book.getId()) != null) return "Fail: Book ID already exists.";
        if (book.getTitle().isEmpty()) return "Fail: Title cannot be empty.";
        if (book.getAuthor().isEmpty()) return "Fail: Author cannot be empty.";
        if (book.getGenre().isEmpty()) return "Fail: Genre cannot be empty.";
        if (book.getQuantity() < 0) return "Fail: Quantity cannot be negative.";

        this.items.add(book);
        return "Success";
    }

    public String updateBookQuantity(String id, String quantityInput) {
        Book b = this.getBookById(id);
        if (b == null) return "Fail: Book not found.";

        if (!quantityInput.isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(quantityInput);
                if (newQuantity < 0) return "Fail: Quantity cannot be negative.";
                b.setQuantity(newQuantity);
            } catch (Exception e) {
                return "Fail: Invalid number.";
            }
        }
        return "Success";
    }

    public String removeBook(String id, boolean isBorrowed) {
        Book b = this.getBookById(id);
        if (b == null) return "Fail: Book not found.";
        if (isBorrowed) return "Fail: Cannot remove this book because it is currently borrowed.";

        this.items.remove(b);
        return "Success";
    }

    public List<Book> getAllBooks() {
        return this.getAll();
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        for (Book b : this.items) {
            if (b.getTitle().toLowerCase().contains(keyword)
                    || b.getAuthor().toLowerCase().contains(keyword)
                    || b.getGenre().toLowerCase().contains(keyword)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> getPopularBooksSimple() {
        List<Book> sorted = new ArrayList<>(this.items);
        sorted.sort((b1, b2) -> Integer.compare(b2.getBorrowCount(), b1.getBorrowCount()));
        return sorted;
    }

    public void addLoadedBook(Book b) {
        this.addLoadedItem(b);
    }
}
