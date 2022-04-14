/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.HashMap;

/**
 *
 * @author Cuong Nguyen
 */
public class Teacher extends User{
    private HashMap<String, AbstractModule> courses;
    
    public Teacher(String username, String fullName, String salt, String password){
        super(username, fullName, salt, password);
    }
}
