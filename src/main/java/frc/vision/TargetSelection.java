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
      // double maxArea = 1000.0;
			// if (area > maxArea) continue;

      // FIXME note that GRIP has a bad import so comment it out
      // import org.opencv.features2d.FeatureDetector;

      // FIXME maybe add min and max Vertices to GRIP

      // FIXME if you like clean then remove the unused imports
      

      gripPipeline.process(mat); // filter the image with the GRIP pipeline

      // The gripPipeline creates an array of contours that must be searched to find the target.
      ArrayList<MatOfPoint> filteredContours;
      filteredContours = new ArrayList<MatOfPoint>(gripPipeline.filterContoursOutput());

      // Check if no contours were found in the camera frame.
      if (filteredContours.isEmpty()) {
        if(displayTargetContours)
        {
          Core.transpose(mat, mat); // camera is rotated so make image look right for humans
          Core.flip(mat, mat, 1);

          // Display a message if no contours are found.
          Imgproc.putText(mat, "No Contours", new Point(10, 20), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
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

          filteredContours.forEach((cd)->contourData.add(new ContourData(cd))); // get out of Mat form

            // if no contours, say no target
            // if one contour, use that one to center
            // if two contours, center on their average
            // if three or more contours,
            //  if largest contour located between the 2nd and 3rd largest then center on that one largest
            //  otherwise center on the average of the 2 largest contours.

            // Also try sliding window to "bin" contours or other clustering techniques
            //  ideally all contours line up in the same x value spread by the different y
            //  keep the set with the largest number in the same X; toss out the rest

            // initial values in case not set elsewhere
            double targetCenterX = -1;
            double targetCenterY = -1;

            if(filteredContours.size() == 1)
            {
              targetCenterX = contourData.get(0).centerX;
              targetCenterY = contourData.get(0).centerY;

              if(displayTargetContours) // list of good contour's boxes to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1],contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              }
            } // end one contour
            else
            if(filteredContours.size() == 2)
            {
              targetCenterX = (contourData.get(0).centerX + contourData.get(1).centerX)/2.;
              targetCenterY = (contourData.get(0).centerY + contourData.get(1).centerY)/2.;

              if(displayTargetContours) // list of good contour's boxes to draw
              {
              listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1], contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
              listBoxContour.add(new MatOfPoint(contourData.get(1).boxPts[0], contourData.get(1).boxPts[1], contourData.get(1).boxPts[2], contourData.get(1).boxPts[3]));
              }
            } // end 2 contours
            else // 3  or more contours
            {
              contourData.sort(ContourData.compareArea()); // sort by area size, order large to small
              // System.out.println(contourData.get(0).area);
              // System.out.println("contourData");contourData.forEach((data)->System.out.println(data));

              // is #0 (max area contour) located between #1 and #2 in the Y-axis?
              if(
                ((contourData.get(0).centerY < contourData.get(1).centerY) &&
                  (contourData.get(0).centerY > contourData.get(2).centerY)    )  ||
                  ((contourData.get(0).centerY > contourData.get(1).centerY) &&
                  (contourData.get(0).centerY < contourData.get(2).centerY)    )     )
              {
                  targetCenterX = contourData.get(0).centerX;
                  targetCenterY = contourData.get(0).centerY;

                  if(displayTargetContours) // list of good contour's boxes to draw
                  {
                  listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1], contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
                  }
              }
              else
              {
                targetCenterX = (contourData.get(0).centerX + contourData.get(1).centerX)/2.;
                targetCenterY = (contourData.get(0).centerY + contourData.get(1).centerY)/2.;
                
                if(displayTargetContours) // list of good contour's boxes to draw
                {
                listBoxContour.add(new MatOfPoint(contourData.get(0).boxPts[0], contourData.get(0).boxPts[1], contourData.get(0).boxPts[2], contourData.get(0).boxPts[3]));
                listBoxContour.add(new MatOfPoint(contourData.get(1).boxPts[0], contourData.get(1).boxPts[1], contourData.get(1).boxPts[2], contourData.get(1).boxPts[3]));
                }
    
              }
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
            // camera misalignment.
            
            nextTargetData.angleToTurn = (Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / Constant.targetCameraHeight)
                    * ((Constant.targetCameraHeight / 2.0)
                    - targetCenterY)
                    + Vision.calibrateAngle;

            if (nextTargetData.angleToTurn <= -Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
                    || nextTargetData.angleToTurn >= Constant.VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { 
                // target not actually "seen" after the calibrateAngle offset was applied - no data
                nextTargetData.hubDistance = -1.;
                nextTargetData.angleToTurn = -1.;
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = false;
            } else {
                // target still in view - good set of data
                nextTargetData.hubDistance = pixelsToInchesTable.lookup(targetCenterX);
                nextTargetData.isFreshData = true;
                nextTargetData.isTargetFound = true;
            }

            if(displayTargetContours)
            {
              Core.transpose(mat, mat); // camera is rotated so make image look right for humans
              Core.flip(mat, mat, 1);

              // display the pixels and distance on image for reviewing or revising the conversion table
              Imgproc.putText(mat,
                String.format("%3.0f px = %3.0funits", targetCenterX, nextTargetData.hubDistance),
                new Point(1, 12),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.4,
                new Scalar(255, 255, 255), 1);
              
              //displays the angle to turn on the image for reviewing or revising
              Imgproc.putText(mat,
                String.format("turn %3.0f deg", nextTargetData.angleToTurn),
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

  // for (MatOfPoint contour : filteredContours) {
          //     contourData.add(new ContourData(contour)); // get out of Mat form
          //     // System.out.println(contour.dump()); // OpenCV Mat dump one line string of numbers
          //     // or more control over formatting with your own array to manipulate
          //     // System.out.println(pId + " " + aContour.size() + " points in contour"); // a contour is a bunch of points
          //     // convert MatofPoint to an array of those Points and iterate (could do list of Points but no need for this)
          //     // for(Point aPoint : aContour.toArray())System.out.print(" " + aPoint); // print each point

          //     // for(int idx = 0; idx < contour.toArray().length; idx++)
          //     // {
          //     // System.out.println("(" + contour.toArray()[idx].x + ", " +
          //     // contour.toArray()[idx].y + ")");
          //     // }

          // } // end loop through all contours

// example extract just the centerX from all contours
// double[] Xdata = new double[contourData.size()];
// for(int i = 0; i < Xdata.length; i++)
// {
//   Xdata[i] = (float)contourData.get(i).centerX;
// }


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


// outputStream =  new CvSource("PiCamera", VideoMode.PixelFormat.kMJPEG, 160, 120, 7);
// outputStream.setFPS(7);

// MjpegServer mjpegServer2 = new MjpegServer("serve_Blur", 1189);
// mjpegServer2.setSource(outputStream);
// Shuffleboard.getTab("Camera").add("MyOpenCV", outputStream);
// Shuffleboard.update();

//        final String PI_ADDRESS ="roboRIO-4237-FRC.local";
//  final int PORT = 1182; // or whatever it ends up being

//    String[] tmp = {"mjpeg:http://" + PI_ADDRESS + ":" + PORT + "/?action=stream", "mjpg:http://169.254.104.211:1182/?action=stream"};

// NetworkTableInstance.getDefault()
//     .getEntry("/CameraPublisher/PiCamera/streams")
//     // .setStringArray(new String({"mjpeg:http://" + PI_ADDRESS + ":" + PORT + "/?action=stream"}));
//     .setStringArray(tmp);
//     // ["mjpg:http://roboRIO-4237-FRC.local:1189/?action=stream","mjpg:http://169.254.104.211:1189/?action=stream"]
//     NetworkTableInstance.getDefault()
//     .getEntry("/CameraPublisher/PiCamera/connected")
//     .setBoolean(true);
//      }

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