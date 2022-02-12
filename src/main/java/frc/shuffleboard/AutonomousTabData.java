package frc.shuffleboard;



// Create a class to hold the data on the Shuffleboard tab
public class AutonomousTabData
{
    public static enum StartingLocation
    {
        kLeft, kMiddle, kRight;
    }

    //-------------------------------------------------------------------//

    public static enum OrderOfOperations
    {
        kShootFirst, kMoveFirst, kDoNothing;
    }

    //-------------------------------------------------------------------//

    public static enum ShootCargo
    {
        k0, k1, k2;
    }

    public static enum ShootDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    //-------------------------------------------------------------------//

    public static enum MoveOffTarmac
    {
        kYes, kNo;
    }

    public static enum MoveDelay
    {
        k0, k1, k2, k3, k4, k5;
    }

    //-------------------------------------------------------------------//

    public static enum PickUpCargo
    {
        kYes, kNo;
    }

    public StartingLocation startingLocation = StartingLocation.kMiddle;

    public OrderOfOperations orderOfOperations = OrderOfOperations.kDoNothing;

    public ShootCargo shootCargo = ShootCargo.k0;
    public ShootDelay shootDelay = ShootDelay.k0;

    public MoveOffTarmac moveOffTarmac = MoveOffTarmac.kYes;
    public MoveDelay moveDelay = MoveDelay.k0;

    public PickUpCargo pickUpCargo = PickUpCargo.kYes;

    @Override
    public String toString()
    {
        String str = "";

        str += " \n";
        str += "*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location     : "  + startingLocation   + "\n";
        str += "Order of Operations   : "  + orderOfOperations  + "\n";
        str += "Shoot Cargo           : "  + shootCargo         + "\n";
        str += "Shoot Delay           : "  + shootDelay         + "\n";
        str += "Move Off Tarmac       : "  + moveOffTarmac      + "\n";
        str += "Move Delay            : "  + moveDelay          + "\n";
        str += "Pick Up Cargo         : "  + pickUpCargo        + "\n";

        return str;
    }

    public void updateData(AutonomousTabData atd)
    {
        startingLocation = atd.startingLocation;
        orderOfOperations = atd.orderOfOperations;
        shootCargo = atd.shootCargo;
        shootDelay = atd.shootDelay;
        moveOffTarmac = atd.moveOffTarmac;
        moveDelay = atd.moveDelay;
        pickUpCargo = atd.pickUpCargo;
    }
}