/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fi.tuni.prog3.sisu.system.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author An Nguyen
 */
public class JsonWriter {
    
    private Gson gson;
    
    /**
     * Construct JsonWriter with GsonBuilder
     */
    public JsonWriter(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserJson.class, new UserJsonAdapter());
        builder.setPrettyPrinting();
        this.gson = builder.create();        
    }
    
    /**
     * Map each object of {@link fi.tuni.prog3.sisu.system.User} to
     * {@link UserJson} in order to write to Json file
     * @param students HashMap of {@link fi.tuni.prog3.sisu.system.Student}
     * @param teachers HashMap of {@link fi.tuni.prog3.sisu.system.Teacher}
     */
    private List<UserJson> mapUserJson(HashMap<String, Student> students, 
            HashMap<String, Teacher> teachers){
        List<UserJson> users = new ArrayList<>();
        
        students.values().forEach(s -> {
            users.add(new UserJson(s.getUsername(), s.getFullName(), s.getSalt(),
                    s.getPassword(), "student"));
        });
        
        teachers.values().forEach(s -> {
            users.add(new UserJson(s.getUsername(), s.getFullName(), s.getSalt(),
                    s.getPassword(), "teacher"));
        });
        
        return users;
    }
    
    /**
     * Write each user information into Json file format
     * @param students HashMap of {@link fi.tuni.prog3.sisu.system.Student}
     * @param teachers HashMap of {@link fi.tuni.prog3.sisu.system.Teacher}
     * @param filePath the relative path of json file
     */
    public void writeUsers(HashMap<String, Student> students, 
            HashMap<String, Teacher> teachers, String filePath){
        List<UserJson> users = mapUserJson(students, teachers);
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            gson.toJson(users, writer);
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
