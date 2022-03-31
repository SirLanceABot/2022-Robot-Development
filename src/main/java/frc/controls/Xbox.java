package frc.controls;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Xbox extends Joystick
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
  
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    public static enum Button
    {
        kA(1), kB(2), kX(3), kY(4), kLeftBumper(5), kRightBumper(6), kBack(7), kStart(8), kLeftStick(9), kRightStick(10),
         kLeftTrigger(12), kRightTrigger(13), /*kLeftArrow(14), kRightArrow(15), kUpArrow(16), kDownArrow(17)*/;

        public final int value;

        private Button(int value)
        {
            this.value = value;
        }
    }

    public static enum Dpad
    {
        kUp(0), kUpRight(45), kRight(90), kDownRight(135), kDown(180), kDownLeft(225), kLeft(270), kUpLeft(315), kNone(-1);

        public final int value;

        private Dpad(int value)
        {
            this.value = value;
        }

        public static Dpad getEnum(int value)
        {
            for(Dpad d : Dpad.values())
            {
                if(d.value == value)
                {
                    return d;
                }
            }
            return Dpad.kNone;
        }
    }

    public static enum Axis
    {
        kLeftX(0), kLeftY(1), kLeftTrigger(2), kRightTrigger(3), kRightX(4), kRightY(5);

        public final int value;

        private Axis(int value)
        {
            this.value = value;
        }
    }

    public static enum AxisScale
    {
        kLinear, kSquared, kCubed;
    }

    public class AxisSettings 
    {
        public double axisDeadzone;
        public double axisMinOutput;
        public double axisMaxOutput;
        public boolean axisIsFlipped;
        public AxisScale axisScale;

        public String toString()
        {
            String str = "";

            str += "[";
            str += axisDeadzone + ", ";
            str += axisMinOutput + ", ";
            str += axisMaxOutput + ", ";
            str += axisIsFlipped + ", ";
            str += axisScale;
            str += "]\n";

            return str;
        }
    }
    
    public class RumbleEvent implements Comparable<RumbleEvent>
    {
        public double startTime;
        public double duration;
        public double leftPower;
        public double rightPower;

        public RumbleEvent(double startTime, double duration, double leftPower, double rightPower)
        {
            this.startTime = startTime;
            this.duration = duration;
            this.leftPower = leftPower;
            this.rightPower = rightPower;
        }

        public int compareTo(RumbleEvent rumbleEvent)
        {
            if (startTime > rumbleEvent.startTime)
                return 1;
            else if (startTime < rumbleEvent.startTime)
                return -1;
            else 
            {
                if (duration > rumbleEvent.duration)
                    return 1;
                else if (duration < rumbleEvent.duration)
                    return -1;
                else
                    return 0;
            }
        }
      
        public String toString()
        {
            String str = "";

            str += startTime + " ";
            str += duration + " ";
            str += leftPower + " ";
            str += rightPower;
            return str;
        }
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private final ArrayList<RumbleEvent> rumbleEvents = new ArrayList<RumbleEvent>();
    private int rumbleCounter = 0;

    // set the default axis values
    private final double DEFAULT_DEADZONE = 0.1;
    private final double DEFAULT_MIN_OUTPUT = 0.0;
    private final double DEFAULT_MAX_OUTPUT = 1.0;
    private final boolean DEFAULT_IS_FLIPPED = false;
    private final AxisScale DEFAULT_AXIS_SCALE = AxisScale.kLinear;

    private static final int NUMBER_OF_AXES = 6;
    private final double[] axisDeadzone = new double[NUMBER_OF_AXES];
    private final double[] axisMinOutput = new double[NUMBER_OF_AXES];
    private final double[] axisMaxOutput = new double[NUMBER_OF_AXES];
    private final boolean[] axisIsFlipped = new boolean[NUMBER_OF_AXES];
    private final AxisScale[] axisScale = new AxisScale[NUMBER_OF_AXES];


    // *** CLASS CONSTRUCTOR ***
    protected Xbox(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        initXbox();
       
        System.out.println(fullClassName + ": Constructor Finished"); 
    }

    
    // *** CLASS & INSTANCE METHODS *** 
    public void initXbox()
    {
        for(int index = 0; index <= NUMBER_OF_AXES - 1; index++)
        {
            axisDeadzone[index] = DEFAULT_DEADZONE;
            axisMinOutput[index] = DEFAULT_MIN_OUTPUT;
            axisMaxOutput[index] = DEFAULT_MAX_OUTPUT;
            axisIsFlipped[index] = DEFAULT_IS_FLIPPED;
            axisScale[index] = DEFAULT_AXIS_SCALE;
        }
    }

    @Override
    /**
     * Public method to get the value of an axis
     * @param axis
     */
    public double getRawAxis(int axis)
    {
        double value = super.getRawAxis(axis);

        if(axisIsFlipped[axis])
        {
            value = -value;
        }
        if(Math.abs(value) <= axisDeadzone[axis])
        {
            value = 0.0;
        }
        else
        {
            switch(axisScale[axis])
            {
                case kLinear:
                    value = (axisMaxOutput[axis] - axisMinOutput[axis]) / (1.0 - axisDeadzone[axis]) * (value - axisDeadzone[axis] * Math.signum(value)) + (axisMinOutput[axis] * Math.signum(value));
                    break;
                case kSquared:
                    value = (axisMaxOutput[axis] - axisMinOutput[axis]) * Math.signum(value) / Math.pow((1.0 - axisDeadzone[axis]), 2) * Math.pow((value - axisDeadzone[axis] * Math.signum(value)), 2)  + (axisMinOutput[axis] * Math.signum(value));
                    break;
                case kCubed:
                    value = (axisMaxOutput[axis] - axisMinOutput[axis]) / Math.pow((1.0 - axisDeadzone[axis]), 3) * Math.pow((value - axisDeadzone[axis] * Math.signum(value)), 3)  + (axisMinOutput[axis] * Math.signum(value));
                    break;
            }
        }
        return value;
    }

     /**
     * This methods returns the value of the axis
     * @param axis
     * @return the value of the specified axis
     */
    public double getRawAxis(Axis axis)
    {
        return getRawAxis(axis.value);
    }
    
    /**
     * This methods returns whether the button is pressed or not
     * @param button the button to return
     * @return whether or not the specified is button is being pressed
     */

    public boolean getRawButton(Button button)
    {
        return getRawButton(button.value);
    }
    
    public boolean getRawButton(int button)
    {

        if (button == Button.kLeftTrigger.value || button == Button.kRightTrigger.value)
        {
            if (getRawAxis(button - 10) < 0.5)
                return false;
            else   
                return true;
        }
        else
            return super.getRawButton(button);
    }

    public Dpad getDpad()
    {
        return Dpad.getEnum(getPOV());
    }

    /**
     * This method returns the axis settings for 1 axis
     * @param axis the axis to get the settings for 
     * @return AxisSettings for the axis sent
     */
    public AxisSettings getAxisSettings(Axis axis)
    {
        AxisSettings axisSettings = new AxisSettings();

        axisSettings.axisDeadzone = axisDeadzone[axis.value];
        axisSettings.axisMinOutput = axisMinOutput[axis.value];
        axisSettings.axisMaxOutput = axisMaxOutput[axis.value];
        axisSettings.axisIsFlipped = axisIsFlipped[axis.value];
        axisSettings.axisScale = axisScale[axis.value];

        return axisSettings;
    }

     /**
     * Public method to manually set an axis deadzone
     * @param axis
     * @param axisDeadzone
     */
    public void setAxisDeadzone(Axis axis, double axisDeadzone)
    {
        axisDeadzone = Math.abs(axisDeadzone);
        axisDeadzone = Math.min(1.0, axisDeadzone);

        this.axisDeadzone[axis.value] = axisDeadzone;
    }

    /**
     * Public method to manually set an axis min output
     * @param axis
     * @param axisMinOutput
     */
    public void setAxisMinOutput(Axis axis, double axisMinOutput)
    {
        axisMinOutput = Math.abs(axisMinOutput);
        axisMinOutput = Math.min(axisMinOutput, 1.0);

        this.axisMinOutput[axis.value] = axisMinOutput;
    }

    /**
     * Public method to manually set an axis max output
     * @param axis
     * @param axisMaxOutput
     */
    public void setAxisMaxOutput(Axis axis, double axisMaxOutput)
    {
        axisMaxOutput = Math.abs(axisMaxOutput);
        axisMaxOutput = Math.min(axisMaxOutput, 1.0);

        this.axisMaxOutput[axis.value] = axisMaxOutput;
    }

    /**
     * Public method to manually set whether an axis is flipped or not
     * @param axis
     * @param axisIsFlipped
     */
    public void setAxisIsFlipped(Axis axis, boolean axisIsFlipped)
    {
        this.axisIsFlipped[axis.value] = axisIsFlipped;
    }

    /**
     * Public method to manually set the scale for an axis
     * @param axis
     * @param axisScale
     */
    public void setAxisScale(Axis axis, AxisScale axisScale)
    {
        this.axisScale[axis.value] = axisScale;
    }

    /**
     * Public method to initialize the settings for one axis
     * @param axis
     * @param axisDeadzone
     * @param axisMinOutput
     * @param axisMaxOutput
     * @param axisIsFlipped
     * @param axisScale
     */
    public void setAxisSettings(Axis axis, double axisDeadzone, double axisMinOutput, double axisMaxOutput, boolean axisIsFlipped, AxisScale axisScale)
    {
        setAxisDeadzone(axis, axisDeadzone);
        setAxisMinOutput(axis, axisMinOutput);
        setAxisMaxOutput(axis, axisMaxOutput);
        setAxisIsFlipped(axis, axisIsFlipped);
        setAxisScale(axis, axisScale);
    }

    /**
     * Public method to initialize the settings for one axis
     * @param axis
     * @param axisSettings
     */
    public void setAxisSettings(Axis axis, AxisSettings axisSettings)
    {
        setAxisSettings(axis, axisSettings.axisDeadzone, axisSettings.axisMinOutput, axisSettings.axisMaxOutput, axisSettings.axisIsFlipped, axisSettings.axisScale);
        
    }
    
    public void createRumbleEvent(double startTime, double duration, double leftPower, double rightPower)
    {
        boolean isNoOverlap = true;
        double endTime = startTime - duration;
        double reEndTime = 0;
        int fail = 0;

        for (RumbleEvent rumbleEvent : rumbleEvents)
        {
            reEndTime = rumbleEvent.startTime - rumbleEvent.duration;
            if (rumbleEvent.startTime >= startTime && startTime > reEndTime)
            {   
                isNoOverlap = false;
                fail = 1;
            }    
            else if (rumbleEvent.startTime > endTime && endTime > reEndTime)
            {   
                isNoOverlap = false;
                fail = 2;
            }  
            else if (startTime >= rumbleEvent.startTime && rumbleEvent.startTime > endTime)
            {   
                isNoOverlap = false;
                fail = 3;
            }  
            else if (startTime > reEndTime && reEndTime > endTime)
            {   
                isNoOverlap = false;
                fail = 4;
            }  
        }

        if (isNoOverlap)
        {
            rumbleEvents.add(new RumbleEvent(startTime, duration, leftPower, rightPower));
            rumbleCounter++;
            Collections.sort(rumbleEvents, Collections.reverseOrder());
            // System.out.println("Rumble Event Created: " + fail + " " + startTime + " " + duration + " " + leftPower + " " + rightPower);
        }
        else 
        {
            System.out.println("Rumble Event Overlap: " + fail + " " + startTime + " " + duration + " " + leftPower + " " + rightPower);
        }
    }

    public void checkRumbleEvent()
    {
        if (rumbleEvents.size() > rumbleCounter)
        {
            double matchTime = DriverStation.getMatchTime();
            double startTime = rumbleEvents.get(rumbleCounter).startTime;
            double duration = rumbleEvents.get(rumbleCounter).duration;

            if (startTime >= matchTime && matchTime > startTime - duration)
            {
                setRumble(RumbleType.kLeftRumble, rumbleEvents.get(rumbleCounter).leftPower);
                setRumble(RumbleType.kRightRumble, rumbleEvents.get(rumbleCounter).rightPower);
            }
            else if (matchTime <= startTime - duration)
            {
                rumbleCounter++;
                setRumble(RumbleType.kLeftRumble, 0.0);
                setRumble(RumbleType.kRightRumble, 0.0); 
            }
        }
    }

    public void resetRumbleCounter()
    {
        rumbleCounter = 0;
    }

    @Override
    public String toString()
    {
        String str = "";

        //buttons
        str = str + "A   " + (getRawButton(Button.kA)) + "\n";
        str = str + "B   " + (getRawButton(Button.kB)) + "\n";
        str = str + "X   " + (getRawButton(Button.kX)) + "\n";
        str = str + "Y   " + (getRawButton(Button.kY)) + "\n";
        str = str + "Left Bumper   " + (getRawButton(Button.kLeftBumper)) + "\n";
        str = str + "Right Bumper   " + (getRawButton(Button.kRightBumper)) + "\n";
        str = str + "Back   " + (getRawButton(Button.kBack)) + "\n";
        str = str + "Start   " + (getRawButton(Button.kStart)) + "\n";
        str = str + "Left Stick   " + (getRawButton(Button.kLeftStick)) + "\n";
        str = str + "Right Stick   " + (getRawButton(Button.kRightStick)) + "\n";

        //axis
        str = str + "Left X   " + String.format("%3.2f", getRawAxis(Axis.kLeftX)) + "\n";
        str = str + "Left Y   " + String.format("%3.2f", getRawAxis(Axis.kLeftY)) + "\n";
        str = str + "Left Trigger   " + String.format("%3.2f", getRawAxis(Axis.kLeftTrigger)) + "\n";
        str = str + "Right Trigger   " + String.format("%3.2f", getRawAxis(Axis.kRightTrigger)) + "\n";
        str = str + "Right X   " + String.format("%3.2f", getRawAxis(Axis.kRightX)) + "\n";
        str = str + "Right Y   " + String.format("%3.2f", getRawAxis(Axis.kRightY)) + "\n";

        return str;
    }

    
    
    
}

    

