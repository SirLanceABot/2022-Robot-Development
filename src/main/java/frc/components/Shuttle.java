package frc.components;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;

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
     * True means sensor is picking something up which is why we are taking opposite
     */
    public boolean measureIntakeSensor()
    {
        return !intakeSensor.get();
    }

    /**
     * True means sensor is picking something up which is why we are taking opposite
     */
    public boolean measureStageOneSensor()
    {
        return !stageOneSensor.get();
    }
    
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
