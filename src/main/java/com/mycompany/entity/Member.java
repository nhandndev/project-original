package com.mycompany.entity;

public class Member {
    private String id;
    private String name;
    private String phone;
    private String email;
    private int currentBorrowCount;
    private int totalBorrowCount;
    
    private String type; // "regular" or "premium"

    public Member(String id, String name, String phone, String email, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentBorrowCount = 0;
        this.totalBorrowCount = 0;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCurrentBorrowCount() {
        return this.currentBorrowCount;
    }

    public void setCurrentBorrowCount(int currentBorrowCount) {
        this.currentBorrowCount = currentBorrowCount;
    }

    public int getTotalBorrowCount() {
        return this.totalBorrowCount;
    }

    public void setTotalBorrowCount(int totalBorrowCount) {
        this.totalBorrowCount = totalBorrowCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void incrementCurrentBorrowCount() {
        this.currentBorrowCount++;
    }

    public void decrementCurrentBorrowCount() {
        if (this.currentBorrowCount > 0) {
            this.currentBorrowCount--;
        }
    }

    public void incrementTotalBorrowCount() {
        this.totalBorrowCount++;
    }

    public int getBorrowLimit() {
        if ("premium".equalsIgnoreCase(this.type)) {
            return 5;
        }
        return 3; // regular
    }

    public long calculateFine(long daysOverdue) {
        if ("premium".equalsIgnoreCase(this.type)) {
            return daysOverdue * 2500;
        }
        return daysOverdue * 5000; // regular
    }

    public int getBorrowPeriod() {
        if ("premium".equalsIgnoreCase(this.type)) {
            return 14;
        }
        return 7; // regular
    }
}
