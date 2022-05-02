/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import fi.tuni.prog3.sisu.system.Student;
import fi.tuni.prog3.sisu.system.Teacher;
import java.util.HashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import fi.tuni.prog3.sisu.system.*;
import java.util.List;

/**
 *
 * @author Cuong Nguyen
 */
public class JsonWriterTest {
    
    /**
     *
     */
    public JsonWriterTest() {
    }

    /**
     * Test of writeUsers method, of class JsonWriter.
     */
    @Test
    public void testWriteUsers() {
        System.out.println("writeUsers");
        HashMap<String, Student> students = new HashMap<>();
        HashMap<String, Teacher> teachers = new HashMap<>();
        
        // add a student and a teacher
        students.put("jadon", new Student("jadon", "jadon stuart","123","456"));
        teachers.put("lacetia", new Teacher("lacetia", "lacetia peptit","abc","xyz"));     
        
        // write users information to json file
        String filePath = "src/test/resources/jsons/writerTest.json";
        JsonWriter instance = new JsonWriter();
        instance.writeUsers(students, teachers, filePath);
        
        // read users information
        JsonReader reader = new JsonReader();
        List<User> users = reader.readUsers(filePath);
        
        boolean flag = true;
        for (User u : users){
            if (u instanceof Student){
                flag = flag && u.equals(students.get("jadon"));
            }
            else{
                flag = flag && u.equals(teachers.get("lacetia"));
            }
        }
        
        assertTrue(flag);
    }
    
}
