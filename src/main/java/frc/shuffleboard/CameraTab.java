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

    // *** CLASS CONSTRUCTOR ***
    public CameraTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // timeRemaining = createTimeRemainingBox();
        // timeRemaining.setString("No Errors");
        cameraTab = Shuffleboard.getTab("Camera");
        // CameraWidget cw = new CameraWidget(cameraTab);
        // cw.name("Intake");
        // cw.setLocation(0, 0, 13, 14);
        // cw.setLocation(0, 0, 16, 19); // For big screen
        // cw.setProperties(false, "white", false, "NONE");
        // cw.createCameraShuffleboardWidget(intakeCameraServer.getSource());
        
        Shuffleboard.update();
        
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
}
