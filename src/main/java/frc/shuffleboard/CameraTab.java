package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTableEntry;

public class CameraTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***

    // Create text output boxes
    private NetworkTableEntry timeRemaining;
    private NetworkTableEntry compressorState;

    private String timeRemainingNoDataMessage = "No data";

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No Errors");

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***
    // private NetworkTableEntry createTimeRemainingBox()
    // {
    //     return CameraTab.add("Error Messages", timeRemainingNoDataMessage)
    //          .withWidget(BuiltInWidgets.kTextView)
    //          .withPosition(1, 10)
    //          .withSize(26, 2)
    //          .getEntry();
    // }

    public void updateTimeRemaining()
    {
        timeRemaining.setString(timeRemainingNoDataMessage);
    }
    
}
