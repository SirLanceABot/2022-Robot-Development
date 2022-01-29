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

    // *** CLASS & INSTANCE VARIABLES ***



    // *** CLASS CONSTRUCTOR ***
   


    // *** CLASS & INSTANCE METHODS ***

    
    public enum DriverButtonAction
    {
        // kRaiseArms(Button.kA),
        // kLowerArms(Button.kB),

        kReverseDirection(Button.kB),

        kIntakeUp(Button.kY),
        kIntakeDown(Button.kX),

        kIntakeReverse(Button.kLeftBumper),
        kIntakeO(Button.kRightBumper),

        // kNoAction(Button.kBack),
        // kNoAction(Button.kStart),

        kDriveBoost(Button.kLeftStick),
        kShiftingUp(Button.kRightStick)
        // kNoAction(Button.kRightStick),
        ;

        public final Button button;

        private DriverButtonAction(Button button)
        {
            this.button = button;
        }
    }
    
    public enum DriverAxisAction
    {
        // kNoAction(Axis.kLeftX, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        kMove(Axis.kLeftY, 0.1, 0.0, 1.0, true, AxisScale.kLinear),

        kRotate(Axis.kRightX, 0.1, 0.0, 1.0, false, AxisScale.kLinear),
        // kNoAction(Axis.kRightY, 0.1, 0.0, 1.0, true, AxisScale.kLinear),

        kUnspoolWinch(Axis.kLeftTrigger, 0.2, 0.3, 0.5, false, AxisScale.kLinear),
        kSpoolWinch(Axis.kRightTrigger, 0.2, 0.3, 1.0, false, AxisScale.kLinear)
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

    public enum DriverPOVAction
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

    // public class RumbleEvent
    // {
    //     public double startTime;
    //     public double duration;
    //     public double leftPower;
    //     public double rightPower;

    //     public RumbleEvent(double startTime, double duration, double leftPower, double rightPower)
    //     {
    //         this.startTime = startTime;
    //         this.duration = duration;
    //         this.leftPower = leftPower;
    //         this.rightPower = rightPower;
    //     }
    // }

    // private ArrayList<RumbleEvent> rumbleEvents = new ArrayList<RumbleEvent>();
    // private int rumbleCounter = 0;

    
        
    

    

    /**
     * Private constructor for driver controller
     */
    public DriverController(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

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

        System.out.println(fullClassName + ": Constructor Finished");
    }

    
        
    

    // public void createRumbleEvent(double startTime, double duration, double leftPower, double rightPower)
    // {
    //     rumbleEvents.add(new RumbleEvent(startTime, duration, leftPower, rightPower));
    // }

    // public void checkRumbleEvent()
    // {
    //     if (rumbleEvents.size() > rumbleCounter)
    //     {
    //         double matchTime = DriverStation.getMatchTime();
    //         double startTime = rumbleEvents.get(rumbleCounter).startTime;
    //         double duration = rumbleEvents.get(rumbleCounter).duration;

    //         if (startTime >= matchTime && matchTime >= startTime - duration)
    //         {
    //             setRumble(RumbleType.kLeftRumble, rumbleEvents.get(rumbleCounter).leftPower);
    //             setRumble(RumbleType.kRightRumble, rumbleEvents.get(rumbleCounter).rightPower);
    //         }
    //         else if (matchTime < startTime - duration)
    //         {
    //             rumbleCounter++;
    //             setRumble(RumbleType.kLeftRumble, 0.0);
    //             setRumble(RumbleType.kRightRumble, 0.0); 
    //         }
    //     }
    // }

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

    // public void resetRumbleCounter()
    // {
    //     rumbleCounter = 0;
    // }
}
