/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import fi.tuni.prog3.sisu.utility.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage all information for the sytem
 * @author Cuong Nguyen
 */
public class SkyNet {
    
    private HashMap<String, Student> students;
    private HashMap<String, Teacher> teachers;
    private final String studyPlanFilePath = "src/main/resources/jsons/studyPlan.json";
    
    private ArrayList<DegreeProgram> programs;
    private APIReader api;
    private User activeUser;
    // private Student activeStudent;
    // private Teacher activeStudent;

    
    /**
     * Construct an initially empty SkyNet object and then
     * do {@link SkyNet#loadUsers() }
     */
    public SkyNet(String usersFilePath){
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.programs = new ArrayList<>();
        this.api = new APIReader();
        
        loadUsers(usersFilePath);
        loadUserStudyPlan();
        
        // demo load Sci&Eng degree structure into DegreeProgam obj
        // loadStudyPlans();

        // Later, comment this line out if there will be multiple different degrees for
        // different students
        // loadCompositeRuleRec(this.programs.get(0).getCompositeRule());
        // printRec(this.programs.get(0));
        
//        this.activeUser = this.students.get("an");
//        StudyModule s = findStudyModuleByID(this.programs.get(0).getCompositeRule(),
//                                        "otm-0fc04f22-f797-48fc-9222-48d5d47d1870");
//        System.out.println(achievedCreStudyModule(s));
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public HashMap<String, Teacher> getTeachers() {
        return teachers;
    }

    public ArrayList<DegreeProgram> getPrograms() {
        return programs;
    }

    public User getActiveUser() {
        return activeUser;
    }
    
    /**
     * Load all completed courses and registered study modules of all users
     */
    private void loadUserStudyPlan(){
        try {
            JsonReader reader = new JsonReader();
            List<StudyPlanJSON> plans = reader.readUserStudyPlan(studyPlanFilePath);
            for (StudyPlanJSON p : plans){
                Student tar = this.students.get(p.getUsername());
                tar.setDegreeID(p.getDegree());
                tar.setModuleIDs(p.getModules());
                tar.setCourseIDs(p.getPassedCourses());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // print detail of a composite rule for debuging
    private void printCompositeRule(SubCompositeRule rule){
        for (StudyModule sm : rule.getSubModules()){
            System.out.println(sm.getName());
            printCompositeRule(sm.getCompositeRule());
        }
        
        for (CourseUnit c : rule.getSubCourses()){
            System.out.println(c.getName());
        }
        
        for (SubCompositeRule r : rule.getSubComposites()){
            printCompositeRule(r);
        }
    }
    
    // // print detail of a degree program for debuging
    private void printRec(DegreeProgram obj){
        System.out.println("xxxxxxxxxxxxxxxxxxx");
        System.out.println(obj.getName());
        printCompositeRule(obj.getCompositeRule());
    }
    
    // Demo load detail structure of degree program
    private void loadStudyPlans(){
        // add Science and Engineering program
        JsonArray degreeArray = this.api.callAllDegrees();
        JsonObject degreeOverview = degreeArray.get(8).getAsJsonObject();
        DegreeProgram dp = getDegreeByID(degreeOverview.get("id").getAsString());
        this.programs.add(dp);
        
        // load degree detail through API to database
        loadCompositeRuleRec(this.programs.get(0).getCompositeRule());
//        printRec(dp);
    }
    
    /**
     * Load user information from json file to HashMaps for separate
     * role of user {@link Student} and {@link Teacher}
     */
    private void loadUsers(String usersFilePath){
        try {
            JsonReader reader = new JsonReader();
            List<User> users = reader.readUsers(usersFilePath);
            
            for(User u : users){
                if (u instanceof Student){
                    this.students.put(u.getUsername(), (Student) u);
                }
                else{
                    this.teachers.put(u.getUsername(), (Teacher) u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Generate a random salt used in hashing password
     * @return a byte-string salt
     */
    private String randomSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Password has at least
     * 6 characters, containing lower and uppercase and at least one special
     * symbol such as !,?,%,^,&,etc.
     * @param password
     * @return True if the password is valid, otherwise False
     */
    private boolean isValidPassword(String password){
        if (password.length() < 6){
            return false;
        }
        
        char ch;
        boolean capitalFlag = false;
        boolean lowerFlag = false;
        boolean specialFlag = false;
        for (int i = 0; i < password.length(); i++){
            ch = password.charAt(i);
            if (Character.isLowerCase(ch)){
                lowerFlag = true;
            }
            else if(Character.isUpperCase(ch)){
                capitalFlag = true;
            }
            else if(!Character.isDigit(ch) && !Character.isAlphabetic(i)){
                // check special symbol
                specialFlag = true;
            }
        }
        
        return lowerFlag && capitalFlag && specialFlag;
    }
    // ============== PUBLIC METHODS ===============
    // Methods should be used in Controllers for GUI
    // =============================================
    
    /**
     * Return the active student in this session
     * @return the active Student 
     */
    public Student getActiveStudent() {
        return this.students.get(this.activeUser.getUsername());
    }
    
    /**
     * Save user data to JSON file
     * @param usersFilePath file path to the JSON file that contains the user data
     */
    public void saveUsers(String usersFilePath){
        JsonWriter writer = new JsonWriter();
        writer.writeUsers(this.students, this.teachers, usersFilePath);
    }
    
    /**
     * Generate a hash of (text_password + salt)
     * @param password password typed by users
     * @param saltString random string added to password before doing hash
     * @return a hashed password - String
     */
    public String hashPassword(String password, String saltString){
        try {
            byte[] salt = Base64.getDecoder().decode(saltString);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        catch (InvalidKeySpecException ikse){
            ikse.printStackTrace();
        }
        return null;
    }
    
    /**
     * Check if the typed username and password are correct
     * @param username username typed by user
     * @param password password typed by user
     * @return True if hash of typed password and stored-hashed password are the same
     */
    public boolean validatePassword(String username, String password){
        User u = this.students.get(username);
        if (u == null){
            u = this.teachers.get(username);
        }
        
        if (u == null){
            return false;
        }
        
        String hashedPassword = u.getPassword();
        String salt = u.getSalt();
        if (hashPassword(password, salt).equals(hashedPassword)) {
            this.activeUser = u;
            return true;
        }
        return false;
    }
    
    /**
     * Add new {@link User} into system
     * @param username username of user
     * @param fullname fullname of user
     * @param password un-hashed password of user
     * @param role role of user: "student" or "teacher"
     * @return integer values in {0,1,2}, 0: successful, 1: error -
     * existed username, 2: error - invalid password. 
     */
    public int addNewUser(String username, String fullname,
                        String password, String role){
        
        // Check already existed user
        User existUser = null;
        if (role.equals("student")){
            existUser = this.students.get(username);
        }
        else if(role.equals("teacher")){
            existUser = this.teachers.get(username);
        }
        if (existUser != null){
            // there is already a user with the same username
            return 1;
        }
        
        // Check the validation of password
        if (!isValidPassword(password)){
            return 2;
        }
        
        // Generate salt for the new user and hash password
        String salt = randomSalt();
        String hashedPassword = hashPassword(password, salt);
        
        if (role.equals("student")){
            this.students.put(username, new Student(username, fullname, 
                    salt, hashedPassword));
        }
        else if (role.equals("teacher")){
            this.teachers.put(username, new Teacher(username, fullname, 
                    salt, hashedPassword));
        }
        
        return 0;
    }
    
    /**
     * Return a object of {@link DegreeProgram} representing its components detail
     * @param id
     * @return a object of {@link DegreeProgram} with given id
     */
    public DegreeProgram getDegreeByID(String id){
        String currentDegreeAPI = this.api.getDegreeDetailAPI() + id;
        try {
            JsonObject degreeDetail = api.connectAPI(currentDegreeAPI, "id");
            return api.JsonToDegreeProgram(degreeDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Recursively get data of a study module
     */
    public void loadStudyModuleRec(StudyModule obj){
        try {
            api.onClickStudyModule(obj);
            loadCompositeRuleRec(obj.getCompositeRule());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Recursively get data of a composite rule
     * @param obj
     */
    public void loadCompositeRuleRec(SubCompositeRule obj){
        try {
            for (int i = 0; i < obj.getSubModules().size(); i++){
                loadStudyModuleRec(obj.getSubModules().get(i));
            }
            for (int i = 0; i < obj.getSubComposites().size(); i++){
                loadCompositeRuleRec(obj.getSubComposites().get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get data of a study module 1-layered depth 
     * @param obj
     */
    public void loadStudyModule(StudyModule obj){
        try {
            api.onClickStudyModule(obj);
            loadCompositeRule(obj.getCompositeRule());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get data of a composite rule 1-layered depth
     * @param obj
     */
    public void loadCompositeRule(SubCompositeRule obj){
        try {
            for (int i = 0; i < obj.getSubModules().size(); i++){
                loadStudyModule(obj.getSubModules().get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Recursively calculating the achieved credits of one specific module 
     * of logged in user
     * @param rule
     * @return achieved credits
     */
    public int achievedCreStudyModule(StudyModule m){
        int cre = 0;  
        System.out.println(m.getName());
        try {
            Student activeStudent = this.students.get(this.activeUser.getUsername());
            ArrayList<String> coursesIDs = activeStudent.getCourseIDs();
            SubCompositeRule rule = m.getCompositeRule();


            for (CourseUnit c : rule.getSubCourses()){
//                System.out.println(c.getGroupID());
                if (coursesIDs.indexOf(c.getGroupID()) != -1){
                    cre += c.getMaxCredit();
                }
            }

            for (StudyModule sm : rule.getSubModules()){
                cre += achievedCreStudyModule(sm);
            }

            for (SubCompositeRule scr : rule.getSubComposites()){
                for (StudyModule s : scr.getSubModules()){
                    cre += achievedCreStudyModule(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return cre;
    }
    
    /**
     * Return a StudyModule object with given ID
     * @param root
     * @param id
     * @return StudyModule object with given ID
     */
    public StudyModule findStudyModuleByID(SubCompositeRule root, String id){
        for (StudyModule sm : root.getSubModules()){
            System.out.println(sm.getId());
            if (sm.getId().equals(id)){
                return sm;
            }
        }
        
        for (StudyModule sm : root.getSubModules()){
            StudyModule result = findStudyModuleByID(sm.getCompositeRule(), id);
            if (result != null){
                return result;
            }
        }
        
        for (SubCompositeRule scr : root.getSubComposites()){
            StudyModule result = findStudyModuleByID(scr, id);
            if (result != null){
                return result;
            }
        }
        return null;
    }
}
