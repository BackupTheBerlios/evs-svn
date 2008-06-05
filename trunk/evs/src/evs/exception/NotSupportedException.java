package evs.exception;

public class NotSupportedException extends MiddlewareException {
	
    private static final long serialVersionUID = 6546682723284534603L;

	public NotSupportedException() {
		super();
	}

	public NotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotSupportedException(String message) {
		super(message);
	}

	public NotSupportedException(Throwable cause) {
		super(cause);
	}
}
