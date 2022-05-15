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
import java.sql.Statement;
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

    @FXML
    void changeToSearch(ActionEvent event) throws IOException {

       Parent root = FXMLLoader.load(getClass().getResource("itemSearchLibraryTest.fxml"));

        Stage window = (Stage) searchItemBtn.getScene().getWindow();
        window.setScene(new Scene(root, 600, 400));
    }

    @FXML
    void returnItem(ActionEvent event) throws IOException {

        for (int i = 0; i < returnList.size(); i++ ) {
            String[] currentItemBarcode = returnList.get(i).split(" ");

            String updateItemcopyQuery = "UPDATE itemCopy SET status = 'Available' WHERE barcode = '" + currentItemBarcode[0] + "';";
            System.out.println(updateItemcopyQuery);

            String updateLoanQuery = "UPDATE loan SET returnDate = CURDATE() WHERE barcode = '" + currentItemBarcode[0] + "';";

            try {
                PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(updateItemcopyQuery);
                preparedStatement.executeUpdate(updateItemcopyQuery);

                PreparedStatement ps = JDBCConnection.jdbcConnection().prepareStatement(updateLoanQuery);
                ps.executeUpdate(updateLoanQuery);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("The item(s) has successfully been returned!");
        alert.showAndWait();

        changeToSearch(event);
    }

    @FXML
    void searchItem(ActionEvent event) throws SQLException {
        String findBarcodeQuery = "SELECT loan.loanID, itemcopy.barcode, book.title, itemcopy.status FROM loan INNER JOIN itemcopy ON loan.barcode = itemcopy.barcode INNER JOIN book ON itemcopy.ISBN = book.ISBN WHERE loan.barcode = '" + searchItemTextField.getText() + "';";
        System.out.println(findBarcodeQuery);
        PreparedStatement preparedStatement = JDBCConnection.jdbcConnection().prepareStatement(findBarcodeQuery);
        ResultSet rs = preparedStatement.executeQuery();

        boolean itemReturnable = false;

        while (rs.next()) {
       //     if (!returnList.contains(rs.getString("barcode") + rs.getString("title") + rs.getString("loanID") + rs.getString("status"))) {
               if (!returnItemList.getItems().contains("barcode")) {
                returnList.add(rs.getString("barcode") + " " + rs.getString("title") + " " + rs.getString("loanID") + " " + rs.getString("status"));

                populateReturnList();
            }
               //Får ej detta att funka...
             if (returnList.contains(searchItemTextField.getText())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("The item already exists in the list.");
                alert.showAndWait();
            }
            itemReturnable = true;
        }
        if (!itemReturnable){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Wrong barcode, please enter the barcode of the item you would like to return.");
            alert.showAndWait();
        }
    }

    private ArrayList<String> returnList = new ArrayList<>();
    void populateReturnList() {
        ObservableList <String> returnListBarcodes = FXCollections.observableArrayList(returnList);
        returnItemList.setItems(returnListBarcodes);
    }

    @FXML
    void removeItemFromList(ActionEvent event) {
        if (!returnItemList.getSelectionModel().isEmpty()) {
            int selectedID = returnItemList.getSelectionModel().getSelectedIndex();
            returnItemList.getItems().remove(selectedID);
            returnList.remove(selectedID);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("You need to select the item you would like to remove from the list!");
            alert.showAndWait();
        }
    }
}
