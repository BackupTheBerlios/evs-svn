package evs.exception;

public class MiddlewareException extends RemotingException{
	
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
