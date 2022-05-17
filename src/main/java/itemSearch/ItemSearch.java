package itemSearch;

public class ItemSearch {
    String isbn;
    String title;
    String author;
    String keyword;
    String language;
    String publisher;
    String actors;
    String ageRestriction;
    String country;
    Integer totalCopies;
    Integer available;

    public ItemSearch(String isbn, String title, String author, String keyword, String language, String publisher, String actors, String ageRestriction, String country, Integer totalCopies, Integer available) {

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.keyword = keyword;
        this.language = language;
        this.publisher = publisher;
        this.actors = actors;
        this.ageRestriction = ageRestriction;
        this.country = country;
        this.totalCopies = totalCopies;
        this.available = available;
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

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(String ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}
