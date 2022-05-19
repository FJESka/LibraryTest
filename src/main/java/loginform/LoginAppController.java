package com.example.loginform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class LoginAppController extends LoginApp {
    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private PasswordField loginPasswordPasswordField;

    @FXML
    private Button LoginButton;

    @FXML
    private Button registerUser;

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
        SQLCode sqlLoginCode = new SQLCode();

        //Wrong username and password gives the user an error with information.
        boolean flag = sqlLoginCode.validate(usernameID, passwordID);
        if (!flag)
        {
            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);
            loginAlert.setTitle("Login Failed");
            loginAlert.setContentText("Please enter correct username and password");
            loginAlert.showAndWait();
        }
        else
        //Successful login will push the user back to the search page where they can find their books that they wish to borrow.
        {
            Scene currentScene = LoginButton.getScene();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("SEARCHPAGE.fxml"));
            Parent tempRoot = loader.load();
            currentScene.setRoot(tempRoot);
        }
        return;
    }

    public void registerUserButton() throws IOException{
            Scene currentScene = registerUser.getScene();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterUser.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }
    }

