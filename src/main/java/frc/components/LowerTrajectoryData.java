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

    private static ArrayList<LowerTrajectoryData> lowerTrajectoryData = new ArrayList<LowerTrajectoryData>();

    private static int index;
    private static double distanceRatio;
    private static boolean flag;

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
        //OLD DATA
        // lowerTrajectoryData.add(new LowerTrajectoryData(0.0 * FEET_TO_METERS, 1500.0, -215.0));
        // lowerTrajectoryData.add(new LowerTrajectoryData(2.0 * FEET_TO_METERS, 1800.0, -170.0));
        // lowerTrajectoryData.add(new LowerTrajectoryData(4.0 * FEET_TO_METERS, 2000.0, -150.0));
        // lowerTrajectoryData.add(new LowerTrajectoryData(6.0 * FEET_TO_METERS, 2200.0, -130.0));

        //Kai's tuning
        lowerTrajectoryData.add(new LowerTrajectoryData(0.0 * FEET_TO_METERS, 600.0, -200.0));
        lowerTrajectoryData.add(new LowerTrajectoryData(2.0 * FEET_TO_METERS, 650.0, -150.0));
        lowerTrajectoryData.add(new LowerTrajectoryData(4.0 * FEET_TO_METERS, 750.0, -150.0));
        lowerTrajectoryData.add(new LowerTrajectoryData(6.0 * FEET_TO_METERS, 850.0, -150.0));
    }

    //speed in rpms
    public static double getSpeed(double distance)
    {
        flag = true;

        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; index < lowerTrajectoryData.size() && flag; index++)
        {
            if (distance < lowerTrajectoryData.get(index).distance)
            {
                flag = false;
            }
        }

        if (index == 0)
        {
            index++;
        }
        else if (index == lowerTrajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - lowerTrajectoryData.get(index - 1).distance) / (lowerTrajectoryData.get(index).distance - lowerTrajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in speeds between current index and previous index, and then adds the base speed
        return distanceRatio * (lowerTrajectoryData.get(index).speed - lowerTrajectoryData.get(index - 1).speed) + lowerTrajectoryData.get(index - 1).speed;
    }

    //angle is in degrees in standard position
    public static double getAngle(double distance)
    {
        flag = true;

        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; index < lowerTrajectoryData.size() && flag; index++)
        {
            if (distance < lowerTrajectoryData.get(index).distance)
            {
                flag = false;
            }
        }

        if (index == 0)
        {
            index++;
        }
        else if (index == lowerTrajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - lowerTrajectoryData.get(index - 1).distance) / (lowerTrajectoryData.get(index).distance - lowerTrajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in angles between current index and previous index, and then adds the base angle
        return distanceRatio * (lowerTrajectoryData.get(index).angle - lowerTrajectoryData.get(index - 1).angle) + lowerTrajectoryData.get(index - 1).angle;
    }
}