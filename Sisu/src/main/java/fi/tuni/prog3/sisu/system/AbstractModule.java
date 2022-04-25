/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 *
 * @author Cuong Nguyen && An Nguyen
 */
public class AbstractModule {
    private String name;
    private String id;
    private String groupID;
    private String API;

    public AbstractModule(String name, String id, String groupID, String API) {
        this.name = name;
        this.id = id;
        this.groupID = groupID;
        this.API = API;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getAPI(){
        return API;
    }
}
