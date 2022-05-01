package fi.tuni.prog3.sisu.system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author An Nguyen
 */
public class AnyRuleTest {
    public void AnyRuleTest(){

    }

    @Test
    public void testGetType(){
        System.out.println("AnyRule getType()");
        AnyRule rule1 = new AnyRule("CompositeRule");
        AnyRule rule2 = new AnyRule("CreditsRule");
        assertEquals("CompositeRule", rule1.getType());
        assertEquals("CreditsRule", rule2.getType());
    }
}
