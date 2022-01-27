package frc.robot;

import java.lang.invoke.MethodHandles;

public class Disabled 
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
    public Disabled()
    {

    }

    /**
     * The intializations run one time before disabled.
     */
    public void init()
    {

    }

    /**
     * The statements run periodically (every 20ms) during disabled.
     */
    public void periodic()
    {

    }

    /**
     * The statements run one time after disabled.
     */
    public void end()
    {

    }
}
