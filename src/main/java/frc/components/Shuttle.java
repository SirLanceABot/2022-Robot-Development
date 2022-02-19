package frc.components;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.CANPIDController;

import frc.constants.Port;

public class Shuttle 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // initializing motors
    private static final CANSparkMax firstStageMotor = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_ONE, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final CANSparkMax secondStageMotor = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_TWO, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final double FIRST_STAGE_SPEED = 0.25;
    private static final double SECOND_STAGE_SPEED = 0.25;

    // initializing sensors TODO
    private static final DigitalInput intakeSensor = new DigitalInput(Port.Sensor.INTAKE_SENSOR);
    private static final DigitalInput firstStageSensor = new DigitalInput(Port.Sensor.FIRST_STAGE_SENSOR);
    private static final DigitalInput secondStageSensor = new DigitalInput(Port.Sensor.SECOND_STAGE_SENSOR);

    // initializing encoders
    // private static CANEncoder firstEncoder = firstStageMotor.getEncoder();
    // private static CANEncoder secondEncoder = secondStageMotor.getEncoder();

    // *** FSM Stuff ***
    // Current state
    private State currentShuttleState;

    // What we want to happen with the motors
    // FIXME Make variables not static
    // TODO: Remove these once transition to motorRequest is done
    //private static final boolean[] motorRequests = new boolean[2];
    private static boolean requestStageOneMotorRunning = false;
    private static boolean requestStageTwoMotorRunning = false;

    // public enum MotorStage
    // {
    //     kOne(0), kTwo(1);

    //     public int value;
        
    //     private MotorStage(int value)
    //     {
    //         this.value = value;
    //     }
    // }

    public static class MotorStage
    {
        public boolean stageOne; 
        public boolean stageTwo;
    }

    private static final MotorStage motorRequest = new MotorStage();

    private Shuttle.Events.event event;

    private boolean previousIntakeCanBe = false;
    private boolean previousStageOneFull = false;
    private boolean previousStageTwoFull = false;
    
    private boolean currentIntakeCanBe = false;
    private boolean currentStageOneFull = false;
    private boolean currentStageTwoFull = false;
    // Perhaps the end of class and instance variables

    /**
     * List of allowed Shuttle states, each state should have a doAction
     */
    public enum State
    {
        NO_CARGO_STORED
        {
            void doAction()
            {
                // Request stage one and two to be turned off
                // requestStageOneMotorRunning = false;
                // requestStageTwoMotorRunning = false;
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;
            }
        },
        
        STORING_CARGO_IN_STAGE_TWO
        {
            void doAction()
            {
                // Run stage one and two
                // requestStageOneMotorRunning = true;
                // requestStageTwoMotorRunning = true;
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;
            }
        },

        CARGO_STORED_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                // requestStageOneMotorRunning = false;
                // requestStageTwoMotorRunning = false;
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;
            }
        },
        
        SHOOTING_CARGO_FROM_STAGE_TWO
        {
            void doAction()
            {
                // Run stage two and turn off stage one
                // requestStageOneMotorRunning = false;
                // requestStageTwoMotorRunning = true;
                motorRequest.stageOne = false;
                motorRequest.stageTwo = true;
            }
        },

        STORING_CARGO_IN_STAGE_ONE
        {
            void doAction()
            {
                // Run stage one
                // requestStageOneMotorRunning = true;
                // requestStageTwoMotorRunning = false;
                motorRequest.stageOne = true;
                //motorRequest.stageTwo = false;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Turn off stage one
                //requestStageOneMotorRunning = false;
                // requestStageTwoMotorRunning = false;
                motorRequest.stageOne = false;
                //motorRequest.stageTwo = false;
            }
        },

        SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Run stage one and two
                // requestStageOneMotorRunning = true;
                // requestStageTwoMotorRunning = true;
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;
            }
        },

        STORING_CARGO_IN_STAGE_TWO_FROM_ONE
        {
            void doAction()
            {
                // Run stage one and two
                // requestStageOneMotorRunning = true;
                // requestStageTwoMotorRunning = true;
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;
            }
        },

        EJECTING_EXTRA_CARGO
        {
            void doAction()
            {
                // Eject extra cargo
                // TODO: Make eject work
                System.out.println("Eject extra cargo");
            }
        };

        // method each state must have for its own because the code is different in this example
        abstract void doAction();
        
        // methods each state can use in common because the code is the same in this example
        // void doEnter()
        // {
        //    System.out.println("entering state " + this.name());
        // }

        // void doExit()
        // {
        //     System.out.println("exiting state " + this.name());
        // }
    }
    // End of State enum


    /**
     * Transitions of FSM
     * 
     * each state Transition state is composed of 2 independent variables (current state and event)
     * and a dependent variable (new state)
     */
    public static enum Transition
    {
        // Transition table
        // transition name (current state, event, new state)
        TRANSITION_1  (State.NO_CARGO_STORED,                       Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.STORING_CARGO_IN_STAGE_TWO),
        TRANSITION_2  (State.STORING_CARGO_IN_STAGE_TWO,            Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_TWO),
        TRANSITION_3  (State.CARGO_STORED_IN_STAGE_TWO,             Events.event.SHOOT_IS_CALLED,                                   State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_4  (State.SHOOTING_CARGO_FROM_STAGE_TWO,         Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES,                 State.NO_CARGO_STORED),
        TRANSITION_5  (State.CARGO_STORED_IN_STAGE_TWO,             Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_6  (State.STORING_CARGO_IN_STAGE_ONE,            Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
        TRANSITION_7  (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO,     Events.event.SHOOT_IS_CALLED,                                   State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO),
        TRANSITION_8  (State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO, Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES,                 State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE),
        TRANSITION_9  (State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE,   Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,                   State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_10 (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO,     Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.EJECTING_EXTRA_CARGO),
        TRANSITION_11 (State.EJECTING_EXTRA_CARGO,                  Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES,   State.CARGO_STORED_IN_STAGE_ONE_AND_TWO);

        private final State currentState;
        private final Events.event event;
        private final State nextState;

        Transition(State currentState, Events.event event, State nextState)
        {
            this.currentState = currentState;
            this.event = event;
            this.nextState = nextState;
        }

        /**
         * Table lookup to determine new state given teh curret state and event
         * 
         * @param currentState
         * @param event
         */
        public static State findNextState(Shuttle.State currentState, Shuttle.Events.event event)
        {
            for (Transition transition : Transition.values())
            {
                if (transition.currentState == currentState && transition.event == event)
                {
                    return transition.nextState;
                }
            }

            return currentState;
        }
    }
    // End of Transition

    public void checkStateChange(Events.event event)
    {
        // make the transition to a new currentState if an event triggered it

        State newShuttleState = Shuttle.Transition.findNextState(currentShuttleState, event); // the next state

        // has the state changed by the event?
        // There is a choice here depending on the system.
        // If the state didn't change by this event, you could still go through the doExit
        // and doEnter because of the event, if that's what makes sense for your FSM.
        // This code doesn't redo the doExit, doEnter, and doAction but there is a comment below
        // to move the doAction if you always want to execute it even if no event or state change.
        if (newShuttleState != currentShuttleState)
        {
            // change states
            // currentShuttleState.doExit(); // exit current state
            currentShuttleState = newShuttleState; // switch states
            // currentShuttleState.doEnter(); // initiate new state
            currentShuttleState.doAction();
        }
        // move above doAction to below to always run it
        // currentFanState.doAction(); // always maintain current state or the new state as determined above
    }

    /**
     * getter for the Shuttle current state
     * @return the current shuttle state
     */
    public State getCurrentState()
    {
        return currentShuttleState;
    }

    // *** CLASS CONSTRUCTOR ***
    // TODO: remove the public access modifier so that the constructor can only be accessed inside the package
    public Shuttle()
    {
        // Intital state of the FSM
        currentShuttleState = measureCurrentState();

        // motorRequests[MotorStage.kOne.value] = false;
        // motorRequests[MotorStage.kTwo.value] = false;

        // Initialize motor commands to stop
        motorRequest.stageOne = false;
        motorRequest.stageTwo = false;
    }


    // *** CLASS & INSTANCE METHODS ***

    public State measureCurrentState()
    {
        State measuredState = State.NO_CARGO_STORED;

        // Measure sensor values to determine initial state
        if (!measureFirstStageSensor() && !measureSecondStageSensor())
        {
            measuredState = State.NO_CARGO_STORED;
        }
        else if (measureFirstStageSensor() && !measureSecondStageSensor())
        {
            // Cargo is stored in stage one, but we want it to be stored in stage two so just say we're doing that
            measuredState = State.STORING_CARGO_IN_STAGE_TWO;
        }
        else if (!measureFirstStageSensor() && measureSecondStageSensor())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_TWO;
        }
        else if (measureFirstStageSensor() && measureSecondStageSensor())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_ONE_AND_TWO;
        }

        return measuredState;
    }

    // TODO: Create a configMotor() method to configure each motor FIXME
    

    public void reverseFirstStage()
    {
        setFirstStageSpeed(-FIRST_STAGE_SPEED);
    }
    
    public void stopFirstStage()
    {
        setFirstStageSpeed(0);
    }

    public void forwardFirstStage()
    {
        setFirstStageSpeed(FIRST_STAGE_SPEED);
    }

    public void reverseSecondStage()
    {
        setSecondStageSpeed(-SECOND_STAGE_SPEED);
    }

    public void stopSecondStage()
    {
        setSecondStageSpeed(0);
    }

    public void forwardSecondStage()
    {
        setSecondStageSpeed(SECOND_STAGE_SPEED);
    }

    private void setFirstStageSpeed(double speed)
    {
        if (speed > 1)
        {
            speed = 1;
        }
        else if (speed < -1)
        {
            speed = -1;
        }
        firstStageMotor.set(speed);
    }

    private void setSecondStageSpeed(double speed)
    {
        if (speed > 1)
        {
            speed = 1;
        }
        else if (speed < -1)
        {
            speed = -1;
        }
        secondStageMotor.set(speed);
    }

    // Proximity sensors
    // (intakeSensor, firstStageSensor, secondStageSensor)
    // FIXME TODO: Figure out what True means
    // True means ____
    public boolean measureIntakeSensor()
    {
        return intakeSensor.get();
    }

    public boolean measureFirstStageSensor()
    {
        return firstStageSensor.get();
    }
    
    public boolean measureSecondStageSensor()
    {
        return secondStageSensor.get();
    }
    
    // TODO: Make toString()
    public String toString()
    {
        return null;
    }

    public static class Events
    {
        public static enum event
        {
            NONE,
            INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
            INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES,
            STAGE_ONE_FULL_SENSOR_ACTIVATES,
            STAGE_TWO_FULL_SENSOR_ACTIVATES,
            STAGE_TWO_FULL_SENSOR_DEACTIVATES,
            SHOOT_IS_CALLED;
        }
    }

    public void fancyRun(Events.event event)
    // public void fancyRun(boolean shoot)
    {
        // TODO: Add measuring sensors to determine event, probablly in a different method
        // Could possibly then change measureState to use that as well
        // determineEvent(shoot);

        // Send event to FSM
        checkStateChange(event);

        // FIXME Make sure that requests do not get whiped out because one of the doActions does not set one motor
        // TODO: Move this to an outer layer where will use flags to run motors, also need to make getMotorRequest()
        if (motorRequest.stageOne)
        {
            forwardFirstStage();
        }
        else
        {
            stopFirstStage();
        }

        if (motorRequest.stageTwo)
        {
            forwardSecondStage();
        }
        else
        {
            stopSecondStage();
        }
    }

    // Determine what event based on proximity sensors and if shoot command is given
    private Events.event determineEvent(boolean shoot)
    {
        // Measure the current state of the proximity sensors
        currentIntakeCanBe = measureIntakeSensor();
        currentStageOneFull = measureFirstStageSensor();
        currentStageTwoFull = measureSecondStageSensor();

        // Initially say there is no event then continue to look for an event
        event = Shuttle.Events.event.NONE;

        if(currentIntakeCanBe != previousIntakeCanBe)
        {
            if (currentIntakeCanBe)
            {
                event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                event = Shuttle.Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            previousIntakeCanBe = currentIntakeCanBe;
        }
        else if(currentStageOneFull != previousStageOneFull)
        {
            if (currentStageOneFull)
            {
                event = Shuttle.Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                // STAGE_ONE_FULL_SENSOR_DEACTIVATES
            }
        }
        else if(currentStageTwoFull != previousStageTwoFull)
        {
            if (currentStageTwoFull)
            {
                event = Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                event = Shuttle.Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            previousStageTwoFull = currentStageTwoFull;
        }
        else if(shoot)
        {
            event = Shuttle.Events.event.SHOOT_IS_CALLED;
        }

        return event;
    }
}
