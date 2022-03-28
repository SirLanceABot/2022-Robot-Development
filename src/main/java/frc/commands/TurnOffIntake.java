package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.robot.RobotContainer;
import frc.components.ShuttleFSM;

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
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private boolean isMoveArmIn;
    private boolean isFinished;

    // This variable is only used to simulate the intake being off
    // private boolean isIntakeOff = false;


    // *** CLASS CONSTRUCTOR ***
    public TurnOffIntake(boolean isMoveArmIn)
    {
        this.isMoveArmIn = isMoveArmIn;

        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);

        isFinished = false;
        // isIntakeOff = false;
    }

    public void execute()
    {
        SHUTTLEFSM.fancyRun(false);

        INTAKE.turnOffRoller();

        if (isMoveArmIn)
        {
            INTAKE.pMoveArmIn();

            if(INTAKE.isArmIn())
            {
                System.out.println("Intake is off");
                isFinished = true;
            }
        }
        else
        {
            System.out.println("Intake is off");
            isFinished = true;
        }
        
        
        // isIntakeOff = true;

        // if(isIntakeOff)
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
        return "TurnOffIntake()";
    }
}