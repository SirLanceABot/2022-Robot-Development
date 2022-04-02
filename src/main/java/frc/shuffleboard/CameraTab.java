package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;
import frc.components.Intake;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.components.Shooter;
import frc.vision.CameraWidget;
import edu.wpi.first.util.sendable.SendableRegistry;

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
    static ShuffleboardTab cameraTab;

    private String timeRemainingNoDataMessage = "No data";

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");
        
        cameraTab = Shuffleboard.getTab("Camera");
        Shuffleboard.update();

        // timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No Errors");

        // limelight on shuffleboard
        CameraWidget cw = new CameraWidget(cameraTab);
        cw.name("LimeLight");
        cw.setLocation(0, 0, 19, 24); // small screen
        cw.setProperties(false, "white", false, "NONE");

        cw.createCameraShuffleboardWidgetLL("limelight", new String[]{"http://10.42.37.11:5800"}); // could get URLs from NT
        
        Shuffleboard.update();

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

}
