package frc.components;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.wpilibj.DriverStation;

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
    //NEO 550
    private final CANSparkMax firstStageClimbMotorLeader;// = new CANSparkMax(/*Port.Motor.CLIMBER_STAGE_ONE_LEADER*/3, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private CANSparkMax firstStageClimbMotorFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_ONE_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private final CANSparkMax secondStageClimbMotorLeader;//  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_LEADER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private final CANSparkMax secondStageClimberFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    private int FCLPosition; //FCL >> FirstClimberLeader
    // private int FCFPosition; //FCF >> FirstClimberFollower
    // private int SCLPosition; //SCL >> SecondClimberLeader
    // private int SCFPosition; //SCF >> SecondClimberFollower

    private final RelativeEncoder FCLEncoder;// = firstStageClimbMotorLeader.getEncoder();
    private final SparkMaxLimitSwitch FCLForwardLimitSwitch;
    private final SparkMaxLimitSwitch FCLBackwardLimitSwitch;
    // private static RelativeEncoder FCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch FCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch FCFBackwardLimitSwitch;
    // private static RelativeEncoder SCLEncoder;// = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch SCLForwardLimitSwitch;
    // private static SparkMaxLimitSwitch SCLBackwardLimitSwitch;
    // private static RelativeEncoder SCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch SCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch SCFBackwardLimitSwitch;


    // *** CLASS CONSTRUCTOR ***
    public Climber(int firstStageClimbMotorPort, int secondStageClimbMotorPort)
    {
        firstStageClimbMotorPort = 1;   // Used ONLY for testing
        secondStageClimbMotorPort = 7;  // Used ONLY for testing

        firstStageClimbMotorLeader = new CANSparkMax(firstStageClimbMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

        FCLBackwardLimitSwitch = firstStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        FCLBackwardLimitSwitch.enableLimitSwitch(false);
        FCLForwardLimitSwitch = firstStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        FCLForwardLimitSwitch.enableLimitSwitch(false);
        //^^Unknown if being used for now^^

        FCLEncoder = firstStageClimbMotorLeader.getEncoder();
        FCLEncoder.setPosition(0);

        // secondStageClimbMotorLeader = new CANSparkMax(secondStageClimbMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        // SCLEncoder = firstStageClimbMotorLeader.getEncoder();

        // configFCF();
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
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 15.5844155844f); //TODO set a soft limit of however far the guy is legally allowed to move
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kForward, true);
    
        
        // FCLBackwardLimitSwitch = firstStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        // FCLBackwardLimitSwitch.enableLimitSwitch(false);
        // FCLForwardLimitSwitch = firstStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        // FCLForwardLimitSwitch.enableLimitSwitch(false);
        // //^^Unknown if being used for now^^
        // FCLEncoder.setPosition(0);
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
    public double getFCLposition(){
        return FCLEncoder.getPosition();
    }
    // public int getFCFPosition(){
    //     return FCFPosition;
    // }
    // public int getSCLposition(){
    //     return SCLPosition;
    // }
    // public int getSCFPosition(){
    //     return SCFPosition;
    // }

    //Setters
    public void setFCLPosition(int FCLPosition) 
    {
        this.FCLPosition = FCLPosition;
    }
    // public void setFCFPosition(int FCFPosition) 
    // {
    //     this.FCFPosition = FCFPosition;
    // }
    // public void setSCLPosition(int SCLPosition) 
    // {
    //     this.SCLPosition = SCLPosition;
    // }
    // public void setSCFPosition(int SCFPosition) 
    // {
    //     this.SCFPosition = SCFPosition;
    // }

    //Everything Else
    private void setFirstStageMotorSpeed(double speed)
    {
        firstStageClimbMotorLeader.set(speed);
    }

    public void shutDown()
    {
        setFirstStageMotorSpeed(0.0);
        DriverStation.reportError("Climber shut down!", false);
        //TODO add whatever motors start getting used to this
    }

    public void climbUp()
    {
        setFirstStageMotorSpeed(0.1);
        DriverStation.reportError("Climber going up", false);
        //TODO make sure this value goes the right direction
    }

    public void climbDown()
    {
        setFirstStageMotorSpeed(-0.1);
        DriverStation.reportError("Climber going down", false);
        //TODO make sure this value goes the right direction
    }

    public void grabFirstRung()
    {

    }
    
    public void grabSecondRung() 
    {
        // .. if neo 1650
        // //41.1in to 60.25in 24 in ball park travel
        // //motor has a diameter of 3.2in
        // //2 * pi * (diameter/2) = circumfrence of 10.0530964915in
        // //24 / ans = 2.38732414638 rotations
        // .. if neo 550
        // 41.1in to 60.25in 24 in ball park travel
        // //motor has a diameter of .49in
        // //2 * pi * (diameter/2) = circumfrence of 1.54in
        // //24 / ans = 15.5844155844 rotations
        // try
        // {
        //     System.out.println("Going up");
        //     // firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 2.38732414638f);
        //     // firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, true);
        //     // setMotorSpeed(firstStageClimbMotorLeader, 0.1);
        //     DriverStation.reportError("UP!", false);
        //     // setMotorSpeed(firstStageClimbMotorLeader, 0);
        //     TimeUnit.SECONDS.sleep(5);
        //     DriverStation.reportError("GO FORWARD", false);
        //     TimeUnit.SECONDS.sleep(5);
        //     // setMotorSpeed(firstStageClimbMotorLeader, -0.1);
        //     DriverStation.reportError("ROBOT UP!", false);
        //     // setMotorSpeed(firstStageClimbMotorLeader, 0);
        //     TimeUnit.SECONDS.sleep(5);
        // }
        // catch(InterruptedException ex)
        // {
        //     ex.printStackTrace();
        // }
    }

    public void grabThirdRung()
    {

    }

    public void grabFourthRung()
    {

    }
}
