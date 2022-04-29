/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import java.util.ArrayList;

/**
 *
 * @author Cuong Nguyen
 */
public class StudyPlanJSON {
    private String username;
    private String degree;
    private ArrayList<String> modules;
    private ArrayList<String> passedCourses;

    public StudyPlanJSON(String username, String degree, ArrayList<String> modules, ArrayList<String> passedCourses) {
        this.username = username;
        this.degree = degree;
        this.modules = modules;
        this.passedCourses = passedCourses;
    }

    StudyPlanJSON() {
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public ArrayList<String> getModules() {
        return modules;
    }

    public void setModules(ArrayList<String> modules) {
        this.modules = modules;
    }

    public ArrayList<String> getPassedCourses() {
        return passedCourses;
    }

    public void setPassedCourses(ArrayList<String> passedCourses) {
        this.passedCourses = passedCourses;
    }
    
    
}
