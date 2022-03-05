package frc.components;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;

public class SensorValues 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***

    

    // *** CLASS & INSTANCE VARIABLES ***

    // Component objects
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;

    // Separated by component

    // Shuttle
    private boolean shuttleIntakeSensorValue = false;
    private boolean shuttleStageOneSensorValue = false;
    private boolean shuttleStageTwoSensorValue = false;

    // CargoManager
    private int cargoCount = 0;
    private boolean isShooterReady = false;
    private boolean isHubCentered = false;

    // *** CLASS CONSTRUCTOR ***
    public SensorValues()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    public void updateValues()
    {
        // Shuttle
        shuttleIntakeSensorValue = SHUTTLE.measureIntakeSensor();
        shuttleStageOneSensorValue = SHUTTLE.measureStageOneSensor();
        shuttleStageTwoSensorValue = SHUTTLE.measureStageTwoSensor();

        // CargoManager
        cargoCount = SHUTTLEFSM.getCargoCount();
        isShooterReady = false; // TODO: Use Elliot's code
        isHubCentered = false;
    }

    // Shuttle setters

    public void setShuttleIntake(boolean shuttleIntakeSensorValue)
    {
        this.shuttleIntakeSensorValue = shuttleIntakeSensorValue;
    }

    public void setShuttleStageOne(boolean shuttleStageOneSensorValue)
    {
        this.shuttleStageOneSensorValue = shuttleStageOneSensorValue;
    }

    public void setShuttleStageTwo(boolean shuttleStageTwoSensorValue)
    {
        this.shuttleStageTwoSensorValue = shuttleStageTwoSensorValue;
    }

    // CargoManager setters

    public void setCargoCount(int cargoCount)
    {
        this.cargoCount = cargoCount;
    }

    public void setIsShooterReady(boolean isShooterReady)
    {
        this.isShooterReady = isShooterReady;
    }

    public void setIsHubCentered(boolean isHubCentered)
    {
        this.isHubCentered = isHubCentered;
    }

    // Shuttle getters

    /**
     * True means sensor is active
     * @return shuttleIntakeSensorValue
     */
    public boolean getShuttleIntake()
    {
        return shuttleIntakeSensorValue;
    }

    /**
     * True means sensor is active
     * @return shuttleStageOneSensorValue
     */
    public boolean getShuttleStageOne()
    {
        return shuttleStageOneSensorValue;
    }

    /**
     * True means sensor is active
     * @return shuttleStageTwoSensorValue
     */
    public boolean getShuttleStageTwo()
    {
        return shuttleStageTwoSensorValue;
    }

    // CargoManager getters

    /**
     * The number of cargo in the shuttle
     * @return cargoCount
     */
    public int getCargoCount()
    {
        return cargoCount;
    }

    /**
     * Is the shooter (flywheel and shroud) ready
     * @return isShooterReady
     */
    public boolean getIsShooterReady()
    {
        return isShooterReady;
    }

    /**
     * Is the hub centered in the camera
     * // TODO: Change to isRobotAimed
     * @return isHubCentered
     */
    public boolean getIsHubCentered()
    {
        return isHubCentered;
    }
}
