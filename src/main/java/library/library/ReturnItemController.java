package library.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private ListView<String> returnItemList;

    @FXML
    private Button searchItemBtn;

    @FXML
    private TextField searchItemTextField;

    @FXML
    void changeToSearch(ActionEvent event) {

    }

    @FXML
    void returnItem(ActionEvent event) throws SQLException {

        


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
            else if (rs.isBeforeFirst()){
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
        //ListView<String> listView = new ListView<>(returnListBarcodes);
    }
}
