package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.vision.CameraWidget;

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
        cw.setLocation(0, 0, 16, 20); // small screen
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
            .withPosition(20, 0)
            .withSize(4, 2)
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

    /**
     * This method updates the LimeLight to set how images are seen
     * and set the LEDs
     */
    // public void updateLimeLightMode()
    // {
    //     boolean shooterMode = RobotContainer.SHOOTER.getIsShooting();
    //     // boolean shooterMode = true;

    //     NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    //     NetworkTableEntry camMode = table.getEntry("camMode");
    //     NetworkTableEntry stream = table.getEntry("stream");
    //     NetworkTableEntry ledMode = table.getEntry("ledMode");

    //     if(shooterMode)
    //     {
    //         camMode.setNumber(0.); // 0 target
    //         stream.setNumber(1.);  // 1 target with small intake
    //         ledMode.setNumber(3.); // 0 use pipeline setting
    //     }
    //     else
    //     {
    //         camMode.setNumber(1.); // 1 driver
    //         stream.setNumber(2.);  // 2 intake with small target
    //         ledMode.setNumber(3.); // 1 off
    //     }
    // }

    public void updateLimeLightMode()
    {
        boolean intakeMode = RobotContainer.INTAKE.getIsIntaking();
        // boolean intakeMode = true;

        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableEntry camMode = table.getEntry("camMode");
        NetworkTableEntry stream = table.getEntry("stream");
        NetworkTableEntry ledMode = table.getEntry("ledMode");

        if(intakeMode)
        {
            camMode.setNumber(1.); // 1 driver
            stream.setNumber(2.);  // 2 driver intake with small target
            ledMode.setNumber(1.); // 1 off
        }
        else
        {
            camMode.setNumber(0.); // 0 vision processor
            stream.setNumber(1.);  // 1 target with small driver
            ledMode.setNumber(3.); // 3 on
        }
    }

    // This method will be run on a slow period - say 1 second
    // LimeLight can't take it any faster and humans don't need it fast, either.
    public void updateCameraTab()
    {
        updateTimeRemaining();
        updateLimeLightMode();
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
