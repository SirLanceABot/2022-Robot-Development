package frc.controls;

import java.lang.invoke.MethodHandles;

public class OperatorController extends Logitech
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    
    // *** INNER ENUMS and INNER CLASSES ***
    public static enum OperatorButtonAction
    {
        kPrepareShooter(Button.k7),
        kLowerHub(Button.k8),
        kShoot(Button.k1),
        kShooterOverride(Button.k2),
        // Slider ShroudAngle and FlywheelRPM in Axis
        
        kCompressorToggle(Button.k10),

        kShuttleOverride(Button.k9), 
        kShuttleStageOneForward(Button.k3),
        kShuttleStageOneReverse(Button.k5),
        kShuttleStageTwoForward(Button.k4),
        kShuttleStageTwoReverse(Button.k6),

        kClimbOneExtend(Button.k11),
        kClimbOneRetract(Button.k12),
        // kClimbShutDown(Button.k12), //Not in use
        // kTurnOnShooterToggle(Button.k8), // Not in use
        

        // kOffTarget(Button.k3),
        // kOnTarget(Button.k4),
        // // kNoAction(Button.k7),

        //IF XBOX CONTROLLER IS USED BY OPERATOR
        // kShoot(Button.kRightTrigger),
        // kShooterOveride(Button.kA),
        // kTurnOnShooter(Button.kLeftBumper,
        // kShootBall(Button.kRightBumper),
        

        // kShuttleOverride(Button.kB), 
        // kShuttle1stStage(Button.kLeftArrow),
        // kShuttle2ndStage(Button.kRightArrow),

        // kAutoClimb(Button.kLeftTrigger),
        // kClimbOverride(Button.kX),
        // kExtendClimbArm(Button.kUpArrow),
        // kShrinkClimbArm(Button.kDownArrow),

       
       
       
        ;

        public final Button button;

        private OperatorButtonAction(Button button)
        {
           this.button = button;
        }
    }

    public static enum OperatorAxisAction
    {
        // kNoAction(Axis.kXAxis, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        kShroud(Axis.kYAxis, 0.2, 0.0, 1.0, true, AxisScale.kLinear),
        //kTurret(Axis.kZAxis, 0.25, 0.0, 1.0, false, AxisScale.kLinear),
        kShooterPower(Axis.kSlider, 0.1, 0.0, 1.0, true, AxisScale.kLinear);

        //iF XBOX CONTROLLER IS USED BY XBOX
        //Shroud(Axis.kLeftY, 0.25, 0.0, 1.0, false, AxisScale.kLinear),
        //kShooterPower(Axis.kLeftX, 0.25, 0.0, 1.0, false, AxisScale.kLinear),

        public final Axis axis;
        public final double axisDeadzone;
        public final double axisMinOutput;
        public final double axisMaxOutput;
        public final boolean axisIsFlipped;
        public final AxisScale axisScale;

        private OperatorAxisAction(Axis axis, double axisDeadzone, double axisMinOutput, double axisMaxOutput, boolean axisIsFlipped, AxisScale axisScale)
        {
            this.axis = axis;
            this.axisDeadzone = axisDeadzone;
            this.axisMinOutput = axisMinOutput;
            this.axisMaxOutput = axisMaxOutput;
            this.axisIsFlipped = axisIsFlipped;
            this.axisScale = axisScale;
        }
    }

    public static enum OperatorDpadAction
    {
        kLED(Dpad.kUp), 
        UpRightMethod(Dpad.kUpRight), 
        // Method(Dpad.kRight), 
        // Method(Dpad.kDownRight), 
        kResetClimberEncoder(Dpad.kDown), 
        // Method(Dpad.kDownLeft), 
        // Method(Dpad.kLeft), 
        // Method(Dpad.kUpLeft), 
        // Method(Dpad.kNone);
        ;

        public final Dpad direction;

        private OperatorDpadAction(Dpad direction)
        {
            this.direction = direction;
        } 
    }

    // *** CLASS CONSTRUCTOR ***
    public OperatorController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        initAxes();
        
        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
    public void initAxes()
    {
        for(OperatorAxisAction action : OperatorAxisAction.values())
        {
            setAxisSettings(action.axis, action.axisDeadzone, action.axisMinOutput, action.axisMaxOutput, action.axisIsFlipped, action.axisScale);
        }
        //createRumbleEvent(60.0, 1.0, 0.5, 0.5);
    }

    public boolean getAction(OperatorButtonAction buttonAction)
    {
        return getRawButton(buttonAction.button);
    }

    public double getAction(OperatorAxisAction axisAction)
    {
        return getRawAxis(axisAction.axis);
    }

    public boolean getAction(OperatorDpadAction dpadAction)
    {
        return getDpad() == dpadAction.direction;
    }
}
    

