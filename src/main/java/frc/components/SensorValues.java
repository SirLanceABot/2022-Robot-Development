package frc.components;

import java.lang.invoke.MethodHandles;

import frc.constants.Constant;
import frc.controls.DriverController;
import frc.controls.DriverController.DriverButtonAction;
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
    private class DebouncedBoolean
    {
        boolean lastValue;
        int counter;

        DebouncedBoolean(boolean initialValue)
        {
            lastValue = initialValue;
        }

        public void update(boolean currentValue)
        {
            if (currentValue != lastValue)
            {
                counter++;
            }
            else
            {
                counter = 0;
            }

            if (counter >= Constant.DEBOUNCE_THRESHOLD)
            {
                lastValue = !lastValue;
            }
        }

        public boolean get()
        {
            return lastValue;
        }
    }

    // *** CLASS & INSTANCE VARIABLES ***

    // Component objects
    private static final DriverController DRIVER_CONTROLLER = RobotContainer.DRIVER_CONTROLLER;
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;

    // Separated by component

    // DriverController
    private boolean shootControllerInput = false;
    private boolean armToggleControllerInput = false;
    private boolean rollerToggleControllerInput = false;

    // Shuttle
    private boolean shuttleIntakeSensorValue = false;
    private boolean shuttleStageOneSensorValue = false;
    private boolean shuttleStageTwoSensorValue = false;
    private boolean isFeedCargoRequested = false;

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
        // DriverController
        if (DRIVER_CONTROLLER != null)
        {
            // TODO: Refactor names if necessary
            shootControllerInput = DRIVER_CONTROLLER.getAction(DriverButtonAction.kShoot);
            armToggleControllerInput = DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeExtendToggle);
            rollerToggleControllerInput = DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff);
        }

        // Shuttle
        if (SHUTTLE != null)
        {
            shuttleIntakeSensorValue = SHUTTLE.measureIntakeSensor();
            shuttleStageOneSensorValue = SHUTTLE.measureStageOneSensor();
            shuttleStageTwoSensorValue = SHUTTLE.measureStageTwoSensor();
        }
        if(SHUTTLEFSM != null)
        {
            isFeedCargoRequested = SHUTTLEFSM.isFeedCargoRequested();
        }

        // CargoManager
        if (SHUTTLEFSM != null)
        {
            cargoCount = SHUTTLEFSM.getCargoCount();
        }

        if (SHOOTER != null)
        {
            isShooterReady = false; // TODO: Use Elliot's code
            isHubCentered = false;
        }
    }

    // DriverController setters

    public void setShootControllerInput(boolean shootControllerInput)
    {
        this.shootControllerInput = shootControllerInput;
    }

    public void setArmToggleControllerInput(boolean armToggleControllerInput)
    {
        this.armToggleControllerInput = armToggleControllerInput;
    }

    public void setRollerToggleControllerInput(boolean rollerToggleControllerInput)
    {
        this.rollerToggleControllerInput = rollerToggleControllerInput;
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

    public void setIsFeedCargoRequested(boolean isFeedCargoRequested)
    {
        this.isFeedCargoRequested = isFeedCargoRequested;
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

    // DriverController getters

    public boolean getShootControllerInput()
    {
        return shootControllerInput;
    }

    public boolean getArmToggleControllerInput()
    {
        return armToggleControllerInput;
    }

    public boolean getRollerToggleControllerInput()
    {
        return rollerToggleControllerInput;
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

    /**
     * Is feed cargo requested for ShuttleFSM
     * @return isFeedCargoRequested
     */
    public boolean getIsFeedCargoRequested()
    {
        // Debugging feedCargo
        // System.out.println("Gotten value of isFeedCargoRequested " + isFeedCargoRequested);
        return isFeedCargoRequested;
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
