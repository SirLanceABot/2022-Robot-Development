package frc.test;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.components.Shuttle;
import frc.controls.DriverController;
import frc.controls.DriverController.DriverButtonAction;
import frc.robot.RobotContainer;

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
    
    Shuttle.State state = Shuttle.State.NO_CARGO_STORED;

    private static int i = 0;

    private boolean previousStateOfBumper = false;


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
        FSMTesting();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {

    }   

    private void FSMTesting()
    {
        Shuttle.Events.event event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;

        boolean currentStateOfBumper = DRIVER_CONTROLLER.getAction(DriverButtonAction.kIntakeToggleOnOff);


        if(previousStateOfBumper != currentStateOfBumper)
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
            System.out.println("State: " + state.toString());
        }
        else
        {
            event = Shuttle.Events.event.NONE;
        }

        state = Shuttle.Transition.findNextState(state, event);

        // SmartDashboard.putString("State", state.toString());
        // SmartDashboard.putString("Event", event.toString());

        previousStateOfBumper = currentStateOfBumper;
    }
}
