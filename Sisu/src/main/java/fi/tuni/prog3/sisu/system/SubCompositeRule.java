package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;

public class SubCompositeRule {
    private int minRequire;
    private int maxRequire;
    private String description;
    private Boolean allMandatory;


    private int minCredit;
    private int maxCredit;

    private ArrayList<CourseUnit> subCourses = new ArrayList<>();
    private ArrayList<StudyModule> subModules = new ArrayList<>();
    private ArrayList<AnyRule> subAnyRules = new ArrayList<>();
    private ArrayList<SubCompositeRule> subComposites= new ArrayList<>();

    public SubCompositeRule(int minRequire, int maxRequire, String description, Boolean allMandatory){
        this.minRequire = minRequire;
        this.maxRequire = maxRequire;
        this.description = description;
        this.allMandatory = allMandatory;
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

    public ArrayList<CourseUnit> getSubCourses(){
        return subCourses;
    }

    public ArrayList<StudyModule> getSubModules(){
        return subModules;
    }

    public ArrayList<AnyRule> getAnyRules(){
        return subAnyRules;
    }

    public ArrayList<SubCompositeRule> getSubComposites(){
        return subComposites;
    }

    public void setMaxCredit(int maxCredit){
        this.maxCredit = maxCredit;
      }
    
    public void setMinCredit(int minCredit){
        this.minCredit = minCredit;
    }

    public void addCourse(CourseUnit course){
        this.subCourses.add(course);
    }

    public void addModule(StudyModule module){
        this.subModules.add(module);
    }

    public void addAnyRule(AnyRule anyRule){
        this.subAnyRules.add(anyRule);
    }
    
    public void addSubComposite(SubCompositeRule subCompositeRule){
        this.subComposites.add(subCompositeRule);
    }
}
