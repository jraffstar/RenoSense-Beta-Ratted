package me.renosense.beta.features.command.commands;

import me.renosense.beta.RenoSense;
import me.renosense.beta.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        RenoSense.reload();
    }
}

