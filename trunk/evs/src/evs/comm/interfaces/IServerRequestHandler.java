/**
 * 
 */
package evs.comm.interfaces;

import evs.IObjectId;
import evs.comm.exception.RemotingException;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerRequestHandler {
	
	byte[] receiveRequest() throws RemotingException;
	void sendResponse(byte[] message) throws RemotingException;

}
