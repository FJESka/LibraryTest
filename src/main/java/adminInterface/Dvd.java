package adminInterface;

public class Dvd {
    private int dvdID;
    private String title;
    private String director;
    private String genre;
    private String language;
    private String actors;
    private String ageRestriction;
    private String country;

    //dvd constructor
    public Dvd(int dvdID, String title, String director, String genre, String language, String actors, String ageRestriction, String country) {
        this.dvdID = dvdID;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.language = language;
        this.actors = actors;
        this.ageRestriction = ageRestriction;
        this.country = country;
    }

    public int getDvdID() {
        return dvdID;
    }

    public void setDvdID(int dvdID) {
        this.dvdID = dvdID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
}
