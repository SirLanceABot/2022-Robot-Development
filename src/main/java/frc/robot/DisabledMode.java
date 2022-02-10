package frc.robot;

import java.lang.invoke.MethodHandles;

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
    
    // TODO @Joel - Add a robotState class variable here
    // See the 2020 Robot Development code on github for help


    // *** CLASS CONSTRUCTOR ***
    public DisabledMode()
    {

    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        // TODO @Joel - we have use the current state of the robot to determine if we try to get new auto data
        // Get the current state from the Robot class, assign to robotState.

    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // TODO @Joel - Write an if statement to check for new auto data if it is still pregame
        // Maybe the checkForNewAutoTabData() should return a boolean, indicating if new data is available
        // Then if there is new data, call the getAutoTabData() method and store it.

    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        // TODO @Joel - Delete these statements. Add them the to if statement in the periodic() method.
        // The checkForNewAutonomousTabData() already prints out the data sent, so the output is not needed here.
        MAIN_SHUFFLEBOARD.checkForNewAutonomousTabData();
        System.out.println(MAIN_SHUFFLEBOARD.getAutonomousTabData());
    }
}
