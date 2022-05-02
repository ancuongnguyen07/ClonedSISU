package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
/**
 * @author An Nguyen
 */
public class StudyModuleTest {
    String description1 = "description1";
    String description2 = "description2";
    String API1 = "API1";
    String API2 = "API2";
    int minRequire1 = 1;
    int minRequire2 = 2;
    int maxRequire1 = 3;
    int maxRequire2 = 4;
    int minCredit1 = 5;
    int maxCredit1 = 7;
    int minCredit2 = 6;
    int maxCredit2 = 8;
    String name1 = "DegreeName1";
    String name2 = "DegreeName2";
    String id1 = "adasgsgf111";
    String id2 = "asdfasfsg1231";
    String groupId1 = "abcd29@";
    String groupId2 = "aasdafq!!!";
    SubCompositeRule testComposite1 = new SubCompositeRule(minRequire1, maxRequire1, description1, false);

    SubCompositeRule testComposite2 = new SubCompositeRule(minRequire2, maxRequire2, description2, true);

    StudyModule studyModule1 = new StudyModule(name1, id1, groupId1, API1);
    StudyModule studyModule2 = new StudyModule(name2, id2, groupId2, API2);

    /**
     *
     */
    public void StudyModuleTest(){

    }

    /**
     *
     */
    @Test
    public void testSetCompositeRule(){
        System.out.println("StudyModule setCompositeRule()");
        studyModule1.setCompositeRule(testComposite1);
        studyModule2.setCompositeRule(testComposite2);
        assertEquals(testComposite1, studyModule1.getCompositeRule());
        assertEquals(testComposite2, studyModule2.getCompositeRule());
    }

    /**
     *
     */
    @Test
    public void testGetCompositeRule(){
        System.out.println("StudyModule getCompositeRule()");
        testComposite1.setMaxCredit(maxCredit1);
        testComposite1.setMinCredit(minCredit1);
        testComposite2.setMaxCredit(maxCredit2);
        testComposite2.setMinCredit(minCredit2);
        studyModule1.setCompositeRule(testComposite1);
        studyModule2.setCompositeRule(testComposite2);
        assertEquals(description1, studyModule1.getCompositeRule().getDescription());
        assertEquals(description2, studyModule2.getCompositeRule().getDescription());
        assertEquals(API1, studyModule1.getAPI());
        assertEquals(API2, studyModule2.getAPI());
        assertEquals(minRequire1, studyModule1.getCompositeRule().getMinRequire());
        assertEquals(maxRequire1, studyModule1.getCompositeRule().getMaxRequire());
        assertEquals(minRequire2, studyModule2.getCompositeRule().getMinRequire());
        assertEquals(maxRequire2, studyModule2.getCompositeRule().getMaxRequire());
        assertEquals(minCredit1, studyModule1.getCompositeRule().getMinCredit());
        assertEquals(maxCredit1, studyModule1.getCompositeRule().getMaxCredit());
        assertEquals(minCredit2, studyModule2.getCompositeRule().getMinCredit());
        assertEquals(maxCredit2, studyModule2.getCompositeRule().getMaxCredit());
        assertEquals(name1, studyModule1.getName());
        assertEquals(name2, studyModule2.getName());
        assertEquals(id1, studyModule1.getId());
        assertEquals(id2, studyModule2.getId());
        assertEquals(groupId1, studyModule1.getGroupID());
        assertEquals(groupId2, studyModule2.getGroupID());
    }
}
