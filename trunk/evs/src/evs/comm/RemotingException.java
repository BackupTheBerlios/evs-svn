/**
 * 
 */
package evs.comm;

/**
 * wraps exceptions thrown by the communication layer.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 */
@SuppressWarnings("serial")
public class RemotingException extends Exception {

	public RemotingException() {
		super();
	}

	public RemotingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemotingException(String message) {
		super(message);
	}

	public RemotingException(Throwable cause) {
		super(cause);
	}
	
}
