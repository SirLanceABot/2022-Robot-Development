package frc.components;

import frc.constants.Port;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
//import com.revrobotics.CANDigitalInput;
//import com.revrobotics.CANEncoder;
//import com.revrobotics.CANSparkMax;
//import com.revrobotics.ControlType;
//import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
//import com.revrobotics.CANPIDController;

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
        //these double values are the same as the 2021 values
        kIn(Math.abs(intakeSpeed)), //is basically posotive speed
        kOut(Math.abs(intakeSpeed)*-1.0), //is basically negative speed
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
    //private static CANSparkMax rollerMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // ^that fella is for when I'm testing with a boxbot

    // TODO: add the modifier final to the rollerMotor
    private static final CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static final CANSparkMax armsMotor = new CANSparkMax(/*elliot needs to add this port*/1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    // TODO: make the following static
    private double armSolenoiod;
    private double armSensor;
    private double armUpSensor;
    private double armDownSensor;
    private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double intakeSpeed = 0.5; //TODO change to constant.intakeSpeed


    // *** CLASS CONSTRUCTOR ***
    Intake()
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
    public static void configMotor(CANSparkMax Motor)
    {
        System.out.println("configurating " + Motor);

        Motor.restoreFactoryDefaults();
        Motor.setInverted(true);
        Motor.setIdleMode(IdleMode.kBrake); // you gotta import IdleMode before you do this

        Motor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        Motor.enableSoftLimit(SoftLimitDirection.kReverse, false);
        Motor.setSoftLimit(SoftLimitDirection.kForward, 0);
        Motor.enableSoftLimit(SoftLimitDirection.kForward, false);

        System.out.println(Motor + "Configurated");
    }


    public RollerDirection getRollerDirection()
    {
        return this.rollerDirection;
    }

    //setters
    // TODO This looks like a setRollerDirection() method, should it be renamed
    // The setRollerSpeed() method should call the set() method of the rollerMotor
    // we dont currenly need a setRollerSpeed() becuase IntakeSpeed is a constant
    private void setRollerDirection(RollerDirection rollerSpeed)
    {
        rollerMotor.set(rollerSpeed.position); //".set" sets the speed, it has to be between 1.0 and -1.0
    }

    //not getters and setters?
    public void outtakeRoller()
    {
        setRollerDirection(RollerDirection.kOut);
        System.out.println("Roller out");
    }
    
    public void intakeRoller()
    {
        setRollerDirection(RollerDirection.kIn);
        System.out.println("Roller in");
    }

    public void turnOffRoller()
    {
        setRollerDirection(RollerDirection.kOff);
        System.out.println("Roller Off");
    }

    public void moveArmOut()
    {
        
    }

    public void moveArmIn()
    {

    } 

    public void updateArmPosition(ArmPosition armPosition)
    {
        
    }

    public double MeasureRollerSpeed()
    {
        return(1); //find the rollers current speed and return it??
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
            //System.out.println(this.toString());
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
    }
}
