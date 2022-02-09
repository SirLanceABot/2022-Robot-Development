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
        kShoot(Button.k1),
        kTurnOnShooter(Button.k2),

        kShuttleOverride(Button.k11), 
        kShuttle1stStage(Button.k3),
        kShuttle2ndStage(Button.k4),

        kAutoClimb(Button.k9),
        kClimbOverride(Button.k12),
        kExtendClimbArm(Button.k5),
        kShrinkClimbArm(Button.k6),

        // kOffTarget(Button.k3),
        // kOnTarget(Button.k4),

        // // kNoAction(Button.k7),
       
       
       
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

    // *** CLASS CONSTRUCTOR ***
    public OperatorController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        initOperatorController();
        
        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
    public void initOperatorController()
    {
        for(OperatorAxisAction action : OperatorAxisAction.values())
        {
            setAxisSettings(action.axis, action.axisDeadzone, action.axisMinOutput, action.axisMaxOutput, action.axisIsFlipped, action.axisScale);
        }
        //createRumbleEvent(60.0, 1.0, 0.5, 0.5);
    }

    @Deprecated
    public boolean getRawButton(Button button)
    {
        return super.getRawButton(button);
    }

    @Deprecated
    public boolean getRawButton(int button)
    {
        return super.getRawButton(button);
    }

    @Deprecated
    public double getRawAxis(Axis axis)
    {
        return super.getRawAxis(axis);
    }

    @Deprecated
    public double getRawAxis(int axis)
    {
        return super.getRawAxis(axis);
    }

    public boolean getAction(OperatorButtonAction buttonAction)
    {
        return getRawButton(buttonAction.button);
    }

    public double getAction(OperatorAxisAction axisAction)
    {
        return getRawAxis(axisAction.axis);
    }
}
    

