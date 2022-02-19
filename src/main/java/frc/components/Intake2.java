package frc.components;

import frc.constants.Constant;
import frc.constants.Port;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;
// import com.revrobotics.SparkMaxRelativeEncoder;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

public class Intake2 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** INNER ENUMS and INNER CLASSES ***
    // public enum ArmPosition
    // {
    //     kIn,
    //     kOut,
    //     kOff
    // }

    // public enum ArmDirection
    // {
    //     kIn(1), 
    //     kOut(-1.0), 
    //     kOff(0.0); //is zero

    //     // private final double direction;
    //     public final double direction;

    //     private ArmDirection(final double direction)
    //     {
    //         this.direction = direction;
    //     }

    //     // public double direction()
    //     // {
    //     //     return direction;
    //     // }
    // }

    public static enum RollerDirection
    {
        kIn(-INTAKE_SPEED), //is basically posotive speed
        kOut(INTAKE_SPEED), //is basically negative speed
        kOff(0.0); //is zero
        //in is negative

        // private final double position;
        public final double position;

        private RollerDirection(final double position)
        {
            this.position = position;
        }

        // public double position()
        // {
        //     return position;
        // }
    }


    // *** CLASS & INSTANCE VARIABLES ***
   

    // private static final CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private static final CANSparkMax armMotor = new CANSparkMax(Port.Motor.INTAKE_ARMS_MOTOR, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final CANSparkMax rollerMotor = new CANSparkMax(5, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    public static final CANSparkMax armMotor = new CANSparkMax(7, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    
    // ^these fellas are for when I'm testing with a boxbot
    private static RelativeEncoder armEncoder = armMotor.getEncoder();
    private static SparkMaxLimitSwitch armForwardLimitSwitch;
    private static SparkMaxLimitSwitch armReverseLimitSwitch;


    // private double armSolenoiod;
    // private double armSensor;
    // private double armUpSensor;
    // private double armDownSensor;
    //^unknown if nessicary because unused
    // private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double INTAKE_SPEED = 0.4;//Constant.INTAKE_SPEED;
    private static final double ARM_SPEED = 0.1;


    // *** CLASS CONSTRUCTOR ***
    public Intake2()
    {
        System.out.println(fullClassName + " : Constructor Started");

        // configMotor(rollerMotor);
        // configMotor(armsMotor);
        configRollerMotor();
        configArmMotor();

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
    //getters
    // public ArmPosition getArmPosition()
    // {
    //     return this.armPosition;
    // }

    // //what this does is set the motors to basically their factory settings in case said mortors had something different done to them at some point.
    // public static void configMotor(CANSparkMax motor)
    // {
    //     System.out.println("configurating " + motor);

    //     motor.restoreFactoryDefaults();
    //     motor.setInverted(true);
    //     motor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

    //     motor.setSoftLimit(SoftLimitDirection.kReverse, 0);
    //     motor.enableSoftLimit(SoftLimitDirection.kReverse, false);
    //     motor.setSoftLimit(SoftLimitDirection.kForward, 0);
    //     motor.enableSoftLimit(SoftLimitDirection.kForward, false);

    //     if(motor == armsMotor) //to my knowledge this is the only motor that'll need an encoder
    //     {
    //         armsBackwardLimitSwitch = motor.getReverseLimitSwitch(Type.kNormallyOpen);
    //         armsBackwardLimitSwitch.enableLimitSwitch(false);
    //         armsForwardLimitSwitch = motor.getReverseLimitSwitch(Type.kNormallyOpen);
    //         armsForwardLimitSwitch.enableLimitSwitch(false);
    //         armsEncoder.setPosition(0);
    //     }

    //     motor.setOpenLoopRampRate(0.1);
    //     motor.setSmartCurrentLimit(40);
        

    //     System.out.println(motor + "Configurated");
    // }


    //what this does is set the motors to basically their factory settings in case said motors had something different done to them at some point.
    private static void configRollerMotor()
    {
        rollerMotor.restoreFactoryDefaults();
        rollerMotor.setInverted(true);
        rollerMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        rollerMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        rollerMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        rollerMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        rollerMotor.enableSoftLimit(SoftLimitDirection.kForward, false);

        rollerMotor.setOpenLoopRampRate(0.1);
        rollerMotor.setSmartCurrentLimit(40);
    }

    //what this does is set the motors to basically their factory settings in case said motors had something different done to them at some point.
    private static void configArmMotor()
    {
        armMotor.restoreFactoryDefaults();
        armMotor.setInverted(true);
        armMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        armMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        armMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        armMotor.setSoftLimit(SoftLimitDirection.kForward, 10);
        armMotor.enableSoftLimit(SoftLimitDirection.kForward, false);

        armReverseLimitSwitch = armMotor.getReverseLimitSwitch(Type.kNormallyOpen);
        armReverseLimitSwitch.enableLimitSwitch(true);
        armForwardLimitSwitch = armMotor.getForwardLimitSwitch(Type.kNormallyOpen);
        armForwardLimitSwitch.enableLimitSwitch(true);
        // armEncoder.setPositionConversionFactor(4096);
        armEncoder.setPosition(0);

        armMotor.setOpenLoopRampRate(0.1);
        armMotor.setSmartCurrentLimit(40);
    }

    public RollerDirection getRollerDirection()
    {
        return this.rollerDirection;
    }

    //setters
    private void setRollerDirection(RollerDirection direction)
    {
        rollerMotor.set(direction.position); //".set" sets the speed, it has to be between 1.0 and -1.0
    }

    private void setArmSpeed(double speed)
    {
        armMotor.set(speed);
    }

    //not getters and setters?
    //7:1 gearbox
    public void outtakeRoller()
    {
        setRollerDirection(RollerDirection.kOut);
        // System.out.println("Roller out");
    }
    
    public void intakeRoller()
    {
        setRollerDirection(RollerDirection.kIn);
        // System.out.println("Roller in");
    }

    public void turnOffRoller()
    {
        setRollerDirection(RollerDirection.kOff);
        // System.out.println("Roller Off");
    }

    public void moveArmOut()
    {
        setArmSpeed(ARM_SPEED);
    }

    public void moveArmIn()
    {
        setArmSpeed(-ARM_SPEED);
    }

    public void stopArm()
    {
        setArmSpeed(0.0);
    }

    public double getArmMotorRotations()
    {
        // 4096 ticks per rev
        return armEncoder.getPosition();
    }

    // public void moveArmOut(double desiredPosition) //FELLA MOVES 8 INCHES
    // //10:1 gear ratio, pulley system 40:24 gear teeth, total gear ratio is 50:3
    // //The motor has a diameter of .49in and a circumference of 1.54in
    // //to move 8in it needs to spin 5.19480519481 times
    // {
    //     desiredPosition -= .05;
    //     System.out.println("Moving arms out...");
    //     setArmSpeed(0.1);
    //     double p = armEncoder.getPosition();
    //     int c = 0;
    //     boolean forceQuit = false;
    //     while(armEncoder.getPosition() < desiredPosition && forceQuit == false/*5.19480519481*50/3*/) //Both getPostion and 5.19480519481 SHOULD be in the unit of rotations //50/3 is the gear ratio
    //     {
    //         if(p != armEncoder.getPosition())
    //         {
    //             c = 0;
    //             p = armEncoder.getPosition();
    //             System.out.println("moving out, position of: " + armEncoder.getPosition());
                
    //         }
    //         else if(armEncoder.getPosition() >= desiredPosition-.8)
    //         {
    //             c++;
    //             System.out.println("C is: " + c);
    //         }
    //         if(armEncoder.getPosition() > desiredPosition-.80)
    //         {
    //             setArmSpeed((desiredPosition-armEncoder.getPosition())/desiredPosition);
    //         }
    //         if(c >= 13 && armEncoder.getPosition() >= desiredPosition-.2)
    //         {
    //             System.out.println("Force quit");
    //             forceQuit = true;
    //         }
    //     }
        
    //     setArmSpeed(0.0);
    //     System.out.println("Final position: " + armEncoder.getPosition());
    //     System.out.println("Arms are out!");
    //     armPosition = ArmPosition.kOut;
        
    // }
    
    // public void moveArmIn(double desiredPosition) //FELLA MOVES 8 INCHES
    // {
    //     desiredPosition += .05;
    //     System.out.println("Moving arms In...");
    //     setArmSpeed(-0.1);
    //     double p = armEncoder.getPosition();
    //     int c = 0;
    //     boolean forceQuit = false;
    //     if(armEncoder.getPosition() > desiredPosition && forceQuit == false) //Both getPostion and 0 SHOULD be in the unit of rotations
    //     {
    //         if(p!=armEncoder.getPosition())
    //         {
    //             c = 0;
    //             p = armEncoder.getPosition();
    //             System.out.println("Moving in, position of: " + armEncoder.getPosition());
    //         }
    //         else if(armEncoder.getPosition() >= desiredPosition+.8)
    //         {
    //             c++;
    //             System.out.println("C is "+ c);
    //         }
    //         if(armEncoder.getPosition() > desiredPosition+.80)
    //         {
    //             setArmSpeed((desiredPosition-armEncoder.getPosition())/desiredPosition);
    //         }
    //         if(c >= 13 && armEncoder.getPosition() >= desiredPosition+.2)
    //         {
    //             System.out.println("Force Quit");
    //             forceQuit = true;
    //         }
           
    //     }
    //     setArmSpeed(0.0);
    //     System.out.println("Arms in!");
    //     armPosition = ArmPosition.kIn;
    // } 
    
    // public void updateArmPosition(ArmPosition armPosition)
    // {
    //     //don't know how worthwile this is when I can just armPosition = ArmPosition.kIn;
    // }

    // public double MeasureMotorSpeed(CANSparkMax motor)
    // {
    //     return(motor.get()); 
    // }

    public String toString()
    {
        String thisthing = "";
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%",  "Arms Position", "Arms Speed");
        // thisthing += String.format("%-20s%-20s%", this.armPosition, armMotor.get()+"\n");
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%-20s%", "RollerDirection", "DesiredRollerSpeed", "RollerSpeed\n");
        thisthing += String.format("%-20s%-20s%-20s%", this.rollerDirection, this.desiredRollerSpeed, this.rollerSpeed+"\n");
        return thisthing;
    }

    // public void TestRoller() //I just wanted to test the motors bro
    // {
    //     //you can find the motor in the red bins in the robotics room and then you need a robot in a box and a laptop
    //     try
    //     {
    //         System.out.println("Starting");
    //         TimeUnit.SECONDS.sleep(2);   
    //         outtakeRoller();
    //         TimeUnit.SECONDS.sleep(2);
    //         turnOffRoller();
    //         TimeUnit.SECONDS.sleep(2);
    //         intakeRoller();
    //         TimeUnit.SECONDS.sleep(2);
    //         turnOffRoller();
    //     }
    //     catch(InterruptedException ex)
    //     {
    //         ex.printStackTrace();
    //     }
        
    // }

    // public void TestArms()
    // {
    //     System.out.println("Starting");
    //     moveArmOut(10);
    //     //moveArmIn();
    //     System.out.println("Complete");
    // }
}
