package piscine.moulinette.console.commands;

import piscine.moulinette.console.repl.ReplContext;
import java.util.List;

public final class ExitCommand implements Command {
    @Override public String name() { return "exit"; }
    @Override public String shortHelp() { return "exit — quitte le REPL"; }
    @Override public CommandResult execute(ReplContext ctx, List<String> args) { return CommandResult.exit(); }
}
