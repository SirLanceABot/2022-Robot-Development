package frc.components;

import java.lang.invoke.MethodHandles;

import frc.robot.RobotContainer;

public class CargoManager 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***


    /**
     * List of allowed CargoManager states, each state should have a doAction
     */
    // TODO: Rename this to cargoManagerState and add to a state class perhaps?
    public enum State
    {
        NOTHING
        {
            void doAction()
            {
                // Stop flywheel, put down shroud, turn off LED
                //TODO: Add more states and call methods
            }
        },
        
        INTAKE_DOWN
        {
            void doAction()
            {
                // Intake out, stop roller
                
            }
        },
        
        INTAKING
        {
            void doAction()
            {
                // Run roller
                
            }
        },

        INTAKE_UP
        {
            void doAction()
            {
                // Intake in, stop roller
                
            }
        },

        COMMENCING_FIRING
        {
            void doAction()
            {
                // Turn on LED, center hub, spin up flywheel, position shroud
                
            }
        },

        CENTERING_HUB
        {
            void doAction()
            {
                // Center hub
                
            }
        },

        PREPARING_SHOOTER
        {
            void doAction()
            {
                // Turn off LED, spin up flywheel, position shroud
                
            }
        },

        SHOOTING
        {
            void doAction()
            {
                // Set feedCargo flag to true, turn off LED
                SHUTTLEFSM.feedCargo();
            }
        };

        // method each state must have for its own because the code is different in this example
        abstract void doAction();
        
        // methods each state can use in common because the code is the same in this example
        // void doEnter()
        // {
        //     System.out.println("entering state " + this.name());
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
        TRANSITION_1  (State.NOTHING,	            Events.CargoManagerEvent.ARM_TOGGLE,	                        State.INTAKE_DOWN),
        TRANSITION_2  (State.INTAKE_DOWN,	        Events.CargoManagerEvent.ROLLER_TOGGLE,	                        State.INTAKING),
        TRANSITION_3  (State.INTAKING,	            Events.CargoManagerEvent.ROLLER_TOGGLE,	                        State.INTAKE_DOWN),
        TRANSITION_4  (State.INTAKING,	            Events.CargoManagerEvent.ARM_TOGGLE,	                        State.INTAKE_UP),
        TRANSITION_5  (State.INTAKE_DOWN,	        Events.CargoManagerEvent.ARM_TOGGLE,	                        State.INTAKE_UP),
        TRANSITION_6  (State.INTAKE_UP,	            Events.CargoManagerEvent.ARM_TOGGLE,	                        State.INTAKE_DOWN),
        TRANSITION_7  (State.INTAKING,	            Events.CargoManagerEvent.SHUTTLE_FULL,	                        State.INTAKE_UP),
        TRANSITION_8  (State.INTAKE_DOWN,	        Events.CargoManagerEvent.SHUTTLE_FULL,	                        State.INTAKE_UP),
        TRANSITION_9  (State.INTAKING,	            Events.CargoManagerEvent.SHOOT_IS_CALLED,	                    State.COMMENCING_FIRING),
        TRANSITION_10 (State.INTAKE_UP,	            Events.CargoManagerEvent.SHOOT_IS_CALLED,	                    State.COMMENCING_FIRING),
        TRANSITION_11 (State.COMMENCING_FIRING,	    Events.CargoManagerEvent.SHOOTER_READY,	                        State.CENTERING_HUB),
        TRANSITION_12 (State.CENTERING_HUB,	        Events.CargoManagerEvent.HUB_IS_CENTERED,	                    State.SHOOTING),
        TRANSITION_13 (State.COMMENCING_FIRING,	    Events.CargoManagerEvent.HUB_IS_CENTERED_AND_SHOOTER_READY,	    State.SHOOTING),
        TRANSITION_14 (State.COMMENCING_FIRING,	    Events.CargoManagerEvent.HUB_IS_CENTERED,	                    State.PREPARING_SHOOTER),
        TRANSITION_15 (State.PREPARING_SHOOTER,	    Events.CargoManagerEvent.SHOOTER_READY,	                        State.SHOOTING),
        TRANSITION_16 (State.SHOOTING,	            Events.CargoManagerEvent.SHOT_ONE_OF_TWO,	                    State.PREPARING_SHOOTER),
        TRANSITION_17 (State.SHOOTING,	            Events.CargoManagerEvent.SHUTTLE_EMPTY,	                        State.NOTHING);
        
        private final State currentState;
        private final Events.CargoManagerEvent event;
        private final State nextState;

        Transition(State currentState, Events.CargoManagerEvent event, State nextState)
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
        public static State findNextState(State currentState, Events.CargoManagerEvent event)
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
    // Use events to run state machine
    private static final EventGenerator EVENT_GENERATOR = RobotContainer.EVENT_GENERATOR;
    private static final ShuttleFSM SHUTTLEFSM = RobotContainer.SHUTTLEFSM;

    // Current state
    private State currentCargoManagerState;

    // *** CLASS CONSTRUCTOR ***
    public CargoManager()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // Intital state of the FSM
        currentCargoManagerState = State.NOTHING;

        // TODO: Decide if should have this
        // Make a motor request that will be processed once enabled
        // currentCargoManagerState.doAction();
        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***

    /**
     * Transition to a new currentState if the event triggers it
     * @param event
     */
    public void checkStateChange(Events.CargoManagerEvent event)
    {
        // make the transition to a new currentState if an event triggered it

        State newCargoManagerState = Transition.findNextState(currentCargoManagerState, event); // the next state

        // has the state changed by the event?
        // There is a choice here depending on the system.
        // If the state didn't change by this event, you could still go through the doExit
        // and doEnter because of the event, if that's what makes sense for your FSM.
        // This code doesn't redo the doExit, doEnter, and doAction but there is a comment below
        // to move the doAction if you always want to execute it even if no event or state change.
        if (newCargoManagerState != currentCargoManagerState)
        {
            // change states
            // currentCargoManagerState.doExit(); // exit current state
            currentCargoManagerState = newCargoManagerState; // switch states
            // currentCargoManagerState.doEnter(); // initiate new state
            // currentCargoManagerState.doAction();

            System.out.println("State: " + currentCargoManagerState);
        }
        // move above doAction to below to always run it
        currentCargoManagerState.doAction(); // always maintain current state or the new state as determined above
    }

    /**
     * Getter for the CargoManager current state
     * @return the current cargo manager state
     */
    public State getCurrentState()
    {
        return currentCargoManagerState;
    }

    public void run()
    {
        // Get CargoManagerEvent
        Events.CargoManagerEvent determinedCargoManagerEvent = EVENT_GENERATOR.getCargoManagerEvent();
        
        // Prints out the event if there is one
        if (determinedCargoManagerEvent != Events.CargoManagerEvent.NONE)
        {
            System.out.println("Event name: " + determinedCargoManagerEvent);
        }

        // Send event to FSM
        checkStateChange(determinedCargoManagerEvent);
    }
}
