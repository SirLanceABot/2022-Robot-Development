package frc.vision;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;

public class Vision extends TimedRobot {

  private static AcquireHubImage target;
  private static Thread targetThread;
  static public TargetData targetData = new TargetData(); // get data from here
  private TargetData targetDataTemp; // Robot's working copy
  static NetworkTableEntry calibrate;
  static double calibrateAngle = 0.;

  @Override
  public void robotInit() {

    // here for this test because I don't have the whole robot code - need it somewhere
    //////////// NAVX GYRO FOR USB LOAD TESTING ////////////
    AHRS ahrs;
    ahrs = new AHRS(SerialPort.Port.kUSB1, SerialDataType.kProcessedData, (byte) 60);
    Timer.delay(1.0); // make sure AHRS USB communication is done before doing other USB.
    ahrs.enableLogging(true);
    System.out.println("NavX update " + ahrs.getActualUpdateRate() + " " + ahrs.getUpdateCount() + " " + ahrs.getRequestedUpdateRate());
    // END NAVX GYRO

  // start thread for target camera and locate target
  target = new AcquireHubImage();
  targetThread = new Thread(target, "TargetCamera");
  targetThread.setDaemon(true);
  targetThread.start();
  // END start target camera and locate target  
        
  Timer.delay(5.);

  
  }
  @Override
  public void robotPeriodic()
  {
    // this is way faster than needed - put in slow addPeriodic?
    if(calibrate != null)
      calibrateAngle = calibrate.getDouble(0.0); // get the camera calibration from the Shuffleboard


     // get the latest targeting data
    targetDataTemp = targetData.get();
    if(targetDataTemp.isFreshData)
    {
      SmartDashboard.putString("vision", targetDataTemp.angleToTurn + " " +
       targetDataTemp.portDistance + " " + Timer.getFPGATimestamp() + " " +
       targetDataTemp.frameNumber);
       SmartDashboard.putBoolean("vision stale", false);
    }
    else
    {
      SmartDashboard.putBoolean("vision stale", true);
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
