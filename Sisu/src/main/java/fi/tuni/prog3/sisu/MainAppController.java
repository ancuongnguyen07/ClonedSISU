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
  @FXML TextField newUserNameSettings;
  @FXML PasswordField newPasswordSettings;
  @FXML TextField confirmNewPasswordSettings;
  @FXML Button saveBtnSettings;
  @FXML Button cancelBtnSettings;
  @FXML Label userUpdatedNoti;

  /**
   * Simple constructor for MainAppController class.
   * @param sn reference to the SkyNet object that is used for this login instance
   */
  public MainAppController(SkyNet sn) {
    this.sn = sn;
  }

  /**
   *  Update the active user that just logged into the program. Will change some GUI displayed elements
   * accordingly to the active user's information.
   */  
  public void updateActiveUser() {
    // Get current active user
    this.activeUser = sn.getActiveUser();

    // Get role and full name of the active user
    String fullName = activeUser.getFullName();
    String role = activeUser.getRole();
    String displayString = fullName + " - " + role;
    // displayString = WordUtils.capitalize(displayString);

    // Update in homepage
    activeUserStatus.setText(displayString);
    activeUserHome.setText(displayString);

    // Update in settings
    fullNameSettings.setText(activeUser.getFullName());
    currentUserNameSettings.setText(activeUser.getUsername());

    // Disable editability in some settings field
    fullNameSettings.setEditable(false);
    currentUserNameSettings.setEditable(false);
  }

  @FXML
  private void GetInfo() {
    System.out.println("Get students: " + sn.getStudents());
    System.out.println("Get teachers: " + sn.getTeachers());
    System.out.println("Get modules: " + sn.getModules());
    System.out.println("Active user: " + sn.getActiveUser());
  }

  /**
   * Log out 
   * @throws IOException
   */
  @FXML
  private void SwitchToLogin() throws IOException {
      App.setRoot("Login.fxml", "login");
  }

  /**
   * Update user info, including username and password
   */
  @FXML
  private void SaveNewUserInfo() {
    String newUserName = newUserNameSettings.getText();
    String newPwd = newPasswordSettings.getText();
    String confirmPwd = confirmNewPasswordSettings.getText();

    if (newUserName.equals("")) {
      userUpdatedNoti.setText("Please enter a new username!");
      return;
    }
    if (newPwd.equals("")) {
      userUpdatedNoti.setText("Please enter a new password!");
      return;
    }
    if (confirmPwd.equals("")) {
      userUpdatedNoti.setText("Please confirm your new password!");
      return;
    }
    if (newPwd.equals(confirmPwd)) {
      this.activeUser.setUsername(newUserName);
      this.activeUser.setPassword(newPwd);
      sn.saveUsers("src/main/resources/jsons/users.json");
      userUpdatedNoti.setText("Update user credentials successfully!");

      newUserNameSettings.setText("");
      newPasswordSettings.setText("");
      confirmNewPasswordSettings.setText("");
    } else {
      userUpdatedNoti.setText("Passwords do not match!");
    }
  }

  /**
   * Cancel update user info
   */
  @FXML 
  private void cancelChangeUserInfo() {
    newUserNameSettings.setText("");
    newPasswordSettings.setText("");
    confirmNewPasswordSettings.setText("");
    userUpdatedNoti.setText("");
  }
}
