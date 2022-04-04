package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import frc.commands.AutonomousCommandList;
import frc.components.CargoManager;
import frc.components.Climber;
import frc.components.EventGenerator;
import frc.components.Intake;
import frc.components.SensorValues;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.ShuttleFSM;
import frc.constants.Port;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.drivetrain.Drivetrain;
import frc.shuffleboard.AutonomousTabData;
import frc.shuffleboard.CameraTab;
import frc.shuffleboard.MainShuffleboard;
import frc.vision.Vision; 

public final class RobotContainer 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    private static final DigitalInput competitionRobotFlag = new DigitalInput(Port.Sensor.COMPETITION_ROBOT);


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    
    // *** INCLUDED ROBOT OBJECTS ***
    // Switch objects to true that you need to use
    private static final boolean useFullRobot               = false;
    
    private static final boolean useDrivetrain              = true;
    private static final boolean useIntake                  = true;
    private static final boolean useShooter                 = true;
    private static final boolean useShuttle                 = true;
    private static final boolean useClimber                 = false;
    
    private static final boolean useSensorValues            = true;
    private static final boolean useEventGenerator          = true;
    private static final boolean useCargoManager            = false;
    private static final boolean useShuttleFSM              = true;


    private static final boolean useDriverController        = true;
    private static final boolean useOperatorController      = true;

    private static final boolean useMainShuffleboard        = true;
    private static final boolean useAutonomousTabData       = true;
    private static final boolean useAutonomousCommandList   = true;

    private static final boolean useVision                  = true; // internal Vision Process soon to be replaced by LimeLight
    private static final boolean useCameraTab               = false; // LimeLight and match countdown clock

    // *** ROBOT OBJECT DECLARATION ***
    public static final Drivetrain DRIVETRAIN;
    public static final Intake INTAKE;
    public static final Shooter SHOOTER;
    public static final Shuttle SHUTTLE;
    public static final Climber CLIMBER;
    
    public static final SensorValues CURRENT_SENSOR_VALUES;
    public static final EventGenerator EVENT_GENERATOR;
    public static final CargoManager CARGO_MANAGER;
    public static final ShuttleFSM SHUTTLEFSM;

    public static final DriverController DRIVER_CONTROLLER;
    public static final OperatorController OPERATOR_CONTROLLER;

    public static final MainShuffleboard MAIN_SHUFFLEBOARD; 
    public static final AutonomousTabData AUTONOMOUS_TAB_DATA;
    public static final AutonomousCommandList AUTONOMOUS_COMMAND_LIST;

    public static final Vision VISION;
    public static final CameraTab CAMERA_TAB;

    public static final PowerDistribution PDH;
   

    // *** ROBOT OBJECT INSTANTIATION ***
    static
    {
        final boolean isCompetitionRobot = !competitionRobotFlag.get();

        final int INTAKE_ROLLER_PORT        = isCompetitionRobot ? Port.Motor.INTAKE_ROLLER : Port.MotorTesting.INTAKE_ROLLER_TEST;
        final int INTAKE_IN_FORWARD_PORT    = isCompetitionRobot ? Port.Motor.INTAKE_IN_FORWARD : Port.MotorTesting.INTAKE_IN_FORWARD_TEST;
        final int INTAKE_IN_REVERSE_PORT    = isCompetitionRobot ? Port.Motor.INTAKE_IN_REVERSE : Port.MotorTesting.INTAKE_IN_REVERSE_TEST;
        final int INTAKE_OUT_FORWARD_PORT   = isCompetitionRobot ? Port.Motor.INTAKE_OUT_FORWARD : Port.MotorTesting.INTAKE_OUT_FORWARD_TEST;
        final int INTAKE_OUT_REVERSE_PORT   = isCompetitionRobot ? Port.Motor.INTAKE_OUT_REVERSE : Port.MotorTesting.INTAKE_OUT_REVERSE_TEST;

        final int SHOOTER_FLYWHEEL_PORT     = isCompetitionRobot ? Port.Motor.SHOOTER_FLYWHEEL : Port.MotorTesting.SHOOTER_FLYWHEEL_TEST;
        final int SHOOTER_SHROUD_PORT       = isCompetitionRobot ? Port.Motor.SHOOTER_SHROUD : Port.MotorTesting.SHOOTER_SHROUD_TEST;

        final int CLIMBER_STAGE_ONE_LEADEAR_PORT = isCompetitionRobot ? Port.Motor.CLIMBER_STAGE_TWO_LEADER : Port.MotorTesting.CLIMBER_STAGE_ONE_LEADER_TEST;
        final int CLIMBER_STAGE_TWO_LEADER_PORT  = isCompetitionRobot ? Port.Motor.CLIMBER_STAGE_ONE_LEADER : Port.MotorTesting.CLIMBER_STAGE_TWO_LEADER_TEST;

        final int INTAKE_MAGNET_IN_TEST_PORT  = isCompetitionRobot ? Port.Sensor.INTAKE_MAGNET_IN : Port.SensorTesting.INTAKE_MAGNET_IN_TEST;
        final int INTAKE_MAGNET_OUT_TEST_PORT = isCompetitionRobot ? Port.Sensor.INTAKE_MAGNET_OUT : Port.SensorTesting.INTAKE_MAGNET_OUT_TEST;

        DRIVETRAIN = useFullRobot || useDrivetrain ? new Drivetrain(Port.DrivetrainSetup.DRIVETRAIN_DATA) : null;
        INTAKE = useFullRobot || useIntake ? new Intake(INTAKE_ROLLER_PORT, INTAKE_IN_FORWARD_PORT, INTAKE_IN_REVERSE_PORT, INTAKE_OUT_FORWARD_PORT, INTAKE_OUT_REVERSE_PORT, INTAKE_MAGNET_IN_TEST_PORT, INTAKE_MAGNET_OUT_TEST_PORT) : null;
        SHOOTER = useFullRobot || useShooter ? new Shooter(SHOOTER_FLYWHEEL_PORT, SHOOTER_SHROUD_PORT) : null;
        SHUTTLE = useFullRobot || useShuttle ? new Shuttle(Port.ShuttleSetup.SHUTTLE_DATA) : null;
        CLIMBER = useFullRobot || useClimber ? new Climber(CLIMBER_STAGE_ONE_LEADEAR_PORT, CLIMBER_STAGE_TWO_LEADER_PORT) : null;
        
        // TODO: Build in using other booleans to trigger eachother
        CURRENT_SENSOR_VALUES = useFullRobot || useSensorValues ? new SensorValues() : null;
        EVENT_GENERATOR = useFullRobot || useEventGenerator ? new EventGenerator() : null;
        CARGO_MANAGER = /*useFullRobot ||*/ useCargoManager ? new CargoManager() : null;
        SHUTTLEFSM = useFullRobot || useShuttleFSM ? new ShuttleFSM() : null;

        DRIVER_CONTROLLER = useFullRobot || useDriverController ? new DriverController(Port.Controller.DRIVER) : null;
        OPERATOR_CONTROLLER = useFullRobot || useOperatorController ? new OperatorController(Port.Controller.OPERATOR) : null;

        MAIN_SHUFFLEBOARD = useFullRobot || useMainShuffleboard ? new MainShuffleboard() : null;
        AUTONOMOUS_TAB_DATA = useFullRobot || useAutonomousTabData ? new AutonomousTabData() : null;
        AUTONOMOUS_COMMAND_LIST = useFullRobot || useAutonomousCommandList ? new AutonomousCommandList() : null;

        VISION = useFullRobot || useVision ? new Vision() : null;
        CAMERA_TAB = /*useFullRobot ||*/ useCameraTab ? new CameraTab() : null;

        PDH = new PowerDistribution(Port.Sensor.PDH_CAN_ID, ModuleType.kRev);
    }


    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
        throw new UnsupportedOperationException("This is a utility class!");
    }


    // *** CLASS & INSTANCE METHODS ***
    public static void runMeFirst()
    {}
        
}
