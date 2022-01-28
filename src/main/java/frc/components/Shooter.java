package frc.components;

import java.lang.invoke.MethodHandles;

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

    //TODO: Configure PID values
    private static final double kP = 0.4;
    private static final double kI = 0.000025;
    private static final double kD = 0.0000001;
    private static final double kF = 0.0;


    // *** CLASS CONSTRUCTOR ***
    public Shooter()
    {
        configMotor();
    }


    // *** CLASS & INSTANCE METHODS ***
    //speeds are all in rpm
    private void setFlywheelMotorSpeed(double speed)
    {

    }

    // public double measureFlywheelSpeed()
    // {

    // }
    
    // public boolean isFlywheelReady()
    // {

    // }

    public void startLongShot()
    {

    }

    public void startShortShot()
    {

    }

    public void startDropShot()
    {

    }

    public void stopFlywheel()
    {

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

        flywheelMotor.configOpenloopRamp(0.5);
        flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);
    }

    // public String toString()
    // {

    // }
}
