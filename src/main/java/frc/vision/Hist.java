package frc.vision;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

class Hist {
/**
 * 
 * @param mat Assume 3 channels that can be BGR, HSV, etc since any 3 channel data are displayed.
 *   Histogram is overlaid in the corner of the input image.
 */
    public void displayHist(Mat mat, Mat dst, Mat mask, String[] label) {
    Mat histImage = new Mat();

    //////////////////////////////////////
    // 3 CHANNEL HISTOGRAM OF IMAGE
    // RGB Or HSV histogram or whatever the 3 channels mean
    //
    // histogram of image mat before any other drawing on mat
    // [image to list as needed by calcHist]
    var matList = new ArrayList<Mat>(1); // need only 1 entry in the list
    matList.add(mat); // put the mat into the list as needed by calcHist

    //! [image to list as needed by calcHist]]

    // [Establish the number of bins]
    int histSize = 90;
    //! [Establish the number of bins]

    // [Set the ranges ( for B,G,R) )]
    MatOfFloat histRange = new MatOfFloat(new float[] {0, 256}); // includes lower boundary; the upper boundary is exclusive of data values
    //! [Set the ranges ( for B,G,R) )]

    // [Set histogram param]
    boolean accumulate = false;
    //! [Set histogram param]

    // [Compute the histograms - one each for the 3 channels]
    Mat bHist = new Mat();
    Mat gHist = new Mat();
    Mat rHist = new Mat();

    Imgproc.calcHist(matList, new MatOfInt(0), mask, bHist, new MatOfInt(histSize), histRange, accumulate); // hist of only channel 0
    Imgproc.calcHist(matList, new MatOfInt(1), mask, gHist, new MatOfInt(histSize), histRange, accumulate);
    Imgproc.calcHist(matList, new MatOfInt(2), mask, rHist, new MatOfInt(histSize), histRange, accumulate);

    //System.out.println("bHist = " + bHist + "gHist = " + gHist + "rHist = " + rHist); // each 128x1 float 1 channel
    //! [Compute the histograms]

    // [Draw the histograms for B, G and R]

    int histW = 90; // pixels width
    int histH = 50; // pixels height
    histImage = new Mat( histH, histW, CvType.CV_8UC3, new Scalar( 0,0,0) );

    int binW = (int) Math.round((double) histW / histSize); // bin width in pixels not the data values

    //! [Draw the histograms for B, G and R]

    // [Normalize the result in each channel to ( 0, histImage.rows/3 )]
    Core.normalize(bHist, bHist, 0, histImage.rows()/3, Core.NORM_MINMAX);
    Core.normalize(gHist, gHist, 0, histImage.rows()/3, Core.NORM_MINMAX);
    Core.normalize(rHist, rHist, 0, histImage.rows()/3, Core.NORM_MINMAX);
    //! [Normalize the result to ( 0, histImage.rows )]

    //! [Draw for all channels - each channel has its own space]

    float[] bHistData = new float[(int) (bHist.total() * bHist.channels())];
    bHist.get(0, 0, bHistData);

    float[] gHistData = new float[(int) (gHist.total() * gHist.channels())];
    gHist.get(0, 0, gHistData);

    float[] rHistData = new float[(int) (rHist.total() * rHist.channels())]; // safety allocate but should be 128 bins x 1 histogram x 1 channel
    rHist.get(0, 0, rHistData);

    Imgproc.putText(histImage, label[0], new Point(10, histH/3 - 2), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
    Imgproc.putText(histImage, label[1], new Point(10, histH*2/3 - 2), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
    Imgproc.putText(histImage, label[2], new Point(10, histH - 2), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(255, 255, 255), 1);
    
    for( int i = 1; i < histSize; i++ ) {
        Imgproc.line(histImage.submat(0, histH/3, 0, histW), new Point(binW * (i - 1), histH/3 - Math.round(bHistData[i - 1])),
        new Point(binW * (i), histH/3 - Math.round(bHistData[i])), new Scalar(255, 0, 0), 2, Imgproc.LINE_4);

        Imgproc.line(histImage.submat(histH/3, histH*2/3, 0, histW), new Point(binW * (i - 1), histH/3 - Math.round(gHistData[i - 1])),
        new Point(binW * (i), histH/3 - Math.round(gHistData[i])), new Scalar(0, 255, 0), 2, Imgproc.LINE_4);

        Imgproc.line(histImage.submat(histH*2/3, histH, 0, histW), new Point(binW * (i - 1), histH/3 - Math.round(rHistData[i - 1])),
        new Point(binW * (i), histH/3 - Math.round(rHistData[i])), new Scalar(0, 0, 255), 2, Imgproc.LINE_4);

        // Core.MinMaxLocResult rHistMinMax = Core.minMaxLoc(rHist);
        // System.out.println("rHist " + rHistMinMax.minLoc + " " + rHistMinMax.minVal + " " + rHistMinMax.maxLoc + " " + rHistMinMax.maxVal);

        // System.out.print(
        // binW * (i - 1) + " " +
        // (histH - Math.round(rHistData[i - 1])) + " ");
    }

    // draw grid
    for (int idxC = 0; idxC <=histW; idxC+=10) {
        Imgproc.line (histImage, new Point(idxC, 0), new Point(idxC, histImage.rows()), new Scalar(255, 255, 255), 1, Imgproc.LINE_4);

    }

    // draw hue strip if first label seems to be for Hue
    if (label[0].substring(0, 1).toLowerCase().equals("h")) {
        Mat matConv = new Mat(4, histW, CvType.CV_8UC3);
        for (int idxC = 0; idxC <= matConv.cols(); idxC++) {
            for (int idxR = 0; idxR < 4; idxR++) {
                matConv.put(idxR, idxC, new byte[]{ (byte)((float)idxC/(float)matConv.cols()*360.f/2.f),  (byte)230,  (byte)200 }); // HSV = color wheel H, 230, 200 somewhat bright but not the brightest
            }
        }
        Imgproc.cvtColor(matConv, matConv, Imgproc.COLOR_HSV2BGR);

        Mat subMatStrip = new Mat(); // place for small hue strip insert on the larger histogram image
        subMatStrip = histImage.submat(0, matConv.rows(), 0, matConv.cols()); // define the insert area on the larger image
        matConv.copyTo(subMatStrip); // copy the insert to where it belongs

        matConv.release();
        subMatStrip.release();
    }

    //! [Draw for all channels]
    // end histogram of image mat before any other drawing on mat

    Mat subMat = new Mat(); // place for small image inserted into large image
    subMat = dst.submat(0, histImage.rows(), 0, histImage.cols()); // define the
    // insert area on the main image
    if( ! histImage.empty() )
    {
    Core.addWeighted(subMat, .20, histImage, .80, 0, subMat);
    }

    histImage.release();
    histRange.release();
    subMat.release();
    while(!matList.isEmpty()) { // should be just 1 entry but maybe above logic was changed so release them all
        matList.get(0).release();
        matList.remove(0);
    }

    //
    // END RGB HISTOGRAM OF IMAGE - except the presentation of it a couple of lines below
    //////////////////////////////////////
    }
}
/*
images; Source arrays. They all should have the same depth, CV_8U or CV_32F, and the same size. Each of them can have an arbitrary number of channels.
channels:List of the dims channels used to compute the histogram. The first array channels are numerated from 0 to images[0].channels()-1, the second array channels are counted from images[0].channels() to images[0].channels() + images[1].channels()-1, and so on.
mask: Optional mask. If the matrix is not empty, it must be an 8-bit array of the same size as images[i]. The non-zero mask elements mark the array elements counted in the histogram.
hist:L Output histogram, which is a dense or sparse dims -dimensional array.
histSize: Array of histogram sizes in each dimension.
ranges: Array of the dims arrays of the histogram bin boundaries in each dimension. When the histogram is uniform (uniform =true), then for each dimension i it is enough to specify the lower (inclusive) boundary L_0 of the 0-th histogram bin and the upper (exclusive) boundary U_(histSize[i]-1) for the last histogram bin histSize[i]-1. That is, in case of a uniform histogram each of ranges[i] is an array of 2 elements. When the histogram is not uniform (uniform=false), then each of ranges[i] contains histSize[i]+1 elements: L_0, U_0=L_1, U_1=L_2,..., U_(histSize[i]-2)=L_(histSize[i]-1), U_(histSize[i]-1). The array elements, that are not between L_0 and U_(histSize[i]-1), are not counted in the histogram.
accumulate: Accumulation flag. If it is set, the histogram is not cleared in the beginning when it is allocated. This feature enables you to compute a single histogram from several sets of arrays, or to update the histogram in time.
*/
// HSV to RGB conversion formula
// When 0 ≤ H < 360, 0 ≤ S ≤ 1 and 0 ≤ V ≤ 1:

// C = V × S

// X = C × (1 - |(H / 60°) mod 2 - 1|)

// m = V - C

// (R',G',B') =
//  (C, X, 0) 0 <= H < 60
//  (X, C, 0) 60 <= H < 120
//  (0, C, X) 120 <= H < 180
//  (0, X, C) 180 <= H < 240
//  (X, 0, C) 240 <= H < 300
//  (C, 0, X) 300 <= H < 360

// (R,G,B) = ((R'+m)×255, (G'+m)×255, (B'+m)×255)

// public static String hsvToRgb(float hue, float saturation, float value) {

//     int h = (int)(hue * 6);
//     float f = hue * 6 - h;
//     float p = value * (1 - saturation);
//     float q = value * (1 - f * saturation);
//     float t = value * (1 - (1 - f) * saturation);

//     switch (h) {
//       case 0: return rgbToString(value, t, p);
//       case 1: return rgbToString(q, value, p);
//       case 2: return rgbToString(p, value, t);
//       case 3: return rgbToString(p, q, value);
//       case 4: return rgbToString(t, p, value);
//       case 5: return rgbToString(value, p, q);
//       default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
//     }
// }

// public static String rgbToString(float r, float g, float b) {
//     String rs = Integer.toHexString((int)(r * 256));
//     String gs = Integer.toHexString((int)(g * 256));
//     String bs = Integer.toHexString((int)(b * 256));
//     return rs + gs + bs;
