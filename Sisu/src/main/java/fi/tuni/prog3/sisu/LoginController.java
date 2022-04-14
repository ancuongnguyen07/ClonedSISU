package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import fi.tuni.prog3.sisu.system.*;

public class LoginController {
    SkyNet sn = new SkyNet("src/main/resources/jsons/users.json");

    @FXML TextField userNameField;
    @FXML PasswordField passwordField;
    @FXML Label alertBox;

    @FXML
    private void Login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();

        // User validation
        if (sn.validatePassword(username, password)) {
            App.setRoot("MainApp");
        } else {
            alertBox.setText("Wrong username/password!");
        }
    }
}