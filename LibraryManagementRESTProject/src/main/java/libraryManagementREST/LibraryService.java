package libraryManagementREST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 饿汉式
public class LibraryService {
    private static final LibraryService instance = new LibraryService();
//    使用static不需要创建LibraryService对象就能访问
//    LibraryService.getInstance(); // ✓ 直接通过类名调用
//    如果是非static，就需要：
//    LibraryService lib = new LibraryService(); // 这违背了单例模式！
//    lib.getInstance(); // ❌ 错误的方式
    // private: 隐藏实现	  if not, 外部可以直接修改：LibraryService.instance = null; // 破坏单例！
    // static:  类级别唯一 if not, 需要先创建对象才能访问，违背单例原则
    // final:   不可改变  if not, 可能被重新赋值：instance = null; 或 instance = anotherInstance;
    
    private Map<Long, Member> members;
    private Map<Long, Book> books;
    private Map<Long, BorrowRecord> borrowRecords;
    
 // private构造函数：防止外部new创建实例
    private LibraryService() {
        this.members = new HashMap<>();
        this.books = new HashMap<>();
        this.borrowRecords = new HashMap<>();
        Book.resetCounter(); // 每次新建 LibraryService 时重置 Book ID
        initializeSampleData(); // put data into books and members when constructor is called 
    }
    
    // 唯一的公共访问点
    public static LibraryService getInstance() {
        return instance;
    }
    
    private void initializeSampleData() {
        // book examples
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 
                             "A classic novel about the American Dream", 1925, 5);
        Book book2 = new Book("1984", "George Orwell", "Dystopian", 
                             "A dystopian social science fiction novel", 1949, 3);
        Book book3 = new Book("To Kill a Mockingbird", "Harper Lee", "Fiction", 
                             "A novel about racial inequality", 1960, 4);
        
        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        books.put(book3.getId(), book3);
        
        // member example
        Member member = new Member("John", "Doe", "john.doe@example.com", "password123");
        members.put(member.getId(), member);
    }
    
    // Members
    public Member registerMember(String firstName, String lastName, String email, String password) {
        Member member = new Member(firstName, lastName, email, password);
        members.put(member.getId(), member);
        return member;
    }
    
    public Member loginMember(String email, String password) {
        return members.values().stream()
                			   .filter(m -> m.getEmail().equals(email) && m.getPassword().equals(password))
			                   .findFirst()
			                   .orElse(null);
    }
    
    public Member getMemberById(Long id) {
        return members.get(id); // Map.get(key)
    }
    
    // Books
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values()); // Map.values() returns a collection
    }
    
    public Book getBookById(Long id) {
        return books.get(id); // Map.get(key)
    }
    
    public List<Book> searchBooksByTitle(String query) {
        return books.values().stream()
                			 .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                			 .collect(Collectors.toList()); 
        // collect 是 Stream 的一个终端操作，用来把流里的元素“收拢”成一个结果（比如 List、Set、Map、字符串、统计值等）。
        // Collectors.toList() 返回一个收集器（Collector），它的作用是：把流中的元素收集进一个新的 List 并返回。
        //.collect(Collectors.toList()) 是一个经典的Stream终端操作，它充当了从流世界到集合世界的桥梁。它将经过各种中间操作（如 filter, map, sorted 等）处理后的元素，规约并封装到一个新的、可变的 List 集合中，以便后续使用。
        //在你的代码中，它正是将过滤后所有符合条件的 Book 对象，打包成一个 List<Book> 并返回给调用者。
    }
    
    public List<Book> searchBooksByAuthor(String query) {
        return books.values().stream()
                			 .filter(book -> book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                			 .collect(Collectors.toList());
    }
    
    public List<Book> getBooksByGenre(String genre) {
        return books.values().stream()
                			 .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                			 .collect(Collectors.toList());
        // equalsIgnoreCase() 方法	精确匹配、忽略大小写
		//特点：只有体裁完全等于搜索词才会匹配
		//示例：搜索"fiction"只会匹配体裁正好是 "Fiction" 的书籍
        //如果使用 contains()可能错误匹配："Science Fiction", "Historical Fiction" 等
    }
    
    // borrow and return
    public BorrowRecord borrowBook(Long memberId, Long bookId) {
        Member member = members.get(memberId);
        Book book = books.get(bookId);
        
        if (member == null || book == null || book.getAvailableCopies() <= 0) {
            return null;
        }
        
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        BorrowRecord record = new BorrowRecord(memberId, bookId, book.getTitle());
        borrowRecords.put(record.getId(), record);
        member.addBorrowRecord(record);
        
        return record;
    }
    
    public boolean returnBook(Long memberId, Long bookId) {
        BorrowRecord record = borrowRecords.values().stream()
                .filter(r -> r.getMemberId().equals(memberId) && 
                           r.getBookId().equals(bookId) && 
                           r.getReturnDate() == null)
                .findFirst()
                .orElse(null);
        
        if (record == null) return false;
        
        record.markReturned();
        Book book = books.get(bookId);
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }
        
        return true;
    }
    
    public List<BorrowRecord> getBorrowingHistory(Long memberId) {
        Member member = members.get(memberId);
        return member != null ? member.getBorrowingHistory() : new ArrayList<>();
    }
}
