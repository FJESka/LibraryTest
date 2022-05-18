package Overdue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static bookSearch.DatabaseConnection.getConnection;

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

        String overdueQuery = "SELECT l.dueDate, l.memberID, b.title FROM Loan l JOIN ItemCopy i ON l.barcode = i.barcode JOIN Book b ON i.ISBN_ItemCopy = b.ISBN_ItemCopy WHERE l.dueDate < current_date UNION SELECT l.dueDate, l.memberID, d.title FROM Loan l JOIN ItemCopy i ON l.barcode = i.barcode JOIN Dvd d ON i.dvdID_ItemCopy = d.dvdID_ItemCopy WHERE l.dueDate < current_date";


        try {

            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(overdueQuery);

            while (resultSet.next()) {
                String queryDuedate = resultSet.getString("dueDate");
                String queryMemberID = resultSet.getString("memberID");
                String queryTitle = resultSet.getString("title");

                overdueSearchlist.add(new overdueTable(queryDuedate, queryMemberID, queryTitle));

            }
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
}