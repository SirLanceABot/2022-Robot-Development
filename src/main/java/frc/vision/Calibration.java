package frc.vision;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.TermCriteria;

public class Calibration
{
    final private boolean makeClusters = true;
    final private boolean displayHistogram = true; // helps with "tuning"
    final private boolean displayClusterColor = false; // it's a busy display
    Mat hsvMat = new Mat();

    public void calibrate(Mat mat, Mat matDisplay, ArrayList<MatOfPoint> filteredContours)
    {
        //System.out.println("test " + DriverStation.isTest());
        
    byte[] gbr = new byte[3];
    mat.get(mat.rows()/2, mat.cols()/2, gbr); // center the center pixel

    Imgproc.cvtColor(mat, hsvMat, Imgproc.COLOR_BGR2HSV);
 
    if(displayHistogram)
        {// histogram of HSV of just the contours - doesn't tell us anything about what wasn't included
        Hist hist = new Hist();
        Mat mask = Mat.zeros(mat.size(), CvType.CV_8UC1); // start mask with all zeros (skip all pixels)
        Imgproc.drawContours(mask, filteredContours, -1, new Scalar(255, 255, 255), -1); // solid white for mask

        hist.displayHist(hsvMat, matDisplay, mask, new String[]{"H", "S", "V"}); // get the HSV histogram of the whole image (the contour we are hopeful)
 }

    Imgproc.circle(matDisplay, new Point(mat.cols()/2, mat.rows()/2), 6, new Scalar(255, 255, 255), 2);
    Imgproc.circle(matDisplay, new Point(mat.cols()/2, mat.rows()/2), 8, new Scalar(0, 0, 0), 2);

    // Imgproc.rectangle(matDisplay, new Point(100, 0), new Point(mat.cols(), 28), new Scalar(0, 0, 0), -1); // blank area for text

    Imgproc.putText(matDisplay,
     String.format("RGB %3d %3d %3d",
     gbr[2]<0?gbr[2]+256:gbr[2], gbr[1]<0?gbr[1]+256:gbr[1], gbr[0]<0?gbr[0]+256:gbr[0]),
     new Point(0, 108), Imgproc.FONT_HERSHEY_SIMPLEX, 0.3, new Scalar(255, 255, 255), 1);

    Imgproc.putText(matDisplay,
     String.format(".5HSV %3.0f %3.0f %3.0f",
     hsvMat.get(mat.rows()/2, mat.cols()/2)[0], hsvMat.get(mat.rows()/2, mat.cols()/2)[1],hsvMat.get(mat.rows()/2, mat.cols()/2)[2]),
     new Point(0, 117), Imgproc.FONT_HERSHEY_SIMPLEX, 0.3, new Scalar(255, 255, 255), 1);

     if(makeClusters) // to cluster or not to cluster that is the question
     // if clusters is run after the mat is draw/written on then those drawing colors effect the clusters
     {
      Mat out1= new Mat();
      Mat out2 = new Mat();
      Mat mask = new Mat();

      Imgproc.cvtColor(mat, out1,Imgproc.COLOR_BGR2RGB);
      Core.inRange(out1, new Scalar(0, 0, 0), new Scalar(250, 250, 250), mask);
  
      /**
      * Filter out an area of an image using a binary mask.
      * @param input The image on which the mask filters.
      * @param mask The binary image that is used to filter.
      * @param output The image in which to store the output.
      */

      mask.convertTo(mask, CvType.CV_8UC1);
      Core.bitwise_xor(out2, out2, out2);
      mat.copyTo(out2, mask);
      out2.copyTo(mat);

      // Each feature is arranged in a column, while each row corresponds to an input test sample.
      Mat hsv = new Mat();
      int countGreen = 0;
      for(int rows = 0; rows<mat.rows();rows++)
      for(int cols = 0; cols<mat.cols();cols++)
      {
        if(mat.get(rows,cols)[2]>= 240 && mat.get(rows,cols)[1] >= 240 && mat.get(rows,cols)[0] >= 240
        && mat.get(rows,cols)[1] > mat.get(rows,cols)[0] && mat.get(rows,cols)[1] > mat.get(rows,cols)[2])
        {
          countGreen++;
        }
        // System.out.printf("R%5f G%5f B%5f\n",mat.get(rows,cols)[2], mat.get(rows,cols)[1], mat.get(rows,cols)[0]);
      }
      System.out.println("count greenish White pixels - see GRIP for cutoff " + countGreen);
      System.out.println("cluster #, pixel count, .5HSV");
      Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
      int kClusters = 8;
      Mat matDisplayTemp = new Mat(matDisplay.rows(), matDisplay.cols(), matDisplay.type());
      Cluster.getClusters(hsv, kClusters, matDisplayTemp);
      if(displayClusterColor)
      {
      Imgproc.cvtColor(matDisplayTemp, matDisplayTemp, Imgproc.COLOR_HSV2BGR);
      Core.addWeighted(matDisplayTemp, .10, matDisplay, .90, 0, matDisplay);
      }
    }
}

static class Cluster {
    
    static public void getClusters(Mat p, int k, Mat dst) {

    Mat samples32f = new Mat(p.rows()*p.cols(), 3, CvType.CV_32F);
    int point = 0;
    for(int rows = 0; rows<p.rows();rows++)
    for(int cols = 0; cols<p.cols();cols++)
    {
      samples32f.put(point++, 0, new float[]{(float)p.get(rows,cols)[0],(float)p.get(rows,cols)[1],(float)p.get(rows,cols)[2]});
    }
      // System.out.println("samples32f " + samples32f.dump());//1 row 6 cols
      Mat bestLabels = new Mat();
      TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 4);
      Mat centers = new Mat();
      try
      { // opencv k-means++ clustering of the HSV data at each pixel
      Core.kmeans(samples32f, k, bestLabels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);
      }
      catch(CvException e)
      {
        System.out.println(e.getMessage());
      }	
      // System.out.println("best labels " + bestLabels.dump());// 6 rows 1 col
      int[] countPoints = new int[centers.rows()]; // # points around a cluster center
      for(int i=0;i<countPoints.length;i++) countPoints[i] = 0; // count points in a cluster
      for(int i=0;i<bestLabels.rows();i++) countPoints[(int)bestLabels.get(i, 0)[0]]++; // all data best labels
      for(int i=0;i<countPoints.length;i++) // print the cluster centers info
      {
        System.out.printf("%d %7d %3.0f %3.0f %3.0f\n",
          i, countPoints[i], centers.get(i, 0)[0], centers.get(i, 1)[0], centers.get(i, 2)[0]);
      }

      // fully saturate and brighten the clusters so we can see the dim ones
      centers.put(0, 1, new double[]{255., 255.});
      centers.put(1, 1, new double[]{255., 255.});
      centers.put(2, 1, new double[]{255., 255.});
      centers.put(3, 1, new double[]{255., 255.});
      centers.put(4, 1, new double[]{255., 255.});
      centers.put(5, 1, new double[]{255., 255.});
      centers.put(6, 1, new double[]{255., 255.});
      centers.put(7, 1, new double[]{255., 255.});

      // index through the 2D fetching from the 1D for its data
      point = 0;
      for(int rows = 0; rows<p.rows();rows++)
      for(int cols = 0; cols<p.cols();cols++)
      {
        var pl = (int)bestLabels.get(point, 0)[0];
        dst.put(rows, cols, new byte[]{(byte)centers.get(pl, 0)[0], (byte)centers.get(pl, 1)[0], (byte)centers.get(pl, 2)[0]});
        point++;
      }
      // System.out.println("centers " + centers.dump());// 2 rows 1 col

      return;
    }
 }
}
// Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
// Mat samples32f = new Mat();
// samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

/*
      Mat dst = new Mat();
      Core.sortIdx(centers, dst, Core.SORT_ASCENDING+Core.SORT_EVERY_COLUMN);
      System.out.println(dst.dump());
*/
// return showClusters(Xdata, Ydata, Zdata, bestLabels, centers);
// private ArrayList<ClusterData> showClusters (float[] Xdata, float[] Ydata, float[] Zdata, Mat bestLabels, Mat centers)
// {
//   ArrayList<ClusterData> clusters = new ArrayList<ClusterData>();

//   for(int i = 0; i < Xdata.length; i++)
//   {
//     if(bestLabels.get(i, 0)[0] == Double.NaN) System.out.println("null label");//????null ptr
//     if(Xdata[i] == Float.NaN)System.out.println("NaN data");
//     var label = (int)bestLabels.get(i, 0)[0];
//     if(centers.get(label, 0)[0] == Double.NaN)System.out.println("NaN centers");
//     System.out.flush();

//     clusters.add(new ClusterData(Xdata[i], label, (int)centers.get(label, 0)[0]));
//   }

//   return clusters;
// }
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
/*

3. Color Quantization
Color Quantization is the process of reducing number of colors in an image. One reason to do so is to reduce the memory. Sometimes, some devices may have limitation such that it can produce only limited number of colors. In those cases also, color quantization is performed. Here we use k-means clustering for color quantization.

There is nothing new to be explained here. There are 3 features, say, R,G,B. So we need to reshape the image to an array of Mx3 size (M is number of pixels in image). And after the clustering, we apply centroid values (it is also R,G,B) to all pixels, such that resulting image will have specified number of colors. And again we need to reshape it back to the shape of original image. Below is the code:

import numpy as np
import cv2 as cv
img = cv.imread('home.jpg')
Z = img.reshape((-1,3))
# convert to np.float32
Z = np.float32(Z)
# define criteria, number of clusters(K) and apply kmeans()
criteria = (cv.TERM_CRITERIA_EPS + cv.TERM_CRITERIA_MAX_ITER, 10, 1.0)
K = 8
ret,label,center=cv.kmeans(Z,K,None,criteria,10,cv.KMEANS_RANDOM_CENTERS)
# Now convert back into uint8, and make original image
center = np.uint8(center)
res = center[label.flatten()]
res2 = res.reshape((img.shape))
cv.imshow('res2',res2)
cv.waitKey(0)
cv.destroyAllWindows()
See the result below for K=8:
*/