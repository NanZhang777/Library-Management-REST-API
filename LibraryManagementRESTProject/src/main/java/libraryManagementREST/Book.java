package libraryManagementREST;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String description;
    private Integer publishedYear;
    private Integer totalCopies;
    private Integer availableCopies;
    
    private static Long lastId = 0L; // set Book id initial value to 0 
    
    public Book() {
        this.id = generateId();
    }
    
    public Book(String title, String author, String genre, String description, 
                Integer publishedYear, Integer totalCopies) {
        this();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.publishedYear = publishedYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    private synchronized Long generateId() {
        return ++lastId; // when generatedId is call, the first Book id is 1
    }
    
    public static void resetCounter() {
        lastId = 0L;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
    public Integer getPublishedYear() { return publishedYear; }
    public Integer getTotalCopies() { return totalCopies; }
    public Integer getAvailableCopies() { return availableCopies; }
    public Boolean getIsAvailable() { return availableCopies > 0; }
    
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDescription(String description) { this.description = description; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
}
