package frc.template;

import java.lang.invoke.MethodHandles;

public class Template 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***



    // *** CLASS CONSTRUCTOR ***
    public Template()
    {

    }


    // *** CLASS & INSTANCE METHODS ***

    
}
