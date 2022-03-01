package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class FlywheelData 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<FlywheelData> flywheelData = new ArrayList<FlywheelData>();

    private static int index;
    private static double distanceRatio;

    private double distance;
    private double speed;
    private double angle;

    private FlywheelData(double distance, double speed, double angle)
    {
        this.distance = distance;
        this.speed = speed;
        this.angle = angle;
    }

    public static void dataInit()
    {
        //put all data points here, DISTANCE MUST BE IN ORDER FROM LOWEST TO HIGHEST
        flywheelData.add(new FlywheelData(0.0, 0.0, 0.0));
    }

    //speed in rpms
    public static double getSpeed(double distance)
    {
        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance <= flywheelData.get(index).distance; index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        distanceRatio = (distance - flywheelData.get(index - 1).distance) / (flywheelData.get(index).distance - flywheelData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in speeds between current index and previous index, and then adds the base speed
        return distanceRatio * (flywheelData.get(index).speed - flywheelData.get(index - 1).speed) + flywheelData.get(index).speed;
    }

    //angle is in degrees in standard position
    public static double getAngle(double distance)
    {
        //this empty loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance <= flywheelData.get(index).distance; index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        distanceRatio = (distance - flywheelData.get(index - 1).distance) / (flywheelData.get(index).distance - flywheelData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in angles between current index and previous index, and then adds the base angle
        return distanceRatio * (flywheelData.get(index).angle - flywheelData.get(index - 1).angle) + flywheelData.get(index).angle;
    }
}