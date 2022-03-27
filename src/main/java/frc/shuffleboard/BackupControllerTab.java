package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.util.sendable.SendableRegistry;

public class BackupControllerTab 
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
    private ShuffleboardTab backupControllerTab = Shuffleboard.getTab("Backup Controller");

    // Create an object to store the data in the Boxes
    private BackupControllerTabData backupControllerTabData = new BackupControllerTabData();

    // Create the Box objects
    // private SendableChooser<BackupControllerTabData.CompressorToggle> compressorToggleBox = new SendableChooser<>();
    // private SendableChooser<BackupControllerTabData.ClimbToggle> climbToggleBox = new SendableChooser<>();
    // private SendableChooser<BackupControllerTabData.IntakeToggle> intakeToggleBox = new SendableChooser<>();
    // private SendableChooser<BackupControllerTabData.RollerToggle> rollerToggleBox = new SendableChooser<>();
    // private SendableChooser<BackupControllerTabData.Aim> aimBox = new SendableChooser<>();
    // private SendableChooser<BackupControllerTabData.Shoot> shootBox = new SendableChooser<>();
     
    private NetworkTableEntry successfulDownload;
    private NetworkTableEntry errorMessageBox;

     // Create the Button object
     private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
     private boolean previousStateOfSendButton = false;
     private boolean isDataValid = true;
     private String errorMessage = "No Errors";

     
    // *** CLASS CONSTRUCTOR ***
    public BackupControllerTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // createCompressorToggleBox();
        // createClimbToggleBox();
        // createIntakeToggletBox();
        // createRollerToggleBox();
        // createAimBox();
        // createShootBox();
        
        // createSendDataButton();
        // successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        // createMessageBox();

        // errorMessageBox = createErrorMessageBox();
        // errorMessageBox.setString("No Errors");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
     /**
    * <b>Compressor Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createCompressorToggleBox()
    {
        //create and name the Box
        // SendableRegistry.add(compressorToggleBox, "Compressor Toggle");
        // SendableRegistry.setName(compressorToggleBox, "Compressor Toggle");
        
        //add options to  Box
        // compressorToggleBox.setDefaultOption("On", BackupControllerTabData.CompressorToggle.kOn);
        // compressorToggleBox.addOption("Off", BackupControllerTabData.CompressorToggle.kOff);

        //put the widget on the shuffleboard
        // backupControllerTab.add(compressorToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(0, 0)
            // .withSize(8, 2);
    }

     /**
    * <b>Climb Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createClimbToggleBox()
    {
        //create and name the Box
        // SendableRegistry.add(climbToggleBox, "Climb Toggle");
        // SendableRegistry.setName(climbToggleBox, "Climb Toggle");
        
        //add options to  Box
        // climbToggleBox.setDefaultOption("Up", BackupControllerTabData.ClimbToggle.kUp);
        // climbToggleBox.addOption("Down", BackupControllerTabData.ClimbToggle.kDown);

        //put the widget on the shuffleboard
        // backupControllerTab.add(climbToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(9, 0)
            // .withSize(8, 2);
    }

     /**
    * <b>Intake Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createIntakeToggleBox()
    {
        //create and name the Box
        // SendableRegistry.add(intakeToggleBox, "Intake Toggle");
        // SendableRegistry.setName(intakeToggleBox, "Intake Toggle");
        
        //add options to  Box
        // intakeToggleBox.setDefaultOption("In", BackupControllerTabData.IntakeToggle.kIn);
        // intakeToggleBox.addOption("Out", BackupControllerTabData.IntakeToggle.kOut);

        //put the widget on the shuffleboard
        // backupControllerTab.add(intakeToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(1, 3)
            // .withSize(8, 2);
    }

     /**
    * <b>Roller Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRollerToggleBox()
    {
        //create and name the Box
        // SendableRegistry.add(rollerToggleBox, "Roller Toggle");
        // SendableRegistry.setName(rollerToggleBox, "Roller Toggle");
        
        //add options to  Box
        // rollerToggleBox.setDefaultOption("On", BackupControllerTabData.RollerToggle.kOn);
        // rollerToggleBox.addOption("Off", BackupControllerTabData.RollerToggle.kOff);

        //put the widget on the shuffleboard
        // backupControllerTab.add(rollerToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(6, 3)
            // .withSize(8, 2);
    }

     /**
    * <b>Aim</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createAimBox()
    {
        //create and name the Box
        // SendableRegistry.add(aimBox, "Aim");
        // SendableRegistry.setName(aimBox, "Aim");
        
        //add options to  Box
        // compressorToggleBox.setDefaultOption("Off", BackupControllerTabData.CompressorToggle.kOff);
        // compressorToggleBox.addOption("On", BackupControllerTabData.CompressorToggle.kOn);

        //put the widget on the shuffleboard
        // backupControllerTab.add(compressorToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(0, 0)
            // .withSize(8, 2);
    }

     /**
    * <b>Shoot</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootBox()
    {
        //create and name the Box
        // SendableRegistry.add(shootBox, "Shoot");
        // SendableRegistry.setName(shootBox, "Shoot");
        
        //add options to  Box
        // compressorToggleBox.setDefaultOption("Off", BackupControllerTabData.CompressorToggle.kOff);
        // compressorToggleBox.addOption("On", BackupControllerTabData.CompressorToggle.kOn);

        //put the widget on the shuffleboard
        // backupControllerTab.add(compressorToggleBox)
            // .withWidget(BuiltInWidgets.kSplitButtonChooser)
            // .withPosition(0, 0)
            // .withSize(8, 2);
    }

    private NetworkTableEntry createSuccessfulDownloadBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return backupControllerTab.add("Successful Download?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(23, 4)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private NetworkTableEntry createErrorMessageBox()
    {
        return backupControllerTab.add("Error Messages", "No Errors")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(1, 10)
             .withSize(26, 2)
             .getEntry();
    }

    









}
