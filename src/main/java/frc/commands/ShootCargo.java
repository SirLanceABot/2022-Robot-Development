package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Shooter;
import frc.robot.RobotContainer;
import frc.components.ShuttleFSM;
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
    private Timer timer = new Timer();
    private int numberOfCargo;
    private int cargoShot = 0;
    private double distance_meters;
    private Shooter.Hub hub;
    private double angleToTurn;
    private boolean isFinished;

    // These variables are only used to simulate cargo being shot
    // private int cargoShotSimulation = 0;
    // private double distanceShotSimulation = 0.0;


    // *** CLASS CONSTRUCTOR ***
    public ShootCargo(int numberOfCargo, double distance_meters, Shooter.Hub hub)
    {
        this.numberOfCargo = numberOfCargo;
        this.hub = hub;
        this.distance_meters = distance_meters;

        isFinished = false;
    }


    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        PDH.setSwitchableChannel(true);

        System.out.println(this);

        timer.reset();
        timer.start();

        isFinished = false;
        // cargoShotSimulation = 0;
    }

    public void execute()
    {
        // Auto aiming
        if (!SHOOTER.isHubAligned())
        {
            angleToTurn = SHOOTER.getHubAngle();

            if (angleToTurn > 0.0)
            {
                DRIVETRAIN.drive(0.0, 0.0, -0.3, true);
            }
            else if (angleToTurn < 0.0)
            {
                DRIVETRAIN.drive(0.0, 0.0, 0.3, true);
            }
        }

        SHOOTER.shoot(hub);

        // if(SHOOTER.isShooterReady())

        if(timer.get() > 2.0)
        {
            timer.reset();

            // SHUTTLEFSM.requestFeedCargo();
            SHUTTLEFSM.fancyRun(true);

            System.out.println("Cargo shot at " + hub.level + " hub at a distance of " + distance_meters + " m");
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
        PDH.setSwitchableChannel(false);

        timer.stop();
        SHOOTER.stopFlywheel();
    }

    public String toString()
    {
        return "ShootCargo(" + numberOfCargo + ", " + distance_meters + ", " + hub + ")";
    }
}
