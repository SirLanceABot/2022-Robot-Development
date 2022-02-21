package frc.vision;

public class Vision
{
    private static final String className = new String("[Vision]");
    
    // Static Initializer Block
    static
    {
        System.out.println(className + " : Class Loading");
    }

    private static boolean isConnected = false;

    private static UdpReceive udpReceive = new UdpReceive(5800); // port must match what the RPi is sending on;
    private static Thread udpReceiverThread = new Thread(udpReceive, "4237UDPreceive");
    public static TargetDataB turretNext = new TargetDataB();
    public static TargetDataE intakeNext = new TargetDataE();

    private static Vision instance = new Vision();
    
    private Vision()
    {
        System.out.println(className + " : Constructor Started");

        udpReceiverThread.start();
        
        System.out.println(className + ": Constructor Finished");
    }

    public static Vision getInstance()
    {
        return instance;
    }

    public static void setIsConnected()
    {
        isConnected = true;
    }

    public static boolean isConnected()
    {
        return isConnected;
    }

    public TargetDataB getTurret()
    {
        return turretNext.get();
    }

    public TargetDataE getIntake()
    {
        return intakeNext.get();
    }
}
