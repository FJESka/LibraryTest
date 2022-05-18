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

    private ArrayList<String> loanList = new ArrayList<>();

    // Method to cancel loan and change scene to itemSearch.
    @FXML
    void cancelLoan(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.setScene(new Scene(root, 600, 400));

    }

    // Method to confirm loan. Creates a new loan and shows receipt.
    @FXML
    void confirmLoan(ActionEvent event) throws IOException {

        for (int i = 0; i < loanList.size(); i++) {
            String[] barcodeVariable = loanList.get(i).split(" ");

            try {
                PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(Queries.LoanUpdateItemcopyQuery(barcodeVariable));
                preparedStatement.executeUpdate(Queries.LoanUpdateItemcopyQuery(barcodeVariable));

                PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(Queries.insertLoanQuery(barcodeVariable));
                ps.executeUpdate(Queries.insertLoanQuery(barcodeVariable));

                PreparedStatement preparedStatement2 = JDBCConnection.jdbcConnection().prepareStatement(Queries.selectLoanQuery(barcodeVariable));
                ResultSet rs = preparedStatement2.executeQuery();

                while (rs.next()) {
                    alertMessage(Alert.AlertType.CONFIRMATION,"The loan was completed, see details below: " + "\n" + "LoanID: " + rs.getString("loanID") + "   " + "Barcode: " + rs.getString("barcode") +"   " + "Title: " + rs.getString("title") + "   " + "MemberID: " + rs.getString("memberID") + "\n" + "Date of loan: " + rs.getString("dateOfLoan") + "\n" + "Due date: " + rs.getString("dueDate"));
                }
                cancelLoan(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to search item and add to listview. Tests if the barcode exits and if it is available for loan.
    @FXML
    void searchItem (ActionEvent event) throws SQLException {
        //PreparedStatement ps = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfBarcodeExistsQuery);
        PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(Queries.LoanCheckIfBarcodeExistsQuery(searchBarcodeTextField.getText()));
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            //PreparedStatement ps1 = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfItemcopyIsAvailable);
            PreparedStatement ps1 = JDBCConnection.jdbcConnection().prepareStatement(Queries.LoanCheckIfItemcopyIsAvailable(searchBarcodeTextField.getText()));
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the item is not available. Try again.");
            }

            //PreparedStatement preparedStatement = DatabaseConnection.getConnection().databaseLink.prepareStatement(findBarcodeQuery);
            PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(Queries.LoanFindBarcodeQuery(searchBarcodeTextField.getText()));
            ResultSet rs2 = preparedStatement.executeQuery();

            while (rs2.next()) {
                if (!loanList.contains(rs2.getString("barcode") + "             " + rs2.getString("title"))) {
                    loanList.add(rs2.getString("barcode") + "             " + rs2.getString("title"));
                    addItemToLoanList();
                    searchBarcodeTextField.clear();
                }
                else {
                    alertMessage(Alert.AlertType.INFORMATION,"The item is already added to the list!" );
                    searchBarcodeTextField.clear();
                }
            }
        }
        else if (searchBarcodeTextField.getText().isEmpty()) {
            alertMessage(Alert.AlertType.INFORMATION, "You need to type in a barcode!");
        }
        else {
            alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the barcode does not exists. Try again.");
        }
    }

    // Method to remove item from listview.
    @FXML
    void removeItemFromList(ActionEvent event) {

        if (!titleList.getSelectionModel().isEmpty()) {
            int selectedID = titleList.getSelectionModel().getSelectedIndex();
            titleList.getItems().remove(selectedID);
            loanList.remove(selectedID);
        }
        else {
            alertMessage(Alert.AlertType.INFORMATION, "You need to select the item you would like to remove from the list!");
        }
    }

    // Method to create alerts.
    private void alertMessage(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to add the barcodes to the listview.
    void addItemToLoanList() {
        ObservableList<String> loanListBarcodes = FXCollections.observableArrayList(loanList);
        titleList.setItems(loanListBarcodes);
    }
}



