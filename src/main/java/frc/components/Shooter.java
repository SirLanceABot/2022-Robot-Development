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
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import frc.constants.*;
import frc.vision.TargetData;
import frc.vision.VisionData;


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
    private final TalonFX flywheelMotor;// = new TalonFX(Port.Motor.SHOOTER_FLYWHEEL);
    // private final TalonSRX flywheelMotor;// = new TalonSRX(Port.Motor.SHOOTER_FLYWHEEL);
    private final TalonSRX shroudMotor;// = new TalonSRX(Port.Motor.SHOOTER_SHROUD);

    // TODO: the shroud Sensor will be plugged into the TalonSRX and thus will not be an AnalogInput
    // private static final AnalogInput shroudSensor = new AnalogInput(Port.Sensor.SHOOTER_SHROUD);

    private static final PowerDistribution PDH = new PowerDistribution(Port.Sensor.PDH_CAN_ID, ModuleType.kRev);

    private TargetData myWorkingCopyOfTargetData;

    private static final int TIMEOUT_MS = 30;

    private static final double LONG_SHOT_SPEED = Constant.LONG_SHOT_SPEED;
    private static final double SHORT_SHOT_SPEED = Constant.SHORT_SHOT_SPEED;
    private static final double DROP_SHOT_SPEED = Constant.DROP_SHOT_SPEED;

    //TODO: Tune PID values
    private static final double kP = 0.14;
    private static final double kI = 0.000000;
    private static final double kD = 0.0000000;
    private static final double kF = 0.035;

    private static final double SHOOT_SPEED_THRESHOLD = Constant.SHOOT_SPEED_THRESHOLD;
    private static final double SHROUD_ANGLE_THRESHOLD = Constant.SHROUD_ANGLE_THRESHOLD;
    private static final double HUB_ALIGNMENT_THRESHOLD = Constant.HUB_ALIGNMENT_THRESHOLD;
    //TODO: Actual gear ratio goes here
    private static final double FLYWHEEL_GEAR_RATIO = 60.0 / 24.0;

    //m/s and degrees
    private static double desiredLaunchSpeed = 2000.0;
    private static double desiredLaunchAngle = -235.0;



    private static double currentFlywheelSpeed = 0.0;
    private static double currentShroudAngle = 0.0;

    private static double distance;

    private static double autonomousDistance;

    //number of consecutive checks saying the shooter is ready to shoot
    private static int successfulChecks = 0;

    //number of consecutive checsk required for the shuttle to empty cargo into the shooter
    private static int requiredChecks = 3;

    private static boolean isShroudDown = false;

    //flywheel.getSelectedSensorVelocity() returns ticks/100ms by default, so we convert to ticks/ms, ticks/s, ticks/min, rot/min, and gear ratio
    //i *think* you divide gear ratio because you're finding flywheel speed given motor speed
    private static final double TICK_TO_RPM = (1.0 / 100.0) * (1000.0 / 1.0) * (60.0 / 1.0) * (1.0 / 2048.0) * FLYWHEEL_GEAR_RATIO;

    private static final double FEET_TO_METERS = 0.3048;

    // *** CLASS CONSTRUCTOR ***
    public Shooter(int flywheelMotorPort, int shroudMotorPort)
    {
        // flywheelMotorPort = 0;  // Used ONLY for testing
        // shroudMotorPort = 1;    // Used ONLY for testing

        flywheelMotor = new TalonFX(flywheelMotorPort);
        // flywheelMotor = new TalonSRX(flywheelMotorPort);
        shroudMotor = new TalonSRX(shroudMotorPort);

        configFlywheelMotor();
        configShroudMotor();

        UpperTrajectoryData.dataInit();
        ShroudData.dataInit();
        ShooterVisionData.dataInit();
    }

    private void configFlywheelMotor()
    {
        flywheelMotor.configFactoryDefault();
        flywheelMotor.setInverted(true);
        flywheelMotor.setNeutralMode(NeutralMode.Coast);

        flywheelMotor.config_kF(0, kF, TIMEOUT_MS);
        flywheelMotor.config_kP(0, kP, TIMEOUT_MS);
        flywheelMotor.config_kI(0, kI, TIMEOUT_MS);
        flywheelMotor.config_kD(0, kD, TIMEOUT_MS);

        flywheelMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, TIMEOUT_MS);
        flywheelMotor.setSensorPhase(false);

        flywheelMotor.configForwardSoftLimitThreshold(0.0);
        flywheelMotor.configForwardSoftLimitEnable(false);
        flywheelMotor.configReverseSoftLimitThreshold(0.0);
        flywheelMotor.configReverseSoftLimitEnable(false);
        
        flywheelMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        flywheelMotor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        //comment out if using a PID
        // flywheelMotor.configOpenloopRamp(0.5);
        // flywheelMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);

        //increase framerate for sensor velocity checks (currently at 100ms)
    }

    private void configShroudMotor()
    {
        //TODO: delete or comment out unnecessary configs
        shroudMotor.configFactoryDefault();
        shroudMotor.setInverted(true);
        shroudMotor.setNeutralMode(NeutralMode.Brake);

        shroudMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, TIMEOUT_MS);
        shroudMotor.setSensorPhase(false);

        shroudMotor.configForwardSoftLimitThreshold(-110.0);
        shroudMotor.configForwardSoftLimitEnable(true);
        shroudMotor.configReverseSoftLimitThreshold(-235.0);
        shroudMotor.configReverseSoftLimitEnable(true);
        
        shroudMotor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        shroudMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);

        //comment out if using a PID
        shroudMotor.configOpenloopRamp(0.5);
        shroudMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 35, 40, 0.5), 10);
    }

    // *** CLASS & INSTANCE METHODS ***
    //speeds are all in rpm
    private void setFlywheelSpeed(double speed)
    {
        // System.out.println("Flywheel velocity: " + measureFlywheelSpeed());
        // System.out.println("Shroud value: " + measureShroudSensorValue());
        flywheelMotor.set(ControlMode.Velocity, speed / TICK_TO_RPM);
    }

    //DO NOT USE UNLESS IN TELEOP MODE
    public void setFlywheelSpeedNew(double speed)
    {
        flywheelMotor.set(ControlMode.PercentOutput, speed);
        System.out.println("Flywheel Velocity: " + measureFlywheelSpeed());
        System.out.println("Shroud Value: " + measureShroudSensorValue());
    }

    //return value is in rpm
    public double measureFlywheelSpeed()
    {
        return flywheelMotor.getSelectedSensorVelocity() * TICK_TO_RPM;
    }
    
    public boolean isFlywheelReady()
    {
        currentFlywheelSpeed = measureFlywheelSpeed();
        return (Math.abs(desiredLaunchSpeed - currentFlywheelSpeed) <= SHOOT_SPEED_THRESHOLD);
    }

    public void shoot()
    {
        shoot(Hub.kUpper);
    }

    public void shoot(Hub hub)
    {
        updateVisionData();

        if (isDataFresh())
        {
            calculateLaunchTrajectory(hub);
        }

        // calculateLaunchTrajectory(hub);

        setFlywheelSpeed(desiredLaunchSpeed);
        setShroudAngle(desiredLaunchAngle);

        // System.out.println("Flywheel velocity: " + flywheelMotor.getSelectedSensorVelocity());
        // System.out.println("Shroud value: " + measureShroudSensorValue());

        // checkIsShooterReady();
    }

    public void shoot(Hub hub, double distance)
    {
        calculateLaunchTrajectory(hub, distance);

        // System.out.println("Flywheel velocity: " + flywheelMotor.getSelectedSensorVelocity());
        // System.out.println("Shroud value: " + measureShroudSensorValue());

        // checkIsShooterReady();
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
        // desiredLaunchSpeed = 0.0;
    }

    private void calculateLaunchTrajectory(Hub hub)
    {
        // distance = ShooterVisionData.getDistance(myWorkingCopyOfTargetData.getPortDistance());
        distance = 0.0 * FEET_TO_METERS;

        calculateLaunchTrajectory(hub, distance);
    }

    private void calculateLaunchTrajectory(Hub hub, double distance)
    {
        if (hub == Hub.kLower)
        {
            desiredLaunchSpeed = LowerTrajectoryData.getSpeed(distance);
            desiredLaunchAngle = LowerTrajectoryData.getAngle(distance);
        }
        else if (hub == Hub.kUpper)
        {
            desiredLaunchSpeed = UpperTrajectoryData.getSpeed(distance);
            desiredLaunchAngle = UpperTrajectoryData.getAngle(distance);
        }
    }

    //this speed is in percent output
    private void setShroudMotorSpeed(double speed)
    {
        shroudMotor.set(ControlMode.PercentOutput, speed);
    }

    // DO NOT USE UNLESS IN TELEOP MODE
    public void setShroudMotorSpeedNew(double speed)
    {
        shroudMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopShroud()
    {
        setShroudMotorSpeed(0.0);
    }

    private void setShroudAngle(double angle)
    {
        if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD + 50.0)
        {
            setShroudMotorSpeed(0.75);
        }
        else if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(0.25);
        }
        else if (measureShroudAngle() - angle > SHROUD_ANGLE_THRESHOLD + 50.0)
        {
            setShroudMotorSpeed(-0.75);
        }
        else if (measureShroudAngle() - angle > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(-0.25);
        }
        else
        {
            stopShroud();
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
        return shroudMotor.getSelectedSensorPosition();
    }

    public boolean isShroudReady()
    {
        currentShroudAngle = measureShroudAngle();
        return (Math.abs(desiredLaunchAngle - currentShroudAngle) < SHROUD_ANGLE_THRESHOLD);
    }

    public void updateVisionData()
    {
        myWorkingCopyOfTargetData = VisionData.targetData.get();
    }

    public boolean isDataFresh()
    {
        return myWorkingCopyOfTargetData.isFreshData();
    }

    //positive is looking left of the hub, negative is looking right (think unit circle)
    public double getHubAngle()
    {
        return myWorkingCopyOfTargetData.getAngleToTurn();
    }

    public boolean isHubAligned()
    {
        return Math.abs(getHubAngle()) < HUB_ALIGNMENT_THRESHOLD;
    }

    private void checkIsShooterReady()
    {
        //TODO: darren wants to remove isHubAligned()
        if (isFlywheelReady() && isShroudReady() && isHubAligned())
        {
            successfulChecks++;
        }
        else
        {
            successfulChecks = 0;
        }
    }

    public boolean isShooterReady()
    {
        return successfulChecks >= requiredChecks;
    }

    public void turnOnLED()
    {
        PDH.setSwitchableChannel(true);
    }

    public void turnOffLED()
    {
        PDH.setSwitchableChannel(false);
    }

    // For testing
    public void outputShroudLimit()
    {
        System.out.println("Shroud: " + measureShroudSensorValue());
    }

    public String toString()
    {
        return "Desired Speed: " + String.format("%.4f", desiredLaunchSpeed) + '\n' + "Current Speed: " + String.format("%.4f", measureFlywheelSpeed()) + '\n' +
               "Desired Angle: " + String.format("%.4f", desiredLaunchAngle) + '\n' + "Current Angle: " + String.format("%.4f", measureShroudAngle());
    }
}