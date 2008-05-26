package evs.interfaces;

import evs.core.InvocationObject;
import evs.exception.MiddlewareException;

public interface IInterceptor {
	
	void beforeInvocation(IInvocationObject object) throws MiddlewareException;
}
