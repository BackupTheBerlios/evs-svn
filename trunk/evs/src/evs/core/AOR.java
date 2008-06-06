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
 * AOR - absolute object reference
 * contains hostname, port, invoker ID and remote object ID
 * used to uniquely identify remote object
 * 
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 * @author Dirk Wallerstorfer
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
	
	/**
	 * implementation of Externalizable interface
	 * @param out ObjectOutput to be written to
	 * @throws IOException when ObjectOutput is corrupt
	 */
	public synchronized void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.location);
		out.writeObject(this.reference);
	}
	
	/**
	 * implementation of Externalizable interface
	 * @param in ObjectInput to be read from
	 * @throws IOException when ObjectInput is corrupt
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public synchronized void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
		this.location = (ILocation) in.readObject();
		this.reference= (IObjectReference) in.readObject();
	}


}
