package overdue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static databaseConnection.DatabaseConnection.getConnection;

public class HelloController implements Initializable  {

    ObservableList<overdueTable> overdueSearchlist = FXCollections.observableArrayList();
    @FXML
    private TableColumn<overdueTable, String> col_dueDate;
    @FXML
    private TableColumn<overdueTable, String> col_memberId;
    @FXML
    private TableColumn<overdueTable, String> col_title;
    @FXML
    private TableView<overdueTable> table_overdue;
    @FXML
    private Button button_Back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Query to get overdue loans
        String overdueQuery = "SELECT l.dueDate, l.memberID, b.title FROM Loan l JOIN ItemCopy i ON l.barcode = i.barcode JOIN Book b ON i.ISBN_ItemCopy = b.isbn WHERE l.dueDate < current_date UNION SELECT l.dueDate, l.memberID, d.title FROM Loan l JOIN ItemCopy i ON l.barcode = i.barcode JOIN Dvd d ON i.dvdID_ItemCopy = d.id WHERE l.dueDate < current_date";


        try {

            //creates a connection and executes query
            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(overdueQuery);

            //sets result to strings and creates a new overdueTable and adds to list
            while (resultSet.next()) {
                String queryDuedate = resultSet.getString("dueDate");
                String queryMemberID = resultSet.getString("memberID");
                String queryTitle = resultSet.getString("title");

                overdueSearchlist.add(new overdueTable(queryMemberID, queryDuedate, queryTitle));

            }
            //sets the columns in tableview to the right values and adds objects to tableview
            col_dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            col_memberId.setCellValueFactory(new PropertyValueFactory<>("memberID"));
            col_title.setCellValueFactory(new PropertyValueFactory<>("title"));

            table_overdue.setItems(overdueSearchlist);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            //Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, (String) null);
        }
    }

    //method to switch back to admin start page
    public void switchBackToAdminStart(ActionEvent event) throws IOException {
        Parent root;
        Stage stage;
        Scene scene;

        root = FXMLLoader.load(getClass().getResource("AdminStart.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.show();
    }
}