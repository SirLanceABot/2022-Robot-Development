package frc.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSource;
import frc.constants.Constant;

public class TargetSelection implements Runnable {

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
      // define our generated image the same size as the camera - doesn't have to be but easy
      outputStream = CameraServer.putVideo("TargetContours", Constant.targetCameraWidth, Constant.targetCameraHeight);
      outputStream.setFPS(7);
    }
 
    // Pixels to Inches Data Table Lookup copied from Constant
    // allocate fixed size array with parameter at least as large as the number
    // of data points - minimum of 2 points
    // Notice the LUT CTOR argument is maximum table size - change it if it needs to be larger
    LUT pixelsToInchesTable = new LUT(10);

    for(int i = 0; i < Constant.pixelsToInchesTable.length; i++)
    {
      pixelsToInchesTable.add(Constant.pixelsToInchesTable[i][0], Constant.pixelsToInchesTable[i][1]);
    }
    System.out.println("[Camera Pixels To Inches To Hub] " + pixelsToInchesTable); // print the whole table
  

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

      // Demo OpenCV draw a rectangle on the image
      // Imgproc.rectangle(mat, new Point(10, 10), new Point(100, 100), new Scalar(255, 255, 255), 5);
      // END Demo OpenCV draw a rectangle on the image

      // System.out.print("[ts" + Timer.getFPGATimestamp()); // doesn't print after a few seconds but no watchdog msg
                                                          //this.image.getImage(mat);
      AcquireHubImage.image.getImage(mat);


// fake contours to test
// Imgproc.rectangle(mat, new Point(10, 10), new Point(20, 30), new Scalar(0, 255, 40), 2);
// Imgproc.rectangle(mat, new Point(12, 50), new Point(21, 73), new Scalar(0, 255, 40), 2);
// Imgproc.rectangle(mat, new Point(14, 90), new Point(22, 132), new Scalar(0, 255, 40), 2);
// Imgproc.rectangle(mat, new Point(74, 90), new Point(82, 132), new Scalar(0, 255, 40), 2);
// Imgproc.rectangle(mat, new Point(90, 150), new Point(120, 155), new Scalar(0, 255, 40), 2);
            
      
      // Reset the next target data and bump up the frame number
      nextTargetData.reset();
      nextTargetData.incrFrameNumber();

/* demo GRIP pipeline */
      gripPipeline.process(mat); // filter the image with the GRIP pipeline

      // The gripPowerPortVisionPipeline creates an array of contours that must
      // be searched to find the target.
      ArrayList<MatOfPoint> filteredContours;
      filteredContours = new ArrayList<MatOfPoint>(gripPipeline.filterContoursOutput());

      // Check if no contours were found in the camera frame.
      if (filteredContours.isEmpty()) {
        if(displayTargetContours)
        {
          // Display a message if no contours are found.
          Imgproc.putText(mat, "No Contours", new Point(20, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
                  new Scalar(255, 255, 255), 1);
        }
          nextTargetData.hubDistance = -1.;
          nextTargetData.angleToTurn = -1.;
          nextTargetData.isFreshData = true;
          nextTargetData.isTargetFound = false;
      } else {
          // contours were found
          // System.out.println(filteredContours.size() + " Contours found");

          // Loop through all contours taking them out of Mat and into ArrayList with everything we want
          ArrayList<ContourData> contourData = new ArrayList<ContourData>();
          
          if(displayTargetContours)
          {
            Imgproc.line(mat, new Point(0,Constant.targetCameraHeight/2.) , 
              new Point(Constant.targetCameraWidth,Constant.targetCameraHeight/2.), new Scalar(255, 255, 255)
              , 1);
            // Draw all contours at once (negative index).
            // Positive thickness means not filled, negative thickness means filled.
            Imgproc.drawContours(mat, filteredContours, -1, new Scalar(255, 0, 0), 1); // all contours in blue

            listBoxContour = new ArrayList<MatOfPoint>();
          }

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

  
              if(displayTargetContours)
              {
              var currentContour = contourData.size()-1; // index of this addition
              listBoxContour.add(new MatOfPoint( // list of contour's boxes to draw
                contourData.get(currentContour).boxPts[0],
                contourData.get(currentContour).boxPts[1],
                contourData.get(currentContour).boxPts[2],
                contourData.get(currentContour).boxPts[3]));
              }

              // Imgproc.putText(mat,
              // String.format("%4.0f", contourData.get(currentContour).angle),
              // contourData.get(currentContour).boxPts[0],
              //   Imgproc.FONT_HERSHEY_SIMPLEX, 0.3,
              //     new Scalar(255, 255, 255), 1);

          } // end loop through all contours

                // if no contours, say no target
                // if one contour, use that one to center
                // if two contours, check if near each other in the X axis and if near each other center on their average otherwise say no target
                // if three or more contours,
                //  if one contour larger than the others by zzz pct then center on that one
                //  otherwise center on the average of the 2 largest contours.
                // Also try sliding window to "bin" contours
                //  ideally all contours line up in the same x value spread by the different y
                //  keep the set with the largest number in the same X; toss out the rest
                
                // also specify max vertices in GripPipeline
    
                // do something to set targetData - this is nonsense but almost right and illustrative
                double targetCenterX = -1;
                double targetCenterY = -1;
                if(filteredContours.size() == 1)
                {
                  targetCenterX = contourData.get(0).centerX;
                  targetCenterY = contourData.get(0).centerY;
                }
                else
                if(filteredContours.size() == 2)
                {
                  targetCenterX = (contourData.get(0).centerX + contourData.get(1).centerX)/2.;
                  targetCenterY = (contourData.get(0).centerY + contourData.get(1).centerY)/2.;
                }
                else // 3  or more contours
                {
                  // example extract just the centerX from all contours
                  // double[] Xdata = new double[contourData.size()];
                  // for(int i = 0; i < Xdata.length; i++)
                  // {
                  //   Xdata[i] = (float)contourData.get(i).centerX;
                  // }

                  contourData.sort(ContourData.compareArea()); // area size order large to small

                  // is #0 (max area contour) located between #1 and #2 in the Y-axis?
                  if(
                    ((contourData.get(0).centerY < contourData.get(1).centerY) &&
                     (contourData.get(0).centerY > contourData.get(2).centerY)    )  ||
                     ((contourData.get(0).centerY > contourData.get(1).centerY) &&
                     (contourData.get(0).centerY < contourData.get(2).centerY)    )     )
                  {
                      targetCenterX = contourData.get(0).centerX;
                      targetCenterY = contourData.get(0).centerY;
                  }
                  else
                  {
                    targetCenterX = (contourData.get(0).centerX + contourData.get(1).centerX)/2.;
                    targetCenterY = (contourData.get(0).centerY + contourData.get(1).centerY)/2.;  
                  }
                  
                } // end 3 or more contours

              
                if(displayTargetContours)
                {
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
                  }

            // // Find the corner points of the bounding rectangle and the image size
            // nextTargetData.portPositionInFrame = 0.0;

            // Find the degrees to turn the turret by finding the difference between the
            // horizontal center of the camera frame
            // and the horizontal center of the target.
            // calibrateAngle is the difference between what the camera sees as the
            // retroreflective tape target and where
            // the Cargo actually hits - the skew of the shooting process or camera
            // misalignment.
            
            nextTargetData.angleToTurn = (Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / Constant.targetCameraHeight)
                    * ((Constant.targetCameraHeight / 2.0)
                    - targetCenterY)
                    + Vision.calibrateAngle;

            if (nextTargetData.angleToTurn <= -Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
                    || nextTargetData.angleToTurn >= Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { 
                // target not actually "seen" after the calibrateAngle offset was applied
                nextTargetData.hubDistance = -1.;
                nextTargetData.angleToTurn = -1.;
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = false;
            } else {
                // target still in view
                nextTargetData.hubDistance = pixelsToInchesTable.lookup(targetCenterX);
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = true;
            }

            if(displayTargetContours)
            {
            // display the pixels and distance on image for reviewing or revising the conversion table
            Imgproc.putText(mat,
              String.format("(%4.0f pixels -> %4.0f inches)", targetCenterX, nextTargetData.hubDistance),
              new Point(100, 20),
              Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
              new Scalar(255, 255, 255), 1);
              
              //displays the angle to turn on the image for reviewing or revising
              Imgproc.putText(mat,
              String.format("(angle to turn %4.0f degrees)", nextTargetData.angleToTurn),
              new Point(100, 40),
              Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
              new Scalar(255, 255, 255), 1);
            }

          } // end contours found

      // update the target information with best contour or the initialized no contour data
        VisionData.targetData.set(nextTargetData);
        // System.out.println(nextTargetData.toString());

        if(displayTargetContours)
        {
          outputStream.putFrame(mat); // Give the output stream a new image to display
        }

    }    // end "infinite" loop
    System.out.println("TargetSelection should never be here");
    } // end run method

  } // end outer class TargetSelection


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
    //////////////////////////////////////////////////////////////////////////////

    // start of failed k-means analysis
    // if 3 or more contours look for 2 clusters of contours (use k-means algorithm)]
    // if the clusters are close to each other in X axis then consider them one cluster and use all contours
    // otherwise if a cluster is far from the other in X axis then delete the contours that are in the cluster with the fewest members

    // int kClusters = 2;
    // ArrayList<ClusterData> clusters = new Cluster().getClusters(Xdata, kClusters); // cluster on X-axis

    // clusters.forEach(System.out::println);
    // System.out.println();
    // // if center of cluster 0 near center of custer 1 then keep all the data
    // // else remove those that are in the least populated cluster


    // class Cluster {    
  
    //   // float data[] = new float[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    //   // Mat mat = new Mat(1, 10, CvType.CV_32F);
    //   // mat.put(0, 0, data);
      
    //       public ArrayList<ClusterData> getClusters(float[] data, int k) {
    //         // Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
    //         // Mat samples32f = new Mat();
    //         // samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);
      
    //         Mat samples32f = new Mat(1, 6, CvType.CV_32F);
    //         samples32f.put(0, 0, data);
    //         // System.out.println("samples32f " + samples32f.dump());//1 row 6 cols
        
    //         Mat bestLabels = new Mat();
    //         TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 4);
    //         Mat centers = new Mat();
    //         try
    //         {
    //         Core.kmeans(samples32f, k, bestLabels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);
    //         }
    //         catch(CvException e)
    //         {
    //           System.out.println(e.getMessage());
    //         }	
    //         // System.out.println("best labels " + bestLabels.dump());// 6 rows 1 col
    //         // System.out.println("centers " + centers.dump());// 2 rows 1 col
        
    //         // samples32f [120, 125, 118, 130, 170, 165]
    //         // labels [1;     
    //         //  1;
    //         //  1;
    //         //  1;
    //         //  0;
    //         //  0]
    //         // centers [167.5;
    //         //  123.25]
     
    //         return showClusters(data, bestLabels, centers);
    //       }
    
    //       private ArrayList<ClusterData> showClusters (float[] data, Mat bestLabels, Mat centers)
    //       {
    //         ArrayList<ClusterData> clusters = new ArrayList<ClusterData>();
    
    //         for(int i = 0; i < data.length; i++)
    //         {
    //           if(bestLabels.get(i, 0)[0] == Double.NaN) System.out.println("null label");
    //           if(data[i] == Float.NaN)System.out.println("NaN data");
    //           var label = (int)bestLabels.get(i, 0)[0];
    //           if(centers.get(label, 0)[0] == Double.NaN)System.out.println("NaN centers");
    //           System.out.flush();
    
    //           clusters.add(new ClusterData(data[i], label, (int)centers.get(label, 0)[0]));
    //         }
    
    //         return clusters;
    //       }
        
    //       // private void showClusters (float[] data, Mat bestLabels, Mat centers) {
    //       //   HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
    //       //   for(int i = 0; i < centers.rows(); i++) counts.put(i, 0);
            
    //       // //   int rows = 0;
    //       // //   for(int y = 0; y < data.rows(); y++) {
    //       // //     for(int x = 0; x < data.cols(); x++) {
    //       // //       int label = (int)labels.get(rows, 0)[0];
    //       // //       int r = (int)centers.get(label, 2)[0];
    //       // //       int g = (int)centers.get(label, 1)[0];
    //       // //       int b = (int)centers.get(label, 0)[0];
    //       // //       counts.put(label, counts.get(label) + 1);
    //       // //       clusters.get(label).put(y, x, b, g, r);
    //       // //       rows++;
    //       // //     }
    //       // //   }
    //       // for(int i = 0; i < data.length; i++)
    //       // {
    //       //     int label = (int)bestLabels.get(i, 0)[0];
    //       //     // System.out.println("datum " + data[i] + ", in cluster " + label + " centered at " + centers.get(label, 0)[0]);// right
    //       //     counts.put(label, counts.get(label) + 1);
    //       // }
    
    //       // // System.out.println("counts " + counts);
    //       // return;
    //       // }
    //    }
       
    //    class ClusterData
    //    {
    //      float datum;
    //      int label;
    //      float center;
    
    //      ClusterData(float datum, int label, float center)
    //      {
    //        this.datum = datum;
    //        this.label = label;
    //        this.center = center;
    //      }
    
    //      public String toString()
    //      {
    //       return "[Cluster] datum " + datum + ", label " + label + ", center " + center;
    //      }
    //    }