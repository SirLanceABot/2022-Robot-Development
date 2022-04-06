package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import frc.robot.RobotContainer;
import frc.vision.CameraWidget;
import frc.components.Intake;

public class CameraTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    private static final Intake INTAKE = RobotContainer.INTAKE;


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

    private Double timeRemainingData = 0.0;
    String compressorStateString = "No data";

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // limelight on shuffleboard
        CameraWidget cw = new CameraWidget(cameraTab);
        cw.name("LimeLight");
        cw.setLocation(0, 0, 16, 20); // small screen
        cw.setProperties(false, "white", false, "NONE");

        cw.createCameraShuffleboardWidgetLL("limelight", new String[]{"http://10.42.37.11:5800"}); // could get URLs from NT

        timeRemaining = createTimeRemainingBox();
        compressorState = createCompressorStateBox();

        System.out.println(fullClassName + ": Constructor Finished");
    }

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

    private NetworkTableEntry createCompressorStateBox()
    {
        return cameraTab.add("Compressor State", compressorStateString)
            .withWidget(BuiltInWidgets.kTextView)
            .withPosition(20, 5)
            .withSize(4, 2)
            .getEntry();
    }

    public void updateCompressorState()
    {
        if(INTAKE != null)
        {
            if(INTAKE.isCompressorDisabled())
            {
                compressorStateString = "Disabled";
            }
            else if(INTAKE.isCompressorRunning())
            {
                compressorStateString = "Running";
            }
            else
            {
                compressorStateString = "Off";
            }
        }
        else
        {
            compressorStateString = "Off";
        }

        compressorState.setString(compressorStateString);
    }

    /**
     * This method updates the LimeLight to set how images are seen
     * and set the LEDs
     * 
     * We want to be in targeting vision processing mode in Autonomous or not Intake.
     * That is, driver mode is not autonomous and intaking (taking in).
     */

    public void updateLimeLightMode()
    {
        boolean driverMode = (RobotContainer.INTAKE != null) && (!DriverStation.isAutonomous()) ?
            RobotContainer.INTAKE.getIsIntaking() : false;
        
        // driverMode = true; // testing force driver mode

        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableEntry camMode = table.getEntry("camMode");
        NetworkTableEntry stream = table.getEntry("stream");
        NetworkTableEntry ledMode = table.getEntry("ledMode");

        if(driverMode)
        {
            camMode.setNumber(1.); // 1 driver
            stream.setNumber(2.);  // 2 driver intake with small target
            ledMode.setNumber(1.); // 1 off
        }
        else
        {
            camMode.setNumber(0.); // 0 vision processor
            stream.setNumber(0.);  // 1 target with small driver
            ledMode.setNumber(0.); // 0 pipeline setting
        }
    }

    // This method will be run on a slow period - say 1 second
    // LimeLight can't take it any faster and humans don't need it fast, either.
    public void updateCameraTab()
    {
        updateTimeRemaining();
        updateLimeLightMode();
        updateCompressorState();
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

//     boolean shooterMode = RobotContainer.SHOOTER.getIsShooting();
