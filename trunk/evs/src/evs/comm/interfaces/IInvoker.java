/**
 * 
 */
package evs.comm.interfaces;


/**
 * @author Gerald Scharitzer
 *
 */
public interface IInvoker {
	
	IInvocationObject invoke(IInvocationObject object) throws evs.comm.exception.RemotingException;

	String getId();
	void setId(String id);
}
