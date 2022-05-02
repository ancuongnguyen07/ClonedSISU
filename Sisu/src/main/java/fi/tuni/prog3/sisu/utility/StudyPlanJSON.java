/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class representing all study information of a student 
 * which is stored in a JSON file - only used for GSON Input/Output features
 * @author Cuong Nguyen
 */
public class StudyPlanJSON {
    private String username;
    private String degree;
    private ArrayList<String> modules;
    private HashMap<String, Integer> passedCourses;

    /**
     * Constructor with all fields in JSON files
     * @param username username of student
     * @param degree id of degree which a student enrolled
     * @param modules id of modules which a student enrolled
     * @param passedCourses id of courses and their grades
     */
    public StudyPlanJSON(String username, String degree, ArrayList<String> modules, 
                        HashMap<String, Integer> passedCourses) {
        this.username = username;
        this.degree = degree;
        this.modules = modules;
        this.passedCourses = passedCourses;
    }

    /**
     * Empty constructor
     */
    public StudyPlanJSON() {
        
    }

    /**
     * return username of user
     * @return username of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * set new username
     * @param username new username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * return degree id of user
     * @return degree id of user
     */
    public String getDegree() {
        return degree;
    }

    /**
     * set new degree id
     * @param degree new degree id
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }

    /**
     * return list of module ids
     * @return list of module ids
     */
    public ArrayList<String> getModules() {
        return modules;
    }

    /**
     * set new list of module ids
     * @param modules new list of module ids
     */
    public void setModules(ArrayList<String> modules) {
        this.modules = modules;
    }

    /**
     * return HashMap(course_id: grade)
     * @return HashMap(course_id: grade)
     */
    public HashMap<String, Integer> getPassedCourses() {
        return passedCourses;
    }

    /**
     * set new HashMap(course_id: grade)
     * @param new HashMap(course_id: grade)
     */
    public void setPassedCourses(HashMap<String, Integer> passedCourses) {
        this.passedCourses = passedCourses;
    }
    
    
}
