package itemSearch;

public class ItemSearchQueries {

    static String searchAvailableItems = "SELECT b.isbn, b.title, b.author, b.keyword, language, publisher, \"n/a\" as actors, \"n/a\" as ageRestriction, \"n/a\" as country, count(IF(status = 0, 1, null)) as available FROM Book b JOIN ItemCopy i \n" +
            "on b.isbn = i.ISBN_ItemCopy \n" +
            "GROUP by ISBN_ItemCopy\n" +
            "HAVING available > 0\n" +
            "\n" +
            "\n" +
            "UNION\n" +
            "\n" +
            "SELECT \"n/a\" as isbn, d.title, d.director, d.genre, language, \"n/a\" as publisher, actors, ageRestriction, country, count(IF(status = 0, 1, null)) as available FROM Dvd d JOIN ItemCopy i\n" +
            "on d.id = i.dvdID_ItemCopy\n" +
            "GROUP by dvdID_ItemCopy\n" +
            "HAVING available > 0;\n";

    static String searchAllItems = "SELECT b.isbn, b.title, b.author, b.keyword, language, publisher, \"n/a\" as actors, \"n/a\" as ageRestriction, \"n/a\" as country, count(IF(status = 0, 1, null)) as available FROM Book b JOIN ItemCopy i \n" +
            "on b.isbn = i.ISBN_ItemCopy \n" +
            "GROUP by ISBN_ItemCopy\n" +
            "\n" +
            "UNION\n" +
            "\n" +
            "SELECT \"n/a\" as isbn, d.title, d.director, d.genre, language, \"n/a\" as publisher, actors, ageRestriction, country, count(IF(status = 0, 1, null)) as available FROM Dvd d JOIN ItemCopy i\n" +
            "on d.id = i.dvdID_ItemCopy\n" +
            "GROUP by dvdID_ItemCopy;\n";

    static String newSearchAllItems = "SELECT b.isbn, b.title, b.author, b.keyword, language, publisher, \"n/a\" as actors, \"n/a\" as ageRestriction, \"n/a\" as country, count(ISBN_ItemCopy) as totalCopies, count(IF(status = 0, 1, null)) as available FROM Book b JOIN ItemCopy i \n" +
            "on b.isbn = i.ISBN_ItemCopy \n" +
            "GROUP by ISBN_ItemCopy\n" +
            "\n" +
            "UNION\n" +
            "\n" +
            "SELECT \"n/a\" as isbn, d.title, d.director, d.genre, language, \"n/a\" as publisher, actors, ageRestriction, country, count(dvdID_ItemCopy) as totalCopies, count(IF(status = 0, 1, null)) as available FROM Dvd d JOIN ItemCopy i\n" +
            "on d.id = i.dvdID_ItemCopy\n" +
            "GROUP by dvdID_ItemCopy;\n";
}
