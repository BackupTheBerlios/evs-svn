/**
 * 
 */
package evs.comm;

import java.io.UnsupportedEncodingException;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class StringObjectId implements IObjectId {
	
	private String id;
	
	private static final String UTF8 = "UFT-8";

	/* (non-Javadoc)
	 * @see evs.comm.IObjectId#toBytes()
	 */
	public byte[] toBytes() {
		byte[] bytes;
		try {
			bytes = id.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
		return bytes;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IObjectId#fromBytes(byte[])
	 */
	public void fromBytes(byte[] bytes) {
		try {
			id = new String(bytes,UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
