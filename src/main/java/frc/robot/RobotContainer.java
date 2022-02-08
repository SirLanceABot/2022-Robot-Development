package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.CargoManager;
import frc.components.Drivetrain;
import frc.components.Intake;
import frc.constants.Port;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.shuffleboard.MainShuffleboard;

public class RobotContainer 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    
    // *** CLASS & INSTANCE VARIABLES ***
    public static Drivetrain DRIVETRAIN = new Drivetrain();
    public static CargoManager CARGO_MANAGER = new CargoManager();
    public static Intake INTAKE = new Intake();
    
    public static DriverController DRIVER_CONTROLLER = new DriverController(Port.Controller.DRIVER);
    public static OperatorController OPERATOR_CONTROLLER = new OperatorController(Port.Controller.OPERATOR);

    public static MainShuffleboard MAIN_SHUFFLEBOARD = new MainShuffleboard();

    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
  
    }


    // *** CLASS & INSTANCE METHODS ***

        
}
