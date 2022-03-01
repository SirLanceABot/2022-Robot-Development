package frc.vision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
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
          // System.out.println("Skipping null mat");
          continue;
      }
      
      if (mat.empty()) // threads start at different times so skip problems that might happen at the beginning
      {
          // System.out.println("Skipping empty mat");
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
          // Imgproc.putText(mat, "No Contours", new Point(20, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
                  // new Scalar(255, 255, 255), 1);
          nextTargetData.portDistance = -1.;
          nextTargetData.angleToTurn = -1.;
          nextTargetData.isFreshData = true;
          nextTargetData.isTargetFound = false;
      } else {
          // contours were found
              // System.out.println(filteredContours.size() + " Contours found");

              // Draw all contours at once (negative index).
              // Positive thickness means not filled, negative thickness means filled.
              Imgproc.drawContours(mat, filteredContours, -1, new Scalar(255, 0, 0), 1); // all contours in blue

          // Loop through all contours taking them out of Mat and into ArrayList with everything we want
          ArrayList<ContourData> contourData = new ArrayList<ContourData>();
          List<MatOfPoint> listBoxContour = new ArrayList<MatOfPoint>();

          for (MatOfPoint contour : filteredContours) {
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

              contourData.add(new ContourData(contour));

              var currentContour = contourData.size()-1; // index of this addition

              listBoxContour.add(new MatOfPoint( // list of contour's boxes to draw
                contourData.get(currentContour).boxPts[0],
                contourData.get(currentContour).boxPts[1],
                contourData.get(currentContour).boxPts[2],
                contourData.get(currentContour).boxPts[3]));
              
              Imgproc.putText(mat,
              String.format("%4.0f", contourData.get(currentContour).angle),
              contourData.get(currentContour).boxPts[0],
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.3,
                  new Scalar(255, 255, 255), 1);

                } // end loop through all contours

                // if no contours, say no target
                // if one contour, use that one to center
                // if two contours, check if near each other in the X axis and if near each other center on their average otherwise say no target
                // if 3 or more contours look for 2 clusters of contours (use k-means algorithm)
                // if the clusters are close to each other in X axis then consider them one cluster and use all contours
                // otherwise if a cluster is far from the other in X axis then delete the contours that are in the cluster with the fewest members
                // if one contour larger than the others by zzz pct then center on that one
                // otherwise center on the average of the 2 largest contours.
                
                // also specify max vertices in GripPipeline
    
                if(filteredContours.size() == 1)
                {
    
                }
                else
                if(filteredContours.size() == 2)
                {
    
                }
                else // 3  or more contours
                {
    //FIXME get all the contours centerX for data
                  float[] data = new float[]{120, 125, 118, 130, 170, 165};
                  int kClusters = 2;
                  new Cluster().getClusters(data, kClusters);
    
                  contourData.sort(ContourData.compareArea()); // area size order large to small

                  }

              Imgproc.polylines(mat, // Matrix obj of the image
                listBoxContour, // draw all the boxes
                      true, // isClosed
                      new Scalar(0, 255, 255), // Scalar object for color
                      1, // Thickness of the line
                      Imgproc.LINE_4 // line type
                      );

                      while(!listBoxContour.isEmpty()) {
                        listBoxContour.get(0).release();
                        listBoxContour.remove(0);
                    }
        
                    // do something to set targetData - this is nonsense but almost right and illustrative
                    var targetCenterX = contourData.get(0).centerX;
                    var targetCenterY = contourData.get(0).centerX;
        
            // // Find the corner points of the bounding rectangle and the image size
            // nextTargetData.portPositionInFrame = 0.0;

            // Find the degrees to turn the turret by finding the difference between the
            // horizontal center of the camera frame
            // and the horizontal center of the target.
            // calibrateAngle is the difference between what the camera sees as the
            // retroreflective tape target and where
            // the Power Cells actually hit - the skew of the shooting process or camera
            // misalignment.
            nextTargetData.angleToTurn = (VERTICAL_CAMERA_ANGLE_OF_VIEW / nextTargetData.imageSize.height)
                    * ((nextTargetData.imageSize.height / 2.0)
                    - targetCenterY)
                    + Vision.calibrateAngle;

            if (nextTargetData.angleToTurn <= -VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
                    || nextTargetData.angleToTurn >= VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { // target not actually "seen"
                                                                                            // after the calibrateAngle
                                                                                            // offset was applied
                nextTargetData.portDistance = -1.;
                nextTargetData.angleToTurn = -1.;
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = false;
            } else { // target still in view
                nextTargetData.portDistance = targetCenterX;
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = true;
            }

          } // end contours found
/* END demo GRIP pipeline */

      // update the target information with best contour or the initialized no contour data
        VisionData.targetData.set(nextTargetData);
        System.out.println(nextTargetData.toString());

        if(displayTargetContours)
        {
          outputStream.putFrame(mat); // Give the output stream a new image to display
        }

    }    // end "infinite" loop
    // System.out.println("TargetSelection should never be here");
    } // end run method

  } // end outer class TargetSelection

    class Cluster {    
  
  // float data[] = new float[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  // Mat mat = new Mat(1, 10, CvType.CV_32F);
  // mat.put(0, 0, data);
  
      public void getClusters(float[] data, int k) {
        // Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
        // Mat samples32f = new Mat();
        // samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);
  
        Mat samples32f = new Mat(1, 6, CvType.CV_32F);
        samples32f.put(0, 0, data);
        System.out.println("samples32f " + samples32f.dump());//1 row 6 cols
    
        Mat bestLabels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
        Mat centers = new Mat();
        Core.kmeans(samples32f, k, bestLabels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);		
        // System.out.println("best labels " + bestLabels.dump());// 6 rows 1 col
        // System.out.println("centers " + centers.dump());// 2 rows 1 col
    
        // samples32f [120, 125, 118, 130, 170, 165]
        // labels [1;     
        //  1;
        //  1;
        //  1;
        //  0;
        //  0]
        // centers [167.5;
        //  123.25]
        showClusters(data, bestLabels, centers);
 
        return;
      }
    
      private void showClusters (float[] data, Mat bestLabels, Mat centers) {
        HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
        for(int i = 0; i < centers.rows(); i++) counts.put(i, 0);
        
      //   int rows = 0;
      //   for(int y = 0; y < data.rows(); y++) {
      //     for(int x = 0; x < data.cols(); x++) {
      //       int label = (int)labels.get(rows, 0)[0];
      //       int r = (int)centers.get(label, 2)[0];
      //       int g = (int)centers.get(label, 1)[0];
      //       int b = (int)centers.get(label, 0)[0];
      //       counts.put(label, counts.get(label) + 1);
      //       clusters.get(label).put(y, x, b, g, r);
      //       rows++;
      //     }
      //   }
  
      for(int i = 0; i < data.length; i++)
      {
          int label = (int)bestLabels.get(i, 0)[0];
          // System.out.println("datum " + data[i] + ", in cluster " + label + " centered at " + centers.get(label, 0)[0]);// right
          counts.put(label, counts.get(label) + 1);
      }

      // System.out.println("counts " + counts);
      return;
      }
     }
  
  
  /*
  https://towardsdatascience.com/understanding-k-means-k-means-and-k-medoids-clustering-algorithms-ad9c9fbf47ca

  The parameters are:
  
  data : Data for clustering.
  nclusters(K) K : Number of clusters to split the set by.
  criteria : The algorithm termination criteria, that is, the maximum number of iterations and/or the desired accuracy. The accuracy is specified as criteria.epsilon. As soon as each of the cluster centers moves by less than criteria.epsilon on some iteration, the algorithm stops.
  attempts : Flag to specify the number of times the algorithm is executed using different initial labellings. The algorithm returns the labels that yield the best compactness (see the last function parameter).
  flags :
  KMEANS_RANDOM_CENTERS - selects random initial centers in each attempt.
  KMEANS_PP_CENTERS - uses kmeans++ center initialization by Arthur and Vassilvitskii.
  KMEANS_USE_INITIAL_LABELS - during the first (and possibly the only) attempt, use the user-supplied labels instead of computing them from the initial centers. For the second and further attempts, use the random or semi-random centers. Use one of KMEANS_*_CENTERS flag to specify the exact method.
  centers : Output matrix of the cluster centers, one row per each cluster center.
  
  
  
  
  Here are the meanings of the return values from cv2.kmeans() function:
  
  compactness : It is the sum of squared distance from each point to their corresponding centers.
  labels : This is the label array where each element marked '0','1',.....
  centers : This is array of centers of clusters.
  
  samples32f [120, 125, 118, 130, 170, 165]
  best labels [1;
   1;
   1;
   1;
   0;
   0]
  centers [167.5;
   123.25]
  datum 120.0, in cluster 1 centered at 123.25
  datum 125.0, in cluster 1 centered at 123.25
  datum 118.0, in cluster 1 centered at 123.25
  datum 130.0, in cluster 1 centered at 123.25
  datum 170.0, in cluster 0 centered at 167.5
  datum 165.0, in cluster 0 centered at 167.5
  counts {0=2, 1=4}
  */


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
    