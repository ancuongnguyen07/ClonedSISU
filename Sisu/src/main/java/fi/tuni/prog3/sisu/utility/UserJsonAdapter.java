/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.utility;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * A class describe type adapter for {@link UserJson}
 * @author Cuong Nguyen
 */
public class UserJsonAdapter extends TypeAdapter<UserJson>{

    /**
     Override method of writing
     */
    @Override
    public void write(JsonWriter writer, UserJson u) throws IOException {
        writer.beginObject();
        writer.name("username");
        writer.value(u.getUsername());
        writer.name("fullname");
        writer.value(u.getFullname());
        writer.name("salt");
        writer.value(u.getSalt());
        writer.name("hashedPassword");
        writer.value(u.getHashedPassword());
        writer.name("role");
        writer.value(u.getRole());
        writer.endObject();
    }

    /**
     * Override method reading
     */
    @Override
    public UserJson read(com.google.gson.stream.JsonReader reader) throws IOException {
        UserJson user = new UserJson();
        reader.beginObject();
        String fieldname = null;
        
        while (reader.hasNext()){
            JsonToken token = reader.peek();
            
            if (token.equals(JsonToken.NAME)){
                // get the current token
                fieldname = reader.nextName();
            }
            
            if ("username".equals(fieldname)){
                // move to the next token
                token = reader.peek();
                user.setUsername(reader.nextString());
            }
            
            if ("fullname".equals(fieldname)){
                // move to the next token
                token = reader.peek();
                user.setFullname(reader.nextString());
            }
            
            if ("salt".equals(fieldname)){
                // move to the next token
                token = reader.peek();
                user.setSalt(reader.nextString());
            }
            
            if ("hashedPassword".equals(fieldname)){
                // move to the next token
                token = reader.peek();
                user.setHashedPassword(reader.nextString());
            }
            
            if ("role".equals(fieldname)){
                // move to the next token
                token = reader.peek();
                user.setRole(reader.nextString());
            }
        }
        reader.endObject();
        return user;
    }
}
