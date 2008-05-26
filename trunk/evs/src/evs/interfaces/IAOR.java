/**
 * 
 */
package evs.interfaces;

import java.io.Externalizable;

/**
 * the interface for absolute object references.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IAOR extends Externalizable {
	
	ILocation getLocation();
	void setLocation(ILocation location);
	
	IObjectReference getReference();
	void setReference(IObjectReference reference);
	
	/**
	 * @return true if this reference points to a local object.
	 */
	boolean isLocal();
	
}
