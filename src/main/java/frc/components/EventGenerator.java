package frc.components;

import java.lang.invoke.MethodHandles;

import frc.controls.DriverController;
import frc.robot.RobotContainer;

public class EventGenerator 
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
    // TODO: Change this to two objects instead of one
    private static final SensorValues PREVIOUS_SENSOR_VALUES = new SensorValues();
    private static final SensorValues CURRENT_SENSOR_VALUES = RobotContainer.CURRENT_SENSOR_VALUES;

    private Events.ShuttleEvent shuttleEvent = Events.ShuttleEvent.NONE;
    private Events.CargoManagerEvent cargoManagerEvent = Events.CargoManagerEvent.NONE;

    // *** CLASS CONSTRUCTOR ***
    public EventGenerator()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // TODO: Perhaps deep copy CURRENT_SENSOR_VALUES to PREVIOUS_SENSOR_VALUES
        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    public void determineEvents(boolean shoot)
    {
        // Update data of input for event generation
        if(CURRENT_SENSOR_VALUES != null)
        {
            CURRENT_SENSOR_VALUES.updateValues();
        }

        // Call each component's determineEvent
        determineShuttleEvent(shoot);
        determineCargoManagerEvent();
    }

    // Determine what event based on proximity sensors and if shoot command is given
    private void determineShuttleEvent(boolean shoot)
    {
        boolean currentShuttleIntakeSensorValue = CURRENT_SENSOR_VALUES.getShuttleIntake();
        boolean currentShuttleStageOneSensorValue = CURRENT_SENSOR_VALUES.getShuttleStageOne();
        boolean currentShuttleStageTwoSensorValue = CURRENT_SENSOR_VALUES.getShuttleStageTwo();
        boolean currentIsFeedCargoRequested = CURRENT_SENSOR_VALUES.getIsFeedCargoRequested();

        // Debugging feedCargo
        // if (currentIsFeedCargoRequested)
        // {
        //     System.out.println("Feed cargo requsted: " + currentIsFeedCargoRequested);
        // }
        
        // Initially say there is no event then continue to look for an event
        Events.ShuttleEvent determinedEvent = Events.ShuttleEvent.NONE;

        // Looking at sensor changes are the highest priority
        if(currentShuttleIntakeSensorValue != PREVIOUS_SENSOR_VALUES.getShuttleIntake())
        {
            if (currentShuttleIntakeSensorValue)
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleIntake(currentShuttleIntakeSensorValue);
        }
        else if(currentShuttleStageOneSensorValue != PREVIOUS_SENSOR_VALUES.getShuttleStageOne())
        {
            if (currentShuttleStageOneSensorValue)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleStageOne(currentShuttleStageOneSensorValue);
        }
        else if(currentShuttleStageTwoSensorValue != PREVIOUS_SENSOR_VALUES.getShuttleStageTwo())
        {
            if (currentShuttleStageTwoSensorValue)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleStageTwo(currentShuttleStageTwoSensorValue);
        }
        else if(shoot)
        {
            determinedEvent = Events.ShuttleEvent.FEED_CARGO;
        }
        // TODO: Make this work
        /*
        else if(currentIsFeedCargoRequested != PREVIOUS_SENSOR_VALUES.getIsFeedCargoRequested())
        {
            if (currentIsFeedCargoRequested)
            {
                // Debugging feedCargo
                // System.out.println("FEEEEEEEEEEEEEEEEEEEEEEEDED"); // testing
                determinedEvent = Events.ShuttleEvent.FEED_CARGO;
            }
            else
            {
                // Do not feed cargo
            }

            PREVIOUS_SENSOR_VALUES.setIsFeedCargoRequested(currentIsFeedCargoRequested);
        }
        */

        shuttleEvent = determinedEvent;
    }

    public Events.ShuttleEvent getShuttleEvent()
    {
        return shuttleEvent;
    }
    
    public void determineCargoManagerEvent()
    {
        // Store current values to variables to save on getter calls

        // Sensor input
        int currentCargoCount = CURRENT_SENSOR_VALUES.getCargoCount();
        boolean currentIsShooterReady = CURRENT_SENSOR_VALUES.getIsShooterReady();
        boolean currentIsHubCentered = CURRENT_SENSOR_VALUES.getIsHubCentered();

        // Controller input
        boolean currentShootControllerInput = CURRENT_SENSOR_VALUES.getShootControllerInput();
        boolean currentArmToggleControllerInput = CURRENT_SENSOR_VALUES.getArmToggleControllerInput();
        boolean currentRollerToggleControllerInput = CURRENT_SENSOR_VALUES.getRollerToggleControllerInput();

        // Store previous values to variables to save on getter calls

        int previousCargoCount = PREVIOUS_SENSOR_VALUES.getCargoCount();

        // Initially say there is no event then continue to look for an event
        Events.CargoManagerEvent determinedEvent = Events.CargoManagerEvent.NONE;

        // Looking at sensor changes are the highest priority
        if(currentCargoCount != previousCargoCount)
        {
            if(currentCargoCount < previousCargoCount && currentCargoCount == 0)
            {
                determinedEvent = Events.CargoManagerEvent.SHUTTLE_EMPTY;
            }
            else if(currentCargoCount < previousCargoCount && currentCargoCount == 1)
            {
                determinedEvent = Events.CargoManagerEvent.SHOT_ONE_OF_TWO;
            }
            else if(currentCargoCount > previousCargoCount && currentCargoCount == 2)
            {
                determinedEvent = Events.CargoManagerEvent.SHUTTLE_FULL;
            }

            PREVIOUS_SENSOR_VALUES.setCargoCount(currentCargoCount);
        }
        // Both isShooterReady and isHubCentered changed
        // Higher priority than the individual ones because those would cause this case to be skipped
        else if(currentIsShooterReady != PREVIOUS_SENSOR_VALUES.getIsShooterReady() && currentIsHubCentered != PREVIOUS_SENSOR_VALUES.getIsHubCentered())
        {
            if (currentIsShooterReady && currentIsHubCentered)
            {
                determinedEvent = Events.CargoManagerEvent.HUB_IS_CENTERED_AND_SHOOTER_READY;
            }
            else
            {
                // Shooter not ready or hub not centered
            }
    
            PREVIOUS_SENSOR_VALUES.setIsShooterReady(currentIsShooterReady);
            PREVIOUS_SENSOR_VALUES.setIsHubCentered(currentIsHubCentered);
        }
        else if(currentIsShooterReady != PREVIOUS_SENSOR_VALUES.getIsShooterReady())
        {
            if (currentIsShooterReady)
            {
                determinedEvent = Events.CargoManagerEvent.SHOOTER_READY;
            }
            else
            {
                // Shooter not ready
            }
    
            PREVIOUS_SENSOR_VALUES.setIsShooterReady(currentIsShooterReady);
        }
        else if(currentIsHubCentered != PREVIOUS_SENSOR_VALUES.getIsHubCentered())
        {
            if (currentIsHubCentered)
            {
                determinedEvent = Events.CargoManagerEvent.HUB_IS_CENTERED;
            }
            else
            {
                // Hub not centered
            }
    
            PREVIOUS_SENSOR_VALUES.setIsHubCentered(currentIsHubCentered);
        }
        // Controller input is lower priority than sensors
        else if(currentShootControllerInput != PREVIOUS_SENSOR_VALUES.getShootControllerInput())
        {
            if (currentShootControllerInput)
            {
                determinedEvent = Events.CargoManagerEvent.SHOOT_IS_CALLED;
            }
            else
            {
                // Shoot not called
            }
    
            PREVIOUS_SENSOR_VALUES.setShootControllerInput(currentShootControllerInput);
        }
        else if(currentArmToggleControllerInput != PREVIOUS_SENSOR_VALUES.getArmToggleControllerInput())
        {
            if (currentArmToggleControllerInput)
            {
                determinedEvent = Events.CargoManagerEvent.ARM_TOGGLE;
            }
            else
            {
                // Don't toggle arm
            }
    
            PREVIOUS_SENSOR_VALUES.setArmToggleControllerInput(currentArmToggleControllerInput);
        }
        else if(currentRollerToggleControllerInput != PREVIOUS_SENSOR_VALUES.getRollerToggleControllerInput())
        {
            if (currentRollerToggleControllerInput)
            {
                determinedEvent = Events.CargoManagerEvent.ROLLER_TOGGLE;
            }
            else
            {
                // Don't toggle roller
            }
    
            PREVIOUS_SENSOR_VALUES.setRollerToggleControllerInput(currentRollerToggleControllerInput);
        }

        cargoManagerEvent = determinedEvent;
    }

    public Events.CargoManagerEvent getCargoManagerEvent()
    {
        return cargoManagerEvent;
    }
}
