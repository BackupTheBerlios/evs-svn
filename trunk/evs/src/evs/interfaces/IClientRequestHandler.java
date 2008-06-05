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
	 * Send the request and wait to receive the response.
	 * @param address the address of the request handler.
	 * @param request the bytes of the serialized request.
	 * @return the bytes of the serialized response.
	 * @throws RemotingException
	 */
	byte[] send(SocketAddress address, byte[] request) throws RemotingException;
	
	/**
	 * Send the request without receiving a response.
	 * @param address the address of the request handler.
	 * @param request the bytes of the serialized request.
	 * @throws RemotingException
	 */
	void fireAndForget(SocketAddress address, byte[] request) throws RemotingException;
	
}
