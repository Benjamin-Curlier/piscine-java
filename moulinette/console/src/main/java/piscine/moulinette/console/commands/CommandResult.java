package piscine.moulinette.console.commands;

public record CommandResult(int exitCode, String output, boolean shouldExit) {
    public static CommandResult ok(String out) { return new CommandResult(0, out, false); }
    public static CommandResult error(String msg) { return new CommandResult(1, msg, false); }
    public static CommandResult exit() { return new CommandResult(0, "", true); }
}
