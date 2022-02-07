package frc.shuffleboard;

import java.lang.invoke.MethodHandles;

import frc.controls.OperatorController;
import frc.robot.RobotContainer;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class OperatorControllerTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***



    // *** CLASS CONSTRUCTOR ***
    public OperatorControllerTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        initOperatorControllerTab();

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***
    private void initOperatorControllerTab()
    {
        // createAxisWidgets(OperatorController.Axis.kLeftX, "Left X", leftXObjects, 0);
        // createAxisWidgets(OperatorController.Axis.kLeftY, "Left Y", leftYObjects, 5);
        // createAxisWidgets(OperatorController.Axis.kRightX, "Right X", rightXObjects, 10);
        // createAxisWidgets(OperatorController.Axis.kRightY, "Right Y", rightYObjects, 15);
    }

    public void setOperatorControllerAxisSettings()
    {

    }
    
}
