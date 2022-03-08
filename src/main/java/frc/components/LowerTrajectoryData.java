package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class LowerTrajectoryData 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<LowerTrajectoryData> trajectoryData = new ArrayList<LowerTrajectoryData>();

    private static int index;
    private static double distanceRatio;

    private static final double FEET_TO_METERS = 0.3048;

    //IN METERS
    private double distance;
    private double speed;
    private double angle;

    private LowerTrajectoryData(double distance, double speed, double angle)
    {
        this.distance = distance;
        this.speed = speed;
        this.angle = angle;
    }

    public static void dataInit()
    {
        //put all data points here, DISTANCE MUST BE IN ORDER FROM LOWEST TO HIGHEST
        //must be at least two data points to work
        trajectoryData.add(new LowerTrajectoryData(0.0 * FEET_TO_METERS, 2500.0, -235.0));
    }

    //speed in rpms
    public static double getSpeed(double distance)
    {
        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance >= trajectoryData.get(index).distance && index < trajectoryData.size(); index++)
        {

        }

        if (index == 0)
        {
            index++;
        }
        else if (index == trajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - trajectoryData.get(index - 1).distance) / (trajectoryData.get(index).distance - trajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in speeds between current index and previous index, and then adds the base speed
        return distanceRatio * (trajectoryData.get(index).speed - trajectoryData.get(index - 1).speed) + trajectoryData.get(index - 1).speed;
    }

    //angle is in degrees in standard position
    public static double getAngle(double distance)
    {
        //this empty loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; distance >= trajectoryData.get(index).distance && index < trajectoryData.size(); index++)
        {

        }

        if (index == 0)
        {
            index++;
        }
        else if (index == trajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - trajectoryData.get(index - 1).distance) / (trajectoryData.get(index).distance - trajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in angles between current index and previous index, and then adds the base angle
        return distanceRatio * (trajectoryData.get(index).angle - trajectoryData.get(index - 1).angle) + trajectoryData.get(index - 1).angle;
    }
}