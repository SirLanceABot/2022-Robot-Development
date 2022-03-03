package frc.vision;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision
{

  private static AcquireHubImage target;
  private static Thread targetThread;
  private TargetData myWorkingCopyOfTargetData; // Robot's working copy
  static NetworkTableEntry calibrate;
  static double calibrateAngle = 0.;

  public Vision() 
  {
  // start thread for target camera and locate target
  target = new AcquireHubImage();
  targetThread = new Thread(target, "TargetCamera");
  targetThread.setDaemon(true);
  targetThread.start();
  // END start target camera and locate target  
        
  Timer.delay(5.);
  
  }

  public void getCalibration()
  {
    // this is way faster than needed - put in slow addPeriodic?
    if(calibrate != null)
      calibrateAngle = calibrate.getDouble(0.0); // get the camera calibration from the Shuffleboard


    if(true)  //FIXME: this example here just to test - set to false for real Robot usage
    {
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
  }
}
// parking lot for junk
// UsbCamera IntakeCamera = CameraServer.startAutomaticCapture("intake", "/dev/v4l/by-id/usb-KYE_Systems_Corp._USB_Camera_200901010001-video-index0");
// MjpegServer server = CameraServer.startAutomaticCapture(IntakeCamera);
//System.out.println("intakecamera" + IntakeCamera.getConfigJson());

//   // start intake camera for human driver - automatically acquires and passes through the image
  //   // this camera and server at 160, 120; 20fps; 35 quality adds about 3% cpu (top %CPU for java)
  //   // set mjpeg stream address at http://roboRIO-4237-FRC.local:1189/?action=stream
  //   // auto sequence starts at 1181 so use the far end to avoid conflicts
  //   UsbCamera IntakeCamera = new UsbCamera("IntakeCamera", "/dev/v4l/by-id/usb-KYE_Systems_Corp._USB_Camera_200901010001-video-index0");
  //   IntakeCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
  //   IntakeCamera.setResolution(160, 120);
  //   IntakeCamera.setFPS(20);
  //   // MjpegServer server = 
  //   CameraServer.startAutomaticCapture(IntakeCamera);
  //   // MjpegServer server = CameraServer.addServer("IntakeServer");
  //   // MjpegServer server = CameraServer.addServer("IntakeServer", 1189);
  //   // server.setSource(IntakeCamera);
  //   // server.setCompression(35); // 35 about as low as you can go for seeing to drive with low bit rate and little lag
    
  //      // Get a CvSink. This will capture Mats from the camera
  //      CvSink inputStream = new CvSink("TargetCvSink");
  //      inputStream.setSource(IntakeCamera);
  //  Mat mat = new Mat();
  // // Tell the CvSink to grab a frame from the camera and put it
  //     // in the source mat.  If there is an error notify the output.
  //     if (inputStream.grabFrame(mat, 1.0) == 0) { // 1.0 second timeout; sometimes the 2nd acquire took longer then ok forever after
  //       // Send the output the error
  //       System.out.println("input camera error " + inputStream.getError());//intake camera error timed out getting frame
  //       // skip the rest of the current iteration
  //     }     


  // CPU utilization stress test for Robot.teleopPeriodic
  

  // var StartTime = Timer.getFPGATimestamp();
  // for(int Mathsine = 1; Mathsine <= 1_200_000; Mathsine ++)
  // {
  //     Math.sin(Math.E/Math.PI);
  // }
  // var StopTime = Timer.getFPGATimestamp();
  // SmartDashboard.putNumber("teleopTimer 0.02", StopTime - StartTime);

