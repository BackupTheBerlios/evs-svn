/**
 * 
 */
package evs.comm;

import evs.exception.MarshallingException;


/**
 * @author Gerald Scharitzer
 *
 */
public interface IMarshaller {
	
	public byte[] serialize(InvocationObject object) throws MarshallingException;
	public InvocationObject deserialize(byte[] bytes) throws MarshallingException;

}
