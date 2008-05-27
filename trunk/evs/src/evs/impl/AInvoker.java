package evs.core;

import evs.interfaces.IInvoker;

public abstract class AInvoker implements IInvoker {

	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
