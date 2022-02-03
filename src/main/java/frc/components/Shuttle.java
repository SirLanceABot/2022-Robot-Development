package frc.components;

import java.lang.invoke.MethodHandles;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANPIDController;

import frc.constants.Port;

public class Shuttle 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // initializing motors
    private static CANSparkMax firstStageMotor = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_ONE, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax secondStageMotor = new CANSparkMax(Port.Motor.SHUTTLE_STAGE_TWO, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);

    // initializing sensors TODO
    // private static SENSOR firstStageSensor = new SENSOR();
    // private static SENSOR secondStageSensor = new SENSOR();

    // initializing encoders
    // private static CANEncoder firstEncoder = firstStageMotor.getEncoder();
    // private static CANEncoder secondEncoder = secondStageMotor.getEncoder();




    // *** CLASS CONSTRUCTOR ***
    // TODO: remove the public access modifier so that the constructor can only be accessed inside the package
    public Shuttle()
    {

    }


    // *** CLASS & INSTANCE METHODS ***

    // TODO: Create a configMotor() method to configure each motor
    

    public void reversefirstStage()
    {

    }
    
    public void stopFirstStage()
    {

    }

    public void forwardFirstStage()
    {

    }

    public void reverseSecondStage()
    {

    }

    public void stopSecondStage()
    {

    }

    public void forwardSecondStage()
    {

    }
    
    public String toString()
    {
        return null;
    }
}
