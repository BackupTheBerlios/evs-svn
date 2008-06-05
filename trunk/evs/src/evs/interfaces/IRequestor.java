/**
 * 
 */
package evs.interfaces;

import evs.core.InvocationStyle;
import evs.exception.RemotingException;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IRequestor {
	
	/**
	 * @param object specifies the invocation object containing the object id, method + parameters
	 * @param isVoid specifies whether the client expects a response
	 * @param callback specifies the callback from and to the client
	 * @param act asynchronous communication token from the client
	 * @return the response. This is always null for asynchronous requests.
	 */
	public Object invoke(IInvocationObject object, boolean isVoid, ICallback callback, IACT act, InvocationStyle style) throws RemotingException;


	/**
	 * @param act asynchronous communication token
	 * @param result result from call of remote object
	 */
	public void returnResult(IACT act, byte[] result);
	
}
