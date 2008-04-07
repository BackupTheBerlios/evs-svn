/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IObjectId {
	
	public byte[] toBytes();
	public void fromBytes(byte[] bytes);
	
}
