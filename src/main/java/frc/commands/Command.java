package frc.commands;

public interface Command
{
    void init();
    void execute();
    boolean isFinished();
    void end();
}
