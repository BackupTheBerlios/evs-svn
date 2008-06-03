/**
 * 
 */
package evs.interfaces;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerRequestHandler extends Runnable {
	
	Thread start();
	void stop();

}
