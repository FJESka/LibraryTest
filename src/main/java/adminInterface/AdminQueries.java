package adminInterface;

public class AdminQueries {

    static int dvdID;
    static String title;
    static String genre;
    static String director;
    static String actors;
    static String language;
    static String ageRestriction;
    static String country;
    static String dvdIDBeforeUpdate;


    static String copies = "SELECT * FROM ItemCopy;";

    static String getInsertQuery(String barcode, int loanPeriod, String isbn, Integer dvdID, String copyType, int status){
        String insert = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`,`dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (" + barcode + ", '" + loanPeriod + "','" + isbn + "'," + dvdID + ", '" + copyType + "', " + status + ")";

        return insert;
    }

//    static String queryDvdisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + type + "', '" + status + "');";
//    static String query = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + dvdID + "', '" + type + "', '" + status + "');";
//    static String queryISBNisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + dvdID + "', '" + type + "', '" + status + "');";
//
//
//    static String updateDvdisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
//    static String updateISBNisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
//    static String update = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";

//    static String insert = "\"INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`) VALUES " + (String, int, String, int, String, int);";

    static String getDvdInsert(int dvdID, String title, String director, String genre, String language, String actors, String ageRestriction, String country){
        String insertDvd = "INSERT INTO `Dvd` (`id`, `title`, `director`, `genre`, `language`, `actors`,`ageRestriction`, `country`) VALUES ('" + dvdID + "', '" + title + "', '" + director +"', '" + genre + "', '" + language + "','" + actors + "', '" + ageRestriction+ "', '" + country+ "');";

        return insertDvd;
    }

    static String getDvdUpdate(int dvdID, String title, String director, String genre, String language, String actors, String ageRestriction, String country, String dvdIDBeforeUpdate){
        String updateDvd = "UPDATE `Dvd` SET `id` = '" + dvdID +"', `title` = '"+ title +"', `director` = '"+ director +"', `genre` = '"+ genre +"', `language` = '"+ language +"', `actors` = '"+ actors +"', `ageRestriction` = '"+ ageRestriction +"', `country` = '"+ country +"' WHERE (`id` = '" + dvdIDBeforeUpdate + "');";
        return updateDvd;
    }

    static String getDvdDelete(String dvdIDBeforeUpdate){
        String deleteDvd = "DELETE FROM `Dvd` WHERE (`id` = '" + dvdIDBeforeUpdate + "');";
        return deleteDvd;
    }


    static String insertDvd1 = "INSERT INTO `Dvd` (`id`, `title`, `director`, `genre`, `language`, `actors`,`ageRestriction`, `country`) VALUES ('" + dvdID + "', '" + title + "', '" + director +"', '" + genre + "', '" + language + "','" + actors + "', '" + ageRestriction+ "', '" + country+ "');";

    static String updateDvd = "UPDATE `Dvd` SET `id` = '" + dvdID +"', `title` = '"+ title +"', `director` = '"+ director +"', `genre` = '"+ genre +"', `language` = '"+ language +"', `actors` = '"+ actors +"', `ageRestriction` = '"+ ageRestriction +"', `country` = '"+ country +"' WHERE (`id` = '" + dvdIDBeforeUpdate + "');";

    static String deleteDvd = "DELETE FROM `Dvd` WHERE (`id` = '" + dvdIDBeforeUpdate + "');";
}
