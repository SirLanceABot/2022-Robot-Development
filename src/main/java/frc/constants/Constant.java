package frc.constants;

//TODO: Add constants
public class Constant 
{
    private static final String test = "tanu";
    private static final String laptop1 = "test";
    private static final String elliot = "test";
    
    //TODO: Configure shot rpm's
    public static final double LONG_SHOT_SPEED = 0.0;
    public static final double SHORT_SHOT_SPEED = 0.0;
    public static final double DROP_SHOT_SPEED = 0.0;

    //max amount of error allowed between desired flywheel speed and actual flywheel speed for it to shoot (in rpms)
    public static final double SHOOT_SPEED_THRESHOLD = 50;

    //max amount of error in degrees between desired and actual shroud angle
    public static final double SHROUD_ANGLE_THRESHOLD = 1;

    public static final double INTAKE_SPEED = 0.5;
}