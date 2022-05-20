package itemSearch;

import bookSearch.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import loginform.SQLLoginCode;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemSearchController implements Initializable {

    @FXML
    private TableView<ItemSearch> tableview1;

    @FXML
    private TableColumn<ItemSearch, String> colISBN1;

    @FXML
    private TableColumn<ItemSearch, String> colTitle1;

    @FXML
    private TableColumn<ItemSearch, String> colAuthor1;

    @FXML
    private TableColumn<ItemSearch, String> colKeyword1;

    @FXML
    private TableColumn<ItemSearch, String> colLanguage1;

    @FXML
    private TableColumn<ItemSearch, String> colPublisher1;

    @FXML
    private TableColumn<ItemSearch, String> colActors1;

    @FXML
    private TableColumn<ItemSearch, String> colAgeRestriction1;

    @FXML
    private TableColumn<ItemSearch, String> colCountry1;

    @FXML
    private TableColumn<ItemSearch, Integer> colTotalCopies1;

    @FXML
    private TableColumn<ItemSearch, Integer> colAvailable1;

    @FXML
    private TextField searchBar1;

    @FXML
    private Button btnAdminView;

    @FXML
    private Button btnLoan;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnReturn;

    ObservableList<ItemSearch> itemSearchObservableList = FXCollections.observableArrayList();

    @FXML
    public void buttonAction(ActionEvent event) throws IOException {
        if(event.getSource() == btnAdminView){
            //Kontroll på om användare är inloggad och om de är en admin?
//            if(SQLLoginCode.Member() != null && SQLLoginCode.Type().equalsIgnoreCase("admin"))
            Scenes.adminPage();
//            else{
//                Warnings a = new Warnings();
//                String message = "Only administrators can access this view.";
//                a.alertMessage(Alert.AlertType.INFORMATION, message);
//            }

        }

        if(event.getSource() == btnLoan){
            //Kontroll för om användare är inloggad?
            if(SQLLoginCode.Member() != null){
                Scenes.loanPage();

            }else{
                Warnings a = new Warnings();
                String message = "You need to be logged in to loan.";
                if(a.alertConfirmation(message) == true){
                    Scenes.loginPage();
                }

            }
        }

        if(event.getSource() == btnLogin){
            //Lägg in if sats för om användare redan är inloggad så kommer alert/profilsida?
            if(SQLLoginCode.Member() == null){
                Scenes.loginPage();
            }else{
                Warnings a = new Warnings();
                String message = "You are already logged in.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
            }

        }

        if(event.getSource() == btnReturn){
            Scenes.returnPage();
        }
    }

//    @FXML
//    public void loanPage(ActionEvent event) throws IOException {
//        if(SQLLoginCode.Member() != null){
//            Parent root = FXMLLoader.load(getClass().getResource("loan2.fxml"));
//            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.show();
//
//        }else{
//            Warnings a = new Warnings();
//            String message = "You need to be logged in to loan.";
//            if(a.alertConfirmation(message) == true){
//                Scenes.loginPage();
//            }
//
//        }
//
//    }

    @Override
   public void initialize(URL url, ResourceBundle resource) {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBConnection();

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(ItemSearchQueries.newSearchAllItems);

            while (resultSet.next()) {
                String qIsbn = resultSet.getString("isbn");
                String qTitle = resultSet.getString("title");
                String qAuthor = resultSet.getString("author");
                String qKeyword = resultSet.getString("keyword");
                String qLanguage = resultSet.getString("language");
                String qPublisher = resultSet.getString("publisher");
                String qActors = resultSet.getString("actors");
                String qAgeRestriction = resultSet.getString("ageRestriction");
                String qCountry = resultSet.getString("country");
                Integer qTotalCopies = resultSet.getInt("totalCopies");
                Integer qAvailable = resultSet.getInt("available");

                //Populate the observableList with results from our SQL Query

                itemSearchObservableList.add(new ItemSearch(qIsbn, qTitle, qAuthor, qKeyword, qLanguage, qPublisher, qActors, qAgeRestriction, qCountry, qTotalCopies, qAvailable));

            }
            // Sets values in the table columns

            colISBN1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("isbn"));
            colTitle1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("title"));
            colAuthor1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("author"));
            colKeyword1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("keyword"));
            colLanguage1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("language"));
            colPublisher1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("publisher"));
            colActors1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("actors"));
            colAgeRestriction1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("ageRestriction"));
            colCountry1.setCellValueFactory(new PropertyValueFactory<ItemSearch, String>("country"));
            colTotalCopies1.setCellValueFactory(new PropertyValueFactory<ItemSearch, Integer>("totalCopies"));
            colAvailable1.setCellValueFactory(new PropertyValueFactory<ItemSearch, Integer>("available"));

            //sets the results from SQL Query to the table view
            tableview1.setItems(itemSearchObservableList);

            FilteredList<ItemSearch> filteredData = new FilteredList<>(itemSearchObservableList, b -> true);

            searchBar1.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ItemSearch -> {
                    //if there's no search results, then display all records
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (ItemSearch.getIsbn().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the isbn attribute
                    } else if (ItemSearch.getTitle().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the title attribute
                    } else if (ItemSearch.getAuthor().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the author attribute
                    } else if (ItemSearch.getKeyword().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the genre/keyword attribute
                    } else if (ItemSearch.getLanguage().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the language attribute
                    } else if (ItemSearch.getPublisher().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the publisher attribute
                    } else if (ItemSearch.getActors().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the actors attribute
                    } else if (ItemSearch.getCountry().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the country attribute
                    } else if (ItemSearch.getAgeRestriction().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; //Means we found a match in the age restriction attribute
                    } else
                        return false;
                });
            });

            SortedList<ItemSearch> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableview1.comparatorProperty());

            tableview1.setItems(sortedData);

        } catch (SQLException e) {
            Logger.getLogger(ItemSearchController.class.getName()).log(Level.SEVERE, null, e);

        }

    }
}
