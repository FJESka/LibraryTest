package loginform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.w3c.dom.Text;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;

public class RegisterUserController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField userNameField;

    @FXML
    private Button backButton;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField socialSecurityField;

    @FXML
    public void register(ActionEvent event) throws SQLException, IOException {

        Window owner = submitButton.getScene().getWindow();

        if (firstNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }

        if (lastNameField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your name");
        }

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        String socialSecurityText = socialSecurityField.getText();


        String userName =userNameField.getText();
        String fName = firstNameField.getText();
        String lName = lastNameField.getText();
        String emailId = emailIdField.getText();
        String phoneNumber = phoneNumberField.getText();
        Long socialSecurity = Long.parseLong(socialSecurityText);
        String password = passwordField.getText();

        //Below commented code is only used to troubleshoot in case of an issue with values.
        //System.out.println(fName+"\n"+lName+"\n"+emailId+"\n"+phoneNumber+"\n"+socialSecurity+"\n"+password);

        SQLCode sql = new SQLCode();
        sql.registerUser(fName, lName, emailId, phoneNumber, socialSecurity, password, userName);

        showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + firstNameField.getText());
    }

    public void backTrack() throws IOException{
        Scene currentScene = backButton.getScene();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent tempRoot = loader.load();
        currentScene.setRoot(tempRoot);
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
