//
// Inputs
//
Inputs
{
	Mat source0;
}

//
// Variables
//
Outputs
{
	Mat rgbThresholdOutput;
	Mat maskOutput;
	Mat blurOutput;
	Mat hsvThresholdOutput;
	Mat cvErode0Output;
	Mat cvDilateOutput;
	Mat cvErode1Output;
	ContoursReport findContoursOutput;
	ContoursReport filterContoursOutput;
}

//
// Steps
//

Step RGB_Threshold0
{
    Mat rgbThresholdInput = source0;
    List rgbThresholdRed = [0.0, 240.0];
    List rgbThresholdGreen = [0.0, 240.0];
    List rgbThresholdBlue = [0.0, 240.0];

    rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);
}

Step Mask0
{
    Mat maskInput = source0;
    Mat maskMask = rgbThresholdOutput;

    mask(maskInput, maskMask, maskOutput);
}

Step Blur0
{
    Mat blurInput = maskOutput;
    BlurType blurType = BOX;
    Double blurRadius = 1.8018018018018018;

    blur(blurInput, blurType, blurRadius, blurOutput);
}

Step HSV_Threshold0
{
    Mat hsvThresholdInput = blurOutput;
    List hsvThresholdHue = [40.46762589928058, 80.17064846416382];
    List hsvThresholdSaturation = [199.50539568345323, 255.0];
    List hsvThresholdValue = [13.758992805755396, 255.0];

    hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);
}

Step CV_erode0
{
    Mat cvErode0Src = hsvThresholdOutput;
    Mat cvErode0Kernel;
    Point cvErode0Anchor = (-1, -1);
    Double cvErode0Iterations = 1.0;
    BorderType cvErode0Bordertype = BORDER_CONSTANT;
    Scalar cvErode0Bordervalue = (-1);

    cvErode(cvErode0Src, cvErode0Kernel, cvErode0Anchor, cvErode0Iterations, cvErode0Bordertype, cvErode0Bordervalue, cvErode0Output);
}

Step CV_dilate0
{
    Mat cvDilateSrc = cvErode0Output;
    Mat cvDilateKernel;
    Point cvDilateAnchor = (-1, -1);
    Double cvDilateIterations = 2.0;
    BorderType cvDilateBordertype = BORDER_CONSTANT;
    Scalar cvDilateBordervalue = (-1);

    cvDilate(cvDilateSrc, cvDilateKernel, cvDilateAnchor, cvDilateIterations, cvDilateBordertype, cvDilateBordervalue, cvDilateOutput);
}

Step CV_erode1
{
    Mat cvErode1Src = cvDilateOutput;
    Mat cvErode1Kernel;
    Point cvErode1Anchor = (-1, -1);
    Double cvErode1Iterations = 1.0;
    BorderType cvErode1Bordertype = BORDER_CONSTANT;
    Scalar cvErode1Bordervalue = (-1);

    cvErode(cvErode1Src, cvErode1Kernel, cvErode1Anchor, cvErode1Iterations, cvErode1Bordertype, cvErode1Bordervalue, cvErode1Output);
}

Step Find_Contours0
{
    Mat findContoursInput = cvErode1Output;
    Boolean findContoursExternalOnly = true;

    findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
}

Step Filter_Contours0
{
    ContoursReport filterContoursContours = findContoursOutput;
    Double filterContoursMinArea = 12.0;
    Double filterContoursMinPerimeter = 14.0;
    Double filterContoursMinWidth = 4.0;
    Double filterContoursMaxWidth = 1000.0;
    Double filterContoursMinHeight = 6.0;
    Double filterContoursMaxHeight = 1000.0;
    List filterContoursSolidity = [60.22388059701493, 100.0];
    Double filterContoursMaxVertices = 1000000.0;
    Double filterContoursMinVertices = 0.0;
    Double filterContoursMinRatio = 0.2;
    Double filterContoursMaxRatio = 1.2;

    filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
}




