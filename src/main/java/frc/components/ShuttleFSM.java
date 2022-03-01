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

                cargoCount = 0;
            }
        },
        
        STORING_CARGO_IN_STAGE_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;

                cargoCount = 1;
            }
        },

        CARGO_STORED_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;

                cargoCount = 1;
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

                cargoCount = 2;
            }
        },

        STORING_CARGO_IN_STAGE_TWO_FROM_TWO
        {
            void doAction()
            {
                // Run stage one and two
                // motorRequest.stageOne = true;
                // motorRequest.stageTwo = true;

                cargoCount = 2;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and run stage two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = true;

                cargoCount = 2;
            }
        },
        
        SHOOTING_CARGO_FROM_STAGE_TWO
        {
            void doAction()
            {
                // Run stage two and turn off stage one
                motorRequest.stageOne = false;
                motorRequest.stageTwo = true;

                cargoCount = 1;
            }
        },

        STORING_CARGO_IN_STAGE_ONE
        {
            void doAction()
            {
                // Run stage one and turn off stage two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = false;

                cargoCount = 2;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequest.stageOne = false;
                motorRequest.stageTwo = false;

                cargoCount = 2;
            }
        },

        SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;

                cargoCount = 2;
            }
        },

        // TODO: Remove this state and replace with STORING_CARGO_IN_STAGE_TWO once tested
        STORING_CARGO_IN_STAGE_TWO_FROM_ONE
        {
            void doAction()
            {
                // Run stage one and two
                motorRequest.stageOne = true;
                motorRequest.stageTwo = true;

                cargoCount = 1;
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
        TRANSITION_1  (State.NO_CARGO_STORED,                       Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.STORING_CARGO_IN_STAGE_TWO),
        TRANSITION_2  (State.STORING_CARGO_IN_STAGE_TWO,            Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_TWO),
        TRANSITION_2A (State.STORING_CARGO_IN_STAGE_TWO,        	Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES, 	   State.STORING_CARGO_IN_STAGE_TWO_AND_ONE),
        TRANSITION_2B (State.STORING_CARGO_IN_STAGE_TWO_AND_ONE,    Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,	               State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_2C (State.STORING_CARGO_IN_STAGE_TWO_AND_ONE,	Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_DEACTIVATES,	               State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO),
        TRANSITION_2D (State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO,	Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,	               State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_2E (State.STORING_CARGO_IN_STAGE_TWO_FROM_TWO,	Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES,	               State.CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO),
        TRANSITION_2F (State.CARGO_STORED_IN_STAGE_ONE_AND_STORING_IN_STAGE_TWO,	Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,   State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
        TRANSITION_3  (State.CARGO_STORED_IN_STAGE_TWO,             Events.ShuttleEvent.SHOOT_IS_CALLED,                                   State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_4  (State.SHOOTING_CARGO_FROM_STAGE_TWO,         Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES,                 State.NO_CARGO_STORED),
        TRANSITION_5  (State.CARGO_STORED_IN_STAGE_TWO,             Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_6  (State.STORING_CARGO_IN_STAGE_ONE,            Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
        TRANSITION_7  (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO,     Events.ShuttleEvent.SHOOT_IS_CALLED,                                   State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO),
        TRANSITION_8  (State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO, Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES,                 State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE),
        TRANSITION_9  (State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE,   Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_TWO);

        private final State currentState;
        private final Events.ShuttleEvent event;
        private final State nextState;

        Transition(State currentState, Events.ShuttleEvent event, State nextState)
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
        public static State findNextState(State currentState, Events.ShuttleEvent event)
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

    // *** CLASS & INSTANCE VARIABLES ***
    private static final SensorValues CURRENT_SENSOR_VALUES = RobotContainer.CURRENT_SENSOR_VALUES;
    private static final EventGenerator EVENT_GENERATOR = RobotContainer.EVENT_GENERATOR;
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;

    // Current state
    private State currentShuttleState;

    private static final MotorStage motorRequest = new MotorStage();

    private static int cargoCount = 0;

    // TODO: Figure out if this is needed or should be the "determinedEvent"
    private Events.ShuttleEvent event;

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

    public void checkStateChange(Events.ShuttleEvent event)
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
        if (!CURRENT_SENSOR_VALUES.getShuttleStageOne() && !CURRENT_SENSOR_VALUES.getShuttleStageTwo())
        {
            measuredState = State.NO_CARGO_STORED;
        }
        else if (CURRENT_SENSOR_VALUES.getShuttleStageOne() && !CURRENT_SENSOR_VALUES.getShuttleStageTwo())
        {
            // Cargo is stored in stage one, but we want it to be stored in stage two so just say we're doing that
            measuredState = State.STORING_CARGO_IN_STAGE_TWO;
        }
        else if (!CURRENT_SENSOR_VALUES.getShuttleStageOne() && CURRENT_SENSOR_VALUES.getShuttleStageTwo())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_TWO;
        }
        else if (CURRENT_SENSOR_VALUES.getShuttleStageOne() && CURRENT_SENSOR_VALUES.getShuttleStageTwo())
        {
            measuredState = State.CARGO_STORED_IN_STAGE_ONE_AND_TWO;
        }

        return measuredState;
    }

    // public void fancyRun(Events.event event)
    public void fancyRun(boolean shoot)
    {
        EVENT_GENERATOR.determineEvents(shoot);

        Events.ShuttleEvent event = EVENT_GENERATOR.getShuttleEvent();
        
        // Prints out the event if there is one
        if (event != Events.ShuttleEvent.NONE)
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
}
