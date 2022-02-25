package frc.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSource;

public class TargetSelection implements Runnable {

    // set this display to false for match play to save Ethernet bandwidth
  // Probably put in config parameters such Constants class or SmartDashboard inputs
  boolean displayTargetContours = true;

  private static final double VERTICAL_CAMERA_ANGLE_OF_VIEW = 35.0;

  // This object is used to send the OpenCV processed image to the Dashboard if desired
    private CvSource outputStream;
    
  GripPipeline gripPipeline = new GripPipeline();

  TargetData nextTargetData = new TargetData();

//   Image image;

//   public TargetSelection(Image image)
//   {
//       this.image = image;
//   }

public void run()
    {
    if(displayTargetContours)
    {
      outputStream = CameraServer.putVideo("TargetContours", 352, 288);
      outputStream.setFPS(7);
    }
 
    // Mats are very memory expensive. Lets reuse this Mat.
    Mat mat = new Mat();

    // loop to grab camera image into a Mat, process the Mat, put an image to be viewed if desired

    // This cannot be 'true'. The program will never exit if it is. This lets
    // the robot stop this thread when restarting robot code or deploying.
    while (!Thread.interrupted())
    {
    // System.gc();
      /* YOUR IMAGE PROCESSING GOES HERE */
      // find the target
      // compute the angle to turn to align and the distance
      // send 4 pieces of data to whatever is driving this beast
      // angle to turn, distance to target, frame sequence number, time of acquisition

      // Demo OpenCV draw a rectangle on the image
      // Imgproc.rectangle(mat, new Point(10, 10), new Point(100, 100), new Scalar(255, 255, 255), 5);
      // END Demo OpenCV draw a rectangle on the image

      // System.out.print("[ts" + Timer.getFPGATimestamp()); // doesn't print after a few seconds but no watchdog msg
                                                          //this.image.getImage(mat);
      AcquireHubImage.image.getImage(mat);
      
      // System.out.println("got it]");

      if (mat == null) // threads start at different times so skip problems that might happen at the beginning
      {
          System.out.println("Skipping null mat");
          continue;
      }
      
      if (mat.empty()) // threads start at different times so skip problems that might happen at the beginning
      {
          System.out.println("Skipping empty mat");
          continue;
      }

    // Reset the next target data and bump up the frame number
    nextTargetData.reset();
    nextTargetData.incrFrameNumber();

/* demo GRIP pipeline */
      gripPipeline.process(mat); // filter the image with the GRIP pipeline

      // The gripPowerPortVisionPipeline creates an array of contours that must
      // be searched to find the target.
      ArrayList<MatOfPoint> filteredContours;
      filteredContours = new ArrayList<MatOfPoint>(gripPipeline.filterContoursOutput());

      
      int contourIndex = -1; // initialize here - using same value to indicate no contours and count contours
      int contourIndexBest = -1;

      // Check if no contours were found in the camera frame.
      if (filteredContours.isEmpty()) {
          // Display a message if no contours are found.
          Imgproc.putText(mat, "No Contours", new Point(20, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
                  new Scalar(255, 255, 255), 1);
          nextTargetData.portDistance = -1.;
          nextTargetData.angleToTurn = -1.;
          nextTargetData.isFreshData = true;
          nextTargetData.isTargetFound = false;
      } else {
          // contours were found
          if (filteredContours.size() > 1) { // not very good if more than one contour
              System.out.println(filteredContours.size() + " Contours found");
          }

          Rect boundRect = null; // upright rectangle

              // Draw all contours at once (negative index).
              // Positive thickness means not filled, negative thickness means filled.
              Imgproc.drawContours(mat, filteredContours, -1, new Scalar(255, 0, 0), 1);

          // Loop through all contours and just remember the best one

          for (MatOfPoint contour : filteredContours) {
              contourIndex++;

              // debug output Print all the contours

              // System.out.println("Contour Index = " + contourIndex);
              // System.out.println(contour.dump()); // OpenCV Mat dump one line string of numbers
              // or more control over formatting with your own array to manipulate
              // System.out.println(pId + " " + aContour.size() + " points in contour"); // a contour is a bunch of points
              // convert MatofPoint to an array of those Points and iterate (could do list of Points but no need for this)
              // for(Point aPoint : aContour.toArray())System.out.print(" " + aPoint); // print each point

              // for(int idx = 0; idx < contour.toArray().length; idx++)
              // {
              // System.out.println("(" + contour.toArray()[idx].x + ", " +
              // contour.toArray()[idx].y + ")");
              // }

              { // create angled bounding rectangle - could use angle to double check position
              RotatedRect rotatedRect;
              MatOfPoint2f NewMtx = new MatOfPoint2f(contour.toArray());
              rotatedRect = Imgproc.minAreaRect(NewMtx);
              NewMtx.release();
              Point[] boxPts = new Point[4];
              rotatedRect.points(boxPts);

              List<MatOfPoint> listMidContour = new ArrayList<MatOfPoint>();
              listMidContour.add(new MatOfPoint(boxPts[0], boxPts[1], boxPts[2], boxPts[3]));

              Imgproc.polylines(mat, // Matrix obj of the image
                  listMidContour, // java.util.List<MatOfPoint> pts
                  true, // isClosed
                  new Scalar(0, 255, 255), // Scalar object for color
                  1, // Thickness of the line
                  Imgproc.LINE_4 // line type
                  );
              
              Imgproc.putText(mat,
                  String.format("%4.0f", rotatedRect.angle),
                  boxPts[0],
                  Imgproc.FONT_HERSHEY_SIMPLEX, 0.3,
                  new Scalar(255, 255, 255), 1);

              while(!listMidContour.isEmpty()) {
                  listMidContour.get(0).release();
                  listMidContour.remove(0);
              }
              }

              // Create a bounding upright rectangle for the contour's points
              MatOfPoint2f NewMtx = new MatOfPoint2f(contour.toArray());
              boundRect = Imgproc.boundingRect(NewMtx);
              NewMtx.release();
              
              // Draw a Rect, using lines, that represents the Rect
              Point boxPts[] = new Point[4];
              boxPts[0] = boundRect.tl();
              boxPts[1] = new Point(boundRect.br().x, boundRect.tl().y);
              boxPts[2] = boundRect.br();
              boxPts[3] = new Point(boundRect.tl().x, boundRect.br().y);

              // draw edges of bounding rectangle
              List<MatOfPoint> listMidContour = new ArrayList<MatOfPoint>();
              listMidContour.add(new MatOfPoint(boxPts[0], boxPts[1], boxPts[2], boxPts[3]));

              Imgproc.polylines(mat, // Matrix obj of the image
                      listMidContour, // java.util.List<MatOfPoint> pts
                      true, // isClosed
                      new Scalar(0, 255, 255), // Scalar object for color
                      1, // Thickness of the line
                      Imgproc.LINE_4 // line type
                      );


            // do something to set targetData - this is nonsense but illustrative
            nextTargetData.angleToTurn = boxPts[0].x;
            nextTargetData.portDistance = boxPts[0].y;


            // if (compare[HuCompareNormalizationMethod] <= shapeMatch || compareR[HuCompareNormalizationMethod] <= shapeMatch) {
            // // the if(=) case covers if only one contour
            // // or if optional shape matching wasn't run in which case the last contour wins

            // // save new best contour\
            // shapeMatch = Math.min(compare[HuCompareNormalizationMethod], compareR[HuCompareNormalizationMethod]);
            // contourIndexBest = contourIndex;

            // // Find the corner points of the bounding rectangle and the image size
            // nextTargetData.boundingBoxPts[0] = boxPts[0];
            // nextTargetData.boundingBoxPts[1] = boxPts[1];
            // nextTargetData.boundingBoxPts[2] = boxPts[2];
            // nextTargetData.boundingBoxPts[3] = boxPts[3];
            // nextTargetData.imageSize.width = mat.width();
            // nextTargetData.imageSize.height = mat.height();
            // nextTargetData.portPositionInFrame = 0.0;

            // // Find the degrees to turn the turret by finding the difference between the
            // // horizontal center of the camera frame
            // // and the horizontal center of the target.
            // // calibrateAngle is the difference between what the camera sees as the
            // // retroreflective tape target and where
            // // the Power Cells actually hit - the skew of the shooting process or camera
            // // misalignment.
            // nextTargetData.angleToTurn = (VERTICAL_CAMERA_ANGLE_OF_VIEW / nextTargetData.imageSize.height)
            //         * ((nextTargetData.imageSize.height / 2.0)
            //                 - ((nextTargetData.boundingBoxPts[1].y + nextTargetData.boundingBoxPts[2].y) / 2.0))
            //         + Main.calibrateAngle;

            // if (nextTargetData.angleToTurn <= -VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
            //         || nextTargetData.angleToTurn >= VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { // target not actually "seen"
            //                                                                                 // after the calibrateAngle
            //                                                                                 // offset was applied
            //     nextTargetData.portDistance = -1.;
            //     nextTargetData.angleToTurn = -1.;
            //     nextTargetData.isFreshData = true;
            //     nextTargetData.isTargetFound = false;
            // } else { // target still in view
            //     Main.angleHistory.addLast(nextTargetData.angleToTurn); // save old angles for debugging insights
            //     nextTargetData.portDistance = pixelsToInchesTable.lookup(boundRect.br().x);
            //     nextTargetData.isFreshData = true;
            //     nextTargetData.isTargetFound = true;
            //     Main.LOGGER.log(Level.FINE, "pixels:" + boundRect.br().x + ", LUT inches:"
            //                 + nextTargetData.portDistance);
            // }
            // }
            } // end loop through all contours
          } // end contours found
/* END demo GRIP pipeline */

      // update the target information with best contour or the initialized no contour data
        Vision.targetData.set(nextTargetData);

        if(displayTargetContours)
        {
          outputStream.putFrame(mat); // Give the output stream a new image to display
        }

    }    // end "infinite" loop
    System.out.println("TargetSelection should never be here");
    } // end run method
} // end class
// parking lot for good junk

// import edu.wpi.first.wpilibj.Watchdog;
    // // restart vision if any watchdog tripped; Exit - should restart itself by WPILib server stuff
    // static Watchdog watchdog = new Watchdog(2., () -> {
    //   // // redirect standard error output to a file and append if it exists
    //   // try {
    //   //     System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream("/home/pi/VisionErrors.txt", true)), true));
    //   // } catch (FileNotFoundException e) {
    //   //     e.printStackTrace();
    //   // }
    //   var watchdogMsg = "Watchdog barked for TargetSelection - exiting";
    //   System.out.println(watchdogMsg);
    //   // System.exit(1);
    //   });
  
    // watchdog.enable();

    // watchdog.reset(); // made it to the end fo the loop
