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

import evs.interfaces.IServerRequestHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ServerRequestHandler implements IServerRequestHandler {
	
	private Socket socket;
	private volatile boolean listen = true;
	
	public ServerRequestHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream inputStream;
		OutputStream outputStream;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		while (listen) {
			int length;
			try {
				length = dataInputStream.readInt();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			byte[] request = new byte[length];
			try {
				dataInputStream.readFully(request);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			byte[] response = invoke(request);
			try {
				dataOutputStream.writeInt(response.length);
				dataOutputStream.write(response);
				dataOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Thread start() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
	public void stop() {
		listen = false;
	}
	
	private byte[] invoke(byte[] request) {
		System.out.println(new String(request)); // TODO remove
		byte[] response = request; // TODO replace with invoker
		return response;
	}

}
