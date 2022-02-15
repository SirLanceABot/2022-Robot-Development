package frc.test;

import java.lang.invoke.MethodHandles;

//import edu.wpi.first.wpilibj.RobotController;
import frc.components.Intake;
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
    private static final Intake INTAKE = RobotContainer.INTAKE;


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
        INTAKE.configMotor(INTAKE.armsMotor);
        INTAKE.moveArmOut(25);
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