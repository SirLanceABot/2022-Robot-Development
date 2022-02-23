package frc.commands;

import java.lang.invoke.MethodHandles;

public class CommandTemplate implements Command 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** CLASS & INSTANCE VARIABLES ***
    private boolean isFinished;


    // *** CLASS CONSTRUCTOR ***
    public CommandTemplate()
    {
        isFinished = false;
    }

    // *** CLASS & INSTANCE METHODS ***
    public void init()
    {
        System.out.println(this);
        isFinished = false;
    }

    public void execute()
    {
        if(true)
        {
            isFinished = true;
        }
    }

    public boolean isFinished()
    {
        return isFinished;
    }

    public void end()
    {

    }

    public String toString()
    {
        return "CommandTemplate()";
    }
}