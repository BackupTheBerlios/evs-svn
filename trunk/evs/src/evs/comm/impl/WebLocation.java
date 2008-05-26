package evs.comm.impl;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import evs.comm.exception.RemotingException;
import evs.comm.interfaces.ILocation;

public class WebLocation implements ILocation{

	private String hostname;
	private String port;
	
	public WebLocation(){}
	
	public WebLocation(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(hostname);
		out.writeUTF(port);
	}
	
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.hostname = in.readUTF();
		this.port = in.readUTF();
	}
}
