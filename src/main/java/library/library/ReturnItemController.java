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

public class ReturnItemController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button returnItemBtn;

    @FXML
    private Button removeItemFromListBtn;

    @FXML
    private ListView<String> returnItemList;

    @FXML
    private Button searchItemBtn;

    @FXML
    private TextField searchItemTextField;

    private ArrayList<String> returnList = new ArrayList<>();

    // Method to cancel return item and change scene to itemSearch.
    @FXML
    void changeToSearch(ActionEvent event) throws IOException {
       Parent root = FXMLLoader.load(getClass().getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) searchItemBtn.getScene().getWindow();
        window.setScene(new Scene(root, 600, 400));
    }

    // Method to confirm the return. Updates the itemcopy and loan tables.
    @FXML
    void returnItem(ActionEvent event) throws IOException {
        for (int i = 0; i < returnList.size(); i++ ) {
            String[] currentItemBarcode = returnList.get(i).split(" ");

            try {
                PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(Queries.ReturnItemUpdateItemQuery(currentItemBarcode));
                preparedStatement.executeUpdate(Queries.ReturnItemUpdateItemQuery(currentItemBarcode));

                PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(Queries.ReturnItemUpdateLoanQuery(currentItemBarcode));
                ps.executeUpdate(Queries.ReturnItemUpdateLoanQuery(currentItemBarcode));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        alertMessage(Alert.AlertType.CONFIRMATION, "The item(s) has successfully been returned!");
        changeToSearch(event);
    }

    // Method to search item and add to listview. Tests if the barcode exists and is possible to return.
    @FXML
    void searchItem(ActionEvent event) throws SQLException {
        PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(Queries.checkIfBarcodeExistsQuery(searchItemTextField.getText()));
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            PreparedStatement ps1 = JDBCConnection.jdbcConnection().prepareStatement(Queries.checkIfItemcopyIsNotAvailable(searchItemTextField.getText()));
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                alertMessage(Alert.AlertType.INFORMATION,"Wrong barcode, the item is not on loan and can not be returned. Try again." );
            }
            PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(Queries.findBarcodeQuery(searchItemTextField.getText()));
            ResultSet rs2 = preparedStatement.executeQuery();

            while (rs2.next()) {
                if (!returnItemList.getItems().contains(rs2.getString("barcode") + "              " + rs2.getString("title") + "                " + rs2.getString("loanID"))) {
                    returnList.add(rs2.getString("barcode") + "              " + rs2.getString("title") + "                " + rs2.getString("loanID"));
                    AddItemToReturnList();
                    searchItemTextField.clear();
                }
                else {
                    alertMessage(Alert.AlertType.INFORMATION,"The item is already added to the list!" );
                    searchItemTextField.clear();
                }
            }
        }
        else if (searchItemTextField.getText().isEmpty()) {
            alertMessage(Alert.AlertType.INFORMATION, "You need to type in a barcode!");
        }
        else {
            alertMessage(Alert.AlertType.INFORMATION, "Wrong barcode, the barcode does not exists. Try again.");
        }
    }

    // Method to remove item from listview.
    @FXML
    void removeItemFromList(ActionEvent event) {
        if (!returnItemList.getSelectionModel().isEmpty()) {
            int selectedID = returnItemList.getSelectionModel().getSelectedIndex();
            returnItemList.getItems().remove(selectedID);
            returnList.remove(selectedID);
        } else {
            alertMessage(Alert.AlertType.INFORMATION,"You need to select the item you would like to remove from the list!" );
        }
    }

    // Method for creating alerts.
    private void alertMessage(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to add the barcodes to the listview.
    void AddItemToReturnList() {
        ObservableList <String> returnListBarcodes = FXCollections.observableArrayList(returnList);
        returnItemList.setItems(returnListBarcodes);
    }
}
