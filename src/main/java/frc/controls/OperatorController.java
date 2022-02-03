package frc.controls;

import java.lang.invoke.MethodHandles;

// TODO: delete the following
import frc.constants.Port;

//import java.util.ArrayList;

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
    // TODO: make the following enums static
    public enum OperatorButtonAction
    {
        kShoot(Button.kTrigger),
        kAutoAim(Button.kHandleSide),

        kOffTarget(Button.kHandleBottomLeft),
        kOnTarget(Button.kHandleBottomRight),
        // kNoAction(Button.kHandleTopLeft),
        // kNoAction(Button.kHandleTopRight),

        // kNoAction(Button.kOuterTop),
        // kNoAction(Button.kInnerTop),
        kFlywheelOverride(Button.kOuterMiddle),
        kShooterOverride(Button.kInnerMiddle),
        kWinch(Button.kOuterBottom),
        kShuttleOverride(Button.kInnerBottom) 
        ;

        public final Button button;

        private OperatorButtonAction(Button button)
        {
           this.button = button;
        }
    }

    public enum OperatorAxisAction
    {
        // kNoAction(Axis.kXAxis, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        kShroud(Axis.kYAxis, 0.2, 0.0, 1.0, true, AxisScale.kLinear),
        kTurret(Axis.kZAxis, 0.25, 0.0, 1.0, false, AxisScale.kLinear),
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

    public OperatorController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        // TODO: Create an init() method and put the following loop and createRumbleEvent() in that method
        // loop to set the defaults for every axis
        for(OperatorAxisAction action : OperatorAxisAction.values())
        {
            setAxisSettings(action.axis, action.axisDeadzone, action.axisMinOutput, action.axisMaxOutput, action.axisIsFlipped, action.axisScale);
        }
        //createRumbleEvent(60.0, 1.0, 0.5, 0.5);

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS ***   

     // TODO: create the init() method here, place the loop in here and call createRumbleEvent()


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
    

