package frc.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

public class CommandListExecuter
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private static enum CommandState
    {
        kInit, kExecute, kEnd;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private static int currentCommandIndex = 0;
    private CommandState currentCommandState = CommandState.kInit;
    private static Command currentCommand;
    private static ArrayList<Command> commandList;// = autoBuilder.getCommandList();


    // *** CLASS CONSTRUCTOR ***
    public CommandListExecuter()
    {
        
    }


    // *** CLASS & INSTANCE METHODS ***
    public void executeCommandList()
    {
        if(currentCommandIndex < commandList.size())
        {
            currentCommand = commandList.get(currentCommandIndex);

            switch(currentCommandState)
            {
            case kInit:
                System.out.println("Initializing the Command number: " + currentCommandIndex);
                // initCommand(currentCommand);
                currentCommand.init();
                currentCommandState = CommandState.kExecute;
                break;

            case kExecute:
                System.out.println("Running the Command number: " + currentCommandIndex);
                // executeCommand(currentCommand);
                currentCommand.execute();
                if(currentCommand.isFinished())
                    currentCommandState = CommandState.kEnd;
                break;

            case kEnd:
                System.out.println("Ending the Command number: " + currentCommandIndex);
                // endCommand(currentCommand);
                currentCommand.end();
                currentCommandIndex++;
                currentCommandState = CommandState.kInit;
                break;
            }
        }
        else
        {
            System.out.println("Done with Autonomous Command List");
        }
    }
}