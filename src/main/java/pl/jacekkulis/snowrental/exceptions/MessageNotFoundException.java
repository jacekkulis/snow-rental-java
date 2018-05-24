package pl.jacekkulis.snowrental.exceptions;

public final class MessageNotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 343214696995513880L;

	public MessageNotFoundException() {
        super();
    }

    public MessageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MessageNotFoundException(final String message) {
        super(message);
    }

    public MessageNotFoundException(final Throwable cause) {
        super(cause);
    }

}
