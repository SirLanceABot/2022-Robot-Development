package frc.components;

import java.lang.invoke.MethodHandles;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

import edu.wpi.first.wpilibj.DriverStation;
import frc.constants.Constant;

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
    private enum MovementType
    {
        kOff, kMoving, kClimbing, kNone
    }

    private MovementType movementType;
    //Motors
    //Talon 
    private final TalonSRX climbBrakeMotor;
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
    private SparkMaxLimitSwitch FCLForwardLimitSwitch;
    private SparkMaxLimitSwitch FCLBackwardLimitSwitch;
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
    public Climber(int firstStageClimbMotorPort, int secondStageClimbMotorPort, int climbBrakeMotorPort)
    {
        firstStageClimbMotorPort = 3;   // Used ONLY for testing
        // secondStageClimbMotorPort = 7;  // Used ONLY for testing
        climbBrakeMotorPort = 0; //Used ONLY for testing

        firstStageClimbMotorLeader = new CANSparkMax(firstStageClimbMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        climbBrakeMotor = new TalonSRX(climbBrakeMotorPort);

        // climbBrakeMotor.configReverseSoftLimitThreshold(0, 0);
        // climbBrakeMotor.configForwardSoftLimitThreshold(-9, 0);
        // climbBrakeMotor.configReverseSoftLimitEnable(true, 0);
        // climbBrakeMotor.configForwardSoftLimitEnable(true, 0);

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

        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kReverse, 0.0f); //TODO set a soft limit of where motor goes
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, false);
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 255.0f); // DF Measured on 3/5/22
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kForward, false);

        // TODO: Figure out how to make this not take as many frames from CANbus
        // firstStageClimbMotorLeader.setControlFramePeriodMs(periodMs);
        // firstStageClimbMotorLeader.setPeriodicFramePeriod(frame, periodMs);
    
        FCLBackwardLimitSwitch = firstStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        FCLBackwardLimitSwitch.enableLimitSwitch(true);
        FCLForwardLimitSwitch = firstStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        FCLForwardLimitSwitch.enableLimitSwitch(true);
        
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
    private MovementType findMovement()
    {
        if(firstStageClimbMotorLeader.getOutputCurrent() >= 20)
        {
            return(MovementType.kClimbing);
        }
        else if(firstStageClimbMotorLeader.getOutputCurrent() < 20 || firstStageClimbMotorLeader.getOutputCurrent() > 0)
        {
            return(MovementType.kMoving);
        }
        else if(firstStageClimbMotorLeader.getOutputCurrent() == 0)
        {
            return(MovementType.kOff);
        }
        else
        {
            return(MovementType.kNone);
        }
       
    }
    private void setFirstStageMotorSpeed(double speed)
    {
        firstStageClimbMotorLeader.set(speed);
    }
    private void setBrakeMotor(double speed)
    {
        climbBrakeMotor.set(ControlMode.PercentOutput, speed);
    }
    public void shutDown()
    {
        System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        if(movementType == MovementType.kMoving) //FIXME: This needs to be kClimbing when testing robot
        {
            setFirstStageMotorSpeed(.35);
        }
        else
        {
            setFirstStageMotorSpeed(0.0);
            //movementType = MovementType.kOff;
        }
        setBrakeMotor(0.0);
    }

    public void climbUp()
    {
        //button 10
        //arms go up
        // setFirstStageMotorSpeed(.25);
        //^robot value
        // if(climbBrakeMotor.isRevLimitSwitchClosed() == 1)
        // {
        //     setBrakeMotor(0);
        //     setFirstStageMotorSpeed(Constant.CLIMBER_UP_SPEED);
        //     // setFirstStageMotorSpeed(.1);
        // }
        // else
        // {
        //     setBrakeMotor(-.1); //TODO make sure this value goes the right direction
        //     setFirstStageMotorSpeed(0);
        // }
        movementType = findMovement();
        setFirstStageMotorSpeed(Constant.CLIMBER_UP_SPEED);
        System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        
    }

    public void climbDown()
    {
        //button 11
        // arms go down
        // setFirstStageMotorSpeed(-1);
        //^robot value
        // if(FCLBackwardLimitSwitch.isPressed() == true)
        // {
        //     // setBrakeMotor(.1);
        //     setFirstStageMotorSpeed(0);
        // }
        // else
        // {
        //     setBrakeMotor(0);
        //     setFirstStageMotorSpeed(-.5);
        //     // setFirstStageMotorSpeed(-Constant.CLIMBER_DOWN_SPEED);
        // }
        movementType = findMovement();
        setFirstStageMotorSpeed(-Constant.CLIMBER_DOWN_SPEED);
        System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        //^Test value
        // DriverStation.reportError("Climber going down", false);
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
        // .. soft soft limit
        // //70:1 gear ratio
        // //2 in diameter
        // //circumference of 6.283185307
        // //3.819718634 * 70.0 rotations
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
