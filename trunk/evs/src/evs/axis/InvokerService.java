/**
 * 
 */
package evs.axis;

import evs.core.Common;
import evs.exception.RemotingException;
import evs.interfaces.IInvocationDispatcher;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class InvokerService {
	
	private IInvocationDispatcher invocationDispatcher;
	
	public InvokerService() {
		invocationDispatcher = Common.getInvocationDispatcher();
	}
	
	public byte[] invoke(byte[] request) {
		byte[] response;
		try {
			response = invocationDispatcher.invoke(request);
		} catch (RemotingException e) {
			e.printStackTrace();
			response = new byte[0];
		}
		return response;
	}

	public void fireAndForget(byte[] request) {
		try {
			invocationDispatcher.invoke(request);
		} catch (RemotingException e) {
			e.printStackTrace();
		}
	}

}
