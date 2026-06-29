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
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.isReturned = false;
        this.type = type;
        if ("physical".equalsIgnoreCase(type)) {
            this.conditionOut = "Good";
            this.conditionIn = "";
        }
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrowDate() {
        return this.borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public boolean isReturned() {
        return this.isReturned;
    }

    public void setReturned(boolean returned) {
        this.isReturned = returned;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConditionOut() {
        return conditionOut;
    }

    public void setConditionOut(String conditionOut) {
        this.conditionOut = conditionOut;
    }

    public String getConditionIn() {
        return conditionIn;
    }

    public void setConditionIn(String conditionIn) {
        this.conditionIn = conditionIn;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public long calculateFine(long daysOverdue, Member member) {
        if ("ebook".equalsIgnoreCase(this.type)) {
            return 0; // Ebooks never have overdue fines
        }
        return member.calculateFine(daysOverdue);
    }
}
