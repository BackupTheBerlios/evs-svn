package evs.comm.interfaces;

import evs.comm.exception.MiddlewareException;
import evs.comm.impl.InvocationObject;

public interface IInterceptor {
	
	void beforeInvocation(IInvocationObject object) throws MiddlewareException;
}
