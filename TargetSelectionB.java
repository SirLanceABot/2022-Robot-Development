import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;
import org.opencv.imgproc.Moments;

import lut.LUT;

import contourUtils.MatchShapes;

/**
 * This class is used to select the target from the camera frame. The user MUST
 * MODIFY the process() method. The user must create a new
 * gripPowerPortVisionPipeline class using GRIP, modify the TargetData
 * class, and modify this class.
 * 
 * @author FRC Team 4237
 * @version 2019.01.28.14.20
 */
public class TargetSelectionB {
    static {
        System.out.println("Starting class: " + MethodHandles.lookup().lookupClass().getCanonicalName());
    }

    private static final String pId = new String("[TargetSelectionB]");

    private static final double VERTICAL_CAMERA_ANGLE_OF_VIEW = 35.0;

    // This object is used to run the gripPowerPortVisionPipeline
    private GRIPPowerPortVisionPipeline gripPowerPortVisionPipeline = new GRIPPowerPortVisionPipeline();

    // This field is used to determine if debugging information should be displayed.
    private boolean debuggingEnabled = false;

    //private static int printCount = 0;

    // Pixels to Inches Data Table Lookup
    LUT pixelsToInchesTable = new LUT(10); // allocate fixed size array with parameter at least as large as the number
                                           // of data points - minimum of 2 points
    // allocate these in common so they can be drawn later
    MatOfPoint idealTurretContour = new MatOfPoint();
    // MatOfPoint belowFrontTurretContour = new MatOfPoint();
    // MatOfPoint belowSlightlyLeftTurretContour = new MatOfPoint();
    MatOfPoint belowRightTurretContour = new MatOfPoint();

    Mat idealTurretContourMomentsHu = Mat.zeros(7, 1, CvType.CV_64FC1); // initialize mat to quiet the compiler likely overkill but descriptive
    Mat belowRightTurretContourMomentsHu = Mat.zeros(7, 1, CvType.CV_64FC1); // initialize mat to quiet the compiler likely overkill but descriptive
    // Mat belowFrontTurretContourMomentsHu = Mat.zeros(7, 1, CvType.CV_64FC1); // initialize mat to quiet the compiler likely overkill but descriptive
    // Mat belowSlightlyLeftTurretContourMomentsHu = Mat.zeros(7, 1, CvType.CV_64FC1); // initialize mat to quiet the compiler likely overkill but descriptive
    private int HuCompareNormalizationMethod = 1; // methods 0 to 6 method [1] used for matching; others better? method [0] is terrible
    
    TargetSelectionB() {

        // Enter more data points for more accuracy. The equation should model some sort
        // of sinusoidal function.
        // The x coordinate is pixels and the y coordinate is the horizontal distance to
        // the target in inches.
        // TODO: Notice the LUT CTOR argument is maximum table size - change it if it
        // needs to be larger
        pixelsToInchesTable.add(60.0, 37.0); // enter (x, y) coordinates x ascending order, must add at least 2 data points
        pixelsToInchesTable.add(615.0, 246.0);
        System.out.println(pId + " pixelsToInchesTable" + pixelsToInchesTable); // print the whole table
        // Questionable testing data of Pixel-Inches Readings:
        // horizontal distance is HD and angled distance is AD
        // (110, HD = 128.5 and AD = 143)
        // (30-60, HD = 38 and AD = 72)
        // (0, HD = 232 and AD = 240)

        // setup ideal target shape to compare with camera image found contour
        // this can vary greatly with perspective distortion so this might have to be
        // dynamically generated based on distance, angles, etc.
        // Seems like it should be better to use expected typical shape assuming the distortion of perspective.
        // So far it doesn't seem that way!  In "noisy" images with extra bars of green light for some reason
        // the Hu moments of the bars were closer to the belowRight ideal contour than a straight-on actual was
        // to the ideal front contour.  Couldn't figure out why so used SOLIDITY in the "GRIP" pipeline
        // to get rid of bars (the trapezoid is highly open - about 35% to 40%).

        idealTurretContour.fromArray( // High Power Port Tape Contour - 2D drawing - no perspective warp
            new Point(13.,  3.),   // point 0
            new Point(68.,  109.),   // point 1
            new Point(188., 109.),   // point 2
            new Point(247., 4.),    // point 3
            new Point(232., 4.),    // point 4
            new Point(180., 95.),   // point 5
            new Point(77., 95.),    // point 6
            new Point(28., 3.)     // point 7
            );
        Moments idealTurretContourMoments = Imgproc.moments(idealTurretContour);
        idealTurretContour.release();
        Imgproc.HuMoments(idealTurretContourMoments, idealTurretContourMomentsHu);

        // from crude hand pointing camera at small model in rkt basement

        belowRightTurretContour.fromArray(
            new Point(229., 136.),
            new Point(228., 143.),
            new Point(235., 158.),
            new Point(251., 162.),
            new Point(266., 164.),//bottom right
            new Point(261., 157.),
            new Point(240., 153.),
            new Point(238., 151.),
            new Point(236., 141.),
            new Point(246., 134.),
            new Point(245., 128.)
            );
        //Core.flip(belowRightTurretContour, belowRightTurretContour, 0);
        //System.out.println(belowRightTurretContour);

        Moments belowRightTurretContourMoments = Imgproc.moments(belowRightTurretContour);
        belowRightTurretContour.release();
        Imgproc.HuMoments(belowRightTurretContourMoments, belowRightTurretContourMomentsHu);

        // belowFrontTurretContour.fromArray(
        //     new Point(494., 131.),
        //     new Point(414., 183.),
        //     new Point(414., 302.),
        //     new Point(487., 351.),
        //     new Point(493., 335.),
        //     new Point(430., 292.),
        //     new Point(429., 210.),
        //     new Point(436., 186.),
        //     new Point(495., 147.)
        //     );
        // Moments belowFrontTurretContourMoments = Imgproc.moments(belowFrontTurretContour);
        // //belowFrontTurretContour.release();
        // Imgproc.HuMoments(belowFrontTurretContourMoments, belowFrontTurretContourMomentsHu);

        // belowSlightlyLeftTurretContour.fromArray(
        //     new Point(151., 249.),
        //     new Point(105., 272.),
        //     new Point(102., 318.),
        //     new Point(138., 337.),
        //     new Point(144., 336.),
        //     new Point(144., 330.),
        //     new Point(112., 311.),
        //     new Point(114., 282.),
        //     new Point(119., 273.),
        //     new Point(152., 258.)
        //     );

        // Moments belowSlightlyLeftTurretContourMoments = Imgproc.moments(belowSlightlyLeftTurretContour);
        // //belowSlightlyLeftTurretContour.release();
        // Imgproc.HuMoments(belowSlightlyLeftTurretContourMoments, belowSlightlyLeftTurretContourMomentsHu);
    }

    /**
     * This method sets the field to display debugging information.
     * 
     * @param enabled Set to true to display debugging information.
     */
    public void setDebuggingEnabled(boolean enabled) {
        debuggingEnabled = enabled;
    }

    /**
     * This method is used to select the next target. The user MUST MODIFY this
     * method.
     * 
     * @param mat            The camera frame containing the image to process.
     * @param nextTargetData The target data found in the camera frame.
     */
    public void process(Mat mat, TargetDataB nextTargetData) {

        // printCount++;
        // if(printCount > 10) printCount = 1;
        // else printCount++;

        // Let the GRIPPowerPortVisionPipeline filter through the camera frame
        gripPowerPortVisionPipeline.process(mat);

        //gripPowerPortVisionPipeline.cvMorph0Output().copyTo(mat);

        // try this model of a vision pipeline from a cyberknight presentation
        // threshold cv_thresh_binary
        // dilate
        // cvtcolor bgr2hsv
        // inrange scalar 40, 0, 0  to 65, 255, 255
        // canny dst canny_out 0, 0
        // findcontours external cv_chain_approx_simple point(0, 0)

        // In several experiments, edge detecting (Canny, Sobel, Gradient, etc) didn't seem to help.
        // Canny was neutral, Sobel hurt with the parameters I used, and Gradient made fat contours
        // The best contours that were the right size were setting the HSV very close to what is needed.
        // Unfortunately setting HSV close for near objects makes far (dim) objects disappear.
        // Need some sort of adaptive setting to minimize the dark border around the contour.
        // That extra thickness of the object doesn't hurt anything except shape analyses using
        // the Hu moments.  The thickness of the object can significantly effect the moments
        // especially when the object is far away and very small.

        // We are cautiously optimistic that GRIP pipeline found the one and only one
        // target object but there might not be one and only one so take evasive action

        // The gripPowerPortVisionPipeline creates an array of contours that must
        // be searched to find the target.
        ArrayList<MatOfPoint> filteredContours;
        filteredContours = new ArrayList<MatOfPoint>(gripPowerPortVisionPipeline.filterContoursOutput());
        int contourIndex = -1; // initialize here - using same value to indicate no contours and count contours
        int contourIndexBest = -1;
        double shapeMatch = Double.MAX_VALUE; // initialize for best shaped contour - max is worst possible match
        //Main.targetIcon = Mat.zeros(24, 24, CvType.CV_8UC3);  // initialize target Icon

        // Check if no contours were found in the camera frame.
        if (filteredContours.isEmpty()) {
            System.out.println(pId + " No Contours");
            if (debuggingEnabled) {
                // Display a message if no contours are found.
                Imgproc.putText(mat, "No Contours", new Point(20, 20), Core.FONT_HERSHEY_SIMPLEX, 0.25,
                        new Scalar(255, 255, 255), 1);
            }

            nextTargetData.portDistance = -1.;
            nextTargetData.angleToTurn = -1.;
            nextTargetData.isFreshData = true;
            nextTargetData.isTargetFound = false;
        } else {
            // contours were found
            if (filteredContours.size() > 1) { // not very good if more than one contour
                System.err.println(pId + " " + filteredContours.size() + " Contours found");
            }

            Rect boundRect = null; // upright rectangle
   
            if (debuggingEnabled) {
                // Draw all contours at once (negative index).
                // Positive thickness means not filled, negative thickness means filled.
                Imgproc.drawContours(mat, filteredContours, -1, new Scalar(255, 0, 0), 1);
            }

            // Loop through all contours and just remember the best one

            for (MatOfPoint contour : filteredContours) {
                contourIndex++;

                // debug output Print all the contours

                // System.out.println("Contour Index = " + contourIndex);
                // System.out.println(contour.dump()); // OpenCV Mat dump one line string of numbers
                // or more control over formating with your own array to manipulate
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
                    Core.FONT_HERSHEY_SIMPLEX, 0.3,
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
                while(!listMidContour.isEmpty()) {
                    listMidContour.get(0).release();
                    listMidContour.remove(0);
                }
                
                // initialize shape comparison quality
                double[] compare =
                { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE}; 
                double[] compareR =
                { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE}; 
                // double[] compareF =
                // { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE}; 
                // double[] compareL =
                // { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE}; 
                // **************************************** */
                // * start match shape
                // **************************************** */
                if (Main.turretTargetMatchShape) {
                    // optional check since everything needed for targeting is initialized outside
                    // this block and can be used for target information even if this block isn't run

                    // Use moments to determine shape. Compare moments to moments of known shape.

                    // ************************************************************** */
                    // start use matchShapes

                    // this matching is rotation invariant so better eliminate incorrect aspect
                    // ratio first in filter contours because, for example, rectangles that are
                    // 2:1 are liked as well as those that are 1:2

                    // compute moments and Hu moments for our own matchShapes. OpenCV matchShapes() does
                    // it for us.
 
                    Moments moments;
                    Mat actualHu = Mat.zeros(7, 1, CvType.CV_64FC1); // initialize mat - need to quiet the compiler but zeros is likely overkill

                    //moments = Imgproc.moments(filteredContours.get(contourIndex)); // same statement as the one below
                    moments = Imgproc.moments(contour);
                    Imgproc.HuMoments(moments, actualHu);
                    //System.out.println(belowRightTurretContourMomentsHu);
                    // Hu moment #7 opposite signs indicates mirror image - didn't seem to work here, though.
                    // if ( Math.signum(actualHu.get(6, 0)[0])
                    //          != Math.signum(idealTurretContourMomentsHu.get(6, 0)[0]) ) {
                    //     System.out.println("ideal Hu moments 7 differ");
                    // }
                    
                    // if ( Math.signum(actualHu.get(6, 0)[0])
                    //          != Math.signum(belowRightTurretContourMomentsHu.get(6, 0)[0]) ) {
                    //     System.out.println("below right Hu moments 7 differ");
                    // }

                    // wipeout the I7 to suppress checking for mirror similarity - we don't care and it causes some jitter
                    actualHu.put(6, 0, 0.);
                    idealTurretContourMomentsHu.put(6, 0, 0.);
                    belowRightTurretContourMomentsHu.put(6, 0, 0.);
                    compare = MatchShapes.matchShapes(idealTurretContourMomentsHu, actualHu);
                    // test out comparison with 3 other shapes expected due to perspective distortion
                    compareR = MatchShapes.matchShapes(belowRightTurretContourMomentsHu, actualHu); 
                    // compareF = MatchShapes.matchShapes(actualHu, belowFrontTurretContourMomentsHu);
                    // compareL = MatchShapes.matchShapes(actualHu, belowSlightlyLeftTurretContourMomentsHu);
                    // some jitter in the last Hu moment
                    // if (printCount == 1) {
                    //     System.out.println("contour " + contourIndex + " " + compare[HuCompareNormalizationMethod] + " " + compareR[HuCompareNormalizationMethod]);
                    //     printHu("front", idealTurretContourMomentsHu);
                    //     printHu("right", belowRightTurretContourMomentsHu);
                    //     printHu("actual", actualHu);
                    // }

                    // trying to eliminate bar contours - those without higher moments (didn't much help)
                    double eps = 1.e-5;
                    if((Math.abs(actualHu.get(4, 0)[0]) < eps) && (Math.abs(actualHu.get(5, 0)[0]) < eps)) {
                        compare[HuCompareNormalizationMethod] = Double.MAX_VALUE;
                        compareR[HuCompareNormalizationMethod] = Double.MAX_VALUE;
                    }

                    Imgproc.putText(mat,
                        String.format("%3.0f%s%3.0f",
                            compare[HuCompareNormalizationMethod], compareR[HuCompareNormalizationMethod] >= 10. ? " " : "       ", compareR[HuCompareNormalizationMethod]),
                        boxPts[0],
                        Core.FONT_HERSHEY_SIMPLEX, 0.3,
                        new Scalar(255, 255, 255), 1);
                    }
                    //System.out.println(pId + toString(compare));
                    // end use matchShapes
                    // ********************************************* */

                    // TODO: display quality of shape on Shuffleboard?

                    // also TODO: use the comparison or absolute value of distance sensors to filter bad "targets"
                    // at St Joe 2020, Robot shot at a big green team sign in the bleachers thinking
                    // it was a small nearby target

                // **************************************** */
                // * end match shape
                // **************************************** */

                // ******************************************* */
                // * save the data for the best contour (so far)
                // *
                // * If the compare variable isn't calculated above (it's an option set in Main),
                // * then the initialized values are used causing all the contours to be "checked"
                // * and the last one wins so there had better be only one countour, if
                // * shape checking is disabled.
                // ******************************************* */

                if (compare[HuCompareNormalizationMethod] <= shapeMatch || compareR[HuCompareNormalizationMethod] <= shapeMatch) {
                    // the if(=) case covers if only one contour
                    // or if optional shape matching wasn't run in which case the last contour wins

                    // save new best contour\
                    shapeMatch = Math.min(compare[HuCompareNormalizationMethod], compareR[HuCompareNormalizationMethod]);
                    contourIndexBest = contourIndex;

                    // Find the corner points of the bounding rectangle and the image size
                    nextTargetData.boundingBoxPts[0] = boxPts[0];
                    nextTargetData.boundingBoxPts[1] = boxPts[1];
                    nextTargetData.boundingBoxPts[2] = boxPts[2];
                    nextTargetData.boundingBoxPts[3] = boxPts[3];
                    nextTargetData.imageSize.width = mat.width();
                    nextTargetData.imageSize.height = mat.height();
                    nextTargetData.portPositionInFrame = 0.0;

                    // Find the degrees to turn the turret by finding the difference between the
                    // horizontal center of the camera frame
                    // and the horizontal center of the target.
                    // calibrateAngle is the difference between what the camera sees as the
                    // retroreflective tape target and where
                    // the Power Cells actually hit - the skew of the shooting process or camera
                    // misalignment.
                    nextTargetData.angleToTurn = (VERTICAL_CAMERA_ANGLE_OF_VIEW / nextTargetData.imageSize.height)
                            * ((nextTargetData.imageSize.height / 2.0)
                                    - ((nextTargetData.boundingBoxPts[1].y + nextTargetData.boundingBoxPts[2].y) / 2.0))
                            + Main.calibrateAngle;

                    if (nextTargetData.angleToTurn <= -VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.
                            || nextTargetData.angleToTurn >= VERTICAL_CAMERA_ANGLE_OF_VIEW / 2.) { // target not actually "seen"
                                                                                                   // after the calibrateAngle
                                                                                                   // offset was applied
                        nextTargetData.portDistance = -1.;
                        nextTargetData.angleToTurn = -1.;
                        nextTargetData.isFreshData = true;
                        nextTargetData.isTargetFound = false;
                    } else { // target still in view
                        Main.angleHistory.addLast(nextTargetData.angleToTurn); // save old angles for debugging insights
                        nextTargetData.portDistance = pixelsToInchesTable.lookup(boundRect.br().x);
                        nextTargetData.isFreshData = true;
                        nextTargetData.isTargetFound = true;
                        if (Main.displayTurretPixelDistance) {
                            System.out.println(pId + " pixels:" + boundRect.br().x + ", LUT inches:"
                                    + nextTargetData.portDistance);
                        }
                    }
                }
                // *********************************************** */
                // * end save the data for the best contour (so far)
                // *********************************************** */

                // gripPowerPortVisionPipeline.cvCannyOutput().copyTo(mat); // show this to verify appearance of the edge detector
                // note that if using canny edge detection as part of the thresholding process,
                // canny does draw the contour outside of the target so there is the color 
                // of the background around the contour - black which appears as the red (0) in the histogram

            } // end of looping through all contours

            // if (false ) {
            //  drawShape(idealTurretContour, mat);
            //  drawShape(belowFrontTurretContour, mat);
            //  drawShape(belowSlightlyLeftTurretContour, mat);
            //  drawShape(belowRightTurretContour, mat);
            // }
  
            // display HSV histograms of the best contour - could do all the "best" contours in above loop
            if (Main.displayTurretContours && Main.displayTurretHistogram) {
                try {
                Hist hist = new Hist();
                Mat mask = Mat.zeros(mat.size(), CvType.CV_8UC1); // start mask with all zeros (skip all pixels)
                Imgproc.drawContours(mask, filteredContours, contourIndexBest, new Scalar(255), -1); // set the mask with the contour
                hist.displayHist(gripPowerPortVisionPipeline.outHSV(), mat, mask, new String[]{"H", "S", "V"}); // get the HSV histogram of the contour
                mask.release();
                // print to verify inside and outside of contour and print the values - the histogram is better
                // MatOfPoint2f  NewMtx2 = new MatOfPoint2f( contour.toArray() );
                // for (double idxR = boundRect.tl().y; idxR < boundRect.br().y; idxR++)
                // for (double idxC = boundRect.tl().x; idxC < boundRect.br().x; idxC++)
                // {
                //  The function returns +1 , -1 , or 0
                //  to indicate if a point is inside, outside, or on the contour, respectively.
                //     double inOrOut = Imgproc.pointPolygonTest(NewMtx2, new Point(idxC, idxR), false);
                //     if (inOrOut >= 0.) {
                //     System.out.format("%d, %d, %5.1f  ", (int)idxC, (int)idxR, inOrOut);
                //     System.out.format("%4.0f",   gripPowerPortVisionPipeline.outHSV().get((int)idxR, (int)idxC)[0]);
                //     System.out.format("%4.0f",   gripPowerPortVisionPipeline.outHSV().get((int)idxR, (int)idxC)[1]);
                //     System.out.format("%4.0f\n", gripPowerPortVisionPipeline.outHSV().get((int)idxR, (int)idxC)[2]);
                //     }
                // }
                } catch(Exception e) {e.printStackTrace();}
            }

            // draw the final best contour to outline the target found
            Imgproc.drawContours(mat, filteredContours, contourIndexBest, new Scalar(255, 255, 255), 1);

            // if ( false ) { // find the simpler version of the contour - fewer points
            //     MatOfPoint2f temp = new MatOfPoint2f(filteredContours.get(contourIndexBest).toArray());
            //     MatOfPoint2f approxCurve = new MatOfPoint2f();
            //     Imgproc.approxPolyDP(
            //         temp, // Mat input curve
            //         approxCurve, // Mat output simpler curve same type as input
            //         0.01*Imgproc.arcLength(temp, true), // double This is the maximum distance between the original curve and its approximation.
            //         true); // If true, the approximated curve is closed (its first and last vertices are connected). Otherwise, it is not closed.
            //         //arcLength is perimeter of closed contour if true or curve length
                
            //     System.out.println(approxCurve.dump());
                
            //     //moments = Imgproc.moments(approxCurve);

            //     // List<Point> listApprox = new ArrayList<Point>(approxCurve.toList());
            //     // System.out.println(temp.size() + " " + listApprox.size()); // 1x86 7
            //     // // draw the simple contour
            //     // for (int idx = 0; idx< listApprox.size(); idx++)
            //     // {
            //     // Imgproc.line(
            //     //     mat,
            //     //     listApprox.get(idx), // one end of the line
            //     //     idx+1==listApprox.size() ? listApprox.get(0): listApprox.get(idx+1), // other end - close the shape on the last point
            //     //     new Scalar(255, 255, 0),
            //     //     1,
            //     //     Imgproc.LINE_AA,
            //     //     0);
            //     // }

            //     temp.release();
            //     approxCurve.release();
            // }

            while(!filteredContours.isEmpty()) {
                filteredContours.get(0).release();
                filteredContours.remove(0);
            }        
        } // end of processing all contours in this camera frames

        
        Mat targetIconTemp = new Mat(); // to resize bounding bounding box of best contour to 24x24 for target icon
                                        // allocate this temp variable to keep processing out of the "synchronized"

        if( contourIndexBest >= 0 ) { // contours found path      
            // save the contour even if out of range (effectively no target found)
            Imgproc.resize(mat.submat( new Rect(nextTargetData.boundingBoxPts[0], nextTargetData.boundingBoxPts[2])),
                targetIconTemp, new Size(24, 24), 0., 0., Imgproc.INTER_LINEAR);
        }
        else { // no contours found path
            Imgproc.resize(mat.submat(mat.height()/2-24, mat.height()/2+24, mat.width()/2-24, mat.width()/2+24), // grab the image center
                targetIconTemp, new Size(24, 24), 0., 0., Imgproc.INTER_LINEAR);
        }

        for (int i = 0; i < 100; i++) {
            // Draw marker representing history of angle to turn.
            Imgproc.drawMarker(mat, new Point( i, 100+Math.min(36, (int)(Math.max(0., Main.angleHistory.get(i)+18.))) ), 
                new Scalar(255, 255, 255), Imgproc.MARKER_STAR, 10);
        }
 
        // update the target information with best contour or the initialized no contour data
        synchronized (Main.tapeLock) {
            Main.tapeDistance = nextTargetData.portDistance;
            Main.tapeAngle = nextTargetData.angleToTurn;
            Main.isTargetFound = nextTargetData.isTargetFound;
            Main.tapeContours = contourIndexBest;
            Main.shapeQuality = shapeMatch;
            targetIconTemp.copyTo(Main.targetIcon);
            Main.isDistanceAngleFresh = nextTargetData.isFreshData;
            Main.tapeLock.notify();
        }

        targetIconTemp.release();
        gripPowerPortVisionPipeline.releaseAll();
    }

    void drawShape(MatOfPoint shape, Mat dst) {
        //System.out.println(shape.dump() + " " + dst.rows()); 

        List<MatOfPoint> temp = new ArrayList<MatOfPoint>();
        temp.add(shape);
        Imgproc.polylines(dst, // Matrix obj of the image
        temp, // java.util.List<MatOfPoint> pts
        true, // isClosed
        new Scalar(255, 255, 255), // Scalar object for color
        1, // Thickness of the line
        Imgproc.LINE_4 // line type
        );

        // for (int idxR =0; idxR < shape.rows(); idxR++) {
        //     int c =  (int)shape.get( idxR,0) [0];
        //     int r =  (int)shape.get( idxR,0) [1];
        //     Imgproc.drawMarker(dst, new Point(c, r), new Scalar(255, 0, 255), Imgproc.MARKER_STAR, 8);// magenta
        // }
    }

    void printHu(String label, Mat HuShape) {
            double[] H = new double[7];
            HuShape.get(0, 0, H);
            System.out.format("%8s %8f, %8f, %8f, %8f, %8f, %8f, %8f]\n", label, H[0], H[1], H[2], H[3], H[4], H[5], H[6]);
     }
    
    String toString(double[] array) {
        return Arrays.stream(array)
                .mapToObj(i -> String.format("%5.2f", i))
                .collect(Collectors.joining(", ", "[", "]"));
                //.collect(Collectors.joining("|", "|", "|"));
            }
}

// Parking lot for some good example code to keep as reference

// convert MatOfPoint to MatOfPoint2f
// easy looking:
// MatOfPoint2f  NewMtx2 = new MatOfPoint2f( contour.toArray() );
// clunky looking:
// List<MatOfPoint2f> matrix = new ArrayList<MatOfPoint2f>();
// for(int i = 0; i < contour.size(i); i++){
//     MatOfPoint2f myPt = new MatOfPoint2f();
//     contour.get(i).convertTo(myPt, CvType.CV_32FC2);
//     matrix.add(myPt);
// }

// // Enhance Image if useful - not associated with the Hough Lines below
// // Convert to YUV
// // Apply histogram equalization
// // Convert Back To BGR
// Mat Image_yuv = new Mat(mat.rows(), mat.cols(), CvType.CV_8UC3);
// List<Mat> yuvPlanes = new ArrayList<>();
// Imgproc.cvtColor(mat, Image_yuv, Imgproc.COLOR_BGR2YUV);
// Core.split(Image_yuv, yuvPlanes);
// Imgproc.equalizeHist(yuvPlanes.get(0), yuvPlanes.get(0));
// Core.merge(yuvPlanes, Image_yuv);
// Imgproc.cvtColor(Image_yuv, mat, Imgproc.COLOR_YUV2BGR);

// Hough Lines example in case that is useful to determine target location
// but it is slow so check the frame rates

// The Feature Detection for line segments in GRIP does not work in the OpenCV
// version in the RPi and roboRIO.
// GRIP has an older version of OpenCV with Line Segment Detection and the RPi
// and roboRIO have a newer OpenCV
// with only HoughLinesP. The latest OpenCV with Fast Line Segment Detection is
// not installed in RPi or roboRIO.

// private HoughLinesRun findLines = new HoughLinesRun(); // testing finding
// lines

// One way to sharpen an image. Not needed for line detection. Not necessarily
// the best sharpener.
// Maybe an OpenCV filter2D edge detector with an appropriate kernel would give
// good results #define sharpen mat3(0, -1, 0, -1, 5, -1, 0, -1, 0)
// Imgproc.filter2D(picture0Gray, picture0GrayFilter1, picture0Gray.depth(), filter1);
// https://en.wikipedia.org/wiki/Kernel_(image_processing)
// // sharpen image
// blur is low pass filter
// subtract the low frequencies from the original leaving the middle and higher
// frequencies
// cv::Mat image = cv::imread(file);
// cv::Mat gaussBlur;
// GaussianBlur(image, gaussBlur, cv::Size(0,0), 3);
// cv::addWeighted(image, 1.5, gaussBlur, -0.5, 0, image);

// findLines.findLines(mat);

// Hough Transform in OpenCV Lines Parameters
// image 8-bit, single-channel binary source image. The image may be modified by
// the function.
// lines output vector of lines(cv.32FC2 type). Each line is represented by a
// two-element vector (rho,theta) . rho is the distance from the coordinate
// origin (0,0). theta is the line rotation angle in radians.
// rho distance resolution of the accumulator in pixels.
// theta angle resolution of the accumulator in radians.
// threshold accumulator threshold parameter. Only those lines are returned that
// get enough votes
// srn for the multi-scale Hough transform, it is a divisor for the distance
// resolution rho . The coarse accumulator distance resolution is rho and the
// accurate accumulator resolution is rho/srn . If both srn=0 and stn=0 , the
// classical Hough transform is used. Otherwise, both these parameters should be
// positive.
// stn for the multi-scale Hough transform, it is a divisor for the distance
// resolution theta.
// min_theta for standard and multi-scale Hough transform, minimum angle to
// check for lines. Must fall between 0 and max_theta.
// max_theta for standard and multi-scale Hough transform, maximum angle to
// check for lines. Must fall between min_theta and CV_PI.

// Probabilistic Hough Transform Lines Parameters
// image 8-bit, single-channel binary source image. The image may be modified by
// the function.
// lines output vector of lines(cv.32SC4 type). Each line is represented by a
// 4-element vector (x1,y1,x2,y2) ,where (x1,y1) and (x2,y2) are the ending
// points of each detected line segment.
// rho distance resolution of the accumulator in pixels.
// theta angle resolution of the accumulator in radians.
// threshold accumulator threshold parameter. Only those lines are returned that
// get enough votes
// minLineLength minimum line length. Line segments shorter than that are
// rejected.
// maxLineGap maximum allowed gap between points on the same line to link them.
// class HoughLinesRun {
// public void findLines(Mat src) {
// // Declare the output variables
// Mat dst = new Mat(), cdst = new Mat(), cdstP;
// // Edge detection
// Imgproc.Canny(src, dst, 50, 200, 3, false);
// // Copy edges to the images that will display the results in BGR
// Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
// cdstP = cdst.clone();
// // Standard Hough Line Transform
// // Line is all the way across the image - probably not what is wanted - it's
// not a segment
// // So don't use this
// Mat lines = new Mat(); // will hold the results of the detection
// Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 60); // runs the actual
// detection
// System.out.println("HoughLines rows = " + lines.rows());
// // Draw the lines
// for (int x = 0; x < lines.rows(); x++) {
// double rho = lines.get(x, 0)[0],
// theta = lines.get(x, 0)[1];
// double a = Math.cos(theta), b = Math.sin(theta);
// double x0 = a*rho, y0 = b*rho;
// Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
// Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
// Imgproc.line(src, pt1, pt2, new Scalar(0, 255, 255), 3, Imgproc.LINE_AA, 0);
// }
// // Probabilistic Line Transform
// // Produces Line Segments - probably what is wanted
// Mat linesP = new Mat(); // will hold the results of the detection
// Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the
// actual detection
// System.out.println("HoughLInesP rows = " + linesP.rows());
// // Draw the lines
// for (int x = 0; x < linesP.rows(); x++) {
// double[] l = linesP.get(x, 0);
// Imgproc.line(src, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0,
// 0, 255), 1, Imgproc.LINE_AA, 0);
// }
// dst.release();
// cdst.release();
// cdstP.release();
// lines.release();
// linesP.release();
// }
// }

// // example process each pixel with conversion to short
// //
// // could stick with the conversion to byte instead of short to save memory
// but that means the data are
// // in Java -128 to 127 signed byte - see example below
// Mat temp = new Mat(mat.rows(), mat.cols(), CvType.CV_16SC3);
// mat.convertTo(temp, CvType.CV_16SC3);
// short[] flatMat = new short[(int)temp.total()*temp.channels()];
// temp.get(0, 0, flatMat);
// for (int idx = 0; idx<mat.total()*mat.channels();idx=idx+mat.channels())
// {
// flatMat[idx] = 0; // B
// flatMat[idx+1] = 0; // G
// flatMat[idx+2] = 255; // R
// // System.out.println(flatMat[idx] + " " + flatMat[idx+1] + " " +
// flatMat[idx+2]);
// }
// temp.put(0, 0, flatMat);
// temp.convertTo(mat, CvType.CV_8UC3);
// for (int idx = 0; idx<60
// /*mat.total()*mat.channels()*/;idx=idx+mat.channels())
// {
// System.out.println(flatMat[idx] + " " + flatMat[idx+1] + " " +
// flatMat[idx+2]);
// }
// //
// // end example process each pixel with conversion to short

// // example process each pixel with byte
// //
// // OpenCV data are 0 to 255, which is 0 to 127 then -128 to -1, 255 is -1 in
// Java, 254 is -2, etc
// byte[] flatMat = new byte[(int)mat.total()*mat.channels()];
// mat.get(0, 0, flatMat);
// int idx = 0;
// int red;
// for (int idxR = 0; idxR<mat.rows(); idxR++)
// for (int idxC = 0; idxC<mat.cols(); idxC++)
// {
// smoothly increase red from 0 to almost 255 from top row to bottom row
// same value of red across the whole row - all columns are the same red
// flatMat[idx] = 0; // B
// flatMat[idx+1] = 0; // G
// red = idxR *2;
// if (red > 127) red = red - 256;
// if (red < -128) red = -128;
// flatMat[idx+2] = (byte)red; // R -1 is actually 255, -2 is 254, etc. -128
// medium dark
// idx = idx + mat.channels();
// // System.out.println(flatMat[idx] + " " + flatMat[idx+1] + " " +
// flatMat[idx+2]);
// }
// for (int idx2 = 0; idx2<mat.total()*mat.channels();
// idx2=idx2+(mat.channels()*mat.cols()))
// {
// System.out.println(flatMat[idx2] + " " + flatMat[idx2+1] + " " +
// flatMat[idx2+2]);
// }
// mat.put(0, 0, flatMat);
// //
// // end example process each pixel with byte

// // example of smoothing values - may help any jitters or maybe useless
// // example is the first Hu moment
// import edu.wpi.first.wpilibj.LinearFilter;
// LinearFilter f = LinearFilter.movingAverage(10); // factory method is
// self-instantiating; argument is number of samples to average
// double[] huTemp = new double[7];
// hu.get(0, 0, huTemp);
// //System.out.println(huTemp[0] + ", smooth=" + f.calculate(huTemp[0]));
// //-np.sign(hu) * np.log10(np.abs(hu)) log of Hu may be an improvement (np is
// NumPy; use Math in Java)

// // draw stars at corners of rectangle
// Imgproc.drawMarker(mat, new Point( boxPts[0].x,
// Math.min(boxPts[0].y,(double)mat.cols() - 10.) ),
// new Scalar( 0, 255, 0), Imgproc.MARKER_STAR, 7);// green - always max y - can
// be > image max y
// Imgproc.drawMarker(mat, boxPts[1], new Scalar(255, 255, 0),
// Imgproc.MARKER_STAR, 7);// teal then cw from 0
// Imgproc.drawMarker(mat, boxPts[2], new Scalar( 0, 255, 255),
// Imgproc.MARKER_STAR, 7);// yellow
// Imgproc.drawMarker(mat, boxPts[3], new Scalar(255, 0, 255),
// Imgproc.MARKER_STAR, 7);// magenta

// //could add additional filters if multiple objects found such as Moments or
// Hu moments
// //risk in that, too, since the target is significantly distorted at the edges
// of the camera range - close and far, left and right
// //see various tutorials such as
// https://www.learnopencv.com/shape-matching-using-hu-moments-c-python/
//
// //start of moment example code hacked from various sources so to use fix it
// up
// //look at reference to finish with matchShapes, etc
// Moments p = Imgproc.moments(contour);
// Point centerOfMass = new Point(moments.m10 / moments.m00, moments.m01 /
// moments.m00, 0);
// int x = (int) (p.get_m10() / p.get_m00());
// int y = (int) (p.get_m01() / p.get_m00());
// //add 1e-5 to avoid division by zero
// mc.add(new Point(mu.get(i).m10 / (mu.get(i).m00 + 1e-5), mu.get(i).m01 /
// (mu.get(i).m00 + 1e-5)));
// //draw the COM as a circle
// Core.circle(smallFrame, new Point(x, y), 4, colors[handIndex]);

// // find the simpler version of the contour - fewer points
// MatOfPoint2f temp = new
// MatOfPoint2f(filteredContours.get(contourIndex).toArray());
// MatOfPoint2f approxCurve = new MatOfPoint2f();
// Imgproc.approxPolyDP(
// temp, // Mat input curve
// approxCurve, // Mat ouptut simpler curve samr type as input
// 0.01*Imgproc.arcLength(temp, true), // double This is the maximum distance between the original curve and its approximation.
// true);// If true, the approximated curve is closed (its first and last vertices are connected). Otherwise, it is not closed.
// arcLength is perimeter of closed contour if true or curve length
// moments = Imgproc.moments(approxCurve);
// List<Point> listApprox = new ArrayList<Point>(approxCurve.toList());
// System.out.println(temp.size() + " " + listApprox.size()); // 1x86 7
// // draw the simple contour
// for (int idx = 0; idx< listApprox.size(); idx++)
// {
// Imgproc.line(
// mat,
// listApprox.get(idx), // one end of the line
// idx+1==listApprox.size() ? listApprox.get(0): listApprox.get(idx+1), // other
// end - close the shape on the last point
// new Scalar(255, 255, 0),
// 1,
// Imgproc.LINE_AA,
// 0);
// }

// example of drawing lines the point to point way
// for(int i = 0; i<4; i++)
// {
// Imgproc.line(mat, boxPts[i], boxPts[(i+1)%4], new Scalar(255, 0, 255));
// }

// EXAMPLE DUMP OF CONTOURS and APPROXIMATE CONTOURS
/*
 * System.out.println(temp + " " + temp.dump()); Mat [ 86*1*CV_32FC2,
 * isCont=true, isSubmat=false, nativeObj=0x61c281b8, dataAddr=0x60ee1bc0 ]
 * [407, 211; 407, 212; 406, 213; 406, 221; 405, 222; 405, 225; 404, 226; 404,
 * 229; 403, 230; 403, 234; 402, 235; 402, 241; 403, 242; 405, 242; 406, 243;
 * 408, 243; 409, 244; 412, 244; 413, 245; 415, 245; 416, 246; 419, 246; 420,
 * 247; 424, 247; 425, 248; 428, 248; 429, 249; 431, 249; 432, 250; 434, 250;
 * 435, 251; 437, 251; 438, 252; 441, 252; 442, 253; 443, 253; 444, 254; 446,
 * 254; 447, 255; 453, 255; 455, 253; 455, 252; 456, 251; 456, 250; 457, 249;
 * 457, 247; 458, 246; 458, 244; 459, 243; 459, 241; 460, 240; 460, 237; 461,
 * 236; 461, 233; 462, 232; 462, 226; 460, 226; 459, 225; 457, 225; 456, 224;
 * 454, 224; 453, 223; 450, 223; 449, 222; 447, 222; 446, 221; 444, 221; 443,
 * 220; 441, 220; 440, 219; 438, 219; 437, 218; 434, 218; 433, 217; 431, 217;
 * 430, 216; 428, 216; 427, 215; 425, 215; 424, 214; 422, 214; 421, 213; 419,
 * 213; 418, 212; 415, 212; 414, 211] System.out.println(approxCurve + " " +
 * approxCurve.dump()); Mat [ 7*1*CV_32FC2, isCont=true, isSubmat=false,
 * nativeObj=0x59660870, dataAddr=0x59660900 ] [388, 213; 384, 247; 434, 261;
 * 443, 259; 449, 234; 447, 227; 396, 212]
 */

// Pieces of using minAreaRect
// RotatedRect boundRectAngled; // min area rotated rectangle
// boundRectAngled= Imgproc.minAreaRect(NewMtx); // maybe a measurement of the perspective distortion
// from being off center
// double angleInnerPort = boundRectAngled.angle;
// if (angleInnerPort < -45.) angleInnerPort += 90.; // making assumptions about
// the width/height ratio
// System.out.println("minAreaRect angle:" + angleInnerPort);


// listMidContour.add(new MatOfPoint(boxPts[0], boxPts[1], boxPts[2], boxPts[3]));

