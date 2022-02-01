package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;

public class TestMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final Intake intake = new Intake();


    // *** CLASS CONSTRUCTOR ***
    public TestMode()
    {
        
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {

    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }    
}
