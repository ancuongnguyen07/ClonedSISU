/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;

/**
 *
 * @author Cuong Nguyen
 */
public class User {
    private String username;
    private String fullName;
    private String salt;
    private String password;
    
    public User(String username, String fullName,String salt, String password){
        this.username = username;
        this.fullName = fullName;
        this.salt = salt;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
    
    public String getSalt(){
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole(){
        return this.getClass().getSimpleName().toLowerCase();
    }
    
    
    /**
     * 
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
    
    @Override
    public String toString(){
       return String.format("username: %s, \nfullname: %s,\n"
               + "salt: %s, \nhashedPassword: %s", 
               this.username, this.fullName, this.salt, this.password);
   }
}
