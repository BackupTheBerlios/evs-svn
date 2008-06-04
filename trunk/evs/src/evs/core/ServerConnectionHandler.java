/**
 * 
 */
package evs.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import evs.exception.RemotingException;
import evs.interfaces.IInvocationDispatcher;
import evs.interfaces.IServerConnectionHandler;
import evs.interfaces.IServerRequestHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ServerConnectionHandler implements IServerConnectionHandler {
	
	private Map<SocketAddress,Socket> connections;
	private ServerSocket serverSocket;
	private IInvocationDispatcher invocationDispatcher;
	private volatile boolean listen = true;
	
	public ServerConnectionHandler() {
		connections = new HashMap<SocketAddress,Socket>();
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
			IServerRequestHandler serverRequestHandler = new ServerRequestHandler(socket);
			serverRequestHandler.setInvocationDispatcher(invocationDispatcher);
			serverRequestHandler.start();
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setInvocationDispatcher(IInvocationDispatcher invocationDispatcher) {
		this.invocationDispatcher = invocationDispatcher;
	}

	public Thread start() {
		Thread t = new Thread(this,this.getClass().getName());
		t.start();
		return t;
	}
	
	public void stop() {
		listen = false;
	}

}
