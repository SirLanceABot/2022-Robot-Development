package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.drivetrain.Drivetrain;
import frc.robot.RobotContainer;

public class StopDriving implements Command 
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
    private boolean isFinished;

    // This variable is only used to simulate the robot stopping
    private boolean isRobotStoppedSimulation = false;


    // *** CLASS CONSTRUCTOR ***
    public StopDriving()
    {
        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);

        isFinished = false;
        isRobotStoppedSimulation = false;

        //TODO: add actual DRIVETRAIN methods
    }

    public void execute()
    {
        isRobotStoppedSimulation = true;
        
        if(isRobotStoppedSimulation)
        {
            System.out.println("Robot is stopped");
            isFinished = true;
        }

        //TODO: add actual DRIVETRAIN methods

        // DRIVETRAIN.stopMotor();
        isFinished = true;
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {

    }

    public String toString()
    {
        return "StopDriving()";
    }
}