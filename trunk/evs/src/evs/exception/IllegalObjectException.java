package evs.exception;

public class IllegalObjectException extends MiddlewareException {

    private static final long serialVersionUID = -9193431443185554672L;

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
