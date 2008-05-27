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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IServerRequestHandler;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class BasicRequestHandler implements IClientRequestHandler,
		IServerRequestHandler, Runnable {
	
	private Socket socket;
	private ServerSocket serverSocket;
	private InputStream socketInputStream;
	private OutputStream socketOutputStream;
	private volatile boolean listen = true;
	
	public BasicRequestHandler() {
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

	public byte[] send(SocketAddress address, byte[] message) throws RemotingException {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			dataOutputStream.writeInt(message.length);
		} catch (IOException e) {
			throw new RemotingException(e);
		}
		return null;
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
		return serverSocket.getLocalPort();
	}

	public void run() {
		while (listen) {
			Socket socket;
			InputStream inputStream;
			try {
				socket = serverSocket.accept();
				inputStream = socket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			DataInputStream dataInputStream;
			dataInputStream = new DataInputStream(inputStream);
			
			int length;
			try {
				length = dataInputStream.readInt();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			System.out.println("length = " + length);
		}
	}
	
	public void stop() {
		listen = false;
	}
	

	

}
