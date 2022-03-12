package frc.robot;

import java.lang.invoke.MethodHandles;

import frc.commands.AutonomousCommandList;
import frc.drivetrain.Drivetrain;
import frc.robot.Robot.RobotState;
import frc.shuffleboard.AutonomousTabData;
import frc.shuffleboard.MainShuffleboard;

public class DisabledMode implements ModeTransition
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final MainShuffleboard MAIN_SHUFFLEBOARD = RobotContainer.MAIN_SHUFFLEBOARD;
    private static final AutonomousTabData AUTONOMOUS_TAB_DATA = RobotContainer.AUTONOMOUS_TAB_DATA;
    private static final AutonomousCommandList AUTONOMOUS_COMMAND_LIST = RobotContainer.AUTONOMOUS_COMMAND_LIST;
    private static final Drivetrain DRIVETRAIN = RobotContainer.DRIVETRAIN;
    
    private RobotState robotState;

    // *** CLASS CONSTRUCTOR ***
    public DisabledMode()
    {
        System.out.println(fullClassName + " : Constructor Started");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        robotState = Robot.getRobotState();

        if (DRIVETRAIN != null)
        {
            DRIVETRAIN.resetEncoders();
        }
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        if (MAIN_SHUFFLEBOARD != null)
        {
            boolean isNewData = MAIN_SHUFFLEBOARD.wasSendDataButtonPressed();

            if (isNewData && AUTONOMOUS_TAB_DATA != null && AUTONOMOUS_COMMAND_LIST != null)
            {
                AUTONOMOUS_TAB_DATA.updateData(MAIN_SHUFFLEBOARD.getAutonomousTabData());
                System.out.println(AUTONOMOUS_TAB_DATA);

                AUTONOMOUS_COMMAND_LIST.build();
                System.out.println(AUTONOMOUS_COMMAND_LIST);

                if (DRIVETRAIN != null)
                {
                    switch (AUTONOMOUS_TAB_DATA.startingLocation)
                    {
                        case kLeft:
                            DRIVETRAIN.setGyro(180.0 - 45.7); //set
                            break;
                        case kMiddleLeft:
                            DRIVETRAIN.setGyro(180.0 + 1.5); //set
                            break;
                        case kMiddleRight:
                            DRIVETRAIN.setGyro(180.0 + 23.97); //set
                            break;
                        case kRight:
                            DRIVETRAIN.setGyro(180.0 + 91.47); //set
                            break;
                    }
                }
            }
        }
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        if(robotState == RobotState.kDisabledAfterRobotInit && MAIN_SHUFFLEBOARD != null)
        {
            MAIN_SHUFFLEBOARD.setDriverControllerSettings();
            MAIN_SHUFFLEBOARD.setOperatorControllerSettings();
        }
    }
}
