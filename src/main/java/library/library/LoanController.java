package library.library;

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
import java.util.Collection;

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

            String updateItemcopyQuery = "UPDATE ItemCopy SET status = 1 WHERE barcode = " + barcodeVariable[0] + ";";

            System.out.println(updateItemcopyQuery);

            String selectLoanQuery = "SELECT * FROM Loan INNER JOIN ItemCopy ON Loan.barcode = ItemCopy.barcode INNER JOIN Member ON Loan.memberID= Member.memberID  WHERE ItemCopy.barcode = " + barcodeVariable[0] + " AND Loan.memberID = " + loginform.SQLLoginCode.Member() + " AND dateOfLoan >= CURDATE() AND returnDate = NULL;";

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

            String findBarcodeQuery = "SELECT ItemCopy.barcode, ItemCopy.status, Book.title FROM ItemCopy INNER JOIN Book ON ItemCopy.ISBN = Book.ISBN WHERE ItemCopy.status = 0 AND ItemCopy.barcode = '" + searchBarcodeTextField.getText() + "';";
            System.out.println(findBarcodeQuery);
            PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(findBarcodeQuery);
            ResultSet rs = preparedStatement.executeQuery();

            boolean availableForLoan = false;

            while (rs.next()) {
                if (!loanList.contains(rs.getString("barcode") + rs.getString("status") + rs.getString("title"))) {
                    loanList.add(rs.getString("barcode") + "  " + rs.getString("title"));

                    populateLoanList();

                    availableForLoan = true;
                }

                //Får ej detta att funka...
                else if (loanList.contains("barcode")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("The item already exists in the list.");
                    alert.showAndWait();
                }

                else if (!availableForLoan) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong barcode, the item is not available or the barcode does not exists. Try again.");
                    alert.showAndWait();
                }
            }
        }

        private ArrayList<String> loanList = new ArrayList<>();
        void populateLoanList() {
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



