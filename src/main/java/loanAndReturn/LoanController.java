package loanAndReturn;

import itemSearch.ItemSearchApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static databaseConnection.DatabaseConnection.getConnection;

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
    private Label currentLoansLabel;

    @FXML
    private Label maxLoansLabel;

    private ArrayList<String> loanList = new ArrayList<>();

    private int maxLoan;
    private int noOfLoans;

    // Method to cancel loan and change scene to itemSearch.
    @FXML
    void cancelLoan(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(ItemSearchApplication.class.getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.setScene(new Scene(root, 1280, 720));

    }

    // Method to check if max loan limit is reached, if it is possible to loan and confirms the loan. Creates a new loan and shows receipt.
    @FXML
    void confirmLoan() throws IOException {
        try {
            for (int i = 0; i < loanList.size(); i++) {
                String[] barcodeVariable = loanList.get(i).split(" ");
                PreparedStatement ps = getConnection().getDBConnection().prepareStatement(Queries.insertLoanQuery(barcodeVariable));
                ps.executeUpdate(Queries.insertLoanQuery(barcodeVariable));

                PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.LoanUpdateItemcopyQuery(barcodeVariable));
                preparedStatement.executeUpdate(Queries.LoanUpdateItemcopyQuery(barcodeVariable));

                PreparedStatement preparedStatement2 = getConnection().getDBConnection().prepareStatement(Queries.selectLoanQuery(barcodeVariable));
                ResultSet rs2 = preparedStatement2.executeQuery();

                while (rs2.next()) {
                    alertMessage(Alert.AlertType.CONFIRMATION, "The loan was completed, see details below: " + "\n" + "LoanID: " + rs2.getString("loanID") + "   " + "Barcode: " + rs2.getString("barcode") + "   " + "Title: " + rs2.getString("title") + "   " + "MemberID: " + rs2.getString("memberID") + "\n" + "Date of loan: " + rs2.getString("dateOfLoan") + "\n" + "Due date: " + rs2.getString("dueDate"));
                }
                //cancelLoan(event);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Method to search item and add to listview. Tests if the barcode exits and if it is available for loan.
    @FXML
    void searchItem (ActionEvent event) throws SQLException {
        try {
            PreparedStatement ps = getConnection().getDBConnection().prepareStatement(Queries.LoanCheckIfBarcodeExistsQuery(searchBarcodeTextField.getText()));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreparedStatement ps1 = getConnection().getDBConnection().prepareStatement(Queries.LoanCheckIfItemCopyIsAvailable(searchBarcodeTextField.getText()));
                ResultSet rs1 = ps1.executeQuery();

                if (rs1.next()) {
                    alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the item is not available. Try again.");
                    searchBarcodeTextField.clear();
                }

                PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.LoanFindBarcodeQuery(searchBarcodeTextField.getText()));
                ResultSet rs2 = preparedStatement.executeQuery();

                PreparedStatement preparedStatement1 = getConnection().getDBConnection().prepareStatement(Queries.LoanFindDvdBarcodeQuery(searchBarcodeTextField.getText()));
                ResultSet rs3 = preparedStatement1.executeQuery();

                if (rs2.next()) {
                    if (!loanList.contains(rs2.getString("barcode") + "             " + rs2.getString("title"))) {
                        loanList.add(rs2.getString("barcode") + "             " + rs2.getString("title"));
                        addItemToLoanList();
                        searchBarcodeTextField.clear();
                    } else {
                        alertMessage(Alert.AlertType.INFORMATION, "The item is already added to the list!");
                        searchBarcodeTextField.clear();
                    }
                }else if (rs3.next()) {
                    if (!loanList.contains(rs3.getString("barcode") + "             " + rs3.getString("title"))) {
                        loanList.add(rs3.getString("barcode") + "             " + rs3.getString("title"));
                        addItemToLoanList();
                        searchBarcodeTextField.clear();
                    } else {
                        alertMessage(Alert.AlertType.INFORMATION, "The item is already added to the list!");
                        searchBarcodeTextField.clear();
                    }
                }
            } else if (searchBarcodeTextField.getText().isEmpty()) {
                alertMessage(Alert.AlertType.INFORMATION, "You need to type in a barcode!");
            } else {
                alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the barcode does not exists. Try again.");
                searchBarcodeTextField.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Method to initialize labels and checkLoans() method.
    public void initialize() throws IOException {
        populateMaxLoanLabel();
        populateCurrentLoansLabel();
        checkLoans();
    }

    // Method to populate label.
    public String populateMaxLoanLabel() {
        try {
            PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.maxLoanLimitQuery());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                maxLoan = rs.getInt("maxLoanLimit");
                maxLoansLabel.setText("Maximum number of loans possible: " + maxLoan + ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxLoansLabel.getText();
    }

    //Method to populate label.
    public Label populateCurrentLoansLabel(){
        try {

            PreparedStatement preparedStatement1 = getConnection().getDBConnection().prepareStatement(Queries.NoOfLoanQuery());
            ResultSet rs1 = preparedStatement1.executeQuery();
            while (rs1.next()) {
                noOfLoans = rs1.getInt(1);
                currentLoansLabel.setText("You currently have: " + noOfLoans + " loans.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return currentLoansLabel;
    }

    //Method to get max loan limit.
    private int getMaxLoanLimit() {
        try {
            PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.maxLoanLimitQuery());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                maxLoan = rs.getInt("maxLoanLimit");
            }
        } catch (Exception e){
            e.printStackTrace();
        }return maxLoan;
    }

    // Method to get current number of loans.
    private int getNoOfLoans() {
        try {
            PreparedStatement preparedStatement1 = getConnection().getDBConnection().prepareStatement(Queries.NoOfLoanQuery());
            ResultSet rs1 = preparedStatement1.executeQuery();
            if (rs1.next()) {
                noOfLoans = rs1.getInt(1);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }return noOfLoans;
    }

    // Method to check if allowed to borrow.
    public boolean checkLoans() throws IOException {
        if (noOfLoans >= maxLoan) {
            alertMessage(Alert.AlertType.INFORMATION, "You have reached your maximum number of loans.");
            searchBtn.setDisable(true);
            confirmBtn.setDisable(true);
        } else {
            confirmLoan();
            return true;
        } return false;
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



