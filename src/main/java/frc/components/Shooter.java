package frc.components;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.constants.*;

public class Shooter 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    //TODO: Add PID
    private static final TalonFX flywheelMotor = new TalonFX(Port.Motor.SHOOTER_FLYWHEEL);

    private static final int TIMEOUT_MS = 30;

    private static final double LONG_SHOT_SPEED = Constant.LONG_SHOT_SPEED;
    private static final double SHORT_SHOT_SPEED = Constant.SHORT_SHOT_SPEED;
    private static final double DROP_SHOT_SPEED = Constant.DROP_SHOT_SPEED;

    //TODO: Tune PID values
    private static final double kP = 0.4;
    private static final double kI = 0.000025;
    private static final double kD = 0.0000001;
    private static final double kF = 0.0;

    private static final double SHOOT_SPEED_THRESHOLD = Constant.SHOOT_SPEED_THRESHOLD;
    //TODO: Actual gear ratio goes here
    private static final double FLYWHEEL_GEAR_RATIO = 1.0;

    private static double desiredFlywheelSpeed = 0.0;

    //flywheel.getSelectedSensorVelocity() returns ticks every 100ms by default, so we convert to ticks/ms, ticks/s, ticks/min, rot/min, and gear ratio
    private static final double TICK_TO_RPM = (1.0 / 100.0) * (1000.0 / 1.0) * (60.0 / 1.0) * (1.0 / 4096.0) * FLYWHEEL_GEAR_RATIO;


    // *** CLASS CONSTRUCTOR ***
    public Shooter()
    {
        configMotor();
    }


    // *** CLASS & INSTANCE METHODS ***
    //speeds are all in rpm
    private void setFlywheelMotorSpeed(double speed)
    {
        flywheelMotor.set(ControlMode.Velocity, speed / TICK_TO_RPM);
        desiredFlywheelSpeed = speed;
    }

    //return value is in rpm
    public double measureFlywheelSpeed()
    {
        return flywheelMotor.getSelectedSensorVelocity() * TICK_TO_RPM;
    }
    
    public boolean isFlywheelReady()
    {
        double currentFlywheelSpeed = measureFlywheelSpeed();
        return (Math.abs((desiredFlywheelSpeed - currentFlywheelSpeed) / desiredFlywheelSpeed) <= SHOOT_SPEED_THRESHOLD);
    }

    public void startLongShot()
    {
        setFlywheelMotorSpeed(LONG_SHOT_SPEED);
    }

    public void startShortShot()
    {
        setFlywheelMotorSpeed(SHORT_SHOT_SPEED);
    }

    public void startDropShot()
    {
        setFlywheelMotorSpeed(DROP_SHOT_SPEED);
    }

    public void stopFlywheel()
    {
        flywheelMotor.set(ControlMode.PercentOutput, 0.0);
        desiredFlywheelSpeed = 0.0;
    }

    private void configMotor()
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

    public String toString()
    {
        return "Desired Speed: " + String.format("%.4f", desiredFlywheelSpeed) + '\n' + "Current Speed: " + String.format("%.4f", measureFlywheelSpeed());
    }
}