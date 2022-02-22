package frc.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import frc.robot.RobotContainer;

public class CommandListBuilder
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private static final ArrayList<Command> AUTO_COMMAND_LIST = RobotContainer.AUTO_COMMAND_LIST;


    // *** CLASS CONSTRUCTOR ***
    public CommandListBuilder()
    {

    }


    // *** CLASS & INSTANCE METHODS ***
    /**
     * only adds one command to the node, aka a sequential command
     * @param sequentialCommand
     */
    public static void addCommand(Command sequentialCommand)
    {
        AUTO_COMMAND_LIST.add(sequentialCommand);
    }

    public void buildCommandList()
    {
        addCommand(new Wait(5.0));
        addCommand(new DriveDistance(2.0, 4.0));


        System.out.println("Command List built");
    }
}