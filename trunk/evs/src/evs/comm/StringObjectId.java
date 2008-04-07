/**
 * 
 */
package evs.comm;

import java.nio.charset.Charset;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class StringObjectId implements IObjectId {
	
	private String id;
	
	private static final Charset UTF8 = Charset.forName("UFT-8");

	/* (non-Javadoc)
	 * @see evs.comm.IObjectId#toBytes()
	 */
	@Override
	public byte[] toBytes() {
		return id.getBytes(UTF8);
	}

	/* (non-Javadoc)
	 * @see evs.comm.IObjectId#fromBytes(byte[])
	 */
	@Override
	public void fromBytes(byte[] bytes) {
		id = new String(bytes,UTF8);
	}
	
	
}
