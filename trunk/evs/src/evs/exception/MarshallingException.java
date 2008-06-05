package evs.exception;

public class MarshallingException extends MiddlewareException {

    private static final long serialVersionUID = 1977456022016623949L;

	public MarshallingException() {
		super();
	}

	public MarshallingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MarshallingException(String message) {
		super(message);
	}

	public MarshallingException(Throwable cause) {
		super(cause);
	}
}
