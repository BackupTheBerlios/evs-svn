/**
 * 
 */
package evs.interfaces;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IResultCallbackHandler extends Runnable {

	void setClientRequestHandler(IClientRequestHandler clientRequestHandler);
	Thread start();

}
