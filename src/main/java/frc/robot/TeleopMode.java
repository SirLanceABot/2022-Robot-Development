package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.components.Intake;
import frc.components.Climber;
import frc.drivetrain.Drivetrain;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.constants.Constant;

import frc.controls.DriverController;
import frc.controls.DriverController.DriverAxisAction;
import frc.controls.DriverController.DriverButtonAction;
import frc.controls.OperatorController;

import frc.controls.OperatorController.OperatorAxisAction;
import frc.controls.OperatorController.OperatorButtonAction;


// TODO : To make the periodic() method more manageable, ...
// 1. Create a method called drivetrain() for all the drivetrain commands
//    In periodic(), keep the statement if(DRIVETRAIN != null), but call the new method in that if statement
// 2. Create a method called intake() and do the same as drivetrain()
// 3. Create a method called shuttle()
// 4. Create a method called shooter()
// 5. Create a method called climber()
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
    private static final OperatorController OPERATOR_CONTROLLER = RobotContainer.OPERATOR_CONTROLLER;
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    private static final Intake INTAKE = RobotContainer.INTAKE;
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;
    private static final Climber CLIMBER = RobotContainer.CLIMBER;
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;


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
        if(DRIVER_CONTROLLER != null && DRIVETRAIN != null)
        {
            DRIVER_CONTROLLER.resetRumbleCounter();
            DRIVETRAIN.resetEncoders();
        }
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(DRIVER_CONTROLLER != null)
        {
            DRIVER_CONTROLLER.checkRumbleEvent();

            if(DRIVETRAIN != null)
            {
                // TODO : Add slew rate limiter
                double drivePowerLimit = 0.6;
                double turnPowerLimit = 0.1;
                double xSpeed = DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveY) * Constant.MAX_DRIVE_SPEED;
                double ySpeed = DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveX) * Constant.MAX_DRIVE_SPEED;
                double turn = DRIVER_CONTROLLER.getAction(DriverAxisAction.kRotate) * Constant.MAX_ROBOT_TURN_SPEED;

                // Scales down the input power
                // TODO : Add button for full power
                drivePowerLimit += DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost) * (1.0 - drivePowerLimit);

                xSpeed *= drivePowerLimit;
                ySpeed *= drivePowerLimit;
                turn *= turnPowerLimit;

                DRIVETRAIN.drive(xSpeed, ySpeed, turn, true);

                // running the drivetrain
                // DRIVETRAIN.moveYAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveY));

                // DRIVETRAIN.moveXAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveX));

                // DRIVETRAIN.rotate(DRIVER_CONTROLLER.getAction(DriverAxisAction.kRotate));

                // DRIVETRAIN.driveBoost(DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost));
            }

            // Running the intake
            if(INTAKE != null)
            {
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
                else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeExtendToggle))
                {
                    if(INTAKE.getArmPosition() == Intake.ArmPosition.kOff || INTAKE.getArmPosition() == Intake.ArmPosition.kIn)
                    {
                        INTAKE.moveArmOut();
                    }
                    else if(INTAKE.getArmPosition() == Intake.ArmPosition.kOut)
                    {
                        INTAKE.moveArmIn();
                    }
                }
                else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeArmStop))
                {
                    INTAKE.stopArm();
                }
            }
        }

        if(OPERATOR_CONTROLLER != null)
        {
            if(SHUTTLE != null)
            {
                // running the shuttle
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleOverride))
                {
                    // SHUTTLE.overrideFSM();
                    if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttle1stStageOn))
                    {
                        SHUTTLE.forwardStageOne();
                    }

                    else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttle2ndStageOn))
                    {
                        SHUTTLE.forwardStageTwo();
                    }

                    else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttle1stStageOff))
                    {
                        SHUTTLE.stopStageOne();
                    }

                    else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttle2ndStageOff))
                    {
                        SHUTTLE.stopStageTwo();
                    }
                }
                else
                {
                    // SHUTTLE.run();
                }
            }

            if(SHOOTER != null && SHUTTLE != null)
            {
                // running the shooter
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShooterOverride))
                {
                    // SHOOTER.overrideFSM();

                    SHOOTER.setShroudAngleNew(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud)); 
                    
                    SHOOTER.setFlywheelSpeedNew(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower));
                
                    if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShootBallToggle))
                    {
                        // SHUTTLE.overrideFSM();
                        SHUTTLE.forwardStageTwo(); 
                        SHUTTLE.forwardStageOne();
                    }
                }
                else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot))
                {
                    SHOOTER.shoot();
                }
            }

            if(CLIMBER != null)
            {
                // running the climber
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbUp))
                {
                    CLIMBER.climbUp();
                }
                else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbDown))
                {
                    CLIMBER.climbDown();
                }
                else// if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbShutDown))
                {
                    CLIMBER.shutDown();
                }
            }
        }
        
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }
}
