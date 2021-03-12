package pl.redny.cqrs.exception;

public class CommandException extends Exception {

    private static final long serialVersionUID = 3713926767575585063L;

    public CommandException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CommandException(final String message) {
        super(message);
    }

    public CommandException(final Throwable cause) {
        super(cause);
    }

}
