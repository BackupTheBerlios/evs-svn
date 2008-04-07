/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IClientRequestHandler {

	public void sendRequest(byte[] message) throws RemotingException;
	public byte[] receiveResponse() throws RemotingException;
	
}
