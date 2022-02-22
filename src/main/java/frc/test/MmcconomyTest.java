package frc.test;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner6;

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
        //INTAKE.TestRoller();
        // INTAKE.moveArmOut();
        // INTAKE.moveArmIn();
        CLIMBER.grabSecondRung();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(CLIMBER.getGamer().equals("Stage2") && CLIMBER.getFCLposition() >= 2.3)
        {
            System.out.println("STAGE 2 COMPLETE!!!");
            CLIMBER.setGamer("");
            CLIMBER.setMotorSpeed(CLIMBER.firstStageClimbMotorLeader, 0.0);
        }
        else
        {
            //System.out.println(CLIMBER.getFCLposition());
        }
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}