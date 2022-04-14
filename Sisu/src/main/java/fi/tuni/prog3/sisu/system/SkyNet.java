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
    private String hashPassword(String password, String saltString){
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
        return hashPassword(password, salt).equals(hashedPassword);
    }
}
