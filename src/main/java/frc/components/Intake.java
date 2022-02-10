package frc.components;
import frc.constants.Constant;
import frc.constants.Port;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
//import com.revrobotics.CANDigitalInput;
import com.revrobotics.SparkMaxLimitSwitch;
//import com.revrobotics.CANEncoder;
//import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.ControlType;
//import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
//import com.revrobotics.CANPIDController;
import com.revrobotics.SparkMaxPIDController;
//import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.RelativeEncoder;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

//test lol

public class Intake 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***
    public enum ArmPosition
    {
        kIn(Math.abs(intakeSpeed)), 
        kOut(Math.abs(intakeSpeed)*-1.0), 
        kOff(0.0); //is zero

        private final double position;

        private ArmPosition(final double position)
        {
            this.position = position;
        }

        public double position()
        {
            return position;
        }
    }

    public static enum RollerDirection
    {
        kIn(Math.abs(intakeSpeed)), //is basically posotive speed
        kOut(Math.abs(intakeSpeed)*-1.0), //is basically negative speed
        kOff(0.0); //is zero

        private final double position;

        private RollerDirection(final double position)
        {
            this.position = position;
        }

        public double position()
        {
            return position;
        }
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static CANSparkMax rollerMotor = new CANSparkMax(2, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // ^that fella is for when I'm testing with a boxbot

    // TODO: add the modifier final to the rollerMotor
    //private static final CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final CANSparkMax armsMotor = new CANSparkMax(/*elliot needs to add this port*/1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    /*private static CANEncoder armsEncoder = armsMotor.getEncoder();
    private static CANDigitalInput armsForwardLimitSwitch;
    private static CANDigitalInput armsReverseLimitSwitch;*/
    private static RelativeEncoder armsEncoder = armsMotor.getEncoder();
    private static SparkMaxLimitSwitch armsForwardLimitSwitch;
    private static SparkMaxLimitSwitch armsBackwardLimitSwitch;


    // TODO: make the following static
    private double armSolenoiod;
    private double armSensor;
    private double armUpSensor;
    private double armDownSensor;
    private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double intakeSpeed = /*Constant.INTAKE_SPEED*/.1;


    // *** CLASS CONSTRUCTOR ***
    public Intake()
    {
        System.out.println("Intake Created");
        configMotor(rollerMotor);
        configMotor(armsMotor);
    }

    // *** CLASS & INSTANCE METHODS ***
    //getters
    public ArmPosition getArmPosition()
    {
        return this.armPosition;
    }

    //create a configRollerMotor() method to configure the roller motor
    //It has become configMotor() because there are two motors that need configuring
    //what this does is set the motors to basically their factory settings in case said mortors had something different done to them at some point.
    public static void configMotor(CANSparkMax motor)
    {
        System.out.println("configurating " + motor);

        motor.restoreFactoryDefaults();
        motor.setInverted(true);
        motor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        motor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        motor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        motor.setSoftLimit(SoftLimitDirection.kForward, 0);
        motor.enableSoftLimit(SoftLimitDirection.kForward, false);

        if(motor == armsMotor) //to my knowledge this is the only motor that'll need an encoder
        {
            /*armsReverseLimitSwitch = Motor.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
            armsReverseLimitSwitch.enableLimitSwitch(false);
            armsForwardLimitSwitch = Motor.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen);
            armsForwardLimitSwitch.enableLimitSwitch(false);
            armsEncoder.setPosition(0);*/
            armsBackwardLimitSwitch = motor.getReverseLimitSwitch(Type.kNormallyOpen);
            armsBackwardLimitSwitch.enableLimitSwitch(false);
            armsForwardLimitSwitch = motor.getReverseLimitSwitch(Type.kNormallyOpen);
            armsForwardLimitSwitch.enableLimitSwitch(false);
            armsEncoder.setPosition(0);
        }

        motor.setOpenLoopRampRate(0.1);
        motor.setSmartCurrentLimit(40);
        

        System.out.println(motor + "Configurated");
    }


    public RollerDirection getRollerDirection()
    {
        return this.rollerDirection;
    }

    //setters
    // TODO This looks like a setRollerDirection() method, should it be renamed
    // The setRollerSpeed() method should call the set() method of the rollerMotor
    // we dont currenly need a setRollerSpeed() becuase IntakeSpeed is a constant
    private void setDirection(CANSparkMax motor, RollerDirection direction)
    {
        motor.set(direction.position); //".set" sets the speed, it has to be between 1.0 and -1.0
    }

    //not getters and setters?
    //7:1 gearbox
    public void outtakeRoller()
    {
        setDirection(rollerMotor, RollerDirection.kOut);
        System.out.println("Roller out");
    }
    
    public void intakeRoller()
    {
        setDirection(rollerMotor, RollerDirection.kIn);
        System.out.println("Roller in");
    }

    public void turnOffRoller()
    {
        setDirection(rollerMotor, RollerDirection.kOff);
        System.out.println("Roller Off");
    }

    public void moveArmOut() //FELLA MOVES 8 INCHES
    //10:1 gear ratio, pulley system 40:24 gear teeth, total gear ratio is 50:3
    //The motor has a diameter of .49in and a circumference of 1.54in
    //to move 8in it needs to spin 5.19480519481 times
    {
        //armsEncoder.setPositionConversionFactor();
        System.out.println("Moving arms out...");
        setDirection(armsMotor, RollerDirection.kIn);
        //double thing = 0;
        while(armsEncoder.getPosition() < 10.0/*5.19480519481*50/3*/) //Both getPostion and 5.19480519481 SHOULD be in the unit of rotations //50/3 is the gear ratio
        {
            //thing = armsEncoder.getPosition();
            System.out.println(armsEncoder.getPosition());
            System.out.println("moving out");
        }
        setDirection(armsMotor, RollerDirection.kOff);
        System.out.println("Arms out!");
        armPosition = ArmPosition.kOut;
        System.out.println("Final position: " + armsEncoder.getPosition());
    }

    public void moveArmIn() //FELLA MOVES 8 INCHES
    {
        System.out.println("Moving arms In...");
        setDirection(armsMotor, RollerDirection.kIn);
        if(armsEncoder.getPosition() > 0 ) //Both getPostion and 0 SHOULD be in the unit of rotations
        {
            
            System.out.println(armsEncoder.getPosition());
            System.out.println("moving in");
        }
        setDirection(armsMotor, RollerDirection.kOff);
        System.out.println("Arms in!");
        armPosition = ArmPosition.kIn;
    } 

    public void updateArmPosition(ArmPosition armPosition)
    {
        //don't know how worthwile this is when I can just armPosition = ArmPosition.kIn;
    }

    public double MeasureMotorSpeed(CANSparkMax motor)
    {
        return(motor.get()); 
    }

    public String toString()
    {
        String thisthing = "";
        thisthing += String.format("%-20s%-20s%-20s%", "armSolenoid", "armPosition\n");
        thisthing += String.format("%-20s%-20s%-20s%", this.armSolenoiod, this.armPosition+"\n");
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%-20s%",  "ArmSensor", "ArmDownSensor", "ArmUpSensor\n");
        thisthing += String.format("%-20s%-20s%-20s%", this.armSensor, this.armUpSensor, this.armDownSensor+"\n");
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%-20s%", "RollerDirection", "DesiredRollerSpeed", "RollerSpeed\n");
        thisthing += String.format("%-20s%-20s%-20s%", this.rollerDirection, this.desiredRollerSpeed, this.rollerSpeed+"\n");
        return thisthing;
    }

    public void TestRoller() //I just wanted to test the motors bro
    {
        //you can find the motor in the red bins in the robotics room and then you need a robot in a box and a laptop
        try
        {
            System.out.println("Starting");
            TimeUnit.SECONDS.sleep(2);   
            outtakeRoller();
            TimeUnit.SECONDS.sleep(2);
            turnOffRoller();
            TimeUnit.SECONDS.sleep(2);
            intakeRoller();
            TimeUnit.SECONDS.sleep(2);
            turnOffRoller();
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
    }

    public void TestArms()
    {
        System.out.println("Starting");
        moveArmOut();
        moveArmIn();
        System.out.println("Complete");
    }
}
