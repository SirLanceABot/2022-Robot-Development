package frc.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.RobotContainer;
import frc.drivetrain.Drivetrain;
import frc.components.Intake;
import frc.components.ShuttleFSM;

public class DriveVectorAndTurnToAngle implements Command 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    private static final Intake INTAKE = RobotContainer.INTAKE;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private double speed_metersPerSecond;
    private double xDisplacement_meters;
    private double yDisplacement_meters;
    private final double SLOW_RAMP_RATE = 0.75;
    private final double FAST_RAMP_RATE = 0.1;
    private Translation2d startingPosition;

    private static final double ANGLE_THRESHOLD = 1.0;
    private double minAngularVelocity;
    private double maxAngularVelocity;
    private double angle;
    private boolean isFinished;


    // *** CLASS CONSTRUCTOR ***
    public DriveVectorAndTurnToAngle(double speed_metersPerSecond, double xDisplacement_meters, double yDisplacement_meters, double minAngularVelocity, double maxAngularVelocity, double angle)
    {
        this.speed_metersPerSecond = speed_metersPerSecond;
        this.xDisplacement_meters = xDisplacement_meters;
        this.yDisplacement_meters = yDisplacement_meters;

        this.minAngularVelocity = minAngularVelocity;
        this.maxAngularVelocity = maxAngularVelocity;
        this.angle = angle;

        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);
        isFinished = false;

        // DRIVETRAIN.resetEncoders();
        startingPosition = DRIVETRAIN.getCurrentTranslation();
        DRIVETRAIN.configOpenLoopRamp(SLOW_RAMP_RATE);
    }

    public void execute()
    {
        SHUTTLEFSM.fancyRun(false);

        // if (INTAKE.measureArmOut())
        // {
        //     INTAKE.pMoveArmFloat();
        // }
        
        if(DRIVETRAIN.driveVectorAndTurnToAngle(startingPosition, speed_metersPerSecond, xDisplacement_meters, yDisplacement_meters, minAngularVelocity, maxAngularVelocity, angle, ANGLE_THRESHOLD))
        {
            System.out.println("Finished Driving");
            isFinished = true;
        }
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {
        DRIVETRAIN.configOpenLoopRamp(FAST_RAMP_RATE);
        DRIVETRAIN.stopMotor();
        // DRIVETRAIN.resetEncoders();
    }

    public String toString()
    {
        return "DriveVectorAndTurnToAngle(" + speed_metersPerSecond + ", " + xDisplacement_meters + ", " + yDisplacement_meters + ", " + minAngularVelocity + ", " + maxAngularVelocity + ", " + angle + ")";
    }
}