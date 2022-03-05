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

    private double pixels;
    private double distance;

    private ShooterVisionData(double pixels, double distance)
    {
        this.pixels = pixels;
        this.distance = distance;
    }

    public static void dataInit()
    {
        //put all data points here, PIXELS MUST BE IN ORDER FROM LOWEST TO HIGHEST
        //must start at 0.0 pixels
        shooterVisionData.add(new ShooterVisionData(0.0, 0.0));
    }


    public static double getDistance(double pixels)
    {
        //this empty loop makes sure the pixels of the index is greater than the passed pixels
        for (index = 0; pixels <= shooterVisionData.get(index).pixels; index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        pixelRatio = (pixels - shooterVisionData.get(index - 1).pixels) / (shooterVisionData.get(index).pixels - shooterVisionData.get(index - 1).pixels);

        //multiplies pixelRatio by the difference in distance between current index and previous index, and then adds the base distance
        return pixelRatio * (shooterVisionData.get(index).distance - shooterVisionData.get(index - 1).distance) + shooterVisionData.get(index - 1).distance;
    }
}