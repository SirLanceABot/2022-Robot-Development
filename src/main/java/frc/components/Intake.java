package frc.components;

import frc.constants.Port;
import com.revrobotics.CANSparkMax;

import java.lang.invoke.MethodHandles;

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
    private enum ArmPosition
    {
        up,
        moving, 
        down
    }

    enum RollerDirection
    {
        in,
        out,
        off
    }

    private static CANSparkMax rollerMotor = new CANSparkMax(Port.Motor.INTAKE_ROLLER, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private double armSolenoiod;
    private double armSensor;
    private double armUpSensor;
    private double armDownSensor;
    private ArmPosition armPosition;
    private RollerDirection rollerdirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    // *** CLASS CONSTRUCTOR ***
    public Intake()
    {

    }

    // *** CLASS & INSTANCE METHODS ***
    //getters
    private ArmPosition getArmPosition()
    {
        return this.armPosition;
    }

    private RollerDirection getRollerDirection()
    {
        return this.rollerdirection;
    }
    //setters
    private void setRollerSpeed(RollerDirection rollerSpeed)
    {
        this.rollerdirection = rollerSpeed;
    }
    //not getters and setters?
    public void outtakeRoller()
    {
        
    }
    
    public void intakeRoller()
    {

    }

    public void turnOffRoller()
    {

    }

    public void moveArmUp()
    {

    }

    public void moveArmDown()
    {

    }

    public void updateArmPosition(ArmPosition armPosition)
    {
        this.armPosition = armPosition;
    }

    public double getRollerSpeed()
    {
        return(1.0); //roller speed
    }

    public void ToString()
    {
        System.out.printf("%-20s%-20s%-20s%", "RollerMotor", "armSolenoid", "armPosition");
        System.out.printf("%-20s%-20s%-20s%", this.rollerMotor, this.armSolenoiod, this.armPosition);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s%-20s%-20s%",  "ArmSensor", "ArmDownSensor", "ArmUpSensor");
        System.out.printf("%-20s%-20s%-20s%", this.armSensor, this.armUpSensor, this.armDownSensor);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-20s%-20s%-20s%", "RollerDirection", "DesiredRollerSpeed", "RollerSpeed");
        System.out.printf("%-20s%-20s%-20s%", this.rollerdirection, this.desiredRollerSpeed, this.rollerSpeed);
    }
}
