/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A child class of {@link User} representing a user as a student role
 * @author Cuong Nguyen
 */
public class Student extends User {
    private String degreeID;
    private ArrayList<String> moduleIDs;
    private HashMap<String, Integer> courses;
    
    /**
     * Constructor with given all information of user as student
     * @param username username of user
     * @param fullName full name of user
     * @param salt salt string of user used for generating password
     * @param password hashed-password of user
     */
    public Student(String username, String fullName, String salt, String password){
        super(username, fullName, salt, password);
        this.moduleIDs = new ArrayList<>();
        this.courses = new HashMap<>();
    }

    /**
     * return the degree id of a student is enrolling
     * @return the degree id of a student is enrolling
     */
    public String getDegreeID() {
        return degreeID;
    }

    /**
     * set new id of new degree which a student enroll
     * @param degreeID id of degree
     */
    public void setDegreeID(String degreeID) {
        this.degreeID = degreeID;
    }

    /**
     * return a ArrayList of ids of modules which a student enrolled
     * @return a ArrayList of ids of modules which a student enrolled
     */
    public ArrayList<String> getModuleIDs() {
        return moduleIDs;
    }

    /**
     * set a new ArrayList of ids of modules which a student enrolls
     * @param moduleIDs ArrayList of ids of modules which a student enrolls
     */
    public void setModuleIDs(ArrayList<String> moduleIDs) {
        this.moduleIDs = moduleIDs;
    }

    /**
     * return a HashMap (key is course group_id/value is its grade) of 
     * courses which a student enrolled
     * @return a HashMap (key is course group_id/value is its grade) of 
     * courses which a student enrolled
     */
    public HashMap<String, Integer> getCourses() {
        return courses;
    }

    /**
     * set a new a HashMap (key is course group_id/value is its grade) of 
     * courses which a student enroll
     * @param courses a new a HashMap (key is course group_id/value is its grade) of 
     * courses which a student enroll
     */
    public void setCourses(HashMap<String, Integer> courses) {
        this.courses = courses;
    }
    
//    @Override
//    public boolean equals(Object o){   
//        if (!(o instanceof Student)){
//            return false;
//        }
//        boolean basicInfo = super.equals(o);
//        
//        if ()
//        Student s = (Student) o;
//        return (s.getDegreeID().equals(this.getDegreeID()) && 
//                s.getModuleIDs().equals(this.getModuleIDs()) &&
//                s.getCourses().equals(this.getCourses()));
//    }
}