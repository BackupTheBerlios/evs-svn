/**
 * 
 */
package evs.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IMessageHeader;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ClientRequestHandler implements IClientRequestHandler {
	
	public ClientRequestHandler() {}

	public byte[] send(SocketAddress address, byte[] request) throws RemotingException {
		Socket socket = sendRequest(address,request);
		return receiveResponse(socket);
	}

	public void fireAndForget(SocketAddress address, byte[] request)
		throws RemotingException {
		Socket socket = new Socket();
		try {
			socket.connect(address);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		
		IMessageHeader messageHeader = new MessageHeader();
		messageHeader.setInvocationStyle(InvocationStyle.FIRE_FORGET);
		messageHeader.setMessageLength(request.length);
		
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			messageHeader.writeExternal(objectOutputStream);
			objectOutputStream.write(request);
			objectOutputStream.flush();
			socket.close();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
	}
	
	/**
	 * 
	 * @param address the address of the request handler.
	 * @param request the bytes of the serialized request.
	 * @return the socket, which was used to send the request and will contain the response.
	 * @throws RemotingException
	 */
	private Socket sendRequest(SocketAddress address, byte[] request) throws RemotingException {
		Socket socket = new Socket();
		try {
			socket.connect(address);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		
		IMessageHeader messageHeader = new MessageHeader();
		messageHeader.setInvocationStyle(InvocationStyle.SYNC);
		messageHeader.setMessageLength(request.length);
		
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			messageHeader.writeExternal(objectOutputStream);
			objectOutputStream.write(request);
			objectOutputStream.flush();
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
		
		ObjectInputStream objectInputStream;
		IMessageHeader messageHeader = new MessageHeader();
		try {
			objectInputStream = new ObjectInputStream(inputStream);
			messageHeader.readExternal(objectInputStream);
		} catch (IOException e) {
			throw new RemotingException(e);
		} catch (ClassNotFoundException e) {
			throw new RemotingException(e);
		}
		
		int messageLength = messageHeader.getMessageLength();
		if (messageLength < 0) {
			throw new RemotingException("The message length was negative.");
		}
		
		byte[] response = new byte[messageLength];
		try {
			objectInputStream.readFully(response);
			socket.close();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		
		return response;
	}

}
