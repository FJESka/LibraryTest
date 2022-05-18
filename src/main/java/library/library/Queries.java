package library.library;

public class Queries {

    public static String getLoans = "SELECT l.loanID, i.barcode, i.status, b.title FROM Loan l JOIN ItemCopy i on l.barcode = i.barcode JOIN Member m on l.memberID = m.memberID JOIN Book b on i.ISBN_ItemCopy = b.ISBN\n" +
            "UNION\n" +
            "SELECT l.loanID, i.barcode, i.status, d.title FROM Loan l JOIN ItemCopy i on l.barcode = i.barcode JOIN Member m on l.memberID = m.memberID JOIN Dvd d on i.dvdID_ItemCopy = d.id;\n";

    public static String insertLoanQuery(String[] barcodeVariable){
        String insertLoanQuery = "INSERT INTO Loan (barcode, memberID, dateOfLoan, dueDate) VALUES (" + barcodeVariable[0] + ", " + loginform.SQLLoginCode.Member() + ", NOW(), (SELECT CURDATE() + INTERVAL (SELECT loanPeriod FROM ItemCopy WHERE barcode = " + barcodeVariable[0] + ")DAY ))";
    return insertLoanQuery;
    }

    public static String LoanUpdateItemcopyQuery(String[] barcodeVariable) {
        String updateItemcopyQuery = "UPDATE ItemCopy SET status = 'Not available' WHERE barcode = " + barcodeVariable[0] + ";";
    return updateItemcopyQuery;
    }

    public static String selectLoanQuery(String[] barcodeVariable) {
        String selectLoanQuery = "SELECT loanID, loan.barcode, loan.memberID, dateOfLoan, dueDate, returnDate, title FROM loan INNER JOIN ItemCopy ON loan.barcode = ItemCopy.barcode INNER JOIN book ON ItemCopy.ISBN_ItemCopy = book.isbn INNER JOIN member ON loan.memberID = member.memberID WHERE loan.barcode = " + barcodeVariable[0] + " AND loan.memberID = " + loginform.SQLLoginCode.Member() + " AND dateOfLoan >= CURDATE() AND returnDate IS NULL";
    return selectLoanQuery;
    }

    public static String LoanCheckIfBarcodeExistsQuery(String searchBarcodeTextField) {
        String checkIfBarcodeExistsQuery = "SELECT barcode FROM ItemCopy WHERE ItemCopy.barcode = '" + searchBarcodeTextField + "';";
    return checkIfBarcodeExistsQuery;
    }

    public static String LoanCheckIfItemCopyIsAvailable(String searchBarcodeTextField) {
        String checkIfItemCopyIsAvailable = "SELECT status FROM ItemCopy WHERE ItemCopy.status = 1 AND ItemCopy.barcode = '" + searchBarcodeTextField + "';";
    return checkIfItemCopyIsAvailable;
    }

    public static String LoanFindBarcodeQuery(String searchBarcodeTextField) {
        String findBarcodeQuery = "SELECT ItemCopy.barcode, ItemCopy.status, Book.title FROM ItemCopy INNER JOIN Book ON ItemCopy.ISBN_ItemCopy = Book.isbn WHERE ItemCopy.status = 0 AND ItemCopy.barcode = '" + searchBarcodeTextField + "';";
    return findBarcodeQuery;
    }

    public static String ReturnItemUpdateItemQuery(String[] currentItemBarcode) {
        String updateItemcopyQuery = "UPDATE ItemCopy SET status = 'Available' WHERE barcode = '" + currentItemBarcode[0] + "';";
    return updateItemcopyQuery;
    }

    public static String ReturnItemUpdateLoanQuery(String[] currentItemBarcode) {
        String updateLoanQuery = "UPDATE loan SET returnDate = CURDATE() WHERE barcode = '" + currentItemBarcode[0] + "';";
    return updateLoanQuery;
    }

    public static String checkIfBarcodeExistsQuery(String searchItemTextField) {
        String checkIfBarcodeExistsQuery = "SELECT barcode FROM ItemCopy WHERE ItemCopy.barcode = '" + searchItemTextField + "';";
    return checkIfBarcodeExistsQuery;
    }

    public static String checkIfItemcopyIsNotAvailable(String searchItemTextField) {
        String checkIfItemcopyIsNotAvailable = "SELECT status FROM ItemCopy WHERE ItemCopy.status = 'Available' AND ItemCopy.barcode = '" + searchItemTextField + "';";
    return checkIfItemcopyIsNotAvailable;
    }

    public static String findBarcodeQuery(String searchItemTextField) {
        String findBarcodeQuery = "SELECT loan.loanID, ItemCopy.barcode, book.title, ItemCopy.status FROM loan INNER JOIN ItemCopy ON loan.barcode = ItemCopy.barcode INNER JOIN book ON ItemCopy.ISBN_ItemCopy = book.isbn WHERE loan.barcode = '" + searchItemTextField + "';";
    return findBarcodeQuery;
    }
}