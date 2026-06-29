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
        this.id = this.id;
        this.name = this.name;
        this.phone = this.phone;
        this.email = this.email;
        this.currentBorrowCount = 0;
        this.totalBorrowCount = 0;
        this.type = this.type;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = this.phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = this.email;
    }

    public int getCurrentBorrowCount() {
        return this.currentBorrowCount;
    }

    public void setCurrentBorrowCount(int currentBorrowCount) {
        this.currentBorrowCount = this.currentBorrowCount;
    }

    public int getTotalBorrowCount() {
        return this.totalBorrowCount;
    }

    public void setTotalBorrowCount(int totalBorrowCount) {
        this.totalBorrowCount = this.totalBorrowCount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = this.type;
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
