package adminInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

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

    public boolean doesISBNExistInDatabase(String isbn) throws SQLException {
        boolean isbnExists = false;
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = doesISBNExist(isbn);
        ResultSet resultSet = statement.executeQuery(query);
        String resultISBN = null;

        if(resultSet.next()){
            resultISBN = resultSet.getString("isbn");
        }
        if(isbn.equalsIgnoreCase(resultISBN)){
            isbnExists = true;
        }
        else{
            isbnExists = false;
        }
        return isbnExists;
    }

    public boolean doesDvdIDExistInDatabase(int dvdID) throws SQLException {
        boolean dvdIDExists = false;
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = doesDvdIDExist(String.valueOf(dvdID));
        ResultSet resultSet = statement.executeQuery(query);
        int resultDvdID = 0;

        if(resultSet.next()){
            resultDvdID = resultSet.getInt("id");
        }
        if(dvdID != 0 && dvdID == resultDvdID){
            dvdIDExists = true;
        }
        else{
            dvdIDExists = false;
        }
        return dvdIDExists;
    }

    public void areFieldsEmpty() {
        if (isFieldEmpty(tfBarcode.getText()) == true) {
            String field = "Barcode";
            emptyFieldAlert(field);
        } else if (isFieldEmpty(tfLoanPeriod.getText()) == true) {
            String field = "Loan period";
            emptyFieldAlert(field);
        } else if (isFieldEmpty(tfStatus.getText()) == true) {
            String field = "Status";
            emptyFieldAlert(field);
        } else if (isFieldEmpty(tfType.getText()) == true) {
            String field = "Type";
            emptyFieldAlert(field);
        }
    }

    public String areInputsCorrect(){
        String feedback = "";
        if((Pattern.matches("[a-zA-Z]+", tfDvdID.getText()) == true)){
            feedback = "DvdID needs to be a number.\n";
        }
        if((Pattern.matches("[a-zA-Z]+", tfLoanPeriod.getText()) == true)){
            String message = "Loan period needs to be a number.\n";
            feedback = feedback.concat(message);
        }
        if((Pattern.matches("[a-zA-Z]+", tfStatus.getText()) == true)){
            String message = "Status needs to be a number.\n";
            feedback = feedback.concat(message);
        }
        return feedback;
    }

    public boolean doValuesExistInDatabase() throws SQLException {
        boolean valuesExist = true;
        if (tfISBN.getText() != null && tfISBN.getText().isBlank() != true) {
            if (doesISBNExistInDatabase(tfISBN.getText()) != true) {
                Warning a = new Warning();
                String message = "The title doesn't exist in the database.\nPlease add the title to the database before adding copies.";
                a.alertMessage(Alert.AlertType.INFORMATION, message);
                valuesExist = false;
            }
            else{
                valuesExist = true;
            }
        } else if (tfDvdID.getText() != null && tfDvdID.getText().isBlank() != true) {
            int dvdID = Integer.parseInt(tfDvdID.getText());
            if (doesDvdIDExistInDatabase(dvdID) != true) {
                Warning a = new Warning();
                String message = "The title doesn't exist in the database.\nPlease add the title to the database before adding copies.";
                a.alertMessage(Alert.AlertType.INFORMATION, message);
                valuesExist = false;
            }
            else{
                valuesExist = true;
            }
        }
        return valuesExist;
    }

    public boolean doesBarcodeExistAlready(String barcode) throws SQLException {
        boolean barcodeExists = false;
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = doesBarcodeExist(barcode);
        ResultSet resultSet = statement.executeQuery(query);
        String resultBarcode = null;

        if(resultSet.next()){
            resultBarcode = (resultSet.getString("barcode"));
        }

        if(resultBarcode != null && barcode.trim().replaceAll(" ", "").equalsIgnoreCase(resultBarcode.replaceAll(" ", ""))){
            barcodeExists = true;
            Warning warning = new Warning();
            String message = "Barcode exists already.";
            warning.alertMessage(Alert.AlertType.INFORMATION, message);
        }
        else{
            barcodeExists = false;
        }
        return barcodeExists;
    }


    public String getValuesAndQuery (String whichQuery) throws SQLException {
            String query = null;
            String retrieveQuery = null;

            areFieldsEmpty();
            if(areInputsCorrect().isBlank() != true){
                Warning warning = new Warning();
                warning.alertMessage(Alert.AlertType.INFORMATION, areInputsCorrect());
            }

            String barcode = tfBarcode.getText();
            int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
            String isbn = tfISBN.getText();
            int status = Integer.parseInt(tfStatus.getText());
            String type = tfType.getText();
            Integer dvdID = null;

            if (whichQuery.equalsIgnoreCase("insert")) {


                    if (tfDvdID.getText().isEmpty() || tfDvdID.getText() == null) {
                        query = getCopiesInsertDvdNull(barcode, loanPeriod, isbn, type, status);
                    } else if (tfISBN.getText().isEmpty() || tfISBN.getText() == null) {
                        dvdID = Integer.valueOf(tfDvdID.getText().trim());
                        query = getCopiesInsertISBNNull(barcode, loanPeriod, dvdID, type, status);
                    } else {
                        Warning a = new Warning();
                        String message = "A copy can't be both a book and a dvd.";
                        a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);

                    }

            } else if (whichQuery.equalsIgnoreCase("update")) {
                String textBarcode = getBarcode();

                if (tfDvdID.getText().isBlank()) {
                    tfDvdID.clear();
                } else {
                    dvdID = Integer.valueOf(tfDvdID.getText().trim());
                }

                if (dvdID == null) {
                    tfDvdID.clear();
                    query = getCopiesUpdateDvdNull(barcode, loanPeriod, isbn, type, status, textBarcode);
                } else if (tfISBN.getText() == null) {
                    query = getCopiesUpdateISBNNull(barcode, loanPeriod, dvdID, type, status, textBarcode);
                } else {
                    Warning a = new Warning();
                    String message = "A copy can't be both a book and a dvd.";
                    a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);

                    //Lägg en alert "ett item kan inte vara både en bok och en dvd"
                }

            } else {
                query = getCopiesDelete(barcode);
            }

        return query;
    }

    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "insert";
            String query = getValuesAndQuery(whichQuery);

            if(query != null && doValuesExistInDatabase() && !doesBarcodeExistAlready(tfBarcode.getText())) {
                statement.executeUpdate(query);
            }
//            String barcode = tfBarcode.getText();
//            if(isFieldEmpty(barcode) == true){
//                emptyFieldAlert();
//            }else{
//                int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
//                String isbn = tfISBN.getText();
//                int status = Integer.parseInt(tfStatus.getText());
//                String type = tfType.getText();
//                Integer dvdID = Integer.valueOf(tfDvdID.getText());
//                String queryDvdisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + type + "', '" + status + "');";
//                String query = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + dvdID + "', '" + type + "', '" + status + "');";
//                String queryISBNisNull = "INSERT INTO `ItemCopy` (`barcode`, `loanPeriod`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + dvdID + "', '" + type + "', '" + status + "');";
//
//                if (tfDvdID.getText().isEmpty() || tfDvdID.getText() == null) {
//                    statement.executeUpdate(queryDvdisNull);
//                } else if (tfISBN.getText().isEmpty() || tfISBN.getText() == null){
//                    statement.executeUpdate(queryISBNisNull);
//                } else{
//                    statement.executeUpdate(query);
//                }
//
//            }
            clearTextfields();
            showList();


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    public void update(){

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "update";
            String query = getValuesAndQuery(whichQuery);
            if(query != null && doValuesExistInDatabase() && !doesBarcodeExistAlready(tfBarcode.getText())){
                statement.executeUpdate(query);
            }

//            String barcode = tfBarcode.getText();
//            if(isFieldEmpty(barcode) == true) {
//                emptyFieldAlert();
//            }else {
//                int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
//                String isbn = tfISBN.getText();
//                int status = Integer.parseInt(tfStatus.getText());
//                String type = tfType.getText();
//                Integer dvdID = null;
//
//                if (tfDvdID.getText().isBlank()){
//                    tfDvdID.clear();
//                }else{
//                    dvdID = Integer.valueOf(tfDvdID.getText());
//                }
//
//                String textBarcode = getBarcode();
//                String updateDvdisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
////            String update = "UPDATE `ItemCopy` SET `barcode` = '" + barcode + "', `loanPeriod` = '" + loanPeriod + "', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '" + dvdID + "', `copyTypeName` = '"+ type +"', `status` = '" + status + "' WHERE (`barcode` = '" + barcode + "')";
//                String updateISBNisNull = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
//
//                String update = "UPDATE `ItemCopy` SET `barcode` = '" +barcode + "' , `loanPeriod` = '"+loanPeriod +"', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '"+ dvdID + "', `copyTypeName` = '" + type + "', `status` = '" + status+ "' WHERE (`barcode` = '" + textBarcode + "');";
//
//                if (tfDvdID.getText().isBlank() || tfDvdID.getText() == null) {
//                    tfDvdID.clear();
//                    statement.executeUpdate(updateDvdisNull);
//                } else if (tfISBN.getText() == null){
//                    statement.executeUpdate(updateISBNisNull);
//                } else{
//                    //Lägg en alert "ett item kan inte vara både en bok och en dvd"
//                    statement.executeUpdate(update);
//                }

            clearTextfields();
            showList();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }


    }

    public void delete(){
//        String barcode = tfBarcode.getText();
//
//        String deleteItemcopy = "DELETE FROM `ItemCopy` WHERE (`barcode` = '" + barcode + "');";

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "delete";
            String query = getValuesAndQuery(whichQuery);

            if(confirmationAlert() == true) {
                statement.executeUpdate(query);
                clearTextfields();
            }

            showList();


        }catch(SQLException e){
            e.printStackTrace();
        }


    }

}
