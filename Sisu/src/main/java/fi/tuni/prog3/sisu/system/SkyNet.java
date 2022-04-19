/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

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
import java.util.List;

/**
 * Manage all information for the sytem
 * @author Cuong Nguyen
 */
public class SkyNet {
    
    private HashMap<String, Student> students;
    private HashMap<String, Teacher> teachers;
    
    private HashMap<String, AbstractModule> modules;
    private User activeUser;
    
    /**
     * Construct an initially empty SkyNet object and then
     * do {@link SkyNet#loadUsers() }
     */
    public SkyNet(String usersFilePath){
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.modules = new HashMap<>();
        
        loadUsers(usersFilePath);
    }

    public HashMap<String, Student> getStudents() {
        return students;
    }

    public HashMap<String, Teacher> getTeachers() {
        return teachers;
    }

    public HashMap<String, AbstractModule> getModules() {
        return modules;
    }

    public User getActiveUser() {
        return activeUser;
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
    // ======================================= PUBLIC METHODS
    // Methods should be used in Controllers
    // =======================================
    
    
    /**
     * Save user information to json file
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
     * existed username, 2: error - invalid password. Password has at least
     * 6 characters, containing lower and uppercase and at least one special
     * symbol such as !,?,%,^,&,etc.
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
}
