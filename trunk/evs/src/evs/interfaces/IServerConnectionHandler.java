/**
 * 
 */
package evs.interfaces;

import java.net.SocketAddress;

import evs.exception.RemotingException;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IServerConnectionHandler extends Runnable {
	
	void bind(SocketAddress localAddress) throws RemotingException;
	int getPort();
	Thread start();
	void stop();

}
