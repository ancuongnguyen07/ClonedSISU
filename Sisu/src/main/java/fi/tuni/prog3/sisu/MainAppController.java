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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fi.tuni.prog3.sisu.system.APIReader;
import fi.tuni.prog3.sisu.system.AnException;
import fi.tuni.prog3.sisu.system.CourseUnit;
import fi.tuni.prog3.sisu.system.DegreeProgram;
import fi.tuni.prog3.sisu.system.SkyNet;
import fi.tuni.prog3.sisu.system.Student;
import fi.tuni.prog3.sisu.system.StudyModule;
import fi.tuni.prog3.sisu.system.SubCompositeRule;
import fi.tuni.prog3.sisu.system.Teacher;
import fi.tuni.prog3.sisu.system.User;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainAppController {
  // ===========================================================================
  // --------------------------- API STUFFS FOR SISU ---------------------------
  // ===========================================================================
  private ArrayList<DegreeProgram> degrees = new ArrayList<DegreeProgram>();
  private ArrayList<StudyModule> modules = new ArrayList<StudyModule>();
  private ArrayList<CourseUnit> courses = new ArrayList<CourseUnit>();

  // ===================================================================================
  // --------------------------- DATA CONTROLLER WITH SKYNET ---------------------------
  // ===================================================================================
  private SkyNet sn;
  private User activeUser;

  private Student activeStudent;
  private DegreeProgram activeStudentDegree;
  private ArrayList<String> activeStudentModuleIDs;
  private ArrayList<String> activeStudentPassedCourseIDs;
  private ArrayList<CourseUnit> activeStudentAllCourses = new ArrayList<CourseUnit>();

  private Teacher activeTeacher;
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
    this.activeStudent = sn.getActiveStudent();
    if (this.activeStudent != null) {
      this.activeStudentDegree = sn.getDegreeByID(this.activeStudent.getDegreeID());
      this.activeStudentPassedCourseIDs = this.activeStudent.getCourseIDs();
      this.activeStudentModuleIDs = this.activeStudent.getModuleIDs();

      sn.loadCompositeRuleRec(this.activeStudentDegree.getCompositeRule());

      showStudyStructure(this.activeStudentDegree);
      updateCredits();
    }

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
  
  // ====================================================================================================
  // --------------------------- API CALLS FOR DISPLAYING THE MODULE CONTENTS ---------------------------
  // ====================================================================================================
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
    degreeButton.setMaxWidth(1000);
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
  

  private void getDegree() throws AnException {

    //----------call API degree list -------------------------------------
    JsonArray degreeArray = api.callAllDegrees();
    // SISU: Take the Bachelor of Science and Engineering degree
    JsonObject degreeOverview = degreeArray.get(8).getAsJsonObject();
    // create the API to call that specific degree
    String currentDegreeAPI = api.getDegreeDetailAPI() + degreeOverview.get("id").getAsString();


    //---------------Call API 1 degree detail --------------------------------
    JsonObject degreeDetail = api.connectAPI(currentDegreeAPI, "id");
    // Make the degree class and add it to the degrees list up in the beginning of this file.
    DegreeProgram degree = api.JsonToDegreeProgram(degreeDetail);
    this.degrees.add(degree);
  }

  // ==============================================================================================
  // --------------------------- DISPLAY STUDY DETAILS FOR ACTIVE USER  ---------------------------
  // ==============================================================================================
  @FXML
  private void showActiveStudentStudies() {
    showStudyStructure(this.activeStudentDegree);
  }

  private void showStudyStructure(DegreeProgram deg) {
    Button degreeButton = new Button(deg.getName());
    degreeButton.getStyleClass().add("module-heading");
    degreeButton.setMaxWidth(1000);
    TreeItem<Button> degreeRoot = new TreeItem<Button>(degreeButton);
    studyStructureTreeView.setRoot(degreeRoot); 
    showCompositeRule(deg.getCompositeRule(), degreeRoot);
  }

  private void showCompositeRule(SubCompositeRule rule, TreeItem parentItem) {
    for (StudyModule sm : rule.getSubModules()) {
      if (this.activeStudentModuleIDs.contains(sm.getId())) {
        showCompositeRule(sm.getCompositeRule(), createStudyPlanHeading(sm.getName(), parentItem));
      }
    }
    
    if (!rule.getSubCourses().isEmpty()) {
      GridPane courseGrid = new GridPane();
      // courseGrid.setMinSize(920, 300);
      courseGrid.setVgap(5);
      courseGrid.setHgap(5);
      for (int i = 0; i < rule.getSubCourses().size(); i++){
        activeStudentAllCourses.add(rule.getSubCourses().get(i));
        addCourseCard(rule.getSubCourses().get(i).getName(), i, courseGrid);
      }
      TreeItem gridItem = new TreeItem(courseGrid);
      parentItem.getChildren().add(gridItem);
    }
    
    for (SubCompositeRule r : rule.getSubComposites()){
      showCompositeRule(r, parentItem);
    }
  }
  private TreeItem createStudyPlanHeading(String name, TreeItem parentItem) {
    Button btn = new Button(name);
    btn.setMaxWidth(1000);
    btn.getStyleClass().add("module-heading");
    TreeItem item = new TreeItem(btn);
    parentItem.getChildren().add(item);
    return item;
  }
  private void addCourseCard(String name, int index, GridPane grid) {
    Button btn = new Button(name);
    btn.setWrapText(true);
    btn.setMinSize(250, 50);
    btn.setMaxSize(250, 50);
    btn.setTextAlignment(TextAlignment.CENTER);
    btn.getStyleClass().add("module-heading");
    grid.add(btn, index % 3, index / 3);
  }

  // ===============================================================================================
  // --------------------------- DISPLAY ALL COURSES OF CURRENT STUDENT  ---------------------------
  // ===============================================================================================
  @FXML private GridPane allCoursesGrid;
  @FXML
  private void showAllCourses() {
    for (int i = 0; i < activeStudentAllCourses.size(); i++) {
      CourseUnit course = activeStudentAllCourses.get(i);
      Button btn = new Button(course.getName());
      btn.setOnAction(event -> {
        Object node = event.getSource();
        Button b = (Button) node;
        displaySelectedCourseInfo(b.getText());
      });
      btn.setWrapText(true);
      btn.setMaxWidth(310);
      btn.setMaxHeight(60);
      btn.setTextAlignment(TextAlignment.CENTER);
      btn.getStyleClass().add("module-heading");
      allCoursesGrid.add(btn, i % 2, i / 2);
    }
  }

  @FXML private Label courseNameLabel;
  @FXML private Label courseCodeLabel;
  @FXML private Label courseStatusLabel;
  @FXML private Label courseGradeLabel;
  @FXML private Label courseCreditLabel;
  private void displaySelectedCourseInfo(String courseName) {
    for (CourseUnit c : activeStudentAllCourses) {
      if (c.getName().equals(courseName)) {
        courseNameLabel.setText(c.getName());
        courseCodeLabel.setText(c.getCourseCode());

        int min = c.getMinCredit();
        int max = c.getMaxCredit();
        System.out.println(c.getAPI());
        System.out.println(min);
        System.out.println(max);
        if (max == -1) {
          courseCreditLabel.setText("Credits: " + min +"cr");
        }
        if (max == min) {
          courseCreditLabel.setText("Credits: " + min +"cr");
        }
        if (max != min && max != -1) {
          courseCreditLabel.setText("Credits: " + min + "-" + max +" cr");
        }
        
        courseStatusLabel.setText("Status: not passed.");
        courseGradeLabel.setText("Grade: 0.");
        if (c.getId().equals(activeStudentPassedCourseIDs)) {
          courseStatusLabel.setText("Status: passed.");
          courseGradeLabel.setText("Grade: Student's grade.");
        }
      }
    }
  }
  
  @FXML private Label completedCreditsLabel;
  @FXML private Label totalCreditsLabel;

  private void updateCredits() {
    int completed = 0;
    int totalMin = 0;
    int totalMax = 0;
    for (CourseUnit c : activeStudentAllCourses) {
      int min = c.getMinCredit();
      int max = c.getMaxCredit();
      totalMin += min;
      if (max != -1) {
        totalMax += max;
      }
      if (activeStudentPassedCourseIDs.contains(c.getId())) {
        completed += min;
      }
    }
    completedCreditsLabel.setText(Integer.toString(completed));
    totalCreditsLabel.setText(Integer.toString(totalMin));
    if (totalMax != totalMin) {
      totalCreditsLabel.setText(Integer.toString(totalMin) + "-" + Integer.toString(totalMax));
    }
  }
}


