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
public class Student extends User{
    private String degreeID;
    private ArrayList<String> moduleIDs;
    private ArrayList<String> courseIDs;
    
    public Student(String username, String fullName, String salt, String password){
        super(username, fullName, salt, password);
        this.moduleIDs = new ArrayList<>();
        this.courseIDs = new ArrayList<>();
    }

    public String getDegreeID() {
        return degreeID;
    }

    public void setDegreeID(String degreeID) {
        this.degreeID = degreeID;
    }

    public ArrayList<String> getModuleIDs() {
        return moduleIDs;
    }

    public void setModuleIDs(ArrayList<String> moduleIDs) {
        this.moduleIDs = moduleIDs;
    }

    public ArrayList<String> getCourseIDs() {
        return courseIDs;
    }

    public void setCourseIDs(ArrayList<String> courseIDs) {
        this.courseIDs = courseIDs;
    }
    
    
}