/**
 * 
 */
package evs.interfaces;


/**
 * @author Gerald Scharitzer
 *
 */
public interface IInvoker {
	
	IInvocationObject invoke(IInvocationObject object) throws evs.exception.RemotingException;

	String getId();
	void setId(String id);
}
