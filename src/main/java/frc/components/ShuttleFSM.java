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
                motorRequests.stageOne = false;
                motorRequests.stageTwo = false;

                cargoCount = 0;
            }
        },
        
        STORING_CARGO_IN_STAGE_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequests.stageOne = true;
                motorRequests.stageTwo = true;

                cargoCount = 1;
            }
        },

        CARGO_STORED_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequests.stageOne = false;
                motorRequests.stageTwo = false;

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
                motorRequests.stageOne = false;
                motorRequests.stageTwo = true;

                cargoCount = 2;
            }
        },
        
        SHOOTING_CARGO_FROM_STAGE_TWO
        {
            void doAction()
            {
                // Run stage two and turn off stage one
                motorRequests.stageOne = false;
                motorRequests.stageTwo = true;

                cargoCount = 1;
            }
        },

        STORING_CARGO_IN_STAGE_ONE
        {
            void doAction()
            {
                // Run stage one and turn off stage two
                motorRequests.stageOne = true;
                motorRequests.stageTwo = false;

                cargoCount = 2;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                motorRequests.stageOne = false;
                motorRequests.stageTwo = false;

                cargoCount = 2;
            }
        },

        SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Run stage one and two
                motorRequests.stageOne = true;
                motorRequests.stageTwo = true;

                cargoCount = 2;
            }
        },

        // TODO: Remove this state and replace with STORING_CARGO_IN_STAGE_TWO once tested
        STORING_CARGO_IN_STAGE_TWO_FROM_ONE
        {
            void doAction()
            {
                // Run stage one and two
                motorRequests.stageOne = true;
                motorRequests.stageTwo = true;

                cargoCount = 1;
            }
        };

        // method each state must have for its own because the code is different in this example
        abstract void doAction();
        
        // methods each state can use in common because the code is the same in this example
        void doEnter()
        {
            // System.out.println("entering state " + this.name());
            
            // Set flag for feedCargo to false as have used event if needed
            // feedCargo = false;
        }

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
        TRANSITION_3  (State.CARGO_STORED_IN_STAGE_TWO,             Events.ShuttleEvent.FEED_CARGO,                                        State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_4  (State.SHOOTING_CARGO_FROM_STAGE_TWO,         Events.ShuttleEvent.STAGE_TWO_FULL_SENSOR_DEACTIVATES,                 State.NO_CARGO_STORED),
        TRANSITION_5  (State.CARGO_STORED_IN_STAGE_TWO,             Events.ShuttleEvent.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,     State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_6  (State.STORING_CARGO_IN_STAGE_ONE,            Events.ShuttleEvent.STAGE_ONE_FULL_SENSOR_ACTIVATES,                   State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
        TRANSITION_7  (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO,     Events.ShuttleEvent.FEED_CARGO,                                        State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO),
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
         * Table lookup to determine new state given the curret state and event
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
    // Sensors are used to determine initial state
    private static final SensorValues CURRENT_SENSOR_VALUES = RobotContainer.CURRENT_SENSOR_VALUES;
    // Use events to run state machine
    private static final EventGenerator EVENT_GENERATOR = RobotContainer.EVENT_GENERATOR;
    private static final Shuttle SHUTTLE = RobotContainer.SHUTTLE;

    // Current state
    private State currentShuttleState;

    private static final MotorStage motorRequests = new MotorStage();

    private static int cargoCount = 0;
    // Tracks inserting cargo into the flywheel, related to FEED_CARGO event
    private static boolean feedCargo = false;

    // *** CLASS CONSTRUCTOR ***
    public ShuttleFSM()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // Initialize motor commands to stop
        motorRequests.stageOne = false;
        motorRequests.stageTwo = false;

        // Intital state of the FSM
        // currentShuttleState = State.NO_CARGO_STORED;
        currentShuttleState = measureCurrentState();
        System.out.println("Initial shuttle state: " + currentShuttleState);
        // Make a motor request that will be processed once enabled
        currentShuttleState.doAction();
        
        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    /**
     * Transition to a new currentState if the event triggers it
     * @param event
     */
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
            currentShuttleState.doEnter(); // initiate new state
            currentShuttleState.doAction();

            System.out.println("State: " + currentShuttleState);
        }
        // move above doAction to below to always run it
        // currentShuttleState.doAction(); // always maintain current state or the new state as determined above
    }

    /**
     * Getter for the Shuttle current state
     * @return the current shuttle state
     */
    public State getCurrentState()
    {
        return currentShuttleState;
    }

    /**
     * Measures sensors and sets the currentShuttleState
     */
    public void measureAndSetCurrentState()
    {
        currentShuttleState = measureCurrentState();
        System.out.println("New measured shuttle state: " + currentShuttleState);
        // Make a motor request that will be processed
        currentShuttleState.doAction();
    }

    public State measureCurrentState()
    {
        boolean stageOneSensorValue = SHUTTLE.measureStageOneSensor();
        boolean stageTwoSensorValue = SHUTTLE.measureStageTwoSensor();

        State measuredState = State.NO_CARGO_STORED;

        // Measure sensor values to determine initial state
        if (!stageOneSensorValue && !stageTwoSensorValue)
        {
            measuredState = State.NO_CARGO_STORED;
        }
        else if (stageOneSensorValue && !stageTwoSensorValue)
        {
            // Cargo is stored in stage one, but we want it to be stored in stage two so just say we're doing that
            measuredState = State.STORING_CARGO_IN_STAGE_TWO;
        }
        else if (!stageOneSensorValue && stageTwoSensorValue)
        {
            measuredState = State.CARGO_STORED_IN_STAGE_TWO;
        }
        else if (stageOneSensorValue && stageTwoSensorValue)
        {
            measuredState = State.CARGO_STORED_IN_STAGE_ONE_AND_TWO;
        }

        return measuredState;
    }

    public int getCargoCount()
    {
        return cargoCount;
    }

    /**
     * Sets a flag for an event to generate, set to false by ShuttleFSM
     */
    public void requestFeedCargo()
    {
        feedCargo = true;
        // Debugging feedCargo
        // System.out.println("It has been fed and is now " + feedCargo);
    }

    public boolean isFeedCargoRequested()
    {
        // Debugging feedCargo
        // System.out.println("isFeedCargoRequested and returns " + feedCargo);
        return feedCargo;
    }
    
    public void fancyRun(boolean shoot)
    {
        // TODO: Put into several run methods in teleop

        // TODO: Only call once
        // Remove this call to outer layer
        EVENT_GENERATOR.determineEvents(shoot);

        Events.ShuttleEvent determinedShuttleEvent = EVENT_GENERATOR.getShuttleEvent();

        // Prints out the event if there is one
        if (determinedShuttleEvent != Events.ShuttleEvent.NONE)
        {
            System.out.println("Event name: " + determinedShuttleEvent);
        }

        // Send event to FSM
        checkStateChange(determinedShuttleEvent);

        // FIXME Make sure that requests do not get whiped out because one of the doActions does not set one motor
        // TODO: Move this to an outer layer where will use flags to run motors, also need to make getMotorRequest()
        if (motorRequests.stageOne)
        {
            SHUTTLE.forwardStageOne();
        }
        else
        {
            SHUTTLE.stopStageOne();
        }

        if (motorRequests.stageTwo)
        {
            SHUTTLE.forwardStageTwo();
        }
        else
        {
            SHUTTLE.stopStageTwo();
        }
    }

    /**
     * Run the FSM
     */
    /*
    // TODO: Break this stuff out of fancyrun for this
    public void run()
    {
        // Get ShuttleEvent
        Events.ShuttleEvent determinedShuttleEvent = EVENT_GENERATOR.getShuttleEvent();
        
        // Prints out the event if there is one
        if (determinedShuttleEvent != Events.ShuttleEvent.NONE)
        {
            System.out.println("Event name: " + determinedShuttleEvent);
        }

        // Send event to FSM
        checkStateChange(determinedShuttleEvent);
    }

    /**
     * Runs the Shuttle based on FSM
     */
    /*
    public void runMotorRequests()
    {
        if (SHUTTLE != null)
        {
            if (motorRequests.stageOne)
            {
                SHUTTLE.forwardStageOne();
            }
            else
            {
                SHUTTLE.stopStageOne();
            }

            if (motorRequests.stageTwo)
            {
                SHUTTLE.forwardStageTwo();
            }
            else
            {
                SHUTTLE.stopStageTwo();
            }
        }
    }
    */
}
