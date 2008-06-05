package evs.exception;

public class RequestException extends MiddlewareException {

    private static final long serialVersionUID = -6807406874423945176L;

	public RequestException() {
		super();
	}

	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestException(String message) {
		super(message);
	}

	public RequestException(Throwable cause) {
		super(cause);
	}
	
}
