/*Relevant Doucumentation:
* https://robotpy.readthedocs.io/projects/rev/en/stable/rev/CANSparkMax.html
*
*/
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

    // *** CLASS & INSTANCE VARIABLES ***
    private enum MovementType
    {
        kOff, kMoving, kClimbing, kReverse, kNone
    }

    private MovementType FCLMovementType;
    private MovementType SCLMovementType;
    //Motors
    //Talon 
    // private final TalonSRX climbBrakeMotor;
    //NEO 550
    private final CANSparkMax firstStageClimbMotorLeader;// = new CANSparkMax(/*Port.Motor.CLIMBER_STAGE_ONE_LEADER*/3, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private CANSparkMax firstStageClimbMotorFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_ONE_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax secondStageClimbMotorLeader;// = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_LEADER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private final CANSparkMax secondStageClimberFollower  = new CANSparkMax(Port.Motor.CLIMBER_STAGE_TWO_FOLLOWER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    private int FCLPosition; //FCL >> FirstClimberLeader
    // private int FCFPosition; //FCF >> FirstClimberFollower
    private int SCLPosition; //SCL >> SecondClimberLeader
    // private int SCFPosition; //SCF >> SecondClimberFollower

    private final RelativeEncoder FCLEncoder;// = firstStageClimbMotorLeader.getEncoder();
    private SparkMaxLimitSwitch FCLForwardLimitSwitch;
    private SparkMaxLimitSwitch FCLReverseLimitSwitch;
    // private static RelativeEncoder FCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch FCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch FCFBackwardLimitSwitch;
    private static RelativeEncoder SCLEncoder;// = firstStageClimbMotorLeader.getEncoder();
    private static SparkMaxLimitSwitch SCLForwardLimitSwitch;
    private static SparkMaxLimitSwitch SCLReverseLimitSwitch;
    // private static RelativeEncoder SCFEncoder = firstStageClimbMotorLeader.getEncoder();
    // private static SparkMaxLimitSwitch SCFForwardLimitSwitch;
    // private static SparkMaxLimitSwitch SCFBackwardLimitSwitch;
    private boolean AutoDone = false;


    // *** CLASS CONSTRUCTOR ***
    public Climber(int firstStageClimbMotorPort, int secondStageClimbMotorPort)
    {
        FCLMovementType = MovementType.kNone;
        SCLMovementType = MovementType.kNone;

        firstStageClimbMotorLeader = new CANSparkMax(firstStageClimbMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        secondStageClimbMotorLeader = new CANSparkMax(secondStageClimbMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        // climbBrakeMotor = new TalonSRX(climbBrakeMotorPort);

        // climbBrakeMotor.configReverseSoftLimitThreshold(0, 0);
        // climbBrakeMotor.configForwardSoftLimitThreshold(-9, 0);
        // climbBrakeMotor.configReverseSoftLimitEnable(true, 0);
        // climbBrakeMotor.configForwardSoftLimitEnable(true, 0);

        FCLEncoder = firstStageClimbMotorLeader.getEncoder();
        FCLEncoder.setPosition(0);
        SCLEncoder = secondStageClimbMotorLeader.getEncoder();
        SCLEncoder.setPosition(0);

        // configFCF();
        configFCL();
        configSCL();
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
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, false); //FIXME change to false after testing
        firstStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 240.0f); // DF Measured on 4/9/22
        firstStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kForward, false);

        // TODO: Figure out how to make this not take as many frames from CANbus
        // firstStageClimbMotorLeader.setControlFramePeriodMs(periodMs);
        // firstStageClimbMotorLeader.setPeriodicFramePeriod(frame, periodMs);
    
        FCLReverseLimitSwitch = firstStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        FCLReverseLimitSwitch.enableLimitSwitch(true);
        FCLForwardLimitSwitch = firstStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        FCLForwardLimitSwitch.enableLimitSwitch(true);
        
        // FCLEncoder.setPosition(0);
        firstStageClimbMotorLeader.setOpenLoopRampRate(0.1);
        firstStageClimbMotorLeader.setSmartCurrentLimit(40);
    }

    public void measureFCLLimits()
    {
        System.out.println("F = " + FCLForwardLimitSwitch.isPressed() + ", R = " + FCLReverseLimitSwitch.isPressed());
        System.out.println("Encoder value = " + FCLEncoder.getPosition());
    }

    public void resetFCLEncoder()
    {
        FCLEncoder.setPosition(0);
    }

    // public void configFCF()
    // {
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
    // }

    public void configSCL() 
    {
        //TODO set a soft limit of however far the guy is legally allowed to move
        secondStageClimbMotorLeader.restoreFactoryDefaults();
        secondStageClimbMotorLeader.setInverted(true);
        secondStageClimbMotorLeader.setIdleMode(IdleMode.kBrake); 
    
        secondStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kReverse, 0.0f); //TODO set a soft limit of however far the guy is legally allowed to move
        secondStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kReverse, false);
        secondStageClimbMotorLeader.setSoftLimit(SoftLimitDirection.kForward, 0.0f); //TODO set a soft limit of however far the guy is legally allowed to move
        secondStageClimbMotorLeader.enableSoftLimit(SoftLimitDirection.kForward, false);
    
        
        SCLReverseLimitSwitch = secondStageClimbMotorLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        SCLReverseLimitSwitch.enableLimitSwitch(false);
        SCLForwardLimitSwitch = secondStageClimbMotorLeader.getForwardLimitSwitch(Type.kNormallyOpen);
        SCLForwardLimitSwitch.enableLimitSwitch(false);
        //^^Unknown if being used for now^^
        SCLEncoder.setPosition(0);
        secondStageClimbMotorLeader.setOpenLoopRampRate(0.1);
        secondStageClimbMotorLeader.setSmartCurrentLimit(40);
    }

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
    // }

    //Getters
    public double getFCLposition(){
        return FCLEncoder.getPosition();
    }
    public MovementType getMovementType()
    {
        return FCLMovementType;
    }
    // public int getFCFPosition(){
    //     return FCFPosition;
    // }
    public int getSCLposition(){
        return SCLPosition;
    }
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
    public void setSCLPosition(int SCLPosition) 
    {
        this.SCLPosition = SCLPosition;
    }
    // public void setSCFPosition(int SCFPosition) 
    // {
    //     this.SCFPosition = SCFPosition;
    // }

    //Asking if any part of auto has been done to any degree not if the auto has been completed
    public void AutoDone()
    {
        this.AutoDone = true;
        this.FCLMovementType = MovementType.kOff;
        this.SCLMovementType = MovementType.kOff;
    }

    // //Everything Else
    // private MovementType findMovement()
    // {
    //     if(firstStageClimbMotorLeader.getOutputCurrent() >= 20)
    //     {
    //         return(MovementType.kClimbing);
    //     }
    //     else if(firstStageClimbMotorLeader.getOutputCurrent() < 20 || firstStageClimbMotorLeader.getOutputCurrent() > 0)
    //     {
    //         return(MovementType.kMoving);
    //     }
    //     else if(firstStageClimbMotorLeader.getOutputCurrent() == 0)
    //     {
    //         return(MovementType.kOff);
    //     }
    //     else
    //     {
    //         return(MovementType.kNone);
    //     }
       
    // }
    private void setFirstStageMotorSpeed(double speed)
    {
        firstStageClimbMotorLeader.set(speed);
    }
    private void setSecondStageMotorSpeed(double speed)
    {
        secondStageClimbMotorLeader.set(speed);
    }
    // private void setBrakeMotor(double speed)
    // {
    //     climbBrakeMotor.set(ControlMode.PercentOutput, speed);
    // }
    public void setMoveOff()
    {
        FCLMovementType = MovementType.kOff;
    }
    public void FCLShutDown()
    {
        setFirstStageMotorSpeed(0.0);
        
        // switch(FCLMovementType)
        // {
        // case kOff:
        //     /*
        //        It should only come into teleop with kOff after auto.
        //        It checks the position of the arms and if its greater than 20 
        //        that means the climber is floating
        //        so it starts to move the arms down.
        //     */
        //     if(FCLEncoder.getPosition() >= 20.0)
        //     {
        //         setFirstStageMotorSpeed(-.20);
        //         FCLMovementType = MovementType.kReverse;
        //     }
        //     break;
        // case kReverse:
        //     /*
        //        You dont't need to set speed here cause this only 
        //        checks to see if you need to go back to kOff after you've had your speed set
        //        See above
        //     */
        //     if(FCLEncoder.getPosition() < 1.0 || FCLReverseLimitSwitch.isPressed())
        //     {
        //         setFirstStageMotorSpeed(0.0);
        //         if(AutoDone == true)
        //             FCLMovementType = MovementType.kOff;   
        //         else
        //             FCLMovementType = MovementType.kNone;
        //     }
        //     break;
        // case kClimbing:
        //     /*
        //         You can only come into shutdown() with kClimbing if the amperge of the motor
        //         is greater that a certain amount after calling armDown()
        //         It sets the motor to a velocity so that it cancels out the robot falling if not latched
        //     */
        //     setFirstStageMotorSpeed(-0.20);
        //     //Do not change the movement type 
        //     break;
        // case kMoving:
        //     /*
        //         Gets set when any move arm method is called and the amperage is low
        //         Since the robot should be climbing of the drivers accord it doesn't
        //         put the motor in any reverse velocity
        //         Moves the robot to kOff because you're not pressing a button after moving.
        //     */
        //     setFirstStageMotorSpeed(0.0);
        //     if(AutoDone == true)
        //         FCLMovementType = MovementType.kOff;
        //     else
        //         FCLMovementType = MovementType.kNone;
        //     break;
        // case kNone:
        //     /*
        //         Should only come into this if you start in teleop
        //         This acts as a defualt shutdown, it does nothing special, just shuts down
        //     */
        //     setFirstStageMotorSpeed(0.0);
        //     break;
        // }
        // System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        // if(FCLEncoder.getPosition() >= 20.0 && movementType == MovementType.kOff)
        // {
        //     setFirstStageMotorSpeed(-.20);
        //     movementType = MovementType.kReverse;
        // }
        // else if(movementType == MovementType.kReverse)
        // {
        //     setFirstStageMotorSpeed(-.20);
        //     if(FCLEncoder.getPosition() < 1.0 || FCLBackwardLimitSwitch.isPressed())
        //     {
        //         movementType = MovementType.kOff;
        //     }
        // }
        // else if(movementType == MovementType.kClimbing)
        // {
        //     setFirstStageMotorSpeed(-0.20);
        // }
        // else
        // {
        //     setFirstStageMotorSpeed(0.0);
        //     movementType = MovementType.kOff;
        // }
    }
    
    public void SCLShutDown()
    {
        setSecondStageMotorSpeed(0.0);
        //For Type context see FCLShutDown()
        // System.out.println("SCL: " + SCLMovementType + "FCL: " + FCLMovementType);
        // switch(SCLMovementType)
        // {
        // case kOff:
        //     if(SCLEncoder.getPosition() >= 20.0)
        //     {
        //         setSecondStageMotorSpeed(-.20);
        //         SCLMovementType = MovementType.kReverse;
        //     }
        //     break;
        // case kReverse:
        //     if(SCLEncoder.getPosition() < 1.0 || SCLReverseLimitSwitch.isPressed())
        //     {
        //         setSecondStageMotorSpeed(0.0);
        //         if(AutoDone == true)
        //             SCLMovementType = MovementType.kOff;
        //         else
        //             SCLMovementType = MovementType.kNone;
        //     }
        //     break;
        // case kClimbing:
        //     setSecondStageMotorSpeed(-0.20);
        //     //Do not change the movement type 
        //     break;
        // case kMoving:
        //     setSecondStageMotorSpeed(0.0);
        //     if(AutoDone == true)
        //         SCLMovementType = MovementType.kOff;
        //     else
        //         SCLMovementType = MovementType.kNone;
        //     break;
        // case kNone:
        //     setSecondStageMotorSpeed(0.0);
        //     break;
        // }
    }

    public void FCLArmUp()
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
        //     setBrakeMotor(-.1); 
        //     setFirstStageMotorSpeed(0);
        // }
        // movementType = findMovement();

        // if(FCLMovementType != MovementType.kClimbing)
        // {
        //     FCLMovementType = MovementType.kMoving;
        // }

        setFirstStageMotorSpeed(Constant.CLIMBER_UP_SPEED);
        // System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        
    }

    public void FCLArmDown()
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

        // double current = firstStageClimbMotorLeader.getOutputCurrent();
        // if(current > 15.0)
        //     FCLMovementType = MovementType.kClimbing;
        // else
        //     FCLMovementType = MovementType.kMoving;

        setFirstStageMotorSpeed(-Constant.CLIMBER_DOWN_SPEED);
        // System.out.println("AMP: " + firstStageClimbMotorLeader.getOutputCurrent() + " MODE: " + movementType);
        //^Test value
        // DriverStation.reportError("Climber going down", false);
    }

    // Darren's idea
    // public void FCLStop()
    // {
    //     setFirstStageMotorSpeed(0.0);
    // }

    // public void FCLHold()
    // {
    //     setFirstStageMotorSpeed(-Constant.CLIMBER_HOLD_SPEED);
    // }

    public void SCLArmUp()
    {
        // if(SCLMovementType != MovementType.kClimbing)
        // {
        //     SCLMovementType = MovementType.kMoving;
        // }
        setSecondStageMotorSpeed(Constant.CLIMBER_UP_SPEED);
    }

    public void SCLArmDown()
    {
        // double current = secondStageClimbMotorLeader.getOutputCurrent();
        // if(current > 20.0)
        //     SCLMovementType = MovementType.kClimbing;
        // else
        //     SCLMovementType =  MovementType.kMoving;
        
        setSecondStageMotorSpeed(-Constant.CLIMBER_DOWN_SPEED);
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
