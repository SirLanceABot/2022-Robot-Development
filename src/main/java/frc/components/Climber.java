package frc.components;

import java.lang.invoke.MethodHandles;
import com.revrobotics.CANSparkMax;
import frc.constants.Port;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;

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

    // *** CLASS & INSTANCE VARIABLES ***
    //Neo 1650
    public static final CANSparkMax firstStageClimbMotorLeader = new CANSparkMax(/*Port.Motor.CLIMBER_STAGE_ONE_LEADER*/3, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    //private static CANSparkMax firstStageClimbMotorFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_ONE_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private static final CANSparkMax secondStageClimberLeader  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_LEADER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private static final CANSparkMax secondStageClimberFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    private int FCLPosition; //FCL >> FirstClimberLeader
    private int FCFPosition; //FCF >> FirstClimberFollower
    private int SCLPosition; //SCL >> SecondClimberLeader
    private int SCFPosition; //SCF >> SecondClimberFollower

    String gamer;
    private static RelativeEncoder FCLEncoder = firstStageClimbMotorLeader.getEncoder();
    private static SparkMaxLimitSwitch FCLForwardLimitSwitch;
    private static SparkMaxLimitSwitch FCLBackwardLimitSwitch;
    // private static RelativeEncoder FCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch FCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch FCFBackwardLimitSwitch;
    // private static RelativeEncoder SCLEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch SCLForwardLimitSwitch;
    // private static SparkMaxLimitSwitch SCLBackwardLimitSwitch;
    // private static RelativeEncoder SCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch SCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch SCFBackwardLimitSwitch;


    // *** CLASS CONSTRUCTOR ***
    public Climber()
    {
        configFCF();
        configFCL();
        //configSCL();
        //configSCF();
        System.out.println("climber constructed");
    }

    // *** CLASS & INSTANCE METHODS ***
    //Configs
    public void configFCL() 
    {
        firstStageClimbMotorLeader.restoreFactoryDefaults();
        firstStageClimbMotorLeader.setInverted(true);
        firstStageClimbMotorLeader.setIdleMode(IdleMode.kBrake); 
    
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kReverse, 0); //TODO set a soft limit of however far the guy is legally allowed to move
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, true);
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 0); //TODO set a soft limit of however far the guy is legally allowed to move
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        
        FCLBackwardLimitSwitch = firstStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        FCLBackwardLimitSwitch.enableLimitSwitch(false);
        FCLForwardLimitSwitch = firstStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        FCLForwardLimitSwitch.enableLimitSwitch(false);
        //^^Unknown if being used for now^^
        FCLEncoder.setPosition(0);
        firstStageClimbMotorLeader.setOpenLoopRampRate(0.1);
        firstStageClimbMotorLeader.setSmartCurrentLimit(40);
    }

    public void configFCF()
    {
        // firstStageClimbMotorFollower = CANSparkMax.follow(firstStageClimbMotorLeader, false);
        // //^^ this isn't working, I'm just leaving it so the next person knows what to do with it
        // firstStageClimbMotorFollower.restoreFactoryDefaults();
        // firstStageClimbMotorFollower.setInverted(true);
        // firstStageClimbMotorFollower.setIdleMode(IdleMode.kBrake); 
    
        // firstStageClimbMotorFollower.setSoftLimit(SoftLimitDirection.kReverse, 0); //TODO set a soft limit of however far the guy is legally allowed to move
        // firstStageClimbMotorFollower.enableSoftLimit(SoftLimitDirection.kReverse, true);
        // firstStageClimbMotorFollower.setSoftLimit(SoftLimitDirection.kForward, 0); //TODO set a soft limit of however far the guy is legally allowed to move
        // firstStageClimbMotorFollower.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        
        // FCFBackwardLimitSwitch = firstStageClimbMotorFollower.getReverseLimitSwitch(Type.kNormallyOpen);
        // FCFBackwardLimitSwitch.enableLimitSwitch(false);
        // FCFForwardLimitSwitch = firstStageClimbMotorFollower.getForwardLimitSwitch(Type.kNormallyOpen);
        // FCFForwardLimitSwitch.enableLimitSwitch(false);
        // //^^Unknown if being used for now^^
        // FCFEncoder.setPosition(0);
        // firstStageClimbMotorFollower.setOpenLoopRampRate(0.1);
        // firstStageClimbMotorFollower.setSmartCurrentLimit(40);
    //}

    // public void configSCL() 
    // {
    //     //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberLeader.restoreFactoryDefaults();
    //     secondStageClimberLeader.setInverted(true);
    //     secondStageClimberLeader.setIdleMode(IdleMode.kBrake); 
    
    //     secondStageClimberLeader.setSoftLimit(SoftLimitDirection.kReverse, 0); //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberLeader.enableSoftLimit(SoftLimitDirection.kReverse, true);
    //     secondStageClimberLeader.setSoftLimit(SoftLimitDirection.kForward, 0); //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberLeader.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        
    //     SCLBackwardLimitSwitch = secondStageClimberLeader.getReverseLimitSwitch(Type.kNormallyOpen);
    //     SCLBackwardLimitSwitch.enableLimitSwitch(false);
    //     SCLForwardLimitSwitch = secondStageClimberLeader.getForwardLimitSwitch(Type.kNormallyOpen);
    //     SCLForwardLimitSwitch.enableLimitSwitch(false);
    //     //^^Unknown if being used for now^^
    //     SCLEncoder.setPosition(0);
    //     secondStageClimberLeader.setOpenLoopRampRate(0.1);
    //     secondStageClimberLeader.setSmartCurrentLimit(40);
    // }

    // public void configSCF() 
    // {
    //     //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberFollower.restoreFactoryDefaults();
    //     secondStageClimberFollower.setInverted(true);
    //     secondStageClimberFollower.setIdleMode(IdleMode.kBrake); 
    
    //     secondStageClimberFollower.setSoftLimit(SoftLimitDirection.kReverse, 0); //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberFollower.enableSoftLimit(SoftLimitDirection.kReverse, true);
    //     secondStageClimberFollower.setSoftLimit(SoftLimitDirection.kForward, 0); //TODO set a soft limit of however far the guy is legally allowed to move
    //     secondStageClimberFollower.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        
    //     SCFBackwardLimitSwitch = secondStageClimberFollower.getReverseLimitSwitch(Type.kNormallyOpen);
    //     SCFBackwardLimitSwitch.enableLimitSwitch(false);
    //     SCFForwardLimitSwitch = secondStageClimberFollower.getForwardLimitSwitch(Type.kNormallyOpen);
    //     SCFForwardLimitSwitch.enableLimitSwitch(false);
    //     //^^Unknown if being used for now^^
    //     SCFEncoder.setPosition(0);
    //     secondStageClimberFollower.setOpenLoopRampRate(0.1);
    //     secondStageClimberFollower.setSmartCurrentLimit(40);
    }

    //Getters
    public int getFCFPosition(){
        return FCFPosition;
    }
    public int getFCLposition(){
        return FCLPosition;
    }
    public int getSCFPosition(){
        return SCFPosition;
    }
    public int getSCLposition(){
        return SCLPosition;
    }
    public String getGamer(){
        return gamer;
    }
    //Setters
    public void setFCFPosition(int FCFPosition) 
    {
        this.FCFPosition = FCFPosition;
    }
    public void setFCLPosition(int FCLPosition) 
    {
        this.FCLPosition = FCLPosition;
    }
    public void setSCFPosition(int SCFPosition) 
    {
        this.SCFPosition = SCFPosition;
    }
    public void setSCLPosition(int SCLPosition) 
    {
        this.SCLPosition = SCLPosition;
    }
    public void setGamer(String gamer)
    {
        this.gamer = gamer;
    }
    //Everything Else
    public void setMotorSpeed(CANSparkMax motor, double speed)
    {
        motor.set(speed);
    }

    public void grabFirstRung()
    {
        //move arm to yada yada position and pull arms up
    }

    public void grabSecondRung() 
    {
        //41.1in to 60.25in 24 in ball park travel
        //motor has a diameter of 3.2
        //2 * pi * (diameter/2) = circumfrence of 10.0530964915in
        //24 / ans = 2.38732414638 rotations
        gamer = "Stage2";
        System.out.println("Going up");
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 2.38732414638f);
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, true);
        setMotorSpeed(firstStageClimbMotorLeader, 0.1);
        System.out.println("UP!");

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
