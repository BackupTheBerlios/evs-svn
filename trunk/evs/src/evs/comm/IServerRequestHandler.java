/**
 * 
 */
package evs.comm;

import evs.exception.RemotingException;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerRequestHandler {
	
	byte[] receiveRequest() throws RemotingException;
	void sendResponse(byte[] message) throws RemotingException;

}
