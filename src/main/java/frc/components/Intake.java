/*Relevant Documentation:
* https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/DoubleSolenoid.html
* https://robotpy.readthedocs.io/projects/rev/en/stable/rev/CANSparkMax.html
* https://docs.wpilib.org/en/stable/docs/software/hardware-apis/pneumatics/pneumatics.html
* https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/Compressor.html
* https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/DigitalInput.html
*
*/

package frc.components;

import com.revrobotics.CANSparkMax;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.constants.Constant;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.wpilibj.DigitalInput;

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
    private final CANSparkMax rollerMotor;// = new CANSparkMax(1, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
    // ^these fellas are for when I'm testing with a boxbot

    // private final Solenoid armOutSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 0); 
    // private final Solenoid armInSolenoid = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    private PneumaticsModuleType moduleType = PneumaticsModuleType.CTREPCM;
    private final DoubleSolenoid armsInSolenoid;
    private final DoubleSolenoid armsOutSolenoid; 
    //Module Number comes from here: https://docs.wpilib.org/en/stable/docs/software/hardware-apis/pneumatics/pneumatics.html

    private final DigitalInput armOutSensor;
    private final DigitalInput armInSensor;

    private final Compressor compressor = new Compressor(moduleType);

    private ArmPosition armPosition;
    private RollerDirection rollerDirection;
    private double desiredRollerSpeed;
    private double rollerSpeed;

    private static final double intakeSpeed = 0.3;//Constant.INTAKE_SPEED;
    private static final double armSpeed = 0.5;


    // *** CLASS CONSTRUCTOR ***
    public Intake(int rollerMotorPort, int armsInForwardChannel, int armsInReverseChannel, int armsOutForwardChannel, int armsOutReverseChannel, int armsInSensorPort, int armsOutSensorPort)
    {
        System.out.println("Intake Created");

        rollerMotor = new CANSparkMax(rollerMotorPort, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
        armsInSolenoid = new DoubleSolenoid(0, moduleType, armsInForwardChannel, armsInReverseChannel); // 5 is Retract, 7 is Retract float
        armsOutSolenoid = new DoubleSolenoid(0, moduleType, armsOutForwardChannel, armsOutReverseChannel); // 4 is Extend, 6 is Extend float
        armOutSensor = new DigitalInput(armsOutSensorPort); //DF 4/3/22
        armInSensor = new DigitalInput(armsInSensorPort); //DF 4/3/22
        
        configRollerMotor();
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

    //compressor controls
    /**
     * Disables the compressor automatic control loop
     */
    public void compressorDisable()
    {
        compressor.disable();
    }

    /**
     * Enables the compressor automatic control loop
     */
    public void compressorEnable()
    {
        compressor.enableDigital();
    }

    /**
     * Returns true if compressor is disabled
     * 
     * @return isCompressorDisabled
     */
    public boolean isCompressorDisabled()
    {
        return !compressor.enabled();
    }

    public boolean isCompressorRunning()
    {
        return (compressor.getCurrent() > Constant.COMPRESSOR_RUNNING_AMPS);
    }

    /**
     * @deprecated
     * A method for testing the amps pulled by compressor
     */
    public void outputCompressorCurrent()
    {
        double compressorCurrent = compressor.getCurrent();

        if (compressorCurrent != 0.0)
        {
            System.out.println("Compressor current: " + compressorCurrent);
        }
    }

    //pnumeatic controls
    public void pMoveArmOut()
    {
        // armsSolenoid.set(DoubleSolenoid.Value.kForward);
        // armOutSolenoid.set(false);
        // armInSolenoid.set(true);
        armsInSolenoid.set(Value.kForward);
        armsOutSolenoid.set(Value.kReverse);
    }

    public void pMoveArmIn()
    {
        // armsSolenoid.set(Value.kReverse);
        // armOutSolenoid.set(true);
        // armInSolenoid.set(false);
        armsInSolenoid.set(Value.kReverse);
        armsOutSolenoid.set(Value.kForward);
    }

    public void pMoveArmFloat()
    {
        // armsSolenoid.set(Value.kOff);
        // armOutSolenoid.set(false);
        // armInSolenoid.set(false);
        armsInSolenoid.set(Value.kReverse);
        armsOutSolenoid.set(Value.kReverse);
    }

    public void pMoveArmOff()
    {
        armsInSolenoid.set(Value.kOff);
        armsOutSolenoid.set(Value.kOff);
    }

    //not getters and setters?
    //7:1 gearbox
    public void armInfo()
    {
        System.out.println(armInSensor.get() + " " + armOutSensor.get());
    }

    //TODO Set these to the correct directions
    public boolean measureArmOut()
    {
        return !armOutSensor.get();
    }

    public boolean measureArmIn()
    {
        return !armInSensor.get();
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

    public double MeasureMotorSpeed(CANSparkMax motor)
    {
        return(motor.get()); 
    }

    public String toString()
    {
        String thisthing = "";
        thisthing += String.format("------------------------------------------------------------\n");
        thisthing += String.format("%-20s%-20s%",  "Arms Position", "Arms Speed");
        thisthing += String.format("%-20s%-20s%", this.armPosition, "wow \n");
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
