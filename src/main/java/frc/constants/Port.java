package frc.constants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.SerialPort;
import frc.drivetrain.DrivetrainData;
import frc.drivetrain.SwerveModuleData;

//TODO: Get right values for each port
public final class Port 
{
    public static class Motor
    {
        public static final int FRONT_LEFT_DRIVE = 7;
        public static final int FRONT_LEFT_TURN = 9;

        public static final int FRONT_RIGHT_DRIVE = 10;
        public static final int FRONT_RIGHT_TURN = 12;

        public static final int BACK_LEFT_DRIVE = 4;
        public static final int BACK_LEFT_TURN = 6;

        public static final int BACK_RIGHT_DRIVE = 1;
        public static final int BACK_RIGHT_TURN = 3;

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

    private static class Module
    {
        // public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData("Front Left", 7, true, 8, -167.255859375, 9);
        // public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData("Front Right", 10, false, 11, -305.947265625, 12);
        // public static final SwerveModuleData BACK_LEFT = new SwerveModuleData("Back Left", 4, true, 5, -348.75, 6);
        // public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData("Back Right", 1, false, 2, -101.953125, 3);

        private static final Translation2d FRONT_LEFT_LOCATION = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d FRONT_RIGHT_LOCATION = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_LEFT_LOCATION = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_RIGHT_LOCATION = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);

        public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData(
            "Front Left", FRONT_LEFT_LOCATION, Motor.FRONT_LEFT_DRIVE, true, Sensor.FRONT_LEFT_ENCODER, -167.255859375, Motor.FRONT_LEFT_TURN);
        public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData(
            "Front Right", FRONT_RIGHT_LOCATION, Motor.FRONT_RIGHT_DRIVE, false, Sensor.FRONT_RIGHT_ENCODER, -305.947265625, Motor.FRONT_RIGHT_TURN);
        public static final SwerveModuleData BACK_LEFT = new SwerveModuleData(
            "Back Left", BACK_LEFT_LOCATION, Motor.BACK_LEFT_DRIVE, true, Sensor.BACK_LEFT_ENCODER, -348.75, Motor.BACK_LEFT_TURN);
        public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData(
            "Back Right", BACK_RIGHT_LOCATION, Motor.BACK_RIGHT_DRIVE, false, Sensor.BACK_RIGHT_ENCODER, -101.953125, Motor.BACK_RIGHT_TURN);
    }

    public static class Drivetrain
    {
        public static final DrivetrainData DRIVETRAIN_DATA = new DrivetrainData(
            Module.FRONT_LEFT, Module.FRONT_RIGHT, Module.BACK_LEFT, Module.BACK_RIGHT, SerialPort.Port.kUSB);
    }

    public static class Sensor
    {
        public static final int FRONT_LEFT_ENCODER = 8;
        public static final int FRONT_RIGHT_ENCODER = 11;
        public static final int BACK_LEFT_ENCODER = 5;
        public static final int BACK_RIGHT_ENCODER = 2;

        public static final int SHOOTER_SHROUD = 0;
        public static final int INTAKE_SENSOR = 0;
        public static final int FIRST_STAGE_SENSOR = 1;
        public static final int SECOND_STAGE_SENSOR = 2;
    }

    public static class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }
}
