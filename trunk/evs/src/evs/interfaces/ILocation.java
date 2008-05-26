/**
 * 
 */
package evs.interfaces;

import java.io.Externalizable;

/**
 * identifies a physical location.
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 */
public interface ILocation extends Externalizable{

	void setHostname(String hostname);
	String getHostname();
	
	void setPort(String port);
	String getPort();
}
