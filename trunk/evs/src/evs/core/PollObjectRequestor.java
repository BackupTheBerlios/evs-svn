/**
 * 
 */
package evs.core;

import java.net.InetSocketAddress;

import evs.exception.MarshallingException;
import evs.exception.RemotingException;
import evs.interfaces.IAOR;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInvocationObject;
import evs.interfaces.ILocation;
import evs.interfaces.IMarshaller;
import evs.interfaces.IPollObject;
import evs.interfaces.IPollObjectRequestor;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class PollObjectRequestor implements IPollObjectRequestor {
	
	private IInvocationObject invocationObject;
	private IMarshaller marshaller;
	private IPollObject pollObject;
	private IClientRequestHandler clientRequestHandler;
	
	public PollObjectRequestor(IInvocationObject invocationObject, IMarshaller marshaller, IPollObject pollObject) {
		this.invocationObject = invocationObject;
		this.marshaller = marshaller;
		this.pollObject = pollObject;
	}

	public void run() {
		byte[] request;
		try {
			request = marshaller.serialize(invocationObject);
		} catch (MarshallingException e) {
			pollObject.setException(e);
			return;
		}

		clientRequestHandler = Common.getClientRequesthandler();
		IAOR aor = invocationObject.getObjectReference();
		
		byte[] response;
		if (aor.isLocal()) {
			try {
				response = Common.getInvocationDispatcher().invoke(request);
			} catch (RemotingException e) {
				pollObject.setException(e);
				return;
			}
		} else {
			InetSocketAddress socketAddress = getInetSocketAddress(aor);
			try {
				response = clientRequestHandler.send(socketAddress,request);
			} catch (RemotingException e) {
				pollObject.setException(e);
				return;
			}
			if (response.length == 0) {
				Exception e = new RemotingException("The remote invocation failed.");
				pollObject.setException(e);
				return;
			}
		}
		
		IInvocationObject responseObject;
		try {
			responseObject = marshaller.deserialize(response);
		} catch (MarshallingException e) {
			pollObject.setException(e);
			return;
		}
		
		Object result = responseObject.getReturnParam();
		pollObject.setResult(result);
	}
	
	public Thread start() {
		Thread t = new Thread(this,this.getClass().getName());
		t.start();
		return t;
	}

	public void stop() {}

	private InetSocketAddress getInetSocketAddress(IAOR aor) {
		ILocation location = aor.getLocation();
		String hostName = location.getHostname();
		String portString = location.getPort();
		int port = Integer.parseInt(portString);
		InetSocketAddress socketAddress = new InetSocketAddress(hostName,port);
		return socketAddress;
	}
	
}
