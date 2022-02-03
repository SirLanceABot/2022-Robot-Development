package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.CargoManager;
import frc.components.Drivetrain;
import frc.constants.Port;
import frc.controls.DriverController;
import frc.controls.OperatorController;

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
    
    public static DriverController DRIVER_CONTROLLER = new DriverController(Port.Controller.DRIVER);
    public static OperatorController OPERATOR_CONTROLLER = new OperatorController(Port.Controller.OPERATOR);



    // *** CLASS CONSTRUCTOR ***
    private RobotContainer()
    {
  
    }


    // *** CLASS & INSTANCE METHODS ***

        
}
