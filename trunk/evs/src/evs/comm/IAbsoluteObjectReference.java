/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IAbsoluteObjectReference {
	
	public ILocation getLocation();
	public void setLocation(ILocation location);
	
	public IObjectId getObjectId();
	public void setObjectId(IObjectId objectId);

}
