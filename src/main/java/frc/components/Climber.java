package frc.components;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;

import frc.constants.Port;

public class Climber 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    //FIXME For the st. joe match, we're only gonna go up to the second bar
    //TODO Set up motors to canspark max

    // *** CLASS & INSTANCE VARIABLES ***
    private static final CANSparkMax firstStageClimbMotorLeader = new CANSparkMax(Port.Motor.CLIMBER_STAGE_ONE_LEADER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);;
    // public climberMotor firstStageClimbMotorFollower;
    // public climberMotor secondStageClimberLeader;
    // public climberMotor secondStageClimberFollower;

    private int FCLPosition;
    private int FCFPosition;
    private int SCLPosition;
    private int SCFPosition;

    // *** CLASS CONSTRUCTOR ***
    public Climber()
    {
        configFCF();
        configFCL();
        configSCL();
        configSCF();
        System.out.println("climber constructed");
    }

    // *** CLASS & INSTANCE METHODS ***
    //Configs
    public void configFCL() //FCL >> FirstClimberLeader
    {
        //TODO set a soft limit of however far the guy is legally allowed to move
    }

    public void configFCF() //FCF >> FirstClimberFollower
    {
        //TODO set a soft limit of however far the guy is legally allowed to move
    }

    public void configSCL() //SCL >> SecondClimberLeader
    {
        //TODO set a soft limit of however far the guy is legally allowed to move
    }

    public void configSCF() //SCF >> SecondClimberFollower
    {
        //TODO set a soft limit of however far the guy is legally allowed to move
    }

    //Getters
    public int getFCFPosition() {
        return FCFPosition;
    }
    public int getFCLposition(){
        return FCLPosition;
    }
    public int getSCFPosition() {
        return SCFPosition;
    }
    public int getSCLposition(){
        return SCLPosition;
    }
    //Setters
    private void setFCFPosition(int FCFPosition) {
        this.FCFPosition = FCFPosition;
    }
    private void setFCLPosition(int FCLPosition) {
        this.FCLPosition = FCLPosition;
    }
    private void setSCFPosition(int SCFPosition) {
        this.SCFPosition = SCFPosition;
    }
    private void setSCLPosition(int SCLPosition) {
        this.SCLPosition = SCLPosition;
    }
    //Everything Else
    public void setMotorSpeed(double speed)
    {
        //TODO change this according to chosen motor
        // motor.speed = speed;
    }
    public void grabFirstRung()
    {
        //move arm to yada yada position and pull arms up
    }
    public void grabSecondRung()
    {
        //move arm to yada yada position and pull arms up
    }

    public void grabThirdRung()
    {
        //move arm to yada yada position and pull arms up
    }
    public void grabFourthRung()
    {
        //move arm to yada yada position and pull arms up
    }
}
