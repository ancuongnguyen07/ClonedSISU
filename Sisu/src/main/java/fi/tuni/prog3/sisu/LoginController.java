/**
 * Handling controllers, GUI
 */
package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import fi.tuni.prog3.sisu.system.*;

/**
 * Controller for the login scene of the app.
 */
public class LoginController {
    private SkyNet sn;

    /**
     * Simple constructor that parses in the SkyNet to handle the data of this instance.
     * @param sn the SkyNet object that will handle the data, including user login in this scene.
     */
    public LoginController(SkyNet sn) {
        this.sn = sn;
    }
    
    @FXML TextField userNameField;
    @FXML PasswordField passwordField;
    @FXML Label alertBox;

    @FXML
    private void Login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        // User validation
        if (sn.validatePassword(username, password)) {
            App.setRoot("MainApp.fxml", "main");
            App.UpdateMainApp();
        } else {
            alertBox.setText("Wrong username/password!");
        }
    }
}