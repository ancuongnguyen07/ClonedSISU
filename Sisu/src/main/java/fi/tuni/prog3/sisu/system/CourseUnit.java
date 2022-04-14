/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;

/**
 *
 * @author Cuong Nguyen
 */
public class CourseUnit extends AbstractModule{
    public CourseUnit(String name, String id, String groupID, int minCredit, int maxCredit) {
        super(name, id, groupID, minCredit, maxCredit);
        //TODO Auto-generated constructor stub
    }
    private String description;
    private String teachers; // name of responsible teachers
    private int period;
    private String substitution; // name of substitution courses
}
