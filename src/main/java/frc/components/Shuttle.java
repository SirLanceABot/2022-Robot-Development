package frc.components;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.constants.Constant;

public class Shuttle 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // initializing motors
    private final CANSparkMax stageOneMotor;// = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_ONE, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax stageTwoMotor;// = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_TWO, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final double FIRST_STAGE_SPEED = 0.5;
    private static final double SECOND_STAGE_SPEED = 0.5;

    // initializing sensors
    private final DigitalInput intakeSensor;// = new DigitalInput(Port.Sensor.SHUTTLE_INTAKE_SENSOR);
    private final DigitalInput stageOneSensor;// = new DigitalInput(Port.Sensor.SHUTTLE_STAGE_ONE_SENSOR);
    private final DigitalInput stageTwoSensor;// = new DigitalInput(Port.Sensor.SHUTTLE_STAGE_TWO_SENSOR);

    // initializing encoders
    // private static CANEncoder stageOneMotorEncoder = stageOneMotor.getEncoder();
    // private static CANEncoder stageTwoMotorEncoder = stageTwoMotor.getEncoder();

    // Used for debouncing methods in here
    // private int intakeSensorActivateCount = 0;
    // private int intakeSensorDeactivateCount = 0;
    // private boolean debouncedIntakeSensor = false;
    // private int stageOneSensorActivateCount = 0;
    // private int stageOneSensorDeactivateCount = 0;
    // private boolean debouncedStageOneSensor = false;
    // private int stageTwoSensorActivateCount = 0;
    // private int stageTwoSensorDeactivateCount = 0;
    // private boolean debouncedStageTwoSensor = false;


    // *** CLASS CONSTRUCTOR ***
    // TODO: remove the public access modifier so that the constructor can only be accessed inside the package
    public Shuttle(ShuttleConfig sd)
    {
        System.out.println(fullClassName + " : Constructor Started");

        // Testing version
        // stageOneMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        // stageTwoMotor = new CANSparkMax(7, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

        stageOneMotor = new CANSparkMax(sd.stageOneMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        stageTwoMotor = new CANSparkMax(sd.stageTwoMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

        intakeSensor = new DigitalInput(sd.intakeSensorPort);
        stageOneSensor = new DigitalInput(sd.stageOneSensorPort);
        stageTwoSensor = new DigitalInput(sd.stageTwoSensorPort);

        configStageOneMotor();
        configStageTwoMotor();

        System.out.println(fullClassName + ": Constructor Finished");
    }

    
    // *** CLASS & INSTANCE METHODS ***
    // what this does is set the motors to basically their factory settings in case said motors had something different done to them at some point.
    private void configStageOneMotor()
    {
        stageOneMotor.restoreFactoryDefaults();
        stageOneMotor.setInverted(false);
        stageOneMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        stageOneMotor.setOpenLoopRampRate(0.1);
        stageOneMotor.setSmartCurrentLimit(40);
    }
    
    // what this does is set the motors to basically their factory settings in case said motors had something different done to them at some point.
    private void configStageTwoMotor()
    {
        stageTwoMotor.restoreFactoryDefaults();
        stageTwoMotor.setInverted(false);
        stageTwoMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        stageTwoMotor.setOpenLoopRampRate(0.1);
        stageTwoMotor.setSmartCurrentLimit(40);
    }

    public void reverseStageOne()
    {
        setStageOneSpeed(-FIRST_STAGE_SPEED);
    }
    
    public void stopStageOne()
    {
        setStageOneSpeed(0);
    }

    public void forwardStageOne()
    {
        setStageOneSpeed(FIRST_STAGE_SPEED);
    }

    public void reverseStageTwo()
    {
        setStageTwoSpeed(-SECOND_STAGE_SPEED);
    }

    public void stopStageTwo()
    {
        setStageTwoSpeed(0);
    }

    public void forwardStageTwo()
    {
        setStageTwoSpeed(SECOND_STAGE_SPEED);
    }

    private void setStageOneSpeed(double speed)
    {
        if (speed > 1)
        {
            speed = 1;
        }
        else if (speed < -1)
        {
            speed = -1;
        }

        stageOneMotor.set(speed);
    }

    private void setStageTwoSpeed(double speed)
    {
        if (speed > 1)
        {
            speed = 1;
        }
        else if (speed < -1)
        {
            speed = -1;
        }

        stageTwoMotor.set(speed);
    }

    // Proximity sensors
    // (intakeSensor, stageOneSensor, stageTwoSensor)
    /**
     * Debounced
     * True means sensor is picking something up which is why we are taking opposite
     */
    // public boolean measureIntakeSensor()
    // {
    //     // TODO: Move the debouncing to SensorValues class
    //     if (!intakeSensor.get())
    //     {
    //         intakeSensorActivateCount++;
    //         intakeSensorDeactivateCount = 0;
    //     }
    //     else
    //     {
    //         intakeSensorActivateCount = 0;
    //         intakeSensorDeactivateCount++;
    //     }

    //     if (intakeSensorActivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedIntakeSensor = true;
    //     }
    //     else if (intakeSensorDeactivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedIntakeSensor = false;
    //     }

    //     return debouncedIntakeSensor;
    // }

    /**
     * True means sensor is picking something up which is why we are taking opposite
     */
    public boolean measureIntakeSensor()
    {
        return !intakeSensor.get();
    }

    /**
     * Debounced
     * True means sensor is picking something up which is why we are taking opposite
     */
    // public boolean measureStageOneSensor()
    // {
    //     // TODO: Move the debouncing to SensorValues class
    //     if (!stageOneSensor.get())
    //     {
    //         stageOneSensorActivateCount++;
    //         stageOneSensorDeactivateCount = 0;
    //     }
    //     else
    //     {
    //         stageOneSensorActivateCount = 0;
    //         stageOneSensorDeactivateCount++;
    //     }

    //     if (stageOneSensorActivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedStageOneSensor = true;
    //     }
    //     else if (stageOneSensorDeactivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedStageOneSensor = false;
    //     }

    //     return debouncedStageOneSensor;
    // }

    /**
     * True means sensor is picking something up which is why we are taking opposite
     */
    public boolean measureStageOneSensor()
    {
        return !stageOneSensor.get();
    }
    
    /**
     * Debounced
     * True means sensor is picking something up which is why we are taking opposite
     */
    // public boolean measureStageTwoSensor()
    // {
    //     // TODO: Move the debouncing to SensorValues class
    //     if (!stageTwoSensor.get())
    //     {
    //         stageTwoSensorActivateCount++;
    //         stageTwoSensorDeactivateCount = 0;
    //     }
    //     else
    //     {
    //         stageTwoSensorActivateCount = 0;
    //         stageTwoSensorDeactivateCount++;
    //     }

    //     if (stageTwoSensorActivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedStageTwoSensor = true;
    //     }
    //     else if (stageTwoSensorDeactivateCount >= Constant.DEBOUNCE_THRESHOLD)
    //     {
    //         debouncedStageTwoSensor = false;
    //     }

    //     return debouncedStageTwoSensor;
    // }

    /**
     * True means sensor is picking something up which is why we are taking opposite
     */
    public boolean measureStageTwoSensor()
    {
        return !stageTwoSensor.get();
    }
    
    // TODO: Make toString()
    public String toString()
    {
        return null;
    }
}
