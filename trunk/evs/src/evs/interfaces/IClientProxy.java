/**
 * 
 */
package evs.interfaces;

import evs.exception.NotSupportedException;

/**
 * @author Gerald Scharitzer
 * @author Dirk Wallerstorfer
 *
 */
public interface IClientProxy{
	
	/**
	 * return absolute object reference
	 * @return aor to be returned
	 */
	IAOR getAOR();

	/**
	 * set absolute object reference
	 * @param aor aor to be set
	 */
	void setAOR(IAOR aor);

	/**
	 * when using LifecycleStrategy.CLIENT* the remote object
	 * must be initiated before use
	 * @param act asynchronous completion token
	 */
	void newInstance(IACT act) throws NotSupportedException;

	/**
	 * when a LifecycleStrategy with passivation is used, one
	 * can send a keepAlive to the remote object, so the object
	 * won't be passivated while it's still in use or needed
	 * later on
	 */
	void keepAlive() throws NotSupportedException;

	/**
	 * when a LifecycleStrategy.CLIENT* is used, the newly created
	 * remote object can be explicitly destroyed using this function
	 */
	void destroy() throws NotSupportedException;
	 
}
