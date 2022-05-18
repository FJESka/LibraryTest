package library.library;

import bookSearch.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static bookSearch.DatabaseConnection.getConnection;

public class LoanController {
    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private Button removeItemFromListBtn;

    @FXML
    TextField searchBarcodeTextField;

    @FXML
    private Button searchBtn;

    @FXML
    private ListView<String> titleList;

    @FXML
    void cancelLoan(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.setScene(new Scene(root, 600, 400));

    }

    //MÅSTE TESTA MED DATABASEN OCH LOGIN FUNKTIONEN OM DET FUNKAR SOM DET SKA...

    @FXML
    void confirmLoan(ActionEvent event) throws IOException {

        for (int i = 0; i < loanList.size(); i++) {
            String[] barcodeVariable = loanList.get(i).split(" ");

            String insertLoanQuery = "INSERT INTO Loan (barcode, memberID, dateOfLoan, dueDate) VALUES (" + barcodeVariable[0] + ", " + loginform.SQLLoginCode.Member() + ", NOW(), (SELECT CURDATE() + INTERVAL (SELECT loanPeriod FROM ItemCopy WHERE barcode = " + barcodeVariable[0] + ")DAY ))";

            String updateItemcopyQuery = "UPDATE ItemCopy SET status = 'Not available' WHERE barcode = " + barcodeVariable[0] + ";";

            System.out.println(updateItemcopyQuery);

            String selectLoanQuery = "SELECT loanID, loan.barcode, loan.memberID, dateOfLoan, dueDate, returnDate, title FROM loan INNER JOIN itemcopy ON loan.barcode = itemcopy.barcode INNER JOIN book ON itemcopy.ISBN = book.ISBN INNER JOIN member ON loan.memberID = member.memberID WHERE loan.barcode = " + barcodeVariable[0] + " AND loan.memberID = " + loginform.SQLLoginCode.Member() + " AND dateOfLoan >= CURDATE() AND returnDate IS NULL";
            try {
                PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(updateItemcopyQuery);
                preparedStatement.executeUpdate(updateItemcopyQuery);

                PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(insertLoanQuery);
                ps.executeUpdate(insertLoanQuery);

                System.out.println(selectLoanQuery);
                PreparedStatement preparedStatement2 = JDBCConnection.jdbcConnection().prepareStatement(selectLoanQuery);
                ResultSet rs = preparedStatement2.executeQuery();

                while (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Confirmation");
                    alert.setContentText("The loan was completed, see details below: " + "\n" + "LoanID: " + rs.getString("loanID") + "   " + "Barcode: " + rs.getString("barcode") +"   " + "Title: " + rs.getString("title") + "   " + "MemberID: " + rs.getString("memberID") + "\n" + "Date of loan: " + rs.getString("dateOfLoan") + "\n" + "Due date: " + rs.getString("dueDate"));
                    alert.showAndWait();
                }

                cancelLoan(event);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        @FXML
        void searchItem (ActionEvent event) throws SQLException {
            String checkIfBarcodeExistsQuery = "SELECT barcode FROM ItemCopy WHERE ItemCopy.barcode = '" + searchBarcodeTextField.getText() + "';";
            System.out.println(checkIfBarcodeExistsQuery);
            //PreparedStatement ps = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfBarcodeExistsQuery);
            PreparedStatement ps = getConnection().getDBConnection().prepareStatement(checkIfBarcodeExistsQuery);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String checkIfItemcopyIsAvailable = "SELECT status FROM ItemCopy WHERE ItemCopy.status = 1 AND ItemCopy.barcode = '" + searchBarcodeTextField.getText() + "';";
                //PreparedStatement ps1 = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfItemcopyIsAvailable);
                PreparedStatement ps1 = getConnection().getDBConnection().prepareStatement(checkIfItemcopyIsAvailable);
                ResultSet rs1 = ps1.executeQuery();

                if (rs1.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong barcode, the item is not available. Try again.");
                    alert.showAndWait();
                }

                String findBarcodeQuery = "SELECT ItemCopy.barcode, ItemCopy.status, Book.title FROM ItemCopy INNER JOIN Book ON ItemCopy.ISBN_ItemCopy = Book.isbn WHERE ItemCopy.status = 0 AND ItemCopy.barcode = '" + searchBarcodeTextField.getText() + "';";
                System.out.println(findBarcodeQuery);
                //PreparedStatement preparedStatement = DatabaseConnection.getConnection().databaseLink.prepareStatement(findBarcodeQuery);
                PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(findBarcodeQuery);
                ResultSet rs2 = preparedStatement.executeQuery();

                while (rs2.next()) {
                    if (!loanList.contains(rs2.getString("barcode") + "             " + rs2.getString("title"))) {
                        loanList.add(rs2.getString("barcode") + "             " + rs2.getString("title"));
                        addItemToLoanList();
                        searchBarcodeTextField.clear();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("The item is already added to the list!");
                        alert.showAndWait();
                        searchBarcodeTextField.clear();
                    }
                }
            }
            else if (searchBarcodeTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("You need to type in a barcode!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Wrong barcode, the barcode does not exists. Try again.");
                alert.showAndWait();
            }
        }

        private ArrayList<String> loanList = new ArrayList<>();
        void addItemToLoanList() {
                ObservableList<String> loanListBarcodes = FXCollections.observableArrayList(loanList);
                titleList.setItems(loanListBarcodes);
        }

        @FXML
        void removeItemFromList(ActionEvent event) {

            if (!titleList.getSelectionModel().isEmpty()) {
                int selectedID = titleList.getSelectionModel().getSelectedIndex();
                titleList.getItems().remove(selectedID);
                loanList.remove(selectedID);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("You need to select the item you would like to remove from the list!");
                alert.showAndWait();
            }
        }
    }



