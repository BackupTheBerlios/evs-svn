/**
 * 
 */
package evs.interfaces;

import java.net.SocketAddress;

import evs.exception.RemotingException;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IClientRequestHandler {

	/**
	 * Send the message and wait for the response.
	 * @param address the address of the request handler.
	 * @param message the bytes of the serialized message.
	 * @return the response message, which is always null for void or asynchronous methods.
	 * @throws RemotingException
	 */
	byte[] send(SocketAddress address, byte[] message) throws RemotingException;
	byte[] receiveResponse() throws RemotingException;
	
}
