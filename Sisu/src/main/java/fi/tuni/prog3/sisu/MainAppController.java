package fi.tuni.prog3.sisu;

import java.io.IOException;

import fi.tuni.prog3.sisu.system.SkyNet;
import fi.tuni.prog3.sisu.system.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainAppController {
  private SkyNet sn;
  private User activeUser;

  @FXML Label activeUserStatus;
  @FXML Label activeUserHome;

  // Settings FXML IDs
  @FXML TextField fullNameSettings;
  @FXML TextField currentUserNameSettings;
  @FXML PasswordField currentPasswordSettings;
  @FXML CheckBox showPasswordToggleSettings;
  @FXML TextField newUserNameSettings;
  @FXML PasswordField newPasswordSettings;
  @FXML TextField confirmNewPasswordSettings;
  @FXML Button saveBtnSettings;
  @FXML Button cancelBtnSettings;
  @FXML Label userUpdatedNoti;

  public MainAppController(SkyNet sn) {
    this.sn = sn;

    
  }
  
  public void updateActiveUser() {
    // Get current active user
    this.activeUser = sn.getActiveUser();

    // Update in homepage
    activeUserStatus.setText(activeUser.getFullName());
    activeUserHome.setText(activeUser.getFullName());

    // Update in settings
    fullNameSettings.setText(activeUser.getFullName());
    currentUserNameSettings.setText(activeUser.getUsername());
    currentPasswordSettings.setText(activeUser.getPassword());

    // Disable editability in some settings field
    fullNameSettings.setEditable(false);
    currentUserNameSettings.setEditable(false);
    currentPasswordSettings.setEditable(false);
  }

  @FXML
  private void GetInfo() {
    System.out.println("Get students: " + sn.getStudents());
    System.out.println("Get teachers: " + sn.getTeachers());
    System.out.println("Get modules: " + sn.getModules());
    System.out.println("Active user: " + sn.getActiveUser());
  }

  @FXML
  private void SwitchToLogin() throws IOException {
      App.setRoot("Login.fxml", "login");
  }

  @FXML
  private void SaveNewUserInfo() {
    String newUserName = newUserNameSettings.getText();
    String newPwd = newPasswordSettings.getText();
    String confirmPwd = confirmNewPasswordSettings.getText();

    if (!newPwd.equals("") && newPwd.equals(confirmPwd)) {
      this.activeUser.setUsername(newUserName);
      this.activeUser.setPassword(newPwd);
      sn.saveUsers("src/main/resources/jsons/users.json");
      userUpdatedNoti.setText("Update user credentials successfully!");
    } else {
      userUpdatedNoti.setText("Passwords do not match!");
    }
  }
}
