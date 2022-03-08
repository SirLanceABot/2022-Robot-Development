package frc.vision;

/*

 Camera Vision Process identifies the target and its position with respect to the camera.

 It produces an angle of deviation between the camera and target and distance to the target.

 Those data are presented in visual form on the Shuffleboard and are available programmatically
 for automated driving the robot toward the target for accurate shooting.

 Camera Vision also connects the intake camera for human vision to the Shuffleboard.

 Camera Vision Processing starts here with the instantiation of this Vision class.

 The Vision class constructor spawns all the tasks for Vision except for two periodic
 functions defined in Robot to run on a different cycle than the rest of Vision.

 The two exceptions are the polling of the ShuffleBoard for the camera calibration often
 enough to avoid irritating humans entering the number and running Java garbage collection
 every few seconds to free up the native memory allocated by OpenCV Mats.

 Vision Processing is two child threads from this parent.  One thread captures the
 target camera image and the other thread processes new images as fast as it can to
 identify the target.

 Flow of this Vision class:
 
 Establishes the ShuffleBoard Camera tab

 Creates the AcquireHubImage thread that acquires the target camera image and starts
 the rest of the image processing including the human intake camera and target identification.

 This class also provides the home for acquiring the camera calibration from the ShuffleBoard
 and passing that data to its uses.

*/

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
