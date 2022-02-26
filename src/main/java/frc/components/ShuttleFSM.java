package frc.components;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;

public class ShuttleFSM 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***
    // What we want to happen with the motors
    // FIXME Make variables not static
    public static class MotorStage
    {
        public boolean stageOne; 
        public boolean stageTwo;
    }

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
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;
            }
        },
        
        STORING_CARGO_IN_STAGE_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;
            }
        },

        CARGO_STORED_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;
            }
        },

        // TODO: Determine if these two should be commented in motor commands
        STORING_CARGO_IN_STAGE_TWO_AND_ONE
        {
            void doAction()
            {
                // Run stage one and two
                // motorRequest.stageOne = true;
                // motorRequest.stageTwo = true;
            }
        },

        STORING_CARGO_IN_STAGE_TWO_FROM_TWO
        {
            void doAction()
            {
                // Run stage one and two
                // motorRequest.stageOne = true;
                // motorRequest.stageTwo = true;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and run stage two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = true;
            }
        },
        
        SHOOTING_CARGO_FROM_STAGE_TWO
        {
            void doAction()
            {
                // Run stage two and turn off stage one
                motorRequest.stageOne = false;
                motorRequest.stageTwo = true;
            }
        },

        STORING_CARGO_IN_STAGE_ONE
        {
            void doAction()
            {
                // Run stage one and turn off stage two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = false;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;
            }
        },

        SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;
            }
        },

        STORING_CARGO_IN_STAGE_TWO_FROM_ONE
        {
            void doAction()
            {
                // Run stage one and two
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
        TRANSITION_2A (State.STORING_CARGO_IN_STAGE_TWO,        	Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES, 	State.STORING_CARGO_IN_STAGE_TWO_AND_ONE),
        TRANSITION_2B (State.STORING_CARGO_IN_STAGE_TWO_AND_ONE,    Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,	                State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_2C (State.STORING_CARGO_IN_STAGE_TWO_AND_ONE,	Events.event.STAGE_ONE_FULL_SENSOR_DEACTIVATES,	                State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO),
        TRANSITION_2D (State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO,	Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,	                State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_2E (State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO,	Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES,	                State.CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO),
        TRANSITION_2F (State.CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO,	Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES,	State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
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
        public static State findNextState(State currentState, Events.event event)
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
    // End of Transition enum

    // TODO: Move this to EventManager class
    public static class Events
    {
        public static enum event
        {
            NONE,
            INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
            INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES,
            STAGE_ONE_FULL_SENSOR_ACTIVATES,
            STAGE_ONE_FULL_SENSOR_DEACTIVATES,
            STAGE_TWO_FULL_SENSOR_ACTIVATES,
            STAGE_TWO_FULL_SENSOR_DEACTIVATES,
            SHOOT_IS_CALLED;
        }
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Current state
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;

    private State currentShuttleState;

    private static final MotorStage motorRequest = new MotorStage();

    // TODO: Figure out if this is needed or should be the "determinedEvent"
    private Events.event event;

    private boolean previousIntakeCanBe = false;
    private boolean previousStageOneFull = false;
    private boolean previousStageTwoFull = false;
    
    private boolean currentIntakeCanBe = false;
    private boolean currentStageOneFull = false;
    private boolean currentStageTwoFull = false;



    // *** CLASS CONSTRUCTOR ***
    public ShuttleFSM()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // Initialize motor commands to stop
        motorRequest.stageOne = false;
        motorRequest.stageTwo = false;

        // Intital state of the FSM
        // currentShuttleState = State.NO_CARGO_STORED;
        currentShuttleState = measureCurrentState();
        // Make a motor request that will be processed once enabled
        currentShuttleState.doAction();
        
        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    public void checkStateChange(Events.event event)
    {
        // make the transition to a new currentState if an event triggered it

        State newShuttleState = Transition.findNextState(currentShuttleState, event); // the next state

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

            System.out.println("State: " + currentShuttleState);
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

    public State measureCurrentState()
    {
        State measuredState = State.NO_CARGO_STORED;

        // Measure sensor values to determine initial state
        if (!SHUTTLE.measureStageOneSensor() && !SHUTTLE.measureStageTwoSensor())
        {
            measuredState = State.NO_CARGO_STORED;
        }
        else if (SHUTTLE.measureStageOneSensor() && !SHUTTLE.measureStageTwoSensor())
        {
            // Cargo is stored in stage one, but we want it to be stored in stage two so just say we're doing that
            measuredState = State.STORING_CARGO_IN_STAGE_TWO;
        }
        else if (!SHUTTLE.measureStageOneSensor() && SHUTTLE.measureStageTwoSensor())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_TWO;
        }
        else if (SHUTTLE.measureStageOneSensor() && SHUTTLE.measureStageTwoSensor())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_ONE_AND_TWO;
        }

        return measuredState;
    }

    // public void fancyRun(Events.event event)
    public void fancyRun(boolean shoot)
    {
        // TODO: Add measuring sensors to determine event, probablly in a different method
        // Could possibly then change measureState to use that as well
        Events.event event = determineEvent(shoot);
        
        // Prints out the event if there is one
        if (event != Events.event.NONE)
        {
            System.out.println("Event name: " + event);
        }

        // Send event to FSM
        checkStateChange(event);

        // FIXME Make sure that requests do not get whiped out because one of the doActions does not set one motor
        // TODO: Move this to an outer layer where will use flags to run motors, also need to make getMotorRequest()
        if (motorRequest.stageOne)
        {
            SHUTTLE.forwardStageOne();
        }
        else
        {
            SHUTTLE.stopStageOne();
        }

        if (motorRequest.stageTwo)
        {
            SHUTTLE.forwardStageTwo();
        }
        else
        {
            SHUTTLE.stopStageTwo();
        }
    }

    // TODO: Move this to EventManager class
    // Determine what event based on proximity sensors and if shoot command is given
    private Events.event determineEvent(boolean shoot)
    {
        // Measure the current state of the proximity sensors
        currentIntakeCanBe = SHUTTLE.measureIntakeSensor();
        currentStageOneFull = SHUTTLE.measureStageOneSensor();
        currentStageTwoFull = SHUTTLE.measureStageTwoSensor();

        // Initially say there is no event then continue to look for an event
        Events.event determinedEvent = Events.event.NONE;

        if(currentIntakeCanBe != previousIntakeCanBe)
        {
            if (currentIntakeCanBe)
            {
                determinedEvent = Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES;
            }

            previousIntakeCanBe = currentIntakeCanBe;
        }
        else if(currentStageOneFull != previousStageOneFull)
        {
            if (currentStageOneFull)
            {
                determinedEvent = Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.event.STAGE_ONE_FULL_SENSOR_DEACTIVATES;
            }

            previousStageOneFull = currentStageOneFull;
        }
        else if(currentStageTwoFull != previousStageTwoFull)
        {
            if (currentStageTwoFull)
            {
                determinedEvent = Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES;
            }
            else
            {
                determinedEvent = Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES;
            }

            previousStageTwoFull = currentStageTwoFull;
        }
        else if(shoot)
        {
            determinedEvent = Events.event.SHOOT_IS_CALLED;
        }

        return determinedEvent;
    }
}
