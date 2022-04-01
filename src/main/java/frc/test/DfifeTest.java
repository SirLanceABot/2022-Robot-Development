package frc.test;

import java.lang.invoke.MethodHandles;

import frc.components.Events;
import frc.components.SensorValues;
import frc.components.Shooter;
import frc.components.Shuttle;
import frc.components.ShuttleFSM;
import frc.constants.Constant;
import frc.controls.DriverController;
import frc.controls.OperatorController;
import frc.controls.Xbox;
import frc.controls.DriverController.DriverAxisAction;
import frc.controls.DriverController.DriverButtonAction;
import frc.controls.DriverController.DriverDpadAction;
import frc.controls.OperatorController.OperatorButtonAction;
import frc.drivetrain.Drivetrain;
import frc.robot.RobotContainer;

// TODO: Testing numbers
/*
// Port.java
// Numbers are what the sparkmaxs say they are 
public static final int SHUTTLE_STAGE_ONE = 7;
public static final int SHUTTLE_STAGE_TWO = 5;

// RobotContainer.java
public static final boolean useIntake2             = false;
public static final boolean useShooter             = false;
public static final boolean useShuttle             = true;

// TestMode.java
import frc.test.DfifeTest;

// private static final BlankTest myTest = new BlankTest();
// private static final AburriTest myTest = new AburriTest();
private static final DfifeTest myTest = new DfifeTest();
*/

public class DfifeTest implements MyTest
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***

    

    // *** CLASS & INSTANCE VARIABLES ***
    private static final DriverController DRIVER_CONTROLLER = RobotContainer.DRIVER_CONTROLLER;
    private static final OperatorController OPERATOR_CONTROLLER = RobotContainer.OPERATOR_CONTROLLER;
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    private static final Shooter SHOOTER = RobotContainer.SHOOTER;
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;

    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;

    // private static Events.ShuttleEvent[] array =
    // {
    //     Events.ShuttleEvent.NONE,
    //     Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
    //     Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES, // FIXME Remove this event and base it on something else?
    //     Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES,
    //     Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_DEACTIVATES,
    //     Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,
    //     Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES,
    //     Events.ShuttleEvent.FEED_CARGO
    // };
    
    // private Events.ShuttleEvent event;

    private boolean previousButtonA = false;
    private boolean previousButtonB = false;
    private boolean previousButtonX = false;
    private boolean previousButtonY = false;

    private boolean currentButtonA = false;
    private boolean currentButtonB = false;
    private boolean currentButtonX = false;
    private boolean currentButtonY = false;

    private static int i = 0;

    private boolean previousStateOfButton = false;
    
    private static double angleToTurn;
    private static double driveTrainRotation;


    // *** CLASS CONSTRUCTOR ***
    public DfifeTest()
    {

    }


    // *** CLASS & INSTANCE METHODS ***
    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {

    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        // Measure ABXY buttons
        measureABXY();

        // System.out.println("Intake Sensor: " + shuttle.measureIntakeSensor());
        // System.out.println("First Stage Sensor: " + shuttle.measureStageOneSensor());
        // System.out.println("Second Stage Sensor: " + shuttle.measureStageTwoSensor());
        // testShuttleSensors();

        driveTrainControlsTesting();

        // FSMTestingV2();

        // shuttleTesting();
        // FSMTestingV1();

        // Remember ABXY buttons
        // rememberABXY();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }

    private void driveTrainControlsTesting()
    {
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

                // Scales down the input power
                // TODO : Add button for full power
                // drivePowerLimit += DRIVER_CONTROLLER.getAction(DriverAxisAction.kDriverBoost) * (1.0 - drivePowerLimit);

                xSpeed *= drivePowerLimit;
                ySpeed *= drivePowerLimit;
                turn *= turnPowerLimit;

                if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kAutoAim) && OPERATOR_CONTROLLER.getAction(OperatorButtonAction.kPrepareShooter))
                {
                    if (!SHOOTER.isHubAligned())
                    {
                        angleToTurn = SHOOTER.getHubAngle();

                        System.out.println("ANGLE TO TURN: " + angleToTurn);

                        driveTrainRotation = -angleToTurn / 15.0 * (0.7 - 0.2) + 0.2 * Math.signum(-angleToTurn);
                    }
                }
                else if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlRight))
                {
                    driveTrainRotation = -0.5;
                }
                else if (DRIVER_CONTROLLER.getAction(DriverButtonAction.kCrawlLeft))
                {
                    driveTrainRotation = 0.5;
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
        }
    }
    
    // Testing the shuttle movements without FSM
    private void shuttleTesting()
    {
        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA))
        {
            // Move first stage
            System.out.println("Stage one activated");
            SHUTTLE.forwardStageOne();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB))
        {
            // Move second stage
            System.out.println("Stage two activated");
            SHUTTLE.forwardStageTwo();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX))
        {
            // Reverse both stages
            System.out.println("Stage one and two reversed");
            SHUTTLE.reverseStageOne();
            SHUTTLE.reverseStageTwo();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY))
        {
            // Move both stages
            System.out.println("Stage one and two activated");
            SHUTTLE.forwardStageOne();
            SHUTTLE.forwardStageTwo();
        }
        else
        {
            // Stop both stages
            SHUTTLE.stopStageOne();
            SHUTTLE.stopStageTwo();
        }
    }

    // Testing FSM out
    private void FSMTestingV2()
    {
        // boolean shoot = DRIVER_CONTROLLER.getRawAxis(Xbox.Axis.kLeftTrigger) > 0.5;

        // Determine an event based on controller input
        // event = determineEventFromController(shoot);
        // shuttle.fancyRun(event);

        // SHUTTLEFSM.fancyRun(shoot);
        // SHUTTLEFSM.run();
    }

    /*
    // Returns a determined event from the controller input
    private Events.ShuttleEvent determineEventFromController(boolean shoot)
    {
        // Initially say there is no event then continue to look for an event
        Events.ShuttleEvent determinedEvent = Events.ShuttleEvent.NONE;

        // Each event is a button
        if(currentButtonA != previousButtonA)
        {
            if (currentButtonA)
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            previousButtonA = currentButtonA;
        }
        else if(currentButtonY != previousButtonY)
        {
            if (currentButtonY)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_DEACTIVATES;
            }

            previousButtonY = currentButtonY;
        }
        else if(currentButtonB != previousButtonB)
        {
            if (currentButtonB)
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            previousButtonB = currentButtonB;
        }
        else if(shoot)
        {
            determinedEvent = Events.ShuttleEvent.FEED_CARGO;
        };

        return determinedEvent;
    }

    // Testing FSM out using cycling through events
    private void FSMTestingV1()
    {
        // Initially say there is no event then continue to look for an event
        event = Events.ShuttleEvent.NONE;

        // Cycle through the available events using a single button
        boolean currentStateOfButton = DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA);

        if(previousStateOfButton != currentStateOfButton)
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
            
            System.out.println("Event: " + event.toString());
        }
        else
        {
            event = Events.ShuttleEvent.NONE;
        }
        
        // shuttle.fancyRun(event);

        previousStateOfButton = currentStateOfButton;
    }
    */

    private void measureABXY()
    {
        currentButtonA = DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA);
        currentButtonB = DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB);
        currentButtonX = DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX);
        currentButtonY = DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY);
    }

    private void rememberABXY()
    {
        previousButtonA = currentButtonA;
        previousButtonB = currentButtonB;
        previousButtonX = currentButtonX;
        previousButtonY = currentButtonY;
    }

    private void testShuttleSensors()
    {
        if (SHUTTLE.measureIntakeSensor() || SHUTTLE.measureStageOneSensor() || SHUTTLE.measureStageTwoSensor())
        {
            System.out.println("Shuttle Sensors: " + SHUTTLE.measureIntakeSensor() + 
                                              ", " + SHUTTLE.measureStageOneSensor() + 
                                              ", " + SHUTTLE.measureStageTwoSensor());
            // System.out.println("Intake Sensor: " + shuttle.measureIntakeSensor());
            // System.out.println("First Stage Sensor: " + shuttle.measureStageOneSensor());
            // System.out.println("Second Stage Sensor: " + shuttle.measureStageTwoSensor());
        }
    }
}
