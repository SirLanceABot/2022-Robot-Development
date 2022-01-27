package frc.robot;

import java.lang.invoke.MethodHandles;

public class Test 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** CLASS & INSTANCE VARIABLES ***


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    public Test()
    {

    }

    /**
     * The intializations run one time before test mode.
     */
    public void init()
    {

    }

    /**
     * The statements run periodically (every 20ms) during test mode.
     */
    public void periodic()
    {

    }

    /**
     * The statements run one time after test mode.
     */
    public void end()
    {

    }    
}
