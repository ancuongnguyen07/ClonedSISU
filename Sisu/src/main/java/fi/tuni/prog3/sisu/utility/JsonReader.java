/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.util.HashMap;
import fi.tuni.prog3.sisu.system.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author An Nguyen
 */
public class JsonReader {
    private Gson gson;
    
    /**
     * Construct an initially empty JsonReader
     */
    public JsonReader(){
        this.gson = new Gson();
    }
    
    
    /**
     * Read user information in Json file into a List<{@link fi.tuni.prog3.sisu.system.User}>
     * @param filePath the path of Json file containing user information
     */
    public List<User> readUsers(String filePath){  
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserJson.class, new UserJsonAdapter());
        builder.setPrettyPrinting();
        this.gson = builder.create();
        
        List<User> users = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            List<UserJson> usersJson = gson.fromJson(reader,
                    new TypeToken<List<UserJson>>() {}.getType());          
            
            for (UserJson u : usersJson){
                User newUser;
                String username = u.getUsername();
                String fullname = u.getFullname();
                String salt = u.getSalt();
                String hashedPassword = u.getHashedPassword();
                String role = u.getRole();
                
                if (role.equals("student")){
                    newUser = new Student(username, fullname, salt, hashedPassword);
                }
                else{
                    newUser = new Teacher(username, fullname, salt, hashedPassword);
                }
                users.add(newUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public List<StudyPlanJSON> readUserStudyPlan(String filePath){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(StudyPlanJSON.class, new StudyPlanJSONAdapter());
        this.gson = builder.create();
        
        List<StudyPlanJSON> plans = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            plans = this.gson.fromJson(reader, 
                    new TypeToken<List<StudyPlanJSON>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plans;
    }
}
