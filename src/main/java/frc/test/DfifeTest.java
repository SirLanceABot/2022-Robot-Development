package frc.test;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.components.Shuttle;
import frc.components.Shuttle.Events.event;
import frc.controls.DriverController;
import frc.controls.Xbox;
import frc.controls.DriverController.DriverButtonAction;
import frc.robot.RobotContainer;

// TODO: Testing numbers
/*
// Port.java
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
    private static final Shuttle shuttle = RobotContainer.SHUTTLE;

    private static Shuttle.Events.event[] array =
    {
        Shuttle.Events.event.NONE,
        Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
        Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES, // FIXME Remove this state and base it on something else?
        Shuttle.Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES,
        Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,
        Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES,
        Shuttle.Events.event.SHOOT_IS_CALLED
    };
    
    private Shuttle.Events.event event;

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

        FSMTestingV2();

        testShuttleSensors();
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
    
    // Testing the shuttle movements without FSM
    private void shuttleTesting()
    {
        if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kA))
        {
            // Move first stage
            System.out.println("Stage one activated");
            shuttle.forwardFirstStage();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kB))
        {
            // Move second stage
            System.out.println("Stage two activated");
            shuttle.forwardSecondStage();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kX))
        {
            // Reverse both stages
            System.out.println("Stage one and two reversed");
            shuttle.reverseFirstStage();
            shuttle.reverseSecondStage();
        }
        else if(DRIVER_CONTROLLER.getRawButton(Xbox.Button.kY))
        {
            // Move both stages
            System.out.println("Stage one and two activated");
            shuttle.forwardFirstStage();
            shuttle.forwardSecondStage();
        }
        else
        {
            // Stop both stages
            shuttle.stopFirstStage();
            shuttle.stopSecondStage();
        }
    }

    // Testing FSM out
    private void FSMTestingV2()
    {
        // Initially say there is no event then continue to look for an event
        event = Shuttle.Events.event.NONE;

        // Each event is a button
        if(currentButtonA != previousButtonA)
        {
            if (currentButtonA)
            {
                event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            previousButtonA = currentButtonA;
        }
        else if(currentButtonY != previousButtonY)
        {
            if (currentButtonY)
            {
                event = Shuttle.Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                // STAGE_ONE_FULL_SENSOR_DEACTIVATES
            }

            previousButtonY = currentButtonY;
        }
        else if(currentButtonB != previousButtonB)
        {
            if (currentButtonB)
            {
                event = Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                event = Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            previousButtonB = currentButtonB;
        }
        else if(DRIVER_CONTROLLER.getRawAxis(Xbox.Axis.kLeftTrigger) > 0.5)
        {
            event = Shuttle.Events.event.SHOOT_IS_CALLED;
        }

        if (event != Shuttle.Events.event.NONE)
        {
            System.out.println("Event name: " + event.toString());
        }
        shuttle.fancyRun(event);
    }

    // Testing FSM out using cycling through events
    private void FSMTestingV1()
    {
        // Initially say there is no event then continue to look for an event
        event = Shuttle.Events.event.NONE;

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
            event = Shuttle.Events.event.NONE;
        }
        
        shuttle.fancyRun(event);

        previousStateOfButton = currentStateOfButton;
    }

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
        System.out.println("Intake Sensor: " + shuttle.measureIntakeSensor());
        System.out.println("First Stage Sensor: " + shuttle.measureFirstStageSensor());
        System.out.println("Second Stage Sensor: " + shuttle.measureSecondStageSensor());
    }
}
