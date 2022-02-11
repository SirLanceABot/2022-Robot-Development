package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.controls.DriverController;
import frc.controls.DriverController.DriverButtonAction;
import frc.shuffleboard.MainShuffleboard;

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

    private static final Intake INTAKE = RobotContainer.INTAKE;

    private static final MainShuffleboard MAIN_SHUFFLEBOARD = RobotContainer.MAIN_SHUFFLEBOARD;

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

        // Running the intake
        if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff))
        {
            if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOff)
            {
                INTAKE.intakeRoller();
            }
            else
            {
                INTAKE.turnOffRoller();
            }
        }
        else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleDirection))
        {
            if(INTAKE.getRollerDirection() == Intake.RollerDirection.kIn)
            {
                INTAKE.outtakeRoller();
            }
            else if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOut)
            {
                INTAKE.intakeRoller();
            }
        }

    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }
}
