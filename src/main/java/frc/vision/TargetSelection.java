package frc.vision;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
// import org.opencv.core.CvException;
// import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.VideoMode;
// import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.constants.Constant;

public class TargetSelection implements Runnable {

  private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static
  {
      System.out.println("Loading: " + fullClassName);
  }

  // set this display to false for match play to save Ethernet bandwidth
  // Probably put in config parameters such Constants class or SmartDashboard inputs
  boolean displayTargetContours = true;

  // This object is used to send the OpenCV processed image to the Dashboard if desired
  private CvSource outputStream;
    
  GripPipeline gripPipeline = new GripPipeline();

  TargetData nextTargetData = new TargetData();

public void run()
    {
    if(displayTargetContours)
    {
      // define our generated image the same size as the camera but rotated - doesn't have to be but easier
      
      // same as putVideo but we need the server returned instead of the source
      //outputStream = CameraServer.putVideo("TargetContours", Constant.targetCameraWidth, Constant.targetCameraHeight);

      outputStream = new CvSource("TargetContours", VideoMode.PixelFormat.kMJPEG,
                      Constant.targetCameraHeight, Constant.targetCameraWidth, 30);

      MjpegServer openCVserver = CameraServer.startAutomaticCapture(outputStream);
      
      // Widget in Shuffleboard Tab
      synchronized(Vision.tabLock)
      {
        CameraWidget cw = new CameraWidget(Vision.cameraTab);
        cw.name("TargetContours");
        cw.setLocation(0, 14, 13, 9);
        cw.setProperties(false, "white", false, "NONE");
        cw.createCameraShuffleboardWidget(openCVserver.getSource());
        
        Shuffleboard.update();
      }
    }

    // Pixels to Units Data Table Lookup copied from Constant
    // allocate fixed size array with parameter at least as large as the number
    // of data points - minimum of 2 points
    // Notice the LUT CTOR argument is maximum table size - change it if it needs to be larger
    LUT pixelsToUnitsTable = new LUT(10);

    for(int i = 0; i < Constant.pixelsToUnitsTable.length; i++)
    {
      pixelsToUnitsTable.add(Constant.pixelsToUnitsTable[i][0], Constant.pixelsToUnitsTable[i][1]);
    }
    System.out.println("[Camera Pixels To Units To Hub] " + pixelsToUnitsTable); // print the whole table

    // Mats are very memory expensive. Lets reuse this Mat.
    Mat mat = new Mat();

    List<MatOfPoint> listBoxContour = null;
    
    // loop to grab camera image into a Mat, process the Mat, put an image to be viewed if desired

    // This cannot be 'true'. The program will never exit if it is. This lets
    // the robot stop this thread when restarting robot code or deploying.
    while (!Thread.interrupted())
    {
      // find the target
      // compute the angle to turn to align and the distance
      // send 4 pieces of data to whatever is driving this beast
      // angle to turn, distance to target, frame sequence number, time of acquisition

      AcquireHubImage.image.getImage(mat);

// fake contours to test
// Imgproc.rectangle(mat, new Point(10, 10), new Point(20, 30), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(12, 50), new Point(21, 73), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(14, 90), new Point(22, 132), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(74, 90), new Point(82, 132), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(90, 150), new Point(120, 155), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
            
      // Reset the next target data and bump up the frame number
      nextTargetData.reset();
      nextTargetData.incrFrameNumber();

      // note that GRIP ratio is width/height

      // FIXME note that GRIP does not have maxArea so add it to filterContours
      // double maxArea = 200.0; // for example
			// if (area > maxArea) continue;

      // FIXME note that GRIP has a bad import so comment it out
      // import org.opencv.features2d.FeatureDetector;

      // FIXME maybe add min and max Vertices to GRIP

      // FIXME if you like clean then remove the unused imports
      

      gripPipeline.process(mat); // filter the image with the GRIP pipeline

      // The gripPipeline creates an array of contours that must be searched to find the target.
      ArrayList<MatOfPoint> filteredContours;
      filteredContours = new ArrayList<MatOfPoint>(gripPipeline.filterContoursOutput());

      // System.out.println(filteredContours.size() + " Contours found");
      // SmartDashboard.putNumber("#contours", filteredContours.size());

      // Check if no contours were found in the camera frame.
      if (filteredContours.isEmpty()) {
        // no contours found

        if(displayTargetContours)
        {
          Core.transpose(mat, mat); // camera is rotated so make image look right for humans
          Core.flip(mat, mat, 1);

          // Display a message if no contours are found.
          Imgproc.putText(mat, "No Contours", new Point(10, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
                  new Scalar(255, 255, 255), 1);
        }
          nextTargetData.hubDistance = -1.;
          nextTargetData.angleToTurn = -999.;
          nextTargetData.isFreshData = true;
          nextTargetData.isTargetFound = false;
      } else {
          // contours found
                    
          if(displayTargetContours)
          {
            // Draw center-line
            Imgproc.line(mat, new Point(0,Constant.targetCameraHeight/2.) , 
              new Point(Constant.targetCameraWidth,Constant.targetCameraHeight/2.), new Scalar(255, 255, 255),
              1, Imgproc.LINE_4);
            // Draw all contours at once (negative index).
            // Positive thickness means not filled, negative thickness means filled.
            // Draw all the contours that gripPipeline found in red
            // Later we'll draw over the ones we use in yellow
            Imgproc.drawContours(mat, filteredContours, -1, new Scalar(0, 0, 255), 1); // red

            listBoxContour = new ArrayList<MatOfPoint>();
          }

          // Loop through all contours taking them out of Mat and into ArrayList with everything we want
          ArrayList<ContourData> contourData = new ArrayList<ContourData>();
          filteredContours.forEach((cd)->contourData.add(new ContourData(cd))); // get out of Mat form

            // if no contours, say no target
            // if one contour, use that one to center
            // if two contours, center on their average
            // if three or more contours use sliding window to find most contours straight across image
            //    then average the ones in the window

            // initial values in case not set elsewhere
            double targetCenterX = -1;
            double targetCenterY = -1;

            if(filteredContours.size() == 1)
            {
              targetCenterX = contourData.get(0).centerX;
              targetCenterY = contourData.get(0).centerY;

              if(displayTargetContours) // list of contour's box to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1],contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              }
            } // end one contour
            else
            if(filteredContours.size() == 2)
            {
              targetCenterX = (contourData.get(0).centerX + contourData.get(1).centerX)/2.;
              targetCenterY = (contourData.get(0).centerY + contourData.get(1).centerY)/2.;

              if(displayTargetContours) // list of good contours' boxes to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1], contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              listBoxContour.add(new MatOfPoint(contourData.get(1).boxPts[0], contourData.get(1).boxPts[1], contourData.get(1).boxPts[2], contourData.get(1).boxPts[3]));
              }
            } // end 2 contours
            else // 3  or more contours for now - may be reduced after windowing
            {
              contourData.sort(ContourData.compareCenterX()); // sort by center X, order small to large X
              // System.out.println("centerX");contourData.forEach((data)->System.out.println(data.centerX));
              // setup sliding window
              int scanWindow = Constant.targetCameraWidth/6;
              int scanStep =  scanWindow/4;

              int countPointsMax = -1; // count of points in highest count window
              ArrayList<ContourData> windowedContourData = new ArrayList<ContourData>();

              var start = (int)(contourData.get(0).centerX) - scanStep - 1; // start 1 window below 1st data point
              var stop = (int)(contourData.get(contourData.size()-1).centerX) + scanStep + 1; // stop 1 window past last data point
              for(int window = start; window <= stop; window+=scanStep) // sliding window
              {
                int countPoints = 0; // initialize count of points in this window
                for(int point = 0; point < contourData.size(); point++) // check all points to see if in this window
                {
                  if((contourData.get(point).centerX >= window) && (contourData.get(point).centerX < (window + scanWindow)))
                  {
                    countPoints++; // count this point in this window
                  }
                }

                // check for new max number of points in window and process it - the last new max will win, though
                if(countPoints > countPointsMax)
                {
                   countPointsMax = countPoints; //  new max window count
                   windowedContourData.clear(); // clear previous max

                  // check all points again to see if in this window - faster, easier to repeat new max loops than to save data every time
                  for(int point = 0; point < contourData.size(); point++)
                  {
                    if((contourData.get(point).centerX >= window) && (contourData.get(point).centerX < (window + scanWindow)))
                    {
                      windowedContourData.add(contourData.get(point)); // all points in this new max window
                    }
                  }
                }
              }
              // System.out.println(windowedContourData); // the contours in max window

              // picking center by size and location of contour (below) seemed good but there was a lot of jitter with bad
              // tape; this was tested without benefit of the windowing scheme but don't bother retesting since averaging
              // the contours in the window reduces jitter so a big chunk is commented out

              // windowedContourData.sort(ContourData.compareArea()); // sort by area size, order large to small
              // System.out.println(windowedContourData.get(0).area);
              // System.out.println("windowedContourData");windowedContourData.forEach((data)->System.out.println(data));

              // // is #0 (max area contour) located between #1 and #2 in the Y-axis?
              // if(
              //   ((windowedContourData.get(0).centerY < windowedContourData.get(1).centerY) &&
              //     (windowedContourData.get(0).centerY > windowedContourData.get(2).centerY)    )  ||
              //     ((windowedContourData.get(0).centerY > windowedContourData.get(1).centerY) &&
              //     (windowedContourData.get(0).centerY < windowedContourData.get(2).centerY)    )     )
              // {
              //     targetCenterX = windowedContourData.get(0).centerX;
              //     targetCenterY = windowedContourData.get(0).centerY;

              //     if(displayTargetContours) // list of good contour's boxes to draw
              //     {
              //     listBoxContour.add(new MatOfPoint(windowedContourData.get(0).boxPts[0], windowedContourData.get(0).boxPts[1], windowedContourData.get(0).boxPts[2], windowedContourData.get(0).boxPts[3]));
              //     }
              // }
              // else
              // {
              //   targetCenterX = (windowedContourData.get(0).centerX + windowedContourData.get(1).centerX)/2.;
              //   targetCenterY = (windowedContourData.get(0).centerY + windowedContourData.get(1).centerY)/2.;
                
              //   if(displayTargetContours) // list of good contour's boxes to draw
              //   {
              //   listBoxContour.add(new MatOfPoint(windowedContourData.get(0).boxPts[0], windowedContourData.get(0).boxPts[1], windowedContourData.get(0).boxPts[2], windowedContourData.get(0).boxPts[3]));
              //   listBoxContour.add(new MatOfPoint(windowedContourData.get(1).boxPts[0], windowedContourData.get(1).boxPts[1], windowedContourData.get(1).boxPts[2], windowedContourData.get(1).boxPts[3]));
              //   }
              // }

              // average all contours in the max window
              targetCenterX = 0; // initialize centering totals
              targetCenterY = 0;

              for(ContourData wcd : windowedContourData)
              {
                targetCenterX += wcd.centerX; // total x and y values
                targetCenterY += wcd.centerY;
                if(displayTargetContours) // list of good contours' boxes to draw
                {
                  // add to list of good contour's boxes to draw
                  listBoxContour.add(new MatOfPoint(wcd.boxPts[0], wcd.boxPts[1], wcd.boxPts[2], wcd.boxPts[3]));
                }
              }
              targetCenterX /= windowedContourData.size(); // average x and y values are the target center (we are hopeful)
              targetCenterY /= windowedContourData.size();

            } // end 3 or more contours
          
            //Draw all the contours that we use in yellow
            if(displayTargetContours)
            {
              Imgproc.polylines(mat, // Matrix obj of the image
                listBoxContour, // draw all the boxes
                true, // isClosed
                new Scalar(0, 255, 255), // yellow
                1, // Thickness of the line
                Imgproc.LINE_4 // line type
                );

                while(!listBoxContour.isEmpty()) {
                  listBoxContour.get(0).release();
                  listBoxContour.remove(0);
                }
              }

            // Find the degrees to turn the turret by finding the difference between the
            // horizontal center of the camera frame and the horizontal center of the target.
            // calibrateAngle is the difference between what the camera sees as the retro-reflective
            // tape target and where the Cargo actually hits - the skew of the shooting process or
            // camera misalignment. It is not intended for shooting offset to the target. That would
            // depend on distance to the target.
            
            nextTargetData.angleToTurn = (Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / Constant.targetCameraHeight)
                    * ((Constant.targetCameraHeight / 2.0)
                    - targetCenterY)
                    + Vision.calibrateAngle;

            if (nextTargetData.angleToTurn <= -Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
                    || nextTargetData.angleToTurn >= Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { 
                // target not actually "seen" after the calibrateAngle offset was applied - no data
                nextTargetData.hubDistance = -1.;
                nextTargetData.angleToTurn = -999.;
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = false;
            } else {
                // target still in view - good set of data
                nextTargetData.hubDistance = pixelsToUnitsTable.lookup(targetCenterX);
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = true;
            }

            if(displayTargetContours)
            {
              Core.transpose(mat, mat); // camera is rotated so make image look right for humans
              Core.flip(mat, mat, 1);

              // display the distance pixels on image for reviewing or revising the conversion table
              Imgproc.putText(mat,
                String.format("%3.0f px", targetCenterX, nextTargetData.hubDistance),
                new Point(1, 12),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.4,
                new Scalar(255, 255, 255), 1);
              
              //displays the angle to turn on the image for reviewing or revising
              Imgproc.putText(mat,
                nextTargetData.isTargetFound ? String.format("turn %3.0f deg", nextTargetData.angleToTurn) : "no calibrated view",
                new Point(2, Constant.targetCameraWidth-8),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.4,
                new Scalar(255, 255, 255), 1);
            }

          } // end contours found

        // update the target information with best data or the initialized no contour data
        VisionData.targetData.set(nextTargetData);

        if(displayTargetContours)
        {
          outputStream.putFrame(mat); // Give the output stream a new image to display
        }

    }    // end "infinite" loop

    System.out.println("TargetSelection should never be here");
    
  } // end run method

  } // end outer class TargetSelection
