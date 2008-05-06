/**
 * 
 */
package evs.comm;

/**
 * the interface for absolute object references.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IAOR {
	
	ILocation getLocation();
	void setLocation(ILocation location);
	
	IObjectId getObjectId();
	void setObjectId(IObjectId objectId);
	
	/**
	 * @return true if this reference points to a local object.
	 */
	boolean isLocal();
	
}
