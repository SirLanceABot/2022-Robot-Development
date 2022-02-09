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

public class AutonomousTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER CLASS & INNER ENUMS ***
    // Create enumerated types for each Box
    //-------------------------------------------------------------------//
    public static enum StartingLocation
    {
        kNone, kRight, kCenter, kLeft;
    }

    //-------------------------------------------------------------------//

    public static enum OrderOfOperations
    {
        kShootThenMove, kMoveThenShoot;
    }

    //-------------------------------------------------------------------//

    public static enum ShootPowerCell
    {
        kYes, kNo;
    }

    public static enum ShootDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    //-------------------------------------------------------------------//

    public static enum MoveOffLine
    {
        kYes, kNo;
    }

    public static enum MoveDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    public static enum DirectionToMove
    {
        kAwayFromPowerPort, kTowardPowerPort;
    }

    //-------------------------------------------------------------------//

    public static enum PickUpPowerCell
    {
        kYes, kNo;
    }

    public static enum PickUpLocation
    {
        kTrench, kRendezvousPoint;
    }

    public static enum ShootNewPowerCells
    {
        kYes, kNo;
    }

    // Create a class to hold the data on the Shuffleboard tab
    protected static class AutonomousTabData
    {
        public StartingLocation startingLocation = StartingLocation.kNone;

        public OrderOfOperations orderOfOperations = OrderOfOperations.kShootThenMove;

        public ShootPowerCell shootPowerCell = ShootPowerCell.kYes;
        public ShootDelay shootDelay = ShootDelay.k0;

        public MoveOffLine moveOffLine = MoveOffLine.kYes;
        public MoveDelay moveDelay = MoveDelay.k0;
        public DirectionToMove directionToMove = DirectionToMove.kAwayFromPowerPort;

        public PickUpPowerCell pickUpPowerCell = PickUpPowerCell.kYes;
        public PickUpLocation pickUpLocation = PickUpLocation.kTrench;
        public ShootNewPowerCells shootNewPowerCells = ShootNewPowerCells.kYes;


        
        @Override
        public String toString()
        {
            String str = "";

            str += " \n";
            str += "*****  AUTONOMOUS SELECTION  *****\n";
            str += "Starting Location     : "  + startingLocation   + "\n";
            str += "Order of Operations   : "  + orderOfOperations  + "\n";
            str += "Shoot Power Cell      : "  + shootPowerCell     + "\n";
            str += "Shoot Delay           : "  + shootDelay         + "\n";
            str += "Move Off Line         : "  + moveOffLine        + "\n";
            str += "Move Delay            : "  + moveDelay          + "\n";
            str += "Direction to Move     : "  + directionToMove    + "\n";
            str += "Pick Up Power Cell    : "  + pickUpPowerCell    + "\n";
            str += "Pick Up Location      : "  + pickUpLocation     + "\n";    
            str += "Shoot New Power Cells : "  + shootNewPowerCells + "\n";

            return str;
        }
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");

    // Create an object to store the data in the Boxes
    private AutonomousTabData autonomousTabData = new AutonomousTabData();
  
    // Create the Box objects
    private SendableChooser<StartingLocation> startingLocationBox = new SendableChooser<>();
 
    private SendableChooser<OrderOfOperations> orderOfOperationsBox = new SendableChooser<>();
 
    private SendableChooser<ShootPowerCell> shootPowerCellBox = new SendableChooser<>();
    private SendableChooser<ShootDelay> shootDelayBox = new SendableChooser<>();
     
    private SendableChooser<MoveOffLine> moveOffLineBox = new SendableChooser<>();
    private SendableChooser<MoveDelay> moveDelayBox = new SendableChooser<>();
    private SendableChooser<DirectionToMove> directionToMoveBox = new SendableChooser<>();
 
    private SendableChooser<PickUpPowerCell> pickUpPowerCellBox = new SendableChooser<>();
    private SendableChooser<PickUpLocation> pickUpLocationBox = new SendableChooser<>();
    private SendableChooser<ShootNewPowerCells> shootNewPowerCellBox = new SendableChooser<>();
     
    private NetworkTableEntry goodToGo;
 
    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private boolean previousStateOfSendButton = false;

    private static AutonomousTab instance = new AutonomousTab();


    // *** CLASS CONSTRUCTOR ***
    public AutonomousTab()
    {
        
    }



        
}


    // *** CLASS & INSTANCE METHODS ***

    

