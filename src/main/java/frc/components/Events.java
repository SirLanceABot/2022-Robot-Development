package frc.components;

public class Events
{
    public static enum CargoManagerEvent
    {
        NONE,
        SHUTTLE_EMPTY,
        SHOT_ONE_OF_TWO,
        SHUTTLE_FULL,
        HUB_IS_CENTERED_AND_SHOOTER_READY,
        SHOOTER_READY,
        HUB_IS_CENTERED,
        SHOOT_IS_CALLED,
        ARM_TOGGLE,
        ROLLER_TOGGLE;

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
        FEED_CARGO;
    }
}
