package libraryManagementREST;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/Library")
public class LibraryResource {
    
	//此时调用getInstance()，如果是饿汉式，实例已经存在
    private LibraryService libraryService = LibraryService.getInstance();

    // 1. Get all books - JSON format
    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }
    
    // 2. Get book details by ID - JSON format (standard REST approach)
    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookById(@PathParam("id") int id) {
        return libraryService.getBookById((long)id);
    }
    
    // 3. Search books - using QueryParam
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> searchBooks(
        @QueryParam("title") String title,
        @QueryParam("author") String author,
        @QueryParam("genre") String genre) {
        
        List<Book> results;
        
        if (title != null) {
            results = libraryService.searchBooksByTitle(title);
        } else if (author != null) {
            results = libraryService.searchBooksByAuthor(author);
        } else if (genre != null) {
            results = libraryService.getBooksByGenre(genre);
        } else {
            results = libraryService.getAllBooks();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("searchResults", results);
        response.put("count", results.size());
        
        return response;
    }
    
    // 4. Member registration
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> registerMember(Member member) {
        Member newMember = libraryService.registerMember(
            member.getFirstName(), 
            member.getLastName(), 
            member.getEmail(), 
            member.getPassword()
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member registered successfully");
        response.put("memberId", newMember.getId());
        response.put("email", newMember.getEmail());
        
        return response;
    }
    
    // 5. Member login
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> loginMember(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Member member = libraryService.loginMember(email, password);
        
        Map<String, Object> response = new HashMap<>();
        if (member != null) {
            response.put("message", "Login successful");
            response.put("memberId", member.getId());
            response.put("firstName", member.getFirstName());
            response.put("lastName", member.getLastName());
        } else {
            response.put("error", "Invalid email or password");
        }
        
        return response;
    }
    
    // 6. Borrow a book
    @POST
    @Path("/borrow/{memberId}/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> borrowBook(
        @PathParam("memberId") int memberId, 
        @PathParam("bookId") int bookId) {
        
        BorrowRecord record = libraryService.borrowBook((long)memberId, (long)bookId);
        
        Map<String, Object> response = new HashMap<>();
        if (record != null) {
            response.put("message", "Book borrowed successfully");
            response.put("borrowRecordId", record.getId());
            response.put("dueDate", record.getDueDate());
            response.put("bookTitle", record.getBookTitle());
        } else {
            response.put("error", "Cannot borrow book");
        }
        
        return response;
    }
    
    // 7. Return a book
    @POST
    @Path("/return/{memberId}/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> returnBook(
        @PathParam("memberId") int memberId, 
        @PathParam("bookId") int bookId) {
        
        boolean success = libraryService.returnBook((long)memberId, (long)bookId);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("message", "Book returned successfully");
        } else {
            response.put("error", "Cannot return book");
        }
        
        return response;
    }
    
    // 8. Get borrowing history
    @GET
    @Path("/history/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getBorrowingHistory(@PathParam("memberId") int memberId) {
        List<BorrowRecord> history = libraryService.getBorrowingHistory((long)memberId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("memberId", memberId);
        response.put("borrowingHistory", history);
        response.put("totalRecords", history.size());
        
        return response;
    }
    
    // 9. Get books list (plain text format) - demonstrate different Produces type
    @GET
    @Path("/booksList")
    @Produces(MediaType.TEXT_PLAIN)
    public String displayBooksList() {
        List<Book> books = libraryService.getAllBooks();
        
        StringBuilder response = new StringBuilder();
        response.append("Library Books List:\n");
        
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            response.append("Book ")
                    .append(i)
                    .append(": [id=")
                    .append(book.getId())
                    .append(", title=")
                    .append(book.getTitle())
                    .append(", author=")
                    .append(book.getAuthor())
                    .append(", available=")
                    .append(book.getIsAvailable())
                    .append("]\n");
        }
        
        return response.toString();
    }    
}
