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
        System.out.println("DriveDistance: speed=" + speed_metersPerSecond + "  distance=" + distance_meters);
        // DRIVETRAIN.resetEncoders();
        // DRIVETRAIN.configLoopRampRate(0.75);

    }

    public void execute()
    {
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
}