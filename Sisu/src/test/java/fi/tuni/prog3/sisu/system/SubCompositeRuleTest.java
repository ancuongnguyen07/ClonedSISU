package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

public class SubCompositeRuleTest {
    private SubCompositeRule testRule = new SubCompositeRule(1, 3, "description", true);

    public void SubCompositeRule(){

    }

    @Test
    public void testGetDescription(){
        System.out.println("SubCompositeRule getDescription()");
        assertEquals("description", testRule.getDescription());
    }

    @Test
    public void testGetMinRequire(){
        System.out.println("SubCompositeRule getMinRequire()");
        assertEquals(1, testRule.getMinRequire());
    }

    @Test
    public void testMaxRequire(){
        System.out.println("SubCompositeRule getMaxRequire()");
        assertEquals(3, testRule.getMaxRequire());
    }

    @Test
    public void testGetAllMandatory(){
        System.out.println("SubCompositeRule getAllMandatory()");
        assertEquals(true, testRule.getAllMandatory());
    }

    @Test 
    public void testSetMaxCredit(){
        System.out.println("SubCompositeRule setMaxCredit()");
        assertEquals(0,testRule.getMaxCredit());
        testRule.setMaxCredit(5);
        assertEquals(5, testRule.getMaxCredit());
    }

    @Test
    public void testSetMinCredit(){
        System.out.println("SubCompositeRule setMinCredit()");
        assertEquals(0, testRule.getMinCredit());
        testRule.setMinCredit(2);
        assertEquals(2, testRule.getMinCredit());
    }

    @Test
    public void testGetMinCredit(){
        System.out.println("SubCompositeRule getMinCredit()");
        assertEquals(0, testRule.getMinCredit());
        testRule.setMinCredit(2);
        assertEquals(2, testRule.getMinCredit());
        testRule.setMinCredit(5);
        assertEquals(5, testRule.getMinCredit());
    }

    @Test
    public void testGetMaxCredit(){
        System.out.println("SubCompositeRule getMaxCredit()");
        assertEquals(0, testRule.getMaxCredit());
        testRule.setMaxCredit(2);
        assertEquals(2, testRule.getMaxCredit());
        testRule.setMaxCredit(5);
        assertEquals(5, testRule.getMaxCredit());
    }

    @Test
    public void testAddGetCourse(){
        System.out.println("SubCompositeRule addCourse() getSubCourses()");
        assertEquals(0, testRule.getSubCourses().size());
        CourseUnit testCourse = new CourseUnit("name", "id", "groupID", 1, 3, "API", "content", "additional", new JsonObject(), new JsonArray(), new JsonArray(), "courseCode", "gradeScaleId", "outcomes", "prerequisites", new JsonArray(), new JsonArray(), new JsonArray(), new JsonArray(), new JsonArray(), new JsonArray(), "inclusionApplicationInstruction");
        testRule.addCourse(testCourse);
        assertEquals(1, testRule.getSubCourses().size());
        assertEquals(testCourse, testRule.getSubCourses().get(0));
    }

    @Test
    public void testAddGetSubModule(){
        System.out.println("SubCompositeRule addStudyModule() getSubModules()");
        assertEquals(0, testRule.getSubModules().size());
        StudyModule testModule = new StudyModule("name", "id", "groupID", "API");
        testRule.addModule(testModule);
        assertEquals(1, testRule.getSubModules().size());
        assertEquals(testModule, testRule.getSubModules().get(0));
    }

    @Test
    public void testAddGetAnyRules(){
        System.out.println("SubCompositeRule addAnyRule() getAnyRules()");
        assertEquals(0, testRule.getAnyRules().size());
        AnyRule testAnyRule = new AnyRule("type");
        testRule.addAnyRule(testAnyRule);
        assertEquals(1, testRule.getAnyRules().size());
        assertEquals(testAnyRule, testRule.getAnyRules().get(0));
    }

    @Test
    public void testAddGetSubComposite(){
        System.out.println("SubCompositeRule addSubComposite() getSubComposites()");
        assertEquals(0, testRule.getSubComposites().size());
        SubCompositeRule subComposite = new SubCompositeRule(5, 8, "description", false);
        testRule.addSubComposite(subComposite);
        assertEquals(1, testRule.getSubComposites().size());
        assertEquals(subComposite, testRule.getSubComposites().get(0));
    }
}
