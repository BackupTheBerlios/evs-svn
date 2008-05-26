/**
 * 
 */
package evs.comm.interfaces;

import evs.comm.exception.MarshallingException;


/**
 * @author Gerald Scharitzer
 *
 */
public interface IMarshaller {
	
	byte[] serialize(IInvocationObject object) throws MarshallingException;
	IInvocationObject deserialize(byte[] bytes) throws MarshallingException;

}
