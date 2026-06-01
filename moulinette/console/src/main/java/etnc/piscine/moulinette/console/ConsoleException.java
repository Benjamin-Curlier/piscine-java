package etnc.piscine.moulinette.console;

public class ConsoleException extends RuntimeException {
    public ConsoleException(String message) { super(message); }
    public ConsoleException(String message, Throwable cause) { super(message, cause); }
}
