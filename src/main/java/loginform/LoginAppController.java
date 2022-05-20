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
    private Button registerUser;

    //This method is triggered once you click the submit button in the FXML view.
    @FXML
    private void submitButtonAction (ActionEvent event) throws SQLException, IOException {
        //Checks if there is a username in the field username.
        if (loginUsernameTextField.getText().isEmpty())
        {
        Alert usernameAlert = new Alert(Alert.AlertType.INFORMATION);
        usernameAlert.setTitle("Missing Username");
        usernameAlert.setContentText("Please enter your username before proceeding");
        usernameAlert.showAndWait();
        return;
        //Checks if there is a password in the field for password.
        }
        if (loginPasswordPasswordField.getText().isEmpty()){
            Alert passwordAlert = new Alert(Alert.AlertType.INFORMATION);
            passwordAlert.setTitle("Display message");
            passwordAlert.setContentText("Please enter your password before proceeding");
            passwordAlert.showAndWait();

            return;
            //If both fields have values upon clicking on the submit button then the text will be given over to the sqlLoginCode method
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
        //Successful login will inform the user that they are now logged in and where they can find the librarians for help.
        {
            Alert loginAlert = new Alert(Alert.AlertType.INFORMATION);
            loginAlert.setTitle("Login Successful");
            loginAlert.setContentText("Login Successful, Welcome "+usernameID+" to your library account.\n" +
                    "Should you need any help you can find our librarians at the helpdesk.");
            loginAlert.show();
        }
        return;
    }

    //THis code is to load the Register User section to allow for a user to register.
    public void registerUserButton() throws IOException{
            Scene currentScene = registerUser.getScene();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterUser.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }
    }

