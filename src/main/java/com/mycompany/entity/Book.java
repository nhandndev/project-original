package com.mycompany.entity;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private int quantity;
    private int borrowCount;
    
    // Properties for physical books
    private double weight;
    private String shelfLocation;
    
    // Properties for ebooks
    private double fileSize;
    private String format;
    
    private String type; // "physical" or "ebook"

    public Book(String id, String title, String author, String genre, int year, int quantity, String type) {
        this.id = this.id;
        this.title = this.title;
        this.author = this.author;
        this.genre = this.genre;
        this.year = this.year;
        this.quantity = this.quantity;
        this.borrowCount = 0;
        this.type = this.type;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = this.author;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = this.genre;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = this.year;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = this.quantity;
    }

    public int getBorrowCount() {
        return this.borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = this.borrowCount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = this.type;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = this.weight;
    }

    public String getShelfLocation() {
        return this.shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = this.shelfLocation;
    }

    public double getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = this.fileSize;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = this.format;
    }

    public void increaseQuantity() {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if ("ebook".equalsIgnoreCase(this.type)) {
            // Ebooks don't decrease in stock
            return;
        }
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public void incrementBorrowCount() {
        this.borrowCount++;
    }
}
