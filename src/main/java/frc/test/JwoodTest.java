package frc.test;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

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

    // private static final CANSparkMax leftMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private static final CANSparkMax rightMotor = new CANSparkMax(7, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);


    // *** CLASS CONSTRUCTOR ***
    public JwoodTest()
    {
        // leftMotor.restoreFactoryDefaults();
        // leftMotor.setIdleMode(IdleMode.kCoast);
        // rightMotor.restoreFactoryDefaults();
        // rightMotor.setIdleMode(IdleMode.kCoast);
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

        // leftMotor.set(DRIVER_CONTROLLER.getRawAxis(Xbox.Axis.kLeftX));
        // rightMotor.set(DRIVER_CONTROLLER.getRawAxis(Xbox.Axis.kLeftX));

        // double armEnc = INTAKE.getArmMotorRotations();

        // // System.out.println("  Enc=" + armEnc);
        // if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA))
        // {
        //     INTAKE.moveArmIn();
        // }
        // else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB))
        // {
        //     INTAKE.moveArmOut();
        // }
        // else
        // {
        //     INTAKE.stopArm();
        // }

        // if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX))
        // {
        //     INTAKE.intakeRoller();
        // }
        // else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY))
        // {
        //     INTAKE.outtakeRoller();
        // }
        // else
        // {
        //     INTAKE.turnOffRoller();
        // }


    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}