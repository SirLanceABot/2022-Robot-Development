package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.components.Intake;
import frc.components.Climber;
import frc.components.EventGenerator;
import frc.drivetrain.Drivetrain;
import frc.shuffleboard.CameraTab;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.ShuttleFSM;
import frc.constants.Constant;

import frc.controls.DriverController;
import frc.controls.DriverController.DriverAxisAction;
import frc.controls.DriverController.DriverButtonAction;
import frc.controls.DriverController.DriverDpadAction;
import frc.controls.OperatorController;
import frc.controls.OperatorController.OperatorAxisAction;
import frc.controls.OperatorController.OperatorButtonAction;
import frc.controls.OperatorController.OperatorDpadAction;


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

    private static final EventGenerator EVENT_GENERATOR = RobotContainer.EVENT_GENERATOR;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;

    private static final PowerDistribution PDH = RobotContainer.PDH;
    private static final CameraTab CAMERA_TAB = RobotContainer.CAMERA_TAB;

    // Testing variable
    private static final double SHOOTER_SPEED = 0.5;

    private static final double FEET_TO_METERS = 0.3048;

    // TODO: Make roller toggle also need to make manual shooter controls
    // Toggle variables
    private static boolean rollerToggle = false;

    private static double angleToTurn;
    private static double driveTrainRotation;

    private static boolean autoRunClimb = true;

    double testingRPM = 0.0;
    double testingShroud = -235;
    double testingDistance = 0.0;
    double useTestDistance = 0.0;


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
        if(CAMERA_TAB != null)
        {
            CAMERA_TAB.updateLimeLightMode();
        }

        if(DRIVER_CONTROLLER != null && DRIVETRAIN != null)
        {
            DRIVER_CONTROLLER.resetRumbleCounter();
            DRIVETRAIN.resetEncoders();
        }

        if (SHUTTLEFSM != null)
        {
            SHUTTLEFSM.measureAndSetCurrentState();
        }

        testingRPM = SmartDashboard.getNumber("RPM", testingRPM);
        testingShroud = SmartDashboard.getNumber("Shroud", testingShroud);
        testingDistance = SmartDashboard.getNumber("Distance", testingDistance);
        useTestDistance = SmartDashboard.getNumber("Use Test Distance (1 for yes, 0 for no)", useTestDistance);
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if(DRIVETRAIN != null)
        {
            // Testing navX
            // printNavX());
        }
        
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
        if(OPERATOR_CONTROLLER == null)
        {
            System.out.println("NO OPERATOR CONTROLLER");
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

            // TODO: Change LED to Dpad on operator controller
            if (OPERATOR_CONTROLLER.getAction(OperatorDpadAction.kLED) || OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
            // if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kLight) || OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
            {
                // PDH.setSwitchableChannel(true);
            }
            else
            {
                // PDH.setSwitchableChannel(false);
            }

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

                    if (SHOOTER != null)
                    {
                        shoot = OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kShoot) || (SHOOTER.isShooterReady());
                    }

                    // TODO: Make this not here
                    SHUTTLEFSM.fancyRun(shoot);
                    // System.out.println("FANCY RUN RAN");
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
                    if (INTAKE != null)
                    {
                        INTAKE.compressorDisable();
                    }

                    // Used to test the shooter values
                    // SHOOTER.testShoot(8000.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower), SHOOTER.measureShroudAngle() + OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud) * 10.0);

                    // SHOOTER.turnOnLED();

                    // Change to kLower or kUpper to determine shot type
                    if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kLowerHub))
                    {
                        SHOOTER.prepareShooter(Shooter.Hub.kLower); //SHOOT
                    }
                    else
                    {
                        SHOOTER.prepareShooter(Shooter.Hub.kUpper); //SHOOT
                    }
                    
                    // System.out.println("PDH READOUT FOR FLYWHEEL: " + PDH.getCurrent(10));
                    if (SHOOTER.isFlywheelReady())
                    {
                        System.out.println("==================== FLYWHEEL IS READY ===================");
                    }
                    if (SHOOTER.isShroudReady())
                    {
                        System.out.println("==================== SHROUD IS READY =====================");
                    }
                    if (SHOOTER.isHubAligned())
                    {
                        System.out.println("==================== HUB IS READY ========================");
                    }
                    if (SHOOTER.isShooterReady())
                    {
                        System.out.println("==================== SHOOTER IS READY ====================");
                    }

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
                    if (INTAKE != null)
                    {
                        INTAKE.compressorDisable();
                    }

                    if (SHOOTER.isFlywheelReady())
                    {
                        System.out.println("==================== FLYWHEEL IS READY ===================");
                    }
                    if (SHOOTER.isShroudReady())
                    {
                        System.out.println("==================== SHROUD IS READY =====================");
                    }
                    if (SHOOTER.isHubAligned())
                    {
                        System.out.println("==================== HUB IS READY ========================");
                    }
                    if (SHOOTER.isShooterReady())
                    {
                        System.out.println("==================== SHOOTER IS READY ====================");
                    }
                    // SHOOTER.testShoot(8000.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower), SHOOTER.measureShroudAngle() + OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud) * 10.0);
                    // SHOOTER.testShoot(0.0, SHOOTER.measureShroudAngle() + OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud) * 10.0);
                    // if (useTestDistance == 1.0)
                    // {
                    //     System.out.println(testingDistance);
                    //     SHOOTER.prepareShooter(Shooter.Hub.kUpper, testingDistance);
                    // }
                    // else
                    // {
                    //     SHOOTER.prepareShooter(testingRPM, testingShroud);
                    // }
                    if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kLowerHub))
                    {
                        SHOOTER.prepareShooter(Shooter.Hub.kLower, 6.5 * FEET_TO_METERS);
                    }
                    else
                    {
                        SHOOTER.prepareShooter(Shooter.Hub.kUpper, 6.5 * FEET_TO_METERS);
                    }
                    
                    // System.out.println("PDH READOUT FOR FLYWHEEL: " + PDH.getCurrent(10));
                    // SHOOTER.prepareShooter(Shooter.Hub.kUpper, 6.5 * FEET_TO_METERS * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShooterPower));
                    // SHOOTER.setShroudMotorSpeedNew(1.0 * OPERATOR_CONTROLLER.getAction(OperatorAxisAction.kShroud));
                }
                else
                {
                    if (INTAKE != null)
                    {
                        if (OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kCompressorToggle))
                        {
                            INTAKE.compressorDisable();
                        }
                        else
                        {
                            INTAKE.compressorEnable();
                        }
                    }
                    // SHOOTER.turnOffLED();
                    SHOOTER.stopShooter();

                    SHOOTER.resetShooterChecks();
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

            if(CLIMBER == null)
            {
                System.out.println("Climber is null");
            }
            
            if(CLIMBER != null)
            {
                // running the climber
                /* // Darren's idea
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbOneExtend) && OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbOneRetract))
                {
                    if(!autoRunClimb)
                    {
                        autoRunClimb = true;
                        System.out.println("Enabled auto climb");
                    }
                }
                else */
                if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbOneExtend))
                {
                    // System.out.println("CLIMB UP");
                    CLIMBER.FCLArmUp();

                    /* // Darren's idea
                    if(autoRunClimb)
                    {
                        autoRunClimb = false;
                        System.out.println("Disabled auto climb");
                    }
                    */

                    //SCLArmUp
                }
                else if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbOneRetract))
                {
                    // System.out.println("CLIMB DOWN");
                    CLIMBER.FCLArmDown();

                    /* // Darren's idea
                    if(autoRunClimb)
                    {
                        autoRunClimb = false;
                        System.out.println("Disabled auto climb");
                    }
                    */
                    //SCLArmDown
                }
                else// if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbShutDown))
                {
                    /* // Darren's idea
                    if(autoRunClimb)
                    {
                        CLIMBER.FCLHold();
                    }
                    else
                    {
                        CLIMBER.FCLStop();
                    }
                    */
                    
                    CLIMBER.FCLShutDown();
                }

                // CLIMBER.measureFCLLimits();

                if(OPERATOR_CONTROLLER.getAction(OperatorDpadAction.kResetClimberEncoder))
                {
                    System.out.println("Set climber encoder to 0.0");
                    CLIMBER.resetFCLEncoder();
                }

                /* // TODO: Use if second stage added
                if (DRIVER_CONTROLLER.getAction(DriverDpadAction.kClimbTwoExtend))
                {
                    CLIMBER.SCLArmUp();
                }
                else if(DRIVER_CONTROLLER.getAction(DriverDpadAction.kClimbTwoRetract))
                {
                    CLIMBER.SCLArmDown();
                }
                else// if(OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kClimbShutDown))
                {
                    CLIMBER.SCLShutDown();
                }
                */
            }
        }

        if(DRIVER_CONTROLLER != null)
        {
            DRIVER_CONTROLLER.checkRumbleEvent();

            if(DRIVETRAIN != null)
            {
                // TODO : Add slew rate limiter
                double drivePowerLimit = 0.8;
                double turnPowerLimit = 0.1;
                double xSpeed = DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveY) * Constant.MAX_DRIVE_SPEED;
                double ySpeed = DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveX) * Constant.MAX_DRIVE_SPEED;
                double turn = DRIVER_CONTROLLER.getAction(DriverAxisAction.kRotate) * Constant.MAX_ROBOT_TURN_SPEED;

                if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kBoostOrAutoAim) && !OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
                {
                    drivePowerLimit = 1.0;
                }

                // Scales down the input power
                // TODO : Add button for full power
                // drivePowerLimit += DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost) * (1.0 - drivePowerLimit);

                xSpeed *= drivePowerLimit;
                ySpeed *= drivePowerLimit;
                turn *= turnPowerLimit;

                if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kBoostOrAutoAim) && OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
                {
                    // if (!SHOOTER.isHubAligned())
                    // {
                        angleToTurn = SHOOTER.getHubAngle();

                        System.out.println("ANGLE TO TURN: " + angleToTurn);

                        driveTrainRotation = DRIVETRAIN.calculateTurnRotation(0.1 * 2 * Math.PI, 0.5 * 2 * Math.PI, DRIVETRAIN.getGyro() - angleToTurn, Constant.HUB_ALIGNMENT_THRESHOLD);
                        
                        // if (Math.abs(angleToTurn) > 15.0)
                        // {
                        //     angleToTurn *= 15.0 / Math.abs(angleToTurn);
                        // }

                        // driveTrainRotation = -angleToTurn / 15.0 * (0.4 * Math.PI - 0.1) + 0.1 * Math.signum(-angleToTurn);
                    // }
                }
                else if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlRight))
                {
                    driveTrainRotation = -0.6;
                }
                else if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlLeft))
                {
                    driveTrainRotation = 0.6;
                }
                else
                {
                    driveTrainRotation = turn;
                }
                
                if (DRIVER_CONTROLLER.getAction(DriverDpadAction.kLockSwerve))
                {
                    // System.out.println("Locking swerve drive");
                    DRIVETRAIN.lock();
                }
                else
                {
                    DRIVETRAIN.drive(xSpeed, ySpeed, driveTrainRotation, !DRIVER_CONTROLLER.getAction(DriverButtonAction.kRobotOriented));
                }

                // running the drivetrain
                // DRIVETRAIN.moveYAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveY));

                // DRIVETRAIN.moveXAxis(DRIVER_CONTROLLER.getAction(DriverAxisAction.kMoveX));

                // DRIVETRAIN.rotate(DRIVER_CONTROLLER.getAction(DriverAxisAction.kRotate));

                // DRIVETRAIN.driveBoost(DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost));

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

                if (EVENT_GENERATOR != null)
                {
                    if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeArmOut))
                    {
                        INTAKE.armOut();
                    }
                    // Sensor option is lower priority so driver can override it by pressing arm out button
                    else if(DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeArmIn) || EVENT_GENERATOR.isIntakeSensorActive())
                    {
                        INTAKE.armIn();
                    }
                    else
                    {
                        if(INTAKE.measureArmOut())
                        {
                            // System.out.println("Arm is out");
                            INTAKE.pMoveArmFloat();
                        }

                        if(INTAKE.measureArmIn())
                        {
                            // System.out.println("Arm is in");
                        }
                    }
                }

                // System.out.println("INTAKE OUT? " + INTAKE.measureArmOut());
                // System.out.println("INTAKE IN? " + INTAKE.measureArmIn());

                // System.out.println("Arm in = " + INTAKE.measureArmIn() + ", Arm out = " + INTAKE.measureArmOut());

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
                boolean shoot = DRIVER_CONTROLLER.getAction(DriverButtonAction.kRobotOriented);

                // SHUTTLEFSM.fancyRun(shoot);
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
