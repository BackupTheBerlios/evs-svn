package evs.comm.interfaces;

public interface IInvocationDispatcher {
	
	void registerInvoker(String invokerId, IInvoker invoker);
	
	void unregisterInvoker(String invokerId);
	
	byte[] invoke(byte[] message) throws evs.comm.exception.RemotingException;
}
