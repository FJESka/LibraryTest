package adminInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static adminInterface.AdminQueries.*;
import static bookSearch.DatabaseConnection.getConnection;

public class ManageCopiesController extends ManageController{

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<ItemCopies, String> colBarcode;

    @FXML
    private TableColumn<ItemCopies, Integer> colDvdID;

    @FXML
    private TableColumn<ItemCopies, String> colISBN;

    @FXML
    private TableColumn<ItemCopies, Integer> colLoanPeriod;

    @FXML
    private TableColumn<ItemCopies, Integer> colStatus;

    @FXML
    private TableColumn<ItemCopies, String> colType;

    @FXML
    private TableView<ItemCopies> mcopiesTableview;

    @FXML
    private TextField tfBarcode;

    @FXML
    private TextField tfDvdID;

    @FXML
    private TextField tfISBN;

    @FXML
    private TextField tfLoanPeriod;

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfType;

//    @FXML
//    public void handleButtonAction(ActionEvent event){
//
//        if(event.getSource() == btnInsert){
//            insert();
//        }
//        if(event.getSource() == btnUpdate){
//            update();
//        }
//        if(event.getSource() == btnDelete){
//            delete();
//        }
//
//    }

    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        ItemCopies item = mcopiesTableview.getSelectionModel().getSelectedItem();
        tfBarcode.setText(item.getBarcode());
        tfISBN.setText(item.getIsbn());
        tfLoanPeriod.setText("" +item.getLoanPeriod());
        tfStatus.setText(""+item.getStatus());
        tfType.setText(item.getCopyType());

        if(item.getDvdID() == null){
            tfDvdID.setText(" ");
        }else{
            tfDvdID.setText("" +item.getDvdID());
        }

    }

    public String getBarcode(){
        ItemCopies item = mcopiesTableview.getSelectionModel().getSelectedItem();
        String barcode = item.getBarcode();
        return barcode;
    }

//    public boolean confirmationAlert(){
//        Boolean okToDelete = false;
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setHeaderText("Are you sure you want to delete?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK){
//            okToDelete = true;
//        }
//        return okToDelete;
//    }


//    public Statement getStatement(){
//        Statement statement = getConnection().getDBConnection().createStatement();
//        return statement;
//    }

    public void clearTextfields(){
        tfBarcode.clear();
        tfISBN.clear();
        tfDvdID.clear();
        tfLoanPeriod.clear();
        tfStatus.clear();
        tfType.clear();
    }

    public ObservableList<ItemCopies> getRecords(){
        ObservableList<ItemCopies> itemCopiesObservableList = FXCollections.observableArrayList();
        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(copies);

            while (resultSet.next()) {
                String qBarcode = resultSet.getString("barcode");
                Integer qDvdID = resultSet.getObject("dvdID_ItemCopy", Integer.class);
                String qISBN = resultSet.getString("ISBN_ItemCopy");
                int qLoanPeriod = resultSet.getInt("loanPeriod");
                int qStatus = resultSet.getInt("status");
                String qType = resultSet.getString("copyTypeName");

                //Populate the observableList with results from our SQL Query
                itemCopiesObservableList.add(new ItemCopies(qBarcode,qLoanPeriod, qDvdID, qISBN, qType, qStatus));

            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return itemCopiesObservableList;

    }

    void showList(){
        ObservableList<ItemCopies> list = getRecords();
        // Sets values in the table columns
        colBarcode.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("barcode"));
        colDvdID.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("dvdID"));
        colISBN.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("isbn"));
        colLoanPeriod.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("loanPeriod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("status"));
        colType.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("copyType"));

        //sets the results from SQL Query to the table view
        mcopiesTableview.setItems(list);
    }

    boolean isFieldEmpty(String barcode){
        boolean isBarcodeEmpty = false;
        if(barcode.isEmpty() || barcode == null) {
            isBarcodeEmpty = true;
        }
        return isBarcodeEmpty;
    }

    public void emptyFieldAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Barcode can't be empty. Please add a barcode or select a row to update.");
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection connectDB = getConnection().getDBConnection();

        showList();
    }

    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String barcode = tfBarcode.getText();
            if(isFieldEmpty(barcode) == true){
                emptyFieldAlert();
            }else{
                int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
                String isbn = tfISBN.getText();
                int status = Integer.parseInt(tfStatus.getText());
                String type = tfType.getText();
                Integer dvdID = Integer.valueOf(tfDvdID.getText());
                String queryDvdisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + type + "', '" + status + "');";
                String query = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + dvdID + "', '" + type + "', '" + status + "');";
                String queryISBNisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + dvdID + "', '" + type + "', '" + status + "');";

                if (tfDvdID.getText().isEmpty() || tfDvdID.getText() == null) {
                    statement.executeUpdate(queryDvdisNull);
                } else if (tfISBN.getText().isEmpty() || tfISBN.getText() == null){
                    statement.executeUpdate(queryISBNisNull);
                } else{
                    statement.executeUpdate(query);
                }

            }
            showList();


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void update(){

        try{
            Statement statement = getConnection().getDBConnection().createStatement();

            String barcode = tfBarcode.getText();
            if(isFieldEmpty(barcode) == true) {
                emptyFieldAlert();
            }else {
                int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
                String isbn = tfISBN.getText();
                int status = Integer.parseInt(tfStatus.getText());
                String type = tfType.getText();
                Integer dvdID = null;
                if (tfDvdID.getText().isBlank()){
                    tfDvdID.clear();
                }else{
                    dvdID = Integer.valueOf(tfDvdID.getText());
                }

                String textBarcode = getBarcode();
                String updateDvdisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
//            String update = "UPDATE `ItemCopy` SET `barcode` = '" + barcode + "', `loanPeriod` = '" + loanPeriod + "', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '" + dvdID + "', `copyTypeName` = '"+ type +"', `status` = '" + status + "' WHERE (`barcode` = '" + barcode + "')";
                String updateISBNisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";

                String update = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";

                if (tfDvdID.getText().isBlank() || tfDvdID.getText() == null) {
                    tfDvdID.clear();
                    statement.executeUpdate(updateDvdisNull);
                } else if (tfISBN.getText() == null){
                    statement.executeUpdate(updateISBNisNull);
                } else{
                    //Lägg en alert "ett item kan inte vara både en bok och en dvd"
                    statement.executeUpdate(update);
                }

            }
            clearTextfields();
            showList();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }


    }

    public void delete(){
        String barcode = tfBarcode.getText();

        String deleteItemcopy = "DELETE FROM `ItemCopy` WHERE (`barcode` = '" + barcode + "');";

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            if(isFieldEmpty(barcode) == true) {
                emptyFieldAlert();
            }else{
                if(confirmationAlert() == true) {
                    statement.executeUpdate(deleteItemcopy);
                    clearTextfields();
                }
                showList();
            }


        }catch(SQLException e){
            e.printStackTrace();
        }


    }

}
