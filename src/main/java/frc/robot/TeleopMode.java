package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.PowerDistribution;
import frc.components.Intake;
import frc.components.Climber;
import frc.components.EventGenerator;
import frc.drivetrain.Drivetrain;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.ShuttleFSM;
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

    // private static final EventGenerator EVENT_GENERATOR = RobotContainer.EVENT_GENERATOR;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;

    private static final PowerDistribution PDH = RobotContainer.PDH;

    // Testing variable
    private static final double SHOOTER_SPEED = 0.5;

    // TODO: Make roller toggle also need to make manual shooter controls
    // Toggle variables
    private static boolean rollerToggle = false;


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

        if (SHUTTLEFSM != null)
        {
            SHUTTLEFSM.measureAndSetCurrentState();
        }
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        
        if(DRIVETRAIN != null)
        {
            // Testing navX
            // System.out.println("Yaw: " + DRIVETRAIN.navX.getYaw());
        }

        if(DRIVER_CONTROLLER != null)
        {
            DRIVER_CONTROLLER.checkRumbleEvent();

            if(DRIVETRAIN != null)
            {
                if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlRight))
                {
                    DRIVETRAIN.drive(0.0, 0.0, -0.5, true);
                }
                else if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlLeft))
                {
                    DRIVETRAIN.drive(0.0, 0.0, 0.5, true);
                }
                else
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

                    if (DRIVER_CONTROLLER.getAction(DriverAxisAction.kRobotOriented) == 1.0)
                    {
                        DRIVETRAIN.drive(xSpeed, ySpeed, turn, false);
                    }
                    else
                    {
                        DRIVETRAIN.drive(xSpeed, ySpeed, turn, true);
                    }

                    // running the drivetrain
                    // DRIVETRAIN.moveYAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveY));

                    // DRIVETRAIN.moveXAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveX));

                    // DRIVETRAIN.rotate(DRIVER_CONTROLLER.getAction(DriverAxisAction.kRotate));

                    // DRIVETRAIN.driveBoost(DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost));
                }

                if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kResetGyro))
                {
                    DRIVETRAIN.resetGyro();
                }
            }

            // Running the intake
            if(INTAKE != null)
            {
                if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff))
                {
                    if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleDirection))
                    {
                        INTAKE.outtakeRoller();
                    }
                    else
                    {
                        INTAKE.intakeRoller();
                    }
                }
                else
                {
                    INTAKE.turnOffRoller();
                }
                // if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff))
                // {
                //     if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOff)
                //     {
                //         INTAKE.intakeRoller();
                //     }
                //     else
                //     {
                //         INTAKE.turnOffRoller();
                //     }
                // }
                // else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleDirection))
                // {
                //     if(INTAKE.getRollerDirection() == Intake.RollerDirection.kIn)
                //     {
                //         INTAKE.outtakeRoller();
                //     }
                //     else if(INTAKE.getRollerDirection() == Intake.RollerDirection.kOut)
                //     {
                //         INTAKE.intakeRoller();
                //     }
                // }
                
                // A testing line
                // INTAKE.outputArmLimit();

                if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeExtendToggle))
                {
                    INTAKE.moveArmOut();
                }
                else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeArmStop))
                {
                    INTAKE.moveArmIn();
                }
                else
                {
                    INTAKE.stopArm();
                }

                // if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeExtendToggle))
                // {
                //     if(INTAKE.getArmPosition() == Intake.ArmPosition.kOff || INTAKE.getArmPosition() == Intake.ArmPosition.kIn)
                //     {
                //         INTAKE.moveArmOut();
                //     }
                //     else if(INTAKE.getArmPosition() == Intake.ArmPosition.kOut)
                //     {
                //         INTAKE.moveArmIn();
                //     }
                // }
                // else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeArmStop))
                // {
                //     INTAKE.stopArm();
                // }

                // System.out.println("INTAKE POSITION " + INTAKE.getArmPosition());
            }
            
        // TODO: Remove this?
        // Running the shuttle
        if (SHUTTLE != null)
        {
            // TODO: Make this not here
            boolean shoot = DRIVER_CONTROLLER.getAction(DriverButtonAction.kShoot);

            // SHUTTLEFSM.fancyRun(shoot);
        }

        if(OPERATOR_CONTROLLER != null)
        {
            // TODO: Fix this feedCargo
            // Manual shoot
            // if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot))
            // {
            //     SHUTTLEFSM.feedCargo();
            //     // Debugging feedCargo
            //     // System.out.println("Feed cargo request");
            // }

            if(SHUTTLE != null)
            {
                // running the shuttle
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleOverride))
                {
                    // SHUTTLE.overrideFSM();
                    if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleStageOneForward))
                    {
                        SHUTTLE.forwardStageOne();
                    }

                    else if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleStageOneReverse))
                    {
                        SHUTTLE.reverseStageOne();
                    }

                    else
                    {
                        SHUTTLE.stopStageOne();
                    }

                    if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleStageTwoForward))
                    {
                        SHUTTLE.forwardStageTwo();
                    }

                    else if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShuttleStageTwoReverse))
                    {
                        SHUTTLE.reverseStageTwo();
                    }

                    else
                    {
                        SHUTTLE.stopStageTwo();
                    }

                    SHUTTLEFSM.measureAndSetCurrentState();
                }
                else
                {
                    // TODO: Break this stuff out of fancyrun for this
                    // Do what the Shuttle has requested
                    // SHUTTLEFSM.runMotorRequests();

                    boolean shoot = OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot);

                    // TODO: Make this not here
                    SHUTTLEFSM.fancyRun(shoot);
                }
            }

            if(SHOOTER != null)
            {
                // TODO: Remove this?
                // Running the shuttle
                if (SHUTTLE != null)
                {
                    // boolean shoot = OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot);

                    // // TODO: Make this not here
                    // SHUTTLEFSM.fancyRun(shoot);
                }

                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
                {
                    // Used to test the shooter values
                    // SHOOTER.testShoot(8000.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower), SHOOTER.measureShroudAngle() + OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud) * 10.0);

                    PDH.setSwitchableChannel(true);
                    // SHOOTER.turnOnLED();

                    // Change to kLower or kUpper to determine shot type
                    SHOOTER.shoot(Shooter.Hub.kUpper); //SHOOT

                    // SHOOTER.setFlywheelSpeedNew(SHOOTER_SPEED);
                    
                    // if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kTurnOnShooterToggle))
                    // {
                    //     // SHOOTER.setFlywheelSpeedNew(-SHOOTER_SPEED);
                    // }
                    // else
                    // {
                    //     SHOOTER.setFlywheelSpeedNew(SHOOTER_SPEED);
                    // }
                }
                else if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShooterOverride))
                {
                    PDH.setSwitchableChannel(false);

                    SHOOTER.testShoot(8000.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower), SHOOTER.measureShroudAngle() + OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud) * 10.0);
                }
                else
                {
                    PDH.setSwitchableChannel(false);
                    // SHOOTER.turnOffLED();
                    SHOOTER.stopFlywheel();
                }

                // SHOOTER.outputShroudLimit();
                // SHOOTER.setShroudMotorSpeedNew(1.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud));
            }

            // if(SHOOTER != null && SHUTTLE != null)
            // {
            //     // running the shooter
            //     if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShooterOverride))
            //     {
            //         // SHOOTER.overrideFSM();

            //         SHOOTER.setShroudAngleNew(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud)); 
                    
            //         SHOOTER.setFlywheelSpeedNew(OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower));
                
            //         if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShootBallToggle))
            //         {
            //             // SHUTTLE.overrideFSM();
            //             SHUTTLE.forwardStageTwo(); 
            //             SHUTTLE.forwardStageOne();
            //         }
            //     }
            //     else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot))
            //     {
            //         SHOOTER.shoot();
            //     }
            // }

            if(CLIMBER != null)
            {
                // running the climber
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbExtend))
                {
                    CLIMBER.climbUp();
                }
                else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbRetract))
                {
                    CLIMBER.climbDown();
                }
                else// if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbShutDown))
                {
                    CLIMBER.shutDown();
                }
            }
        }}
        
        // TODO: Break this stuff out of fancyrun for this
        /*
        // Generate events
        if(EVENT_GENERATOR != null)
        {
            EVENT_GENERATOR.determineEvents();
        }
        // Run ShuttleFSM to generate motor requests
        if(SHUTTLEFSM != null)
        {
            SHUTTLEFSM.run();
        }
        */
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }
}
