/**
 * 
 */
package evs.interfaces;

import evs.exception.MarshallingException;


/**
 * @author Gerald Scharitzer
 * @author Dirk Wallerstorfer *
 */
public interface IMarshaller {
	
	/**
	 * transforms passed object into byte array
	 * @param object object to be transformed
	 * @return byte array the object was transformed into
	 * @throws MarshallingException when something went wrong with the marshalling process
	 */
	byte[] serialize(IInvocationObject object) throws MarshallingException;
	
	/**
	 * unmarshalling of an byte array to an object
	 * @param bytes byte array to be unmarshalled
	 * @return object that's unmarshalled from the byte array
	 * @throws MarshallingException when something went wrong with the marshalling process
	 */
	IInvocationObject deserialize(byte[] bytes) throws MarshallingException;

}
