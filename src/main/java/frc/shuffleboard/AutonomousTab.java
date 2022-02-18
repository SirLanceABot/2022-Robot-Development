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

// TODO @Joel - Look for these TODO comments in this class and in the DisableMode.java class. They have a blue mark on the scroll bar.
// Here is the basic idea.
// 1. The RobotContainer.java class has the following:
//    a. An AUTONOMOUS_TAB_DATA variable that will store the auto selection (not sure if this is needed)
//    b. An AUTONOMOUS_COMMANDS ArrayList that will contain the list of commands we want to complete in autonomous
// 2. Change the checkForNewAutoTabData() so that it returns a boolean, and you may want to rename it isThereNewAutoTabData() or something like that
// 3. In the DisabledMode.java, add the following:
//    a. Create a variable for the robotState
//    b. Initialize the robotState in the init() method
//    c. Add an if statement in the periodic() method that checks if the robot is in kDisabledAfterRobotInit mode
//       1. Then call the check...() method to see if there is new auto tab data
//       2. If there is new data, then store the data in the RobotContainer AUTONOMOUS_TAB_DATA
//       3. Build the ArrayList that creates the list of commands we want to do in autonomous
//       4. We want this all done while the robot is disabled, it should not being wasting time in autonomous fetching data or building a command list
//    d. Remove the statements from the exit() method.
// 4. In the AutonomousMode.java class, use a loop to go thru the ArrayList to complete the desired tasks in auto.
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
 
    private SendableChooser<AutonomousTabData.ShootCargo> shootCargoBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ShootDelay> shootDelayBox = new SendableChooser<>();
     
    private SendableChooser<AutonomousTabData.MoveOffTarmac> moveOffTarmacBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveDelay> moveDelayBox = new SendableChooser<>();
 
    private SendableChooser<AutonomousTabData.PickUpCargo> pickUpCargoBox = new SendableChooser<>();
     
    private NetworkTableEntry goodToGo;
 
    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private boolean previousStateOfSendButton = false;


    // *** CLASS CONSTRUCTOR ***
    public AutonomousTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        createStartingLocationBox();

        createOrderOfOperationsBox();
        
        createShootCargoBox();
        createShootDelayBox();
        
        createMoveOffTarmacBox();
        createMoveDelayBox();

        createPickUpCargoBox();
        
        // sendDataButton = createSendDataButton();
        // sendDataButton.setBoolean(false);
        createSendDataButton();

        goodToGo = createRedLightGreenLightBox();
        goodToGo.setBoolean(false);

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
        startingLocationBox.setDefaultOption("Left", AutonomousTabData.StartingLocation.kLeft);
        startingLocationBox.addOption("Middle", AutonomousTabData.StartingLocation.kMiddle);
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
        orderOfOperationsBox.setDefaultOption("Shoot First", AutonomousTabData.OrderOfOperations.kShootFirst);
        orderOfOperationsBox.addOption("Move First", AutonomousTabData.OrderOfOperations.kMoveFirst);
        orderOfOperationsBox.addOption("Do Nothing", AutonomousTabData.OrderOfOperations.kDoNothing);

        //put the widget on the Shuffleboard
        autonomousTab.add(orderOfOperationsBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(9,0)
            .withSize(8, 2);
    }

    /**
    * <b>Shoot Cargo</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createShootCargoBox()
    {
        //create and name the Box
        SendableRegistry.add(shootCargoBox, "Shoot Cargo");
        SendableRegistry.setName(shootCargoBox, "Shoot Cargo");

        //add options to Box
        shootCargoBox.setDefaultOption("0", AutonomousTabData.ShootCargo.k0);
        shootCargoBox.addOption("1", AutonomousTabData.ShootCargo.k1);
        shootCargoBox.addOption("2", AutonomousTabData.ShootCargo.k2);

        //put the widget on the shuffleboard
        autonomousTab.add(shootCargoBox)
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
        pickUpCargoBox.setDefaultOption("Yes", AutonomousTabData.PickUpCargo.kYes);
        pickUpCargoBox.addOption("No", AutonomousTabData.PickUpCargo.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(pickUpCargoBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 9)
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
        
        // return autonomousTab.add("Send Data", false)
        //     .withWidget(BuiltInWidgets.kToggleSwitch)
        //     .withPosition(23, 1)
        //     .withSize(4, 2)
        //     .getEntry();
    }

    private NetworkTableEntry createRedLightGreenLightBox()
    {
        //SendableRegistry.add(redLightGreenLightBox, "Good to Go?");
        //SendableRegistry.setName(redLightGreenLightBox, "Good to Go?");

        // redLightGreenLightBox.setDefaultOption("No", false);
        // redLightGreenLightBox.addOption("Yes", true);

        Map<String, Object> booleanBoxProperties = new HashMap<>();
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        return autonomousTab.add("Good to Go?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(23, 4)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();

        autonomousTabData.orderOfOperations = orderOfOperationsBox.getSelected();
        
        autonomousTabData.shootCargo = shootCargoBox.getSelected();
        autonomousTabData.shootDelay = shootDelayBox.getSelected();

        autonomousTabData.moveOffTarmac = moveOffTarmacBox.getSelected();
        autonomousTabData.moveDelay = moveDelayBox.getSelected();

        autonomousTabData.pickUpCargo = pickUpCargoBox.getSelected();
    }

    public boolean isThereNewAutonomousTabData()
    {
        boolean isNewData = false;
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        if(isSendDataButtonPressed && !previousStateOfSendButton)
        {
            previousStateOfSendButton = true;
            isNewData = true;

            if(isDataValid())
            {
                goodToGo.setBoolean(true);
                updateAutonomousTabData();
            }
            else
            {
                goodToGo.setBoolean(false);
            }

            System.out.println(autonomousTabData);
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

    private boolean isDataValid()
    {
        boolean isValid = true;

        boolean isPickUpCargo = (pickUpCargoBox.getSelected() == AutonomousTabData.PickUpCargo.kYes);
        boolean isMoveOffTarmac = (moveOffTarmacBox.getSelected() == AutonomousTabData.MoveOffTarmac.kYes);

        boolean isMoveDelay = (moveDelayBox.getSelected() != AutonomousTabData.MoveDelay.k0);
        boolean isShootDelay = (shootDelayBox.getSelected() != AutonomousTabData.ShootDelay.k0);
        boolean isShootCargo = (shootCargoBox.getSelected() != AutonomousTabData.ShootCargo.k0);

        boolean isShootTwo = (shootCargoBox.getSelected() == AutonomousTabData.ShootCargo.k2);

        boolean isDoNothing = (orderOfOperationsBox.getSelected() == AutonomousTabData.OrderOfOperations.kDoNothing);

        // if trying to pick up cargo without moving off tarmac
        if(isPickUpCargo && !isMoveOffTarmac)
        {
            isValid = false;
            
            DriverStation.reportWarning("Cannot Pick Up Cargo Without Moving Off Tarmac", false);
        }

        // if trying to set a delay for an action not taken
        if((!isMoveOffTarmac && isMoveDelay) || (!isShootCargo && isShootDelay))
        {
            isValid = false;
            
            DriverStation.reportWarning("Cannot Set A Delay For An Action Not Taken", false);
        }

        // if trying to shoot two cargo without picking up cargo
        if(isShootTwo && !isPickUpCargo)
        {
            isValid = false;

            DriverStation.reportWarning("Cannot Shoot Two Cargo Wihtout Picking Up Cargo", false);
        }

        // if selecting do nothing and trying to move
        if (isDoNothing && isMoveOffTarmac)
        {
            isValid = false;
            
            DriverStation.reportWarning("Cannot Move Off Tarmac And Do Nothing", false);
        }

        // TODO: trying to move first without moving off tarmac

        return isValid;
    }
}
