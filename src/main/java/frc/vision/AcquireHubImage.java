package frc.vision;

/*

  This class starts the bulk of the camera processing.

  It starts the Target camera server, the human intake camera server, defines the
  ShuffleBoard layout for Vision information and then creates its child thread
  TargetSelection to process the target camera image which identifies the target
  and measures the target location (angle, distance).

  This class then enters an "infinite" loop to acquire target camera images.

*/

import java.lang.invoke.MethodHandles;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cscore.CameraServerJNI;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import frc.constants.Constant;

public class AcquireHubImage  implements Runnable
{
  private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static
  {
      System.out.println("Loading: " + fullClassName);
  }

  static
  {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  // set this display to false for match play to save Ethernet bandwidth
  // Probably put in config parameters such Constants class or SmartDashboard inputs
  boolean displayTargetCamera = true;

	// This object is used to capture frames from the camera. The captured image is stored to a Mat
	private CvSink inputStream;
  
  public static Image image = new Image(); // where a video frame goes for others to use

  private TargetSelection targetSelection;
  private Thread targetSelectionThread;

  public void run() {

    /********start target camera******* */
    /*                                  */
    // The Registered Trademark looks right in VSC but it isn't "/dev/v4l/by-id/usb-Microsoft_Microsoft®_LifeCam_HD-3000-video-index0"
    UsbCamera TargetCamera = new UsbCamera("TargetCamera", "/dev/v4l/by-id/usb-Microsoft_Microsoft\u00AE_LifeCam_HD-3000-video-index0");
    TargetCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    TargetCamera.setResolution(Constant.targetCameraWidth, Constant.targetCameraHeight); // 10fps ok
    TargetCamera.setFPS(10);
    TargetCamera.setWhiteBalanceManual(4800); // around green
    TargetCamera.setBrightness(0);
    TargetCamera.setExposureManual(0); // This is a critical camera parameter set here.
    int cameraHandle = TargetCamera.getHandle();
    CameraServerJNI.setProperty(CameraServerJNI.getSourceProperty(cameraHandle, "contrast"), 100);
    CameraServerJNI.setProperty(CameraServerJNI.getSourceProperty(cameraHandle, "saturation"), 100);

    // The first image filter is the camera exposure is set to nearly "off" so only the very
    // brightest objects leave the camera.  This assumes a bright light is illuminating the
    // target and nothing else.  Retro-reflective tape and a headlight are used to do this.
  
    // Tell the CameraServer to send to the dashboard for debugging
    if(displayTargetCamera)
    {
      CameraServer.startAutomaticCapture(TargetCamera);
    }

    // Get a CvSink. This will capture Mats from the camera
    inputStream = new CvSink("TargetCvSink");
    inputStream.setSource(TargetCamera);
    
    // Mats are very memory expensive. Lets reuse this Mat.
    Mat mat = new Mat();
    /*                                     */
    /*******END start target camera******* */


    /********start intake camera for human driver******* */
    /*                                                   */
    // has to be after the other camera because first one is lost - BUG??
    //  - automatically acquires and passes through the image
    // mjpeg stream address at http://roboRIO-4237-FRC.local:118x/?action=stream
    // x auto sequence starts at 1  (port 1181)
    
    UsbCamera IntakeCamera = new UsbCamera("IntakeCamera", "/dev/v4l/by-id/usb-KYE_Systems_Corp._USB_Camera_200901010001-video-index0");
    IntakeCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    IntakeCamera.setResolution(Constant.intakeCameraWidth, Constant.intakeCameraHeight);
    IntakeCamera.setFPS(30);
   
    // MjpegServer intakeCameraServer = CameraServer.startAutomaticCapture(IntakeCamera); // why not use this one?  can't remember
    MjpegServer intakeCameraServer = CameraServer.addServer("IntakeServer");
    intakeCameraServer.setSource(IntakeCamera);
    intakeCameraServer.setCompression(35); // 35 about as low as you can go for seeing to drive with low bit rate and little lag
    /*                                                          */
    /********END of start intake camera for human driver******* */


    /*****start shuffleboard stuff*****/
    /*                                */
    // Intake Camera Widget in Shuffleboard Tab
    synchronized(Vision.tabLock)
    {
      CameraWidget cw = new CameraWidget(Vision.cameraTab);
      cw.name("Intake");
      cw.setLocation(0, 0, 13, 14);
      cw.setProperties(false, "white", false, "NONE");
      cw.createCameraShuffleboardWidget(intakeCameraServer.getSource());
      
      Shuffleboard.update();
    }

    // Get an angle Calibration from Shuffleboard
    synchronized(Vision.tabLock)
    {
      Vision.calibrate =
          Vision.cameraTab.add("Turret Calibration", 0.0)
          .withSize(4, 2)
          .withPosition(23, 9)
          .withSize(4, 3)
          .getEntry();
      
      Shuffleboard.update();
    }
    /*                              */
    /*****END shuffleboard stuff*****/

  
    // start thread to start target camera and process to locate target
    targetSelection = new TargetSelection();
    targetSelectionThread = new Thread(targetSelection, "TargetSelection");
    final int parentPriority = Thread.currentThread().getPriority();
    targetSelectionThread.setPriority(parentPriority-1);
    targetSelectionThread.setDaemon(true);
    targetSelectionThread.start();
    // END start thread to process target camera and locate target


    // infinite loop to acquire target camera images
    while (!Thread.interrupted())
    {
      // Tell the CvSink to grab a frame from the camera and put it
      // in the source mat.  If there is an error notify the output.
      if (inputStream.grabFrame(mat, 1.0) == 0) { // 1.0 second timeout; sometimes the 2nd acquire took longer then ok forever after
        // Send the output the error
        System.out.println("target camera error " + inputStream.getError());//intake camera error timed out getting frame
        // skip the rest of the current iteration
        continue;
      }

      if (mat == null) // threads start at different times so skip problems that might happen at the beginning
      {
          System.out.println("target camera skipping null mat");
          continue;
      }
      
      if (mat.empty()) // threads start at different times so skip problems that might happen at the beginning
      {
          System.out.println("target camera skipping empty mat");
          continue;
      }

      // new Image available for use so pass it on
      image.setImage(mat);

    } // the "infinite" loop acquiring target camera images

    System.out.println("AcquireHubImage should never be here");
  
  } // end run method
} // end class
    
// parking lot for good junk
/* pscp  -v lvuser@roborio-4237-frc.local:/home/lvuser/camerid.txt  c:\\users\\rkt\\cameraid.txt*/
