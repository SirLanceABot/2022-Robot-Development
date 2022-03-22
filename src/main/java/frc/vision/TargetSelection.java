package frc.vision;

/*
  This class processes the target camera image to identify the target and
  present the location (angle, distance) to other Robot processes such as the
  shooter.  It optionally presents the data to the ShuffleBoard for use by
  the robot driver and operator.

  It initializes the ShuffleBoard Camera tab for the target information display
  and the distance units conversion table.

  The bulk of this class is the "infinite" loop to take the last fresh image from
  the target camera and find the target and optionally display it.

  Target identification is as follows:

  [The first image filter was defined when the camera was turned on earlier. The camera
  exposure is set to nearly "off" so only the very brightest objects leave the camera.
  This assumes a bright light is illuminating the target and nothing else.
  Retro-reflective tape and a headlight are used to do this.]

  Get the current fresh camera image

  Run the image processing pipeline
  
  This code was largely but not fully the code generated by experimenting on target
  images with GRIP.  That is if the GRIP pipeline is regenerated from GRIP, a little
  hand fixup is required.  Usually better to just change minor tweaks to the pipeline
  here in Java than to completely overlay the GripPipeline.java file and then have to
  fix it.

  The GRIP pipeline does the bulk of the image filtering to highlight likely targets.
  Filtering is based on the color, size, and somewhat shape of objects in the image.
  We use green light as that's a relatively rarely seen color in the arena - at least
  compared to red and blue.  We have shot at green cheer banners in past competitions
  so make the filters the best you can to see only real targets.

  Note that a copy of the GRIP file is also maintained in this project and that
  file should be used to start any GRIP sessions.

  Display a message if no target contours found.

  Verify a target is in the image if there are contours.

  Draw a center line on the human display for aiming.

  If one contour is found then aim at it.

  If two contours are found then aim between them.

  If three or more contours found, verify which contours are likely to be the target.

  Use a sliding window to identify strips of contours as expected by the several
  (usually 3 to 5) pieces of tape visible if looking near the hub.  That is, perform a
  simplified density binning based on how many objects (tape we hope) that we see across
  the image.

  The window scans from bottom to top of the image and uses the strip with the most objects
  found across the strip.  The algorithm is very simplistic as there is no more CPU time
  available to do more.

  This window process is a tunable system
  * how many strips the image is broken into -
  use large enough strips to hold the expected tape height and don't forget the distortion
  at the limits of angle and distance.
  * how many scan steps to take down the image -
  use small enough steps not to miss a set of objects that go together.  Very small steps
  are best for accuracy but there isn't enough CPU time available for many steps.

  The strip with the most objects is declared the winner.  The angle is based on the
  position of the edge objects in the strip and the distance is based on the
  location of the strip.

  Conceivably multiple strips have the same maximum number of objects.  The strip with
  the largest contour area beats others.

  The angle and distance are calculated from camera parameters and the location of the
  objects in the camera image.

  Data are presented for the rest of the world to use.

  Optionally information is sent to ShuffleBoard.

  GRIP filtered contours are outlined in red.  Of those in that initial set, actually used
  contours - those that made it through the density binning are outlined in yellow.
*/

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
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

  // save Ethernet bandwidth back to the DriverStation if false but then you can't see what's happening
  boolean displayTargetContours = true;

  // This object is used to send the OpenCV processed image to the Dashboard if desired w/ displayTargetContours = true
  private CvSource outputStream;
    
  GripPipeline gripPipeline = new GripPipeline();

  TargetData nextTargetData = new TargetData();

  private boolean calibrateMode = false; // FIXME: set true for test mode

public void run()
    {
    if(displayTargetContours)
    {
      // define our generated image the same size as the camera but rotated - doesn't have to be but easier
      
      // same as putVideo method but we need the server returned instead of the source
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
    Mat matDisplay = new Mat();

    List<MatOfPoint> listBoxContour = null;
    
    // loop to grab camera image into a Mat, process the Mat, put an image to be viewed if desired

    // This cannot be 'true'. The program will never exit if it is. This lets
    // the robot stop this thread when restarting robot code or deploying.
    while (!Thread.interrupted())
    {
      // find the target
      // compute the angle to turn to align and the distance
      // send data to whatever is driving this beast
      // angle to turn, distance to target, frame sequence number, fresh indicator, target found indicator

      AcquireHubImage.image.getImage(mat); // get the buffered image that magically appears for us

// fake contours to test
// Imgproc.rectangle(mat, new Point(10, 10), new Point(20, 30), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(12, 50), new Point(21, 73), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(14, 90), new Point(22, 132), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(74, 90), new Point(82, 132), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
// Imgproc.rectangle(mat, new Point(90, 150), new Point(120, 155), new Scalar(0, 255, 40), 2, Imgproc.LINE_4);
      
      mat.copyTo(matDisplay); // display has original image from here then add our drawings

      // Reset the next target data and bump up the frame number
      nextTargetData.reset();
      nextTargetData.incrFrameNumber();

      // note that GRIP contour filter ratio is width/height

      // FIXME note that GRIP does not have maxArea so add it to filterContours
      // double maxArea = 200.0; // for example
			// if (area > maxArea) continue;

      // FIXME note that GRIP has a bad import so comment it out
      // import org.opencv.features2d.FeatureDetector;

      // FIXME maybe add min and max Vertices to GRIP

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
          Core.transpose(matDisplay, matDisplay); // camera is rotated so make image look right for humans
          Core.flip(matDisplay, matDisplay, 1);

          // Display a message if no contours are found.
          Imgproc.putText(matDisplay, "No Contours", new Point(10, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
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
            Imgproc.line(matDisplay, new Point(0,Constant.targetCameraHeight/2.) , 
              new Point(Constant.targetCameraWidth,Constant.targetCameraHeight/2.), new Scalar(255, 255, 255),
              1, Imgproc.LINE_4);
            // Draw all contours at once (negative index).
            // Positive thickness means not filled, negative thickness means filled.
            // Draw all the contours that gripPipeline found in red
            // Later we'll draw over the ones we use in yellow
            Imgproc.drawContours(matDisplay, filteredContours, -1, new Scalar(0, 0, 255), 1); // red for GRIP contours

            listBoxContour = new ArrayList<MatOfPoint>();
          }

          // Loop through all contours taking them out of Mat and into ArrayList with everything we want
          ArrayList<ContourData> contourData = new ArrayList<ContourData>();
          filteredContours.forEach((cd)->contourData.add(new ContourData(cd))); // get out of Mat form

            // if no contours, say no target
          // if one contour, center on that one
          // if two contours, center on their group width
          // if three or more contours use sliding window down the image to find the most contours
          //  straight across image then center on the group width in that scanning window. After
          //  most contours the preference to break ties is largest total area of contours.

            // initial values in case not set elsewhere
            double targetCenterX = -1;
            double targetCenterY = -1;

          if(contourData.size() == 1)
          { // center on the extremes of the one box - not much to go on but it's all we have
            targetCenterX = (contourData.get(0).tl.x + contourData.get(0).br.x)/2.;
            targetCenterY = (contourData.get(0).tl.y + contourData.get(0).br.y)/2.;

              if(displayTargetContours) // list of contour's box to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1],contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              }
            } // end one contour
            else
          if(contourData.size() == 2)
          { // center on the extremes of the two boxes - crude approximation at edges but don't care about edges
            // no idea if these two boxes go together in the same stripe or if they overlap but again it's
            // all we have to go on
            targetCenterX = (Math.min(contourData.get(0).tl.x, contourData.get(1).tl.x) +
                              Math.max(contourData.get(0).br.x, contourData.get(1).br.x))/2.;
            targetCenterY = (Math.min(contourData.get(0).tl.y, contourData.get(1).tl.y) +
                              Math.max(contourData.get(0).br.y, contourData.get(1).br.y))/2.;

              if(displayTargetContours) // list of good contours' boxes to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1], contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              listBoxContour.add(new MatOfPoint(contourData.get(1).boxPts[0], contourData.get(1).boxPts[1], contourData.get(1).boxPts[2], contourData.get(1).boxPts[3]));
              }
            } // end 2 contours
          else // 3  or more contours for now - may be reduced after windowing
          { // center on likely symmetric group across the image
          
            // System.out.println("centerX");contourData.forEach((data)->System.out.println(data.centerX));

            // setup sliding window
            int scanWindow = Constant.targetCameraWidth/7; // 6 or 7 might be appropriate?  tune FIXME:
            int scanStep =  scanWindow/4; // guesstimate  FIXME:

            int countPointsMax = -1; // initial count of points in highest count window
            double groupAreaMax = -1; // area of all contours in this window - used to break ties on contour counts

              ArrayList<ContourData> windowedContourData = new ArrayList<ContourData>();

            for(int window = 0; window < Constant.intakeCameraWidth; window+=scanStep) // the whole screen
              {
              int countContours = 0; // initialize count of contours in this window
              double groupArea = 0.; // initialize area of all contours in this window
              for(int contour = 0; contour < contourData.size(); contour++)
              { // check all contours to see which are completely in this window
                if((contourData.get(contour).tl.x >= window) && (contourData.get(contour).br.x < (window + scanWindow)))
                {
                  countContours++; // count this contour in this window
                  // there is a risk to relying on the count of contours. At long distances the contours get
                  // small and might merge into one big one at the low resolution.  The count would be small
                  // although the contours would be valid and large through merging.
                  groupArea += contourData.get(contour).area; //count this area in the window
                  }
                }

              // check for new max number of points in window and process it
              // if same max number of contours in a group, then retain the group with the largest group area
              if((countContours > countPointsMax) || (countContours == countPointsMax && groupArea > groupAreaMax))
                {
                  countPointsMax = countContours; //  new max window count
                  groupAreaMax = groupArea; // area of new max window count
                   windowedContourData.clear(); // clear previous max

                // check all contours again to see if completely in this window
                // faster, easier to repeat new max loops than to save data every time in above loop
                  for(int point = 0; point < contourData.size(); point++)
                  {
                  if((contourData.get(point).tl.x >= window) && (contourData.get(point).br.x < (window + scanWindow)))
                    {
                    windowedContourData.add(contourData.get(point)); // list of points in this new max window
                    }
                  }
                }
              }
              // System.out.println(windowedContourData); // the contours in max window

            // find center of the group of contours in the max window
                
              targetCenterX = 0; // initialize centering totals
              targetCenterY = 0;

            // if still more than 4 contours sort by contour area large to small and keep the 4 largest contours
            // picked four because that many contours are clearly seen head on; 1 or 2 other small ones might be
            // seen at the edges and they don't contribute much to centering and might jitter so drop them
            if(windowedContourData.size() > 4)
            {
              windowedContourData.sort(ContourData.compareArea()); // sort by area size, order large to small
            }
              // find min and max x and y of the 4 (at most) largest areas of individual contours (not group area)
              double minX = 999999.;
              double maxX = -1.;
              double minY = 999999.;
              double maxY = -1.;

              for(int i = 0; i < Math.min(4, windowedContourData.size()); i++)
              {
                if(windowedContourData.get(i).tl.x < minX) minX = windowedContourData.get(i).tl.x;               
                if(windowedContourData.get(i).br.x > maxX) maxX = windowedContourData.get(i).br.x;
                if(windowedContourData.get(i).tl.y < minY) minY = windowedContourData.get(i).tl.y;            
                if(windowedContourData.get(i).br.y > maxY) maxY = windowedContourData.get(i).br.y;

                if(displayTargetContours) // list of good contours' boxes to draw
                {
                  // add to list of good contour's boxes to draw
                  listBoxContour.add(new MatOfPoint(
                    windowedContourData.get(i).boxPts[0],
                    windowedContourData.get(i).boxPts[1],
                    windowedContourData.get(i).boxPts[2],
                    windowedContourData.get(i).boxPts[3]));
                }
              }

              targetCenterX = (minX + maxX)/2.; // average x and y values are the target center (we are hopeful)
              targetCenterY = (minY + maxY)/2.;

             // System.out.println(windowedContourData.size() + " " + targetCenterX + ", " + targetCenterY);

           } // end 3 or more contours at the start - some might have been tossed out   
          
            //Draw all the contours that we use in yellow
            if(displayTargetContours)
            {
              Imgproc.polylines(matDisplay, // Matrix obj of the image
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

            // Find the degrees to turn the shooter by finding the difference between the
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
              Core.transpose(matDisplay, matDisplay); // camera is rotated so make image look right for humans
              Core.flip(matDisplay, matDisplay, 1);

              // display the distance pixels on image for reviewing or revising the conversion table
              Imgproc.putText(matDisplay,
                String.format("%3.0f px", targetCenterX, nextTargetData.hubDistance),
                new Point(1, 12),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.4,
                new Scalar(255, 255, 255), 1);
              
              //displays the angle to turn on the image for reviewing or revising
              Imgproc.putText(matDisplay,
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
        if(calibrateMode)
        {
            Core.transpose(mat, mat); // flipped matDisplay above; must flip mat to match it
            Core.flip(mat, mat, 1);
            new Calibration().calibrate(mat, matDisplay, filteredContours); // get calibration info
        }

          outputStream.putFrame(matDisplay); // Give the output stream a new image to display
        }
    }    // end "infinite" loop

    System.out.println("TargetSelection should never be here");
    
  } // end run method

} // end outer class TargetSelection

// parking lot of junk
// contourData.sort(ContourData.compareCenterX()); // sort by center X, order small to large X
// System.out.println("centerX");contourData.forEach((data)->System.out.println(data.centerX));

// contours might look prettier but it doesn't much change the recognition of them
// sharpen

// cvSharpen(blurOutput, blurOutput);

// private void cvSharpen(Mat src, Mat out)
// {
//   Mat sharpenFilter = new Mat(3, 3, CvType.CV_8S);
//       sharpenFilter.put(0, 0,
//           -1, -1, -1, // also tried 0, -1, 0, -1, 5, -1, 0, -1, 0
//           -1,  9, -1,
//           -1, -1, -1 
//       );
//   Imgproc.filter2D(src, out, src.depth(), sharpenFilter);
// }