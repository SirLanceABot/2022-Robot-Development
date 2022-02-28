package frc.commands;

import java.lang.invoke.MethodHandles;

import frc.components.Shooter;
import frc.robot.RobotContainer;

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
    private int numberOfCargo;
    private double distance_meters;
    private Shooter.Hub hub;
    private boolean isFinished;

    // These variables are only used to simulate cargo being shot
    private int cargoShotSimulation = 0;
    private double distanceShotSimulation = 0.0;


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

        isFinished = false;
        cargoShotSimulation = 0;
    }

    public void execute()
    {
        distanceShotSimulation = distance_meters;
        cargoShotSimulation++;
        System.out.println("Cargo shot into " + hub.level + " hub at a distance of " + distanceShotSimulation + " m");
        
        if(cargoShotSimulation == numberOfCargo)
        {
            isFinished = true;
        }
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {

    }

    public String toString()
    {
        return "ShootCargo(" + numberOfCargo + ", " + distance_meters + ", " + hub + ")";
    }
}
