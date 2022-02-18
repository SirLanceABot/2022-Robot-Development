package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import frc.components.CargoManager;
import frc.components.Climber;
import frc.components.Intake;
import frc.components.Shooter;
import frc.components.Shuttle;
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
    private static final boolean useDrivetrain          = false;
    private static final boolean useCargoManager        = false;
    private static final boolean useIntake              = true;
    private static final boolean useShooter             = false;
    private static final boolean useShuttle             = false;
    private static final boolean useClimber             = false;

    private static final boolean useDriverController    = false;
    private static final boolean useOperatorController  = false;

    private static final boolean useMainShuffleboard    = false;
    private static final boolean useAutonomousTabData   = false;
    private static final boolean useAutonomousCommands  = false;


    // *** ROBOT OBJECT DECLARATION ***
    public static final Drivetrain DRIVETRAIN; // = new Drivetrain();
    public static final CargoManager CARGO_MANAGER; // = new CargoManager();
    public static final Intake INTAKE; // = new Intake();
    public static final Shooter SHOOTER; // = new Shooter();
    public static final Shuttle SHUTTLE; // = new Shuttle();
    public static final Climber CLIMBER; // = new Climber();

    public static final DriverController DRIVER_CONTROLLER; // = new DriverController(Port.Controller.DRIVER);
    public static final OperatorController OPERATOR_CONTROLLER; // = new OperatorController(Port.Controller.OPERATOR);

    public static final MainShuffleboard MAIN_SHUFFLEBOARD; // = new MainShuffleboard(); 
    public static final AutonomousTabData AUTONOMOUS_TAB_DATA; // = new AutonomousTabData();
    // The following is just an ArrayList of Strings for now, but it would be better to be a new AutoCommand type
    public static final ArrayList<String> AUTONOMOUS_COMMANDS; // = new ArrayList<>();
   

    // *** ROBOT OBJECT INSTANTIATION ***
    static
    {
        DRIVETRAIN = useDrivetrain ? new Drivetrain() : null;
        CARGO_MANAGER = useCargoManager ? new CargoManager() : null;
        INTAKE = useIntake ? new Intake() : null;
        SHOOTER = useShooter ? new Shooter() : null;
        SHUTTLE = useShuttle ? new Shuttle() : null;
        CLIMBER = useClimber ? new Climber() : null;

        DRIVER_CONTROLLER = useDriverController ? new DriverController(Port.Controller.DRIVER) : null;
        OPERATOR_CONTROLLER = useOperatorController ? new OperatorController(Port.Controller.OPERATOR) : null;

        MAIN_SHUFFLEBOARD = useMainShuffleboard ? new MainShuffleboard() : null;
        AUTONOMOUS_TAB_DATA = useAutonomousTabData ? new AutonomousTabData() : null;
        AUTONOMOUS_COMMANDS = useAutonomousCommands ? new ArrayList<>() : null;
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
