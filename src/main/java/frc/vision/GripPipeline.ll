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
	Mat cvTransposeOutput;
	Mat cvFlipOutput;
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

Step CV_transpose0
{
    Mat cvTransposeSrc1 = source0;

    cvTranspose(cvTransposeSrc1, cvTransposeOutput);
}

Step CV_flip0
{
    Mat cvFlipSrc = cvTransposeOutput;
    FlipCode cvFlipFlipcode = X_AXIS;

    cvFlip(cvFlipSrc, cvFlipFlipcode, cvFlipOutput);
}

Step RGB_Threshold0
{
    Mat rgbThresholdInput = cvFlipOutput;
    List rgbThresholdRed = [0.0, 179.99999999999997];
    List rgbThresholdGreen = [0.0, 179.99999999999997];
    List rgbThresholdBlue = [0.0, 180.0];

    rgbThreshold(rgbThresholdInput, rgbThresholdRed, rgbThresholdGreen, rgbThresholdBlue, rgbThresholdOutput);
}

Step Mask0
{
    Mat maskInput = cvFlipOutput;
    Mat maskMask = rgbThresholdOutput;

    mask(maskInput, maskMask, maskOutput);
}

Step Blur0
{
    Mat blurInput = maskOutput;
    BlurType blurType = BOX;
    Double blurRadius = 1.8867924528301887;

    blur(blurInput, blurType, blurRadius, blurOutput);
}

Step HSV_Threshold0
{
    Mat hsvThresholdInput = blurOutput;
    List hsvThresholdHue = [40.46762589928058, 80.17064846416379];
    List hsvThresholdSaturation = [89.0534182823233, 255.0];
    List hsvThresholdValue = [13.758992805755396, 255.0];

    hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);
}

Step CV_erode0
{
    Mat cvErode0Src = hsvThresholdOutput;
    Mat cvErode0Kernel;
    Point cvErode0Anchor = (-1, -1);
    Double cvErode0Iterations = 0.0;
    BorderType cvErode0Bordertype = BORDER_DEFAULT;
    Scalar cvErode0Bordervalue = (-1);

    cvErode(cvErode0Src, cvErode0Kernel, cvErode0Anchor, cvErode0Iterations, cvErode0Bordertype, cvErode0Bordervalue, cvErode0Output);
}

Step CV_dilate0
{
    Mat cvDilateSrc = cvErode0Output;
    Mat cvDilateKernel;
    Point cvDilateAnchor = (-1, -1);
    Double cvDilateIterations = 1.0;
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
    Double filterContoursMinArea = 0.0;
    Double filterContoursMinPerimeter = 0.0;
    Double filterContoursMinWidth = 0.0;
    Double filterContoursMaxWidth = 10000.0;
    Double filterContoursMinHeight = 0.0;
    Double filterContoursMaxHeight = 10000.0;
    List filterContoursSolidity = [60.263653483992464, 100.0];
    Double filterContoursMaxVertices = 1000000.0;
    Double filterContoursMinVertices = 2.0;
    Double filterContoursMinRatio = 0.1;
    Double filterContoursMaxRatio = 8.0;

    filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
}




