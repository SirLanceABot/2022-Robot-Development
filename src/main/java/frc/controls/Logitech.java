package frc.controls;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Joystick;

public class Logitech extends Joystick
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
        k1(1), k2(2), k3(3), k4(4), k5(5), k6(6), 
        k7(7), k8(8), k9(9), k10(10), k11(11), k12(12);

        public final int value;

        private Button(int value)
        {
            this.value = value;
        }
    }

    public static enum Axis
    {
        kXAxis(0), kYAxis(1), kZAxis(2), kSlider(3);

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
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final double DEFAULT_DEADZONE = 0.1;
    private static final double DEFAULT_MAX_OUTPUT = 1.0;
    private static final double DEFAULT_MIN_OUTPUT = 0.0;
    private static final boolean DEFAULT_IS_FLIPPED = false;
    private static final AxisScale DEFAULT_AXIS_SCALE = AxisScale.kLinear;

    private static final int NUMBER_OF_AXES = 4;
    private final double[] axisDeadzone = new double[NUMBER_OF_AXES];
    private final double[] axisMaxOutput = new double[NUMBER_OF_AXES];
    private final double[] axisMinOutput = new double[NUMBER_OF_AXES];
    private final boolean[] axisIsFlipped = new boolean[NUMBER_OF_AXES];
    private final AxisScale[] axisScale = new AxisScale[NUMBER_OF_AXES];
    private final Button[] buttons = new Button[11];


    // *** CLASS CONSTRUCTOR ***
    protected Logitech(int port)
    {
        super(port);

        System.out.println(fullClassName + " : Constructor Started");
      
        init();

        System.out.println(fullClassName + ": Constructor Finished");
    }


    // *** CLASS & INSTANCE METHODS *** 
    public void init()
    {
        for(int index = 0; index <= NUMBER_OF_AXES - 1; index++)
        {
            axisDeadzone[index] = DEFAULT_DEADZONE;
            axisMaxOutput[index] = DEFAULT_MAX_OUTPUT;
            axisMaxOutput[index] = DEFAULT_MIN_OUTPUT;
            axisIsFlipped[index] = DEFAULT_IS_FLIPPED;
            axisScale[index] = DEFAULT_AXIS_SCALE;
        }
    }

    /**
     * Returns the value of the specified axis
     * @param axis
     */
    @Override
    public double getRawAxis(int axis)
    {
        double value = super.getRawAxis(axis);

        if(axisIsFlipped[axis])
        {
            value *= -1; //value = value * -1;
        }

        if(axis == Axis.kSlider.value)
        {
            value = (value + 1.0) / 2.0;  // scales slider so it goes from 0 to 1 instead of -1 to 1
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
        return super.getRawButton(button.value);
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
     * Used to set the deadzone of the specified axis
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
     * Used to set the maximum output of the specified axis
     * @param axis
     * @param axisMaxOutput
     */
    public void setAxisMaxOutput(Axis axis, double axisMaxOutput)
    {
        axisMaxOutput = Math.abs(axisMaxOutput);
        axisMaxOutput = Math.min(axisMaxOutput, 1.0);

        if(axisMaxOutput >= axisMinOutput[axis.value])
        {
            this.axisMaxOutput[axis.value] = axisMaxOutput;
        }
    }

    /**
     * Used to set the minimum output of the specified axis
     * @param axis
     * @param minOutput
     */
    public void setAxisMinOutput(Axis axis, double axisMinOutput)
    {
        axisMinOutput = Math.abs(axisMinOutput);
        axisMinOutput = Math.min(axisMinOutput, 1.0);

        if(axisMinOutput <= axisMaxOutput[axis.value])
        {
            this.axisMinOutput[axis.value] = axisMinOutput;
        }
    }

    /**
     * Used to flip the output value of the specified axis
     * @param axis
     * @param axisIsFlipped
     */
    public void setAxisIsFlipped(Axis axis, boolean axisIsFlipped)
    {
        this.axisIsFlipped[axis.value] = axisIsFlipped;
    }

    /**
     * Used to set the scale of the specified axis
     * (kLinear, kSquared, kCubed)
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

    
    
    // TODO: Clean up the toString() method
    public String toString()
    {
        String str = "";

        // Concatenates the button values to the string
        str = str + (getRawButton(1) ? " 1" : " 0");
        str = str + (getRawButton(2) ? " 1" : " 0");
        str = str + (getRawButton(3) ? " 1" : " 0");
        str = str + (getRawButton(4) ? " 1" : " 0");
        str = str + (getRawButton(5) ? " 1" : " 0");
        str = str + (getRawButton(6) ? " 1" : " 0");
        str = str + (getRawButton(7) ? " 1" : " 0");
        str = str + (getRawButton(8) ? " 1" : " 0");
        str = str + (getRawButton(9) ? " 1" : " 0");
        str = str + (getRawButton(10) ? " 1" : " 0");
        str = str + (getRawButton(11) ? " 1" : " 0");
        str = str + (getRawButton(12) ? " 1" : " 0");
     
        // for(Button button: Button.values())
        // {
        //     if(getRawButton(button))
        //     {
        //         str = str + " 1";
        //     }
        //     else
        //     {
        //         str = str + " 0";
        //     }
        // }
        
        // Concatenates the axis values to the stirng
        str = str + String.format(" % 3.2f % 3.2f % 3.2f % 3.2f", getRawAxis(0), getRawAxis(1), getRawAxis(2), getRawAxis(3));

        // for(Axis axis: Axis.values())
        // {
        //     str = str + String.format("% 3.2f", getRawAxis(axis));
        // }

        return str;
    }
}
    

