package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.commands.AutonomousCommandList;
import frc.drivetrain.Drivetrain;
import frc.shuffleboard.CameraTab;

public class AutonomousMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    private static final AutonomousCommandList AUTONOMOUS_COMMAND_LIST = RobotContainer.AUTONOMOUS_COMMAND_LIST;
    private static final CameraTab CAMERA_TAB = RobotContainer.CAMERA_TAB;
    private static final Climber CLIMBER = RobotContainer.Climber;


    // *** CLASS CONSTRUCTOR ***
    public AutonomousMode()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        Climber.setKOff();
        if(CAMERA_TAB != null)
        {
            CAMERA_TAB.updateLimeLightMode();
        }

        if(AUTONOMOUS_COMMAND_LIST != null)
        {
            AUTONOMOUS_COMMAND_LIST.init();
        }

        if(DRIVETRAIN != null)
        {
            DRIVETRAIN.resetOdometry();
        }
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(AUTONOMOUS_COMMAND_LIST != null)
        {
            AUTONOMOUS_COMMAND_LIST.execute();
        }
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        if(AUTONOMOUS_COMMAND_LIST != null)
        {
            AUTONOMOUS_COMMAND_LIST.end();
        }
    }
}
