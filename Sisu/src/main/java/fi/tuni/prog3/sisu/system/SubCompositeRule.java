package fi.tuni.prog3.sisu.system;

import java.util.ArrayList;

/**
 * Contains information of the rule the Degree/StudyModule have. Since Credits Rule is always followed by a CompositeRule. I merge the two and created this SubCompositeRule that has Credits when it's CreditsRule and don't when it's CompositeRule.
 * @author An Nguyen
 *  
 */
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

    /**
     * Give subCompositeRule its entities to become a CompositeRule.
     * @param minRequire
     * @param maxRequire
     * @param description
     * @param allMandatory
     */
    public SubCompositeRule(int minRequire, int maxRequire, String description, Boolean allMandatory){
        this.minRequire = minRequire;
        this.maxRequire = maxRequire;
        this.description = description;
        this.allMandatory = allMandatory;
    }
    /**
     * Return the description of the Rule
     * @return the description of the Rule
     */
    public String getDescription(){
        return description;
    }
    /**
     * Return the AllMandatory boolean of the Rule
     * @return allMandatory making all the sub arrays of the rule Mandatory.
     */
    public boolean getAllMandatory(){
        return allMandatory;
    }
    /**
     * Return the Min Require of the Rule
     * @return the Min Require of the Rule
     */
    public int getMinRequire(){
        return minRequire;
    }
    /**
     * Return the MinCredit of the CreditsRule
     * @return the MinCredit of the CreditsRule
     */
    public int getMinCredit(){
        return minCredit;
    }   
    /**
     * Return the Max Require of the Rule
     * @return the Max Require of the Rule
     */
    public int getMaxRequire(){
        return maxRequire;
    }   
    /**
     * Return the Max Credit of the CreditsRule
     * @return the Max Credit of the CreditsRule
     */
    public int getMaxCredit(){
        return maxCredit;
    }  
    /**
     * Return the sub Courses of the Rule
     * @return the sub Courses of the Rule
     */
    public ArrayList<CourseUnit> getSubCourses(){
        return subCourses;
    }
    /**
     * Return the Sub Modules of the Rule
     * @return the Sub Modules of the Rule
     */
    public ArrayList<StudyModule> getSubModules(){
        return subModules;
    }
    /**
     * Return the Any Rules of the Rule
     * @return the Any Rules of the Rule
     */
    public ArrayList<AnyRule> getAnyRules(){
        return subAnyRules;
    }
    /**
     * Return the SubComposites of the Rule since it can be nested
     * @return the SubComposites of the Rule
     */
    public ArrayList<SubCompositeRule> getSubComposites(){
        return subComposites;
    }
    /**
     * Set the max credit, only when the Rule is CreditsRule
     * @param maxCredit
     */
    public void setMaxCredit(int maxCredit){
        this.maxCredit = maxCredit;
      }
    /**
     * Set the min credit, only when the Rule is CreditsRule
     * @param minCredit
     */   
    public void setMinCredit(int minCredit){
        this.minCredit = minCredit;
    }

    /**
     * Add a course for the Rule
     * @param course
     */
    public void addCourse(CourseUnit course){
        this.subCourses.add(course);
    }

    /**
     * Add a studyModule for the Rule
     * @param module
     */
    public void addModule(StudyModule module){
        this.subModules.add(module);
    }

    /**
     * Add an AnyRule for the Rule
     * @param anyRule
     */
    public void addAnyRule(AnyRule anyRule){
        this.subAnyRules.add(anyRule);
    }
    
    /**
     * Add a subCompositeRule for the Rule
     * @param subCompositeRule
     */
    public void addSubComposite(SubCompositeRule subCompositeRule){
        this.subComposites.add(subCompositeRule);
    }
}
