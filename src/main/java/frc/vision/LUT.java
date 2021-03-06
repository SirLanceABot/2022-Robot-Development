package frc.vision;

import java.lang.invoke.MethodHandles;

/*

Table lookup of X, Y co-ordinates

Features and limitations:
Given an independent variable value a dependent value is returned
Straight line interpolation between table entries
Double.NEGATIVE_INFINITY and Double.POSITIVE_INFINITY independent values are inferred as extensions of the first and last table entries
    that is all values less than the first table entry to *.lookup(Double.NEGATIVE_INFINITY) return the first table entry as extrapolation
    and similarly all values greater than the last table entry to *.lookup(Double.POSITIVE_INFINITY) return the last table entry as
    extrapolation.  If this is inappropriate for the usage then make sure the first and last data points bound the possible data to
    prevent attempted extrapolation.
Not much is optimized for anything except easy to program and easy to use.
    That implies relatively few table entries.
    Data must be added to the table with the independent variable increasing from the previous entry.
    Program searches for the appropriate table entries by brute force starting at the first table entry and checks until data found.
    Each interpolation request is from equation of line with 2 points instead of slightly more efficient slope/intercept.
Interpolation (lookup) returns NaN on any error

Example usage:
 public static void main(String[] args)
    {
        App x = new App(); // get a non-static object if needed

        LUT conversion = x.new LUT(10); // allocate fixed size array with parameter at least as large as the number of data points - minimum of 2 points
        conversion.add(1.0, 100.); // enter X, Y co-ordinate
        conversion.add(2.5, 200.); // enter the data in X ascending order, must add at least 2 data points
        System.out.println("1.5 Converted to " + conversion.lookup(1.5)); // lookup returns the value of Y corresponding to the X parameter
        System.out.println(conversion); // print the whole table
    }
*/

public class LUT
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    Point LUT[];

    private int capacity=0;
    private int next=0;

    class Point
    {
        double X;
        double Y;
        Point (double  X, double Y){this.X = X; this.Y = Y;}
    }

    public LUT(int capacity)
    {
        if (capacity < 2) throw new IllegalArgumentException("Must have at least 2 data points; you tried to use " + capacity);
        this.capacity = capacity;
        LUT = new Point[capacity];
    }

    public void add(double X, double Y)
    {
        if (next >= capacity) throw new IllegalArgumentException("Too many entries attempted to be added to LUT; max is " + capacity);
        if (next >= 1 && X <= LUT[next-1].X) throw new IllegalArgumentException("X not entered in ascending order");
        LUT[next++] = new Point(X, Y);
    }

    public double lookup(double X)
    {
        double Y=Double.NaN;
        final int tableMin = 0;
        final int tableMax = next - 1;
        int i;

        if (tableMax < 1) throw new IllegalArgumentException("Must have at least 2 data points; number of points is " + next);

        if (X < LUT[tableMin].X)
            {Y = LUT[tableMin].Y;} // below table

        else if (X > LUT[tableMax].X)
            {Y = LUT[tableMax].Y;} // above table

        else
            { // within table, find out where
            for (i = tableMin+1; i <= tableMax-1; i++)
                {if (X < LUT[i].X) break;}
            Y =  LUT[i-1].Y + (LUT[i].Y - LUT[i-1].Y)*((X -LUT[i-1].X)/(LUT[i].X - LUT[i-1].X));
            }

        return Y;
    }

    public String toString()
    {
        String str = "LUT {";
        for (int idx = 0; idx < next; idx++)
        str = str + "{" + LUT[idx].X + ", " + LUT[idx].Y + "}";
        str = str + "}";
        return str;
    }
}
