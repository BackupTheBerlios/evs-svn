/**
 * 
 */
package evs.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import evs.interfaces.IAOR;
import evs.interfaces.ILocation;
import evs.interfaces.IObjectReference;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public class AOR implements IAOR {
	
	private ILocation location;
	private IObjectReference reference;

	public AOR(){}
	
	public AOR(ILocation location, IObjectReference reference){
		this.location = location;
		this.reference = reference;
	}
	
	/* (non-Javadoc)
	 * @see evs.comm.IAOR#getLocation()
	 */
	public ILocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see evs.comm.IAOR#setLocation(evs.comm.ILocation)
	 */
	public void setLocation(ILocation location) {
		this.location = location;
	}
	
	public IObjectReference getReference() {
		return reference;
	}

	public void setReference(IObjectReference reference) {
		this.reference = reference;
	}
	
	/* (non-Javadoc)
	 * @see evs.comm.IAOR#isLocal()
	 */
	public boolean isLocal() {
		// check if the ILocation.hostname and ILocation.port are
		// the same as where the service from the peer is provided
		if(this.location.getHostname().equals(Common.getLocation().getHostname()) 
		   && this.location.getPort().equals(Common.getLocation().getPort()))
			return true;
		return false;
	}
	
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.location);
		out.writeObject(this.reference);
	}
	
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.location = (ILocation) in.readObject();
		this.reference= (IObjectReference) in.readObject();
	}


}
