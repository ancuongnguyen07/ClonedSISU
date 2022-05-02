package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * The controller for the MainApp window of the program. This class handles most of the GUI functionalities
 * happening on the program. 
 * 
 * @author Khoa Nguyen
 */
public class MainAppController {
  // ===========================================================================
  // --------------------------- API STUFFS FOR SISU ---------------------------
  // ===========================================================================
  private ArrayList<String> allDegreeName = new ArrayList<String>();
  private ArrayList<StudyModule> modules = new ArrayList<StudyModule>();
  private ArrayList<CourseUnit> courses = new ArrayList<CourseUnit>();


  private SkyNet sn;
  private User activeUser;

  private Student activeStudent;
  private DegreeProgram activeStudentDegree;
  private ArrayList<String> activeStudentModuleIDs;
  private HashMap<String, Integer> activeStudentPassedCourses;
  private ArrayList<CourseUnit> activeStudentAllCourses = new ArrayList<CourseUnit>();

  // ==================================================================
  // --------------------------- GUI STUFFS ---------------------------
  // ==================================================================
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
   * Simple constructor for MainAppController class. Parses in a SkyNet object to handle data of this use instance.
   * Also creates a new APIReader object to fetch data from the API so they can be displayed on screen.
   * @param sn reference to the SkyNet object that is used for this login instance
   */
  public MainAppController(SkyNet sn) {
    this.sn = sn;
    this.api = new APIReader();
  }
  
  /**
   * Switch to the login scene
   */
  @FXML
  private void SwitchToLogin() {
      try {
        App.setRoot("Login.fxml", "login");
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
  // =======================================================================================
  // --------------------------- UPDATE GUI BASED ON ACTIVE USER ---------------------------
  // =======================================================================================

  /**
   * Update the information in the GUI based on the information of the current logged-in user.
   * This includes all user-related data, such as full name, role, average grade, courses, degree programs,
   * total credits, credits passed, etc.
   */
  public void updateActiveUser() {
    // Get current active user
    this.activeUser = sn.getActiveUser();
    this.activeStudent = sn.getActiveStudent();
    if (this.activeStudent != null) {
      this.activeStudentDegree = sn.getDegreeByID(this.activeStudent.getDegreeID());
      this.activeStudentPassedCourses = this.activeStudent.getCourses();
      this.activeStudentModuleIDs = this.activeStudent.getModuleIDs();

      sn.loadCompositeRuleRec(this.activeStudentDegree.getCompositeRule());

      showStudyStructure(this.activeStudentDegree, studyStructureTreeView, true);
      updateCredits();
    }
    // Get role and full name of the active user
    String fullName = activeUser.getFullName();
    String role = activeUser.getRole();
    String displayString = fullName + " - " + role;

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

  // ===========================================================================================
  // =-------------------------- UPDATE USER CREDENTIALS IN SETTINGS --------------------------=
  // =-----------------------------------------------------------------------------------------=
  // =- This part of the controller handle the Update User Information tab on the GUI. This ---=
  // =- includes update the username and passwords of the active user, checkers, and cancel ---=
  // =- those changes. ------------------------------------------------------------------------=
  // ===========================================================================================
  /**
   * Check, and save new username when user update their username in settings
   */
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

  /**
   * Check, and save new password when user update their password in settings
   */
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

  /**
   * Cancel username change
   */
  @FXML 
  private void cancelUsernameChange() {
    newUserNameSettings.setText("");
    userUpdatedNoti.setText("");
    confirmNewUsernameSettings.setText("");
  }

  /**
   * Cancel password change
   */
  @FXML
  private void cancelPasswordChange() {
    newPasswordSettings.setText("");
    confirmNewPasswordSettings.setText("");
    userUpdatedNoti.setText("");
  }
  
  // ===========================================================================================
  // =-------------------------- API CALLS FOR DISPLAYING ALL DEGREES -------------------------=
  // =-----------------------------------------------------------------------------------------=
  // =- This part of the controller handle the All Degrees tab of the GUI. This includes ------=
  // =- fetching all 269 degrees from the API, and when the user click on one degree, fetches -=
  // =- and display the data from that degree on the screen. ----------------------------------=
  // ===========================================================================================
  @FXML private TreeView degreeSelectionTreeView;
  @FXML private TreeView degreeOverviewTreeView;
  /**
   * Get all 269 degree programs available from the Sisu API, and display them on screen
   */
  @FXML
  private void getAllDegreeProgram() {
    TreeItem root = new TreeItem();
    degreeSelectionTreeView.setRoot(root);

    JsonArray degreeArray = this.api.callAllDegrees();
    for (int i = 0; i < degreeArray.size(); i++) {
      JsonObject degreeOverview = degreeArray.get(i).getAsJsonObject();
      String degreeName = degreeOverview.get("name").getAsString();
      this.allDegreeName.add(degreeName);
      if (degreeName != null) {
        createDegreeHeading(degreeName, root);
      }
    }
    degreeSelectionTreeView.setShowRoot(false);
  }

  /**
   * Create the heading buttons for a degree program
   * @param name Name of the degree
   * @param parent The parent to put the newly created heading in
   * @return The created heading
   */
  private TreeItem createDegreeHeading(String name, TreeItem parent) {
    Button btn = new Button(name);
    btn.setOnAction(event -> {
      Object node = event.getSource();
      Button b = (Button) node;
      showDegreeData(b.getText());
    });
    btn.setMaxWidth(200);
    btn.setWrapText(true);
    btn.setId(name.replace(" ", "").replace("'", ""));
    btn.getStyleClass().add("module-heading");
    TreeItem item = new TreeItem(btn);
    parent.getChildren().add(item);
    return item;
  }

  /**
   * Showing the data of the given degree, including sub modules and courses
   * @param degreeName Name of the degree to show
   */
  private void showDegreeData(String degreeName) {
    JsonArray degreeArray = this.api.callAllDegrees();
    DegreeProgram dp;
    for (int i = 0; i < degreeArray.size(); i++) {
      JsonObject degreeOverview = degreeArray.get(i).getAsJsonObject();
      String name = degreeOverview.get("name").getAsString();
      if (degreeName.equals(name)) {
        dp = sn.getDegreeByID(degreeOverview.get("id").getAsString());
        sn.loadCompositeRuleRec(dp.getCompositeRule());
        showStudyStructure(dp, degreeOverviewTreeView, false);
        break;
      }
    }
  }

  /**
   * Show composite rule for everything under that rule, regardless if those are included
   * in the study structure of the active user or not
   * @param rule The sub composite rule to show
   * @param parentItem The parent to display the data in
   */
  private void showCompositeRuleForAll(SubCompositeRule rule, TreeItem parentItem) {
    for (StudyModule sm : rule.getSubModules()) {
      showCompositeRuleForAll(sm.getCompositeRule(), createStudyPlanHeading(sm.getName(), parentItem));
    }
    
    if (!rule.getSubCourses().isEmpty()) {
      GridPane courseGrid = new GridPane();
      // courseGrid.setMinSize(920, 300);
      courseGrid.setVgap(5);
      courseGrid.setHgap(5);
      for (int i = 0; i < rule.getSubCourses().size(); i++){
        // System.out.println(rule.getSubCourses().get(i).getName());
        addCourseCard(rule.getSubCourses().get(i), i, courseGrid);
      }
      TreeItem gridItem = new TreeItem(courseGrid);
      parentItem.getChildren().add(gridItem);
    }
    
    for (SubCompositeRule r : rule.getSubComposites()){
      showCompositeRule(r, parentItem);
    }
  }

  // ==============================================================================================
  // =-------------------------- DISPLAY STUDY DETAILS FOR ACTIVE USER  --------------------------=
  // =--------------------------------------------------------------------------------------------=
  // =- This part of the controller handle the study degree view in the Homepage tab of the GUI. -=
  // =- This includes fetching all the data from the degree specified in the user's JSON file, ---=
  // =- match the data with the study modules of the user, and display those modules and their ---=
  // =- course units only. Everything is showed in a TreeView in a nice format. ------------------=
  // ==============================================================================================
  /**
   * Show the study structure of the current active student
   */
  @FXML
  private void showActiveStudentStudies() {
    showStudyStructure(this.activeStudentDegree, studyStructureTreeView, true);
  }

  /**
   * Show the study structure of the given degree program. Can choose to match it with the active user 
   * or not e.g., only show submodules that exist in the student's study plan.
   * @param deg The degree program to show
   * @param treeView  The parent to put the data in
   * @param matchWithUser Match data with the user's structure or not
   */
  private void showStudyStructure(DegreeProgram deg, TreeView treeView, Boolean matchWithUser) {
    Button degreeButton = new Button(deg.getName());
    degreeButton.getStyleClass().add("module-heading");
    degreeButton.setMaxWidth(1000);
    degreeButton.setId(deg.getName().replace(" ", "").replace("'", ""));
    TreeItem<Button> degreeRoot = new TreeItem<Button>(degreeButton);
    treeView.setRoot(degreeRoot); 
    if (matchWithUser) {
      showCompositeRule(deg.getCompositeRule(), degreeRoot);
    } else {
      showCompositeRuleForAll(deg.getCompositeRule(), degreeRoot);
    }
  }

  /**
   * Show data from a composite rule i.e., show the sub courses, sub modules, and sub
   * composite rules in the given rule.
   * @param rule The rule to show
   * @param parentItem The parent TreeItem to put the data in
   */
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
        // System.out.println(rule.getSubCourses().get(i).getName());
        addCourseCard(rule.getSubCourses().get(i), i, courseGrid);
      }
      TreeItem gridItem = new TreeItem(courseGrid);
      parentItem.getChildren().add(gridItem);
    }
    
    for (SubCompositeRule r : rule.getSubComposites()){
      showCompositeRule(r, parentItem);
    }
  }
  
  /**
   * Create a heading for the given study plan
   * @param name Name of the study plan
   * @param parentItem The parent TreeItem to put the created heading in
   * @return The created TreeItem heading
   */
  private TreeItem createStudyPlanHeading(String name, TreeItem parentItem) {
    Button btn = new Button(name);
    btn.setMaxWidth(1000);
    btn.getStyleClass().add("module-heading");
    btn.setId(name.replace(" ", "").replace("'", ""));
    TreeItem item = new TreeItem(btn);
    parentItem.getChildren().add(item);
    return item;
  }
  
  /**
   * Create a course card for the given course
   * @param course The course to create the course card for
   * @param index The index of that course card in its container i.e., a grid
   * @param grid The GridPane to put the created course card in
   */
  private void addCourseCard(CourseUnit course, int index, GridPane grid) {
    Button btn;
    btn = new Button(course.getName());
    btn.setId(course.getName().replace(" ", "").replace("'", ""));
    btn.setWrapText(true);
    btn.setMinSize(250, 50);
    btn.setMaxSize(250, 50);
    btn.setTextAlignment(TextAlignment.CENTER);
    if (activeStudentPassedCourses.keySet().contains(course.getGroupID())) {
      btn.getStyleClass().add("passed-course");
    } else {
      btn.getStyleClass().add("module-heading");
    }
    grid.add(btn, index % 3, index / 3);
  }

  // ===============================================================================================
  // =-------------------------- DISPLAY ALL COURSES OF CURRENT STUDENT  --------------------------=
  // =---------------------------------------------------------------------------------------------=
  // =- This part of the controller handles the Courses tab in the GUI. It updates the progress of =
  // =- the student: showing average grade, completed credits, and total credits planned. It also -=
  // =- shows all of the student's courses, and can display information on those courses when -----=
  // =- any of the courses are clicked on. --------------------------------------------------------=
  // ===============================================================================================
  @FXML private GridPane allCoursesGrid;

  /**
   * Show all courses in the study structure of the active user
   */
  @FXML
  private void showAllCourses() {
    for (int i = 0; i < activeStudentAllCourses.size(); i++) {
      CourseUnit course = activeStudentAllCourses.get(i);
      Button btn;
      
      btn = new Button(course.getName());
      btn.setId(course.getName().replace(" ", "").replace("'", ""));
      btn.setOnAction(event -> {
        Object node = event.getSource();
        Button b = (Button) node;
        displaySelectedCourseInfo(b.getText());
      });
      btn.setWrapText(true);
      btn.setMaxWidth(310);
      btn.setMaxHeight(60);
      btn.setTextAlignment(TextAlignment.CENTER);
      
      if (activeStudentPassedCourses.keySet().contains(course.getGroupID())) {
        btn.getStyleClass().add("passed-course");
      } else {
        btn.getStyleClass().add("module-heading");
      }
      allCoursesGrid.add(btn, i % 2, i / 2);
    }
  }

  @FXML private Label courseNameLabel;
  @FXML private Label courseCodeLabel;
  @FXML private Label courseStatusLabel;
  @FXML private Label courseGradeLabel;
  @FXML private Label courseCreditLabel;

  /**
   * Display additional information for the selected course when clicked on the course card
   * of that course
   * @param courseName The name of the course to show information
   */
  private void displaySelectedCourseInfo(String courseName) {
    for (CourseUnit c : activeStudentAllCourses) {
      if (c.getName().equals(courseName)) {
        courseNameLabel.setText(c.getName());
        courseCodeLabel.setText(c.getCourseCode());

        int min = c.getMinCredit();
        int max = c.getMaxCredit();
        // System.out.println(c.getAPI());
        // System.out.println(min);
        // System.out.println(max);
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
        if (activeStudentPassedCourses.keySet().contains(c.getGroupID())) {
          courseStatusLabel.setText("Status: passed.");
          courseGradeLabel.setText("Grade: " + activeStudentPassedCourses.get(c.getGroupID()));
        }
      }
    }
  }
  
  @FXML private Label completedCreditsLabel;
  @FXML private Label totalCreditsLabel;
  @FXML private Label studentAverageGradeLabel;

  /**
   * Update study progress of active student i.e., average grades, finished credits,
   * and total credits planned
   */
  private void updateCredits() {
    int completed = 0;
    int totalMin = 0;
    int totalMax = 0;
    float sumGrade = 0;
    for (CourseUnit c : activeStudentAllCourses) {
      int min = c.getMinCredit();
      int max = c.getMaxCredit();
      totalMin += min;
      if (max != -1) {
        totalMax += max;
      }
      if (activeStudentPassedCourses.keySet().contains(c.getGroupID())) {
        completed += min;
      }
    }
    completedCreditsLabel.setText(Integer.toString(completed));
    totalCreditsLabel.setText(Integer.toString(totalMin));
    // Average grade of student
    for (Integer grade : activeStudentPassedCourses.values()){
        sumGrade += grade;
    }
    int n = activeStudentPassedCourses.size();
    // only show 2 decimal points
    NumberFormat formatter = new DecimalFormat("0.00");
    System.out.println(sumGrade / n);
    studentAverageGradeLabel.setText(formatter.format(sumGrade / n));
    if (totalMax != totalMin) {
      totalCreditsLabel.setText(Integer.toString(totalMin) + "-" + Integer.toString(totalMax));
    }
  }
}


