package frc.vision;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

public class AcquireTarget  implements Runnable{
  private VideoSource camera;
  	// This object is used to send the image to the Dashboard
	private CvSource outputStream;
	// This object is used to capture frames from the camera.
	// The captured image is stored to a Mat
	private CvSink inputStream;
  private GripPipeLine gripPipeLine = new GripPipeLine();


    public void run() {
    // UsbCamera TargetCamera = new UsbCamera("target", "/dev/v4l/by-id/usb-Microsoft_Microsoft®_LifeCam_HD-3000-video-index0");

      UsbCamera TargetCamera = new UsbCamera("target", "/dev/video0");
      TargetCamera.setResolution(160, 120);
      TargetCamera.setFPS(20);
      TargetCamera.setExposureManual(0);

        // Get the UsbCamera from CameraServer
        // MjpegServer server = CameraServer.startAutomaticCapture(TargetCamera);

        outputStream = CameraServer.putVideo("TurretContours", 160, 120);
        outputStream.setFPS(7);

        // Get a CvSink. This will capture Mats from the camera
        inputStream = new CvSink("cvsink");
        inputStream.setSource(TargetCamera);

        // Mats are very memory expensive. Lets reuse this Mat.
        Mat mat = new Mat();

        // This cannot be 'true'. The program will never exit if it is. This
        // lets the robot stop this thread when restarting robot code or
        // deploying.
        while (!Thread.interrupted()) {
          // Tell the CvSink to grab a frame from the camera and put it
          // in the source mat.  If there is an error notify the output.
          if (inputStream.grabFrame(mat) == 0) {
            // Send the output the error.
            outputStream.notifyError(inputStream.getError());
            // skip the rest of the current iteration
            continue;
          }
          gripPipeLine.process(mat);
          // Put a rectangle on the image
          Imgproc.rectangle(
              mat, new Point(10, 10), new Point(100, 100), new Scalar(255, 255, 255), 5);
          // Give the output stream a new image to display
          outputStream.putFrame(mat);
        }
      }
    }