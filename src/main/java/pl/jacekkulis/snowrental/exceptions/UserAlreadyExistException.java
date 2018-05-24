package pl.jacekkulis.snowrental.exceptions;

public final class UserAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 219515806722883669L;

	public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
