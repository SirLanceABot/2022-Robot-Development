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

    // Separated by component

    // Shuttle
    private boolean shuttleIntakeSensorValue = false;
    private boolean shuttleStageOneSensorValue = false;
    private boolean shuttleStageTwoSensorValue = false;

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
}
