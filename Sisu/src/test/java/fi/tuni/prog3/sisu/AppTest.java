package fi.tuni.prog3.sisu;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

/**
 *
 * @author Khoa Nguyen
 */
public class AppTest extends ApplicationTest {

    /**
     *
     * @param stage
     * @throws IOException
     */
    @Override
  public void start(Stage stage) throws IOException {
    new App().start(stage);
  }

    /**
     *
     * @throws IOException
     */
    @Test
  public void appUseTest() throws IOException {
    System.out.println("================================================================");
    System.out.println("Attempt to login with wrong credentials...");
    Label alert = lookup("#alertBox").query();
    clickOn("#userNameField").write("an");
    clickOn("#passwordField").write("1");
    clickOn("#loginBtn");
    Assertions.assertThat(alert).isVisible();
    Assertions.assertThat(alert.getText()).isEqualTo("Wrong username/password!");

    System.out.println("Attempt to login with Student credentials of An Nguyen...");
    doubleClickOn("#userNameField").write("an");
    doubleClickOn("#passwordField").write("an");
    System.out.println("Logging in... Please wait for up to a minute...");
    clickOn("#loginBtn");

    Label userStatus = lookup("#activeUserStatus").query();
    Button degreeRoot = lookup("#BachelorsProgrammeinScienceandEngineering").query();
    Assertions.assertThat(userStatus).isVisible();
    Assertions.assertThat(userStatus.getText()).isEqualTo("An Nguyen - student");
    Assertions.assertThat(degreeRoot).isVisible();
    Assertions.assertThat(degreeRoot.getText()).isEqualTo("Bachelor's Programme in Science and Engineering");
    System.out.println("Login successfully! Will now test Courses tab...");

    clickOn("#coursesTab");
    Label completedCredits = lookup("#completedCreditsLabel").query();
    Label totalCredits = lookup("#totalCreditsLabel").query();
    Label averageGrade = lookup("#studentAverageGradeLabel").query();
    
    Assertions.assertThat(completedCredits).isVisible();
    Assertions.assertThat(completedCredits.getText()).isEqualTo("64");
    Assertions.assertThat(totalCredits).isVisible();
    Assertions.assertThat(totalCredits.getText()).isEqualTo("246-248");
    Assertions.assertThat(averageGrade).isVisible();
    Assertions.assertThat(averageGrade.getText()).isEqualTo("4.07");
    System.out.println("Credits information in courses tab is correct!");

    System.out.println("Attempt to view all courses of this student.");
    clickOn("#showAllCoursesBtn");
    clickOn("#CareerPlanning");
    Label courseName = lookup("#courseNameLabel").query();
    Label courseCode = lookup("#courseCodeLabel").query();
    Label courseCredits = lookup("#courseCreditLabel").query();
    Label courseStatus = lookup("#courseStatusLabel").query();
    Label courseGrade = lookup("#courseGradeLabel").query();

    Assertions.assertThat(courseName).isVisible();
    Assertions.assertThat(courseName.getText()).isEqualTo("Career Planning");
    Assertions.assertThat(courseCode).isVisible();
    Assertions.assertThat(courseCode.getText()).isEqualTo("ENS.320");
    Assertions.assertThat(courseCredits).isVisible();
    Assertions.assertThat(courseCredits.getText()).isEqualTo("Credits: 1cr");
    Assertions.assertThat(courseStatus).isVisible();
    Assertions.assertThat(courseStatus.getText()).isEqualTo("Status: passed.");
    Assertions.assertThat(courseGrade).isVisible();
    Assertions.assertThat(courseGrade.getText()).isEqualTo("Grade: 4");
    System.out.println("Correct data for course! Move on to settings");

    clickOn("#settingsTab");
    TextField fullNameSettings = lookup("#fullNameSettings").query();
    TextField usernameSettings = lookup("#currentUserNameSettings").query();
    Assertions.assertThat(fullNameSettings).isVisible();
    Assertions.assertThat(fullNameSettings.getText()).isEqualTo("An Nguyen");
    Assertions.assertThat(usernameSettings).isVisible();
    Assertions.assertThat(usernameSettings.getText()).isEqualTo("an");

    Label noti = lookup("#userUpdatedNoti").query();
    
    // Save new username without enter anything
    clickOn("#saveUsernameBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Please enter a new username!");

    // Enter new username but not confirm with password
    clickOn("#newUserNameSettings").write("a");
    clickOn("#saveUsernameBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Please confirm change by entering your password!");

    // Save new password without enter anything
    clickOn("#savePasswordBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Please enter a new password!");

    // Enter new password but not confirm it
    clickOn("#newPasswordSettings").write("a");
    clickOn("#savePasswordBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Please confirm your new password!");

    // Cancel username change
    TextField newUsername = lookup("#newUserNameSettings").query();
    PasswordField confirmNewUsername = lookup("#confirmNewUsernameSettings").query();
    clickOn("#cancelUsernameBtn");
    Assertions.assertThat(newUsername.getText()).isEqualTo("");
    Assertions.assertThat(confirmNewUsername.getText()).isEqualTo("");

    // Cancel password change
    PasswordField newPassword = lookup("#newPasswordSettings").query();
    PasswordField confirmNewPassword = lookup("#confirmNewPasswordSettings").query();
    clickOn("#cancelPasswordBtn");
    Assertions.assertThat(newPassword.getText()).isEqualTo("");
    Assertions.assertThat(confirmNewPassword.getText()).isEqualTo("");

    // Change new username, but wrong password
    doubleClickOn("#newUserNameSettings").write("a");
    doubleClickOn("#confirmNewUsernameSettings").write("abc");
    clickOn("#saveUsernameBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Password do not match! Please try again");

    // Change new username
    doubleClickOn("#newUserNameSettings").write("a");
    doubleClickOn("#confirmNewUsernameSettings").write("an");
    clickOn("#saveUsernameBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Update new username successfully!");

    // Change new password, but they don't match
    doubleClickOn("#newPasswordSettings").write("abc");
    doubleClickOn("#confirmNewPasswordSettings").write("an");
    clickOn("#savePasswordBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Password do not match! Please try again");

    // Change new password
    doubleClickOn("#newPasswordSettings").write("abc");
    doubleClickOn("#confirmNewPasswordSettings").write("abc");
    clickOn("#savePasswordBtn");
    Assertions.assertThat(noti).isVisible();
    Assertions.assertThat(noti.getText()).isEqualTo("Update new password successfully!");

    System.out.println("Update user information passed! Move on to view all degrees");

    clickOn("#degreesTab");
    clickOn("#showAllDegreesBtn");
    System.out.println("Clicked on all degree... Please wait for the program to fetch the API data...");
    TreeView allDegree = lookup("#degreeSelectionTreeView").query();
    // When the all degrees button is pressed, show more that 260 degrees (should be 269)
    Assertions.assertThat(allDegree.getRoot().getChildren().size()).isGreaterThan(260);

    System.out.println("Move on to help!");

    clickOn("#helpTab");

    System.out.println("Attemp to logout...");
    clickOn("#logOutBtn");
    
    clickOn("#loginBtn");
    Assertions.assertThat(alert).isVisible();
    Assertions.assertThat(alert.getText()).isEqualTo("Wrong username/password!");

    System.out.println("Finished testing the GUI! Everything works!");
    System.out.println("================================================================");
  }
}
