/**
 * 
 */
package evs.comm;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class AOR implements IAOR {
	
	private ILocation location;
	private IObjectId objectId;

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#getLocation()
	 */
	public ILocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#getObjectId()
	 */
	public IObjectId getObjectId() {
		return objectId;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#setLocation(evs.comm.ILocation)
	 */
	public void setLocation(ILocation location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#setObjectId(evs.comm.IObjectId)
	 */
	public void setObjectId(IObjectId objectId) {
		this.objectId = objectId;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#isLocal()
	 */
	public boolean isLocal() {
		// TODO Auto-generated method stub
		return false;
	}

}
