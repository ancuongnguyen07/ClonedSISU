/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

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
    private String userFilePath = "src/test/resources/jsons/usersTest.json";
    
    public SkyNetTest() {
    }

    /**
     * Test of Constructor method, of class SkyNet
     * if they load correct user information at initializing
     */
    @org.junit.jupiter.api.Test
    public void testLoadUsers(){
        System.out.println("loadUsers");
        SkyNet instance = new SkyNet(userFilePath);
        HashMap<String, Student> studentTest = new HashMap<>();
        HashMap<String, Teacher> teacherTest = new HashMap<>();
        
        teacherTest.put("khoa", new Teacher("khoa", "khoa nguyen",
                        "k/Rcd6ELpCLtH4RAWrXhqQ==", "omhH9RT77dsEZI9Wpi9dUw=="));
        studentTest.put("an", new Student("an", "an nguyen",
                        "ufxZLnxh27y8lkYXfBJfXw==", "GUx4lpRvPI0rYWwLqB77Pg=="));
        
        HashMap<String, Student> students = instance.getStudents();
        HashMap<String, Teacher> teachers = instance.getTeachers();
        
        assertTrue(students.get("an").equals(studentTest.get("an"))
                    && teachers.get("khoa").equals(teacherTest.get("khoa")));
    }
    
    /**
     * Test of validatePassword method, of class SkyNet.
     */
    @org.junit.jupiter.api.Test
    public void testValidatePassword() {
        System.out.println("validatePassword");
        String username = "khoa";
        String password = "jadon";
        SkyNet instance = new SkyNet(userFilePath);
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
        SkyNet sn = new SkyNet(userFilePath);
        
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
