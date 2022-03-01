package frc.components;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;

import frc.constants.*;


// TODO: here are some things that need to change
// 1. The Analog Input sensor will be plugged into the Shroud TalonSRX, use that instead of the QuadEncoder for Feedback
// 2. The Shroud TalonSRX will have a reverse limit switch

public class Shooter 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    public static enum Hub
    {
        kUpper("Upper"),
        kLower("Lower")
        ;

        public final String level;

        private Hub(String level)
        {
            this.level = level;
        } 
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // private static final TalonSRX flywheelMotor = new TalonFX(Port.Motor.SHOOTER_FLYWHEEL);
    private final TalonSRX flywheelMotor;// = new TalonSRX(Port.Motor.SHOOTER_FLYWHEEL);
    private final TalonSRX shroudMotor;// = new TalonSRX(Port.Motor.SHOOTER_SHROUD);
    private static final AnalogInput shroudSensor = new AnalogInput(Port.Sensor.SHOOTER_SHROUD);

    private static final int TIMEOUT_MS = 30;

    private static final double LONG_SHOT_SPEED = Constant.LONG_SHOT_SPEED;
    private static final double SHORT_SHOT_SPEED = Constant.SHORT_SHOT_SPEED;
    private static final double DROP_SHOT_SPEED = Constant.DROP_SHOT_SPEED;

    //TODO: Tune PID values
    private static final double kP = 0.2;
    private static final double kI = 0.000000;
    private static final double kD = 0.0000000;
    private static final double kF = 0.53;

    private static final double SHOOT_SPEED_THRESHOLD = Constant.SHOOT_SPEED_THRESHOLD;
    private static final double SHROUD_ANGLE_THRESHOLD = Constant.SHROUD_ANGLE_THRESHOLD;
    //TODO: Actual gear ratio goes here
    private static final double FLYWHEEL_GEAR_RATIO = 1.0;

    //m/s and degrees
    private static double desiredLaunchSpeed = 0.0;
    private static double desiredLaunchAngle = 0.0;

    private static double currentFlywheelSpeed = 0.0;
    private static double currentShroudAngle = 0.0;

    private static double distance;

    //flywheel.getSelectedSensorVelocity() returns ticks/100ms by default, so we convert to ticks/ms, ticks/s, ticks/min, rot/min, and gear ratio
    //i *think* you divide gear ratio because you're finding flywheel speed given motor speed
    private static final double TICK_TO_RPM = (1.0 / 100.0) * (1000.0 / 1.0) * (60.0 / 1.0) * (1.0 / 4096.0) / FLYWHEEL_GEAR_RATIO;


    // *** CLASS CONSTRUCTOR ***
    public Shooter(int flywheelMotorPort, int shroudMotorPort)
    {
        flywheelMotorPort = 0;  // Used ONLY for testing
        shroudMotorPort = 1;    // Used ONLY for testing

        flywheelMotor = new TalonSRX(flywheelMotorPort);
        shroudMotor = new TalonSRX(shroudMotorPort);

        configFlywheelMotor();
        configShroudMotor();

        FlywheelData.dataInit();
        ShroudData.dataInit();
    }


    // *** CLASS & INSTANCE METHODS ***
    //speeds are all in rpm
    private void setFlywheelSpeed(double speed)
    {
        flywheelMotor.set(ControlMode.Velocity, speed / TICK_TO_RPM);
    }

    //DO NOT USE UNLESS IN TELEOP MODE
    public void setFlywheelSpeedNew(double speed)
    {
        flywheelMotor.set(ControlMode.Velocity, speed / TICK_TO_RPM);
    }

    //return value is in rpm
    public double measureFlywheelSpeed()
    {
        return flywheelMotor.getSelectedSensorVelocity();
    }
    
    public boolean isFlywheelReady()
    {
        currentFlywheelSpeed = measureFlywheelSpeed();
        return (Math.abs(desiredLaunchSpeed - currentFlywheelSpeed) <= SHOOT_SPEED_THRESHOLD);
    }

    public void shoot()
    {
        calculateLaunchTrajectory();
        setFlywheelSpeed(desiredLaunchSpeed);
        setShroudAngle(desiredLaunchAngle);
    }

    public void startLongShot()
    {
        setFlywheelSpeed(LONG_SHOT_SPEED);
        setShroudAngle(30);
    }

    public void startShortShot()
    {
        setFlywheelSpeed(SHORT_SHOT_SPEED);
        setShroudAngle(45);
    }

    public void startDropShot()
    {
        setFlywheelSpeed(DROP_SHOT_SPEED);
        setShroudAngle(45);
    }

    public void stopFlywheel()
    {
        flywheelMotor.set(ControlMode.PercentOutput, 0.0);
        desiredLaunchSpeed = 0.0;
    }

    //TODO: actual calculations for trajectory, given vision distances, could also be from a table of experimented values (the latter is more likely) (will require testing)
    private void calculateLaunchTrajectory()
    {
        //TODO: vision distance calculation
        distance = 0.0;

        desiredLaunchSpeed = FlywheelData.getSpeed(distance);
        desiredLaunchAngle = FlywheelData.getAngle(distance);
    }

    //this speed is in percent output
    private void setShroudMotorSpeed(double speed)
    {
        shroudMotor.set(ControlMode.PercentOutput, speed);
    }

    private void setShroudAngle(double angle)
    {
        if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(0.25);
        }
        else if (angle - measureShroudAngle() < SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(-0.25);
        }
        else
        {
            setShroudMotorSpeed(0.0);
        }
    }
    // DO NOT USE UNLESS IN TELEOP MODE
    public void setShroudAngleNew(double angle)
    {
        if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(0.25);
        }
        else if (angle - measureShroudAngle() < SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(-0.25);
        }
        else
        {
            setShroudMotorSpeed(0.0);
        }
    }

    public double measureShroudAngle()
    {
        //TODO: Voltage conversions for actual distance, and then shroud angle (will require testing)
        return ShroudData.getAngle(measureShroudSensorValue());
    }

    private double measureShroudSensorValue()
    {
        return shroudSensor.getAverageVoltage();
    }

    public boolean isShroudReady()
    {
        currentShroudAngle = measureShroudAngle();
        return (Math.abs(desiredLaunchAngle - currentShroudAngle) < SHROUD_ANGLE_THRESHOLD);
    }

    private void configFlywheelMotor()
    {
        flywheelMotor.configFactoryDefault();
        flywheelMotor.setInverted(false);
        flywheelMotor.setNeutralMode(NeutralMode.Coast);

        flywheelMotor.config_kF(0, kF, TIMEOUT_MS);
        flywheelMotor.config_kP(0, kP, TIMEOUT_MS);
        flywheelMotor.config_kI(0, kI, TIMEOUT_MS);
        flywheelMotor.config_kD(0, kD, TIMEOUT_MS);

        flywheelMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, TIMEOUT_MS);
        flywheelMotor.setSensorPhase(false);

        flywheelMotor.configForwardSoftLimitThreshold(0.0);
        flywheelMotor.configForwardSoftLimitEnable(false);
        flywheelMotor.configReverseSoftLimitThreshold(0.0);
        flywheelMotor.configReverseSoftLimitEnable(false);
        
        flywheelMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        flywheelMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        //comment out if using a PID
        // flywheelMotor.configOpenloopRamp(0.5);
        flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);

        //increase framerate for sensor velocity checks (currently at 100ms)
        
        
    }

    private void configShroudMotor()
    {
        //TODO: delete or comment out unnecessary configs
        shroudMotor.configFactoryDefault();
        shroudMotor.setInverted(false);
        shroudMotor.setNeutralMode(NeutralMode.Brake);

        shroudMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, TIMEOUT_MS);
        shroudMotor.setSensorPhase(false);

        shroudMotor.configForwardSoftLimitThreshold(0.0);
        shroudMotor.configForwardSoftLimitEnable(false);
        shroudMotor.configReverseSoftLimitThreshold(0.0);
        shroudMotor.configReverseSoftLimitEnable(false);
        
        shroudMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        shroudMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        //comment out if using a PID
        shroudMotor.configOpenloopRamp(0.5);
        shroudMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);
    }

    public String toString()
    {
        return "Desired Speed: " + String.format("%.4f", desiredLaunchSpeed) + '\n' + "Current Speed: " + String.format("%.4f", measureFlywheelSpeed()) + '\n' +
               "Desired Angle: " + String.format("%.4f", desiredLaunchAngle) + '\n' + "Current Angle: " + String.format("%.4f", measureShroudAngle());
    }
}