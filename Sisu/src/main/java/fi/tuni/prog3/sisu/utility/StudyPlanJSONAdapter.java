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
import java.util.HashMap;

/**
 * A class describe type adapter for {@link StudyPlanJSON}
 * @author Cuong Nguyen
 */
public class StudyPlanJSONAdapter extends TypeAdapter<StudyPlanJSON>{

    /**
     * Override method of writing which is unsupported in this case
     * @param writer
     * @param t
     * @throws java.io.IOException
     */
    @Override
    public void write(JsonWriter writer, StudyPlanJSON t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Override method of reading JSON files
     * @param reader
     * @return 
     * @throws java.io.IOException 
     */
    @Override
    public StudyPlanJSON read(JsonReader reader) throws IOException {
        StudyPlanJSON plan = new StudyPlanJSON();
        reader.beginObject();
        String fieldname = null;
        
        while(reader.hasNext()){
            switch(reader.nextName()){
                case "username":
                    plan.setUsername(reader.nextString());
                    break;
                case "degree":
                    plan.setDegree(reader.nextString());
                    break;
                case "modules":
                    reader.beginArray();
                    ArrayList<String> ms = new ArrayList<>();
                    while (reader.hasNext()){
                        ms.add(reader.nextString());
                    }
                    plan.setModules(ms);
                    reader.endArray();
                    break;
                case "passedCourses":
                    reader.beginArray();
                    
                    HashMap<String, Integer> pc = new HashMap<>();
                    String id = "";
                    while(reader.hasNext()){ 
                        reader.beginObject();
                        while(reader.hasNext()){
                            switch(reader.nextName()){
                            case "courseGroupID":
                                id = reader.nextString();
                                pc.put(id, 0);
                                break;
                            case "grade":
                                pc.replace(id, reader.nextInt());
                                break;
                            }
                        }
                        
                        reader.endObject();
                    }
                    plan.setPassedCourses(pc);
                    reader.endArray();
                    
                    break;
            }
        }
        reader.endObject();
        return plan;
    }

    
}
