package frc.components;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

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
        kIn,
        kOut,
        kOff
    }

    public static enum RollerDirection
    {
        kIn(-intakeSpeed), //is basically positive speed
        kOut(intakeSpeed), //is basically negative speed
        kOff(0.0); //is zero
        //in is negative

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
   

    // private static final CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // private static final CANSparkMax armsMotor = new CANSparkMax(Port.Motor.INTAKE_ARMS_MOTOR, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rollerMotor;// = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax armsMotor;// = new CANSparkMax(5, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // ^these fellas are for when I'm testing with a boxbot
    private final RelativeEncoder armsEncoder;// = armsMotor.getEncoder();
    private final SparkMaxLimitSwitch armsForwardLimitSwitch;
    private final SparkMaxLimitSwitch armsBackwardLimitSwitch;

    private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double intakeSpeed = 0.3;//Constant.INTAKE_SPEED;
    private static final double armSpeed = 0.5;


    // *** CLASS CONSTRUCTOR ***
    public Intake(int rollerMotorPort, int armsMotorPort)
    {
        System.out.println("Intake Created");

        // rollerMotorPort = 1;  // Used ONLY for testing
        // armsMotorPort = 7;    // Used ONLY for testing

        rollerMotor = new CANSparkMax(rollerMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        armsMotor = new CANSparkMax(armsMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        
        configArmsMotor();
        configRollerMotor();

        armsBackwardLimitSwitch = armsMotor.getReverseLimitSwitch(Type.kNormallyOpen);
        armsBackwardLimitSwitch.enableLimitSwitch(true);
        armsForwardLimitSwitch = armsMotor.getForwardLimitSwitch(Type.kNormallyOpen);
        armsForwardLimitSwitch.enableLimitSwitch(true);

        armsEncoder = armsMotor.getEncoder();
        armsEncoder.setPosition(0);
    }

    // *** CLASS & INSTANCE METHODS ***
    //what this does is set the motors to basically their factory settings in case said mortors had something different done to them at some point.
    private void configRollerMotor()
    {
        System.out.println("configurating Intake Motor");
    
        rollerMotor.restoreFactoryDefaults();
        rollerMotor.setInverted(false);
        rollerMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this
    
        rollerMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        rollerMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        rollerMotor.setSoftLimit(SoftLimitDirection.kForward, 0);
        rollerMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
    
        rollerMotor.setOpenLoopRampRate(0.1);
        rollerMotor.setSmartCurrentLimit(40);
        
        System.out.println("Configurated");
    }

    private void configArmsMotor()
    {
        System.out.println("configurating arms motor");
    
        armsMotor.restoreFactoryDefaults();
        armsMotor.setInverted(false);
        armsMotor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this
    
        armsMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        armsMotor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        armsMotor.setSoftLimit(SoftLimitDirection.kForward, 103); // DF 3/06/22
        armsMotor.enableSoftLimit(SoftLimitDirection.kForward, false);
    
        // armsEncoder.setPosition(0);
        
    
        armsMotor.setOpenLoopRampRate(0.1);
        armsMotor.setSmartCurrentLimit(40);
        
        System.out.println("Configurated");
    }

    //getters
    public ArmPosition getArmPosition()
    {
        return this.armPosition;
    }

    public RollerDirection getRollerDirection()
    {
        return this.rollerDirection;
    }

    //setters
    private void setRollerVelocity(double velocity)
    {
        rollerMotor.set(velocity); // it has to be between 1.0 and -1.0
    }

    private void setArmVelocity(double velocity)
    {
        armsMotor.set(velocity);
    }

    //not getters and setters?
    //7:1 gearbox
    public boolean isArmOut()
    {
       return armsForwardLimitSwitch.isPressed();
    }

    public boolean isArmIn()
    {
       return armsBackwardLimitSwitch.isPressed();
    }

    public void outtakeRoller()
    {
        setRollerVelocity(intakeSpeed);
        // System.out.println("Roller out");
    }
    
    public void intakeRoller()
    {
        setRollerVelocity(-intakeSpeed);
        // System.out.println("Roller in");
    }

    public void turnOffRoller()
    {
        setRollerVelocity(0.0);
        // System.out.println("Roller Off");
    }

    //100:1 gearbox
    public void moveArmOut()
    {
        setArmVelocity(armSpeed);
    }

    public void moveArmIn()
    {
        setArmVelocity(-armSpeed);
    }

    public void stopArm()
    {
        setArmVelocity(0.0);
    }

    public double MeasureMotorSpeed(CANSparkMax motor)
    {
        return(motor.get()); 
    }

    public double getArmMotorRotations()
    {
        // 4096 ticks per rev
        // ^I don't know what that means lmao
        return armsEncoder.getPosition();
    }

    // public void moveArmOut(double desiredPosition) 
    // //FELLA MOVES 5 INCHES
    // //The motor has a diameter of .49in and a circumference of 1.54in
    // //I forgot the gear ratio lol
    // {
    //     desiredPosition -= .05;
    //     System.out.println("Moving arms out...");
    //     setArmSpeed(0.1);
    //     double p = armsEncoder.getPosition();
    //     int c = 0;
    //     boolean forceQuit = false;
    //     while(armsEncoder.getPosition() < desiredPosition && forceQuit == false/*5.19480519481*50/3*/) //Both getPostion and desiredPostion SHOULD be in the unit of rotations //50/3 is the gear ratio
    //     {
    //         if(p != armsEncoder.getPosition())
    //         {
    //             c = 0;
    //             p = armsEncoder.getPosition();
    //             System.out.println("moving out, position of: " + armsEncoder.getPosition());
                
    //         }
    //         else if(armsEncoder.getPosition() >= desiredPosition-.8)
    //         {
    //             c++;
    //             System.out.println("C is: " + c);
    //         }
    //         if(armsEncoder.getPosition() > desiredPosition-.80)
    //         {
    //             setArmSpeed((desiredPosition-armsEncoder.getPosition())/desiredPosition);
    //         }
    //         if(c >= 13 && armsEncoder.getPosition() >= desiredPosition-.2)
    //         {
    //             System.out.println("Force quit");
    //             forceQuit = true;
    //         }
    //     }
        
    //     setArmSpeed(0.0);
    //     System.out.println("Final position: " + armsEncoder.getPosition());
    //     System.out.println("Arms are out!");
    //     armPosition = ArmPosition.kOut;
        
    // }
    
    // public void moveArmIn(double desiredPosition) //FELLA MOVES 8 INCHES
    // {
    //     desiredPosition += .05;
    //     System.out.println("Moving arms In...");
    //     setArmSpeed(-0.1);
    //     double p = armsEncoder.getPosition();
    //     int c = 0;
    //     boolean forceQuit = false;
    //     if(armsEncoder.getPosition() > desiredPosition && forceQuit == false) //Both getPostion and 0 SHOULD be in the unit of rotations
    //     {
    //         if(p!=armsEncoder.getPosition())
    //         {
    //             c = 0;
    //             p = armsEncoder.getPosition();
    //             System.out.println("Moving in, position of: " + armsEncoder.getPosition());
    //         }
    //         else if(armsEncoder.getPosition() >= desiredPosition+.8)
    //         {
    //             c++;
    //             System.out.println("C is "+ c);
    //         }
    //         if(armsEncoder.getPosition() > desiredPosition+.80)
    //         {
    //             setArmSpeed((desiredPosition-armsEncoder.getPosition())/desiredPosition);
    //         }
    //         if(c >= 13 && armsEncoder.getPosition() >= desiredPosition+.2)
    //         {
    //             System.out.println("Force Quit");
    //             forceQuit = true;
    //         }
           
    //     }
    //     setArmSpeed(0.0);
    //     System.out.println("Arms in!");
    //     armPosition = ArmPosition.kIn;
    // } 

    public void outputArmLimit()
    {
        System.out.println("Arm: Encoder " + armsEncoder.getPosition());
        // System.out.println("Arm: Forward Limit Switch " + armsForwardLimitSwitch.isPressed() + ", Backward Limit Switch " + armsBackwardLimitSwitch.isPressed());
    }

    public String toString()
    {
        String thisthing = "";
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%",  "Arms Position", "Arms Speed");
        thisthing += String.format("%-20s%-20s%", this.armPosition, armsMotor.get()+"\n");
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%-20s%", "RollerDirection", "DesiredRollerSpeed", "RollerSpeed\n");
        thisthing += String.format("%-20s%-20s%-20s%", this.rollerDirection, this.desiredRollerSpeed, this.rollerSpeed+"\n");
        return thisthing;
    }

    public void TestRoller()
    {
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
}
