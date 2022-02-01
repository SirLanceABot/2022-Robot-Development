package frc.components;

import frc.constants.Port;
import com.revrobotics.CANSparkMax;

import java.lang.invoke.MethodHandles;
import java.util.Date;
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

    // *** CLASS & INSTANCE VARIABLES ***
    // testing this out
    private enum ArmPosition
    {
        up,
        moving, 
        down
    }

    enum RollerDirection
    {
        in, //positive
        out, //nagative
        off //zero 
    }

    //private static CANSparkMax rollerMotor = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // ^that fella is for when I'm testing with a boxbot
    private static CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private double armSolenoiod;
    private double armSensor;
    private double armUpSensor;
    private double armDownSensor;
    private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double IntakeSpeed = 0.5; //TODO change to constant.intakeSpeed

    // *** CLASS CONSTRUCTOR ***
    public Intake()
    {
        System.out.println("Intake Created");
    }

    // *** CLASS & INSTANCE METHODS ***
    //getters
    private ArmPosition getArmPosition()
    {
        return this.armPosition;
    }

    private RollerDirection getRollerDirection()
    {
        return this.rollerDirection;
    }
    //setters
    private void setRollerSpeed(RollerDirection rollerSpeed)
    {
        this.rollerDirection = rollerSpeed;
    }
    //not getters and setters?
    public void outtakeRoller()
    {
        setRollerSpeed(RollerDirection.out);
        rollerMotor.set(-0.5); //".set" sets the speed, it has to be between 1.0 and -1.0
        System.out.println("Roller out");
    }
    
    public void intakeRoller()
    {
        setRollerSpeed(RollerDirection.in);
        rollerMotor.set(0.5);
        System.out.println("Roller in");
    }

    public void turnOffRoller()
    {
        setRollerSpeed(RollerDirection.off);
        rollerMotor.set(0.0);
        System.out.println("Roller Off");
    }

    public void moveArmUp()
    {
        
    }

    public void moveArmDown()
    {

    } 

    public void updateArmPosition(ArmPosition armPosition)
    {
        if(armPosition == ArmPosition.down)
        {
            moveArmDown();
        }
        else if(armPosition == ArmPosition.up)
        {
            moveArmUp();
        }
    }

    public RollerDirection getRollerSpeed()
    {
        return(rollerDirection); //roller speed
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
    {//TODO try this code on monday
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