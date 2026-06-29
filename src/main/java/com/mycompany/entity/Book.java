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
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.quantity = quantity;
        this.borrowCount = 0;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBorrowCount() {
        return this.borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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
