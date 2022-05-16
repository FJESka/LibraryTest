package com.example.loginform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Login page");
        stage.setScene(scene);
        stage.show();
    }

    public void profilePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("ProfilePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        Stage stage = new Stage();
        stage.setTitle("Profile page");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}