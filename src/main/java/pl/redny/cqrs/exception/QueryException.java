package pl.redny.cqrs.exception;

public class QueryException extends Exception {

    private static final long serialVersionUID = 9143605894838728186L;

    public QueryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public QueryException(final String message) {
        super(message);
    }

    public QueryException(final Throwable cause) {
        super(cause);
    }

}
