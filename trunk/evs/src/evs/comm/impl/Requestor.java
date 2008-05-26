package evs.comm.impl;

import java.net.SocketAddress;

import evs.comm.exception.RemotingException;
import evs.comm.exception.RequestException;
import evs.comm.interfaces.IClientRequestHandler;
import evs.comm.interfaces.IInterceptor;
import evs.comm.interfaces.IInvocationObject;
import evs.comm.interfaces.IRequestor;

public class Requestor implements IRequestor {
	
	public Object invoke(IInvocationObject object, boolean isVoid) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		byte[] marshalledRequest = Common.getMarshaller().serialize(object);
		
		//TODO: get address of remote object
		SocketAddress address = null;
		IClientRequestHandler handler = Common.getClientRequesthandler();
		
		switch(object.getRequestType()){
			case SYNC:
				//TEMPORARY leave out request handler for testing purposes
				//handler.send(address, marshalledRequest);
				//byte[] marshalledResponse = handler.receiveResponse();
				byte[] marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
				InvocationObject response = (InvocationObject) Common.getMarshaller().deserialize(marshalledResponse);
				return response.getReturnParam();
			case FIRE_FORGET:
				if(!isVoid){
					throw new RequestException("\"Fire and Forget\" not available for non-void methods!");
				}
				handler.send(address, marshalledRequest);
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
