package frc.drivetrain;

import edu.wpi.first.math.geometry.Translation2d;

public class SwerveModuleConfig
{
    public final String moduleName;
    public final Translation2d moduleLocation;
    public final int driveMotorChannel;
    public final boolean driveMotorInverted;
    public final int turnEncoderChannel;
    public final double turnEncoderOffset;
    public final int turnMotorChannel;

    /**
     * @param moduleName
     * @param driveMotorChannel
     * @param driveMotorInverted
     * @param turnEncoderChannel
     * @param turnEncoderOffset
     * @param turnMotorChannel
     */
    public SwerveModuleConfig(String moduleName,
                            Translation2d moduleLocation,
                            int driveMotorChannel, 
                            boolean driveMotorInverted, 
                            int turnEncoderChannel, 
                            double turnEncoderOffset, 
                            int turnMotorChannel)
    {
        this.moduleName = moduleName;
        this.moduleLocation = moduleLocation;
        this.driveMotorChannel = driveMotorChannel;
        this.driveMotorInverted = driveMotorInverted;
        this.turnEncoderChannel = turnEncoderChannel;
        this.turnEncoderOffset = turnEncoderOffset;
        this.turnMotorChannel = turnMotorChannel;
    }

    // TODO: Use more formatting to make header same spaces for toString
    public String toString()
    {
        StringBuilder sb = new StringBuilder(400);
        
        sb.append("   Name       DriveMotor     Turn Encoder  Turn Motor\n");
        sb.append("           Channel Inverted Channel Offset   Channel\n");

        sb.append(String.format("%10s  %3d     %5b    %3d    %6.1f    %3d\n",
            moduleName,
            driveMotorChannel,
            driveMotorInverted,
            turnEncoderChannel,
            turnEncoderOffset,
            turnMotorChannel));

        return sb.toString();
    }
}

