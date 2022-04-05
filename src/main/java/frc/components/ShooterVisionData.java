package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class ShooterVisionData
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<ShooterVisionData> shooterVisionData = new ArrayList<ShooterVisionData>();

    private static int index;
    private static double pixelRatio;
    private static boolean flag;

    private double pixels;
    private double distance;

    private static final double FEET_TO_METERS = 0.3048;

    private ShooterVisionData(double pixels, double distance)
    {
        this.pixels = pixels;
        this.distance = distance;
    }

    public static void dataInit()
    {
        //put all data points here, PIXELS MUST BE IN ORDER FROM LOWEST TO HIGHEST
        //must start at 0.0 pixels
        shooterVisionData.add(new ShooterVisionData(-33.9, 20.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-33.1, 19.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-32.7, 18.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-31.8, 17.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-30.8, 16.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-29.9, 15.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-29.1, 14.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-28.3, 13.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-27.2, 12.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-25.7, 11.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-24.1, 10.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-22.1, 9.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-20.3, 8.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-18.0, 7.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-14.9, 6.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-11.8, 5.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-8.6, 4.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(-3.6, 3.0 * FEET_TO_METERS));
        shooterVisionData.add(new ShooterVisionData(3.4, 2.0 * FEET_TO_METERS));
    }


    public static double getDistance(double pixels)
    {
        flag = true;

        //this loop makes sure the pixels of the index is greater than the passed pixels
        for (index = 0; index < shooterVisionData.size() && flag; index++)
        {
            if (pixels < shooterVisionData.get(index).pixels)
            {
                flag = false;
            }
        }

        if (index == 0)
        {
            index++;
        }
        else if (index == shooterVisionData.size())
        {
            index--;
        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        pixelRatio = (pixels - shooterVisionData.get(index - 1).pixels) / (shooterVisionData.get(index).pixels - shooterVisionData.get(index - 1).pixels);

        //multiplies pixelRatio by the difference in distance between current index and previous index, and then adds the base distance
        return pixelRatio * (shooterVisionData.get(index).distance - shooterVisionData.get(index - 1).distance) + shooterVisionData.get(index - 1).distance;
    }
}