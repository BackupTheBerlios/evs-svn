/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class AbsoluteObjectReference implements IAbsoluteObjectReference {
	
	private ILocation location;
	private IObjectId objectId;

	/* (non-Javadoc)
	 * @see evs.comm.IAbsoluteObjectReference#getLocation()
	 */
	public ILocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAbsoluteObjectReference#getObjectId()
	 */
	public IObjectId getObjectId() {
		return objectId;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAbsoluteObjectReference#setLocation(evs.comm.ILocation)
	 */
	public void setLocation(ILocation location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAbsoluteObjectReference#setObjectId(evs.comm.IObjectId)
	 */
	public void setObjectId(IObjectId objectId) {
		this.objectId = objectId;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAbsoluteObjectReference#isLocal()
	 */
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

}
