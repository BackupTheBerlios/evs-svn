/**
 * 
 */
package evs.interfaces;

import java.io.Serializable;

/**
 * @author Gerald Scharitzer
 *
 */
public interface IRemoteObject extends Serializable {
	
	String getId();
	void setId(String id);
	
	Long getExpiration();
	void setExpiration(Long expiration);
	
}
