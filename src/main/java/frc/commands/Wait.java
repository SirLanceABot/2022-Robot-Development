package frc.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.components.Intake;
import frc.components.ShuttleFSM;

public class Wait implements Command 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private Timer timer = new Timer();
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private double waitTime_seconds;
    private boolean isFinished;
    private static final Intake INTAKE = RobotContainer.INTAKE;
    

    // *** CLASS CONSTRUCTOR ***
    public Wait(double waitTime_seconds)
    {
        this.waitTime_seconds = waitTime_seconds;
        isFinished = false;
    }


    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);

        isFinished = false;
        timer.stop();
        timer.reset();
        timer.start();
    }

    public void execute()
    {
        INTAKE.pMoveArmFloat();

        SHUTTLEFSM.fancyRun(false);

        System.out.printf("Wait time: %5.2f seconds\n", timer.get());

        if(timer.get() > waitTime_seconds)
        {
            isFinished = true;
        }
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {
        timer.stop();
        timer.reset();
    }

    public String toString()
    {
        return "Wait(" + waitTime_seconds + ")";
    }

}