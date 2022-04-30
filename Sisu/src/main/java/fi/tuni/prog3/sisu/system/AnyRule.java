/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 * The special AnyRule (having any course or any StudyModule is possible)
 * @author An Nguyen
 */
public class AnyRule {
    private String type;

    /**
     * Constructor for AnyRule to set its type
     * @param type Can be AnyCourse or AnyStudyModule
     */
    public AnyRule(String type){
        this.type = type;
    }

    /**
     * Return the type of this AnyRule
     * @return the type of this AnyRule
     */
    public String getType(){
        return this.type;
    }
}