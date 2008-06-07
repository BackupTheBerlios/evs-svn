/**
 * 
 */
package evs.core;

import evs.interfaces.IACT;
import evs.interfaces.ICallback;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class Callback implements ICallback {

	public void resultReturned(IACT act, Object result) {
		System.out.println("Callback act=" + act.getTimestamp() +
			" result=" + result);
	}

}
