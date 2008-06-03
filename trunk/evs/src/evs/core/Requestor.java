package evs.core;

import java.net.InetSocketAddress;

import evs.exception.RemotingException;
import evs.exception.RequestException;
import evs.interfaces.IAOR;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInterceptor;
import evs.interfaces.IInvocationObject;
import evs.interfaces.ILocation;
import evs.interfaces.IRequestor;

public class Requestor implements IRequestor {
	
	public Object invoke(IInvocationObject object, boolean isVoid) throws RemotingException {
		
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
				ILocation location = objectReference.getLocation();
				String hostName = location.getHostname();
				String portString = location.getPort();
				int port = Integer.parseInt(portString);
				InetSocketAddress socketAddress = new InetSocketAddress(hostName,port);
				//TEMPORARY leave out request handler for testing purposes
				//handler.send(socketAddress,marshalledRequest);
				//byte[] marshalledResponse = handler.receiveResponse();
				if (objectReference.isLocal()) {
					marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
				} else {
					// TODO
					// durch RequestHandler-Aufruf ersetzen
					marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
				}
				InvocationObject response = (InvocationObject) Common.getMarshaller().deserialize(marshalledResponse);
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
			default:
				throw new RequestException("Unknown Requesttype!");
				
		}
	}
	
}
