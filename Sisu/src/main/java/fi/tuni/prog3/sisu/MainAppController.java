package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

// import javax.swing.JSpinner.DefaultEditor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fi.tuni.prog3.sisu.system.APIReader;
import fi.tuni.prog3.sisu.system.AnException;
import fi.tuni.prog3.sisu.system.CourseUnit;
import fi.tuni.prog3.sisu.system.DegreeProgram;
import fi.tuni.prog3.sisu.system.SkyNet;
import fi.tuni.prog3.sisu.system.StudyModule;
import fi.tuni.prog3.sisu.system.SubCompositeRule;
import fi.tuni.prog3.sisu.system.User;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;

public class MainAppController {
  // ===========================================================================
  // --------------------------- API STUFFS FOR SISU ---------------------------
  // ===========================================================================
  private ArrayList<DegreeProgram> degrees = new ArrayList<DegreeProgram>();
  private ArrayList<StudyModule> modules = new ArrayList<StudyModule>();
  private ArrayList<CourseUnit> courses = new ArrayList<CourseUnit>();
  private final String degreeListAPI = "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000";
  private final String degreeDetailAPI = "https://sis-tuni.funidata.fi/kori/api/modules/";
  private final String studyModuleAPI = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=";
  private final String courseUnitAPI = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=";
  private final String identifierTUNI = "&universityId=tuni-university-root-id";

  private SkyNet sn;
  private User activeUser;
  private APIReader api;

  @FXML private Label activeUserStatus;
  @FXML private Label activeUserHome;

  // Settings FXML IDs
  @FXML private TextField fullNameSettings;
  @FXML private TextField currentUserNameSettings;

  // IDs to change username/password
  @FXML private TextField newUserNameSettings;
  @FXML private PasswordField confirmNewUsernameSettings;

  @FXML private PasswordField newPasswordSettings;
  @FXML private PasswordField confirmNewPasswordSettings;

  @FXML private Button saveUsernameBtn;
  @FXML private Button cancelUsernameBtn;
  @FXML private Button savePasswordBtn;
  @FXML private Button cancelPasswordBtn;

  @FXML private Label userUpdatedNoti;

  @FXML private TreeView studyStructureTreeView;

  /**
   * Simple constructor for MainAppController class.
   * @param sn reference to the SkyNet object that is used for this login instance
   */
  public MainAppController(SkyNet sn) {
    this.sn = sn;
    this.api = new APIReader();
  }
  // =======================================================================================
  // --------------------------- UPDATE GUI BASED ON ACTIVE USER ---------------------------
  // =======================================================================================
  /**
   * Update the information in the GUI based on the information of the current logged-in user
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
  private void SwitchToLogin() {
      try {
        App.setRoot("Login.fxml", "login");
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
  // ===========================================================================================
  // --------------------------- UPDATE USER CREDENTIALS IN SETTINGS ---------------------------
  // ===========================================================================================
  @FXML
  private void SaveNewUsername() {
    String newUserName = newUserNameSettings.getText();
    String confirmPwd = confirmNewUsernameSettings.getText();

    if (newUserName.equals("")) {
      userUpdatedNoti.setText("Please enter a new username!");
      return;
    }
    if (confirmPwd.equals("")) {
      userUpdatedNoti.setText("Please confirm change by entering your password!");
      return;
    }
    if (sn.validatePassword(activeUser.getUsername(), confirmPwd)) {
      this.activeUser.setUsername(newUserName);
      sn.saveUsers("src/main/resources/jsons/users.json");
      userUpdatedNoti.setText("Update new username successfully!");

      newUserNameSettings.setText("");
      confirmNewUsernameSettings.setText("");
    } else {
      userUpdatedNoti.setText("Password do not match! Please try again");
      newUserNameSettings.setText("");
    }
  }

  @FXML 
  private void SaveNewPassword() {
    String newPwd = newPasswordSettings.getText();
    String confirmNewPwd = confirmNewPasswordSettings.getText();

    if (newPwd.equals("")) {
      userUpdatedNoti.setText("Please enter a new password!");
      return;
    }
    if (confirmNewPwd.equals("")) {
      userUpdatedNoti.setText("Please confirm your new password!");
      return;
    }
    if (newPwd.equals(confirmNewPwd)) {
      // save the hashed password instead of the original password
      this.activeUser.setPassword(sn.hashPassword(newPwd, this.activeUser.getSalt()));
      sn.saveUsers("src/main/resources/jsons/users.json");
      userUpdatedNoti.setText("Update new password successfully!");

      newPasswordSettings.setText("");
      confirmNewPasswordSettings.setText("");
    } else {
      userUpdatedNoti.setText("Password do not match! Please try again");
      confirmNewPasswordSettings.setText("");
      newPasswordSettings.setText("");
    }
  }

  @FXML 
  private void cancelUsernameChange() {
    newUserNameSettings.setText("");
    userUpdatedNoti.setText("");
    confirmNewUsernameSettings.setText("");
  }

  @FXML
  private void cancelPasswordChange() {
    newPasswordSettings.setText("");
    confirmNewPasswordSettings.setText("");
    userUpdatedNoti.setText("");
  }
  
  private void updateStudyStructureDisplay(String moduleName, TreeItem parentItem, String moduleType) throws AnException {
    if (moduleType.equals("DegreeProgram") && parentItem.getChildren().size() == 0) {
      // If the parsed module is a degree program
      for (DegreeProgram degree : this.degrees) {
        if (degree.getName().equals(moduleName)) {
          for (StudyModule module : degree.getCompositeRule().getSubModules()) {
            this.modules.add(module);

            Button btn = new Button(module.getName());
            btn.getStyleClass().add("module-heading");
            TreeItem item = new TreeItem(btn);
            parentItem.getChildren().add(item);

            btn.setOnAction(event -> {
              
              Object node = event.getSource();
              Button b = (Button)node;
              System.out.println(b.getText());
              System.out.println(module.getClass().getSimpleName());
              try {
                updateStudyStructureDisplay(b.getText(), item, module.getClass().getSimpleName());
              } catch (AnException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            });
          }
        }
      }
    }
    
    if (moduleType.equals("StudyModule") && parentItem.getChildren().size() == 0) {
      // If the parsed module is a study module
      for (StudyModule module : this.modules) {
        if (module.getName().equals(moduleName)) {
          api.onClickStudyModule(module);
          ArrayList<StudyModule> subModules = module.getCompositeRule().getSubModules();
          ArrayList<CourseUnit> subCourses = module.getCompositeRule().getSubCourses();

          for (StudyModule m : subModules) {
            this.modules.add(m);

            Button btn = new Button(m.getName());
            btn.getStyleClass().add("module-heading");
            TreeItem item = new TreeItem(btn);
            parentItem.getChildren().add(item);

            btn.setOnAction(event -> {
              
              Object node = event.getSource();
              Button b = (Button)node;
              System.out.println(b.getText());
              System.out.println(m.getClass().getSimpleName());
              try {
                updateStudyStructureDisplay(b.getText(), item, m.getClass().getSimpleName());
              } catch (AnException e) {
                e.printStackTrace();
              }
            });
          }
          for (CourseUnit course : subCourses) {
            this.courses.add(course);

            Button btn = new Button(course.getName());
            btn.getStyleClass().add("module-heading");
            TreeItem item = new TreeItem(btn);
            parentItem.getChildren().add(item);

            btn.setOnAction(event -> {
              
              Object node = event.getSource();
              Button b = (Button)node;
              System.out.println(b.getText());
              System.out.println(course.getClass().getSimpleName());
              try {
                updateStudyStructureDisplay(b.getText(), item, course.getClass().getSimpleName());
              } catch (AnException e) {
                e.printStackTrace();
              }
            });
          }
          System.out.println("Sub module(s) of " + moduleName + " are: " + subModules);
          System.out.println("Sub course(s) of " + moduleName + " are: " + subCourses);
        }
      }
    }
  }

  @FXML 
  private void callAPI() throws AnException {
    getDegree();

    // Degree: Bachelor program in Sci&En
    DegreeProgram degree = degrees.get(0);
    Button degreeButton = new Button(degree.getName());
    degreeButton.getStyleClass().add("module-heading");
    TreeItem<Button> degreeRoot = new TreeItem<Button>(degreeButton);
    degreeButton.setOnAction(event -> {
      Object node = event.getSource();
      Button b = (Button)node;
      try {
        updateStudyStructureDisplay(b.getText(), degreeRoot, degree.getClass().getSimpleName());
      } catch (AnException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    });
    studyStructureTreeView.setRoot(degreeRoot);    

    

  }
  
  // ====================================================================================================
  // --------------------------- API CALLS FOR DISPLAYING THE MODULE CONTENTS ---------------------------
  // ====================================================================================================
  private void getDegree() throws AnException {

    //----------call API degree list -------------------------------------
    JsonArray degreeArray = api.callAllDegrees();
    // SISU: Take the Bachelor of Science and Engineering degree
    JsonObject degreeOverview = degreeArray.get(8).getAsJsonObject();
    // create the API to call that specific degree
    String currentDegreeAPI = degreeDetailAPI + degreeOverview.get("id").getAsString();

    // -------------- and added to degrees array -----------------
    

    //System.out.println(degrees.size());
    //---------------Call API 1 degree detail --------------------------------
    JsonObject degreeDetail = api.connectAPI(currentDegreeAPI, "id");
    // Make the degree class and add it to the degrees list up in the beginning of this file.
    DegreeProgram degree = api.JsonToDegreeProgram(degreeDetail);
    this.degrees.add(degree);

    // Name of the degree
    // String degreeName = degree.getName();
    // // Composite Rule for Sub Modules of the degree
    // SubCompositeRule degreeSubModule = degree.getCompositeRule();
    // // System.out.println(degree.getCompositeRule().getSubModules());

    // JsonArray degreeRules = api.takeRules(degreeDetail.get("rule").getAsJsonObject());

    // // ---------------- take submodules/courses from degree detail----------------

    // // call out 1 module (usually suppose to be only 1) ----------------------------
    // // SISU: Get Natural Sciences and Mathematics path
    // JsonObject studyModuleOverview = degreeRules.get(0).getAsJsonObject();
    // String currentStudyModuleAPI = studyModuleAPI + studyModuleOverview.get("moduleGroupId").getAsString() + identifierTUNI;
    // // contain: submodule or course
    // JsonObject studyModuleDetailJson = api.connectAPI(currentStudyModuleAPI, "groupId");
    // StudyModule firStudyModule = api.JsonToStudyModule(studyModuleDetailJson);
    // // User click on the studyModule to see more details
    // api.onClickStudyModule(studyModuleDetailJson, firStudyModule);
    // // Look at the Basic Studies in Natural Sciences in Natural Sciences and Mathematics
    // String basic_studies_api = firStudyModule.getCompositeRule().getSubModules().get(1).getAPI();
    // JsonObject basic_studies_obj = api.connectAPI(basic_studies_api, "groupId");
    // StudyModule basicStudiesModule = firStudyModule.getCompositeRule().getSubModules().get(1);
    // // When user click on it
    // api.onClickStudyModule(basic_studies_obj, basicStudiesModule);
    // ArrayList<CourseUnit> basicStudiesCourses = basicStudiesModule.getCompositeRule().getSubCourses();
    // for (int i = 0; i < basicStudiesCourses.size(); i++) {
    //   // System.out.println(basicStudiesCourses.get(i).getCourseCode());
    //   // System.out.println(basicStudiesCourses.get(i).getName());
    // }
  }


}
