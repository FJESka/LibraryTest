package bookSearch;

public class BookSearch {
    String isbn;
    String title;
    String author;
    String keyword;
    String language;
    String publisher;

    public BookSearch(String isbn,String title, String author, String keyword, String language, String publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.keyword = keyword;
        this.language = language;
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

