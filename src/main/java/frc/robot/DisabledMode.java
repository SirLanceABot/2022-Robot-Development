package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import frc.robot.Robot.RobotState;
import frc.shuffleboard.AutonomousTab;
import frc.shuffleboard.AutonomousTabData;
import frc.shuffleboard.MainShuffleboard;

public class DisabledMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final MainShuffleboard MAIN_SHUFFLEBOARD = RobotContainer.MAIN_SHUFFLEBOARD;
    private static final AutonomousTabData AUTONOMOUS_TAB_DATA = RobotContainer.AUTONOMOUS_TAB_DATA;
    private static final ArrayList<String> AUTONOMOUS_COMMANDS = RobotContainer.AUTONOMOUS_COMMANDS;
    
    private RobotState robotState;

    // *** CLASS CONSTRUCTOR ***
    public DisabledMode()
    {

    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        robotState = Robot.getRobotState();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if (robotState == RobotState.kDisabledAfterRobotInit)
        {
            boolean isNewData = MAIN_SHUFFLEBOARD.checkForNewAutonomousTabData();

            if (isNewData)
            {
                AUTONOMOUS_TAB_DATA.updateData(MAIN_SHUFFLEBOARD.getAutonomousTabData());

                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.startingLocation.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.orderOfOperations.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.shootCargo.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.shootDelay.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.moveOffTarmac.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.moveDelay.toString());
                AUTONOMOUS_COMMANDS.add(AUTONOMOUS_TAB_DATA.pickUpCargo.toString());
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
