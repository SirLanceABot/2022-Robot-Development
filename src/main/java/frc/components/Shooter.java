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

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.constants.*;
import frc.robot.RobotContainer;
import frc.vision.TargetData;
import frc.vision.VisionData;
import frc.shuffleboard.CameraTab;


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

    private static final PowerDistribution PDH = RobotContainer.PDH;

    private TargetData myWorkingCopyOfTargetData;

    private static final int TIMEOUT_MS = 30;

    private static final double LONG_SHOT_SPEED = Constant.LONG_SHOT_SPEED;
    private static final double SHORT_SHOT_SPEED = Constant.SHORT_SHOT_SPEED;
    private static final double DROP_SHOT_SPEED = Constant.DROP_SHOT_SPEED;

    //TODO: Tune PID values
    //original PID values are kP = 0.14 and kF = 0.035
    //second set of PID values are kP = 0.017 and kF = 0.049
    //encoder ticks at 10 foot shot = 13,000
    private static final double kP = 0.018;
    private static final double kI = 0.0002;
    private static final double kD = 0.000;
    private static final double kF = 0.0475;

    private static final double SHOOT_SPEED_THRESHOLD = Constant.SHOOT_SPEED_THRESHOLD;
    private static final double SHROUD_ANGLE_THRESHOLD = Constant.SHROUD_ANGLE_THRESHOLD;
    private static final double HUB_ALIGNMENT_THRESHOLD = Constant.HUB_ALIGNMENT_THRESHOLD;
    //TODO: Actual gear ratio goes here
    private static final double FLYWHEEL_GEAR_RATIO = 24.0 / 60.0;
    // private static final double FLYWHEEL_GEAR_RATIO = 1.0; //ONLY FOR TESTING

    //m/s and degrees
    private static double desiredLaunchSpeed = 1850.0;
    private static double desiredLaunchAngle = -150.0;



    private static double currentFlywheelSpeed = 0.0;
    private static double currentShroudAngle = 0.0;

    private static double distance;

    private static double autonomousDistance;

    //number of consecutive checks saying the shooter is ready to shoot
    private static int successfulChecks = 0;

    //number of consecutive checsk required for the shuttle to empty cargo into the shooter
    private static int requiredChecks = 6;

    private static boolean isShroudDown = false;

    //flywheel.getSelectedSensorVelocity() returns ticks/100ms by default, so we convert to ticks/ms, ticks/s, ticks/min, rot/min, and gear ratio
    //i *think* you divide gear ratio because you're finding flywheel speed given motor speed
    private static final double TICK_TO_RPM = (1.0 / 100.0) * (1000.0 / 1.0) * (60.0 / 1.0) * (1.0 / 2048.0) * FLYWHEEL_GEAR_RATIO;

    private static final double FEET_TO_METERS = 0.3048;

    private static boolean isShooting = false;

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

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
        LowerTrajectoryData.dataInit();
        ShroudData.dataInit();
        ShooterVisionData.dataInit();
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
        shroudMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 25, 30, 0.02), 10);
    }

    // *** CLASS & INSTANCE METHODS ***
    //speeds are all in rpm
    private void setFlywheelSpeed(double speed)
    {
        //TODO: BRING BACK TO RUN SHOOTER
        flywheelMotor.set(ControlMode.Velocity, speed / TICK_TO_RPM);
        // System.out.println("DESIRED FLYWHEEL SPEED: " + speed / TICK_TO_RPM);
        // System.out.println("ACTUAL FLYWHEEL SPEED: " + flywheelMotor.getSelectedSensorVelocity());
        System.out.println("Flywheel velocity: " + measureFlywheelSpeed());
        System.out.println("Shroud value: " + measureShroudSensorValue());
    }


    //DO NOT USE UNLESS IN TELEOP MODE
    public void setFlywheelSpeedNew(double speed)
    {
        flywheelMotor.set(ControlMode.PercentOutput, speed);
        // System.out.println("Flywheel Velocity: " + measureFlywheelSpeed());
        // System.out.println("Shroud Value: " + measureShroudSensorValue());
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

    public void prepareShooter()
    {
        prepareShooter(Hub.kUpper);
    }

    public void prepareShooter(Hub hub)
    {
        isShooting = true;
        // RobotContainer.CAMERA_TAB.updateLimeLightMode();

        // //vision code
        updateVisionData();

        // Comment this out to test for new data and set the variables up above
        // if (isDataFresh() && isTargetFound())
        // {
        //     // System.out.println("CONTOURS FOUND");
        //     calculateLaunchTrajectory(hub);
        // }
        if (isTargetFound())
        {
            // System.out.println("CONTOURS FOUND");
            calculateLaunchTrajectory(hub);
        }

        // calculateLaunchTrajectory(hub);

        System.out.println("DESIRED LAUNCH SPEED: " + desiredLaunchSpeed);
        System.out.println("DESIRED LAUNCH ANGLE: " + desiredLaunchAngle);
        setFlywheelSpeed(desiredLaunchSpeed);
        setShroudAngle(desiredLaunchAngle);

        checkIsShooterReady();
    }

    public void prepareShooter(Hub hub, double distance)
    {
        isShooting = true;

        calculateLaunchTrajectory(hub, distance);

        System.out.println("DESIRED LAUNCH SPEED: " + desiredLaunchSpeed);
        System.out.println("DESIRED LAUNCH ANGLE: " + desiredLaunchAngle);
        setFlywheelSpeed(desiredLaunchSpeed);
        setShroudAngle(desiredLaunchAngle);

        checkIsShooterReady();
    }

    public void prepareShooter(double desiredLaunchSpeed, double desiredLaunchAngle)
    {
        isShooting = true;

        Shooter.desiredLaunchSpeed = desiredLaunchSpeed;
        Shooter.desiredLaunchAngle = desiredLaunchAngle;
        System.out.println("DESIRED LAUNCH SPEED: " + desiredLaunchSpeed);
        System.out.println("DESIRED LAUNCH ANGLE: " + desiredLaunchAngle);
        setFlywheelSpeed(desiredLaunchSpeed);
        setShroudAngle(desiredLaunchAngle);

        checkIsShooterReady();
    }

    public void startLongShot()
    {
        isShooting = true;

        setFlywheelSpeed(LONG_SHOT_SPEED);
        setShroudAngle(30);
    }

    public void startShortShot()
    {
        isShooting = true;
        
        setFlywheelSpeed(SHORT_SHOT_SPEED);
        setShroudAngle(45);
    }

    public void startDropShot()
    {
        isShooting = true;
        
        setFlywheelSpeed(DROP_SHOT_SPEED);
        setShroudAngle(45);
    }

    public void stopFlywheel()
    {
        flywheelMotor.set(ControlMode.PercentOutput, 0.0);
        // desiredLaunchSpeed = 0.0;
    }

    public void stopShooter()
    {
        isShooting = false;
        // RobotContainer.CAMERA_TAB.updateLimeLightMode();

        stopFlywheel();
        stopShroud();
    }

    private void calculateLaunchTrajectory(Hub hub)
    {
        //vision code
        // distance = ShooterVisionData.getDistance(myWorkingCopyOfTargetData.getPortDistance());
        distance = ShooterVisionData.getDistance(table.getEntry("ty").getDouble(0.0));
        // System.out.println("DISTANCE: " + distance);
        // distance = 8.0 * FEET_TO_METERS;

        calculateLaunchTrajectory(hub, distance);
    }

    private void calculateLaunchTrajectory(Hub hub, double distance)
    {
        if (hub == Hub.kLower)
        {
            desiredLaunchSpeed = LowerTrajectoryData.getSpeed(distance); //slowed down for testing
            desiredLaunchAngle = LowerTrajectoryData.getAngle(distance);
        }
        else if (hub == Hub.kUpper)
        {
            desiredLaunchSpeed = UpperTrajectoryData.getSpeed(distance); //slowed down for testing
            desiredLaunchAngle = UpperTrajectoryData.getAngle(distance);
        }

        if (desiredLaunchSpeed < 0.0)
        {
            desiredLaunchSpeed = 0.0;
        }
        else if (desiredLaunchSpeed > 10000.0)
        {
            desiredLaunchSpeed = 10000.0;
        }
        
        if (desiredLaunchAngle < -235.0)
        {
            desiredLaunchAngle = -235.0;
        }
        else if (desiredLaunchAngle > -110.0)
        {
            desiredLaunchAngle = -110.0;
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
        if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD + 40.0)
        {
            setShroudMotorSpeed(0.8);
        }
        else if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD + 10.0)
        {
            setShroudMotorSpeed(0.3);
        }
        else if (angle - measureShroudAngle() > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(0.2);
        }
        else if (measureShroudAngle() - angle > SHROUD_ANGLE_THRESHOLD + 40.0)
        {
            setShroudMotorSpeed(-0.6);
        }
        else if (measureShroudAngle() - angle > SHROUD_ANGLE_THRESHOLD + 10.0)
        {
            setShroudMotorSpeed(-0.2);
        }
        else if (measureShroudAngle() - angle > SHROUD_ANGLE_THRESHOLD)
        {
            setShroudMotorSpeed(-0.15);
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
        // myWorkingCopyOfTargetData = VisionData.targetData.get();

        NetworkTableEntry tx = table.getEntry("tx"); // angle to turn
        NetworkTableEntry ty = table.getEntry("ty"); // related to distance to hub
        NetworkTableEntry tv = table.getEntry("tv"); // <1. is no target (0 no target;1 target found)
        
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double valid = tv.getDouble(0.0);

        // testing - post to SmartDashboard to see that LL is working
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightValid", valid);
        SmartDashboard.putString("valid target", valid < 1.0 ? "not found" : "found");
    }

    public boolean isDataFresh()
    {
        return myWorkingCopyOfTargetData.isFreshData();
    }

    public boolean isTargetFound()
    {
        // return myWorkingCopyOfTargetData.isTargetFound();
        return table.getEntry("tv").getDouble(0.0) == 1;
    }

    //positive is looking left of the hub, negative is looking right (think unit circle)
    public double getHubAngle()
    {
        // if (myWorkingCopyOfTargetData.isTargetFound())
        // {
        //     return myWorkingCopyOfTargetData.getAngleToTurn();
        // }
        // else
        // {
        //     return 0.0;
        // }

        if (isTargetFound())
        {
            return -table.getEntry("tx").getDouble(0.0);
        }
        else
        {
            return 0.0;
        }
    }

    public boolean isHubAligned()
    {
        return Math.abs(getHubAngle()) < HUB_ALIGNMENT_THRESHOLD;
    }

    private void checkIsShooterReady()
    {
        if (isFlywheelReady() && isShroudReady())
        {
            successfulChecks++;
        }
        else
        {
            successfulChecks = 0;
        }
        System.out.println("SUCCESSFUL CHECKS: " + successfulChecks);
    }

    public void resetShooterChecks()
    {
        successfulChecks = 0;
    }

    public boolean isShooterReady()
    {
        return successfulChecks >= requiredChecks;
    }

    public boolean getIsShooting()
    {
        return isShooting;
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