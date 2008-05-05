/**
 * 
 */
package evs.comm;

/**
 * identifies an instance with the local context.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IObjectId {
	
	byte[] toBytes();
	void fromBytes(byte[] bytes);
	
}
