package frc.test;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.Joystick;

import java.util.concurrent.TimeUnit;


//import edu.wpi.first.wpilibj.RobotController;
import frc.components.Intake;
import frc.components.Climber;
import frc.robot.RobotContainer;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.controls.DriverController.DriverDpadAction;
import frc.controls.OperatorController.OperatorDpadAction;



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
    // private static final Climber CLIMBER = RobotContainer.CLIMBER;
    private static final DriverController JOYSTICK = new DriverController(0);
    // private static final OperatorController NOTJOYSTICK = new OperatorController(1);


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
        // INTAKE.compressorDisable();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // if(NOTJOYSTICK.getAction(OperatorDpadAction.kLED))
        // {
        //     System.out.println("Up");
        // }
        // else if(NOTJOYSTICK.getAction(OperatorDpadAction.DownMethod))
        // {
        //     System.out.println("Down");
        // }
        // else if(NOTJOYSTICK.getAction(OperatorDpadAction.UpRightMethod))
        // {
        //     System.out.println("Up Right");
        // }

        // if(JOYSTICK.getAction(DriverDpadAction.UpMethod))
        // {
        //     System.out.println("Up");
        // }
        // else if(JOYSTICK.getAction(DriverDpadAction.kLockSwerve))
        // {
        //     System.out.println("Down");
        // }
        // else if(JOYSTICK.getAction(DriverDpadAction.UpRightMethod))
        // {
        //     System.out.println("Up Right");
        // }

        if(JOYSTICK.getRawButton(3))
        {
            INTAKE.compressorDisable();
            // CLIMBER.setMoveOff();
        }
        else if(JOYSTICK.getRawButton(4))
        {
            INTAKE.compressorEnable();
            // System.out.println(CLIMBER.getMovementType());
        }
        else
        {
            
        }
        
        if(JOYSTICK.getRawButton(1))
        {
            INTAKE.pMoveArmIn();
            // CLIMBER.FCLArmUp();
        }
        else if(JOYSTICK.getRawButton(2))
        {
            INTAKE.pMoveArmOut();
            // CLIMBER.FCLArmDown();
        }
        else
        {
            INTAKE.pMoveArmOff();
            // CLIMBER.FCLShutDown();
        }

        if(JOYSTICK.getRawButton(5))
        {
            // System.out.println(CLIMBER.getFCLposition());
            System.out.println("Is Arm Out: " + INTAKE.isArmOut() + " \nIs Arm In: " + INTAKE.isArmIn());
            // INTAKE.armInfo();
        }
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}