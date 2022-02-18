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

    // *** INNER ENUMS and INNER CLASSES ***

    

    // *** CLASS & INSTANCE VARIABLES ***



    // *** CLASS CONSTRUCTOR ***
    public Template()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    
}
