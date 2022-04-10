package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void SwitchToMainApp() throws IOException {
        App.setRoot("MainApp");
    }
}