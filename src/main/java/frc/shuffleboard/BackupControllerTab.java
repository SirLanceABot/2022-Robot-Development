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
import frc.components.Intake;

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
    private SendableChooser<Boolean> compressorToggleBox = new SendableChooser<>();
    private SendableChooser<BackupControllerTabData.ClimbStage1Toggle> climbStage1ToggleBox = new SendableChooser<>();
    private SendableChooser<BackupControllerTabData.ClimbStage2Toggle> climbStage2ToggleBox = new SendableChooser<>();
    private SendableChooser<BackupControllerTabData.IntakeToggle> intakeToggleBox = new SendableChooser<>();
    private SendableChooser<BackupControllerTabData.EnableBackupController> enableBackupControllerBox = new SendableChooser<>();    private SendableChooser<Boolean> rollerToggleBox = new SendableChooser<>();
    private SendableChooser<Boolean> aimBox = new SendableChooser<>();
    private SendableChooser<Boolean> shootBox = new SendableChooser<>();
     
    private NetworkTableEntry successfulDownload;
    private NetworkTableEntry errorMessageBox;

     // Create the Button object
     private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    //  private boolean previousStateOfSendButton = false;
    //  private boolean isDataValid = true;
    //  private String errorMessage = "No Errors";

     
    // *** CLASS CONSTRUCTOR ***
    public BackupControllerTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        createCompressorToggleBox();
        createClimbStage1ToggleBox();
        createClimbStage2ToggleBox();
        createIntakeToggleBox();
        createEnableBackupControllerBox();
        createRollerToggleBox();
        createAimBox();
        createShootBox();
        
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
        SendableRegistry.add(compressorToggleBox, "Compressor Toggle");
        SendableRegistry.setName(compressorToggleBox, "Compressor Toggle");
        
        //put the widget on the shuffleboard
        backupControllerTab.add(compressorToggleBox)
            .withWidget(BuiltInWidgets.kToggleButton)
            .withPosition(0, 0)
            .withSize(8, 2);
    }

     /**
    * <b>Climb Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createClimbStage1ToggleBox()
    {
        //create and name the Box
        SendableRegistry.add(climbStage1ToggleBox, "Climb Toggle");
        SendableRegistry.setName(climbStage1ToggleBox, "Climb Toggle");
        
        // //add options to  Box
        climbStage1ToggleBox.setDefaultOption("Off", BackupControllerTabData.ClimbStage1Toggle.kOff);
        climbStage1ToggleBox.addOption("Up", BackupControllerTabData.ClimbStage1Toggle.kUp);
        climbStage1ToggleBox.addOption("Down", BackupControllerTabData.ClimbStage1Toggle.kDown);


        // //put the widget on the shuffleboard
        backupControllerTab.add(climbStage1ToggleBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(9, 0)
            .withSize(8, 2);
    }

    private void createClimbStage2ToggleBox()
    {
        //create and name the Box
        SendableRegistry.add(climbStage2ToggleBox, "Climb Toggle");
        SendableRegistry.setName(climbStage2ToggleBox, "Climb Toggle");
        
        // //add options to  Box
        climbStage2ToggleBox.setDefaultOption("Off", BackupControllerTabData.ClimbStage2Toggle.kOff);
        climbStage2ToggleBox.addOption("Up", BackupControllerTabData.ClimbStage2Toggle.kUp);
        climbStage2ToggleBox.addOption("Down", BackupControllerTabData.ClimbStage2Toggle.kDown);


        // //put the widget on the shuffleboard
        backupControllerTab.add(climbStage2ToggleBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(9, 9)
            .withSize(8, 2);
    }

     /**
    * <b>Intake Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createIntakeToggleBox()
    {
        //create and name the Box
        SendableRegistry.add(intakeToggleBox, "Intake Toggle");
        SendableRegistry.setName(intakeToggleBox, "Intake Toggle");
        
        // //add options to  Box
        intakeToggleBox.setDefaultOption("Off", BackupControllerTabData.IntakeToggle.kOff);
        intakeToggleBox.addOption("In", BackupControllerTabData.IntakeToggle.kIn);
        intakeToggleBox.addOption("Out", BackupControllerTabData.IntakeToggle.kOut);

        // //put the widget on the shuffleboard
        backupControllerTab.add(intakeToggleBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 3)
            .withSize(8, 2);
    }

    private void createEnableBackupControllerBox()
    {
        //create and name the Box
        SendableRegistry.add(enableBackupControllerBox, "Enable Backup Controller");
        SendableRegistry.setName(enableBackupControllerBox, "Enable Backup Controller");
        
        // //add options to  Box
        enableBackupControllerBox.setDefaultOption("Off", BackupControllerTabData.EnableBackupController.kOff);
        enableBackupControllerBox.addOption("On", BackupControllerTabData.EnableBackupController.kOn);

        // //put the widget on the shuffleboard
        backupControllerTab.add(enableBackupControllerBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(10, 3)
            .withSize(8, 2);
    }

     /**
    * <b>Roller Toggle</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRollerToggleBox()
    {
        //create and name the Box
        SendableRegistry.add(rollerToggleBox, "Roller Toggle");
        SendableRegistry.setName(rollerToggleBox, "Roller Toggle");
        
        // //put the widget on the shuffleboard
        backupControllerTab.add(rollerToggleBox)
            .withWidget(BuiltInWidgets.kToggleButton)
            .withPosition(6, 3)
            .withSize(8, 2);
    }

     /**
    * <b>Aim</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createAimBox()
    {
        //create and name the Box
        SendableRegistry.add(aimBox, "Aim");
        SendableRegistry.setName(aimBox, "Aim");
        
        // //put the widget on the shuffleboard
        backupControllerTab.add(compressorToggleBox)
            .withWidget(BuiltInWidgets.kToggleButton)
            .withPosition(0, 0)
            .withSize(8, 2);
    }

     /**
    * <b>Shoot</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootBox()
    {
        //create and name the Box
        SendableRegistry.add(shootBox, "Shoot");
        SendableRegistry.setName(shootBox, "Shoot");

        // //put the widget on the shuffleboard
        backupControllerTab.add(compressorToggleBox)
            .withWidget(BuiltInWidgets.kToggleButton)
            .withPosition(0, 0)
            .withSize(8, 2);
    }

    // private NetworkTableEntry createSuccessfulDownloadBox()
    // {
    //     Map<String, Object> booleanBoxProperties = new HashMap<>();
    //     booleanBoxProperties.put("Color when true", "Lime");
    //     booleanBoxProperties.put("Color when false", "Red");
        
    //     return backupControllerTab.add("Successful Download?", false)
    //          .withWidget(BuiltInWidgets.kBooleanBox)
    //          .withPosition(23, 4)
    //          .withSize(4, 4)
    //          .withProperties(booleanBoxProperties)
    //          .getEntry();
    // }

    // private NetworkTableEntry createErrorMessageBox()
    // {
    //     return backupControllerTab.add("Error Messages", "No Errors")
    //          .withWidget(BuiltInWidgets.kTextView)
    //          .withPosition(1, 10)
    //          .withSize(26, 2)
    //          .getEntry();
    // }

    private void updateBackupControllerTabData()
    {
        backupControllerTabData.compressorToggle = compressorToggleBox.getSelected();
        backupControllerTabData.climbStage1Toggle = climbStage1ToggleBox.getSelected();
        backupControllerTabData.climbStage2Toggle = climbStage2ToggleBox.getSelected();
        backupControllerTabData.enableBackupController = enableBackupControllerBox.getSelected();
        backupControllerTabData.intakeToggle = intakeToggleBox.getSelected();
        backupControllerTabData.rollerToggle = rollerToggleBox.getSelected();
        backupControllerTabData.aim = aimBox.getSelected();
        backupControllerTabData.shoot = shootBox.getSelected();
    }

    public BackupControllerTabData getBackupControllerTabData()
    {
        return backupControllerTabData;
 
   }

}
