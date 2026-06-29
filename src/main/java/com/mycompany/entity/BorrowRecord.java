package com.mycompany.entity;

public class BorrowRecord {
    private String memberId;
    private String bookId;
    private String borrowDate;
    private boolean isReturned;
    
    private String type; // "physical" or "ebook"
    
    // Properties for physical borrow records
    private String conditionOut;
    private String conditionIn;
    
    // Properties for ebook borrow records
    private String downloadLink;

    public BorrowRecord(String memberId, String bookId, String borrowDate, String type) {
        this.memberId = this.memberId;
        this.bookId = this.bookId;
        this.borrowDate = this.borrowDate;
        this.isReturned = false;
        this.type = this.type;
        if ("physical".equalsIgnoreCase(this.type)) {
            this.conditionOut = "Good";
            this.conditionIn = "";
        }
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = this.memberId;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = this.bookId;
    }

    public String getBorrowDate() {
        return this.borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = this.borrowDate;
    }

    public boolean isReturned() {
        return this.isReturned;
    }

    public void setReturned(boolean returned) {
        this.isReturned = returned;
    }
    
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = this.type;
    }

    public String getConditionOut() {
        return this.conditionOut;
    }

    public void setConditionOut(String conditionOut) {
        this.conditionOut = this.conditionOut;
    }

    public String getConditionIn() {
        return this.conditionIn;
    }

    public void setConditionIn(String conditionIn) {
        this.conditionIn = this.conditionIn;
    }

    public String getDownloadLink() {
        return this.downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = this.downloadLink;
    }

    public long calculateFine(long daysOverdue, Member member) {
        if ("ebook".equalsIgnoreCase(this.type)) {
            return 0; // Ebooks never have overdue fines
        }
        return member.calculateFine(daysOverdue);
    }
}
