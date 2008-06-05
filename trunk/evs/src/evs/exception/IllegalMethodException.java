package evs.exception;

public class IllegalMethodException extends MiddlewareException {

    private static final long serialVersionUID = 4837582298696299766L;

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
