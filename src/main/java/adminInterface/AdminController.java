package adminInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToManageCopies(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manageCopies.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToManageBooks(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manageBooks.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToManageDvds(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manageDvds.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToOverdueLoans(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("overdue.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 900, 400);
        stage.setScene(scene);
        stage.show();
    }

}
