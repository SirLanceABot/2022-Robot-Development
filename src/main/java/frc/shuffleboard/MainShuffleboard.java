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
    // TODO: remove the following
    private DriverControllerTab driverControllerTab = new DriverControllerTab();
    
    // TODO: instead use the following statement, notice the new name is all caps
    // private static final DriverControllerTab DRIVER_CONTROLLER_TAB = RobotContainer.DRIVER_CONTROLLER_TAB;

    
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
       driverControllerTab.setDriverControllerAxisSettings();
    }
    
}
