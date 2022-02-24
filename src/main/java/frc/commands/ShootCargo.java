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
    // TODO: enum for upper/lower hub
    private boolean isFinished;

    // These variables are only used to simulate cargo being shot
    private int cargoShotSimulation = 0;
    private double distanceShotSimulation = 0.0;
    // TODO: enum for upper/lower hub


    // *** CLASS CONSTRUCTOR ***
    public ShootCargo(int numberOfCargo, double distance_meters)
    {
        this.numberOfCargo = numberOfCargo;
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
        System.out.println("Cargo shot at a distance of " + distanceShotSimulation + " m");
        // TODO: does distanceShotSimulation need to exist? Can distance_meters just be displayed instead?
        
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
        return "ShootCargo()";
    }
}
