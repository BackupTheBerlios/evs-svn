/**
 * 
 */
package evs.comm;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class MinimalRequestHandler implements IClientRequestHandler,
		IServerRequestHandler {
	
	private Socket socket;
	private ServerSocket serverSocket;
	private InputStream socketInputStream;
	private OutputStream socketOutputStream;
	
	public MinimalRequestHandler() {
		socket = new Socket();
	}

	/* (non-Javadoc)
	 * @see evs.comm.IClientRequestHandler#receiveResponse()
	 */
	public byte[] receiveResponse() throws RemotingException {
		DataInput dataInput = new DataInputStream(socketInputStream);
		int messageLength;
		try {
			messageLength = dataInput.readInt();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		if (messageLength < 0) {
			throw new RemotingException("The message length was negative.");
		}
		byte[] buffer = new byte[messageLength];
		try {
			dataInput.readFully(buffer);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		return buffer;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IClientRequestHandler#sendRequest(byte[])
	 */
	public void sendRequest(byte[] message) throws RemotingException {
		try {
			socketOutputStream.write(message);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
	}

	/* (non-Javadoc)
	 * @see evs.comm.IServerRequestHandler#receiveRequest()
	 */
	public byte[] receiveRequest() throws RemotingException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IServerRequestHandler#sendResponse(byte[])
	 */
	public void sendResponse(byte[] message) throws RemotingException {
		// TODO Auto-generated method stub

	}
	
	public void bind(SocketAddress localAddress) throws RemotingException {
		try {
			socket.bind(localAddress);
			serverSocket = new ServerSocket();
			serverSocket.bind(localAddress);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
	}
	
	public void connect(SocketAddress remoteAddress) throws RemotingException {
		try {
			socket.connect(remoteAddress);
			socketInputStream = socket.getInputStream();
			socketOutputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
	}
	
	public int getPort() {
		return socket.getLocalPort();
	}
}
