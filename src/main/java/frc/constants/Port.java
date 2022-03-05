package frc.constants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.SerialPort;
import frc.components.ShuttleConfig;
import frc.drivetrain.DrivetrainConfig;
import frc.drivetrain.SwerveModuleConfig;

//TODO: Get right values for each port
public final class Port 
{
    public static class Motor
    {
        private static final int FRONT_LEFT_DRIVE   =  7;  // MM 2/28/22
        private static final int FRONT_LEFT_TURN    =  9;  // MM 2/28/22

        private static final int FRONT_RIGHT_DRIVE  = 10;  // MM 2/28/22
        private static final int FRONT_RIGHT_TURN   = 12;  // MM 2/28/22

        private static final int BACK_LEFT_DRIVE    =  4;  // MM 2/28/22
        private static final int BACK_LEFT_TURN     =  6;  // MM 2/28/22

        private static final int BACK_RIGHT_DRIVE   =  1;  // MM 2/28/22
        private static final int BACK_RIGHT_TURN    =  3;  // MM 2/28/22

        public static final int INTAKE_ROLLER       = 38;
        public static final int INTAKE_ARMS_MOTOR   =  5;  // MM 2/26/22

        private static final int SHUTTLE_STAGE_ONE  =  4;  // MM 2/26/22
        private static final int SHUTTLE_STAGE_TWO  = 10;  // MM 2/26/22

        public static final int SHOOTER_FLYWHEEL    = 13;  // JW 2/28/22
        public static final int SHOOTER_SHROUD      = 14;  // JW 2/28/22
        
        public static final int CLIMBER_STAGE_ONE_LEADER    = 1;  // MM 3/02/22
        public static final int CLIMBER_STAGE_TWO_LEADER    = 43;
        public static final int CLIMBER_STAGE_ONE_FOLLOWER  = 44;
        public static final int CLIMBER_STAGE_TWO_FOLLOWER  = 45;
        public static final int CLIMBER_BRAKE_MOTOR         = 15; // DF 3/05/22
    }

    public static class Sensor
    {
        private static final int FRONT_LEFT_ENCODER         =  8;
        private static final int FRONT_RIGHT_ENCODER        = 11;
        private static final int BACK_LEFT_ENCODER          =  5;
        private static final int BACK_RIGHT_ENCODER         =  2;

        public static final int SHOOTER_SHROUD              =  0;

        private static final int SHUTTLE_INTAKE_SENSOR      =  0;
        private static final int SHUTTLE_STAGE_ONE_SENSOR   =  1;
        private static final int SHUTTLE_STAGE_TWO_SENSOR   =  2;

        public static final int PDH_CAN_ID                  =  1;

        private static final SerialPort.Port NAV_X = SerialPort.Port.kUSB;
    }

    public static class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }

    private static class SwerveModuleSetup
    {
        // public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData("Front Left", 7, true, 8, -167.255859375, 9);
        // public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData("Front Right", 10, false, 11, -305.947265625, 12);
        // public static final SwerveModuleData BACK_LEFT = new SwerveModuleData("Back Left", 4, true, 5, -348.75, 6);
        // public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData("Back Right", 1, false, 2, -101.953125, 3);

        private static final double FRONT_LEFT_ENCODER_OFFSET   = -167.255859375;
        private static final double FRONT_RIGHT_ENCODER_OFFSET  = -305.947265625;
        private static final double BACK_LEFT_ENCODER_OFFSET    = -348.75;
        private static final double BACK_RIGHT_ENCODER_OFFSET   = -101.953125;

        private static final Translation2d FRONT_LEFT_LOCATION = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d FRONT_RIGHT_LOCATION = new Translation2d(Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_LEFT_LOCATION = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_RIGHT_LOCATION = new Translation2d(-Constant.DRIVETRAIN_WHEELBASE_METERS / 2, -Constant.DRIVETRAIN_TRACKWIDTH_METERS / 2);

        private static final SwerveModuleConfig FRONT_LEFT = new SwerveModuleConfig(
            "Front Left", FRONT_LEFT_LOCATION, Motor.FRONT_LEFT_DRIVE, true, Sensor.FRONT_LEFT_ENCODER, FRONT_LEFT_ENCODER_OFFSET, Motor.FRONT_LEFT_TURN);
        private static final SwerveModuleConfig FRONT_RIGHT = new SwerveModuleConfig(
            "Front Right", FRONT_RIGHT_LOCATION, Motor.FRONT_RIGHT_DRIVE, false, Sensor.FRONT_RIGHT_ENCODER, FRONT_RIGHT_ENCODER_OFFSET, Motor.FRONT_RIGHT_TURN);
        private static final SwerveModuleConfig BACK_LEFT = new SwerveModuleConfig(
            "Back Left", BACK_LEFT_LOCATION, Motor.BACK_LEFT_DRIVE, true, Sensor.BACK_LEFT_ENCODER, BACK_LEFT_ENCODER_OFFSET, Motor.BACK_LEFT_TURN);
        private static final SwerveModuleConfig BACK_RIGHT = new SwerveModuleConfig(
            "Back Right", BACK_RIGHT_LOCATION, Motor.BACK_RIGHT_DRIVE, false, Sensor.BACK_RIGHT_ENCODER, BACK_RIGHT_ENCODER_OFFSET, Motor.BACK_RIGHT_TURN);
    }

    public static class DrivetrainSetup
    {
        public static final DrivetrainConfig DRIVETRAIN_DATA = new DrivetrainConfig(
            SwerveModuleSetup.FRONT_LEFT, SwerveModuleSetup.FRONT_RIGHT, SwerveModuleSetup.BACK_LEFT, SwerveModuleSetup.BACK_RIGHT, Sensor.NAV_X);
    }

    public static class ShuttleSetup
    {
        public static final ShuttleConfig SHUTTLE_DATA = new ShuttleConfig(
            Motor.SHUTTLE_STAGE_ONE, Motor.SHUTTLE_STAGE_TWO, Sensor.SHUTTLE_INTAKE_SENSOR, Sensor.SHUTTLE_STAGE_ONE_SENSOR, Sensor.SHUTTLE_STAGE_TWO_SENSOR);
    }

}
