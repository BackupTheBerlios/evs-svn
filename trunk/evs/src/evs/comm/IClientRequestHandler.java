/**
 * 
 */
package evs.comm;

import java.net.SocketAddress;

import evs.exception.RemotingException;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IClientRequestHandler {

	/**
	 * 
	 * @param address the address of the request handler.
	 * @param message the bytes of the serialized message.
	 * @return the response message, which is always null for void or asynchronous methods.
	 * @throws RemotingException
	 */
	public byte[] send(SocketAddress address, byte[] message) throws RemotingException;
	public byte[] receiveResponse() throws RemotingException;
	
}
