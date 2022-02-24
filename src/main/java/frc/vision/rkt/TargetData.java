package frc.robot;

// import com.google.gson.Gson;
import org.opencv.core.Point;
import org.opencv.core.Size;

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
    Size imageSize;
    double portPositionInFrame;
    double portDistance;
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
        imageSize = new Size(-1.0, -1.0);
        portPositionInFrame = -1.0;
        portDistance = -1.0;
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
        imageSize.width = targetData.imageSize.width;
        imageSize.height = targetData.imageSize.height;
        portPositionInFrame = targetData.portPositionInFrame;
        portDistance = targetData.portDistance;
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
        return portDistance;
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
        targetData.imageSize.width = imageSize.width;
        targetData.imageSize.height = imageSize.height;
        targetData.portPositionInFrame = portPositionInFrame;
        targetData.portDistance = portDistance;
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

    public synchronized Size getImageSize()
    {
        return imageSize;
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
       return String.format("Frame = %d, %s\nimageSize.width = %f, imageSize.height = %f,\nportPositionInFrame = %f, portDistance = %f,\nangleToTurn = %f %s", 
            frameNumber, isTargetFound ? "target" : "no target",
            imageSize.width, imageSize.height, portPositionInFrame, portDistance, angleToTurn, isFreshData ? "FRESH" : "stale");
    }
}
/*
Here's how to use the TargetData class.
(If we want to make improvements for next year, this is a good model to start with and tweak.)

In the Vision Process (class VisionProcess{}) define two TargetData objects say TargetData and
newTargetData (public TargetData TargetData, newTargetData;).

The intention is another thread - TargetSelection - stuffs the
latest data into targetData as fast as it can. This is expected to be relatively slowly as it's
data from the camera.

The Vision Process pulls the data from newTargetData into TargetData.

Use the data in targetData

First check if it's fresh data or the same as before
(if(targetData.isFresh()) do something else it's stale

If the targetData is not fresh then keep using the old data or skip that loop.

Check if there is target data to be used
(if(TargetData.isTargetFound())
 {double a=targetDataTemp.getAngleToTurn(); double d = targetDataTemp.getPortDistance();}
else no target data

Vision Process runs relatively fast and often so mostly it sees that TargetData is not fresh.


The methods of TargetData are all synchronized so the data cannot be accessed and potentially
corrupted by two threads setting and getting at the same time.  However the get methods for angle and
distance and others like isFresh are separate and there could be an update between getting the angle
and getting the distance thus that data pair would not be atomic in this design if there was only one
object that can be accessed and updated by more than one thread.  That is why there are two copies of
TargetData - targetData and targetDataTemp so Vision Process controls when the now atomic object of
TargetData is updated from newTargetData.

*/
