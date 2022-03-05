package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class TrajectoryData 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<TrajectoryData> trajectoryData = new ArrayList<TrajectoryData>();

    private static int index;
    private static double distanceRatio;

    private double distance;
    private double speed;
    private double angle;

    private TrajectoryData(double distance, double speed, double angle)
    {
        this.distance = distance;
        this.speed = speed;
        this.angle = angle;
    }

    public static void dataInit()
    {
        //put all data points here, DISTANCE MUST BE IN ORDER FROM LOWEST TO HIGHEST
        trajectoryData.add(new TrajectoryData(0.0, 0.0, 0.0));
    }

    //speed in rpms
    public static double getSpeed(double distance)
    {
        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance <= trajectoryData.get(index).distance; index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        distanceRatio = (distance - trajectoryData.get(index - 1).distance) / (trajectoryData.get(index).distance - trajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in speeds between current index and previous index, and then adds the base speed
        return distanceRatio * (trajectoryData.get(index).speed - trajectoryData.get(index - 1).speed) + trajectoryData.get(index).speed;
    }

    //angle is in degrees in standard position
    public static double getAngle(double distance)
    {
        //this empty loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance <= trajectoryData.get(index).distance; index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        distanceRatio = (distance - trajectoryData.get(index - 1).distance) / (trajectoryData.get(index).distance - trajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in angles between current index and previous index, and then adds the base angle
        return distanceRatio * (trajectoryData.get(index).angle - trajectoryData.get(index - 1).angle) + trajectoryData.get(index - 1).angle;
    }
}