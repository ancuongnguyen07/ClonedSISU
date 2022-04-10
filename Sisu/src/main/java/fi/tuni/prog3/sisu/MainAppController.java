package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.fxml.FXML;

public class MainAppController {

  @FXML
  private void SwitchToLogin() throws IOException {
      App.setRoot("Login");
  }
}
