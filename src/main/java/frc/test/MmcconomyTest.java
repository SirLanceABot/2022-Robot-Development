package frc.test;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner6;

import java.util.concurrent.TimeUnit;


//import edu.wpi.first.wpilibj.RobotController;
import frc.components.Intake;
import frc.components.Climber;
import frc.robot.RobotContainer;



public class MmcconomyTest implements MyTest
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***

    

    // *** CLASS & INSTANCE VARIABLES ***
    //private static final Intake INTAKE = RobotContainer.INTAKE;
    private static final Climber CLIMBER = RobotContainer.CLIMBER;


    // *** CLASS CONSTRUCTOR ***
    public MmcconomyTest()
    {

    }


    // *** CLASS & INSTANCE METHODS ***
    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        try
        {
            System.out.println("Starting");
            //TimeUnit.SECONDS.sleep(2);   
            CLIMBER.climbUp();
            TimeUnit.SECONDS.sleep(2);
            System.out.println(CLIMBER.getFCLposition());
            CLIMBER.shutDown();
            TimeUnit.SECONDS.sleep(2);
            CLIMBER.climbDown();
            TimeUnit.SECONDS.sleep(2);
            CLIMBER.shutDown();
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {

    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}