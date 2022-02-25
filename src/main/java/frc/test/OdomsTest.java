package frc.test;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;

import frc.vision.AcquireHubImage;
import frc.vision.Vision;

public class OdomsTest implements MyTest
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
    private static AcquireHubImage target;
    private static Thread targetThread;


    // *** CLASS CONSTRUCTOR ***
    public OdomsTest()
    {
        //private static final Intake INTAKE = RobotContainer.INTAKE;
    }


    // *** CLASS & INSTANCE METHODS ***
    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        
         // start thread for target camera and locate target
        target = new AcquireHubImage();
        targetThread = new Thread(target, "TargetCamera");
        targetThread.setDaemon(true);
        targetThread.start();

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
