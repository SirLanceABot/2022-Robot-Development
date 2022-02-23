package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.drivetrain.Drivetrain;
import frc.robot.RobotContainer;

public class DriveDistance implements Command 
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
    private double speed_metersPerSecond;
    private double distance_meters;
    private boolean isFinished;

    // This variable is only used to simulate the distance driven
    private double distanceDrivenSimulation = 0.0;


    // *** CLASS CONSTRUCTOR ***
    public DriveDistance(double speed_metersPerSecond, double distance_meters)
    {
        this.speed_metersPerSecond = speed_metersPerSecond;
        this.distance_meters = distance_meters;
        isFinished = false;
    }

    
    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);

        isFinished = false;
        distanceDrivenSimulation = 0.0;

        // DRIVETRAIN.resetEncoders();
        // DRIVETRAIN.configLoopRampRate(0.75);
    }

    public void execute()
    {
        distanceDrivenSimulation += 0.05;
        System.out.printf("Distance driven: %5.2f meters\n", distanceDrivenSimulation);

        if(distanceDrivenSimulation > distance_meters)
        {
            isFinished = true;
        }

        // if(DRIVETRAIN.driveDistance(speed_metersPerSecond, distance_meters)))
        // {
        //     isFinished = true;
        // }
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {
        // DRIVETRAIN.configLoopRampRate(0.1);
    }

    public String toString()
    {
        return "DriveDistance(" + speed_metersPerSecond + ", " + distance_meters + ")";
    }
}