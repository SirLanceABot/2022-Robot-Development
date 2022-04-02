package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;
import frc.drivetrain.Drivetrain;

public class RotateToAngle implements Command 
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
    private double angle;
    private boolean isFinished;


    // *** CLASS CONSTRUCTOR ***
    public RotateToAngle(double angle)
    {
        this.angle = angle;

        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        // DRIVETRAIN.rotateToAngle(angle);

        System.out.println(this);
        isFinished = false;
    }

    public void execute()
    {
        // if(DRIVETRAIN.isAtAngle(angle))
        {
            System.out.println("Angle is correct");
            isFinished = true;
        }
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
        return "RotateToAngle()";
    }
}