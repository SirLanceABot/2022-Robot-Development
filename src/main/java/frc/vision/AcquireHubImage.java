package frc.vision;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.constants.Constant;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.MjpegServer;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class AcquireHubImage  implements Runnable
{
  // minimal acquire, add a rectangle to one, and display both streams adds about 8% cpu utilization
  // these are named streams 
  // Tell the CameraServer to send to the dashboard for debugging
  // Doubt camera will be streamed to humans during a match but if ti is
  // be sure to reduce the resolution
    
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
  static Object tabLock = new Object(); // synchronizing lock between this program and the related Shuffleboard tab
  static ShuffleboardTab cameraTab;


  public void run() {

    // start thread to process target camera and locate target
    targetSelection = new TargetSelection();
    targetSelectionThread = new Thread(targetSelection, "TargetSelection");
    final int parentPriority = Thread.currentThread().getPriority();
    targetSelectionThread.setPriority(parentPriority-1);
    //  targetSelectionThread.setDaemon(true);
    targetSelectionThread.start();
    System.out.println("parent/child priorities " + Thread.currentThread().getPriority() + " " + targetSelectionThread.getPriority());
    // END start thread to process target camera and locate target


    /********start target camera******* */
    // The Registered Trademark looks right in VSC but it isn't "/dev/v4l/by-id/usb-Microsoft_Microsoft®_LifeCam_HD-3000-video-index0"
    UsbCamera TargetCamera = new UsbCamera("TargetCamera", "/dev/v4l/by-id/usb-Microsoft_Microsoft\u00AE_LifeCam_HD-3000-video-index0");
    // UsbCamera TargetCamera = new UsbCamera("TargetCamera", "usb-USB_Camera_USB_Camera_SN0001-video-index0"); // rkt tenvis
    // UsbCamera TargetCamera = new UsbCamera("TargetCamera", "/dev/video0"); // rkt tenvis
    TargetCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    TargetCamera.setResolution(Constant.targetCameraWidth, Constant.targetCameraHeight); // 10fps ok
    TargetCamera.setFPS(10);
    TargetCamera.setExposureManual(0);
 
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
    /*******END start target camera******* */


    /********start intake camera for human driver******* */
    // has to be after the other camera because only the last one can be in ShuffleBoard
    //  - automatically acquires and passes through the image
    // this camera and server at 160, 120; 20fps; 35 quality adds about 3% cpu (top %CPU for java)
    // set mjpeg stream address at http://roboRIO-4237-FRC.local:1189/?action=stream  bad bad bad had to remove the 1189
    // auto sequence starts at 1181 so use the far end 1189 to avoid conflicts
    UsbCamera IntakeCamera = new UsbCamera("IntakeCamera", "/dev/v4l/by-id/usb-KYE_Systems_Corp._USB_Camera_200901010001-video-index0");
    IntakeCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    IntakeCamera.setResolution(160, 120);
    IntakeCamera.setFPS(30);
    // MjpegServer server = CameraServer.startAutomaticCapture(IntakeCamera);
    // MjpegServer server = CameraServer.addServer("IntakeServer");
    MjpegServer intakeCameraServer = CameraServer.addServer("IntakeServer");
    intakeCameraServer.setSource(IntakeCamera);
    intakeCameraServer.setCompression(35); // 35 about as low as you can go for seeing to drive with low bit rate and little lag
   /********END of start intake camera for human driver******* */


   //start shuffleboard stuff

  // Create the Camera tab on the shuffleboard
  synchronized(tabLock)
  {
      cameraTab = Shuffleboard.getTab("Camera");
  }

  try {
    Thread.sleep(3000); // wait for CameraServer to make the intakeCamera for the ShuffleBoard
  } catch (InterruptedException e) {
    e.printStackTrace();
  }

  // Widget in Shuffleboard Tab
  CameraWidget cw = new CameraWidget();
  cw.name = "IntakeCamera";
  cw.setLocation(0, 0, 10, 14);
  cw.setProperties(false, "white", false, "NONE");
  createCameraShuffleboardWidget(intakeCameraServer.getSource(), cw);

  // END start intake camera

  // Get an angle Calibration from Shuffleboard
      //ShuffleboardTab tab = Shuffleboard.getTab("Camera");
      synchronized(tabLock)
      {
      Vision.calibrate =
          cameraTab.add("Turret Calibration", 0.0)
          .withSize(4, 2)
          .withPosition(5, 22)
          .getEntry();
      
      Shuffleboard.update();
      }

   // end shuffleboard stuff

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

    } // the "infinite" loop
    System.out.println("AcquireHubImage should never be here");
  } // end run method


  public static class CameraWidget {
    public String name;
  
    public int column;
    public int row;
    public int width;
    public int height;
  
    // Name Type Default Value Notes
    // Show crosshair Boolean true Show or hide a cross-hair on the image
    // Crosshair color Color "white" Can be a string or a rgba integer
    // Show controls Boolean true Show or hide the stream controls
    // Rotation String "NONE" Rotates the displayed image. One of ["NONE",
    // "QUARTER_CW", "QUARTER_CCW", "HALF"]
  
    public boolean showCrosshair;
    public String crosshairColor;
    public boolean showControls;
    public String rotation;
  
    public void setLocation(int row, int column, int height, int width) {
        this.column = column;
        this.row = row;
        this.height = height;
        this.width = width;
    }
  
    public void setProperties(boolean showCrosshair, String crosshairColor, boolean showControls, String rotation) {
        this.showCrosshair = showCrosshair;
        this.crosshairColor = crosshairColor;
        this.showControls = showControls;
        this.rotation = rotation;
    }
  }

    
  private static void createCameraShuffleboardWidget(VideoSource camera, CameraWidget cw) {
    // Name Type Default Value Notes
    // ----------------- --------- --------
    // ----------------------------------------------------
    // Show crosshair Boolean true Show or hide a cross-hair on the image
    // Crosshair color Color "white" Can be a string or a rgba integer
    // Show controls Boolean true Show or hide the stream controls
    // Rotation String "NONE" Rotates the displayed image.
    // One of ["NONE", "QUARTER_CW", "QUARTER_CCW", "HALF"]

    Map<String, Object> cameraWidgetProperties = new HashMap<String, Object>();
    cameraWidgetProperties.put("Show crosshair", cw.showCrosshair);
    cameraWidgetProperties.put("Crosshair color", cw.crosshairColor);
    cameraWidgetProperties.put("Show controls", cw.showControls);
    cameraWidgetProperties.put("Rotation", cw.rotation);

    synchronized (tabLock) {
        cameraTab.add(cw.name + " Camera", camera).withWidget(BuiltInWidgets.kCameraStream)
                .withPosition(cw.column, cw.row).withSize(cw.width, cw.height)
                .withProperties(cameraWidgetProperties);
        Shuffleboard.update(); // FIXME this is failing sometimes on startup and robot code has to be restarted
    }
}

} // end class
    
// parking lot for good junk
/* pscp  -v lvuser@roborio-4237-frc.local:/home/lvuser/camerid.txt  c:\\users\\rkt\\cameraid.txt*/
//MjpegServer server = CameraServer.startAutomaticCapture(TargetCamera); // save the server to enable changing quality, etc.
//System.out.println("targetcamera" + TargetCamera.getConfigJson());

// import edu.wpi.first.wpilibj.Watchdog;
    // // restart vision if any watchdog tripped; Exit - should restart itself by WPILib server stuff
    // static Watchdog watchdog = new Watchdog(2., () -> {
    //   // // redirect standard error output to a file and append if it exists
    //   // try {
    //   //     System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream("/home/pi/VisionErrors.txt", true)), true));
    //   // } catch (FileNotFoundException e) {
    //   //     e.printStackTrace();
    //   // }
    //   var watchdogMsg = "Watchdog barked for AcquireHubImage - exiting";
    //   System.out.println(watchdogMsg);
    //   // System.exit(1);
    // });

//    watchdog.enable();
// watchdog.reset(); // made it to the end fo the loop
