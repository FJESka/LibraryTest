package adminInterface;

import bookSearch.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static adminInterface.AdminQueries.copies;
import static bookSearch.DatabaseConnection.getConnection;

public class ManageCopiesController implements Initializable {

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

    @FXML
    public void handleButtonAction(ActionEvent event){

        if(event.getSource() == btnInsert){
            insert();
        }
        if(event.getSource() == btnUpdate){
            update();
        }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection connectDB = getConnection().getDBConnection();

        showList();
    }

    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String barcode = tfBarcode.getText();
            int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
            String isbn = tfISBN.getText();
            int status = Integer.parseInt(tfStatus.getText());
            String type = tfType.getText();
            Integer dvdID = Integer.valueOf(tfDvdID.getText());
            String queryDvdisNull = "INSERT INTO `LibraryTest`.`ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + type + "', '" + status + "');";
            String query = "INSERT INTO `LibraryTest`.`ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + dvdID + "', '" + type + "', '" + status + "');";
            String queryISBNisNull = "INSERT INTO `LibraryTest`.`ItemCopy` (`barcode`, `loanPeriod`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + dvdID + "', '" + type + "', '" + status + "');";

            if (tfDvdID.getText().isEmpty() || tfDvdID.getText() == null) {
                statement.executeUpdate(queryDvdisNull);
            } else if (tfISBN.getText().isEmpty() || tfISBN.getText() == null){
                statement.executeUpdate(queryISBNisNull);
            } else{
                statement.executeUpdate(query);
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
            int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
            String isbn = tfISBN.getText();
            int status = Integer.parseInt(tfStatus.getText());
            String type = tfType.getText();
            Integer dvdID = Integer.valueOf(tfDvdID.getText());

            String updateDvdisNull = "UPDATE `LibraryTest`.`ItemCopy` SET `barcode` = '" + barcode + "', `loanPeriod` = '" + loanPeriod + "', `ISBN_ItemCopy` = '" + isbn + "', `copyTypeName` = '"+ type +"', `status` = '" + status + "' WHERE (`barcode` = '" + barcode + "')";
            String update = "UPDATE `LibraryTest`.`ItemCopy` SET `barcode` = '" + barcode + "', `loanPeriod` = '" + loanPeriod + "', `ISBN_ItemCopy` = '" + isbn + "', `dvdID_ItemCopy` = '" + dvdID + "', `copyTypeName` = '"+ type +"', `status` = '" + status + "' WHERE (`barcode` = '" + barcode + "')";
            String updateISBNisNull = "UPDATE `LibraryTest`.`ItemCopy` SET `barcode` = '" + barcode + "', `loanPeriod` = '" + loanPeriod + "', `dvdID_ItemCopy` = '" + dvdID + "', `copyTypeName` = '"+ type +"', `status` = '" + status + "' WHERE (`barcode` = '" + barcode + "')";

            if (tfDvdID.getText().isEmpty() || tfDvdID.getText() == null) {
                statement.executeUpdate(updateDvdisNull);
            } else if (tfISBN.getText().isEmpty() || tfISBN.getText() == null){
                statement.executeUpdate(updateISBNisNull);
            } else{
                statement.executeUpdate(update);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        showList();


    }

}
