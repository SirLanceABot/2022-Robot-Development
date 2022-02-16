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
        System.out.println("Loading: " + fullClassName);
        robotState = RobotState.kNone;
    }

    /**
     * This keeps track of the current state of the robot, from startup to auto, to teleop, etc.
     */
    public static enum RobotState
    {
        kNone,
        kRobotInit,
        kDisabledAfterRobotInit,
        kAutonomous,
        kDisabledAfterAutonomous,
        kTeleop,
        kDisabledAfterTeleop,
        kTest;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static final TestMode test;
    private static final AutonomousMode autonomous;
    private static final DisabledMode disabled;
    private static final TeleopMode teleop;

    private static RobotState robotState = RobotState.kNone;

    static
    {
        test = new TestMode();
        autonomous = new AutonomousMode();
        disabled = new DisabledMode();
        teleop = new TeleopMode();

        // test = null;
        // autonomous = null;
        // disabled = null;
        // teleop = null;
    }


    // *** CLASS CONSTRUCTOR ***
    public Robot()
    {

    }

    /**
     * This method runs when the robot first starts up.
     */
    @Override
    public void robotInit()
    {
        System.out.println("\n\n2022-Robot-Development\n\n");
        robotState = RobotState.kRobotInit;
    }

    /**
     * This method runs periodically (20ms) while the robot is powered on.
     */
    @Override
    public void robotPeriodic()
    {

    }

    /**
     * This method runs one time when the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit()
    {
        robotState = RobotState.kAutonomous;

        autonomous.init();
    }

    /**
     * This method runs periodically (20ms) during autonomous mode.
     */
    @Override
    public void autonomousPeriodic()
    {
        autonomous.periodic();
    }

    /**
     * This method runs one time when the robot exits autonomous mode.
     */
    @Override
    public void autonomousExit()
    {
        autonomous.exit();
    }

    /**
     * This method runs one time when the robot enters teleop mode.
     */
    @Override
    public void teleopInit()
    {
        robotState = RobotState.kTeleop;

        teleop.init();
    }

    /**
     * This method runs periodically (20ms) during teleop mode.
     */
    @Override
    public void teleopPeriodic()
    {
        teleop.periodic();
    }

    /**
     * This method runs one time when the robot exits teleop mode.
     */
    @Override
    public void teleopExit()
    {
        teleop.exit();
    }

    /**
     * This method runs one time when the robot enters test mode.
     */
    @Override
    public void testInit()
    {
        robotState = RobotState.kTest;

        test.init();
    }

    /**
     * This method runs periodically (20ms) during test mode.
     */
    @Override
    public void testPeriodic()
    {
        test.periodic();
    }

    /**
     * This method runs one time when the robot exits test mode.
     */
    @Override
    public void testExit()
    {
        test.exit();
    }

    /**
     * This method runs one time when the robot enters disabled mode.
     */
    @Override
    public void disabledInit()
    {
        if (robotState == RobotState.kRobotInit)
        {
            robotState = RobotState.kDisabledAfterRobotInit;
        }
        else if (robotState == RobotState.kAutonomous)
        {
            robotState = RobotState.kDisabledAfterAutonomous;
        }
        else if (robotState == RobotState.kTeleop)
        {
            robotState = RobotState.kDisabledAfterTeleop;
        }
        else if (robotState == RobotState.kTest)
        {
            robotState = RobotState.kDisabledAfterRobotInit;
        }

        disabled.init();
    }

    /**
     * This method runs periodically (20ms) during disabled mode.
     */
    @Override
    public void disabledPeriodic()
    {
        disabled.periodic();
    }

    /**
     * This method runs one time when the robot exits disabled mode.
     */
    @Override
    public void disabledExit()
    {
        disabled.exit();
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