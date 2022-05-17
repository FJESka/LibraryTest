package adminInterface;

public class AdminQueries {

    static String copies = "SELECT * FROM ItemCopy;";

    static String getInsertQuery(String barcode, int loanPeriod, String isbn, Integer dvdID, String copyType, int status){
        String insert = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`,`dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (" + barcode + ", '" + loanPeriod + "','" + isbn + "'," + dvdID + ", '" + copyType + "', " + status + ")";

        return insert;
    }

//    static String insert = "\"INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`) VALUES " + (String, int, String, int, String, int);";
}
