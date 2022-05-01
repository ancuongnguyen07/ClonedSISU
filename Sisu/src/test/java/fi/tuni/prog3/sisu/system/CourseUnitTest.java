package fi.tuni.prog3.sisu.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseUnitTest {
    private APIReader apiReader = new APIReader();
    private JsonObject learningMaterial = apiReader.connectAPI("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-39231&universityId=tuni-university-root-id", "groupId");
    private String name = "name";
    private String id = "id";
    private String groupID = "groupId";
    private String API = "AP1";
    private JsonArray mainArray = apiReader.callAllDegrees();
    private String content = "content";
    private String additional = "additional";
    private JsonArray substitutions = takeSubArr(111, 256);
    private JsonArray completionMethods = takeSubArr(11, 124);
    private String courseCode = "courseCode";
    private String gradeScaleId = "scaleID";
    private String outcomes = "learned sth";
    private String prerequisites = "nothing";
    private JsonArray recommendedFormalPrerequisites = takeSubArr(50, 123);
    private JsonArray compulsoryFormalPrerequisites = takeSubArr(211, 262);
    private JsonArray studyFields = takeSubArr(20, 70);
    private JsonArray responsibilityInfos = takeSubArr(1, 60);
    private JsonArray possibleAttainmentLanguages = takeSubArr(52, 120);
    private JsonArray curriculumPeriodIds = takeSubArr(184, 222);
    private String inclusionApplicationInstruction = "noInstruction";
    private int minCredit = 2;
    private int maxCredit = 6;
    private CourseUnit testCourseUnit = new CourseUnit(name, id, groupID, minCredit, maxCredit, API, content, additional, learningMaterial, substitutions, completionMethods, courseCode, gradeScaleId, outcomes, prerequisites, recommendedFormalPrerequisites, compulsoryFormalPrerequisites, studyFields, responsibilityInfos, possibleAttainmentLanguages, curriculumPeriodIds, inclusionApplicationInstruction);
    public void CourseUnitTest(){

    }

    private JsonArray takeSubArr(int start, int end){
        JsonArray returnArr = new JsonArray();
        for (int i = start; i<end; i++){
            returnArr.add(mainArray.get(i).getAsJsonObject());
        }
        return returnArr;
    }

    @Test
    public void testGetName(){
        System.out.println("CourseUnit getName()");
        assertEquals(name, testCourseUnit.getName());
    }

    @Test
    public void testGetId(){
        System.out.println("CourseUnit getId()");
        assertEquals(id, testCourseUnit.getId());
    }

    @Test
    public void testGetGroupID(){
        System.out.println("CourseUnit getGroupID()");
        assertEquals(groupID, testCourseUnit.getGroupID());
    }

    @Test
    public void testGetAPI(){
        System.out.println("CourseUnit getAPI()");
        assertEquals(API, testCourseUnit.getAPI());
    }

    @Test
    public void testGetContent(){
        System.out.println("CourseUnit getContent()");
        assertEquals(content, testCourseUnit.getContent());
    }

    @Test
    public void testGetAdditional(){
        System.out.println("CourseUnit getAdditional()");
        assertEquals(additional, testCourseUnit.getAdditional());
    }

    @Test
    public void testGetLearningMaterial(){
        System.out.println("CourseUnit getLearningMaterial()");
        assertEquals(learningMaterial, testCourseUnit.getLearningMaterial());
    }

    @Test
    public void testGetSubstitutions(){
        System.out.println("CourseUnit getSubstitutions()");
        assertEquals(substitutions, testCourseUnit.getSubstitutions());
    }

    @Test
    public void testGetCompletionMethods(){
        System.out.println("CourseUnit getCompletionMethods()");
        assertEquals(completionMethods, testCourseUnit.getCompletionMethods());
    }

    @Test
    public void testGetCourseCode(){
        System.out.println("CourseUnit getCourseCode()");
        assertEquals(courseCode, testCourseUnit.getCourseCode());
    }

    @Test
    public void testGetGradeScaleId(){
        System.out.println("CourseUnit getGradeScaleId()");
        assertEquals(gradeScaleId, testCourseUnit.getGradeScaleId());
    }

    @Test
    public void testGetOutcomes(){
        System.out.println("CourseUnit getOutcomes()");
        assertEquals(outcomes, testCourseUnit.getOutcomes());
    }

    @Test
    public void testGetPrerequisites(){
        System.out.println("CourseUnit getPrerequisites()");
        assertEquals(prerequisites, testCourseUnit.getPrerequisites());
    }

    @Test
    public void testGetRecommendedFormalPrerequisites(){
        System.out.println("CourseUnit getRecommendedFormalPrerequisites()");
        assertEquals(recommendedFormalPrerequisites, testCourseUnit.getRecommendedFormalPrerequisites());
    }

    @Test
    public void testGetCompulsoryFormalPrerequisites(){
        System.out.println("CourseUnit getCompulsoryFormalPrerequisites()");
        assertEquals(compulsoryFormalPrerequisites, testCourseUnit.getCompulsoryFormalPrerequisites());
    }

    @Test
    public void testGetStudyFields(){
        System.out.println("CourseUnit getStudyFields()");
        assertEquals(studyFields, testCourseUnit.getStudyFields());
    }

    @Test
    public void testGetResponsibilityInfos(){
        System.out.println("CourseUnit getResponsibilityInfos()");
        assertEquals(responsibilityInfos, testCourseUnit.getResponsibilityInfos());
    }

    @Test
    public void testGetPossibleAttainmentLanguages(){
        System.out.println("CourseUnit getPossibleAttainmentLanguages()");
        assertEquals(possibleAttainmentLanguages, testCourseUnit.getPossibleAttainmentLanguages());
    }

    @Test
    public void testGetCurriculumPeriodIds(){
        System.out.println("CourseUnit getCurriculumPeriodIds()");
        assertEquals(curriculumPeriodIds, testCourseUnit.getCurriculumPeriodIds());
    }

    @Test
    public void testGetInclusionApplicationInstruction(){
        System.out.println("CourseUnit getInclusionApplicationInstruction()");
        assertEquals(inclusionApplicationInstruction, testCourseUnit.getInclusionApplicationInstruction());
    }

    @Test
    public void testGetMinCredit(){
        System.out.println("CourseUnit getMinCredit()");
        assertEquals(minCredit, testCourseUnit.getMinCredit());
    }

    @Test
    public void testGetMaxCredit(){
        System.out.println("CourseUnit getMaxCredit()");
        assertEquals(maxCredit, testCourseUnit.getMaxCredit());
    }

    
}
