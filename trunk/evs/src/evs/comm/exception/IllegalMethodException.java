package evs.comm.exception;

public class IllegalMethodException extends MiddlewareException {
	
	public IllegalMethodException() {
		super();
	}

	public IllegalMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalMethodException(String message) {
		super(message);
	}

	public IllegalMethodException(Throwable cause) {
		super(cause);
	}
}
