/**
 * 
 */
package evs.interfaces;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerRequestHandler extends Runnable {
	
	void setInvocationDispatcher(IInvocationDispatcher invocationDispatcher);
	Thread start();
	void stop();

}
