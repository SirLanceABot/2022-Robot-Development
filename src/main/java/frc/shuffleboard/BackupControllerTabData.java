package frc.shuffleboard;

public class BackupControllerTabData 
{
  public static enum ClimbStage1Toggle
  {
    kOff, kUp, kDown 
  }

  public static enum ClimbStage2Toggle
  {
    kOff, kUp, kDown 
  }

  public static enum IntakeToggle 
  {
    kOff, kIn, kOut 
  }
  public static enum EnableBackupController
  {
    kOff, kOn
  }
  public Boolean compressorToggle = false;
  public Boolean aim = false;
  public Boolean shoot = false;
  public Boolean rollerToggle = false;
  public ClimbStage1Toggle climbStage1Toggle = ClimbStage1Toggle.kOff;
  public ClimbStage2Toggle climbStage2Toggle = ClimbStage2Toggle.kOff;
  public IntakeToggle intakeToggle = IntakeToggle.kOff; 
  public EnableBackupController enableBackupController = EnableBackupController.kOff;
}
