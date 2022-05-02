/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Cuong Nguyen
 */
public class SkyNetTest {
    private final String userFilePath = "src/test/resources/jsons/usersTest.json";
    private final String studyPlanFilePath = "src/test/resources/jsons/studyPlanTest.json";

    /**
     *
     */
    public SkyNetTest() {
    }

    /**
     * Test of Constructor method, of class SkyNet
     * if they load correct user information at initializing
     */
    @org.junit.jupiter.api.Test
    public void testLoadUsers(){
        System.out.println("loadUsers");
        SkyNet instance = new SkyNet(userFilePath, studyPlanFilePath);
        HashMap<String, Student> studentTest = new HashMap<>();
        HashMap<String, Teacher> teacherTest = new HashMap<>();
             
        HashMap<String, Student> students = instance.getStudents();
        HashMap<String, Teacher> teachers = instance.getTeachers();
        
        // set degree id
        Student tar = new Student("an", "an nguyen",
                        "ufxZLnxh27y8lkYXfBJfXw==", "GUx4lpRvPI0rYWwLqB77Pg==");
        String degreeID = "otm-df83fbbd-f82d-4fda-b819-78f6b2077fcb";
        tar.setDegreeID(degreeID);
        
        // set modules list
        ArrayList<String> moduleList = new ArrayList<>();
        moduleList.add("otm-db1d8c96-6cfe-4ae3-b5e2-a8c557ba87fa");
        moduleList.add("otm-de1f8d9b-db28-424e-bba1-06f110e08195");
        moduleList.add("otm-0fc04f22-f797-48fc-9222-48d5d47d1870");
        tar.setModuleIDs(moduleList);
        
        // set passed courses
        HashMap<String, Integer> courses = new HashMap<>();
        courses.put("tut-cu-g-45454", 5);
        courses.put("otm-d7c61c94-eb14-478d-b7c5-431c164d4957", 4);
        courses.put("tut-cu-g-45620", 5);
        tar.setCourses(courses);
        
        // testing user information
        teacherTest.put("khoa", new Teacher("khoa", "khoa nguyen",
                        "k/Rcd6ELpCLtH4RAWrXhqQ==", "omhH9RT77dsEZI9Wpi9dUw=="));
        studentTest.put("an", tar);
        
        assertTrue(students.get("an").equals(studentTest.get("an"))
                    && teachers.get("khoa").equals(teacherTest.get("khoa")));
        assertTrue(tar.getModuleIDs().equals(moduleList));
        assertTrue(tar.getDegreeID().equals(degreeID));
        assertTrue(tar.getCourses().equals(courses));
    }
    
    /**
     * Test of validatePassword method, of class SkyNet.
     */
    @org.junit.jupiter.api.Test
    public void testValidatePassword() {
        System.out.println("validatePassword");
        String username = "khoa";
        String password = "jadon";
        SkyNet instance = new SkyNet(userFilePath, studyPlanFilePath);
        boolean expResult = false;
        boolean result = instance.validatePassword(username, password);
        assertEquals(expResult, result);
    }
    
    /**
     * Test addNewUser function
     */
    @Test
    public void testAddNewUser(){
        System.out.println("addNewUser");
        SkyNet sn = new SkyNet(userFilePath, studyPlanFilePath);
        
        // case 1: add existed username
        String username = "khoa";
        String fullname = "khoa nguyen";
        String password = "abc123E!"; // valid password
        String role = "teacher";
        
        int code = sn.addNewUser(username, fullname, password, role);
        boolean flag1 = code == 1;
        System.out.println(flag1);
        
        // case 2: invalid password
        username = "jadon";
        password = "abcertE";
        
        code = sn.addNewUser(username, fullname, password, role);
        boolean flag2 = code == 2;
        System.out.println(flag2);
        
        // case 3: successful
        password = "Jadon123!"; // valid password
        
        code = sn.addNewUser(username, fullname, password, role);
        boolean flag3 = code == 0;
        System.out.println(flag3);
        
        Teacher addedUser = sn.getTeachers().get("jadon");
        String salt = addedUser.getSalt();
        Teacher testUser = new Teacher(username, fullname, salt, 
                                        sn.hashPassword(password, salt));
        boolean flag4 = addedUser.equals(testUser);
        System.out.println(flag4);
        
        assertTrue(flag1 && flag2 && flag3 && flag4);
    }
}
