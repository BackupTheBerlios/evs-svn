/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IMarshaller {
	
	public byte[] serialize(Object object);
	public Object deserialize(byte[] bytes);

}
