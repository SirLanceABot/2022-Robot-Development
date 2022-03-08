package frc.components;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class ShroudData 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static ArrayList<ShroudData> shroudData = new ArrayList<ShroudData>();

    private static int index;
    private static double voltageRatio;

    private double voltage;
    private double angle;

    private ShroudData(double voltage, double angle)
    {
        this.voltage = voltage;
        this.angle = angle;
    }

    public static void dataInit()
    {
        //put all data points here, VOLTAGE MUST BE IN ORDER FROM LOWEST TO HIGHEST
        //must start with lower limit and end with upper limit, DO NOT LEAVE OUT
        shroudData.add(new ShroudData(-1000.0, -1000.0));
        shroudData.add(new ShroudData(1000.0, 1000.0));
    }

    //angle is in degrees in standard position
    public static double getAngle(double voltage)
    {
        //this empty loop makes sure the voltage of the index is greater than the passed voltage
        for (index = 0; voltage >= shroudData.get(index).voltage && index < shroudData.size(); index++)
        {

        }

        //distance in between current index and index one position behind, should be number from 0.0 to 1.0, not including 1.0
        //number is negative if input is below lowest point
        //number is above 1 if input is above highest point
        voltageRatio = (voltage - shroudData.get(index - 1).voltage) / (shroudData.get(index).voltage - shroudData.get(index - 1).voltage);

        //multiplies voltageRatio by the difference in angles between current index and previous index, and then adds the base angle
        return voltageRatio * (shroudData.get(index).angle - shroudData.get(index - 1).angle) + shroudData.get(index - 1).angle;
    }
}