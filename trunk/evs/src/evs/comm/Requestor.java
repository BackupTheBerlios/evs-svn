package evs.comm;

import java.net.SocketAddress;

import evs.exception.RemotingException;
import evs.exception.RequestException;

public class Requestor implements IRequestor {

	private IMarshaller marshaller;
	
	public Object invoke(InvocationObject object, boolean isVoid) throws RemotingException {
		//TODO: obtain reference to request handler
		IClientRequestHandler handler = null;

		//TODO: obtain reference to marshaller
		byte[] marshalledRequest = marshaller.serialize(object);
		
		//TODO: get address of remote object
		SocketAddress address = null;
		
		switch(object.getRequestType()){
			case SYNC:
				handler.send(address, marshalledRequest);
				byte[] marshalledResponse = handler.receiveResponse();
				return marshaller.deserialize(marshalledResponse);
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
