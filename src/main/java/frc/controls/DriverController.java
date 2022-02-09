package frc.controls;

import java.lang.invoke.MethodHandles;

//import frc.constants.Port;

//import edu.wpi.first.wpilibj.DriverStation;

public class DriverController extends Xbox
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    

    // *** INNER ENUMS and INNER CLASSES ***
    public static enum DriverButtonAction
    {
        kTurnOnRobot(Button.kStart),
        // kRaiseArms(Button.kA),
        // kLowerArms(Button.kB),

        // kReverseDirection(Button.kB),


        // kIntakeUp(Button.kY),
        // kIntakeDown(Button.kX),


        kIntakeReverse(Button.kRightBumper),
        kIntakeOn(Button.kLeftBumper),
        kExtendIntake(Button.kRightStick),

        // kNoAction(Button.kBack),
        // kNoAction(Button.kStart),

        // kDriveBoost(Button.kLeftStick),
        // kNoAction(Button.kRightStick),

        // kNoAction(Button.kLeftTrigger),
        kNoAction(Button.kRightTrigger),

        ;

        public final Button button;

        private DriverButtonAction(Button button)
        {
            this.button = button;
        }
    }
    
    public static enum DriverAxisAction
    {
        // kNoAction(Axis.kLeftX, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        kMoveY(Axis.kLeftY, 0.1, 0.0, 1.0, true, AxisScale.kLinear),
        kMoveX (Axis.kLeftX, 0.1, 0.0, 1.0, true, AxisScale.kLinear),

        kRotate(Axis.kRightX, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        // kNoAction(Axis.kRightY, 0.1, 0.0, 1.0, true, AxisScale.kLinear),

        // kExtendIntake(Axis.kLeftTrigger, 0.2, 0.3, 0.5, false, AxisScale.kLinear),
        kDriverBoost(Axis.kRightTrigger, 0.2, 0.3, 1.0, false, AxisScale.kLinear)
        ;

        public final Axis axis;
        public final double axisDeadzone;
        public final double axisMinOutput;
        public final double axisMaxOutput;
        public final boolean axisIsFlipped;
        public final AxisScale axisScale;

        private DriverAxisAction(Axis axis, double axisDeadzone, double axisMinOutput, double axisMaxOutput, boolean axisIsFlipped, AxisScale axisScale)
        {
            this.axis = axis;
            this.axisDeadzone = axisDeadzone;
            this.axisMinOutput = axisMinOutput;
            this.axisMaxOutput = axisMaxOutput;
            this.axisIsFlipped = axisIsFlipped;
            this.axisScale = axisScale;
        } 
    }

    public static enum DriverPOVAction
    {
        kShiftingDown(180),
        kShiftingUp(0)
        ;

        public final int direction;

        private DriverPOVAction(int direction)
        {
            this.direction = direction;
        } 
    }

    
    // *** CLASS CONSTRUCTOR ***
    public DriverController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");
        
        initDriverController();

        for(DriverButtonAction dba : DriverButtonAction.values())
        {
            if(dba.button == Button.kLeftTrigger || dba.button == Button.kRightTrigger)
            {
                for(DriverAxisAction daa : DriverAxisAction.values())
                {
                    if(daa.axis == Axis.kLeftTrigger && dba.button == Button.kLeftTrigger)
                        System.out.println("ERROR - Left Trigger is button and axis");
                    if(daa.axis == Axis.kRightTrigger && dba.button == Button.kRightTrigger)
                        System.out.println("ERROR - Right Trigger is button and axis");
                }
            }
        }

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS *** 

    public void initDriverController()
    {
        // loop to set the defaults for every axis
        for(DriverAxisAction action : DriverAxisAction.values())
        {
            setAxisSettings(action.axis, action.axisDeadzone, action.axisMinOutput, action.axisMaxOutput, action.axisIsFlipped, action.axisScale);
        }

            
        createRumbleEvent(30.0, 2.0, 0.75, 0.75);
        createRumbleEvent(10.0, 1.0, 1.0, 1.0);
        createRumbleEvent(5.0, 0.25, 1.0, 1.0);
        createRumbleEvent(4.0, 0.25, 1.0, 1.0);
        createRumbleEvent(3.0, 0.25, 1.0, 1.0);
        createRumbleEvent(2.0, 0.25, 1.0, 1.0);
        createRumbleEvent(1.0, 0.25, 1.0, 1.0);
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

    public boolean getAction(DriverButtonAction buttonAction)
    {
        return getRawButton(buttonAction.button);
    }

    public double getAction(DriverAxisAction axisAction)
    {
        return getRawAxis(axisAction.axis);
    }

    public int getAction()
    {
        return getPOV();
    }

}
