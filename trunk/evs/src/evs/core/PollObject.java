/**
 * 
 */
package evs.core;

import evs.interfaces.IPollObject;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class PollObject implements IPollObject {
	
	private Object result;
	private Exception exception;
	private boolean available;
	
	public PollObject() {
		reset();
	}

	public synchronized Object getResult() throws Exception {
		if (exception != null)
			throw exception;
		return result;
	}

	public synchronized boolean isResultAvailable() {
		return available;
	}

	public synchronized void setResult(Object result) {
		this.result = result;
		available = true;
	}
	
	public synchronized void setException(Exception e) {
		exception = e;
		available = true;
	}
	
	public synchronized void reset() {
		result = null;
		exception = null;
		available = false;
	}

}
