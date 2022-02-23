package frc.vision;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cscore.CvSink;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class NewAcquireHubImage  implements Runnable
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
  
  public NewImage newImage = new NewImage(); // where a video frame goes for others to use

  private NewTargetSelection newTargetSelection;
  private Thread targetSelectionThread;

  public void run() {

 // start thread for target camera and locate target
 newTargetSelection = new NewTargetSelection(newImage);
 targetSelectionThread = new Thread(newTargetSelection, "TargetSelection");
 targetSelectionThread.setDaemon(true);
 targetSelectionThread.start();

    // The Registered Trademark looks right in VSC but it isn't "/dev/v4l/by-id/usb-Microsoft_Microsoft®_LifeCam_HD-3000-video-index0"
    UsbCamera TargetCamera = new UsbCamera("TargetCamera", "/dev/v4l/by-id/usb-Microsoft_Microsoft\u00AE_LifeCam_HD-3000-video-index0");
    TargetCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    TargetCamera.setResolution(352, 288); // 10fps ok
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

    while (!Thread.interrupted())
    {
  // Tell the CvSink to grab a frame from the camera and put it
      // in the source mat.  If there is an error notify the output.
      if (inputStream.grabFrame(mat, 1.0) == 0) { // 1.0 second timeout; sometimes the 2nd acquire took longer then ok forever
        // Send the output the error
        System.out.println("intake camera error " + inputStream.getError());//intake camera error timed out getting frame
        // skip the rest of the current iteration
        continue;
      }

      // new Image available for use so pass it on
      this.newImage.setImage(mat);

    

    } // the "infinite" loop
  } // end run method
} // end class
    
/* pscp  -v lvuser@roborio-4237-frc.local:/home/lvuser/camerid.txt  c:\\users\\rkt\\cameraid.txt*/
//MjpegServer server = CameraServer.startAutomaticCapture(TargetCamera); // save the server to enable changing quality, etc.
//System.out.println("targetcamera" + TargetCamera.getConfigJson());

// ["640x480 YUYV 30 fps","640x480 YUYV 20 fps","640x480 YUYV 15 fps","640x480 YUYV 10 fps","640x480 YUYV 7 fps","1280x720 YUYV 10 fps","1280x720 YUYV 7 fps","960x544 YUYV 15 fps","960x544 YUYV 10 fps","960x544 YUYV 7 fps","800x448 YUYV 20 fps","800x448 YUYV 15 fps","800x448 YUYV 10 fps","800x448 YUYV 7 fps","640x360 YUYV 30 fps","640x360 YUYV 20 fps","640x360 YUYV 15 fps","640x360 YUYV 10 fps","640x360 YUYV 7 fps","424x240 YUYV 30 fps","424x240 YUYV 20 fps","424x240 YUYV 15 fps","424x240 YUYV 10 fps","424x240 YUYV 7 fps","352x288 YUYV 30 fps","352x288 YUYV 20 fps","352x288 YUYV 15 fps","352x288 YUYV 10 fps","352x288 YUYV 7 fps","320x240 YUYV 30 fps","320x240 YUYV 20 fps","320x240 YUYV 15 fps","320x240 YUYV 10 fps","320x240 YUYV 7 fps","800x600 YUYV 15 fps","800x600 YUYV 10 fps","800x600 YUYV 7 fps","176x144 YUYV 30 fps","176x144 YUYV 20 fps","176x144 YUYV 15 fps","176x144 YUYV 10 fps","176x144 YUYV 7 fps","160x120 YUYV 30 fps","160x120 YUYV 20 fps","160x120 YUYV 15 fps","160x120 YUYV 10 fps","160x120 YUYV 7 fps","1280x800 YUYV 10 fps",
// "640x480 MJPEG 30 fps","640x480 MJPEG 20 fps","640x480 MJPEG 15 fps","640x480 MJPEG 10 fps","640x480 MJPEG 7 fps","1280x720 MJPEG 30 fps","1280x720 MJPEG 20 fps","1280x720 MJPEG 15 fps","1280x720 MJPEG 10 fps","1280x720 MJPEG 7 fps","960x544 MJPEG 30 fps","960x544 MJPEG 20 fps","960x544 MJPEG 15 fps","960x544 MJPEG 10 fps","960x544 MJPEG 7 fps","800x448 MJPEG 30 fps","800x448 MJPEG 20 fps","800x448 MJPEG 15 fps","800x448 MJPEG 10 fps","800x448 MJPEG 7 fps","640x360 MJPEG 30 fps","640x360 MJPEG 20 fps","640x360 MJPEG 15 fps","640x360 MJPEG 10 fps","640x360 MJPEG 7 fps","800x600 MJPEG 30 fps","800x600 MJPEG 20 fps","800x600 MJPEG 15 fps","800x600 MJPEG 10 fps","800x600 MJPEG 7 fps",
// "416x240 MJPEG 30 fps","416x240 MJPEG 20 fps","416x240 MJPEG 15 fps","416x240 MJPEG 10 fps","416x240 MJPEG 7 fps",
// "352x288 MJPEG 30 fps","352x288 MJPEG 20 fps","352x288 MJPEG 15 fps","352x288 MJPEG 10 fps","352x288 MJPEG 7 fps",
// "176x144 MJPEG 30 fps","176x144 MJPEG 20 fps","176x144 MJPEG 15 fps","176x144 MJPEG 10 fps","176x144 MJPEG 7 fps",
// "320x240 MJPEG 30 fps","320x240 MJPEG 20 fps","320x240 MJPEG 15 fps","320x240 MJPEG 10 fps","320x240 MJPEG 7 fps","160x120 MJPEG 30 fps","160x120 MJPEG 20 fps","160x120 MJPEG 15 fps","160x120 MJPEG 10 fps","160x120 MJPEG 7 fps"]