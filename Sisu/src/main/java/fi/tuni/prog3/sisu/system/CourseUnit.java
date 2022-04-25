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
    

    public CourseUnit(String name, String id, String groupID, int minCredit, int maxCredit, String API, String content, String additional, JsonObject learningMaterial, JsonArray substitutions, JsonArray completionMethods, String courseCode, String gradeScaleId, String outcomes, String prerequisites, JsonArray recommendedFormalPrerequisites, JsonArray compulsoryFormalPrerequisites, JsonArray studyFields, JsonArray responsibilityInfos, JsonArray possibleAttainmentLanguages, JsonArray curriculumPeriodIds, String inclusionApplicationInstruction ) {
        super(name, id, groupID, API);
        this.content = content;
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
    
    public String getContent(){
        return content;
    }
    
    public String getAdditional(){
        return additional;
    }

    public JsonObject getLearningMaterial(){
        return learningMaterial;
    }

    public JsonArray getSubstitutions(){
        return substitutions;
    }

    public JsonArray getCompletionMethods(){
        return completionMethods;
    }

    public String getCourseCode(){
        return courseCode;
    }

    public String getGradeScaleId(){
        return gradeScaleId;
    }

    public String getOutcomes(){
        return outcomes;
    }

     public String getPrerequisites(){
        return prerequisites;
    }
     
     public JsonArray getRecommendedFormalPrerequisites(){
        return recommendedFormalPrerequisites;
    }    
     
    public JsonArray getCompulsoryFormalPrerequisites(){
        return compulsoryFormalPrerequisites;
    }     
     
     public JsonArray getStudyFields(){
        return studyFields;
    }    
     
     public JsonArray getResponsibilityInfos(){
        return responsibilityInfos;
    }    
     
     public JsonArray getPossibleAttainmentLanguages(){
        return possibleAttainmentLanguages;
    }    
     
     public JsonArray getCurriculumPeriodIds(){
        return curriculumPeriodIds;
    }    
     
     public String getInclusionApplicationInstruction(){
        return inclusionApplicationInstruction;
    }     
}
