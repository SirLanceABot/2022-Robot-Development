package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.robot.RobotContainer;
import frc.components.ShuttleFSM;

public class TurnOnIntake implements Command 
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
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private boolean isFinished;

    // This variable is only used to simulate the intake being on
    // private boolean isIntakeOn = false;


    // *** CLASS CONSTRUCTOR ***
    public TurnOnIntake()
    {
        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);



        isFinished = false;
        // isIntakeOn = false;
    }

    public void execute()
    {
        SHUTTLEFSM.fancyRun(false);

        INTAKE.moveArmOut();

        if (INTAKE.isArmOut())
        {
            INTAKE.intakeRoller();

            System.out.println("Intake is on");
            isFinished = true;
        }
        
        // isIntakeOn = true;

        // if(isIntakeOn)
        // {
            
        // }
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
        return "TurnOnIntake()";
    }
}