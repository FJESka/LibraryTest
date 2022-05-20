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
import static databaseConnection.DatabaseConnection.getConnection;

public class ManageCopiesController extends ManageController{

    @FXML
    private TableColumn<ItemCopy, String> colBarcode;

    @FXML
    private TableColumn<ItemCopy, Integer> colDvdID;

    @FXML
    private TableColumn<ItemCopy, String> colISBN;

    @FXML
    private TableColumn<ItemCopy, Integer> colLoanPeriod;

    @FXML
    private TableColumn<ItemCopy, Integer> colStatus;

    @FXML
    private TableColumn<ItemCopy, String> colType;

    @FXML
    private TableView<ItemCopy> mcopiesTableview;

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

    //Hanterar val i tableview och sätter textfält till value av det valda objektet
    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        ItemCopy item = mcopiesTableview.getSelectionModel().getSelectedItem();
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
        ItemCopy item = mcopiesTableview.getSelectionModel().getSelectedItem();
        String barcode = item.getBarcode();
        return barcode;
    }

    //Rensar alla textfält
    public void clearTextfields(){
        tfBarcode.clear();
        tfISBN.clear();
        tfDvdID.clear();
        tfLoanPeriod.clear();
        tfStatus.clear();
        tfType.clear();
    }

    //Kör sökquery och lägger resultaten i en lista
    public ObservableList<ItemCopy> getRecords(){
        ObservableList<ItemCopy> itemCopyObservableList = FXCollections.observableArrayList();
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
                itemCopyObservableList.add(new ItemCopy(qBarcode,qLoanPeriod, qDvdID, qISBN, qType, qStatus));

            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return itemCopyObservableList;

    }

    //sätter värdena i tableviews fält och tableview
    public void showList(){
        ObservableList<ItemCopy> list = getRecords();
        // Sets values in the table columns
        colBarcode.setCellValueFactory(new PropertyValueFactory<ItemCopy, String>("barcode"));
        colDvdID.setCellValueFactory(new PropertyValueFactory<ItemCopy, Integer>("dvdID"));
        colISBN.setCellValueFactory(new PropertyValueFactory<ItemCopy, String>("isbn"));
        colLoanPeriod.setCellValueFactory(new PropertyValueFactory<ItemCopy, Integer>("loanPeriod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<ItemCopy, Integer>("status"));
        colType.setCellValueFactory(new PropertyValueFactory<ItemCopy, String>("copyType"));

        //sets the results from SQL Query to the table view
        mcopiesTableview.setItems(list);
    }

    //kollar om textfält är tomma, om de är tomma skapas en alert
    public boolean areFieldsEmpty() {
        boolean fieldsEmpty = false;
        if (isFieldEmpty(tfBarcode.getText()) == true) {
            String field = "Barcode";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfLoanPeriod.getText()) == true) {
            String field = "Loan period";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfStatus.getText()) == true) {
            String field = "Status";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfType.getText()) == true) {
            String field = "Type";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }
        return fieldsEmpty;
    }

    //kollar så att de attribut som är integers får rätt input i textfälten
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

    //kollar så att de attribut som är integers får rätt input i textfälten, om inte skapas en alert
    public boolean inputsCorrect(){
        boolean inputsCorrect = false;
        String feedback = areInputsCorrect();
        if(feedback.isEmpty()){
            inputsCorrect = true;
        }
        else{
            Alert alert = new Alert();
            alert.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, areInputsCorrect());
        }
        return inputsCorrect;
    }

    //Kollar om foreignkey värden som användaren försöker ange finns i databasen
    public boolean doValuesExistInDatabase() throws SQLException {
        boolean valuesExist = true;
        if (tfISBN.getText() != null && tfISBN.getText().isBlank() != true) {
            if (doesISBNExistInDatabase(tfISBN.getText()) != true) {
                Alert a = new Alert();
                String message = "The title doesn't exist in the database.\nPlease add the title to the database before adding copies.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
                valuesExist = false;
            }
            else{
                valuesExist = true;
            }
        } else if (tfDvdID.getText() != null && tfDvdID.getText().isBlank() != true) {
            int dvdID = Integer.parseInt(tfDvdID.getText());
            if (doesDvdIDExistInDatabase(dvdID) != true) {
                Alert a = new Alert();
                String message = "The title doesn't exist in the database.\nPlease add the title to the database before adding copies.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
                valuesExist = false;
            }
            else{
                valuesExist = true;
            }
        }
        return valuesExist;
    }

    //kollar om barcode redan finns i databasen och skapar en alert om den gör det
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
            Alert alert = new Alert();
            String message = "Barcode exists already.";
            alert.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
        }
        else{
            barcodeExists = false;
        }
        return barcodeExists;
    }


    //hämtar values från användarens input och returnerar rätt query beroende på vilken knapp användaren tryckt på
    public String getValuesAndQuery (String whichQuery) throws SQLException {
        String query = null;
        String retrieveQuery = null;

        areFieldsEmpty();
        if (areFieldsEmpty() == true && inputsCorrect() != true) {

        } else {
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
                    Alert a = new Alert();
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
                    Alert a = new Alert();
                    String message = "A copy can't be both a book and a dvd.";
                    a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
                }

            } else {
                query = getCopiesDelete(barcode);
            }
        }

        return query;
    }

    //Metoden för att göra insert, körs när användaren trycker på insert knappen
    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "insert";
            String query = getValuesAndQuery(whichQuery);

            if(query != null && doValuesExistInDatabase() && !doesBarcodeExistAlready(tfBarcode.getText()) && areFieldsEmpty() == false) {
                statement.executeUpdate(query);
            }
            clearTextfields();
            showList();


        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    //Metoden för att göra update, körs när användaren trycker på update knappen
    public void update(){

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "update";
            String query = getValuesAndQuery(whichQuery);
            if(query != null && doValuesExistInDatabase() && !doesBarcodeExistAlready(tfBarcode.getText()) && areFieldsEmpty() == false){
                statement.executeUpdate(query);
            }

            clearTextfields();
            showList();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }


    }

    //Metoden för att göra delete, körs när användaren trycker på delet knappen.
    //Skapar en confirmation alert innan delete querien körs för att kolla så användaren är säker på att de vill delete.
    public void delete(){

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
