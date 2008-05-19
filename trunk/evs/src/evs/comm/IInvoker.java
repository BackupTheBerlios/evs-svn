/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IInvoker {
	
	byte[] invoke(byte[] message) throws evs.exception.RemotingException;

}
