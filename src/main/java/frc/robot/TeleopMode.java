package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.controls.DriverController;

public class TeleopMode implements ModeTransition
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


    // *** CLASS CONSTRUCTOR ***
    public TeleopMode()
    {

    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        DRIVER_CONTROLLER.resetRumbleCounter();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        DRIVER_CONTROLLER.checkRumbleEvent();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }
}
