/**
 * 
 */
package evs.interfaces;

import evs.exception.RemotingException;
import evs.unused.IObjectId;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerRequestHandler {
	
	byte[] receiveRequest() throws RemotingException;
	void sendResponse(byte[] message) throws RemotingException;

}
