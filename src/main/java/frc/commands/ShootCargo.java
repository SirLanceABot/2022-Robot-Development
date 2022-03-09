package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Shooter;
import frc.robot.RobotContainer;
import frc.components.ShuttleFSM;

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
    private int numberOfCargo;
    private int cargoShot = 0;
    private double distance_meters;
    private Shooter.Hub hub;
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
        System.out.println(this);

        SHOOTER.shoot(hub, distance_meters);

        isFinished = false;
        // cargoShotSimulation = 0;
    }

    public void execute()
    {
        if(SHOOTER.isShooterReady())
        {
            SHUTTLEFSM.feedCargo();

            System.out.println("Cargo shot at " + hub.level + " hub at a distance of " + distance_meters + " m");
            cargoShot++;
        }
        
        if ((cargoShot == numberOfCargo) || (numberOfCargo == 0))
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
        SHOOTER.stopFlywheel();
    }

    public String toString()
    {
        return "ShootCargo(" + numberOfCargo + ", " + distance_meters + ", " + hub + ")";
    }
}
