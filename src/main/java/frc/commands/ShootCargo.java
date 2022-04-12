package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.components.Shooter;
import frc.robot.RobotContainer;
import frc.components.ShuttleFSM;
import frc.constants.Constant;
import frc.drivetrain.Drivetrain;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.Timer;

public class ShootCargo implements Command 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    private static final PowerDistribution PDH = RobotContainer.PDH;
    private static final Intake INTAKE = RobotContainer.INTAKE;
    private Timer timer = new Timer();
    private int numberOfCargo;
    private int cargoShot = 0;
    private Shooter.Hub hub;
    private double angleToTurn;
    private boolean isFinished;

    // These variables are only used to simulate cargo being shot
    // private int cargoShotSimulation = 0;
    // private double distanceShotSimulation = 0.0;


    // *** CLASS CONSTRUCTOR ***
    public ShootCargo(int numberOfCargo, Shooter.Hub hub)
    {
        this.numberOfCargo = numberOfCargo;
        this.hub = hub;

        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        // PDH.setSwitchableChannel(true);

        System.out.println(this);

        timer.reset();
        timer.start();

        INTAKE.compressorDisable();

        isFinished = false;
        // cargoShotSimulation = 0;
    }

    public void execute()
    {
        SHOOTER.startDropShot();
        
        // SHOOTER.prepareShooter(hub);

        // Auto aiming
        // if (!SHOOTER.isHubAligned())
        // {
            // angleToTurn = SHOOTER.getHubAngle();

            // DRIVETRAIN.drive(0.0, 0.0, -angleToTurn / 15.0 * (0.7 - 0.2) + 0.2 * Math.signum(-angleToTurn), true);
            // DRIVETRAIN.turnToAngle(0.1 * 2 * Math.PI, 0.5 * 2 * Math.PI, DRIVETRAIN.getGyro() - angleToTurn, Constant.HUB_ALIGNMENT_THRESHOLD);

            // if (angleToTurn > 0.0)
            // {
            //     DRIVETRAIN.drive(0.0, 0.0, -0.3, true);
            // }
            // else if (angleToTurn < 0.0)
            // {
            //     DRIVETRAIN.drive(0.0, 0.0, 0.3, true);
            // }
        // }
        

        // if(SHOOTER.isShooterReady())

        if((timer.get() > 1.5) && !(cargoShot == numberOfCargo))
        {
            timer.reset();

            // SHUTTLEFSM.requestFeedCargo();
            SHUTTLEFSM.fancyRun(true);

            System.out.println("Cargo shot into " + hub.level + " hub");
            cargoShot++;
        }
        else
        {
            SHUTTLEFSM.fancyRun(false);
        }
        
        if ((cargoShot == numberOfCargo) && (timer.get() > 1.0))
        {
            isFinished = true;
        }

        // distanceShotSimulation = distance_meters;
        // cargoShotSimulation++;
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {
        // PDH.setSwitchableChannel(false);

        timer.stop();

        INTAKE.compressorEnable();

        SHOOTER.stopShooter(); // TODO: Perhaps call stopShooter
    }

    public String toString()
    {
        return "ShootCargo(" + numberOfCargo + ", " + hub + ")";
    }
}

