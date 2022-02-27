package frc.components;

public class ShuttleData 
{
    public final int stageOneMotorPort;
    public final int stageTwoMotorPort;
    public final int intakeSensorPort;
    public final int stageOneSensorPort;
    public final int stageTwoSensorPort;

    public ShuttleData(int stageOneMotorPort,
                        int stageTwoMotorPort,
                        int intakeSensorPort,
                        int stageOneSensorPort,
                        int stageTwoSensorPort)
    {
        this.stageOneMotorPort = stageOneMotorPort;
        this.stageTwoMotorPort = stageTwoMotorPort;
        this.intakeSensorPort = intakeSensorPort;
        this.stageOneSensorPort = stageOneSensorPort;
        this.stageTwoSensorPort = stageTwoSensorPort;
    }
}
