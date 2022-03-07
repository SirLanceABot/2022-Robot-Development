package frc.vision;

import java.util.Comparator;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

class ContourData
{

    double area;
    Point center;
    double centerX;
    double centerY;
    Point[] boxPts = new Point[4]; // maybe not used
    double angle; // could use angle to double check position but not now
 
    ContourData(MatOfPoint contour)
    {
        RotatedRect rotatedRect;
        MatOfPoint2f NewMtx = new MatOfPoint2f(contour.toArray());
        rotatedRect = Imgproc.minAreaRect(NewMtx); // create angled bounding rectangle
        NewMtx.release();

        this.area = rotatedRect.size.area();
        this.center = rotatedRect.center;
        this.centerX = rotatedRect.center.x;
        this.centerY = rotatedRect.center.y;
        rotatedRect.points(this.boxPts);
        this.angle = rotatedRect.angle;
    }

    public double getArea(ContourData cd)
    {
        return this.area;
    }

    /**
     * Compares two contour's areas - sort descending
     *
     * @return the Comparator that defines this ordering on points
     */
    public static Comparator<ContourData> compareArea()
    {
      return new Comparator<ContourData>()
      {
        @Override
        public int compare(ContourData cd1, ContourData cd2)
        {
          if(cd1.area <  cd2.area) return  1;
          if(cd1.area >  cd2.area) return -1;
          return 0; // they are equal or it's the first add to TreeSet
        }
      };
    }

    /**
     * Compares two contour's centerX - sort ascending
     *
     * @return the Comparator that defines this ordering on points
     */
    public static Comparator<ContourData> compareCenterX()
    {
      return new Comparator<ContourData>()
      {
        @Override
        public int compare(ContourData cd1, ContourData cd2)
        {
          if(cd1.centerX >  cd2.centerX) return  1;
          if(cd1.centerX <  cd2.centerX) return -1;
          return 0; // they are equal or it's the first add to TreeSet
        }
      };
    }

    public String toString()
    {
        return String.format("COG (" + centerX + ", " + centerY + ") area " + area);
    }
}