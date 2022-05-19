package library.library;

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
    private Label currentLoansLabel;

    @FXML
    private Label maxLoansLabel;

    private ArrayList<String> loanList = new ArrayList<>();

    // Method to cancel loan and change scene to itemSearch.
    @FXML
    void cancelLoan(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(ItemSearchApplication.class.getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) cancelBtn.getScene().getWindow();
        window.setScene(new Scene(root, 1280, 720));

    }

    // Method to check if max loan limit is reached, if it is possible to loan and confirms the loan. Creates a new loan and shows receipt.
    @FXML
    void confirmLoan(ActionEvent event) throws IOException {
        //checkIfAllowedToBorrow();

        try {
            PreparedStatement preparedStatement4 = getConnection().getDBConnection().prepareStatement(Queries.maxLoanLimitQuery());
            ResultSet rs = preparedStatement4.executeQuery();

            PreparedStatement preparedStatement5 = getConnection().getDBConnection().prepareStatement(Queries.NoOfLoanQuery());
            ResultSet rs1 = preparedStatement5.executeQuery();

            while (rs.next()) {
                int maxLoans = rs.getInt("maxLoanLimit");

                while (rs1.next()) {
                    int noOfLoans = rs1.getInt(1);


                    if (noOfLoans > maxLoans) {
                        PreparedStatement preparedStatement2 = getConnection().getDBConnection().prepareStatement(Queries.memberAllowedToBorrow());
                        preparedStatement2.executeUpdate(Queries.memberAllowedToBorrow());

                        alertMessage(Alert.AlertType.INFORMATION, "You have reached your maximum number of loans.");

                        confirmBtn.setDisable(true);
                    } else {

                        for (int i = 0; i < loanList.size(); i++) {
                            String[] barcodeVariable = loanList.get(i).split(" ");

                            try {
                                PreparedStatement ps = getConnection().getDBConnection().prepareStatement(Queries.insertLoanQuery(barcodeVariable));
                                ps.executeUpdate(Queries.insertLoanQuery(barcodeVariable));

                                PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.LoanUpdateItemcopyQuery(barcodeVariable));
                                preparedStatement.executeUpdate(Queries.LoanUpdateItemcopyQuery(barcodeVariable));

                                PreparedStatement preparedStatement2 = getConnection().getDBConnection().prepareStatement(Queries.selectLoanQuery(barcodeVariable));
                                ResultSet rs2 = preparedStatement2.executeQuery();

                                while (rs2.next()) {
                                    alertMessage(Alert.AlertType.CONFIRMATION, "The loan was completed, see details below: " + "\n" + "LoanID: " + rs.getString("loanID") + "   " + "Barcode: " + rs.getString("barcode") + "   " + "Title: " + rs.getString("title") + "   " + "MemberID: " + rs.getString("memberID") + "\n" + "Date of loan: " + rs.getString("dateOfLoan") + "\n" + "Due date: " + rs.getString("dueDate"));
                                }
                                cancelLoan(event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Method to search item and add to listview. Tests if the barcode exits and if it is available for loan.
    @FXML
    void searchItem (ActionEvent event) throws SQLException {
        //PreparedStatement ps = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfBarcodeExistsQuery);
        PreparedStatement ps = getConnection().getDBConnection().prepareStatement(Queries.LoanCheckIfBarcodeExistsQuery(searchBarcodeTextField.getText()));
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            //PreparedStatement ps1 = DatabaseConnection.getConnection().databaseLink.prepareStatement(checkIfItemcopyIsAvailable);
            PreparedStatement ps1 = getConnection().getDBConnection().prepareStatement(Queries.LoanCheckIfItemCopyIsAvailable(searchBarcodeTextField.getText()));
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the item is not available. Try again.");
            } else {

            //PreparedStatement preparedStatement = DatabaseConnection.getConnection().databaseLink.prepareStatement(findBarcodeQuery);
            PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.LoanFindBarcodeQuery(searchBarcodeTextField.getText()));
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
        } }
        else if (searchBarcodeTextField.getText().isEmpty()) {
            alertMessage(Alert.AlertType.INFORMATION, "You need to type in a barcode!");
        }
        else {
            alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the barcode does not exists. Try again.");
        }
    }

    // Method to initialize labels.
    public void initialize(){
        populateMaxLoanLabel();
        populateCurrentLoansLabel();
    }

    // Method to populate label.
    public String populateMaxLoanLabel() {
        try {
            PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.maxLoanLimitQuery());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                int maxLoans = rs.getInt("maxLoanLimit");
                maxLoansLabel.setText("Maximum number of loans possible: " + maxLoans + ".");
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
                int noOfLoans = rs1.getInt(1);
                currentLoansLabel.setText("You currently have: " + noOfLoans + " loans.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return currentLoansLabel;
    }

    // DENNA METOD SKA TROLIGTVIS BORT; HAR DEN I CONFIRM-METODEN LÄNGRE UPP.
    public void checkIfAllowedToBorrow() {

        try {
            PreparedStatement preparedStatement = getConnection().getDBConnection().prepareStatement(Queries.maxLoanLimitQuery());
            ResultSet rs = preparedStatement.executeQuery();

            PreparedStatement preparedStatement1 = getConnection().getDBConnection().prepareStatement(Queries.NoOfLoanQuery());
            ResultSet rs1 = preparedStatement1.executeQuery();

            while (rs.next()) {
                int maxLoans = rs.getInt("maxLoanLimit");

                while (rs1.next()) {
                    int noOfLoans = rs1.getInt(1);


                if (noOfLoans > maxLoans) {
                    PreparedStatement preparedStatement2 = getConnection().getDBConnection().prepareStatement(Queries.memberAllowedToBorrow());
                    preparedStatement2.executeUpdate(Queries.memberAllowedToBorrow());

                    alertMessage(Alert.AlertType.INFORMATION, "You have reached your maximum number of loans.");

                    confirmBtn.setDisable(true);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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



