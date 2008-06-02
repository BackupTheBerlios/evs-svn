/**
 * 
 */
package evs.core;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ClientRequestHandler implements IClientRequestHandler {
	
	private Map<SocketAddress,Socket> connections; // TODO synchronize
	
	public ClientRequestHandler() {
		connections = new HashMap<SocketAddress,Socket>();
	}

	public byte[] receiveResponse() throws RemotingException {
		return null; // TODO implement
	}

	public byte[] send(SocketAddress address, byte[] message) throws RemotingException {
		Socket socket = sendRequest(address,message);
		return receiveResponse(socket);
	}
	
	/**
	 * 
	 * @param address the address of the request handler.
	 * @param message the bytes of the serialized message.
	 * @return the socket, which was used to send the message and will contain the response.
	 * @throws RemotingException
	 */
	private Socket sendRequest(SocketAddress address, byte[] message) throws RemotingException {
		Socket socket = connections.get(address);
		if (socket == null) { // create new connection
			socket = new Socket();
			try {
				socket.connect(address);
			} catch (IOException e) {
				throw new RemotingException(e);
			}
			connections.put(address,socket);
		}
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			dataOutputStream.writeInt(message.length);
			dataOutputStream.write(message);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		return socket;
	}
	
	/**
	 * 
	 * @param socket the socket to receive the response from.
	 * @return the response.
	 * @throws RemotingException
	 */
	private byte[] receiveResponse(Socket socket) throws RemotingException {
		InputStream inputStream;
		try {
			inputStream = socket.getInputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		DataInput dataInput = new DataInputStream(inputStream);
		int messageLength;
		try {
			messageLength = dataInput.readInt();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		if (messageLength < 0) {
			throw new RemotingException("The message length was negative.");
		}
		byte[] response = new byte[messageLength];
		try {
			dataInput.readFully(response);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		return response;
	}

}
