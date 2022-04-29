/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Cuong Nguyen
 */
public class StudyPlanJSONAdapter extends TypeAdapter<StudyPlanJSON>{

    @Override
    public void write(JsonWriter writer, StudyPlanJSON t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StudyPlanJSON read(JsonReader reader) throws IOException {
        StudyPlanJSON plan = new StudyPlanJSON();
        reader.beginObject();
        String fieldname = null;
        
        while(reader.hasNext()){
            JsonToken token = reader.peek();
            
            if (token.equals(JsonToken.NAME)){
                // get the current token
                fieldname = reader.nextName();
            }
            
            if ("username".equals(fieldname)){
                token = reader.peek();
                plan.setUsername(reader.nextString());
            }
                
            if ("degree".equals(fieldname)){
                token = reader.peek();
                plan.setDegree(reader.nextString());
            }
                
            if ("modules".equals(fieldname)){
                token = reader.peek();
                reader.beginArray();
                ArrayList<String> ms = new ArrayList<>();
                while(reader.hasNext()){
                    ms.add(reader.nextString());
                }
                plan.setModules(ms);
                reader.endArray();
            }
                
            if ("passedCourses".equals(fieldname)){
                token = reader.peek();
                reader.beginArray();
                ArrayList<String> cs = new ArrayList<>();
                while(reader.hasNext()){
                    cs.add(reader.nextString());
                }
                plan.setPassedCourses(cs);
                reader.endArray();
            }
        }
        reader.endObject();
        return plan;
    }

    
}
