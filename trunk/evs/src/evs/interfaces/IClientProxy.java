/**
 * 
 */
package evs.interfaces;

import evs.exception.NotSupportedException;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IClientProxy{
	
	IAOR getAOR();
	void setAOR(IAOR aor);
	
	void newInstance() throws NotSupportedException;
	void keepAlive() throws NotSupportedException;
	void destroy() throws NotSupportedException;
	 
}