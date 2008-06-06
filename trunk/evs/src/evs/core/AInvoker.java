package evs.core;

import evs.interfaces.IInvoker;

/**
 * Abstract implementation of an invoker
 * 
 * @author Dirk Wallerstorfer
 *
 */
public abstract class AInvoker implements IInvoker {

	protected String id;

	/**
	 * get the Invoker ID
	 * @return Invoker ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * set the Invoker ID
	 * @param id Invoker ID to be set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
