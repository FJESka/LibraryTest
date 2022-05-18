package com.example.loginform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class ButtonController {

        @FXML
        private Text profileinfo;

        @FXML
        private Button SearchButton;

        @FXML
        private Button ReturnButton;

        @FXML
        private Button BorrowButton;

        @FXML
        private Button LoginButton;

        @FXML
        private Button ProfileButton;

        @FXML
        private ImageView Profilbild;

        public void loginButtonAction() throws IOException {
            Scene currentScene = LoginButton.getScene();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }

        public void profileButtonAction() throws IOException{
            Scene currentScene = ProfileButton.getScene();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfilePage.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }

        public void borrowButtonAction() throws IOException{
            Scene currentScene = BorrowButton.getScene();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("INSERTBORROWBUTTONTEXTHERE.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }

        public void returnButtonAction() throws IOException{
            Scene currentScene = ReturnButton.getScene();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("INSERTRETURNBUTTONTEXTHERE.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }

        public void searchButtonAction() throws IOException{
            Scene currentScene = SearchButton.getScene();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("INSERTSEARCHBUTTONTEXTHERE.fxml"));
            Parent nextScene = loader.load();
            currentScene.setRoot(nextScene);
        }
}
