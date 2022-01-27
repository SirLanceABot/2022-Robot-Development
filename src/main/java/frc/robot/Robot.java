// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading " + fullClassName);
    }    
    
    // *** CLASS & INSTANCE VARIABLES ***


    /**
     * This keeps track of the current state of the robot, from startup to auto, to teleop, etc.
     */
    public enum RobotState
    {
        kNone,
        kStartup,
        kDisabledBeforeGame,
        kAutonomous,
        kDisabledBetweenAutonomousAndTeleop,
        kTeleop,
        kDisabledAfterGame,
        kTest;
    }

    
    private static Test test = new Test();
    private static Autonomous autonomous = new Autonomous();
    private static Disabled disabled = new Disabled();
    private static Teleop teleop = new Teleop();

    private static RobotState robotState = RobotState.kNone;


    public Robot()
    {
        robotState = RobotState.kStartup;
    }

    /**
     * This method is run when the robot is first started up and should be used for initialization code.
     */
    @Override
    public void robotInit()
    {
        System.out.println("\n\n2022-Robot-Development\n\n");
    }

    /**
     * This method is called periodically.
     */
    @Override
    public void robotPeriodic()
    {

    }

    /**
     * This method is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit()
    {
        robotState = RobotState.kAutonomous;

        autonomous.init();
    }

    /**
     * This method is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic()
    {
        autonomous.periodic();
    }

    /**
     * This method is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit()
    {
        robotState = RobotState.kTeleop;

        teleop.init();
    }

    /**
     * This method is called periodically during teleoperated mode.
     */
    @Override
    public void teleopPeriodic()
    {
        teleop.periodic();
    }

    /**
     * This method is called once each time the robot enters test mode.
     */
    @Override
    public void testInit()
    {
        robotState = RobotState.kTest;

        test.init();
    }

    /**
     * This method is called periodically during test mode.
     */
    @Override
    public void testPeriodic()
    {
        test.periodic();
    }

    /**
     * This method is called once each time the robot is disabled.
     */
    @Override
    public void disabledInit()
    {
        if (robotState == RobotState.kStartup)
            robotState = RobotState.kDisabledBeforeGame;
        else if (robotState == RobotState.kAutonomous)
        {
            robotState = RobotState.kDisabledBetweenAutonomousAndTeleop;
            autonomous.end();
        }
        else if (robotState == RobotState.kTeleop)
        {
            robotState = RobotState.kDisabledAfterGame;
            teleop.end();
        }
        else if (robotState == RobotState.kTest)
        {
            robotState = RobotState.kDisabledBeforeGame;
            test.end();
        }

        disabled.init();
    }

    /**
     * This method is called periodically when the robot is disabled.
     */
    @Override
    public void disabledPeriodic()
    {
        disabled.periodic();
    }

    /**
     * This method returns the current state of the robot
     * @return the robot state
     * @see RobotState
     */
    public static RobotState getRobotState()
    {
        return robotState;
    }
}