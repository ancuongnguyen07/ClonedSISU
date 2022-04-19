/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author Cuong Nguyen && An Nguyen
 */

public class DegreeProgram extends AbstractModule{
    // Link all degree: https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000
    // Link: https://sis-tuni.funidata.fi/kori/api/modules/otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b

    private JsonArray modules = new JsonArray();

    //for CompositeRule 

    private int minRequire;
    private int maxRequire;
    private String description;
    private boolean allMandatory;

    //for CreditRule

    private int minCredit;
    private int maxCredit;


    private String type;
    //constructor for CompositeRule
    public DegreeProgram(String name, String id, String groupID, String API,
            int minRequire, int maxRequire, String description, boolean allMandatory) {
      super(name, id, groupID, API);
      //TODO Auto-generated constructor stub
      this.type = "composite";
      this.minRequire = minRequire;
      this.maxRequire = maxRequire;
      this.description = description;
      this.allMandatory = allMandatory;

    }

    //constructor for CreditRule
    public DegreeProgram(String name, String id, String groupID, String API, int minCredit, int maxCredit) {
      super(name, id, groupID, API);
      //TODO Auto-generated constructor stub
      this.type = "credits";
      this.minCredit = minCredit;
      this.maxCredit = maxCredit;
      
    }

    public void addStudyModule(JsonObject studyModule){
      this.modules.add(studyModule);
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
