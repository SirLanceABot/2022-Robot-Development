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
import frc.vision.Vision;

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
    private static final boolean useIntake                  = false;
    private static final boolean useShooter                 = false;
    private static final boolean useShuttle                 = false;
    private static final boolean useClimber                 = false;
    
    private static final boolean useCargoManager            = false;
    private static final boolean useShuttleFSM              = false;

    private static final boolean useDriverController        = true;
    private static final boolean useOperatorController      = false;

    private static final boolean useMainShuffleboard        = true;
    private static final boolean useAutonomousTabData       = true;
    private static final boolean useAutonomousCommandList   = true;

    private static final boolean useVision                  = false;


    // *** ROBOT OBJECT DECLARATION ***
    public static final Drivetrain DRIVETRAIN;
    public static final CargoManager CARGO_MANAGER;
    public static final Intake INTAKE;
    public static final Shooter SHOOTER;
    public static final Shuttle SHUTTLE;
    public static final Climber CLIMBER;
    
    public static final ShuttleFSM SHUTTLEFSM;

    public static final DriverController DRIVER_CONTROLLER;
    public static final OperatorController OPERATOR_CONTROLLER;

    public static final MainShuffleboard MAIN_SHUFFLEBOARD; 
    public static final AutonomousTabData AUTONOMOUS_TAB_DATA;
    public static final AutonomousCommandList AUTONOMOUS_COMMAND_LIST;

    public static final Vision VISION;
   

    // *** ROBOT OBJECT INSTANTIATION ***
    static
    {
        DRIVETRAIN = useFullRobot || useDrivetrain ? new Drivetrain(Port.DrivetrainSetup.DRIVETRAIN_DATA) : null;
        CARGO_MANAGER = useFullRobot || useCargoManager ? new CargoManager() : null;
        INTAKE = useFullRobot || useIntake ? new Intake(Port.Motor.INTAKE_ROLLER, Port.Motor.INTAKE_ARMS_MOTOR) : null;
        SHOOTER = useFullRobot || useShooter ? new Shooter(Port.Motor.SHOOTER_FLYWHEEL, Port.Motor.SHOOTER_SHROUD) : null;
        SHUTTLE = useFullRobot || useShuttle ? new Shuttle(Port.ShuttleSetup.SHUTTLE_DATA) : null;
        CLIMBER = useFullRobot || useClimber ? new Climber(Port.Motor.CLIMBER_STAGE_ONE_LEADER, Port.Motor.CLIMBER_STAGE_TWO_LEADER) : null;
        
        SHUTTLEFSM = useFullRobot || useShuttleFSM ? new ShuttleFSM() : null;

        DRIVER_CONTROLLER = useFullRobot || useDriverController ? new DriverController(Port.Controller.DRIVER) : null;
        OPERATOR_CONTROLLER = useFullRobot || useOperatorController ? new OperatorController(Port.Controller.OPERATOR) : null;

        MAIN_SHUFFLEBOARD = useFullRobot || useMainShuffleboard ? new MainShuffleboard() : null;
        AUTONOMOUS_TAB_DATA = useFullRobot || useAutonomousTabData ? new AutonomousTabData() : null;
        AUTONOMOUS_COMMAND_LIST = useFullRobot || useAutonomousCommandList ? new AutonomousCommandList() : null;

        VISION = useFullRobot || useVision ? new Vision() : null;
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
