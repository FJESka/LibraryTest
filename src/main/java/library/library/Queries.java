package library.library;

public class Queries {

    public static String getLoans = "SELECT l.loanID, i.barcode, i.status, b.title FROM Loan l JOIN ItemCopy i on l.barcode = i.barcode JOIN Member m on l.memberID = m.memberID JOIN Book b on i.ISBN_ItemCopy = b.ISBN\n" +
            "UNION\n" +
            "SELECT l.loanID, i.barcode, i.status, d.title FROM Loan l JOIN ItemCopy i on l.barcode = i.barcode JOIN Member m on l.memberID = m.memberID JOIN Dvd d on i.dvdID_ItemCopy = d.id;\n";
}
