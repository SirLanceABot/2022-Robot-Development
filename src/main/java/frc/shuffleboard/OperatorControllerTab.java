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


    // *** INNER ENUMS & INNER CLASSES ***
    private class AxisObjects
    {
        private NetworkTableEntry deadzoneEntry;
        private NetworkTableEntry minOutputEntry;
        private NetworkTableEntry maxOutputEntry;
        private SendableChooser<Boolean> isFlipped = new SendableChooser<>();
        private SendableChooser<OperatorController.AxisScale> axisScaleComboBox = new SendableChooser<>();
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final AxisObjects shroudObjects = new AxisObjects();
    private final AxisObjects shooterPowerObjects = new AxisObjects();

    private final OperatorController OPERATOR_CONTROLLER = RobotContainer.OPERATOR_CONTROLLER;

    private ShuffleboardTab operatorControllerTab = Shuffleboard.getTab("Operator Controller");


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
        createAxisWidgets(OperatorController.OperatorAxisAction.kShroud, "Shroud", shroudObjects, 0);
        createAxisWidgets(OperatorController.OperatorAxisAction.kShooterPower, "Shooter Power", shooterPowerObjects, 5);
    }

    private void createAxisWidgets(OperatorController.OperatorAxisAction axis, String name, AxisObjects axisObjects, int column)
    {
        int row = 0;
        int width = 4;
        int height = 2;

        // Get the current axis settings on the Driver Controller for the given axis
        OperatorController.AxisSettings axisSettings = OPERATOR_CONTROLLER.new AxisSettings();
        axisSettings = OPERATOR_CONTROLLER.getAxisSettings(axis.axis);

        // Create the text box to set the deadzone of the axis
        axisObjects.deadzoneEntry = createTextBox(name + " Deadzone", Double.toString(axisSettings.axisDeadzone), column, row, width, height);
        
        //Create the text box to set the min output of the axis
        row += 2;
        axisObjects.minOutputEntry = createTextBox(name + " Min Output", Double.toString(axisSettings.axisMinOutput), column, row, width, height);

        // Create the text box to set the max output of the axis
        row += 2;
        axisObjects.maxOutputEntry = createTextBox(name + " Max Output", Double.toString(axisSettings.axisMaxOutput), column, row, width, height);

        // Create the button to flip the axis (swap negative and positive)
        row += 2;
        createSplitButtonChooser(axisObjects.isFlipped, name + " Is Flipped", axisSettings.axisIsFlipped, column, row, width, height);

        // Create the combo box to set the axis scale
        row += 3;
        createComboBox(axisObjects.axisScaleComboBox, name + " Axis Scale", axisSettings.axisScale, column, row, width, height);
    }
    
    private NetworkTableEntry createTextBox(String title, String defaultValue, int column, int row, int width, int height)
    {
        return operatorControllerTab.add(title, defaultValue)
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(column, row)  // sets position of widget
        .withSize(width, height)    // sets size of widget
        .getEntry();
    }

    private void createComboBox(SendableChooser<OperatorController.AxisScale> comboBox, String title, OperatorController.AxisScale defaultValue, int column, int row, int width, int height)
    {
        SendableRegistry.add(comboBox, title);
        SendableRegistry.setName(comboBox, title);

        for(OperatorController.AxisScale axisScale: OperatorController.AxisScale.values())
        {
            if(axisScale == defaultValue)
            {
                comboBox.setDefaultOption(axisScale.toString(), axisScale);
            }
            else
            {
                comboBox.addOption(axisScale.toString(), axisScale);
            }
        }

        operatorControllerTab.add(comboBox)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(column, row)
            .withSize(width, height);
    }

    private void createSplitButtonChooser(SendableChooser<Boolean> splitButtonChooser, String title, boolean defaultValue, int column, int row, int width, int height)
    {
        SendableRegistry.add(splitButtonChooser, title);
        SendableRegistry.setName(splitButtonChooser, title);

        splitButtonChooser.setDefaultOption((defaultValue ? "Yes" : "No"), defaultValue);
        splitButtonChooser.addOption((!defaultValue ? "Yes" : "No"), !defaultValue);

        operatorControllerTab.add(splitButtonChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(column, row)
            .withSize(width, height);
    }

    private OperatorController.AxisSettings getAxisSettingsFromShuffleboard(AxisObjects axisObjects)
    {
        OperatorController.AxisSettings axisSettings = OPERATOR_CONTROLLER.new AxisSettings();

        // These MUST be read from the shuffleboard as a String, then converted to a Double because it is a "Text Box"
        axisSettings.axisDeadzone = Double.valueOf(axisObjects.deadzoneEntry.getString("0.1"));
        axisSettings.axisMinOutput = Double.valueOf(axisObjects.minOutputEntry.getString("0.0"));
        axisSettings.axisMaxOutput = Double.valueOf(axisObjects.maxOutputEntry.getString("1.0"));

        axisSettings.axisIsFlipped = axisObjects.isFlipped.getSelected();
        axisSettings.axisScale = axisObjects.axisScaleComboBox.getSelected();

        // System.out.println(axisSettings);
        return axisSettings;
    }

    public void setOperatorControllerAxisSettings()
    {
        OperatorController.AxisSettings axisSettings = OPERATOR_CONTROLLER.new AxisSettings();

        axisSettings = getAxisSettingsFromShuffleboard(shroudObjects);
        OPERATOR_CONTROLLER.setAxisSettings(OperatorController.OperatorAxisAction.kShroud.axis, axisSettings);

        axisSettings = getAxisSettingsFromShuffleboard(shooterPowerObjects);
        OPERATOR_CONTROLLER.setAxisSettings(OperatorController.OperatorAxisAction.kShooterPower.axis, axisSettings);
    }
}
