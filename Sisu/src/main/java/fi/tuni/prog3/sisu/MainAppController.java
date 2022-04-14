package fi.tuni.prog3.sisu;

import java.io.IOException;

import fi.tuni.prog3.sisu.system.SkyNet;
import javafx.fxml.FXML;

public class MainAppController {
  private SkyNet sn;
  public MainAppController(SkyNet sn) {
    this.sn = sn;
  }

  @FXML
  private void GetInfo() {
    System.out.println(sn.getStudents());
  }

  @FXML
  private void SwitchToLogin() throws IOException {
      App.setRoot("Login.fxml", "login");
  }
}
