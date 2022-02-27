package frc.components;

import java.lang.invoke.MethodHandles;

import frc.components.Events.ShuttleEvent;
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
    private static final SensorValues PREVIOUS_SENSOR_VALUES = new SensorValues();
    private static final SensorValues CURRENT_SENSOR_VALUES = RobotContainer.CURRENT_SENSOR_VALUES;

    private Events.ShuttleEvent shuttleEvent = Events.ShuttleEvent.NONE;

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
        CURRENT_SENSOR_VALUES.updateValues();

        // Call each component's determineEvent
        determineShuttleEvent(shoot);
    }

    // Determine what event based on proximity sensors and if shoot command is given
    // FIXME: Use controller in here to get shoot
    private void determineShuttleEvent(boolean shoot)
    {
        // TODO: Decide if need to refactor these to current____SensorValue
        boolean currentShuttleIntake = CURRENT_SENSOR_VALUES.getShuttleIntake();
        boolean currentShuttleStageOne = CURRENT_SENSOR_VALUES.getShuttleStageOne();
        boolean currentShuttleStageTwo = CURRENT_SENSOR_VALUES.getShuttleStageTwo();

        // Initially say there is no event then continue to look for an event
        Events.ShuttleEvent determinedEvent = Events.ShuttleEvent.NONE;

        if(currentShuttleIntake != PREVIOUS_SENSOR_VALUES.getShuttleIntake())
        {
            if (currentShuttleIntake)
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleIntake(currentShuttleIntake);
        }
        else if(currentShuttleStageOne != PREVIOUS_SENSOR_VALUES.getShuttleStageOne())
        {
            if (currentShuttleStageOne)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleStageOne(currentShuttleStageOne);
        }
        else if(currentShuttleStageTwo != PREVIOUS_SENSOR_VALUES.getShuttleStageTwo())
        {
            if (currentShuttleStageTwo)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            PREVIOUS_SENSOR_VALUES.setShuttleStageTwo(currentShuttleStageTwo);
        }
        else if(shoot)
        {
            determinedEvent = Events.ShuttleEvent.SHOOT_IS_CALLED;
        }

        shuttleEvent = determinedEvent;
    }

    public Events.ShuttleEvent getShuttleEvent()
    {
        return shuttleEvent;
    }
    
}
