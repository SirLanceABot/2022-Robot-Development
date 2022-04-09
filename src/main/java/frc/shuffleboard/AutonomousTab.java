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
import frc.components.Shooter;
import edu.wpi.first.util.sendable.SendableRegistry;

public class AutonomousTab 
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
    private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");

    // Create an object to store the data in the Boxes
    private AutonomousTabData autonomousTabData = new AutonomousTabData();
  
    // Create the Box objects
    private SendableChooser<AutonomousTabData.StartingLocation> startingLocationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.OrderOfOperations> orderOfOperationsBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ShootCargoAmount> shootCargoAmountBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ShootDelay> shootDelayBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveOffTarmac> moveOffTarmacBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveDelay> moveDelayBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PickUpCargo> pickUpCargoBox = new SendableChooser<>();
    private SendableChooser<Shooter.Hub> hubBox = new SendableChooser<>();

    private NetworkTableEntry successfulDownload;
    private NetworkTableEntry errorMessageBox;

    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private boolean previousStateOfSendButton = false;
    private boolean isDataValid = true;
    private String errorMessage = "No Errors";

    // *** CLASS CONSTRUCTOR ***
    public AutonomousTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        createStartingLocationBox();
        createOrderOfOperationsBox();
        createShootCargoAmountBox();
        createShootDelayBox();
        createMoveOffTarmacBox();
        createMoveDelayBox();
        createPickUpCargoBox();
        createHubBox();
        
        createSendDataButton();
        successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        // createMessageBox();

        errorMessageBox = createErrorMessageBox();
        // errorMessageBox.setString("No Errors");

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***
    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createStartingLocationBox()
    {
        //create and name the Box
        SendableRegistry.add(startingLocationBox, "Starting Location");
        SendableRegistry.setName(startingLocationBox, "Starting Location");
        
        //add options to  Box
        startingLocationBox.addOption("Left", AutonomousTabData.StartingLocation.kLeft);
        startingLocationBox.setDefaultOption("Middle Left", AutonomousTabData.StartingLocation.kMiddleLeft);
        startingLocationBox.addOption("Middle Right", AutonomousTabData.StartingLocation.kMiddleRight);
        startingLocationBox.addOption("Right", AutonomousTabData.StartingLocation.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(startingLocationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(0, 0)
            .withSize(8, 2);
    }

    /**
    * <b>Order of Operations</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createOrderOfOperationsBox()
    {
        //create and name the Box
        SendableRegistry.add(orderOfOperationsBox, "Order of Operations");
        SendableRegistry.setName(orderOfOperationsBox, "Order of Operations");

        //add options to box
        orderOfOperationsBox.addOption("Shoot First", AutonomousTabData.OrderOfOperations.kShootFirst);
        orderOfOperationsBox.setDefaultOption("Move First", AutonomousTabData.OrderOfOperations.kMoveFirst);
        orderOfOperationsBox.addOption("Shoot Move Shoot", AutonomousTabData.OrderOfOperations.kShootMoveShoot);
        orderOfOperationsBox.addOption("4-Ball Auto", AutonomousTabData.OrderOfOperations.kFourBallAuto);
        orderOfOperationsBox.addOption("Do Nothing", AutonomousTabData.OrderOfOperations.kDoNothing);

        //put the widget on the Shuffleboard
        autonomousTab.add(orderOfOperationsBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(9,0)
            .withSize(16, 2);
    }

    /**
    * <b>Shoot Cargo</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootCargoAmountBox()
    {
        //create and name the Box
        SendableRegistry.add(shootCargoAmountBox, "Shoot Cargo Amount");
        SendableRegistry.setName(shootCargoAmountBox, "Shoot Cargo Amount");

        //add options to Box
        shootCargoAmountBox.setDefaultOption("0", AutonomousTabData.ShootCargoAmount.k0);
        shootCargoAmountBox.addOption("1", AutonomousTabData.ShootCargoAmount.k1);
        shootCargoAmountBox.addOption("2", AutonomousTabData.ShootCargoAmount.k2);

        //put the widget on the shuffleboard
        autonomousTab.add(shootCargoAmountBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 3)
            .withSize(4, 2);
    }

    /**
    * <b>Shoot Delay</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootDelayBox()
    {
        //create and name the Box
        SendableRegistry.add(shootDelayBox, "Shoot Delay (Seconds)");
        SendableRegistry.setName(shootDelayBox, "Shoot Delay (Seconds)");

        //add options to Box
        shootDelayBox.setDefaultOption("0", AutonomousTabData.ShootDelay.k0);
        shootDelayBox.addOption("1", AutonomousTabData.ShootDelay.k1);
        shootDelayBox.addOption("2", AutonomousTabData.ShootDelay.k2);
        shootDelayBox.addOption("3", AutonomousTabData.ShootDelay.k3);
        shootDelayBox.addOption("4", AutonomousTabData.ShootDelay.k4);
        shootDelayBox.addOption("5", AutonomousTabData.ShootDelay.k5);

        //put the widget on the shuffleboard
        autonomousTab.add(shootDelayBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 3)
            .withSize(6, 2);
    }

    /**
    * <b>Move</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMoveOffTarmacBox()
    {
        //create and name the Box
        SendableRegistry.add(moveOffTarmacBox, "Move Off Tarmac");
        SendableRegistry.setName(moveOffTarmacBox, "Move Off Tarmac");

        //add options to Box
        moveOffTarmacBox.setDefaultOption("Yes", AutonomousTabData.MoveOffTarmac.kYes);
        moveOffTarmacBox.addOption("No", AutonomousTabData.MoveOffTarmac.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(moveOffTarmacBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 6)
            .withSize(4, 2);
    }

    /**
    * <b>Move Delay</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMoveDelayBox()
    {
        //create and name the Box
        SendableRegistry.add(moveDelayBox, "Move Delay (Seconds)");
        SendableRegistry.setName(moveDelayBox, "Move Delay (Seconds)");

        //add options to Box
        moveDelayBox.setDefaultOption("0", AutonomousTabData.MoveDelay.k0);
        moveDelayBox.addOption("1", AutonomousTabData.MoveDelay.k1);
        moveDelayBox.addOption("2", AutonomousTabData.MoveDelay.k2);
        moveDelayBox.addOption("3", AutonomousTabData.MoveDelay.k3);
        moveDelayBox.addOption("4", AutonomousTabData.MoveDelay.k4);
        moveDelayBox.addOption("5", AutonomousTabData.MoveDelay.k5);

        //put the widget on the shuffleboard
        autonomousTab.add(moveDelayBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 6)
            .withSize(6, 2);
    }

    /**
    * <b>Pick Up Cargo</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPickUpCargoBox()
    {
        //create and name the Box
        SendableRegistry.add(pickUpCargoBox, "Pick Up Cargo");
        SendableRegistry.setName(pickUpCargoBox, "Pick Up Cargo");

        //add options to Box
        pickUpCargoBox.addOption("Yes", AutonomousTabData.PickUpCargo.kYes);
        pickUpCargoBox.setDefaultOption("No", AutonomousTabData.PickUpCargo.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(pickUpCargoBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(13, 3) //.withPosition(1, 9)
            .withSize(4, 2);
    }

    private void createHubBox()
    {
        //create and name the Box
        SendableRegistry.add(hubBox, "Hub");
        SendableRegistry.setName(hubBox, "Hub");

        //add options to Box
        hubBox.addOption("Lower", Shooter.Hub.kLower);
        hubBox.setDefaultOption("Upper", Shooter.Hub.kUpper);

        //put the widget on the shuffleboard
        autonomousTab.add(hubBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(13, 6)
            .withSize(4, 2);
    }

    /**
     * <b>Send Data</b> Button
     * <p>
     * Create an entry in the Network Table and add the Button to the Shuffleboard
     * Tab
     * 
     * @return
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);

        autonomousTab.add(sendDataButton)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(23, 1)
            .withSize(4, 2);
    }

    private NetworkTableEntry createSuccessfulDownloadBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return autonomousTab.add("Successful Download?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(23, 4)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private NetworkTableEntry createErrorMessageBox()
    {
        return autonomousTab.add("Error Messages", "No Errors")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(1, 10)
             .withSize(26, 2)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        autonomousTabData.orderOfOperations = orderOfOperationsBox.getSelected();
        autonomousTabData.shootCargoAmount = shootCargoAmountBox.getSelected();
        autonomousTabData.shootDelay = shootDelayBox.getSelected();
        autonomousTabData.moveOffTarmac = moveOffTarmacBox.getSelected();
        autonomousTabData.moveDelay = moveDelayBox.getSelected();
        autonomousTabData.pickUpCargo = pickUpCargoBox.getSelected();
    }

    public boolean wasSendDataButtonPressed()
    {
        boolean isNewData = false;
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        updateIsDataValidAndErrorMessage();

        if(isSendDataButtonPressed && !previousStateOfSendButton)
        {
            previousStateOfSendButton = true;
            isNewData = true;

            if(isDataValid)
            {
                successfulDownload.setBoolean(true);
                updateAutonomousTabData();
            }
            else
            {
                successfulDownload.setBoolean(false);
                DriverStation.reportWarning(errorMessage, false);
                errorMessageBox.setString(errorMessage);
            }
        }
        
        if (!isSendDataButtonPressed && previousStateOfSendButton)
        {
            previousStateOfSendButton = false;
        }

        return isNewData;
    }

    public AutonomousTabData getAutonomousTabData()
    {
        return autonomousTabData;
    }

    public void updateIsDataValidAndErrorMessage()
    {
        errorMessage = "No Errors";
        String msg = "";
        boolean isValid = true;

        boolean isPickUpCargo = (pickUpCargoBox.getSelected() == AutonomousTabData.PickUpCargo.kYes);
        boolean isMoveOffTarmac = (moveOffTarmacBox.getSelected() == AutonomousTabData.MoveOffTarmac.kYes);
        boolean isMoveDelay = (moveDelayBox.getSelected() != AutonomousTabData.MoveDelay.k0);
        boolean isShootDelay = (shootDelayBox.getSelected() != AutonomousTabData.ShootDelay.k0);
        boolean isShootCargo = (shootCargoAmountBox.getSelected() != AutonomousTabData.ShootCargoAmount.k0);
        boolean isShootTwo = (shootCargoAmountBox.getSelected() == AutonomousTabData.ShootCargoAmount.k2);
        boolean isDoNothing = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kDoNothing);
        boolean isMoveFirst = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kMoveFirst);
        boolean isShootFirst = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kShootFirst);
        boolean isShootMoveShoot = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kShootMoveShoot);
        boolean isFourBallAuto = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kFourBallAuto);
        boolean isMiddleRight = (startingLocationBox.getSelected() == AutonomousTabData.StartingLocation.kMiddleRight);

        // if trying to pick up cargo without moving off tarmac
        if(isPickUpCargo && !isMoveOffTarmac)
        {
            isValid = false;
            
            msg += "[ Cannot Pick Up Cargo Without Moving Off Tarmac ]  \n";
        }

        // if trying to set a delay for an action not taken
        if((!isMoveOffTarmac && isMoveDelay) || (!isShootCargo && isShootDelay))
        {
            isValid = false;

            msg += "[ Cannot Set A Delay For An Action Not Taken ]  \n";
        }

        // if trying to shoot two cargo without picking up cargo
        if(isShootTwo && !isPickUpCargo)
        {
            isValid = false;

            msg += "[ Cannot Shoot Two Cargo Without Picking Up Cargo ]  \n";
        }

        // if selecting do nothing and trying to move or shoot
        if ((isDoNothing && isMoveOffTarmac) || (isDoNothing && isShootCargo))
        {
            isValid = false;
 
            msg += "[ Cannot Do Nothing And Move Or Shoot ]  \n";
        }

        // if trying to move first without moving off tarmac
        if (isMoveFirst && !isMoveOffTarmac)
        {
            isValid = false;

            msg += "[ Cannot Move First Without Moving Off Tarmac ] \n";
        }

        // if trying to shoot first without shooting any cargo
        if (isShootFirst && !isShootCargo)
        {
            isValid = false;

            msg += "[ Cannot Shoot First Without Shooting Any Cargo ] \n";
        }

        // if trying to shoot, move, and shoot without shooting 2 cargo
        if (isShootMoveShoot && !isShootTwo)
        {
            isValid = false;

            msg += "[ Cannot Shoot, Move, And Shoot Without Shooting Two Cargo ] \n";
        }

        // if trying to run four ball without starting in middle right
        if (isFourBallAuto && !isMiddleRight)
        {
            isValid = false;

            msg += "[ Cannot Run Four Ball Auto without Starting in Middle Right Location ] \n";
        }
        
        
        if(!isValid)
            errorMessage = msg;
        
        errorMessageBox.setString(errorMessage);

        isDataValid = isValid;
    }
}
