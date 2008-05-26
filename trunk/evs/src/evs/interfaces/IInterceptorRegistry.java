package evs.interfaces;

import java.util.List;

public interface IInterceptorRegistry {

	List<IInterceptor> getInterceptors();
	
	void registerInterceptor(IInterceptor interceptor);
	
}
