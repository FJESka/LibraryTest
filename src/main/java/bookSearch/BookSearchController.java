package bookSearch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookSearchController implements Initializable {

    @FXML
    private TableView<BookSearch> tableview;

    @FXML
    private TableColumn<BookSearch, String> colISBN;

    @FXML
    private TableColumn<BookSearch, String> colTitle;

    @FXML
    private TableColumn<BookSearch, String> colAuthor;

    @FXML
    private TableColumn<BookSearch, String> colKeyword;

    @FXML
    private TableColumn<BookSearch, String> colLanguage;

    @FXML
    private TableColumn<BookSearch, String> colPublisher;

    @FXML
    private TextField searchBar;

    ObservableList<BookSearch> bookSearchObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resource) {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        String bookQuery = "SELECT isbn, title, author, keyword, language, publisher FROM LibraryTest.Book";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(bookQuery);

            while(resultSet.next()){
                String queryIsbn = resultSet.getString("isbn");
                String queryTitle = resultSet.getString("title");
                String queryAuthor = resultSet.getString("author");
                String queryKeyword = resultSet.getString("keyword");
                String queryLanguage = resultSet.getString("language");
                String queryPublisher = resultSet.getString("publisher");

                //Populate the observableList with results from our SQL Query
                bookSearchObservableList.add(new BookSearch(queryIsbn, queryTitle, queryAuthor, queryKeyword,queryLanguage,queryPublisher));

            }
            // Sets values in the table columns
            colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            colKeyword.setCellValueFactory(new PropertyValueFactory<>("keyword"));
            colLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
            colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));

            //sets the results from SQL Query to the table view
            tableview.setItems(bookSearchObservableList);

            FilteredList<BookSearch> filteredData = new FilteredList<>(bookSearchObservableList, b -> true);

            searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(BookSearch -> {
                    //if there's no search results, then display all records
                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(BookSearch.getIsbn().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; //Means we found a match in the isbn attribute
                    }
                    else if(BookSearch.getTitle().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; //Means we found a match in the title attribute
                    }
                    else if(BookSearch.getAuthor().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; //Means we found a match in the author attribute
                    }
                    else if(BookSearch.getKeyword().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; //Means we found a match in the genre/keyword attribute
                    }
                    else
                        return false;
                });
            });

            SortedList<BookSearch> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableview.comparatorProperty());

            tableview.setItems(sortedData);

        }catch (SQLException e){
            Logger.getLogger(BookSearchController.class.getName()).log(Level.SEVERE, null, e);

        }

    }
}