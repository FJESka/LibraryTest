package adminInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;


public class AdminApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Locale.setDefault(new Locale("English", "en"));
        Parent root = FXMLLoader.load(getClass().getResource("AdminStart.fxml"));
        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Manage menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

