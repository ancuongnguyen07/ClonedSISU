/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 * The class provide basic template for degree, study module and course unit.
 * @author Cuong Nguyen
 * @author An Nguyen
 */
public class AbstractModule {
    private String name;
    private String id;
    private String groupID;
    private String API;

    /**
     * Constructor of AbstractModule storing needed information
     * @param name name of the given Object
     * @param id id of the given Object
     * @param groupID groupID of the given Object
     * @param API API of the given Object
     */
    public AbstractModule(String name, String id, String groupID, String API) {
        this.name = name;
        this.id = id;
        this.groupID = groupID;
        this.API = API;
    }

    /**
     * Returns the name of the Object
     * @return the name of the Object
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the id of the Object
     * @return the id of the Object
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the groupId of the Object
     * @return the groupId of the Object
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * Returns the API of the Object
     * @return the API of the Object
     */
    public String getAPI(){
        return API;
    }
}
