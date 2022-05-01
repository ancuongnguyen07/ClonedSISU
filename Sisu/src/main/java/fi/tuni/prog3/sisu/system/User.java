/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;

/**
 * The User is a parent class of {@link Student}, {@link Teacher}
 * represent an instance of user in the system
 * @author Cuong Nguyen
 */
public class User {
    private String username;
    private String fullName;
    private final String salt;
    private String password;
    
    /**
     * Constructor with given all information of user
     * @param username username of user
     * @param fullName full name of user
     * @param salt salt string of user used for generating password
     * @param password hashed-password of user
     */
    public User(String username, String fullName,String salt, String password){
        this.username = username;
        this.fullName = fullName;
        this.salt = salt;
        this.password = password;
    }

    /**
     * return the username of user
     * @return username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * return full name of user
     * @return full name of user
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * return salt-string of user
     * @return salt-string of user
     */
    public String getSalt(){
        return salt;
    }

    /**
     * return password of user
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * set new username for user
     * @param username new username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * set new full name for user
     * @param fullName new full name of user
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * set new password for user
     * @param password new password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * return role of user: 'student' or 'teacher'
     * @return role of user: 'student' or 'teacher'
     */
    public String getRole(){
        return this.getClass().getSimpleName().toLowerCase();
    }
    
    
    /**
     * comparison mechanism of two User objects
     * @param o object to be compared
     * @return True if two objects are equal
     */
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        
        if (!(o instanceof User)){
            return false;
        }
        
        User u = (User) o;
        return this.username.equals(u.getUsername()) && this.password.equals(u.getPassword())
                && this.fullName.equals(u.getFullName()) && this.salt.equals(u.salt);
    }
    
    /**
     * return a String representing all information of a User object
     * @return a String representing all information of a User object
     */
    @Override
    public String toString(){
       return String.format("username: %s, \nfullname: %s,\n"
               + "salt: %s, \nhashedPassword: %s", 
               this.username, this.fullName, this.salt, this.password);
   }
}
