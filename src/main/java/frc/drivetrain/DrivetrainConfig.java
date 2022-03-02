package frc.drivetrain;

import edu.wpi.first.wpilibj.SerialPort;

public class DrivetrainConfig
{
    public final SwerveModuleConfig frontLeftSwerveModule;
    public final SwerveModuleConfig frontRightSwerveModule;
    public final SwerveModuleConfig backLeftSwerveModule;
    public final SwerveModuleConfig backRightSwerveModule;
    public final SerialPort.Port navXChannel;

    public DrivetrainConfig(SwerveModuleConfig frontLeftSwerveModule,
                          SwerveModuleConfig frontRightSwerveModule,
                          SwerveModuleConfig backLeftSwerveModule,
                          SwerveModuleConfig backRightSwerveModule,
                          SerialPort.Port navXChannel)
    {
        this.frontLeftSwerveModule = frontLeftSwerveModule;
        this.frontRightSwerveModule = frontRightSwerveModule;
        this.backLeftSwerveModule = backLeftSwerveModule;
        this.backRightSwerveModule = backRightSwerveModule;
        this.navXChannel = navXChannel;
    }
}
