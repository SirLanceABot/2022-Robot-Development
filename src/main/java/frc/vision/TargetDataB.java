package frc.vision;

// import com.google.gson.Gson;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 * This class is used to store the target data. The user MUST MODIFY the
 * process() method. The user must create a new GripPipeline class using GRIP,
 * modify the TargetSelection class, and modify this class.
 * 
 * @author FRC Team 4237
 * @version 2019.01.28.14.20
 */
public class TargetDataB
{
    private static final String className = new String("[TargetDataB]");
    
    // Static Initializer Block
    static
    {
        System.out.println(className + " : Class Loading");
    }

    private static final String pId = new String("[TargetDataB]");

    // NOTE: No modifier means visible to both the class and package.

    // Target data that we need
    Point[] boundingBoxPts;
    Size imageSize;
    double portPositionInFrame, portDistance;
    double angleToTurn;

    Point center;
    Size size;
    double angle;
    public double fixedAngle;

    // These fields are used to track the validity of the data.
    int frameNumber; // Number of the camera frame

    // Variable isFreshData means whether the roboRIO has seen the current data from the Raspberry Pi already.
    boolean isFreshData; // Is the data fresh?
    boolean isTargetFound;

    /**
     * Default contructor - resets all of the target data.
     */
    public TargetDataB()
    {
        System.out.println(className + " : Constructor Started");

        boundingBoxPts = new Point[4];
        reset();
        frameNumber = 0;

        System.out.println(className + ": Constructor Finished");
    }

    /**
     * This method resets all of the target data, except the frameNumber. The user MUST MODIFY.
     */
    public synchronized void reset()
    {
        boundingBoxPts[0] = new Point(-1.0, -1.0);
        boundingBoxPts[1] = new Point(-1.0, -1.0);
        boundingBoxPts[2] = new Point(-1.0, -1.0);
        boundingBoxPts[3] = new Point(-1.0, -1.0);
        imageSize = new Size(-1.0, -1.0);
        portPositionInFrame = -1.0;
        portDistance = -1.0;
        angleToTurn = 0;
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
    public synchronized void set(TargetDataB targetData)
    {
        boundingBoxPts[0].x = targetData.boundingBoxPts[0].x;
        boundingBoxPts[0].y = targetData.boundingBoxPts[0].y;
        boundingBoxPts[1].x = targetData.boundingBoxPts[1].x;
        boundingBoxPts[1].y = targetData.boundingBoxPts[1].y;
        boundingBoxPts[2].x = targetData.boundingBoxPts[2].x;
        boundingBoxPts[2].y = targetData.boundingBoxPts[2].y;
        boundingBoxPts[3].x = targetData.boundingBoxPts[3].x;
        boundingBoxPts[3].y = targetData.boundingBoxPts[3].y;
        imageSize.width = targetData.imageSize.width;
        imageSize.height = targetData.imageSize.height;
        portPositionInFrame = targetData.portPositionInFrame;
        portDistance = targetData.portDistance;
        angleToTurn = targetData.angleToTurn;

        // center.x = targetData.center.x;
        // center.y = targetData.center.y;
        // size.width = targetData.size.width;
        // size.height = targetData.size.height;
        // angle = targetData.angle;
        // fixedAngle = targetData.fixedAngle;
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
        // isFreshData = false;
        return angleToTurn;
    }

     /**
     * This method returns all of the target data.
     * 
     * @return The target data.
     */
    public synchronized TargetDataB get()
    {
        TargetDataB targetData = new TargetDataB();
        targetData.boundingBoxPts[0].x = boundingBoxPts[0].x;
        targetData.boundingBoxPts[0].y = boundingBoxPts[0].y;
        targetData.boundingBoxPts[1].x = boundingBoxPts[1].x;
        targetData.boundingBoxPts[1].y = boundingBoxPts[1].y;
        targetData.boundingBoxPts[2].x = boundingBoxPts[2].x;
        targetData.boundingBoxPts[2].y = boundingBoxPts[2].y;
        targetData.boundingBoxPts[3].x = boundingBoxPts[3].x;
        targetData.boundingBoxPts[3].y = boundingBoxPts[3].y;
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

    public synchronized Point[] getBoundingBoxPts()
    {
        return boundingBoxPts;
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

    public synchronized void fromJson(String message)
    {
        // TargetDataB temp = new Gson().fromJson(message, TargetDataB.class);
        // set(temp);
    }

    // public synchronized String toJson()
    {
        // Gson gson = new Gson(); // Or use new GsonBuilder().create();
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
       return String.format("Frame = %d, %s, boundingBoxPts = [{%f, %f}, {%f, %f}, {%f, %f}, {%f, %f}],\nimageSize.width = %f, imageSize.height = %f,\nportPositionInFrame = %f, portDistance = %f,\nangleToTurn = %f %s", 
            frameNumber, isTargetFound ? "target" : "no target",
            boundingBoxPts[0].x, boundingBoxPts[0].y, boundingBoxPts[1].x, boundingBoxPts[1].y,
            boundingBoxPts[2].x, boundingBoxPts[2].y, boundingBoxPts[3].x, boundingBoxPts[3].y,
            imageSize.width, imageSize.height, portPositionInFrame, portDistance, angleToTurn, isFreshData ? "FRESH" : "stale");
    }
}