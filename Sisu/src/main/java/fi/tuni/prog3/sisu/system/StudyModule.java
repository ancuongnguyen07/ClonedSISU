/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;

/**
 *
 * @author Cuong Nguyen && An Nguyen 
 */
public class StudyModule extends AbstractModule{
    // Link: https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id 
    //for CompositeRule 
    private int minRequire;
    private int maxRequire;
    private String description;
    private boolean allMandatory;

    //for CreditRule
    private int minCredit;
    private int maxCredit;
    private String type;

    //for submodules and courses and anyRules
    private ArrayList<AnyRule> anyRules = new ArrayList<>();
    private ArrayList<StudyModule> submodules = new ArrayList<>(); 
    private ArrayList<CourseUnit> subCourses = new ArrayList<>();
  //constructor for CompositeRule
  public StudyModule(String name, String id, String groupID, String API, int minRequire, int maxRequire, String description, boolean allMandatory) {
    super(name, id, groupID, API);
    //TODO Auto-generated constructor stub
    this.type = "composite";
    this.minRequire = minRequire;
    this.maxRequire = maxRequire;
    this.description = description;
    this.allMandatory = allMandatory;
    
  }

  public StudyModule(String name, String id, String groupID, String API, int minCredit, int maxCredit) {
    super(name, id, groupID, API);
    //TODO Auto-generated constructor stub
    this.type = "credits";
    this.minCredit = minCredit;
    this.maxCredit = maxCredit;

  }
  
  public void addAnyRule(AnyRule anyRule){
    this.anyRules.add(anyRule);
  }

  public void addSubModule(StudyModule subModule){
    this.submodules.add(subModule);
  }

  public void addSubCourse(CourseUnit courseUnit){
    this.subCourses.add(courseUnit);
  }
  
      public String getDescription(){
        return description;
    }
    
    public boolean getAllMandatory(){
        return allMandatory;
    }
    
    public int getMinRequire(){
        return minRequire;
    }
    
     public int getMinCredit(){
        return minCredit;
    }   
    
     public int getMaxRequire(){
        return maxRequire;
    }   
    
    public int getMaxCredit(){
        return maxCredit;
    }      
}
