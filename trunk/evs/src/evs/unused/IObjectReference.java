/**
 * 
 */
package evs.unused;

import java.io.Externalizable;

/**
 * @author Gerald Scharitzer (e0127228 at student dot tuwien dot ac dot at)
 *
 */
public interface IObjectReference extends Externalizable {
	
	IObjectReference fromString(String s);
	String toString();

}
