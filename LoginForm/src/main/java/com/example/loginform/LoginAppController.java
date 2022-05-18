package com.example.loginform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginAppController extends LoginApp {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private PasswordField loginPasswordPasswordField;

    @FXML
    private BorderPane BorderPane1;

    @FXML
    private Button LoginButton;

    @FXML
    private void submitButtonAction (ActionEvent event) throws SQLException, IOException {

        if (loginUsernameTextField.getText().isEmpty())
        {
        Alert usernameAlert = new Alert(Alert.AlertType.INFORMATION);
        usernameAlert.setTitle("Missing Username");
        usernameAlert.setContentText("Please enter your username before proceeding");
        usernameAlert.showAndWait();
        return;
        }
        if (loginPasswordPasswordField.getText().isEmpty()){
            Alert passwordAlert = new Alert(Alert.AlertType.INFORMATION);
            passwordAlert.setTitle("Display message");
            passwordAlert.setContentText("Please enter your password before proceeding");
            passwordAlert.showAndWait();
            return;
        }

        String usernameID = loginUsernameTextField.getText();
        String passwordID = loginPasswordPasswordField.getText();
        SQLLoginCode sqlLoginCode = new SQLLoginCode();
        boolean flag = sqlLoginCode.validate(usernameID, passwordID);
        if (!flag)
        {
            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);
            loginAlert.setTitle("Login Failed");
            loginAlert.setContentText("Please enter correct username and password");
            loginAlert.showAndWait();
        }
        else
        {
            System.out.println(SQLLoginCode.getMember());
            Scene currentScene = LoginButton.getScene();
            //Parent currentRoot = currentScene.getRoot();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
            Parent tempRoot = loader.load();
            ProfilePageController controller = loader.<ProfilePageController>getController();
            controller.Profiledata();
            currentScene.setRoot(tempRoot);
            //INSERT CODE HERE TO DIRECT USER TO PROFILE PAGE
        }
        return;
    }

}