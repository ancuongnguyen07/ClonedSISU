/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.HashMap;

/**
 * A child class of {@link User} representing a user as a teacher role
 * @author Cuong Nguyen
 */
public class Teacher extends User{
    private HashMap<String, AbstractModule> courses;
    
    /**
     * Constructor with given all information of user
     * @param username username of user
     * @param fullName full name of user
     * @param salt salt string of user used for generating password
     * @param password hashed-password of user
     */
    public Teacher(String username, String fullName, String salt, String password){
        super(username, fullName, salt, password);
    }
}
