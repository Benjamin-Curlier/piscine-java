package piscine.moulinette.console.git;

public record GitResult(int exitCode, String stdout, String stderr) {
    public boolean ok() { return exitCode == 0; }
}
