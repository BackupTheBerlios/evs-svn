package evs.core;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import evs.exception.MarshallingException;
import evs.exception.RemotingException;
import evs.interfaces.IACT;
import evs.interfaces.IAOR;
import evs.interfaces.ICallback;
import evs.interfaces.IInterceptor;
import evs.interfaces.IInvocationObject;
import evs.interfaces.ILocation;
import evs.interfaces.IMarshaller;
import evs.interfaces.IPollObject;
import evs.interfaces.IPollObjectRequestor;
import evs.interfaces.IRequestor;
import evsbsp.exception.DummyException;

public class Requestor implements IRequestor {
	
	private Map<IACT, ICallback> clientCallbacks = new HashMap<IACT, ICallback>();
	
	// Callback
	public Object invoke(IInvocationObject object, boolean isVoid, ICallback callback, IACT act, InvocationStyle style) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		byte[] marshalledRequest = Common.getMarshaller().serialize(object);
		
		IAOR objectReference = object.getObjectReference();

		if(objectReference.isLocal())
			return Common.getInvocationDispatcher().invoke(marshalledRequest);
		
		InetSocketAddress socketAddress = getInetSocketAddress(objectReference);

		switch(style)
		{
			case SYNC:
				return Common.getMarshaller().deserialize(Common.getClientRequesthandler().send(socketAddress, marshalledRequest));
			case POLL_OBJECT:
				if(act != null)
					throw new DummyException("No ACT is needed with InvocationStyle POLL_OBJECT");
				
				IPollObject pollObject = new PollObject();
				IMarshaller marshaller = Common.getMarshaller();
				IPollObjectRequestor pollObjectRequestor = new PollObjectRequestor(object, marshaller, pollObject);
				pollObjectRequestor.start();
				return pollObject;
			case FIRE_FORGET:
				Common.getClientRequesthandler().send_fireforget(socketAddress, marshalledRequest);
				return null;
			case RESULT_CALLBACK:
			default:
				if(act == null)
					throw new DummyException("An ACT is needed for RESULT_CALLBACK InvocationStyle.");

				clientCallbacks.put(act, callback);				
				Common.getClientRequesthandler().send_callback(socketAddress, marshalledRequest, this);
				return null;
		}
	}
	
	public synchronized void returnResult(IACT act, byte[] result) {
		ICallback clientCallback = clientCallbacks.get(act);
		
		try
        {
	        clientCallback.resultReturned(act, Common.getMarshaller().deserialize(result));
        }
        catch(MarshallingException e)
        {
	        e.printStackTrace();
        }
    }
	
	private InetSocketAddress getInetSocketAddress(IAOR aor) {
		ILocation location = aor.getLocation();
		String hostName = location.getHostname();
		String portString = location.getPort();
		int port = Integer.parseInt(portString);
		InetSocketAddress socketAddress = new InetSocketAddress(hostName,port);
		return socketAddress;
	}
	
}
