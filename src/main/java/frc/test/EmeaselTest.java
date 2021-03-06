package frc.test;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;
import frc.components.Shooter;

public class EmeaselTest implements MyTest
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
    // private static Shooter shooter = new Shooter(0, 1);
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;

    private static double shroudSensorInput;
    private static double shroudSensorOutput;
    private static double shroudSensorOutputAdjusted;


    // *** CLASS CONSTRUCTOR ***
    public EmeaselTest()
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
        // shroudSensorInput = RobotController.getVoltage5V();
        // shroudSensorOutput = shooter.measureShroudAngle();
        // shroudSensorOutputAdjusted = shroudSensorInput / 5.0 * shroudSensorOutput;

        // //when testing sensor output values, square ruler, sensor, and reflector, and then subtract 1.4 cm from the final reading
        // System.out.printf("%9s %1.3f", "Input:  ", shroudSensorInput);
        // System.out.println();
        // System.out.printf("%9s %1.3f", "Output:  ", shroudSensorOutput);
        // System.out.println();
        // System.out.printf("%17s %1.3f", "Adjusted Output: ", shroudSensorOutputAdjusted);
        // System.out.println();

        // // System.out.println(RobotController.getVoltage5V() + " " + shooter.measureShroudAngle());

        SHOOTER.prepareShooter(Shooter.Hub.kUpper);
        System.out.println(SHOOTER.measureFlywheelSpeed());
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   
}