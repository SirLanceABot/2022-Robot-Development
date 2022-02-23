package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.robot.RobotContainer;

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
    private static final DriverController DRIVER_CONTROLLER = RobotContainer.DRIVER_CONTROLLER;
    private static final OperatorController OPERATOR_CONTROLLER = RobotContainer.OPERATOR_CONTROLLER;

    private static final DriverControllerTab DRIVER_CONTROLLER_TAB;// = new DriverControllerTab();
    private static final OperatorControllerTab OPERATOR_CONTROLLER_TAB;// = new OperatorControllerTab();
    private static final AutonomousTab AUTONOMOUS_TAB = new AutonomousTab();


    // *** OBJECT INSTANTIATION ***
    static
    {
        // Do NOT construct these tabs if the controller is not instantiated
        DRIVER_CONTROLLER_TAB = DRIVER_CONTROLLER != null ? new DriverControllerTab() : null;
        OPERATOR_CONTROLLER_TAB = OPERATOR_CONTROLLER != null ? new OperatorControllerTab() : null;
    }
    
    
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

    public boolean wasSendDataButtonPressed()
    {
        return AUTONOMOUS_TAB.wasSendDataButtonPressed();
    }


}
