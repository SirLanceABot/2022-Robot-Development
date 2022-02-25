package frc.vision;

import org.opencv.core.Mat;

/**
 * image from camera in OpenCV Mat form
 */
public class Image
{
    private Mat mat = new Mat();
    private boolean isFreshImage = false;

    public synchronized void setImage(Mat mat)
    {
        mat.copyTo(this.mat);
        this.isFreshImage = true;
        notify(); // fresh image so tell whoever is waiting for it
    }

    public synchronized void getImage(Mat mat)
    {
        try
        {
            while(!this.isFreshImage) // make sure awakened for the right reason
            {
                wait(0L, 0); // stale image so wait for a new image no timeout
            }
        } 
        catch (Exception e)
        {
            System.out.println("getImage Exception " + e.toString());
            throw new RuntimeException(e);
        }
        this.isFreshImage = false;
        this.mat.copyTo(mat);
    }

    public synchronized boolean isFreshImage()
    {
        return this.isFreshImage;
    }
}