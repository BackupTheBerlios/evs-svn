package evs.core;

import java.util.HashMap;

import evs.exception.IllegalObjectException;
import evs.interfaces.IInvocationDispatcher;
import evs.interfaces.IInvocationObject;
import evs.interfaces.IInvoker;

public class InvocationDispatcher implements IInvocationDispatcher {

	private HashMap<String, IInvoker> invokers = new HashMap<String, IInvoker>();
	
	public void registerInvoker(String invokerId, IInvoker invoker){
		invokers.put(invokerId, invoker);
	}
	
	public void unregisterInvoker(String invokerId){
		invokers.remove(invokerId);
	}
	
	public synchronized byte[] invoke(byte[] message) throws evs.exception.RemotingException{
		IInvocationObject object = Common.getMarshaller().deserialize(message);

		IInvoker invoker = invokers.get(object.getObjectReference().getReference().getInvokerId());
		if(invoker == null) throw new IllegalObjectException();
		
		object = invoker.invoke(object);
		return Common.getMarshaller().serialize(object);	
	}
	
}
