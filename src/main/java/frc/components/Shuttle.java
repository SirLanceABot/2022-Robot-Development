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

    // What we want to happen with the motors
    // FIXME Make variables not static 
    private static boolean requestStageOneMotorRunning = false;
    private static boolean requestStageTwoMotorRunning = false;

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
                requestStageOneMotorRunning = false;
                requestStageTwoMotorRunning = false;
            }
        },

        
        STORING_CARGO_IN_STAGE_TWO
        {
            void doAction()
            {
                // Run stage one and two
                requestStageOneMotorRunning = true;
                requestStageTwoMotorRunning = true;
            }
        },

        CARGO_STORED_IN_STAGE_TWO
        {
            void doAction()
            {
                // Turn off stage one and two
                requestStageOneMotorRunning = false;
                requestStageTwoMotorRunning = false;
            }
        },
        
        SHOOTING_CARGO_FROM_STAGE_TWO
        {
            void doAction()
            {
                // Run stage two and turn off stage one
                requestStageOneMotorRunning = false;
                requestStageTwoMotorRunning = true;
            }
        },

        STORING_CARGO_IN_STAGE_ONE
        {
            void doAction()
            {
                // Run stage one
                requestStageOneMotorRunning = true;
                // requestStageTwoMotorRunning = false;
            }
        },

        CARGO_STORED_IN_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Turn off stage one
                requestStageOneMotorRunning = false;
                // requestStageTwoMotorRunning = false;
            }
        },

        SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO
        {
            void doAction()
            {
                // Run stage one and two
                requestStageOneMotorRunning = true;
                requestStageTwoMotorRunning = true;
            }
        },

        STORING_CARGO_IN_STAGE_TWO_FROM_ONE
        {
            void doAction()
            {
                // Run stage one and two
                requestStageOneMotorRunning = true;
                requestStageTwoMotorRunning = true;
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
    }

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
        TRANSITION_1 (State.NO_CARGO_STORED, Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES, State.STORING_CARGO_IN_STAGE_TWO),
        TRANSITION_2 (State.STORING_CARGO_IN_STAGE_TWO, Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES, State.CARGO_STORED_IN_STAGE_TWO),
        TRANSITION_3 (State.CARGO_STORED_IN_STAGE_TWO, Events.event.SHOOT_IS_CALLED, State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_4 (State.SHOOTING_CARGO_FROM_STAGE_TWO, Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES, State.NO_CARGO_STORED),
        TRANSITION_5 (State.CARGO_STORED_IN_STAGE_TWO, Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES, State.STORING_CARGO_IN_STAGE_ONE),
        TRANSITION_6 (State.STORING_CARGO_IN_STAGE_ONE, Events.event.STAGE_ONE_FULL_SENSOR_ACTIVATES, State.CARGO_STORED_IN_STAGE_ONE_AND_TWO),
        TRANSITION_7 (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO, Events.event.SHOOT_IS_CALLED, State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO),
        TRANSITION_8 (State.SHOOTING_CARGO_FROM_STAGE_ONE_AND_TWO, Events.event.STAGE_TWO_FULL_SENSOR_DEACTIVATES, State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE),
        TRANSITION_9 (State.STORING_CARGO_IN_STAGE_TWO_FROM_ONE, Events.event.STAGE_TWO_FULL_SENSOR_ACTIVATES, State.SHOOTING_CARGO_FROM_STAGE_TWO),
        TRANSITION_10 (State.CARGO_STORED_IN_STAGE_ONE_AND_TWO, Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES, State.EJECTING_EXTRA_CARGO),
        TRANSITION_11 (State.EJECTING_EXTRA_CARGO, Events.event.INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES, State.CARGO_STORED_IN_STAGE_ONE_AND_TWO);

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


    // *** CLASS CONSTRUCTOR ***
    // TODO: remove the public access modifier so that the constructor can only be accessed inside the package
    public Shuttle()
    {

    }


    // *** CLASS & INSTANCE METHODS ***

    // TODO: Create a configMotor() method to configure each motor
    

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
}
