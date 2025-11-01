package libraryManagementREST;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowRecord {
    private Long id;
    private Long memberId;
    private Long bookId;
    private String bookTitle;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    
    private static Long lastId = 0L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public BorrowRecord() {
        this.id = generateId();
    }
    
    public BorrowRecord(Long memberId, Long bookId, String bookTitle) {
        this();
        this.memberId = memberId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = LocalDate.now().format(formatter);
        this.dueDate = LocalDate.now().plusWeeks(2).format(formatter);
        this.returnDate = null;
    }
    
    private synchronized Long generateId() {
        return ++lastId;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public Long getBookId() { return bookId; }
    public String getBookTitle() { return bookTitle; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnDate() { return returnDate; }
    
    public void setId(Long id) { this.id = id; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    
    public void markReturned() {
        this.returnDate = LocalDate.now().format(formatter);
    }
}
