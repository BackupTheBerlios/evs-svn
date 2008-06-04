package evs.core;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import evs.exception.RemotingException;
import evs.exception.RequestException;
import evs.interfaces.IACT;
import evs.interfaces.IAOR;
import evs.interfaces.ICallback;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInterceptor;
import evs.interfaces.IInvocationObject;
import evs.interfaces.ILocation;
import evs.interfaces.IRequestor;

public class Requestor implements IRequestor {
	
	private Map<IACT, ICallback> clientCallbacks = new HashMap<IACT, ICallback>();
	
	public Object invoke(IInvocationObject object, boolean isVoid, ICallback callback, IACT act) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		byte[] marshalledRequest = Common.getMarshaller().serialize(object);
		byte[] marshalledResponse;
		
		IClientRequestHandler handler = Common.getClientRequesthandler();
		
		switch(object.getRequestType()){
			case SYNC:
				IAOR objectReference = object.getObjectReference();
				if (objectReference.isLocal()) {
					marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
				} else {
					ILocation location = objectReference.getLocation();
					String hostName = location.getHostname();
					String portString = location.getPort();
					int port = Integer.parseInt(portString);
					InetSocketAddress socketAddress = new InetSocketAddress(hostName,port);
					marshalledResponse = handler.send(socketAddress,marshalledRequest);
				}
				if (marshalledResponse.length == 0) {
					throw new RemotingException("The remote invocation failed.");
				}
				IInvocationObject response = (IInvocationObject) Common.getMarshaller().deserialize(marshalledResponse);
				return response.getReturnParam();
			case FIRE_FORGET:
				if(!isVoid){
					throw new RequestException("\"Fire and Forget\" not available for non-void methods!");
				}
				//handler.send(address, marshalledRequest);
				return null;
			case POLL_OBJECT:
				//TODO
			case RESULT_CALLBACK:
				//TODO
				// save callback received from client with according ACT in map
			default:
				throw new RequestException("Unknown Requesttype!");
				
		}
	}
	
	// Fire and Forget 
	public Object invoke(IInvocationObject object, boolean isVoid) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		byte[] marshalledRequest = Common.getMarshaller().serialize(object);
		byte[] marshalledResponse;
		
		IClientRequestHandler handler = Common.getClientRequesthandler();
		
		if(!isVoid){
			throw new RequestException("\"Fire and Forget\" not available for non-void methods!");
		}
		IAOR objectReference = object.getObjectReference();
		if (objectReference.isLocal()) {
			marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
		} else {
			ILocation location = objectReference.getLocation();
			String hostName = location.getHostname();
			String portString = location.getPort();
			int port = Integer.parseInt(portString);
			InetSocketAddress socketAddress = new InetSocketAddress(hostName,port);
			marshalledResponse = handler.send(socketAddress,marshalledRequest);
		}
		if (marshalledResponse.length == 0) {
			throw new RemotingException("The remote invocation failed.");
		}
		IInvocationObject response = (IInvocationObject) Common.getMarshaller().deserialize(marshalledResponse);
		return response.getReturnParam();
	}

	public void returnResult(IACT act, byte[] result)
    {
		ICallback clientCallback = clientCallbacks.get(act);
		clientCallback.resultReturned(act, result);
    }
	
}
