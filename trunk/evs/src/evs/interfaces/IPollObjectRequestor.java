/**
 * 
 */
package evs.interfaces;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IPollObjectRequestor extends Runnable {
	
	void setClientRequestHandler(IClientRequestHandler clientRequestHandler);
	Thread start();
	void stop();

}
