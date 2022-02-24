package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.robot.RobotContainer;

public class TurnOffIntake implements Command 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final Intake INTAKE = RobotContainer.INTAKE;
    private boolean isFinished;

    // This variable is only used to simulate the intake being off
    private boolean isIntakeOff = false;


    // *** CLASS CONSTRUCTOR ***
    public TurnOffIntake()
    {
        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);

        isFinished = false;
        isIntakeOff = false;

        //TODO: add actual INTAKE methods
    }

    public void execute()
    {
        isIntakeOff = true;

        if(isIntakeOff)
        {
            System.out.println("Intake is off");
            isFinished = true;
        }

        // TODO: add actual INTAKE methods
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
        return "TurnOffIntake()";
    }
}