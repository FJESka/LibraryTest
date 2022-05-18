package adminInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;


public class AdminApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Locale.setDefault(new Locale("English", "en"));
        FXMLLoader fxmlLoader = new FXMLLoader(AdminApplication.class.getResource("manageCopies.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Manage copies");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

