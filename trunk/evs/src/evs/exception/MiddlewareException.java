package evs.exception;

public class MiddlewareException extends RemotingException{
	
    private static final long serialVersionUID = 8986322148021165944L;

	public MiddlewareException() {
		super();
	}

	public MiddlewareException(String message, Throwable cause) {
		super(message, cause);
	}

	public MiddlewareException(String message) {
		super(message);
	}

	public MiddlewareException(Throwable cause) {
		super(cause);
	}
}
