// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.vision;

import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;

import org.opencv.core.Core;

/**
 * This is a demo program showing the use of OpenCV to do vision processing. The image is acquired
 * from the USB camera, then a rectangle is put on the image and sent to the dashboard. OpenCV has
 * many methods for different types of processing.
 */
public class RobotVision extends TimedRobot {
  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
}
  private static AcquireTarget target;
  private static Thread targetThread;

  @Override
  public void robotInit() {

    // // start intake camera for human driver - automatically acquires and passes through the image
    // UsbCamera IntakeCamera = CameraServer.startAutomaticCapture("intake", "/dev/v4l/by-id/usb-KYE_Systems_Corp._USB_Camera_200901010001-video-index0");
    // IntakeCamera.setResolution(160, 120);
    // IntakeCamera.setFPS(20);

    // start thread for target camera and locate target
    target = new AcquireTarget();
    targetThread = new Thread(target, "TargetCamera");
    targetThread.setDaemon(true);
    targetThread.start();

  }
}
