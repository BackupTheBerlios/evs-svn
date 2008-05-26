package evs.exception;

public class IllegalObjectException extends MiddlewareException {

	public IllegalObjectException() {
		super();
	}

	public IllegalObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalObjectException(String message) {
		super(message);
	}

	public IllegalObjectException(Throwable cause) {
		super(cause);
	}
}
