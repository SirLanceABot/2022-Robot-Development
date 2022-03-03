package frc.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import frc.robot.RobotContainer;
import frc.shuffleboard.AutonomousTabData;
import frc.shuffleboard.AutonomousTabData.MoveDelay;
import frc.shuffleboard.AutonomousTabData.MoveOffTarmac;
import frc.shuffleboard.AutonomousTabData.OrderOfOperations;
import frc.shuffleboard.AutonomousTabData.PickUpCargo;
import frc.shuffleboard.AutonomousTabData.ShootDelay;
import frc.shuffleboard.AutonomousTabData.ShootCargoAmount;
import frc.components.Shooter;


// TODO: Create option to move out to cargo location but not pick it up

public class AutonomousCommandList
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    private static enum CommandState
    {
        kInit, kExecute, kEnd, kAllDone;
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final AutonomousTabData AUTONOMOUS_TAB_DATA = RobotContainer.AUTONOMOUS_TAB_DATA;

    private static int currentCommandIndex = 0;
    private static CommandState currentCommandState = CommandState.kAllDone;
    private static boolean headerNeedsToBeDisplayed = false;
    private static Command currentCommand;

    private static final ArrayList<Command> commandList = new ArrayList<>();


    // *** CLASS CONSTRUCTOR ***
    public AutonomousCommandList()
    {

    }


    // *** CLASS & INSTANCE METHODS ***
    public void build()
    {
        // Clear the command list
        commandList.clear();
        
        switch (AUTONOMOUS_TAB_DATA.orderOfOperations)
        {
        case kMoveFirst:
            move();
            shoot();
            break;
        case kShootFirst:
            shoot();
            move();
            break;
        case kShootMoveShoot:
            shootMoveShoot();
            break;
        case kDoNothing:
            break;
        }
    }

    // TODO: get actual drive speeds and distances
    private void move()
    {
        if (AUTONOMOUS_TAB_DATA.moveOffTarmac == MoveOffTarmac.kYes)
        {
            if (AUTONOMOUS_TAB_DATA.moveDelay != MoveDelay.k0)
            {
                addCommand(new Wait(AUTONOMOUS_TAB_DATA.moveDelay.value));
            }
            
            if (AUTONOMOUS_TAB_DATA.pickUpCargo == PickUpCargo.kYes)
            {
                addCommand(new TurnOnIntake());
                addCommand(new DriveStraight(1.0, 5.0));
                addCommand(new StopDriving());
                addCommand(new TurnOffIntake());
            }
            else
            {
                addCommand(new DriveStraight(1.0, 2.0));
                addCommand(new StopDriving());
            }
        }
    }

    // TODO: get actual shooter distances
    private void shoot()
    {
        if (AUTONOMOUS_TAB_DATA.shootDelay != ShootDelay.k0)
        {
             addCommand(new Wait(AUTONOMOUS_TAB_DATA.shootDelay.value));
        }

        if (AUTONOMOUS_TAB_DATA.orderOfOperations == OrderOfOperations.kMoveFirst)
        {
            if (AUTONOMOUS_TAB_DATA.shootCargoAmount != ShootCargoAmount.k0)
            {
                if (AUTONOMOUS_TAB_DATA.pickUpCargo == PickUpCargo.kYes)
                {
                    addCommand(new ShootCargo(AUTONOMOUS_TAB_DATA.shootCargoAmount.value, 5.0, Shooter.Hub.kUpper));
                }
                else
                {
                    addCommand(new ShootCargo(AUTONOMOUS_TAB_DATA.shootCargoAmount.value, 3.0, Shooter.Hub.kUpper));
                }
            }
        }

        if (AUTONOMOUS_TAB_DATA.orderOfOperations == OrderOfOperations.kShootFirst)
        {
            addCommand(new ShootCargo(1, 1.0, Shooter.Hub.kLower));
        }
    }

    private void shootMoveShoot()
    {
        addCommand(new ShootCargo(1, 1.0, Shooter.Hub.kLower));

        if (AUTONOMOUS_TAB_DATA.moveDelay != MoveDelay.k0)
        {
            addCommand(new Wait(AUTONOMOUS_TAB_DATA.moveDelay.value));
        }

        addCommand(new TurnOnIntake());
        addCommand(new DriveStraight(1.0, 5.0));
        addCommand(new StopDriving());
        addCommand(new TurnOffIntake());

        if (AUTONOMOUS_TAB_DATA.shootDelay != ShootDelay.k0)
        {
             addCommand(new Wait(AUTONOMOUS_TAB_DATA.shootDelay.value));
        }

        addCommand(new ShootCargo(1, 5.0, Shooter.Hub.kUpper));
    }

    private void addCommand(Command command)
    {
        commandList.add(command);
    }

    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList
    public void init()
    {
        currentCommandIndex = 0;
        headerNeedsToBeDisplayed = true;

        if(currentCommandIndex < commandList.size())
        {
            currentCommand = commandList.get(0);
            currentCommandState = CommandState.kInit;
        }
        else
        {
            currentCommandState = CommandState.kAllDone;
        }
    }

    public void execute()
    {
        switch(currentCommandState)
        {
        case kInit:
            System.out.println("Initializing command number: " + currentCommandIndex);
            
            currentCommand.init();
            currentCommandState = CommandState.kExecute;
            break;
        case kExecute:
            if(headerNeedsToBeDisplayed)
            {
                System.out.println("Executing command number: " + currentCommandIndex);
                headerNeedsToBeDisplayed = false;
            }
            
            currentCommand.execute();
            if(currentCommand.isFinished())
            {
                currentCommandState = CommandState.kEnd;
                headerNeedsToBeDisplayed = true;
            }
            break;
        case kEnd:
            System.out.println("Ending command number: " + currentCommandIndex);
            
            currentCommand.end();
            currentCommandIndex++;
            if(currentCommandIndex < commandList.size())
            {
                currentCommandState = CommandState.kInit;
                currentCommand = commandList.get(currentCommandIndex);
            }
            else
            {
                currentCommandState = CommandState.kAllDone;
            }
            break;
        case kAllDone:
            if(headerNeedsToBeDisplayed)
            {
                System.out.println("Done with Autonomous Command List");
                headerNeedsToBeDisplayed = false;
            }
            break;
        }
    }

    public void end()
    {
        currentCommandIndex = 0;
        headerNeedsToBeDisplayed = false;
        currentCommandState = CommandState.kAllDone;
        // currentCommand = null;
        // commandList.clear();
    }

    public String toString()
    {
        String str = "";

        str += "\n***** AUTONOMOUS COMMAND LIST *****\n";
        for(Command command : commandList)
        {
            str += command + "\n";
        }

        return str;
    }
}
