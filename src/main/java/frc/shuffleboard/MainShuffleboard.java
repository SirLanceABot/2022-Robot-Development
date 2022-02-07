package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

public class MainShuffleboard 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final DriverControllerTab DRIVER_CONTROLLER_TAB = new DriverControllerTab();
    private static final OperatorControllerTab OPERATOR_CONTROLLER_TAB = new OperatorControllerTab();


    
    // *** CLASS CONSTRUCTOR ***
    public MainShuffleboard()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    
    // *** CLASS & INSTANCE METHODS ***
    // DRIVER CONTROLLER TAB
    public void setDriverControllerSettings()
    {
       DRIVER_CONTROLLER_TAB.setDriverControllerAxisSettings();
    }
    
    public void setOperatorControllerSettings()
    {
        OPERATOR_CONTROLLER_TAB.setOperatorControllerAxisSettings();
    }
}
