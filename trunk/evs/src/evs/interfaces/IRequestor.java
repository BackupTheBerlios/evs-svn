/**
 * 
 */
package evs.interfaces;

import evs.core.InvocationObject;
import evs.exception.RemotingException;
import evs.exception.RequestException;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IRequestor {
	
	/**
	 * @param object specifies the invocation object containing the object id, method + parameters
	 * @param isVoid specifies whether the client expects a response
	 * @return the response. This is always null for asynchronous requests.
	 */
	public Object invoke(IInvocationObject object, boolean isVoid) throws RemotingException;
	
	
	/**
	 * 
	 * @param object specifies the object to receive the request.
	 * @param method specifies the method to invoke.
	 * @param arguments specifies and provides the arguments, which are passed to the method.
	 * @return the response. This is always null for asynchronous requests.
	 */
	//public Object invoke(IAOR object, Object method, Object... arguments);



}
