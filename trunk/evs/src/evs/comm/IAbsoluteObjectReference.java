/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IAbsoluteObjectReference {
	
	ILocation getLocation();
	void setLocation(ILocation location);
	
	IObjectId getObjectId();
	void setObjectId(IObjectId objectId);
	
	/**
	 * @return true if this reference points to a local object.
	 */
	boolean isLocal();
	
}
