package itemSearch;

import adminInterface.AdminApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.library.HelloApplication;
import loginform.LoginApp;

import java.io.IOException;
import java.util.Locale;

public class Scenes {

    public static void loginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        Stage stage = new Stage();
        stage.setTitle("Login page");
        stage.setScene(scene);
        stage.show();
    }

    public static void loanPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loan.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Return Item");
        stage.setScene(scene);
        stage.show();

    }

    public static void adminPage() throws IOException {
        Locale.setDefault(new Locale("English", "en"));
        Parent root = FXMLLoader.load(AdminApplication.class.getResource("AdminStart.fxml"));
        Scene scene = new Scene(root, 700, 400);
        Stage stage = new Stage();
        stage.setTitle("Manage menu");
        stage.setScene(scene);
        stage.show();

    }

    public static void returnPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("returnItem.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Return Item");
        stage.setScene(scene);
        stage.show();

    }

}
