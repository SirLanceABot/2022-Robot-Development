package frc.constants;

//TODO: Add constants
public final class Constant 
{
    //TODO: Configure shot rpm's
    public static final double LONG_SHOT_SPEED = 0.0;
    public static final double SHORT_SHOT_SPEED = 0.0;
    public static final double DROP_SHOT_SPEED = 0.0;

    //max amount of error allowed between desired flywheel speed and actual flywheel speed for it to shoot (in rpms)
    public static final double SHOOT_SPEED_THRESHOLD = 50;

    //max amount of error in degrees between desired and actual shroud angle
    public static final double SHROUD_ANGLE_THRESHOLD = 1;

    public static final double INTAKE_SPEED = 0.5;




    public static final double INCHES_TO_METERS = 0.0254;

    // FIXME Check that this is y coord and the other is x coord
    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     * Measured in inches, y-coordinate
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 23.5 * INCHES_TO_METERS;
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     * Measured in inches, x-coordinate
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 23.5 * INCHES_TO_METERS;

    public static final double WHEEL_RADIUS_METERS = 2.0 * INCHES_TO_METERS;

    public static final int DRIVETRAIN_NAVX_ID = 0; // FIXEDME Set Pigeon ID

    // Drivetrain constants
    // FIXME Check the max speeds and accelerations
    public static final double MAX_DRIVE_SPEED = 4.4; // meters per second
    public static final double MAX_MODULE_TURN_SPEED = 1080.0; // degrees per second, this is 3.0 rev/sec, used to be 1980 and 5.5 rev/sec
    public static final double MAX_ROBOT_TURN_SPEED = 360.0; // FIXME to more accurate degrees per second, this is 2 rev/sec, used to be 1080 and 3 rev/sec
    public static final double MAX_MODULE_TURN_ACCELERATION = 1728.0; // degrees per second per second, this is 4.8 rev/sec^2, used to be 17280 and 48 rev/sec^2
    // FIXME Changing radians to degrees, same measurements but in radians
    // public static final double MAX_MODULE_TURN_SPEED = 35.0; // radians per second
    // public static final double MAX_ROBOT_TURN_SPEED = 18.0; // FIXME radians per second
    // public static final double MAX_MODULE_TURN_ACCELERATION = 300.0; // radians per second per second

    public static final int DRIVE_MOTOR_ENCODER_RESOLUTION = 2048;
    public static final int TURN_MOTOR_ENCODER_RESOLUTION = 4096;
    public static final double DRIVE_MOTOR_GEAR_RATIO = 8.14;
    public static final double TURN_MOTOR_GEAR_RATIO = 12.8;

    public static final double DRIVE_ENCODER_RATE_TO_METERS_PER_SEC = 
        ((10.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
    // FIXME Changing drivePID, converting to something
    public static final double DRIVE_ENCODER_RATE_IN_MOTOR_TICKS_PER_100MS = 
        ((1.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);

    public static final double TURN_ENCODER_RATE_TO_RADIANS_PER_SEC = 
        (1.0 / TURN_MOTOR_ENCODER_RESOLUTION) * (2.0 * Math.PI);

    public static final double MAX_BATTERY_VOLTAGE = 12.0;
}