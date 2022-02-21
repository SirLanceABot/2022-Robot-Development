package frc.vision;

import java.lang.invoke.MethodHandles;
import frc.vision.GripPipeLine;
// import com.google.gson.Gson;

public class TargetDataE extends GripPipeLine
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // NOTE: No modifier means visible to both the class and package.

    // The user MUST MODIFY these following fields.
    // --------------------------------------------------------------------------
    // Target data of the bounding rectangle around the contour
    double center;
    double distance;
    // --------------------------------------------------------------------------

    // These fields are used to track the validity of the data.
    int frameNumber; // Number of the camera frame
    boolean isFreshData; // Is the data fresh?
    boolean isTargetFound;
    
    /**
     * Default contructor - resets all of the target data.
     */
    public TargetDataE()
    {
        //System.out.println(className + " : Constructor Started");

        reset();
        frameNumber = 0;

        //System.out.println(className + ": Constructor Finished");
    }

    /**
     * This method resets all of the target data, except the frameNumber. The user
     * MUST MODIFY
     */
    public synchronized void reset()
    {
        center = -1;
        distance = -1;
        isTargetFound = false;

        // DO NOT reset the frameNumber
        isFreshData = true;
    }

    /**
     * This method stores all of the target data.
     * 
     * @param targetData
     *                       The new target data to store.
     */
    public synchronized void set(TargetDataE targetData)
    {
        center = targetData.center;
        distance = targetData.distance;
        isTargetFound = targetData.isTargetFound;
        frameNumber = targetData.frameNumber;

        // DO NOT MODIFY this value.
        isFreshData = true;
    }

   /**
     * This method returns all of the target data.
     * 
     * @return The target data.
     */
    public synchronized TargetDataE get()
    {
       TargetDataE targetData = new TargetDataE();

       targetData.distance = distance;
       targetData.center = center;
       targetData.isTargetFound = isTargetFound;
       targetData.frameNumber = frameNumber;
       targetData.isFreshData = isFreshData;

       // Indicate that the data is no longer fresh data.
       isFreshData = false;

        return targetData;
    }

    /**
     * This method increments the frame number of the target data.
     */
    public synchronized void incrFrameNumber()
    {
            frameNumber++;
    }

    public synchronized double getCenter()
    {
        return center;
    }

    public synchronized double getDistance()
    {
        return distance;
    }

    /**
     * This method indicates if a target was found.
     * 
     * @return True if target is found. False if target is not found.
     */
    public synchronized boolean isTargetFound()
    {
        return isTargetFound;
    }

    /**
     * This method returns the frame number of the image.
     * 
     * @return The frame number of the camera image, starting at 1 and incrementing
     *         by 1.
     */
    public synchronized int getFrameNumber()
    {
        return frameNumber;
    }

    /**
     * This method indicates if the target data is fresh (new).
     * 
     * @return True if data is fresh. False is data is not fresh.
     */
    public synchronized boolean isFreshData()
    {
        return isFreshData;
    }

    public synchronized void fromJson(String message)
    {
        // Vision temp = new Gson().fromJson(message, Vision.class);
        // set(temp);
    }

    //  public synchronized String toJson()
    {
        //Gson gson = new Gson(); // Or use new GsonBuilder().create();
        // String json = gson.toJson(this); // serializes target to Json
        // return json;
    }

    /**
     * This method converts the data to a string format for output.
     * 
     * @return The string to display.
     */
    public synchronized String toString()
    {
        return String.format("Frame = %d, %s, center = %f, distance = %f, %s",
            frameNumber, isTargetFound ? "target" : "no target", center, distance, isFreshData ? "FRESH" : "stale");
    }


    // *** CLASS & INSTANCE METHODS ***
}