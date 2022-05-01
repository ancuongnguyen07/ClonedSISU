package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author An Nguyen
 */
public class AbstractModuleTest {
    private String API1 = "API1";
    private String API2 = "API2";
    private String name1 = "DegreeName1";
    private String name2 = "DegreeName2";
    private String id1 = "adasgsgf111";
    private String id2 = "asdfasfsg1231";
    private String groupId1 = "abcd29@";
    private String groupId2 = "aasdafq!!!";
    AbstractModule testModule1 = new AbstractModule(name1, id1, groupId1, API1);
    AbstractModule testModule2 = new AbstractModule(name2, id2, groupId2, API2);
    public void AbstractModule(){

    }

    @Test
    public void testGetName(){
        System.out.println("AbstractModule getName()");
        assertEquals(name1, testModule1.getName());
        assertEquals(name2, testModule2.getName());
    }

    @Test 
    public void testGetId(){
        System.out.println("AbstractModule getId()");
        assertEquals(id1, testModule1.getId());
        assertEquals(id2, testModule2.getId());
    }

    @Test
    public void testGetGroupID(){
        System.out.println("AbstractModule getGroupID()");
        assertEquals(groupId1, testModule1.getGroupID());
        assertEquals(groupId2, testModule2.getGroupID());
    }

    @Test
    public void testGetAPI(){
        System.out.println("AbstractModule getAPI()");
        assertEquals(API1, testModule1.getAPI());
        assertEquals(API2, testModule2.getAPI());
    }

}
