package adminInterface;

import bookSearch.BookSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static bookSearch.DatabaseConnection.getConnection;

public class ManageBooksController extends ManageController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<BookSearch, String > colBookAuthor;

    @FXML
    private TableColumn<BookSearch, String> colBookISBN;

    @FXML
    private TableColumn<BookSearch, String> colBookKeyword;

    @FXML
    private TableColumn<BookSearch, String> colBookLanguage;

    @FXML
    private TableColumn<BookSearch, String> colBookPublisher;

    @FXML
    private TableColumn<BookSearch, String> colBookTitle;

    @FXML
    private TableView<BookSearch> mBooksTableview;

    @FXML
    private TextField tfBookAuthor;

    @FXML
    private TextField tfBookISBN;

    @FXML
    private TextField tfBookKeyword;

    @FXML
    private TextField tfBookLanguage;

    @FXML
    private TextField tfBookPublisher;

    @FXML
    private TextField tfBookTitle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showList();
    }

    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        BookSearch book = mBooksTableview.getSelectionModel().getSelectedItem();
        tfBookISBN.setText(book.getIsbn());
        tfBookTitle.setText(book.getTitle());
        tfBookAuthor.setText(book.getAuthor());
        tfBookKeyword.setText(book.getKeyword());
        tfBookLanguage.setText(book.getLanguage());
        tfBookPublisher.setText(book.getPublisher());

    }


    public ObservableList<BookSearch> getRecords() {
        String bookQuery = "SELECT isbn, title, author, keyword, language, publisher FROM Book";
        ObservableList<BookSearch> bookSearchObservableList = FXCollections.observableArrayList();

        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(bookQuery);

            while (resultSet.next()) {
                String queryIsbn = resultSet.getString("isbn");
                String queryTitle = resultSet.getString("title");
                String queryAuthor = resultSet.getString("author");
                String queryKeyword = resultSet.getString("keyword");
                String queryLanguage = resultSet.getString("language");
                String queryPublisher = resultSet.getString("publisher");

                //Populate the observableList with results from our SQL Query
                bookSearchObservableList.add(new BookSearch(queryIsbn, queryTitle, queryAuthor, queryKeyword, queryLanguage, queryPublisher));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookSearchObservableList;
    }

    public void showList(){
        ObservableList<BookSearch> list = getRecords();
        // Sets values in the table columns
        colBookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colBookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colBookKeyword.setCellValueFactory(new PropertyValueFactory<>("keyword"));
        colBookLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        colBookPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        //sets the results from SQL Query to the table view
        mBooksTableview.setItems(list);
    }

    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String isbn = tfBookISBN.getText();
            if(isFieldEmpty(isbn) == true){
                emptyFieldAlert();
            }else{
                String title = tfBookTitle.getText();
                String author = tfBookAuthor.getText();
                String keyword = tfBookKeyword.getText();
                String language = tfBookLanguage.getText();
                String publisher = tfBookPublisher.getText();

                String insert = "INSERT INTO `Book` (`isbn`, `title`, `author`, `keyword`, `language`, `publisher`) VALUES ('" + isbn + "', '" + title + "', '" + author +"', '" + keyword + "', '" + language + "','" + publisher + "');";

                statement.executeUpdate(insert);
            }
            showList();


        } catch (SQLException ex) {
            ex.printStackTrace();

        }

    }

    public void update(){

    }

    public void delete(){

    }


}
