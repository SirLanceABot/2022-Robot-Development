package frc.vision;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Vision
{

  private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static
  {
      System.out.println("Loading: " + fullClassName);
  }

  private static AcquireHubImage target;
  private static Thread targetThread;
  static NetworkTableEntry calibrate;
  static double calibrateAngle = 0.;

  static Object tabLock = new Object(); // synchronizing lock between this program and the related Shuffleboard tab
  static ShuffleboardTab cameraTab;

  public Vision() 
  {
  // start thread for target camera and locate target
  System.out.println(fullClassName + " : Constructor Started");

  // Create the Camera tab on the shuffleboard
  synchronized(Vision.tabLock)
  {
      Vision.cameraTab = Shuffleboard.getTab("Camera");
  }
  
  target = new AcquireHubImage();
  targetThread = new Thread(target, "TargetCamera");
  targetThread.setDaemon(true);
  targetThread.start();
        
  Timer.delay(5.);
  
  System.out.println(fullClassName + ": Constructor Finished");
  // END start target camera and locate target  
  }

  public void getCalibration()
  {
    // human input interface can be slow - put in slow addPeriodic in Robot
    if(calibrate != null)
      calibrateAngle = calibrate.getDouble(0.0); // get the camera calibration from the Shuffleboard
  }
}
