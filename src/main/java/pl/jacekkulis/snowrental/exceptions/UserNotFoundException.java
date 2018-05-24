package pl.jacekkulis.snowrental.exceptions;

public final class UserNotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 9089469470523561928L;

	public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException(final Throwable cause) {
        super(cause);
    }

}
