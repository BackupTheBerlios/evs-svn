package evs.core;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import dummy.exception.DummyException;

import evs.exception.MarshallingException;
import evs.exception.NotSupportedException;
import evs.exception.RemotingException;
import evs.interfaces.IACT;
import evs.interfaces.IAOR;
import evs.interfaces.ICallback;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IInterceptor;
import evs.interfaces.IInvocationObject;
import evs.interfaces.ILocation;
import evs.interfaces.IMarshaller;
import evs.interfaces.IPollObject;
import evs.interfaces.IPollObjectRequestor;
import evs.interfaces.IRequestor;
import evs.interfaces.IResultCallbackHandler;

public class Requestor implements IRequestor {
	
	private Map<IACT, ICallback> clientCallbacks = new HashMap<IACT, ICallback>();
	
	// Callback
	public Object invoke(IInvocationObject object, boolean isVoid, ICallback callback, IACT act, InvocationStyle style) throws RemotingException {
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.beforeInvocation(object);
		}
		
		IMarshaller marshaller = Common.getMarshaller();
		byte[] marshalledRequest = marshaller.serialize(object);
		
		IAOR objectReference = object.getObjectReference();

		if(objectReference.isLocal())
			return Common.getInvocationDispatcher().invoke(marshalledRequest);
		
		InetSocketAddress socketAddress = getInetSocketAddress(objectReference);
		IClientRequestHandler clientRequestHandler = Common.getClientRequesthandler();

		Object returnObject = null;
		switch(style) {
			case SYNC:
				returnObject = marshaller.deserialize(clientRequestHandler.send(socketAddress, marshalledRequest));
				break;
			case POLL_OBJECT:
				if (act != null)
					throw new DummyException("No ACT is needed for the POLL_OBJECT invocation style.");
				IPollObject pollObject = new PollObject();
				IPollObjectRequestor pollObjectRequestor =
					new PollObjectRequestor(object, marshaller, pollObject);
				pollObjectRequestor.start();
				returnObject = pollObject;
				break;
			case FIRE_FORGET:
				clientRequestHandler.fireAndForget(socketAddress,marshalledRequest);
				break;
			case RESULT_CALLBACK:
				if (act == null)
					throw new DummyException("An ACT is required for the RESULT_CALLBACK invocation style.");
				
				clientCallbacks.put(act, callback);
				IResultCallbackHandler resultCallbackHandler =
					new ResultCallbackHandler(socketAddress, marshalledRequest, this, act);
				resultCallbackHandler.start();
				break;
			default:
				throw new NotSupportedException("The invocation style " + style + " is not supported.");
		}
		
		//handle interceptors
		for(IInterceptor interceptor: Common.getClientInterceptors().getInterceptors()){
			interceptor.afterInvocation(object);
		}
		
		return returnObject;
		
	}
	
	public synchronized void returnResult(IACT act, byte[] result) {
		ICallback clientCallback = clientCallbacks.get(act);
		
		try
        {
	        clientCallback.resultReturned(act, (Common.getMarshaller().deserialize(result)).getReturnParam());
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
