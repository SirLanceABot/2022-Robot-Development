// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.shuffleboard.MainShuffleboard;
import frc.vision.Vision;

public class Robot extends TimedRobot
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
        RobotContainer.constructMeFirst();
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
    private static final Vision VISION = RobotContainer.VISION;
    private static final PowerDistribution PDH = RobotContainer.PDH;
    private static final MainShuffleboard MAIN_SHUFFLEBOARD = RobotContainer.MAIN_SHUFFLEBOARD;

    private static final DisabledMode disabled = new DisabledMode();
    private static final TestMode test = new TestMode();
    private static final AutonomousMode autonomous = new AutonomousMode();
    private static final TeleopMode teleop = new TeleopMode();

    private static RobotState robotState = RobotState.kNone;


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
        
        PDH.setSwitchableChannel(false);
        
        if(VISION != null)
        {
            addPeriodic(() -> VISION.getCalibration(), 2.0, 0.0180); // get camera-target/shooter calibration from shuffleboard every 2 seconds
            addPeriodic(() -> System.gc(), 5.0, 0.0182); // needed for all the OpenCV Mats
        }

        // update everything on the Camera Tab on ShuffleBoard every 1 second
        //  Send to LimeLight how to display cameras' images
        //  Remaining match time
        if(MAIN_SHUFFLEBOARD != null)
        {
            addPeriodic(() -> MAIN_SHUFFLEBOARD.updateCameraTab(), 1.0, 0.0180);
        }

        SmartDashboard.putNumber("RPM", 0.0);
        SmartDashboard.putNumber("Shroud", -235);
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
