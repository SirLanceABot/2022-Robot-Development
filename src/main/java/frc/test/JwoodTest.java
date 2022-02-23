package frc.test;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
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
    private static final Intake INTAKE = RobotContainer.INTAKE;


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
        double armEnc = INTAKE.getArmMotorRotations();

        System.out.println("  Enc=" + armEnc);
        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA))
        {
            INTAKE.moveArmIn();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB))
        {
            INTAKE.moveArmOut();
        }
        else
        {
            INTAKE.stopArm();
        }

        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX))
        {
            INTAKE.intakeRoller();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY))
        {
            INTAKE.outtakeRoller();
        }
        else
        {
            INTAKE.turnOffRoller();
        }

    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}