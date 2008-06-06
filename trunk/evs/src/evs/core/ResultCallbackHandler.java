/**
 * 
 */
package evs.core;

import java.net.SocketAddress;

import evs.exception.RemotingException;
import evs.interfaces.IACT;
import evs.interfaces.IClientRequestHandler;
import evs.interfaces.IRequestor;
import evs.interfaces.IResultCallbackHandler;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class ResultCallbackHandler implements IResultCallbackHandler {
	
	private IClientRequestHandler clientRequestHandler;
	private SocketAddress socketAddress;
	private byte[] request;
	private IRequestor requestor;
	private IACT act;
	
	public ResultCallbackHandler(SocketAddress socketAddress, byte[] request, IRequestor requestor, IACT act) {
		this.socketAddress = socketAddress;
		this.request = request;
		this.requestor = requestor;
		this.act = act;
		clientRequestHandler = Common.getClientRequesthandler();
	}

	public void run() {
		byte[] response;
		try {
			response = clientRequestHandler.send(socketAddress,request);
		} catch (RemotingException e) {
			e.printStackTrace();
			return;
		}
		requestor.returnResult(act, response);
	}

	public void setClientRequestHandler(IClientRequestHandler clientRequestHandler) {
		this.clientRequestHandler = clientRequestHandler;
	}

	public Thread start() {
		Thread t = new Thread(this,this.getClass().getName());
		t.start();
		return t;
	}

}
