package me.renosense.beta.features.command.commands;

import me.renosense.beta.RenoSense;
import me.renosense.beta.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        RenoSense.unload(true);
    }
}

