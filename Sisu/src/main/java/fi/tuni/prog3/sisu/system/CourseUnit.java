/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package fi.tuni.prog3.sisu.system;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 *  The CourseUnit that represent a course
 * @author Cuong Nguyen && An Nguyen
 */
public class CourseUnit extends AbstractModule {
    //https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-39231&universityId=tuni-university-root-id
    private String content;
    private String additional;
    private JsonObject learningMaterial; // not see example yet (usually JsonNull)
    //private String teachers; // name of responsible teachers
    //private int period;
    private JsonArray substitutions;
    private JsonArray completionMethods;
    private String courseCode;
    private String gradeScaleId;
    private String outcomes;
    private String prerequisites;
    private JsonArray recommendedFormalPrerequisites;
    private JsonArray compulsoryFormalPrerequisites;
    private JsonArray studyFields;
    private JsonArray responsibilityInfos;
    private JsonArray possibleAttainmentLanguages;
    private JsonArray curriculumPeriodIds;
    private String inclusionApplicationInstruction;
    private int minCredit;
    private int maxCredit;
    
    /**
     * Constructor for CourseUnit setting all the information that the course has.
     * @param name
     * @param id
     * @param groupID
     * @param minCredit
     * @param maxCredit
     * @param API
     * @param content
     * @param additional
     * @param learningMaterial
     * @param substitutions
     * @param completionMethods
     * @param courseCode
     * @param gradeScaleId
     * @param outcomes
     * @param prerequisites
     * @param recommendedFormalPrerequisites
     * @param compulsoryFormalPrerequisites
     * @param studyFields
     * @param responsibilityInfos
     * @param possibleAttainmentLanguages
     * @param curriculumPeriodIds
     * @param inclusionApplicationInstruction
     */
    public CourseUnit(String name, String id, String groupID, int minCredit,
            int maxCredit, String API, String content, String additional, 
            JsonObject learningMaterial, JsonArray substitutions, 
            JsonArray completionMethods, String courseCode, String gradeScaleId,
            String outcomes, String prerequisites, JsonArray recommendedFormalPrerequisites, 
            JsonArray compulsoryFormalPrerequisites, JsonArray studyFields, 
            JsonArray responsibilityInfos, JsonArray possibleAttainmentLanguages,
            JsonArray curriculumPeriodIds, String inclusionApplicationInstruction ) {
        super(name, id, groupID, API);
        this.content = content;
        this.minCredit = minCredit;
        this.maxCredit = maxCredit;
        this.additional = additional;
        this.learningMaterial = learningMaterial;
        this.substitutions = substitutions;
        this.completionMethods = completionMethods;
        this.courseCode = courseCode;
        this.gradeScaleId = gradeScaleId;
        this.outcomes = outcomes;
        this.prerequisites = prerequisites;
        this.recommendedFormalPrerequisites = recommendedFormalPrerequisites;
        this.compulsoryFormalPrerequisites = compulsoryFormalPrerequisites;
        this.studyFields = studyFields;
        this.responsibilityInfos = responsibilityInfos;
        this.possibleAttainmentLanguages = possibleAttainmentLanguages;
        this.curriculumPeriodIds = curriculumPeriodIds;
        this.inclusionApplicationInstruction = inclusionApplicationInstruction;  
    }
    
    /**
     * Return the content of the course
     * @return the content of the course
     */
    public String getContent(){
        return content;
    }
    
    /**
     * Return the Additional of the course
     * @return the Additional of the course
     */    
    public String getAdditional(){
        return additional;
    }

        /**
     * Return the Learning Material of the course
     * @return the Learning Material of the course
     */
    public JsonObject getLearningMaterial(){
        return learningMaterial;
    }

        /**
     * Return the Min Credit of the course
     * @return the Min Credit of the course
     */
    public int getMinCredit() {
        return minCredit;
    }
    /**
     * Return the Max Credit of the course
     * @return the Max Credit of the course
     */
    public int getMaxCredit() {
        return maxCredit;
    }
    /**
     * Return the Substitutions of the course
     * @return the Substitutions of the course
     */        
    public JsonArray getSubstitutions(){
        return substitutions;
    }
    /**
     * Return the Completion Methods of the course
     * @return the Completion Methods of the course
     */
    public JsonArray getCompletionMethods(){
        return completionMethods;
    }
    /**
     * Return the CourseCode of the course
     * @return the CourseCode of the course
     */
    public String getCourseCode(){
        return courseCode;
    }
    /**
     * Return the Grade Scale Id of the course
     * @return the Grade Scale Id of the course
     */
    public String getGradeScaleId(){
        return gradeScaleId;
    }
    /**
     * Return the Outcomes of the course
     * @return the Outcomes of the course
     */
    public String getOutcomes(){
        return outcomes;
    }
    /**
     * Return the Prerequisites of the course
     * @return the Prerequisites of the course
     */
     public String getPrerequisites(){
        return prerequisites;
    }
    /**
     * Return the Recommended Formal Prerequisites of the course
     * @return the Recommended Formal Prerequisites of the course
     */     
     public JsonArray getRecommendedFormalPrerequisites(){
        return recommendedFormalPrerequisites;
    }    
     /**
     * Return the Compulsory Formal Prerequisites of the course
     * @return the Compulsory Formal Prerequisites of the course
     */    
    public JsonArray getCompulsoryFormalPrerequisites(){
        return compulsoryFormalPrerequisites;
    }     
    /**
     * Return the StudyFields of the course
     * @return the StudyFields of the course
     */     
     public JsonArray getStudyFields(){
        return studyFields;
    }    
    /**
     * Return the Responsibility Infos of the course
     * @return the Responsibility Infos of the course
     */     
     public JsonArray getResponsibilityInfos(){
        return responsibilityInfos;
    }    
    /**
     * Return the Possible Attainment Languages of the course
     * @return the Possible Attainment Languages of the course
     */     
     public JsonArray getPossibleAttainmentLanguages(){
        return possibleAttainmentLanguages;
    }    
    /**
     * Return the Curriculum Period Ids of the course
     * @return the Curriculum Period Ids of the course
     */     
     public JsonArray getCurriculumPeriodIds(){
        return curriculumPeriodIds;
    }  

    /**
     * Return the Inclusion Application Instruction of the course
     * @return the Inclusion Application Instruction of the course
     */     
     public String getInclusionApplicationInstruction(){
        return inclusionApplicationInstruction;
    }     
}
