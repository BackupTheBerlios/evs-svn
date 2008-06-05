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

import evs.exception.RemotingException;
import evs.interfaces.IInvocationDispatcher;
import evs.interfaces.IMessageHeader;
import evs.interfaces.IServerRequestHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ServerRequestHandler implements IServerRequestHandler {
	
	private Socket socket;
	private IInvocationDispatcher invocationDispatcher;
	
	public ServerRequestHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream inputStream;
		ObjectInputStream objectInputStream;
		IMessageHeader messageHeader = new MessageHeader();
		try {
			inputStream = socket.getInputStream();
			objectInputStream = new ObjectInputStream(inputStream);
			messageHeader.readExternal(objectInputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		int messageLength = messageHeader.getMessageLength();
		byte[] request = new byte[messageLength];
		try {
			objectInputStream.readFully(request);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		byte[] response = invoke(request);
		
		if (messageHeader.getInvocationStyle() == InvocationStyle.FIRE_FORGET) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			return;
		}
		
		messageHeader.setMessageLength(response.length);
		try {
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			messageHeader.writeExternal(objectOutputStream);
			objectOutputStream.write(response);
			objectOutputStream.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
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
	
	public void stop() {}
	
	private byte[] invoke(byte[] request) {
		byte[] response;
		try {
			response = invocationDispatcher.invoke(request);
		} catch (RemotingException e) {
			e.printStackTrace();
			response = new byte[0];
		}
		return response;
	}

}
