package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.CargoManager;
import frc.components.Intake;
import frc.constants.Port;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.drivetrain.Drivetrain;
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
    
    public static final DriverController DRIVER_CONTROLLER = new DriverController(Port.Controller.DRIVER);
    public static final OperatorController OPERATOR_CONTROLLER = new OperatorController(Port.Controller.OPERATOR);

    public static final MainShuffleboard MAIN_SHUFFLEBOARD = new MainShuffleboard();

    
    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
        throw new UnsupportedOperationException("This is a utility class!");
    }


    // *** CLASS & INSTANCE METHODS ***

        
}
