package adminInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import static databaseConnection.DatabaseConnection.getConnection;
import static adminInterface.AdminQueries.doesDvdIDExist;
import static adminInterface.AdminQueries.doesISBNExist;

public class ManageController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showList();
    }

    @FXML

    public void handleButtonAction(ActionEvent event){

        if(event.getSource() == btnInsert){
            insert();
        }
        if(event.getSource() == btnUpdate){
            update();
        }
        if(event.getSource() == btnDelete){
            delete();
        }

    }


    //skapar en alert som frågar om du är säker på att du vill radera
    public boolean confirmationAlert(){
        Boolean okToDelete = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            okToDelete = true;
        }
        return okToDelete;
    }

    //Rensar alla textfält
    public void clearTextfields(){

    }


    //kollar om fält är tomt
    boolean isFieldEmpty(String string){
        boolean isFieldEmpty = false;
        if(string.isEmpty() || string == null) {
            isFieldEmpty = true;
        }
        return isFieldEmpty;
    }

    //skapar en alert som säger att textfält är tomt
    public void emptyFieldAlert(String field){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(field + " can't be empty. Please add a " + field + " or select a row to update.");
        alert.showAndWait();
    }


    //buter tillbaka till admin startsida
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

    //Kollar om isbn finns i databasen redan
    public boolean doesISBNExistInDatabase(String isbn) throws SQLException {
        boolean isbnExists = false;
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = doesISBNExist(isbn);
        ResultSet resultSet = statement.executeQuery(query);
        String resultISBN = null;

        if(resultSet.next()){
            resultISBN = resultSet.getString("isbn");
        }
        if(isbn.equalsIgnoreCase(resultISBN)){
            isbnExists = true;
        }
        else{
            isbnExists = false;
        }
        return isbnExists;
    }

    //Kollar om dvdID finns i databasen redan
    public boolean doesDvdIDExistInDatabase(int dvdID) throws SQLException {
        boolean dvdIDExists = false;
        Statement statement = getConnection().getDBConnection().createStatement();

        String query = doesDvdIDExist(String.valueOf(dvdID));
        ResultSet resultSet = statement.executeQuery(query);
        int resultDvdID = 0;

        if(resultSet.next()){
            resultDvdID = resultSet.getInt("id");
        }
        if(dvdID != 0 && dvdID == resultDvdID){
            dvdIDExists = true;
        }
        else{
            dvdIDExists = false;
        }
        return dvdIDExists;
    }

    public void insert(){

    }

    public void update(){

    }

    public void delete(){

    }

    public void showList(){

    }
}

