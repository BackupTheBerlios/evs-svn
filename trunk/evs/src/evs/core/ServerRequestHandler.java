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
import java.util.HashMap;
import java.util.Map;

import evs.exception.RemotingException;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IServerRequestHandler;


/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ServerRequestHandler implements IServerRequestHandler, Runnable {
	
	private Map<SocketAddress,Socket> connections;
	private ServerSocket serverSocket;
	private volatile boolean listen = true;
	
	public ServerRequestHandler() {
		connections = new HashMap<SocketAddress,Socket>();
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
	
	public int getPort() {
		return serverSocket.getLocalPort();
	}

	public void run() {
		while (listen) {
			Socket socket;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			SocketAddress address = socket.getRemoteSocketAddress();
			connections.put(address,socket);
			
		}
	}
	
	public void stop() {
		listen = false;
	}

}
