package fi.tuni.prog3.sisu.system;


/**
 * The exception class mostly used for Type error in APIReader.java
 * @author An Nguyen 
 */
public class AnException extends Exception {
    /**
     * Constructor of AnException 
     * @param message The print out error message
     */
    public AnException(String message){
        super(message);
    }
    
}
