package frc.controls;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class Xbox extends Joystick
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    // TODO: remove the following
    //private static final String Owen = "Owen is cool";


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    // TODO: make the following enums static
    public enum Button
    {
        kA(1), kB(2), kX(3), kY(4), kLeftBumper(5), kRightBumper(6), kBack(7), kStart(8), kLeftStick(9), kRightStick(10);

        public final int value;

        private Button(int value)
        {
            this.value = value;
        }
    }

    public enum Axis
    {
        kLeftX(0), kLeftY(1), kLeftTrigger(2), kRightTrigger(3), kRightX(4), kRightY(5);

        public final int value;

        private Axis(int value)
        {
            this.value = value;
        }
    }

    public enum AxisScale
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
    }
    
    // TODO: Implement the Comparable interface, so the array list can be sorted
    // Sort first by startTime, then by duration
    // Check this out for help - https://www.geeksforgeeks.org/java-program-to-sort-an-arraylist/
    public class RumbleEvent
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

        // TODO: Add the compareTo() method, first compare startTime, then duration

        // TODO: Add a toString() method
    }


    // *** CLASS & INSTANCE VARIABLES ***
    // TODO: make the rumbleEvents final
    private ArrayList<RumbleEvent> rumbleEvents = new ArrayList<RumbleEvent>();
    private int rumbleCounter = 0;

    // set the default axis values
    private final double DEFAULT_DEADZONE = 0.1;
    private final double DEFAULT_MIN_OUTPUT = 0.0;
    private final double DEFAULT_MAX_OUTPUT = 1.0;
    private final boolean DEFAULT_IS_FLIPPED = false;
    private final AxisScale DEFAULT_AXIS_SCALE = AxisScale.kLinear;

    // TODO: Add the "final" modifier so that these cannot change
    // TODO: create an constanct NUMBER_OF_AXES, set it to 6 and use it in the following declarations
    private double[] axisDeadzone = new double[6];
    private double[] axisMinOutput = new double[6];
    private double[] axisMaxOutput = new double[6];
    private boolean[] axisIsFlipped = new boolean[6];
    private AxisScale[] axisScale = new AxisScale[6];


    // *** CLASS CONSTRUCTOR ***
    protected Xbox(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");

        // TODO: Create an init() method and put the following loop in that method
        // loop to set the defaults for every axis
        for(int index = 0; index <= 5; index++)
        {
            axisDeadzone[index] = DEFAULT_DEADZONE;
            axisMinOutput[index] = DEFAULT_MIN_OUTPUT;
            axisMaxOutput[index] = DEFAULT_MAX_OUTPUT;
            axisIsFlipped[index] = DEFAULT_IS_FLIPPED;
            axisScale[index] = DEFAULT_AXIS_SCALE;
        }

        System.out.println(fullClassName + ": Constructor Finished"); 
    }


    // *** CLASS & INSTANCE METHODS *** 

    // TODO: create the init() method here, place the loop in here and call resetRumbleCounter()

    public double getRawAxis(Axis axis)
    {
        return getRawAxis(axis.value);
    }

    public boolean getRawButton(Button button)
    {
        return super.getRawButton(button.value);
    }

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

    // TODO: move the comment below up here
    public void setAxisSettings(Axis axis, double axisDeadzone, double axisMinOutput, double axisMaxOutput, boolean axisIsFlipped, AxisScale axisScale)
    {
        setAxisDeadzone(axis, axisDeadzone);
        setAxisMinOutput(axis, axisMinOutput);
        setAxisMaxOutput(axis, axisMaxOutput);
        setAxisIsFlipped(axis, axisIsFlipped);
        setAxisScale(axis, axisScale);
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
            value *= -1;
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

    // TODO: This goes above the setAxisSettings() method
    /**
     * Public method to initialize the settings for one axis
     * @param axis
     * @param axisDeadzone
     * @param axisMinOutput
     * @param axisMaxOutput
     * @param axisIsFlipped
     * @param axisScale
     */

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
        // TODO: sort the array list in descending order after adding the event
        // Check this out for help - https://www.geeksforgeeks.org/java-program-to-sort-an-arraylist/
        // Add a loop to check that the new rumble event does not overlap any other events
        // If there is no overlap, then create the rumble event

        rumbleEvents.add(new RumbleEvent(startTime, duration, leftPower, rightPower));
    }

    public void checkRumbleEvent()
    {
        if (rumbleEvents.size() > rumbleCounter)
        {
            double matchTime = DriverStation.getMatchTime();
            double startTime = rumbleEvents.get(rumbleCounter).startTime;
            double duration = rumbleEvents.get(rumbleCounter).duration;

            // TODO: change the second relational operator to >
            if (startTime >= matchTime && matchTime >= startTime - duration)
            {
                setRumble(RumbleType.kLeftRumble, rumbleEvents.get(rumbleCounter).leftPower);
                setRumble(RumbleType.kRightRumble, rumbleEvents.get(rumbleCounter).rightPower);
            }
            // TODO: change the relational operator to <=
            else if (matchTime < startTime - duration)
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

    // TODO: Fix this toString() method
    // @Override
    // public String toString()
    // {
    //     String str = "";

    //     for (int index = 1; index <= 10; index++)
    //     { 
    //         if (getRawButton(index)) 
    //             str = str + "1 ";
    //         else
    //             str = str + "0 ";
    //     }

    //     for (int index = 0; index <= 5; index++)
    //     {
    //         str = str + getRawAxis(index);
    //     }

    //     return str;
    // }

    
    
    
}

    

