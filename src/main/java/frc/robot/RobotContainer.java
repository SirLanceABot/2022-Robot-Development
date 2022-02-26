package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.commands.AutonomousCommandList;
import frc.components.CargoManager;
import frc.components.Climber;
import frc.components.Intake;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.ShuttleFSM;
import frc.constants.Port;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.drivetrain.Drivetrain;
import frc.shuffleboard.AutonomousTabData;
import frc.shuffleboard.MainShuffleboard;

public final class RobotContainer 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    
    // *** INCLUDED ROBOT OBJECTS ***
    // Switch objects to true that you need to use
    private static final boolean useFullRobot               = false;
    
    private static final boolean useDrivetrain              = false;
    private static final boolean useCargoManager            = false;
    private static final boolean useIntake                  = false;
    private static final boolean useShooter                 = false;
    private static final boolean useShuttle                 = false;
    private static final boolean useClimber                 = false;
    
    private static final boolean useShuttleFSM              = false;

    private static final boolean useDriverController        = true;
    private static final boolean useOperatorController      = false;

    private static final boolean useMainShuffleboard        = true;
    private static final boolean useAutonomousTabData       = true;
    private static final boolean useAutonomousCommandList   = true;


    // *** ROBOT OBJECT DECLARATION ***
    public static final Drivetrain DRIVETRAIN; // = new Drivetrain();
    public static final CargoManager CARGO_MANAGER; // = new CargoManager();
    public static final Intake INTAKE; // = new Intake();
    public static final Shooter SHOOTER; // = new Shooter();
    public static final Shuttle SHUTTLE; // = new Shuttle();
    public static final Climber CLIMBER; // = new Climber();
    
    public static final ShuttleFSM SHUTTLEFSM; // = new ShuttleFSM();

    public static final DriverController DRIVER_CONTROLLER; // = new DriverController(Port.Controller.DRIVER);
    public static final OperatorController OPERATOR_CONTROLLER; // = new OperatorController(Port.Controller.OPERATOR);

    public static final MainShuffleboard MAIN_SHUFFLEBOARD; // = new MainShuffleboard(); 
    public static final AutonomousTabData AUTONOMOUS_TAB_DATA; // = new AutonomousTabData();
    public static final AutonomousCommandList AUTONOMOUS_COMMAND_LIST;
   

    // *** ROBOT OBJECT INSTANTIATION ***
    static
    {
        DRIVETRAIN = useFullRobot || useDrivetrain ? new Drivetrain(Port.Drivetrain.DRIVETRAIN_DATA) : null;
        CARGO_MANAGER = useFullRobot || useCargoManager ? new CargoManager() : null;
        INTAKE = useFullRobot || useIntake ? new Intake() : null;
        SHOOTER = useFullRobot || useShooter ? new Shooter() : null;
        SHUTTLE = useFullRobot || useShuttle ? new Shuttle() : null;
        CLIMBER = useFullRobot || useClimber ? new Climber() : null;
        
        SHUTTLEFSM = useFullRobot || useShuttleFSM ? new ShuttleFSM() : null;

        DRIVER_CONTROLLER = useFullRobot || useDriverController ? new DriverController(Port.Controller.DRIVER) : null;
        OPERATOR_CONTROLLER = useFullRobot || useOperatorController ? new OperatorController(Port.Controller.OPERATOR) : null;

        MAIN_SHUFFLEBOARD = useFullRobot || useMainShuffleboard ? new MainShuffleboard() : null;
        AUTONOMOUS_TAB_DATA = useFullRobot || useAutonomousTabData ? new AutonomousTabData() : null;
        AUTONOMOUS_COMMAND_LIST = useFullRobot || useAutonomousCommandList ? new AutonomousCommandList() : null;
    }


    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
        throw new UnsupportedOperationException("This is a utility class!");
    }


    // *** CLASS & INSTANCE METHODS ***
    public static void constructMeFirst()
    {}
        
}
