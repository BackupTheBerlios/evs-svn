/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IServerRequestHandler {
	
	public byte[] receiveRequest() throws RemotingException;
	public void sendResponse(byte[] message) throws RemotingException;

}
