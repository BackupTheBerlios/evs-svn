package evs.core;

import evs.exception.RemotingException;
import evs.exception.RequestException;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInterceptor;
import evs.interfaces.IInvocationObject;
import evs.interfaces.IRequestor;

public class Requestor implements IRequestor {
	
	public Object invoke(IInvocationObject object, boolean isVoid) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		byte[] marshalledRequest = Common.getMarshaller().serialize(object);
		
		IClientRequestHandler handler = Common.getClientRequesthandler();
		
		switch(object.getRequestType()){
			case SYNC:
				//TEMPORARY leave out request handler for testing purposes
				//handler.send(object.getObjectReference().getLocation(), marshalledRequest);
				//byte[] marshalledResponse = handler.receiveResponse();
				byte[] marshalledResponse = Common.getInvocationDispatcher().invoke(marshalledRequest);
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
