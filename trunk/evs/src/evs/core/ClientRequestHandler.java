/**
 * 
 */
package evs.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IRequestor;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ClientRequestHandler implements IClientRequestHandler {
	
	public ClientRequestHandler() {}

	public byte[] receive() throws RemotingException {
		return null; // TODO implement
	}

	public byte[] send(SocketAddress address, byte[] request) throws RemotingException {
		Socket socket = sendRequest(address,request);
		return receiveResponse(socket);
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
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			dataOutputStream.writeInt(request.length);
			dataOutputStream.write(request);
			dataOutputStream.flush();
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
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		int messageLength;
		try {
			messageLength = dataInputStream.readInt();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		if (messageLength < 0) {
			throw new RemotingException("The message length was negative.");
		}
		byte[] response = new byte[messageLength];
		try {
			dataInputStream.readFully(response);
			socket.close();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		return response;
	}

	public byte[] send_callback(SocketAddress address, byte[] request,
            IRequestor requestor) throws RemotingException
    {
	    // TODO Auto-generated method stub
	    return null;
    }

	public void send_fireforget(SocketAddress address, byte[] request)
            throws RemotingException
    {
	    // TODO Auto-generated method stub
	    
    }

}
