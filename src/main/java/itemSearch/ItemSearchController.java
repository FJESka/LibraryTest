package itemSearch;

import databaseConnection.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import loginform.SQLCode;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static databaseConnection.DatabaseConnection.getConnection;

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
    //Hanterar buttons admin view, loan, login och return
    public void buttonAction(ActionEvent event) throws IOException, SQLException {
        if(event.getSource() == btnAdminView){
            //Kontroll p?? om anv??ndare ??r inloggad och om de ??r en admin. Om de ??r en admin skickas de till admin startsida, om inte skapas en alert.
            if(SQLCode.getMember() != null && memberType(SQLCode.getMember()).equalsIgnoreCase("admin"))
            Scenes.adminPage();
            else{
                Alert a = new Alert();
                String message = "Only administrators can access this view.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
            }

        }

        if(event.getSource() == btnLoan){
            //Kontroll f??r om anv??ndare ??r inloggad, om de ??r det kommer de till l??nsidan, om inte skapas en alert
            if(SQLCode.getMember() != null){
                Scenes.loanPage();

            }else{
                Alert a = new Alert();
                String message = "You need to be logged in to loan.";
                if(a.alertConfirmation(message) == true){
                    Scenes.loginPage();
                }

            }
        }

        if(event.getSource() == btnLogin){
            //Kontroll f??r om anv??ndare ??r inloggad, om de inte ??r det kommer de till l??nsidan, om de ??r det skapas en alert
            if(SQLCode.getMember() == null){
                Scenes.loginPage();
            }else{
                Alert a = new Alert();
                String message = "You are already logged in.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
            }

        }

        //Tar anv??ndaren till retur sida
        if(event.getSource() == btnReturn){
            Scenes.returnPage();
        }
    }


    @Override
   public void initialize(URL url, ResourceBundle resource) {

        try {
            //Skapar en databaseconnection och k??r s??kquery
            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(ItemSearchQueries.newSearchAllItems);

            //s??tter s??kresultat i strings, skapar ett item och l??gger in det i en lista
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


                itemSearchObservableList.add(new ItemSearch(qIsbn, qTitle, qAuthor, qKeyword, qLanguage, qPublisher, qActors, qAgeRestriction, qCountry, qTotalCopies, qAvailable));

            }
            // S??tter v??rden i tabellkolumner och i tableview
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

            //Skapar en listener som kollar s??kf??ltets nya v??rden
            searchBar1.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ItemSearch -> {
                    //Om det inte matas in n??got nytt value, visa alla resultat
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    //Kollar det inmatade s??kordet mot v??rden i tableview
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

            //Skapar en sorted list som har s??kresultaten
            SortedList<ItemSearch> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableview1.comparatorProperty());

            //s??tter s??kresultatet i tableview
            tableview1.setItems(sortedData);

        } catch (SQLException e) {
            Logger.getLogger(ItemSearchController.class.getName()).log(Level.SEVERE, null, e);

        }

    }

    //H??mtar memberType fr??n memberID
    public String memberType(int memberID) throws SQLException {
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = ItemSearchQueries.getMemberType(memberID);
        ResultSet resultSet = statement.executeQuery(query);
        String result = null;

        if(resultSet.next()){
            result = resultSet.getString("memberType");
        }
        return result;
    }
}
