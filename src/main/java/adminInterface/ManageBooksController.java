package adminInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static databaseConnection.DatabaseConnection.getConnection;

public class ManageBooksController extends ManageController {


    @FXML
    private TableColumn<Book, String > colBookAuthor;

    @FXML
    private TableColumn<Book, String> colBookISBN;

    @FXML
    private TableColumn<Book, String> colBookKeyword;

    @FXML
    private TableColumn<Book, String> colBookLanguage;

    @FXML
    private TableColumn<Book, String> colBookPublisher;

    @FXML
    private TableColumn<Book, String> colBookTitle;

    @FXML
    private TableView<Book> mBooksTableview;

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

    //Hanterar val i tableview och sätter textfält till value av det valda objektet
    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        Book book = mBooksTableview.getSelectionModel().getSelectedItem();
        tfBookISBN.setText(book.getIsbn());
        tfBookTitle.setText(book.getTitle());
        tfBookAuthor.setText(book.getAuthor());
        tfBookKeyword.setText(book.getKeyword());
        tfBookLanguage.setText(book.getLanguage());
        tfBookPublisher.setText(book.getPublisher());

    }

    //Rensar alla textfält
    public void clearTextfields(){
        tfBookISBN.clear();
        tfBookTitle.clear();
        tfBookAuthor.clear();
        tfBookKeyword.clear();
        tfBookLanguage.clear();
        tfBookPublisher.clear();
    }

    //Hämtar isbn från det valda objektet
    public String getIsbn(){
        Book book = mBooksTableview.getSelectionModel().getSelectedItem();
        String isbn = book.getIsbn();
        return isbn;
    }

    //Kör sökquery och lägger resultaten i en lista
    public ObservableList<Book> getRecords() {
        String bookQuery = "SELECT isbn, title, author, keyword, language, publisher FROM Book";
        ObservableList<Book> bookObservableList = FXCollections.observableArrayList();

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
                bookObservableList.add(new Book(queryIsbn, queryTitle, queryAuthor, queryKeyword, queryLanguage, queryPublisher));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookObservableList;
    }


    //sätter värdena i tableviews fält och tableview
    public void showList(){
        ObservableList<Book> list = getRecords();
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

    //kollar om textfält är tomma, om de är tomma skapas en alert
    public boolean areFieldsEmpty() {
        boolean fieldsEmpty = false;
        if (isFieldEmpty(tfBookISBN.getText()) == true) {
            String field = "ISBN";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfBookTitle.getText()) == true) {
            String field = "Title";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfBookAuthor.getText()) == true) {
            String field = "Author";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfBookLanguage.getText()) == true) {
            String field = "Language";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }else if (isFieldEmpty(tfBookPublisher.getText()) == true) {
            String field = "Publisher";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }else if (isFieldEmpty(tfBookKeyword.getText()) == true) {
            String field = "Keyword";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }
        return fieldsEmpty;
    }

    //Kollar om isbn som användaren försöker ange finns i databasen redan
    public boolean doValuesExistInDatabase() throws SQLException {
        boolean valuesExist = true;
        if (tfBookISBN.getText() != null && tfBookISBN.getText().isBlank() != true) {
            if (doesISBNExistInDatabase(tfBookISBN.getText()) == true) {
                Alert a = new Alert();
                String message = "The title already exists in the database.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
                valuesExist = true;
            } else {
                valuesExist = false;
            }
        }
        return valuesExist;
    }

    //hämtar values från användarens input och returnerar rätt query beroende på vilken knapp användaren tryckt på
    public String getValuesAndQuery(Statement statement, String whichQuery) throws SQLException {
        String query = null;

        if(areFieldsEmpty() == true){

        }
        else {
            String isbn = tfBookISBN.getText();
            String title = tfBookTitle.getText();
            String author = tfBookAuthor.getText();
            String keyword = tfBookKeyword.getText();
            String language = tfBookLanguage.getText();
            String publisher = tfBookPublisher.getText();

            if (whichQuery.equalsIgnoreCase("insert")) {

                String insert = "INSERT INTO `Book` (`isbn`, `title`, `author`, `keyword`, `language`, `publisher`) VALUES ('" + isbn + "', '" + title + "', '" + author + "', '" + keyword + "', '" + language + "','" + publisher + "');";

                query = insert;


            } else if (whichQuery.equalsIgnoreCase("update")) {
                String isbnBeforeUpdate = getIsbn();

                String update = "UPDATE `Book` SET `isbn` = '" + isbn + "', `title` = '" + title + "', `author` = '" + author + "', `keyword` = '" + keyword + "', `language` = '" + language + "' WHERE (`isbn` = '" + isbnBeforeUpdate + "');";

                query = update;


            } else {

                String deleteBook = "DELETE FROM `Book` WHERE (`isbn` = '" + isbn + "');";
                query = deleteBook;
            }
        }

        return query;

    }

    //Metoden för att göra insert, körs när användaren trycker på insert knappen
    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "insert";
            String query = getValuesAndQuery(statement,whichQuery );

            if(query != null && doValuesExistInDatabase() == false && areFieldsEmpty() == false){
                statement.executeUpdate(query);
            }

            showList();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

    }

    //Metoden för att göra update, körs när användaren trycker på update knappen
    public void update() {
        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String whichQuery = "update";
            String query = getValuesAndQuery(statement,whichQuery );

            if(query != null && doValuesExistInDatabase() == false && areFieldsEmpty() == false){
                statement.executeUpdate(query);
            }

            clearTextfields();
            showList();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Metoden för att göra delete, körs när användaren trycker på delet knappen.
    //Skapar en confirmation alert innan delete querien körs för att kolla så användaren är säker på att de vill delete.
    public void delete(){

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "delete";
            String query = getValuesAndQuery(statement, whichQuery);

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
