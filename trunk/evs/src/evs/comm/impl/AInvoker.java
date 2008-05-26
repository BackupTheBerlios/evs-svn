package evs.comm.impl;

import evs.comm.interfaces.IInvoker;

public abstract class AInvoker implements IInvoker {

	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
