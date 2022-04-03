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
import frc.components.Shooter;
import frc.vision.CameraWidget;
import edu.wpi.first.util.sendable.SendableRegistry;

import edu.wpi.first.networktables.NetworkTableEntry;
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

    
    NetworkTable table;

    // example x, y, valid for shooter - not used here
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry tv;
    // actually need to read values periodically for shooter not here
    double x;
    double y;
    double valid;

    // manipulate the images based of shooter mode or not
    NetworkTableEntry camMode;
    NetworkTableEntry stream;

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");
        
        cameraTab = Shuffleboard.getTab("Camera");
        // Shuffleboard.update();

        // timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No Errors");

        // limelight on shuffleboard
        CameraWidget cw = new CameraWidget(cameraTab);
        cw.name("LimeLight");
        cw.setLocation(0, 0, 19, 24); // small screen
        cw.setProperties(false, "white", false, "NONE");

        cw.createCameraShuffleboardWidgetLL("limelight", new String[]{"http://10.42.37.11:5800"}); // could get URLs from NT
        // Shuffleboard.update();
        NetworkTableInstance.getDefault().flush();

        table = NetworkTableInstance.getDefault().getTable("limelight");

        // example x, y, valid for shooter - not used here
        tx = table.getEntry("tx"); // angle to turn
        ty = table.getEntry("ty"); // related to distance
        tv = table.getEntry("tv"); // valid target
        // actually need to read values periodically for shooter not here
        x = tx.getDouble(0.0);
        y = ty.getDouble(0.0);
        valid = tv.getDouble(0.0);
    
        // manipulate the images based of shooter mode or not
        camMode = table.getEntry("camMode"); // 0 target; 1 driver
        stream = table.getEntry("stream"); // 1 target with small intake; 2 intake with small target
    
        checkLLShooterMode(); // testing; this belongs in a periodic

        // Shuffleboard.update();

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
    
    // public void cameraStreamMode()
    // {
    //     if(cameraMode != null)
    //         cameraMode = cameraMode.getDouble(2.0);
    // }

    // private void createCameraModeBox()
    // {
    //     //create and name the Box
    //     SendableRegistry.add(cameraModeBox, "Camera Mode");
    //     SendableRegistry.setName(cameraModeBox, "Camera Mode");
        
    //     //add options to  Box
    //     cameraModeBox.setDefaultOption("0", CameraTabData.CameraMode.k0);
    //     cameraModeBox.addOption("1", CameraTabData.CameraMode.k1);
    //     cameraModeBox.addOption("2", CameraTabData.CameraMode.k1);

    //     //put the widget on the shuffleboard
    //     CameraTab.add(cameraModeBox)
    //         .withWidget(BuiltInWidgets.kSplitButtonChooser)
    //         .withPosition(0, 0)
    //         .withSize(8, 2);
    // }

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

    boolean shooterMode = false; // testing; get the real value below

    public void checkLLShooterMode()
    {
        if(shooterMode)
        {
            System.out.println("shooter Mode");
            System.out.println("camMode " + camMode.setDouble(0.));
            System.out.println("stream " + stream.setDouble(1.));
        }
        else
        {
            System.out.println("intake Mode");
            camMode.setDouble(1.);
            stream.setDouble(2.);
        }
    }
/*
NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(<value>);

double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

if (tv < 1.0)
{
    m_LimelightHasValidTarget = false;
    m_LimelightDriveCommand = 0.0;
    m_LimelightSteerCommand = 0.0;
}

NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

NetworkTableEntry tx = table.getEntry("tx");
NetworkTableEntry ty = table.getEntry("ty");
NetworkTableEntry ta = table.getEntry("tv");

//read values periodically
double x = tx.getDouble(0.0);
double y = ty.getDouble(0.0);
double area = tv.getDouble(0.0);

  
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
