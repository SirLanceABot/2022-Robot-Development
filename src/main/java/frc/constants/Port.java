package frc.constants;

import frc.drivetrain.SwerveModuleData;

//TODO: Get right values for each port
public final class Port 
{

    // TODO: Make up numbers for these so that they avoid conflicts
    // Maybe start with 30 and count up from there
    public class Motor
    {
        public static final int FRONT_LEFT_DRIVE = 30;
        public static final int FRONT_LEFT_TURN = 31;
        public static final int FRONT_RIGHT_DRIVE = 32;
        public static final int FRONT_RIGHT_TURN = 33;
        public static final int BACK_LEFT_DRIVE = 34;
        public static final int BACK_LEFT_TURN = 35;
        public static final int BACK_RIGHT_DRIVE = 36;
        public static final int BACK_RIGHT_TURN = 37;

        public static final int INTAKE_ROLLER = 38;
        public static final int INTAKE_ARMS_MOTOR = 39;

        public static final int SHUTTLE_STAGE_ONE = 40;
        public static final int SHUTTLE_STAGE_TWO = 41;

        public static final int CLIMBER_STAGE_ONE_LEADER = 42;
        public static final int CLIMBER_STAGE_TWO_LEADER = 43;
        public static final int CLIMBER_STAGE_ONE_FOLLOWER = 44;
        public static final int CLIMBER_STAGE_TWO_FOLLOWER = 45;

        public static final int SHOOTER_FLYWHEEL = 46;
        public static final int SHOOTER_SHROUD = 47;
    }

    public static class Module
    {
        public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData("Front Left", 7, true, 8, -167.255859375, 9);
        public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData("Front Right", 10, false, 11, -305.947265625, 12);
        public static final SwerveModuleData BACK_LEFT = new SwerveModuleData("Back Left", 4, true, 5, -348.75, 6);
        public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData("Back Right", 1, false, 2, -101.953125, 3);
    
    }

    public class Sensor
    {
        public static final int SHOOTER_SHROUD = 0;
        public static final int INTAKE_SENSOR = 0;
        public static final int FIRST_STAGE_SENSOR = 1;
        public static final int SECOND_STAGE_SENSOR = 2;
    }

    public class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }
}
