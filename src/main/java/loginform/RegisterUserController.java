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
    //FXML elements are loaded into the class
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
    //This handles the information, errorhandling and alike in the registration form.
    public void register(ActionEvent event) throws SQLException, IOException
    {

        Window owner = submitButton.getScene().getWindow();

        //Handles the firstname field
        if (firstNameField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }

        //Handles the lastname field
        if (lastNameField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please enter your name");
        }

        //Handles the email field
        if (emailIdField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email");
            return;
        }

        //Phone number field handling
        if (phoneNumberField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a phone number");
            return;
        }

        //SocialSecurity handling
        if (socialSecurityField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your social security number");
            return;
        }

        //password field handling
        if (passwordField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        //username field handling
        if (userNameField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a username");
            return;
        }

        //Once all fields have been filled the following code is ran, collecting the
        //Information from all fields and placing them inside of variables.
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

        //A new SQLCode is created and then registerUser is done with the variables from
        //the above code ran as arguments for the method.
        SQLCode sql = new SQLCode();
        sql.registerUser(fName, lName, emailId, phoneNumber, socialSecurity, password, userName);

        //Once the process is successful a message is displayed to the user to inform them
        //That they are now logged in
        showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + firstNameField.getText());
        backTrack();
    }

    //Here below you will find the button in the Register site that takes you "Backwards" or back to the login screen should you
    //Have clicked wrong earlier. It identifies the button used and then gets the scene before overlapping it with the login page again.
    public void backTrack() throws IOException{
        Scene currentScene = backButton.getScene();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent tempRoot = loader.load();
        currentScene.setRoot(tempRoot);
    }

    //This method will handle all the alerst used to push down on repeat code in our class.
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
