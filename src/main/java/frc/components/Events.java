package frc.components;

public class Events
{
    public static enum CargoManagerEvent
    {
        NONE,
        INTAKE_OUT,
        INTAKE_IN,
        RUN_ROLLER,
        STOP_ROLLER,
        SHOOT_IS_CALLED,
        SHUTTLE_EMPTY,
        SHOT_ONE_OF_TWO,
        SHUTTLE_FULL,
        FLYWHEEL_READY,
        HUB_IS_CENTERED,
        HUB_IS_CENTERED_AND_FLYWHEEL_READY;

    }

    public static enum ShuttleEvent
    {
        NONE,
        INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_ACTIVATES,
        INTAKE_CARGO_CAN_BE_SHUTTLED_SENSOR_DEACTIVATES,
        STAGE_ONE_FULL_SENSOR_ACTIVATES,
        STAGE_ONE_FULL_SENSOR_DEACTIVATES,
        STAGE_TWO_FULL_SENSOR_ACTIVATES,
        STAGE_TWO_FULL_SENSOR_DEACTIVATES,
        SHOOT_IS_CALLED;
    }
}
