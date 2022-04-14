/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 *
 * @author Cuong Nguyen
 */
public class Student extends User{
    private StudyPlan plan;
    
    public Student(String username, String fullName, String salt, String password){
        super(username, fullName, salt, password);
    }
}
