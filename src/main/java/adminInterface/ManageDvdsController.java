package adminInterface;

import bookSearch.BookSearch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class ManageDvdsController extends ManageController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

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

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        showList();
//    }

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

    public String getDvdID(){
        Dvd dvd = mDvdTableview.getSelectionModel().getSelectedItem();
        String dvdID = "" + dvd.getDvdID();
        return dvdID;
    }

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

    public void getValues(Statement statement, String whichQuery) throws SQLException {

        int dvdID = Integer.parseInt(tfDvdID.getText());
        if(isFieldEmpty(String.valueOf(dvdID)) == true){
            String field = "DvdID";
            emptyFieldAlert(field);
        }else{
            String title = tfDvdTitle.getText();
            String genre = tfDvdGenre.getText();
            String director = tfDvdDirector.getText();
            String actors = tfDvdActors.getText();
            String language = tfDvdLanguage.getText();
            String ageRestriction = tfDvdAgeRestriction.getText();
            String country = tfDvdCountry.getText();


            if(whichQuery.equalsIgnoreCase("insert")){
                String insert = AdminQueries.getDvdInsert(dvdID, title, director, genre, language, actors, ageRestriction, country);
                statement.executeUpdate(insert);
            }
            if(whichQuery.equalsIgnoreCase("update")){
                String dvdIDBeforeUpdate = getDvdID();
                String update = AdminQueries.getDvdUpdate(dvdID, title, director, genre, language, actors, ageRestriction, country, dvdIDBeforeUpdate);
                statement.executeUpdate(update);
            }


        }
    }

    public void insert(){

        try {
            Statement statement = getConnection().getDBConnection().createStatement();

            String whichQuery = "insert";
            getValues(statement, whichQuery);

        showList();

        } catch (SQLException ex) {
            ex.printStackTrace();

        }

    }

    public void update() {
        try {
            Statement statement = getConnection().getDBConnection().createStatement();
            String whichQuery = "update";
            getValues(statement, whichQuery);

//                String update = "UPDATE `Dvd` SET `id` = '" + dvdID +"', `title` = '"+ title +"', `director` = '"+ director +"', `genre` = '"+ genre +"', `language` = '"+ language +"', `actors` = '"+ actors +"', `ageRestriction` = '"+ ageRestriction +"', `country` = '"+ country +"' WHERE (`id` = '" + dvdIDBeforeUpdate + "');";

            clearTextfields();
            showList();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

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
