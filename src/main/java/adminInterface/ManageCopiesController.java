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

    }

    ObservableList<ItemCopies> itemCopiesObservableList = FXCollections.observableArrayList();


    void getRecords(){
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
                showList();

            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    void showList(){
        // Sets values in the table columns

        colBarcode.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("barcode"));
        colDvdID.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("dvdID"));
        colISBN.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("isbn"));
        colLoanPeriod.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("loanPeriod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("status"));
        colType.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("copyType"));

        //sets the results from SQL Query to the table view
        mcopiesTableview.setItems(itemCopiesObservableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection connectDB = getConnection().getDBConnection();

        getRecords();

//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet resultSet = statement.executeQuery(copies);
//
//            while (resultSet.next()) {
//                String qBarcode = resultSet.getString("barcode");
//                Integer qDvdID = resultSet.getObject("dvdID_ItemCopy", Integer.class);
//                String qISBN = resultSet.getString("ISBN_ItemCopy");
//                int qLoanPeriod = resultSet.getInt("loanPeriod");
//                int qStatus = resultSet.getInt("status");
//                String qType = resultSet.getString("copyTypeName");
//
//                //Populate the observableList with results from our SQL Query
//
//                itemCopiesObservableList.add(new ItemCopies(qBarcode,qLoanPeriod, qDvdID, qISBN, qType, qStatus));
//
//            }
//            // Sets values in the table columns
//
//            colBarcode.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("barcode"));
//            colDvdID.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("dvdID"));
//            colISBN.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("isbn"));
//            colLoanPeriod.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("loanPeriod"));
//            colStatus.setCellValueFactory(new PropertyValueFactory<ItemCopies, Integer>("status"));
//            colType.setCellValueFactory(new PropertyValueFactory<ItemCopies, String>("copyType"));
//
//            //sets the results from SQL Query to the table view
//            mcopiesTableview.setItems(itemCopiesObservableList);
//
//        } catch (SQLException e){
//            e.printStackTrace();
//        }

    }

    public void insert(){

        try {
            String barcode = tfBarcode.getText();
            int loanPeriod = Integer.parseInt(tfLoanPeriod.getText());
            String isbn = tfISBN.getText();
            Integer dvdID;
            if (tfDvdID.getText().isEmpty()) {
                dvdID = 0;
            } else {
                dvdID = Integer.valueOf(tfDvdID.getText());
            }
            int status = Integer.parseInt(tfStatus.getText());
            String type = tfType.getText();


            Statement statement = getConnection().getDBConnection().createStatement();

//            String query = AdminQueries.getInsertQuery(barcode, loanPeriod, isbn, dvdID, type, status);
//            statement.executeUpdate(query);

            String query = "INSERT INTO `LibraryTest`.`ItemCopy` (`barcode`, `loanPeriod`, `ISBN_ItemCopy`, `dvdID_ItemCopy`, `copyTypeName`, `status`) VALUES (' " + barcode + "', ' " + loanPeriod + " ', '" + isbn + "', '" + dvdID + "', '" + type + "', '" + status + "');";

            statement.executeUpdate(query);

            Connection connectDB = getConnection().getDBConnection();



        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

}
