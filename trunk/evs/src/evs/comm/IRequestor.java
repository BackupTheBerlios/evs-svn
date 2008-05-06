/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IRequestor {
	
	/**
	 * 
	 * @param object specifies the object to receive the request.
	 * @param method specifies the method to invoke.
	 * @param arguments specifies and provides the arguments, which are passed to the method.
	 * @return the response. This is always null for asynchronous requests.
	 */
	public Object invoke(IAOR object, Object method, Object... arguments);

}
