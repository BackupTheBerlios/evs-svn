package evs.comm.interfaces;

import evs.comm.exception.IllegalObjectException;
import evs.comm.exception.NotSupportedException;
import evs.comm.impl.LifecycleStrategy;

public interface ILifecycleManager {
	
	IRemoteObject invocationArrived(IObjectReference ref);
	void invocationDone(IObjectReference ref, IRemoteObject object);

	void register(IObjectReference ref) throws IllegalObjectException;
	void register(IObjectReference ref, LifecycleStrategy strategy) throws IllegalObjectException;
	void unregister(IObjectReference ref);
	
	Integer newInstance(IObjectReference ref) throws NotSupportedException, IllegalObjectException;
	void keepAlive(IObjectReference ref) throws NotSupportedException, IllegalObjectException;
	void destroy(IObjectReference ref) throws NotSupportedException, IllegalObjectException;
	
}
