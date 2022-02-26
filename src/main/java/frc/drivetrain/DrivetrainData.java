package frc.drivetrain;

import edu.wpi.first.wpilibj.SerialPort;

public class DrivetrainData
{
    public final SwerveModuleData frontLeftSwerveModule;
    public final SwerveModuleData frontRightSwerveModule;
    public final SwerveModuleData backLeftSwerveModule;
    public final SwerveModuleData backRightSwerveModule;
    public final SerialPort.Port navXChannel;

    public DrivetrainData(SwerveModuleData frontLeftSwerveModule,
                          SwerveModuleData frontRightSwerveModule,
                          SwerveModuleData backLeftSwerveModule,
                          SwerveModuleData backRightSwerveModule,
                          SerialPort.Port navXChannel)
    {
        this.frontLeftSwerveModule = frontLeftSwerveModule;
        this.frontRightSwerveModule = frontRightSwerveModule;
        this.backLeftSwerveModule = backLeftSwerveModule;
        this.backRightSwerveModule = backRightSwerveModule;
        this.navXChannel = navXChannel;
    }
}
