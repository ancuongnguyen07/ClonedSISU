package fi.tuni.prog3.sisu.system;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author An Nguyen
 */
public class APIReaderTest {
    public APIReaderTest(){

    }
    private APIReader apiReader = new APIReader();

    @Test
    public void testGetDegreeListAPI(){
        System.out.println("APIReader getDegreeListAPI()");
        assertEquals(apiReader.getDegreeListAPI(), "https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
    }

    @Test
    public void testGetDegreeDetailAPI(){
        System.out.println("APIReader getDegreeDetailAPI()");
        assertEquals(apiReader.getDegreeDetailAPI(), "https://sis-tuni.funidata.fi/kori/api/modules/");
    }

    @Test
    public void testGetStudyModuleAPI(){
        System.out.println("APIReader getStudyModuleAPI()");
        assertEquals(apiReader.getStudyModuleAPI(), "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=");
    }

    @Test
    public void testGetCourseUnitAPI(){
        System.out.println("APIReader getCourseUnitAPI()");
        assertEquals(apiReader.getCourseUnitAPI(),"https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=");
    }

    @Test
    public void testGetIdentifierTUNI(){
        System.out.println("APIReader getIdentifierTUNI()");
        assertEquals(apiReader.getIdentifierTUNI(), "&universityId=tuni-university-root-id");
    }

    @Test
    public void testConnectAPI(){
        System.out.println("APIReader connectAPI()");
        
        System.out.println("Correct API id");
        String validIdAPI = "https://sis-tuni.funidata.fi/kori/api/modules/otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b";
        assertEquals(apiReader.connectAPI(validIdAPI, "id").get("groupId").getAsString(), "uta-tohjelma-1705");
        
        System.out.println("Correct API groupId");
        String validGroupIdAPI = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-39231&universityId=tuni-university-root-id";
        assertEquals(apiReader.connectAPI(validGroupIdAPI, "groupId").get("id").getAsString(), "otm-5e96814d-3ca6-490d-b9f4-6307e974f32c");


        System.out.println("Wrong API: This will have MalformedURLException in console because the MalformedURLException is thrown but it's caught in the code so the return is null, can't asserThrows MalformedURLException here");
        String wrongAPI = "Me wrong";
        assertNull(apiReader.connectAPI(wrongAPI, "id"));

        System.out.println("Wrong Type: This will have AnException in console because the AnException is thrown but it's caught in the code so the return is null, can't asserThrows AnException here");
        assertNull(apiReader.connectAPI(validIdAPI, "no niin"));
    }

    @Test
    public void testCallAllDegrees(){
        System.out.println("APIReader callAllDegrees()");
        System.out.println("Test total degree number");
        assertEquals(269, apiReader.callAllDegrees().size());

        System.out.println("Test first degree Id");
        assertEquals("otm-87fb9507-a6dd-41aa-b924-2f15eca3b7ae", apiReader.callAllDegrees().get(0).getAsJsonObject().get("id").getAsString());

        System.out.println("Test 9th degree name");
        assertEquals("Bachelor's Programme in Science and Engineering", apiReader.callAllDegrees().get(8).getAsJsonObject().get("name").getAsString());
    }

    @Test
    public void testGetSubModuleGroupId() throws AnException{
        System.out.println("APIReader getSubModulesGroupId()");
        ArrayList<String> givenSubModulesId = new ArrayList<>();
        givenSubModulesId.add("otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff");
        givenSubModulesId.add("uta-ok-ykoodi-41176");
        givenSubModulesId.add("uta-ok-ykoodi-41177");
        givenSubModulesId.add("otm-6c36cb36-1507-44ff-baab-a30ac76ca786");
        givenSubModulesId.add("otm-35d5a7e1-71c1-456a-8783-9cf8c34262f5");
        assertEquals(givenSubModulesId, apiReader.getSubModulesGroupId("uta-tohjelma-1705"));
    }

    @Test
    public void testOnClickStudyModule() throws AnException{
        System.out.println("APIReader onClickStudyModule()");
        String studyModuleGroupId = "uta-tohjelma-1705";
        String link = apiReader.getStudyModuleAPI() + studyModuleGroupId + apiReader.getIdentifierTUNI();
        JsonObject studyModuleJson = apiReader.connectAPI(link, "groupId");
        StudyModule studyModule = apiReader.JsonToStudyModule(studyModuleJson);
        assertNull(studyModule.getCompositeRule());

        apiReader.onClickStudyModule(studyModule);
        SubCompositeRule actualRule = studyModule.getCompositeRule();
        assertNotNull(actualRule);
        assertEquals(SubCompositeRule.class, actualRule.getClass());
        assertEquals(-1,actualRule.getMinRequire());
        assertEquals(-1,actualRule.getMaxRequire());
        assertEquals(180, actualRule.getMinCredit());
        assertEquals(180, actualRule.getMaxCredit());
        assertEquals(null, actualRule.getDescription());
        assertEquals(true, actualRule.getAllMandatory());
    }

    @Test
    public void testJsonToDegreeProgram() throws AnException{
        System.out.println("APIReader JsonToDegreeProgram()");
        JsonObject testDegreeJson = apiReader.connectAPI("https://sis-tuni.funidata.fi/kori/api/modules/otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b","id");
        DegreeProgram testDegree = apiReader.JsonToDegreeProgram(testDegreeJson);
        assertEquals("otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b", testDegree.getId());
        assertEquals("uta-tohjelma-1705", testDegree.getGroupID());
        assertEquals("Bachelor's Programme in Computer Sciences", testDegree.getName());
        assertEquals("https://sis-tuni.funidata.fi/kori/api/modules/otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b", testDegree.getAPI());
    }

    @Test
    public void testJsonToStudyModule() throws AnException{
        System.out.println("APIReader JsonToStudyModule()");
        JsonObject testStudyModuleJson = apiReader.connectAPI("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id", "groupId");
        StudyModule testStudyModule = apiReader.JsonToStudyModule(testStudyModuleJson);
        apiReader.onClickStudyModule(testStudyModule);
        assertEquals(null, testStudyModule.getCompositeRule().getDescription());

        assertEquals("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=uta-ok-ykoodi-41176&universityId=tuni-university-root-id", testStudyModule.getAPI());

        assertEquals(-1, testStudyModule.getCompositeRule().getMinRequire());
        assertEquals(-1, testStudyModule.getCompositeRule().getMaxRequire());

        assertEquals(0,testStudyModule.getCompositeRule().getMinCredit());
        assertEquals(0,testStudyModule.getCompositeRule().getMaxCredit());

        assertEquals("Basic Studies in Computer Sciences", testStudyModule.getName());

        assertEquals("otm-af70be28-9bf5-49f7-b8fc-41a2bafbf2f2", testStudyModule.getId());

        assertEquals("uta-ok-ykoodi-41176", testStudyModule.getGroupID());
    }

    @Test
    public void testJsonToCourseUnit(){
        System.out.println("APIReader JsonToCourseUnit()");
        JsonObject testCourseUnitJson = apiReader.connectAPI("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-39231&universityId=tuni-university-root-id", "groupId");
        CourseUnit testCourseUnit = apiReader.JsonToCourseUnit(testCourseUnitJson);
        assertEquals(testCourseUnitJson.get("name").getAsJsonObject().get("en").getAsString(), testCourseUnit.getName());
        assertEquals(testCourseUnitJson.get("id").getAsString(), testCourseUnit.getId());
        assertEquals(testCourseUnitJson.get("groupId").getAsString(), testCourseUnit.getGroupID());
        assertEquals("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-39231&universityId=tuni-university-root-id", testCourseUnit.getAPI());
        assertEquals(testCourseUnitJson.get("content").getAsJsonObject().get("fi").getAsString(), testCourseUnit.getContent());
        assertEquals(testCourseUnitJson.get("additional").getAsJsonObject().get("fi").getAsString(), testCourseUnit.getAdditional());
        assertEquals(new JsonObject(), testCourseUnit.getLearningMaterial());
        assertEquals(testCourseUnitJson.get("substitutions").getAsJsonArray(), testCourseUnit.getSubstitutions());
        assertEquals(testCourseUnitJson.get("completionMethods").getAsJsonArray(), testCourseUnit.getCompletionMethods());
        assertEquals(testCourseUnitJson.get("code").getAsString(), testCourseUnit.getCourseCode());
        assertEquals(testCourseUnitJson.get("gradeScaleId").getAsString(), testCourseUnit.getGradeScaleId());
        assertEquals(testCourseUnitJson.get("outcomes").getAsJsonObject().get("fi").getAsString(), testCourseUnit.getOutcomes());
        assertNull(testCourseUnit.getPrerequisites());
        assertEquals(testCourseUnitJson.get("recommendedFormalPrerequisites").getAsJsonArray(), testCourseUnit.getRecommendedFormalPrerequisites());
        assertEquals(testCourseUnitJson.get("compulsoryFormalPrerequisites").getAsJsonArray(), testCourseUnit.getCompulsoryFormalPrerequisites());
        assertEquals(testCourseUnitJson.get("studyFields").getAsJsonArray(), testCourseUnit.getStudyFields());
        assertEquals(testCourseUnitJson.get("responsibilityInfos").getAsJsonArray(), testCourseUnit.getResponsibilityInfos());
        assertEquals(testCourseUnitJson.get("possibleAttainmentLanguages").getAsJsonArray(), testCourseUnit.getPossibleAttainmentLanguages());
        assertEquals(testCourseUnitJson.get("curriculumPeriodIds").getAsJsonArray(), testCourseUnit.getCurriculumPeriodIds());
        assertNull( testCourseUnit.getInclusionApplicationInstruction());
        assertEquals(testCourseUnitJson.get("credits").getAsJsonObject().get("min").getAsInt(), testCourseUnit.getMinCredit());
        assertEquals(testCourseUnitJson.get("credits").getAsJsonObject().get("max").getAsInt(), testCourseUnit.getMaxCredit());

    }

    
}
