package frc.constants;

 //TODO: Get right values for each port
public class Port 
{
    public class Motor
    {
        public static final int FRONT_LEFT_DRIVE = 0;
        public static final int FRONT_LEFT_TURN = 0;
        public static final int FRONT_RIGHT_DRIVE = 0;
        public static final int FRONT_RIGHT_TURN = 0;
        public static final int BACK_LEFT_DRIVE = 0;
        public static final int BACK_LEFT_TURN = 0;
        public static final int BACK_RIGHT_DRIVE = 0;
        public static final int BACK_RIGHT_TURN = 0;

        public static final int INTAKE_ROLLER = 0;

        public static final int SHUTTLE_STAGE_ONE = 1;
        public static final int SHUTTLE_STAGE_TWO = 5;

        public static final int CLIMBER_STAGE_ONE = 0;
        public static final int CLIMBER_STAGE_TWO = 0;

        public static final int SHOOTER_FLYWHEEL = 0;
        public static final int SHOOTER_SHROUD = 0;
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
        public static final int OPERATOR = 0;
    }
}
