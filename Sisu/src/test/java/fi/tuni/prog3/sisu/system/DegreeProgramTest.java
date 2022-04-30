package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DegreeProgramTest {
    private SubCompositeRule testCompositeFalse = new SubCompositeRule(1, 3, "description", false);

    private SubCompositeRule testCompositeTrue = new SubCompositeRule(1, 3, "description", true);
    
    public DegreeProgramTest(){

    }
    private String setCredits(){

        this.testCompositeFalse.setMaxCredit(7);
        this.testCompositeFalse.setMinCredit(5);
        this.testCompositeTrue.setMaxCredit(7);
        this.testCompositeFalse.setMinCredit(5);
        return null;
    }
    // because can't call above method directly, it must be done like this.
    private String set_str = setCredits();

    @Test
    public void testDegreeProgramConstructor(){
        System.out.println("Wrong format degree API");
        assertThrows(AnException.class, ()-> {new DegreeProgram("name", "id", "groupID", "API", testCompositeFalse);});

    }

    // @Test
    // public void testGetCompositeRule(){

    // }
}
