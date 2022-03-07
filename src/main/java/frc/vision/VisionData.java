package frc.vision;

public class VisionData 
{
    public static final TargetData targetData = new TargetData(); // others get data from here
}

    // Example of how to access the Vision targeting data

    // private TargetData myWorkingCopyOfTargetData; // Robot's working copy

    // // get the latest targeting data every 20ms for my use
    // myWorkingCopyOfTargetData = VisionData.targetData.get();

    // if(myWorkingCopyOfTargetData.isFreshData)
    //  // Vision process finally gave me an update.
    //  // If there is a target found, then update the setpoint for the "drive to" angle
    //  // or if within your tolerance stop.
    //  // If aligned, you might want to wait a little bit to settle to make sure
    //  // the robot is still aligned then take the shot.
    //  // If target is not found, then the camera doubts it's near.
    //  // To verify the camera is still working check that the frame count is increasing.
    // { // access my working copy without getters since it's mine
    //   SmartDashboard.putString("vision",
    //     String.format("%5.1f, %5.1f, %b, %d", // better use toString but that doesn't show variable access I wanted to show
    //     myWorkingCopyOfTargetData.angleToTurn,
    //     myWorkingCopyOfTargetData.hubDistance,
    //     myWorkingCopyOfTargetData.isTargetFound,
    //     myWorkingCopyOfTargetData.frameNumber));
    // }
    //  // Camera is slow so expect a lot of stale data.
    //  // Keep moving toward last target if that's what's in progress and
    //  // has not yet been completed, otherwise, if you have arrived, then stop.
    // else
    // {

    // }