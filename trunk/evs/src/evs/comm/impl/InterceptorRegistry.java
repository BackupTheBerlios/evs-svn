package evs.comm.impl;

import java.util.ArrayList;
import java.util.List;

import evs.comm.interfaces.IInterceptor;
import evs.comm.interfaces.IInterceptorRegistry;

public class InterceptorRegistry implements IInterceptorRegistry {
	private List<IInterceptor> interceptors;
	
	public InterceptorRegistry(){
		this.interceptors = new ArrayList<IInterceptor>();
	}

	public List<IInterceptor> getInterceptors() {
		return this.interceptors;
	}
	
	public void registerInterceptor(IInterceptor interceptor){
		this.interceptors.add(interceptor);
	}

}
