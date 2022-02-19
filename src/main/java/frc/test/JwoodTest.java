package frc.test;

import java.lang.invoke.MethodHandles;

import frc.components.Intake2;
import frc.controls.DriverController;
import frc.controls.Xbox;
import frc.robot.RobotContainer;

public class JwoodTest implements MyTest
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
    private static final DriverController DRIVER_CONTROLLER = RobotContainer.DRIVER_CONTROLLER;
    private static final Intake2 INTAKE2 = RobotContainer.INTAKE2;


    // *** CLASS CONSTRUCTOR ***
    public JwoodTest()
    {

    }


    // *** CLASS & INSTANCE METHODS ***
    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {

    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        double armEnc = INTAKE2.getArmMotorRotations();

        System.out.println("  Enc=" + armEnc);
        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA))
        {
            INTAKE2.moveArmIn();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB))
        {
            INTAKE2.moveArmOut();
        }
        else
        {
            INTAKE2.stopArm();
        }

        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX))
        {
            INTAKE2.intakeRoller();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY))
        {
            INTAKE2.outtakeRoller();
        }
        else
        {
            INTAKE2.turnOffRoller();
        }

    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}