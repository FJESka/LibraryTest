package adminInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static databaseConnection.DatabaseConnection.getConnection;

public class ManageDvdsController extends ManageController {


    @FXML
    private TableColumn<Dvd, String> colDvdActors;

    @FXML
    private TableColumn<Dvd, String> colDvdAgeRestriction;

    @FXML
    private TableColumn<Dvd, String> colDvdCountry;

    @FXML
    private TableColumn<Dvd, String> colDvdDirector;

    @FXML
    private TableColumn<Dvd, String> colDvdGenre;

    @FXML
    private TableColumn<Dvd, Integer> colDvdID;

    @FXML
    private TableColumn<Dvd, String> colDvdLanguage;

    @FXML
    private TableColumn<Dvd, String> colDvdTitle;

    @FXML
    private TableView<Dvd> mDvdTableview;

    @FXML
    private TextField tfDvdActors;

    @FXML
    private TextField tfDvdAgeRestriction;

    @FXML
    private TextField tfDvdCountry;

    @FXML
    private TextField tfDvdDirector;

    @FXML
    private TextField tfDvdGenre;

    @FXML
    private TextField tfDvdID;

    @FXML
    private TextField tfDvdLanguage;

    @FXML
    private TextField tfDvdTitle;


    //Hanterar val i tableview och sätter textfält till value av det valda objektet
    public void handleMouseAction(javafx.scene.input.MouseEvent mouseEvent) {
        Dvd dvd = mDvdTableview.getSelectionModel().getSelectedItem();
        tfDvdID.setText("" + dvd.getDvdID());
        tfDvdTitle.setText(dvd.getTitle());
        tfDvdDirector.setText(dvd.getDirector());
        tfDvdActors.setText(dvd.getActors());
        tfDvdGenre.setText(dvd.getGenre());
        tfDvdLanguage.setText(dvd.getLanguage());
        tfDvdAgeRestriction.setText(dvd.getAgeRestriction());
        tfDvdCountry.setText(dvd.getCountry());

    }

    //Rensar alla textfält
    public void clearTextfields(){
        tfDvdID.clear();
        tfDvdTitle.clear();
        tfDvdDirector.clear();
        tfDvdGenre.clear();
        tfDvdActors.clear();
        tfDvdLanguage.clear();
        tfDvdAgeRestriction.clear();
        tfDvdCountry.clear();
    }

    //kollar om textfält är tomma, om de är tomma skapas en alert
    public boolean areFieldsEmpty() {
        boolean fieldsEmpty = false;
        if (isFieldEmpty(tfDvdID.getText()) == true) {
            String field = "Dvd ID";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfDvdTitle.getText()) == true) {
            String field = "Title";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfDvdDirector.getText()) == true) {
            String field = "Director";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        } else if (isFieldEmpty(tfDvdLanguage.getText()) == true) {
            String field = "Language";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }else if (isFieldEmpty(tfDvdActors.getText()) == true) {
            String field = "Actors";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }else if (isFieldEmpty(tfDvdGenre.getText()) == true) {
            String field = "Genre";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }else if (isFieldEmpty(tfDvdCountry.getText()) == true) {
            String field = "Country";
            emptyFieldAlert(field);
            fieldsEmpty = true;
        }
        return fieldsEmpty;
    }

    //Hämtar dvdID från det valda objektet
    public String getDvdID(){
        Dvd dvd = mDvdTableview.getSelectionModel().getSelectedItem();
        String dvdID = "" + dvd.getDvdID();
        return dvdID;
    }

    //Kör sökquery och lägger resultaten i en lista
    public ObservableList<Dvd> getRecords() {
        String dvdQuery = "SELECT id, title, director, genre, language, actors, ageRestriction, country FROM Dvd";
        ObservableList<Dvd> dvdObservableList = FXCollections.observableArrayList();

        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(dvdQuery);

            while (resultSet.next()) {
                int querydvdID = resultSet.getInt("id");
                String queryTitle = resultSet.getString("title");
                String queryDirector = resultSet.getString("director");
                String queryGenre = resultSet.getString("genre");
                String queryLanguage = resultSet.getString("language");
                String queryActors = resultSet.getString("actors");
                String queryAgeRestriction = resultSet.getString("ageRestriction");
                String queryCountry = resultSet.getString("country");

                //Populate the observableList with results from our SQL Query
                dvdObservableList.add(new Dvd(querydvdID, queryTitle, queryDirector, queryGenre, queryLanguage, queryActors, queryAgeRestriction, queryCountry));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dvdObservableList;
    }

    //Kollar om dvdID som användaren försöker ange finns i databasen redan
    public boolean doValuesExistInDatabase() throws SQLException {
        boolean valuesExist = true;

        if (tfDvdID.getText() != null && tfDvdID.getText().isBlank() != true) {
            int dvdID = Integer.parseInt(tfDvdID.getText());
            if (doesDvdIDExistInDatabase(dvdID) == true) {
                Alert a = new Alert();
                String message = "The title already exists in the database.";
                a.alertMessage(javafx.scene.control.Alert.AlertType.INFORMATION, message);
                valuesExist = true;
            }
            else{
                valuesExist = false;
            }
        }
        return valuesExist;
    }

    //sätter värdena i tableviews fält och tableview
    public void showList(){
        ObservableList<Dvd> list = getRecords();
        // Sets values in the table columns
        colDvdID.setCellValueFactory(new PropertyValueFactory<Dvd, Integer>("dvdID"));
        colDvdTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDvdDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        colDvdGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDvdLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        colDvdActors.setCellValueFactory(new PropertyValueFactory<>("actors"));
        colDvdAgeRestriction.setCellValueFactory(new PropertyValueFactory<>("ageRestriction"));
        colDvdCountry.setCellValueFactory(new PropertyValueFactory<>("country"));


        //sets the results from SQL Query to the table view
        mDvdTableview.setItems(list);
    }

    //hämtar values från användarens input och returnerar rätt query beroende på vilken knapp användaren tryckt på
    public String getValuesAndQuery(Statement statement, String whichQuery) throws SQLException {
        String query = null;

        if(areFieldsEmpty() == true){

        }else {
            int dvdID = Integer.parseInt(tfDvdID.getText());
            String title = tfDvdTitle.getText();
            String genre = tfDvdGenre.getText();
            String director = tfDvdDirector.getText();
            String actors = tfDvdActors.getText();
            String language = tfDvdLanguage.getText();
            String ageRestriction = tfDvdAgeRestriction.getText();
            String country = tfDvdCountry.getText();


            if (whichQuery.equalsIgnoreCase("insert")) {
                String insert = AdminQueries.getDvdInsert(dvdID, title, director, genre, language, actors, ageRestriction, country);
                query = insert;
            }
            if (whichQuery.equalsIgnoreCase("update")) {
                String dvdIDBeforeUpdate = getDvdID();
                String update = AdminQueries.getDvdUpdate(dvdID, title, director, genre, language, actors, ageRestriction, country, dvdIDBeforeUpdate);
                query = update;
            }
        }

        return query;
    }

    //Metoden för att göra insert, körs när användaren trycker på insert knappen
    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String whichQuery = "insert";
            String query = getValuesAndQuery(statement, whichQuery);

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
            String query = getValuesAndQuery(statement, whichQuery);

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
        String dvdIDBeforeUpdate = getDvdID();
        String delete = AdminQueries.getDvdDelete(dvdIDBeforeUpdate);

        try{
            Statement statement = getConnection().getDBConnection().createStatement();
            int dvdID = Integer.parseInt(tfDvdID.getText());
            if(isFieldEmpty(String.valueOf(dvdID)) == true) {
                String field = "DvdID";
                emptyFieldAlert(field);
            }else{
                if(confirmationAlert() == true) {
                    statement.executeUpdate(delete);
                    clearTextfields();
                }
                showList();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }


}
