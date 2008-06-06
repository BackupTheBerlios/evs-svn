package evs.interfaces;

import java.io.Externalizable;
import java.sql.Timestamp;

public interface IACT extends Externalizable {
	
	/**
	 * return the set Timestamp
	 * @return the set Timestamp
	 */
	public Timestamp getTimestamp();
	
	/**
	 * set the Timestamp
	 * @param timestamp Timestamp to be set
	 */
	public void setTimestamp(Timestamp timestamp);

}
