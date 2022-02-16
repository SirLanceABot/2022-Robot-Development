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
    public static final Drivetrain DRIVETRAIN;
    public static final CargoManager CARGO_MANAGER;
    public static final Intake INTAKE;
    public static final Shooter SHOOTER;
    public static final Shuttle SHUTTLE;
    public static final Climber CLIMBER;

    public static final DriverController DRIVER_CONTROLLER;
    public static final OperatorController OPERATOR_CONTROLLER;

    public static final MainShuffleboard MAIN_SHUFFLEBOARD;

    public static final AutonomousTabData AUTONOMOUS_TAB_DATA;
    // The following is just an ArrayList of Strings for now, but it would be better to be a new AutoCommand type
    public static final ArrayList<String> AUTONOMOUS_COMMANDS;

    static
    {
        DRIVETRAIN = new Drivetrain();
        CARGO_MANAGER = new CargoManager();
        INTAKE = new Intake();
        SHOOTER = new Shooter();
        SHUTTLE = new Shuttle();
        CLIMBER = new Climber();
    
        DRIVER_CONTROLLER = new DriverController(Port.Controller.DRIVER);
        OPERATOR_CONTROLLER = new OperatorController(Port.Controller.OPERATOR);
    
        MAIN_SHUFFLEBOARD = new MainShuffleboard();
        AUTONOMOUS_TAB_DATA = new AutonomousTabData();
        // The following is just an ArrayList of Strings for now, but it would be better to be a new AutoCommand type
        AUTONOMOUS_COMMANDS = new ArrayList<>();

        // DRIVETRAIN = null;
        // CARGO_MANAGER = null;
        // INTAKE = null;
        // SHOOTER = null;
        // SHUTTLE = null;
        // CLIMBER = null;
        // DRIVER_CONTROLLER = null;
        // OPERATOR_CONTROLLER = null;
        // MAIN_SHUFFLEBOARD = null;
        // AUTONOMOUS_TAB_DATA = null;
        // AUTONOMOUS_COMMANDS = null;
    }
    

    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
        throw new UnsupportedOperationException("This is a utility class!");
    }


    // *** CLASS & INSTANCE METHODS ***

        
}
