/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

import java.util.HashMap;

/**
 *
 * @author Cuong Nguyen
 */
public class AbstractModule {
    private String name;
    private String id;
    private String groupID;
    private int minCredit;
    private int maxCredit;
    private HashMap<String, AbstractModule> subModules;

    public AbstractModule(String name, String id, String groupID, int minCredit, int maxCredit) {
        this.name = name;
        this.id = id;
        this.groupID = groupID;
        this.minCredit = minCredit;
        this.maxCredit = maxCredit;
        this.subModules = new HashMap<>();
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

    public int getMinCredit() {
        return minCredit;
    }

    public int getMaxCredit() {
        return maxCredit;
    }

    public HashMap<String, AbstractModule> getSubModules() {
        return subModules;
    }
    
    public void addSubModule(String id, AbstractModule submodule){
        this.subModules.put(id, submodule);
    }
}
