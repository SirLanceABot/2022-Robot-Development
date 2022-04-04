package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;
import frc.components.Intake;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.components.Shooter;
import frc.vision.CameraWidget;
import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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
    // Create a Shuffleboard Tab
    private ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");

    private int oldTime = 0;

    // Create text output boxes
    private NetworkTableEntry timeRemaining;
    private NetworkTableEntry compressorState;

    // private String timeRemainingData = "void";
    private Double timeRemainingData = 0.0;

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");
        
        cameraTab = Shuffleboard.getTab("Camera");

        // timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No Errors");

        // limelight on shuffleboard
        CameraWidget cw = new CameraWidget(cameraTab);
        cw.name("LimeLight");
        cw.setLocation(0, 0, 19, 24); // small screen
        cw.setProperties(false, "white", false, "NONE");

        cw.createCameraShuffleboardWidgetLL("limelight", new String[]{"http://10.42.37.11:5800"}); // could get URLs from NT

        timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No data");

        System.out.println(fullClassName + ": Constructor Finished");
    }
    // private void makeTimeRemainingBox()
    // {
    //     Shuffleboard.getTab("Camera")
    //         .add("Time remaining", timeRemainingData)
    //         .withWidget(BuiltInWidgets.kTextView)
    //         .withPosition(1, 10)
    //         .withSize(26, 2)
    //         .getEntry();
    // }
    


    // *** CLASS & INSTANCE METHODS ***
    private NetworkTableEntry createTimeRemainingBox()
    {
        return cameraTab.add("Time Remaining", timeRemainingData.toString())
            .withWidget(BuiltInWidgets.kTextView)
            .withPosition(1, 10)
            .withSize(26, 2)
            .getEntry();
    }

    public void updateTimeRemaining()
    {
        timeRemainingData = Timer.getMatchTime();
        int timeRemainingInt = timeRemainingData.intValue();

        if (timeRemainingInt == -1)
        {
            timeRemaining.setString("0");
        }
        else if (timeRemainingInt != oldTime)
        {
            timeRemaining.setString("" + timeRemainingInt);
            oldTime = timeRemainingInt;
        }
    }

    public void updateLimeLightMode()
    {
        boolean shooterMode = false; //FIXME: testing; get the real value that means shooting commenced

        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableEntry camMode = table.getEntry("camMode");
        NetworkTableEntry stream = table.getEntry("stream");
            
        if(shooterMode)
        {
            camMode.setNumber(0.); // 0 target; 1 driver
            stream.setNumber(1.);  // 1 target with small intake; 2 intake with small target
        }
        else
        {
            camMode.setNumber(1.); // 0 target; 1 driver
            stream.setNumber(2.);  // 1 target with small intake; 2 intake with small target
        }
        
        { //FIXME: this stuff will go into shooter to get the hub angle and distance 
        NetworkTableEntry tx = table.getEntry("tx"); // angle to turn
        NetworkTableEntry ty = table.getEntry("ty"); // related to distance to hub
        NetworkTableEntry tv = table.getEntry("tv"); // <1. is no target (0 no target;1 target found)
        
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double valid = tv.getDouble(0.0);

        // testing - post to SmartDashboard to see that LL is working
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightValid", valid);
        SmartDashboard.putString("valid target", valid < 1.0 ? "not found" : "found");
        }
    }
/*

ledMode	Sets limelight’s LED state
0	use the LED Mode set in the current pipeline
1	force off
2	force blink
3	force on

camMode	Sets limelight’s operation mode
0	Vision processor
1	Driver Camera (Increases exposure, disables vision processing)

pipeline	Sets limelight’s current pipeline
0 .. 9	Select pipeline 0..9

stream	Sets limelight’s streaming mode
0	Standard - Side-by-side streams if a webcam is attached to Limelight
1	PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream
2	PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream

snapshot	Allows users to take snapshots during a match
0	Stop taking snapshots
1	Take two snapshots per second

*/

}
