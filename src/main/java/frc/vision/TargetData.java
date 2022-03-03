package frc.vision;

// Expected usage documented at the bottom of the code

/**
 * This class is used to store the target data. The user MUST MODIFY the
 * process() method. The user must create a new GripPipeline class using GRIP,
 * modify the TargetSelection class, and modify this class.
 * 
 * @author FRC Team 4237
 * @version 2019.01.28.14.20
 */
public class TargetData
{
    // NOTE: No modifier means visible to both the class and package.

    // Target data that we need
    double hubDistance;
    double angleToTurn;

    // These fields are used to track the validity of the data.
    int frameNumber; // Number of the camera frame

    // Variable isFreshData means whether the roboRIO has seen the current data from the Raspberry Pi already.
    boolean isFreshData; // Is the data fresh?
    boolean isTargetFound;

    /**
     * Default constructor - resets all of the target data.
     */
    public TargetData()
    {
        reset();
        frameNumber = 0;
    }

    /**
     * This method resets all of the target data, except the frameNumber. The user MUST MODIFY.
     */
    public synchronized void reset()
    {
        hubDistance = -1.0;
        angleToTurn = 0.;
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
    public synchronized void set(TargetData targetData)
    {
        hubDistance = targetData.hubDistance;
        angleToTurn = targetData.angleToTurn;

        isTargetFound = targetData.isTargetFound;
        frameNumber = targetData.frameNumber;

        // DO NOT MODIFY this value.
        isFreshData = true;
 
        // System.out.println(pId + " " + center.x + " " + center.y);
    }

    /**
     * This method returns the target data for the port distance.
     * 
     * @return The port distance.
     */
    public synchronized double getPortDistance()
    {
        return hubDistance;
    }

    /**
     * This method returns the target data for the angle to turn.
     * 
     * @return The angle to turn.
     */
    public synchronized double getAngleToTurn()
    {
        return angleToTurn;
    }

     /**
     * This method returns all of the target data.
     * 
     * @return The target data.
     */
    public synchronized TargetData get()
    {
        TargetData targetData = new TargetData();
        targetData.hubDistance = hubDistance;
        targetData.angleToTurn = angleToTurn;
        targetData.isTargetFound = isTargetFound;
        targetData.frameNumber = frameNumber;
        targetData.isFreshData = isFreshData;

        // Indicate that the data is no longer fresh data.
        // It is not until the set() method gets called that isFreshData changes.
        this.isFreshData = false;

        // System.out.println(pId + " " + center.x + " " + center.y);

        return targetData;
    }

    /**
     * This method increments the frame number of the target data.
     */
    public synchronized void incrFrameNumber()
    {
            frameNumber++;
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

    /**
     * This method converts the data to a string format for output.
     * 
     * @return The string to display.
     */
    public synchronized String toString()
    {
       return String.format("Frame = %d, %s hubDistance = %5.1f, angleToTurn = %5.1f, %s", 
            frameNumber, isTargetFound ? "target" : "no target",
            hubDistance, angleToTurn, isFreshData ? "FRESH" : "stale");
    }
}
/*
Here's how to use the TargetData class.
(If we want to make improvements for next year, this is a good model to start with and tweak.)

In the Vision Process (class VisionProcess{}) define two TargetData objects say targetData and
myTargetData (public TargetData targetData, myTargetData;).

The intention is another thread - TargetSelection - stuffs the
latest data into targetData as fast as it can. This is expected to be relatively slowly as it's
data from the camera.

The Vision using Process pulls the data from targetData into myTargetData.

Use the data in myTargetData

First check if it's fresh data or the same as before
(if(myTargetData.isFresh) do something else it's stale

If myTargetData is fresh then update the angle-to-turn setpoint.
If the myTargetData is not fresh then keep using the old data to keep driving toward the previously set angle.

Vision using Process runs relatively fast and often so mostly it sees that myTargetData is not fresh.


The methods of TargetData are all synchronized so the data cannot be accessed and potentially
corrupted by two threads setting and getting at the same time.  However the get methods for angle and
distance and others like isFresh are separate and there could be an update between getting the angle
and getting the distance thus that data pair would not be atomic in this design if there was only one
object that can be accessed and updated by more than one thread.  That is why there are two copies of
TargetData - targetData and myTargetData so Vision using Process controls when the now atomic object of
myTargetData is updated from targetData.

   // get the latest targeting data every 20ms for my use
    myWorkingCopyOfTargetData = VisionData.targetData.get();

    if(myWorkingCopyOfTargetData.isFreshData)
     // Vision process finally gave me an update.
     // If there is a target found, then update the setpoint for the "drive to" angle
     // or if within your tolerance stop.
     // If aligned, you might want to wait a little bit to settle to make sure
     // the robot is still aligned then take the shot.
     // If target is not found, then the camera doubts it's near.
     // To verify the camera is still working check that the frame count is increasing.
    { // access my working copy without getters since it's mine
      SmartDashboard.putString("vision",
        String.format("%5.1f, %5.1f, %b, %d", // better use toString but that doesn't show variable access I wanted to show
        myWorkingCopyOfTargetData.angleToTurn,
        myWorkingCopyOfTargetData.hubDistance,
        myWorkingCopyOfTargetData.isTargetFound,
        myWorkingCopyOfTargetData.frameNumber));
    }
     // Camera is slow so expect a lot of stale data.
     // Keep moving toward last target if that's what's in progress and
     // has not yet been completed, otherwise, if you have arrived, then stop.
    else
    {

    }
  }

*/
