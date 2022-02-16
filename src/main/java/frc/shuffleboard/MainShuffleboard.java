package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

// import frc.robot.RobotContainer;

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
    // private static final DriverControllerTab DRIVER_CONTROLLER_TAB = RobotContainer.DRIVER_CONTROLLER_TAB;
    // private static final OperatorControllerTab OPERATOR_CONTROLLER_TAB = RobotContainer.OPERATOR_CONTROLLER_TAB;
    // private static final AutonomousTab AUTONOMOUS_TAB = RobotContainer.AUTONOMOUS_TAB;
    private static final DriverControllerTab DRIVER_CONTROLLER_TAB = new DriverControllerTab();
    private static final OperatorControllerTab OPERATOR_CONTROLLER_TAB = new OperatorControllerTab();
    private static final AutonomousTab AUTONOMOUS_TAB = new AutonomousTab();


    
    // *** CLASS CONSTRUCTOR ***
    public MainShuffleboard()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    
    // *** CLASS & INSTANCE METHODS ***
    //-------------------------------------------------------------------//
    // DRIVER CONTROLLER TAB
    public void setDriverControllerSettings()
    {
       DRIVER_CONTROLLER_TAB.setDriverControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // OPERATOR CONTROLLER TAB
    public void setOperatorControllerSettings()
    {
        OPERATOR_CONTROLLER_TAB.setOperatorControllerAxisSettings();
    }

    //-------------------------------------------------------------------//
    // AUTONOMOUS TAB
    public AutonomousTabData getAutonomousTabData()
    {
        return AUTONOMOUS_TAB.getAutonomousTabData();
    }

    public boolean checkForNewAutonomousTabData()
    {
        return AUTONOMOUS_TAB.isThereNewAutonomousTabData();
    }


}
