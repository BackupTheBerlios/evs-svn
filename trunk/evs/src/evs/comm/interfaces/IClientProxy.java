/**
 * 
 */
package evs.comm.interfaces;

import evs.comm.exception.NotSupportedException;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IClientProxy {
	
	IAOR getAOR();
	void setAOR(IAOR aor);
	
	void newInstance() throws NotSupportedException;
	void keepAlive() throws NotSupportedException;
	void destroy() throws NotSupportedException;
	 
}
