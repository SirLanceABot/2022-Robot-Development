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

    
    // *** CLASS & INSTANCE VARIABLES ***
    public static final Drivetrain DRIVETRAIN = new Drivetrain();
    public static final CargoManager CARGO_MANAGER = new CargoManager();
    public static final Intake INTAKE = new Intake();
    public static final Shooter SHOOTER = new Shooter();
    public static final Shuttle SHUTTLE = new Shuttle();
    public static final Climber CLIMBER = new Climber();

    public static final DriverController DRIVER_CONTROLLER = new DriverController(Port.Controller.DRIVER);
    public static final OperatorController OPERATOR_CONTROLLER = new OperatorController(Port.Controller.OPERATOR);

    public static final MainShuffleboard MAIN_SHUFFLEBOARD = new MainShuffleboard();

    public static final AutonomousTabData AUTONOMOUS_TAB_DATA = new AutonomousTabData();
    // The following is just an ArrayList of Strings for now, but it would be better to be a new AutoCommand type
    public static final ArrayList<String> AUTONOMOUS_COMMANDS = new ArrayList<>();

    
    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
        throw new UnsupportedOperationException("This is a utility class!");
    }


    // *** CLASS & INSTANCE METHODS ***

        
}
