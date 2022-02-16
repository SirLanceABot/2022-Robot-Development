package frc.constants;

 //TODO: Get right values for each port
public class Port 
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

        public static final int SHUTTLE_STAGE_ONE = 39;
        public static final int SHUTTLE_STAGE_TWO = 40;

        public static final int CLIMBER_STAGE_ONE = 41;
        public static final int CLIMBER_STAGE_TWO = 42;

        public static final int SHOOTER_FLYWHEEL = 43;
        public static final int SHOOTER_SHROUD = 44;
    }

    public class Sensor
    {
        public static final int SHOOTER_SHROUD = 3;
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
