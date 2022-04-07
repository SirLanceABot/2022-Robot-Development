package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class UpperTrajectoryData 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<UpperTrajectoryData> upperTrajectoryData = new ArrayList<UpperTrajectoryData>();

    private static int index;
    private static double distanceRatio;
    private static boolean flag;

    private static final double FEET_TO_METERS = 0.3048;

    //IN METERS
    private double distance;
    private double speed;
    private double angle;

    private UpperTrajectoryData(double distance, double speed, double angle)
    {
        this.distance = distance;
        this.speed = speed;
        this.angle = angle;
    }

    public static void dataInit()
    {
        //put all data points here, DISTANCE MUST BE IN ORDER FROM LOWEST TO HIGHEST
        //must be at least two data points to work
        // upperTrajectoryData.add(new UpperTrajectoryData(0.0 * FEET_TO_METERS, 2500.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(2.0 * FEET_TO_METERS, 1360.0, -235.0)); // Doesn't exist
        // upperTrajectoryData.add(new UpperTrajectoryData(5.0 * FEET_TO_METERS, 1400.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(6.0 * FEET_TO_METERS, 1400.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(8.0 * FEET_TO_METERS, 1450.0, -205.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(10.0 * FEET_TO_METERS, 1500.0, -190.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(12.0 * FEET_TO_METERS, 1700.0, -190.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(14.0 * FEET_TO_METERS, 1950.0, -180.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(16.0 * FEET_TO_METERS, 2100.0, -170.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(18.0 * FEET_TO_METERS, 2400.0, -155.0));

        //Kai's tuning
        upperTrajectoryData.add(new UpperTrajectoryData(4.0 * FEET_TO_METERS, 1400.0, -235.0));
        upperTrajectoryData.add(new UpperTrajectoryData(6.0 * FEET_TO_METERS, 1400.0, -235.0));
        upperTrajectoryData.add(new UpperTrajectoryData(8.0 * FEET_TO_METERS, 1425.0, -205.0));
        upperTrajectoryData.add(new UpperTrajectoryData(10.0 * FEET_TO_METERS, 1550.0, -190.0));
        upperTrajectoryData.add(new UpperTrajectoryData(12.0 * FEET_TO_METERS, 1700.0, -175.0));
        upperTrajectoryData.add(new UpperTrajectoryData(14.0 * FEET_TO_METERS, 1875.0, -170.0));
        upperTrajectoryData.add(new UpperTrajectoryData(16.0 * FEET_TO_METERS, 1975.0, -150.0));
        upperTrajectoryData.add(new UpperTrajectoryData(18.0 * FEET_TO_METERS, 2150.0, -140.0));
        upperTrajectoryData.add(new UpperTrajectoryData(20.0 * FEET_TO_METERS, 2400.0, -130.0));

        //new
        // upperTrajectoryData.add(new UpperTrajectoryData(4.0 * FEET_TO_METERS, 1300.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(5.0 * FEET_TO_METERS, 1350.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(6.0 * FEET_TO_METERS, 1400.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(8.0 * FEET_TO_METERS, 1450.0, -220.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(10.0 * FEET_TO_METERS, 1520.0, -200.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(12.0 * FEET_TO_METERS, 1700.0, -190.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(14.0 * FEET_TO_METERS, 1950.0, -180.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(16.0 * FEET_TO_METERS, 2100.0, -170.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(18.0 * FEET_TO_METERS, 2400.0, -155.0));

        //OLD DATA
        // upperTrajectoryData.add(new UpperTrajectoryData(0.0 * FEET_TO_METERS, 2500.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(2.0 * FEET_TO_METERS, 2700.0, -235.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(4.0 * FEET_TO_METERS, 2900.0, -220.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(6.0 * FEET_TO_METERS, 3100.0, -200.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(8.0 * FEET_TO_METERS, 3300.0, -190.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(10.0 * FEET_TO_METERS, 3500.0, -180.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(12.0 * FEET_TO_METERS, 3600.0, -170.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(14.0 * FEET_TO_METERS, 3700.0, -160.0));
        // upperTrajectoryData.add(new UpperTrajectoryData(16.0 * FEET_TO_METERS, 3900.0, -150.0));
    }

    //speed in rpms
    public static double getSpeed(double distance)
    {
        flag = true;

        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; index < upperTrajectoryData.size() && flag; index++)
        {
            if (distance < upperTrajectoryData.get(index).distance)
            {
                flag = false;
            }
        }

        if (index == 0)
        {
            index++;
        }
        else if (index == upperTrajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - upperTrajectoryData.get(index - 1).distance) / (upperTrajectoryData.get(index).distance - upperTrajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in speeds between current index and previous index, and then adds the base speed
        return distanceRatio * (upperTrajectoryData.get(index).speed - upperTrajectoryData.get(index - 1).speed) + upperTrajectoryData.get(index - 1).speed;
    }

    //angle is in degrees in standard position
    public static double getAngle(double distance)
    {
        flag = true;

        //this loop makes sure the distance of the index is greater than the passed distance
        for (index = 0; index < upperTrajectoryData.size() && flag; index++)
        {
            if (distance < upperTrajectoryData.get(index).distance)
            {
                flag = false;
            }
        }

        if (index == 0)
        {
            index++;
        }
        else if (index == upperTrajectoryData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        distanceRatio = (distance - upperTrajectoryData.get(index - 1).distance) / (upperTrajectoryData.get(index).distance - upperTrajectoryData.get(index - 1).distance);

        //multiplies distanceRatio by the difference in angles between current index and previous index, and then adds the base angle
        return distanceRatio * (upperTrajectoryData.get(index).angle - upperTrajectoryData.get(index - 1).angle) + upperTrajectoryData.get(index - 1).angle;
    }
}