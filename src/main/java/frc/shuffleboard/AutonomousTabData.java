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
        k0(0), k1(1), k2(2);
        
        public int value;

        private ShootCargo(int value)
        {
            this.value = value;
        }
    }

    public static enum ShootDelay
    {
        k0(0.0), k1(1.0), k2(2.0), k3(3.0), k4(4.0), k5(5.0);
        
        public double value;

        private ShootDelay(double value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum MoveOffTarmac
    {
        kYes, kNo;
    }

    public static enum MoveDelay
    {
        k0(0.0), k1(1.0), k2(2.0), k3(3.0), k4(4.0), k5(5.0);
        
        public double value;

        private MoveDelay(double value)
        {
            this.value = value;
        }
    }

    //-------------------------------------------------------------------//

    public static enum PickUpCargo
    {
        kYes, kNo;
    }

    public StartingLocation startingLocation = StartingLocation.kMiddle;
    public OrderOfOperations orderOfOperations = OrderOfOperations.kMoveFirst;
    public ShootCargo shootCargo = ShootCargo.k0;
    public ShootDelay shootDelay = ShootDelay.k0;
    public MoveOffTarmac moveOffTarmac = MoveOffTarmac.kYes;
    public MoveDelay moveDelay = MoveDelay.k0;
    public PickUpCargo pickUpCargo = PickUpCargo.kNo;

    public String toString()
    {
        String str = "";

        str += "\n*****  AUTONOMOUS SELECTION  *****\n";
        str += "Starting Location     : "  + startingLocation   + "\n";
        str += "Order of Operations   : "  + orderOfOperations  + "\n";
        str += "Shoot Cargo           : "  + shootCargo         + "\n";
        str += "Shoot Delay           : "  + shootDelay         + "\n";
        str += "Move Off Tarmac       : "  + moveOffTarmac      + "\n";
        str += "Move Delay            : "  + moveDelay          + "\n";
        str += "Pick Up Cargo         : "  + pickUpCargo        + "\n";
        str += "\n";

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