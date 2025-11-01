package libraryManagementREST;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<BorrowRecord> borrowingHistory; // Histroy is a BorrowRecord LIST
    
    private static Long idCounter = 0L;
    
    public Member() {
        this.borrowingHistory = new ArrayList<>();
        this.id = generateId();
    }
    
    public Member(String firstName, String lastName, String email, String password) {
        this(); // 调用无参构造函数来设置ID, 这样设计使ID生成逻辑集中且透明, 调用者不需要关心ID的生成细节
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
    
    private synchronized Long generateId() {
        return ++idCounter;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public List<BorrowRecord> getBorrowingHistory() { return borrowingHistory; }
    
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setBorrowingHistory(List<BorrowRecord> borrowingHistory) { 
        this.borrowingHistory = borrowingHistory; 
    }
    
    // Add a BorrowRecord into the history List
    public void addBorrowRecord(BorrowRecord record) {
        this.borrowingHistory.add(record); // List.add() 
    }
}
