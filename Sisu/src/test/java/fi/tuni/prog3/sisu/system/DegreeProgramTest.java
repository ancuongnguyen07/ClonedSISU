package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author An Nguyen
 */
public class DegreeProgramTest {
    
    public DegreeProgramTest(){

    }

    @Test
    public void testGetCompositeRule(){
        System.out.println("Test getCompositeRule of degree");
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
        String degreeName1 = "DegreeName1";
        String degreeName2 = "DegreeName2";
        String id1 = "adasgsgf111";
        String id2 = "asdfasfsg1231";
        String groupId1 = "abcd29@";
        String groupId2 = "aasdafq!!!";
        SubCompositeRule testComposite1 = new SubCompositeRule(minRequire1, maxRequire1, description1, false);

        SubCompositeRule testComposite2 = new SubCompositeRule(minRequire2, maxRequire2, description2, true);
    
        DegreeProgram testDegree1 = new DegreeProgram(degreeName1, id1, groupId1, API1, testComposite1);
    
        DegreeProgram testDegree2 = new DegreeProgram(degreeName2, id2, groupId2, API2, testComposite2);

        
        testComposite1.setMaxCredit(maxCredit1);
        testComposite1.setMinCredit(minCredit1);
        testComposite2.setMaxCredit(maxCredit2);
        testComposite2.setMinCredit(minCredit2);
        assertEquals(description1, testDegree1.getCompositeRule().getDescription());
        assertEquals(description2, testDegree2.getCompositeRule().getDescription());
        assertEquals(API1, testDegree1.getAPI());
        assertEquals(API2, testDegree2.getAPI());
        assertEquals(minRequire1, testDegree1.getCompositeRule().getMinRequire());
        assertEquals(maxRequire1, testDegree1.getCompositeRule().getMaxRequire());
        assertEquals(minRequire2, testDegree2.getCompositeRule().getMinRequire());
        assertEquals(maxRequire2, testDegree2.getCompositeRule().getMaxRequire());
        assertEquals(minCredit1, testDegree1.getCompositeRule().getMinCredit());
        assertEquals(maxCredit1, testDegree1.getCompositeRule().getMaxCredit());
        assertEquals(minCredit2, testDegree2.getCompositeRule().getMinCredit());
        assertEquals(maxCredit2, testDegree2.getCompositeRule().getMaxCredit());
        assertEquals(degreeName1, testDegree1.getName());
        assertEquals(degreeName2, testDegree2.getName());
        assertEquals(id1, testDegree1.getId());
        assertEquals(id2, testDegree2.getId());
        assertEquals(groupId1, testDegree1.getGroupID());
        assertEquals(groupId2, testDegree2.getGroupID());

    }
}
