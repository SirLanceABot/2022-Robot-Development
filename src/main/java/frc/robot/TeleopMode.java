package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.components.Intake;
import frc.components.Climber;
//import frc.components.Drivetrain;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.CargoManager;

import frc.components.Shuttle;
import frc.controls.DriverController;
import frc.controls.DriverController.DriverButtonAction;
import frc.controls.DriverController.DriverAxisAction;
import frc.controls.DriverController.DriverPOVAction;
import frc.controls.Logitech.AxisSettings;
import frc.controls.OperatorController;
import frc.controls.Xbox;

import frc.controls.OperatorController.OperatorAxisAction;
import frc.controls.OperatorController.OperatorButtonAction;
import frc.controls.Xbox.Button;

import frc.shuffleboard.MainShuffleboard;

public class TeleopMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final DriverController DRIVER_CONTROLLER = RobotContainer.DRIVER_CONTROLLER;
    private static final Intake INTAKE = RobotContainer.INTAKE;
    private static final MainShuffleboard MAIN_SHUFFLEBOARD = RobotContainer.MAIN_SHUFFLEBOARD;
    private static final OperatorController OPERATOR_CONTROLLER = RobotContainer.OPERATOR_CONTROLLER;
    //private static final Shooter SHOOTER = RobotContainer.SHOOTER;
    //private static final Climber CLIMBER = RobotContainer.CLIMBER;
    //private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;
    //private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;

    private static Shuttle.Events.event[] array =
    {
        Shuttle.Events.event.NONE,
        Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
        Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES,
        Shuttle.Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES,
        Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,
        Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES,
        Shuttle.Events.event.SHOOT_IS_CALLED
    };
    
    private static int i = 0;

    private boolean tracker = false;

    // *** CLASS CONSTRUCTOR ***
    public TeleopMode()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        DRIVER_CONTROLLER.resetRumbleCounter();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        DRIVER_CONTROLLER.checkRumbleEvent();

        // Running the intake
        if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff))
        {
            if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOff)
            {
                INTAKE.intakeRoller();
            }
            else
            {
                INTAKE.turnOffRoller();
            }
        }
        else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleDirection))
        {
            if(INTAKE.getRollerDirection() == Intake.RollerDirection.kIn)
            {
                INTAKE.outtakeRoller();
            }
            else if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOut)
            {
                INTAKE.intakeRoller();
            }
        }

        // running the shuttle
        if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleOverride))
        {
            //SHUTTLE.overrideFSm();
            if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttle1stStageToggle))
            {
               // if(SHUTTLE.forwardFirstStage());
                {

                }
                    

            }
        }
        else
        {
            //shuttle.runFSM();
        }

    
        // running the shooter
        if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShooterOverride))
        {
            // SHOOTER.overrideFSM();

            // SHOOTER.setShroudMotorSpeed(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud); 
            
            // SHOOTER.setFlyWheelSpeed(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower);
        
            if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShootBallToggle))
            {
                // SHUTTLE.overrideFSM();
                // SHUTTLE.forwardSecondStage(); 
                // SHUTTLE.forwardFirstStage();
            }
        }
        else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot))
        {
            //SHOOTER.Shooter();
        }


        // running the climber
        if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kAutoClimb))
        {
            //CLIMBER.run();
        }
        else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbOverride))
        {
            //CLIMBER.overrideFSM();
            if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kMoveClimbToggle))
            {
                
            }

        }


        // running the drivetrain
        //DRIVETRAIN.moveYAxis()
        //DRIVETRAIN.
        //DRIVETRAIN.



    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }

    private void DarrensTestingMode()
    {
        
        Shuttle.Events.event event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
        Shuttle.State state = Shuttle.State.NO_CARGO_STORED;

        if(tracker != DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff))
        {
            event = array[i];

            System.out.println("Pressed");
            if (i < array.length - 1)
            {
                i++;
            }
            else
            {
                i = 0;
            }
        }
        else
        {
            // event = Shuttle.Events.event.NONE;
        }

        state = Shuttle.Transition.findNextState(state, event);

        // System.out.println("State: " + state.toString());
        SmartDashboard.putString("State", state.toString());
        SmartDashboard.putString("Event", event.toString());

        tracker = DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff);
    }
}
