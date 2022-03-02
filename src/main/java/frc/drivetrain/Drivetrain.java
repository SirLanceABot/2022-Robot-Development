package frc.drivetrain;

import java.lang.invoke.MethodHandles;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.constants.Constant;

/** Represents a swerve drive style drivetrain. */
public class Drivetrain extends RobotDriveBase
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // private static final Translation2d frontLeftLocation = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d frontRightLocation = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d backLeftLocation = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
    // private static final Translation2d backRightLocation = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);

    private final SwerveModule frontLeft;// = new SwerveModule(Port.Module.FRONT_LEFT);
    private final SwerveModule frontRight;// = new SwerveModule(Port.Module.FRONT_RIGHT);
    private final SwerveModule backLeft;// = new SwerveModule(Port.Module.BACK_LEFT);
    private final SwerveModule backRight;// = new SwerveModule(Port.Module.BACK_RIGHT);

    private final AHRS navX;// = new AHRS(SerialPort.Port.kUSB);

    // private static final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
    //         frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    private final SwerveDriveKinematics kinematics;// = new SwerveDriveKinematics(
            // Port.Module.FRONT_LEFT.moduleLocation, Port.Module.FRONT_RIGHT.moduleLocation, Port.Module.BACK_LEFT.moduleLocation, Port.Module.BACK_RIGHT.moduleLocation);

    private final SwerveDriveOdometry odometry;// = new SwerveDriveOdometry(kinematics, navX.getRotation2d());

    // TODO: Make final by setting to an initial stopped state
    private SwerveModuleState[] previousSwerveModuleStates = null;


    // *** CLASS CONSTRUCTOR ***
    public Drivetrain(DrivetrainConfig dd)
    {
        super();  // call the RobotDriveBase constructor

        frontLeft = new SwerveModule(dd.frontLeftSwerveModule);
        frontRight = new SwerveModule(dd.frontRightSwerveModule);
        backLeft = new SwerveModule(dd.backLeftSwerveModule);
        backRight = new SwerveModule(dd.backRightSwerveModule);

        navX = new AHRS(dd.navXChannel);

        kinematics = new SwerveDriveKinematics(dd.frontLeftSwerveModule.moduleLocation, dd.frontRightSwerveModule.moduleLocation,
                                                dd.backLeftSwerveModule.moduleLocation, dd.backRightSwerveModule.moduleLocation);

        odometry = new SwerveDriveOdometry(kinematics, navX.getRotation2d());

        navX.reset();
        odometry.resetPosition(new Pose2d(), navX.getRotation2d());
    }


    // *** CLASS & INSTANCE METHODS ***

    /**
     * Method to drive the robot using joystick info.
     *
     * @param xSpeed Speed of the robot in the x direction (forward).
     * @param ySpeed Speed of the robot in the y direction (sideways).
     * @param turn Angular rate of the robot.
     * @param fieldRelative Whether the provided x and y speeds are relative to the field.
     */
    @SuppressWarnings("ParameterName")
    public void drive(double xSpeed, double ySpeed, double turn, boolean fieldRelative)
    {
        ChassisSpeeds chassisSpeeds;
        SwerveModuleState[] swerveModuleStates;

        if(fieldRelative)
            chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, turn, navX.getRotation2d());
        else
            chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, turn);
        
        swerveModuleStates = kinematics.toSwerveModuleStates(chassisSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constant.MAX_DRIVE_SPEED);
        // printDesiredStates(swerveModuleStates);

        if(xSpeed == 0 && ySpeed == 0 && turn == 0 && previousSwerveModuleStates != null)
        {
            for(int i = 0; i < swerveModuleStates.length; i++)
            {
                swerveModuleStates[i].angle = previousSwerveModuleStates[i].angle;
            }
        }

        frontLeft.setDesiredState(swerveModuleStates[0]);
        frontRight.setDesiredState(swerveModuleStates[1]);
        backLeft.setDesiredState(swerveModuleStates[2]);
        backRight.setDesiredState(swerveModuleStates[3]);

        previousSwerveModuleStates = swerveModuleStates;

        feedWatchdog();
    }

    /**
     * Drive a "straight" distance in meters
     * 
     * @param velocity in meters per second (+ forward, - reverse)
     * @param distanceToDrive in meters
     * @return true when drive is complete
     */
    public boolean driveStraight(double velocity, double distanceToDrive)
    {
        boolean isDone = false;
        double distanceDriven = odometry.getPoseMeters().getTranslation().getDistance(new Translation2d(0,0));

        updateOdometry();

        if(Math.abs(distanceDriven) < Math.abs(distanceToDrive))
        {
            drive(velocity, 0.0, 0.0, true);
        }
        else
        {
            stopMotor();
            isDone = true;
            System.out.println("Dist (meters) = " + distanceDriven);
        }

        return isDone;
    }

    /** Updates the field relative position of the robot. */
    public void updateOdometry()
    {
        odometry.update(
            navX.getRotation2d(),
            frontLeft.getState(),
            frontRight.getState(),
            backLeft.getState(),
            backRight.getState());
    }

    public void resetEncoders()
    {
        frontLeft.resetEncoders();
        frontRight.resetEncoders();
        backLeft.resetEncoders();
        backRight.resetEncoders();
    }

    @Override
    public void stopMotor()
    {
        frontLeft.stopModule();
        frontRight.stopModule();
        backLeft.stopModule();
        backRight.stopModule();
        feedWatchdog();
    }

    @Override
    public String getDescription()
    {
        return "Swerve Drivetrain";
    }
}